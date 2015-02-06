package org.jomics.isoelectricpoint.sequence.isoelectricpoint.svmpI;

import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.IsoelectricPointMethod;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.bjellpI.BjellpI;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.cofactorAdjacentpI.CofactorAdjacentpI;
import org.jomics.isoelectricpoint.utils.svm.NormalizeVector;
import org.jomics.isoelectricpoint.utils.svm.SvmClassifier;
import org.jomics.isoelectricpoint.utils.svm.SvmKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.*;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * This method computed the isoelectric point using support vector machines and molecular descriptors
 * such as Bjell method and Zimmerman () descriptor from AAindex. The method was developed by Perez-Riverol
 * and cols:
 *   - Yasset Perez-Riverol, Enrique Audain, Aleli Millan, Yassel Ramos, Aniel Sanchez,Juan Antonio Vizcaíno,
 *     Rui Wang, Markus Müller, Yoan J. Machado, Lazaro H. Betancourt, Luis J. González, Gabriel Padrón,
 *     Vladimir Besada,J. Proteomics, 2012, 2269–2274.
 *
 * User: yperez
 * Date: 8/5/13
 *
 * */
public class SvmpI implements IsoelectricPointMethod {

    private static volatile SvmpI instance = null;

    private static BjellpI bjellpI         = BjellpI.getInstance(BjellpI.CALIBRATED_PKMETHOD,4.5);
    private static IsoelectricPointMethod cargille = CofactorAdjacentpI.getInstance();

    private static double rmseMin          = 14.0;

    private static int countModels         = 5;

    public static double SIGMA = 0.5;       //The Exponent to use (i.e RBFKernel)

    public static double C_FACTOR = 1.0;    //The complexity constant C.

    private static int cache               = 100000;    //parameter defined

    private static String[] options        = {"-L 1e-03", "-W 1", "-P 1e-12", "-T 1e-03" , "-V"};

    private static Map<AminoAcid, Double> ZimmermanpK = new HashMap<AminoAcid, Double>(){{
        put(AminoAcid.D, Double.valueOf(2.77D)); put(AminoAcid.E, Double.valueOf(3.22D)); put(AminoAcid.K, Double.valueOf(9.74D));
        put(AminoAcid.R, Double.valueOf(10.76D)); put(AminoAcid.H, Double.valueOf(7.59D));put(AminoAcid.C, Double.valueOf(5.05D));
        put(AminoAcid.Y, Double.valueOf(5.66D));
    }};

    private static SvmClassifier currentClassifier = null;

    private double bjellNorm;

    private double zimmermanNorm;

    public static SvmpI getInstance(Map<List<AminoAcid>, Double> peptides, boolean defaultSvm) throws Exception {
        if (instance == null) {
            synchronized (SvmpI .class){
                if (instance == null) {
                    instance = new SvmpI(peptides, defaultSvm);
                }
            }
        }
        return instance;
    }

    private SvmpI(Map<List<AminoAcid>, Double> peptides, boolean defaultSvm) throws Exception {

        if(!defaultSvm && peptides != null){
            List<SvmPiDescriptor> svmPiDescriptors = getPeptideDescriptorList(peptides);
            BuildPIDataSet pIDataSet = new BuildPIDataSet(svmPiDescriptors, peptides.size());
            currentClassifier = getBestClassifier(pIDataSet, true);
        }else{
            URL url =SvmpI.class.getClassLoader().getResource("svmDataDefault.csv");
            if (url == null) {
                throw new IllegalStateException("no file for input found!");
            }
            File inputFile = new File(url.toURI());
            peptides = new HashMap<List<AminoAcid>, Double>();
            Scanner scanner = new Scanner(inputFile);
            //first use a Scanner to get each line
            while (scanner.hasNextLine() ){
                String stringLine = scanner.nextLine();
                getSequenceMapFromString(stringLine,peptides);
            }
            List<SvmPiDescriptor> svmPiDescriptors = getPeptideDescriptorList(peptides);
            BuildPIDataSet pIDataSet = new BuildPIDataSet(svmPiDescriptors, peptides.size());
            currentClassifier = getBestClassifier(pIDataSet, true);
        }
    }

    /*
     * This method compute the isoelectric point for a given sequence. It use the SVM current machine in memory.
     * The method constructs a weka instance of a peptide and then compute the pI.
     *
     * @param sequence The sequence of the peptide to compute the isoelectric point.
     * @return isoelectric point
     * */

    @Override
    public Double computePI(List<AminoAcid> sequence) throws Exception {

        double bjellPI = getBjell(sequence)/ bjellNorm;
        double zimmermanPI = getZimmermanDescriptor(sequence) / zimmermanNorm;
        Instance instance = BuildPIDataSet.getEvaluateInstance(bjellPI,zimmermanPI,1.0);
        /*To compute the isoelectric point no matter the previous class of the peptide, then
        * we define 1.0 by default, then the class will be rewrote by the svmClassifier*/
        return currentClassifier.classifyInstance(instance);
    }

    @Override
    public Double computeChargeAtpH(List<AminoAcid> sequence, Double pH) {
        return null;
    }

    @Override
    public Map<Double, Double> computeStepMethodPI(List<AminoAcid> sequence) {
        return null;
    }

    /*
     * This method construct five classifiers using the training dataset and storage
     * the classifiers using Serialization file. Each classifier is evaluated using a test
     * dataset and the classifier with a better standard deviation is selected as the best
     * classifier.
     *
     * @param data The Dataset for classifier training.
     * @return SvmClassifier Best Classifier
     */

    public SvmClassifier getBestClassifier (BuildPIDataSet piData, boolean filterOutliers) throws Exception {

        SvmClassifier svm;
        BuildPIDataSet pICleanDataSet = piData;

        if (filterOutliers)
        // pICleanDataSet = FilterOutLiers.removeOutliersByFraction(piData);

        for(int i = 0; i < countModels; i++){

            pICleanDataSet.randomizeDataSet();
            /*Randomize the dataset each time that a new Classifier is created and Study*/

            Instances train = pICleanDataSet.getTrainDataset(5,0);
            Instances test = pICleanDataSet.getTestDataset(5,0);
            /* Divide the dataset in two subsets: training and test datasets*/

            test.setClassIndex(test.numAttributes() - 1);
            train.setClassIndex(train.numAttributes() - 1);
            /* Setting class attribute for test and training datasets*/

            RBFKernel rbf = SvmKernel.getInstanceRBFKernel(train, cache, SIGMA);
            /* Build a new Radial Kernel*/


            svm = new SvmClassifier(train, options, rbf);
            /* Create a new SvmClassifier using the kernel, options and the train dataset*/

            //set C parameter
            svm.setComplexityFactor(C_FACTOR);

            double svmRmsd = svm.evaluateRMSE(test);
            /*Compute the rmsd for the svm and take the */
            System.out.println(svmRmsd);


            if( svmRmsd < rmseMin){
                rmseMin = svmRmsd;
                currentClassifier = svm;
            }
        }
        return currentClassifier;
    }


    /*
     * Zimmerman Descriptor compute using only the ionizable groups and measure as
     * the average of the contribution of all AA.
     *
     * @param sequence Peptide or Protein. The list of AA in the sequence.
     */
    private Double getZimmermanDescriptor(List<AminoAcid> sequence){

        int count = 2;
        double zimmerman = bjellpI.getCTermSelected(sequence.get(sequence.size()-1))+bjellpI.getNTermSelected(sequence.get(0));
        /* Start from 2 taking to account the C and N terminus contribution*/

        for(AminoAcid aminoAcid: sequence){
            zimmerman =+ ((ZimmermanpK.containsKey(aminoAcid))? ZimmermanpK.get(aminoAcid):0);
            count = (ZimmermanpK.containsKey(aminoAcid))?count++:count;
        }
        zimmerman = zimmerman/count;
        return zimmerman;
    }

    /*
     * Bjell descriptor using the CALIBRATED pk set. The bjell descriptor is computed using the class
     * BjellpI.
     *
     * @param sequence Peptide or Protein. The list of AA in the sequence.
     */
    private Double getBjell(List<AminoAcid> sequence){
        try {
            return bjellpI.computePI(sequence);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /*
    * Get a Peptide Descriptor List from a list of sequences. A SvmPiDescriptor is an
    * Object that contains the properties of a peptide to be included in the SVM model.
    *
    * */
    private List<SvmPiDescriptor> getPeptideDescriptorList(Map<List<AminoAcid>,Double> peptides){
        double[] zimmermanpIs = new double[peptides.size()];
        double[] bjellpIs     = new double[peptides.size()];
        double[] exppIs       = new double[peptides.size()];
        List<SvmPiDescriptor> svmPiDescriptors = new ArrayList<SvmPiDescriptor>();
        int count = 0;
        for(List<AminoAcid> sequence: peptides.keySet()){
            zimmermanpIs[count] = getZimmermanDescriptor(sequence);
            bjellpIs[count]     = getBjell(sequence);
            exppIs[count]       = peptides.get(sequence);
            count++;
        }
        zimmermanNorm = NormalizeVector.getMaxElement(zimmermanpIs);
        zimmermanpIs  = NormalizeVector.normalizeMax(zimmermanpIs, zimmermanNorm);
        bjellNorm     = NormalizeVector.getMaxElement(bjellpIs);
        bjellpIs     = NormalizeVector.normalizeMax(bjellpIs,bjellNorm);
        for(int i = 0; i < peptides.size(); i++) {
            SvmPiDescriptor svmPiDescriptor = new SvmPiDescriptor(bjellpIs[i],zimmermanpIs[i],exppIs[i]);
            svmPiDescriptors.add(svmPiDescriptor);
        }
        return svmPiDescriptors;
    }

    private static Map<List<AminoAcid>,Double> getSequenceMapFromString(String line, Map<List<AminoAcid>,Double> mapSequences){
        line.trim();
        String[] attr = line.split(",");
        List<AminoAcid> sequence = new ArrayList<AminoAcid>();
        for(Character character: attr[0].toCharArray()) sequence.add(AminoAcid.getAminoAcid(character));
        mapSequences.put(sequence,Double.parseDouble(attr[1]));
        return mapSequences;
    }





}
