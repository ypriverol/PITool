package org.jomics.isoelectricpoint.sequence.isoelectricpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.bjellpI.BjellpI;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.svmpI.SvmpI;
import weka.experiment.PairedStats;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/15/13
 * Time: 12:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class SvmpITest {

    SvmpI calculator = null;
    BjellpI calculatorBjel = null;
    private HashMap<List<AminoAcid>, Double> mapSequences;


    @Test
    public void testComputePIGroup() throws Exception {

        /*List<AminoAcid> sequence = new ArrayList<AminoAcid>();
        String temp = "EYQLNDSASYYLNDLDR";
        for(Character character: temp.toCharArray()) sequence.add(AminoAcid.getAminoAcid(character));

        List<List<AminoAcid>> sequences = new ArrayList<List<AminoAcid>>();
        sequences.add(sequence);*/

        double[] svmpIs   = new double[mapSequences.size()];
        double[] bjellpIs = new double[mapSequences.size()];
        double[] exppI    = new double[mapSequences.size()];
        int i = 0;
        for(List<AminoAcid> sequence: mapSequences.keySet()){
            Double svmPI = calculator.computePI(sequence);
            Double bjellPI = calculatorBjel.computePI(sequence);
            svmpIs[i] = svmPI;
            bjellpIs[i] = bjellPI;
            exppI[i]    = mapSequences.get(sequence);
            i++;
        }
        PairedStats bjellStats = new PairedStats(1.0);
        bjellStats.add(bjellpIs,exppI);
        bjellStats.calculateDerived();
        double stDBjell = bjellStats.differencesStats.stdDev;
        double correlationBjel = bjellStats.correlation;

        PairedStats svmStats = new PairedStats(1.0);
        svmStats.add(svmpIs,exppI);
        svmStats.calculateDerived();
        double sTDSVM = svmStats.differencesStats.stdDev;
        double correlationSVM = svmStats.correlation;



        assertTrue("Standard deviation should be less for SVM ", sTDSVM < stDBjell);
        assertTrue("Correlation should be better for SVM ", correlationSVM > correlationBjel);


    }

    @Before
    public void setUp() throws Exception {
        URL url = SvmpITest.class.getClassLoader().getResource("svmDataDefault.csv");
        if (url == null) {
            throw new IllegalStateException("no file for input found!");
        }
        File inputFile = new File(url.toURI());
        Scanner scanner = new Scanner(inputFile);
        mapSequences = new HashMap<List<AminoAcid>, Double>();
        //first use a Scanner to get each line
        while ( scanner.hasNextLine() ){
            String stringLine = scanner.nextLine();
            stringLine.trim();
            String[] attr = stringLine.split(",");
            List<AminoAcid> sequence = new ArrayList<AminoAcid>();
            for(Character character: attr[0].toCharArray()) sequence.add(AminoAcid.getAminoAcid(character));
            mapSequences.put(sequence,Double.parseDouble(attr[1]));
        }
        calculator = SvmpI.getInstance(mapSequences, false);
        calculatorBjel = BjellpI.getInstance(BjellpI.CALIBRATED_PKMETHOD,3.4);
    }

    //@Test
    public void testSetDefaultSVM() throws Exception {
        SvmpI calculatorDefault = SvmpI.getInstance(null,true);

        double[] svmpIs   = new double[mapSequences.size()];
        double[] bjellpIs = new double[mapSequences.size()];
        double[] exppI    = new double[mapSequences.size()];
        int i = 0;
        for(List<AminoAcid> sequence: mapSequences.keySet()){
            Double svmPI = calculatorDefault.computePI(sequence);
            Double bjellPI = calculatorBjel.computePI(sequence);
            svmpIs[i] = svmPI;
            bjellpIs[i] = bjellPI;
            exppI[i]    = mapSequences.get(sequence);
            i++;

        }
        PairedStats bjellStats = new PairedStats(1.0);
        bjellStats.add(bjellpIs,exppI);
        bjellStats.calculateDerived();
        double stDBjell = bjellStats.differencesStats.stdDev;
        double correlationBjel = bjellStats.correlation;

        PairedStats svmStats = new PairedStats(1.0);
        svmStats.add(svmpIs,exppI);
        svmStats.calculateDerived();
        double sTDSVM = svmStats.differencesStats.stdDev;
        double correlationSVM = svmStats.correlation;



        assertTrue("Standard deviation should be less for SVM ", sTDSVM < stDBjell);
        assertTrue("Correlation should be better for SVM ", correlationSVM > correlationBjel);
    }

    @After
    public void tearDown() throws Exception {
    }
}
