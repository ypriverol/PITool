package uk.ac.ebi.pride.sequence.isoelectricpoint.svmpI;

/**
 * Created with IntelliJ IDEA.
 * User: yperez, enriquea
 * Date: 1/08/13
 * Time: 22:22
 *
 */

import uk.ac.ebi.pride.utils.svm.HeaderSvmConstants;
import weka.core.*;
import weka.core.Debug.Random;

import java.util.List;


public class BuildPIDataSet {

    private Instances dataset;

    private static FastVector atts;

    //Constructor
    public BuildPIDataSet(String namedata, int size){
        atts = getAtts();
        /*Set the FVector attributes of the BuildPIDataSet*/
        this.dataset = new Instances(namedata, atts, size);
    }

    public BuildPIDataSet(List<SvmPiDescriptor> svmPiDescriptors, int size){
        atts = getAtts();
        /*Set the FVector attributes of the BuildPIDataSet*/

        this.dataset = new Instances(HeaderSvmConstants.DEFAULT_DATA_NAME, atts, size);

        for(SvmPiDescriptor svmPiDescriptor : svmPiDescriptors){
            add(svmPiDescriptor.getBjellpI(), svmPiDescriptor.getZimmermanpI(), svmPiDescriptor.getExppI());
        }
    }

    public static SvmPiDescriptor getPeptideDescriptor(double bjellpI, double zimmermanpI, double exppI){
        return new SvmPiDescriptor(bjellpI, zimmermanpI,exppI);
    }

    /*
     * This method adds a new intance to the dataset, using the descriptors from
     * zimmerman, bjell and the experimental isoelectric point.
     * @param bjellpI
     */
    public void add(double bjellpI, double zimmermanpI, double exppI){
        dataset.add(getInstance(bjellpI,zimmermanpI,exppI));
        /* Adding a new instance to the intances of the dataset*/
    }

    /*
     * This method returns a weka instance using the bjell, zimmerman descriptor
     * and the experimental isoelectric point.
     *
     * @param bjellpI isoelectric point of Bjell
     * @param zimmermanpI isoelectric point from Zimmerman.
     * @param exppI experimental isoelectric point
     *
     * @return weka instance
     */
    public static Instance getInstance(double bjellpI, double zimmermanpI, double exppI){
        double[] values = new double[atts.size()];
        values[0] = bjellpI;
        values[1] = zimmermanpI;
        values[2] = exppI;
        return new Instance(1.0, values);   //Changed DenseInstance to Instance
    }

    //This method return traninig dataset with croos validation approach
    public Instances getTrainDataset (int folds, int fold){
        return dataset.trainCV(folds, fold);

    }
    //This method return testing dataset with croos validation approach
    public Instances getTestDataset (int folds, int fold){
          return dataset.testCV(folds, fold);

    }

    //This method randomize the full datset using Random instance
    public void randomizeDataSet(){
        Random ran = new Random();  //argument could be System.currentTimeMillis()
        this.dataset.randomize(ran);
    }

    public FastVector getAtts(){
        FastVector localAtts = new FastVector(3);
        localAtts.addElement(new Attribute(HeaderSvmConstants.BJELLPI_INSTANCE_HEADER));    //first attribute
        localAtts.addElement(new Attribute(HeaderSvmConstants.ZIMMERPI_INSTANCE_HEADER));   //second attribute
        localAtts.addElement(new Attribute(HeaderSvmConstants.CLASS_INSTANCE_HEADER));      //class attribute (pI experimental)
        return localAtts;
    }

    public static Instance getEvaluateInstance(double bjellpI, double zimmermanpI, double exppI){
        Instance evalInstance = getInstance(bjellpI,zimmermanpI,exppI);
        Instances instances = new Instances(HeaderSvmConstants.DEFAULT_DATA_NAME, atts, atts.size());
        instances.setClassIndex(atts.size()-1);
        evalInstance.setDataset(instances);
        return evalInstance;
    }

    public Instances getDataset(){
        return dataset;
    }


}