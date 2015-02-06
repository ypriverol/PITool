package uk.ac.ebi.pride.utils;

import uk.ac.ebi.pride.sequence.isoelectricpoint.svmpI.BuildPIDataSet;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 9/16/13
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class FilterOutLiers {

 /*   public static BuildPIDataSet removeOutliersByFraction (BuildPIDataSet piDataset){
        piDataset.getDataset().setClassIndex(piDataset.getDataset().numAttributes() - 1);
        System.out.println(piDataset.getDataset().numInstances());
        BuildPIDataSet cleanDataSet = piDataset;
        List<Instance> instances = removeOutliers(splitDatabyFraction(piDataset),0);
        cleanDataSet.getDataset().delete();
        cleanDataSet.getDataset().addAll(instances);
        System.out.println(cleanDataSet.getDataset().numInstances());
        return cleanDataSet;
    }

    private static Map<Double, Instances> splitDatabyFraction(BuildPIDataSet piDataSet){
        Map<Double, List<Instance>> fractions = new HashMap<Double, List<Instance>>();
        Map<Double, Instances> instanceFraction = new HashMap<Double, Instances>();
        for (int i = 0; i < piDataSet.getDataset().size(); i++){
            List<Instance> instances = null;
            if(fractions.containsKey(piDataSet.getDataset().get(i).classValue())){
                instances = fractions.get(piDataSet.getDataset().get(i).classValue());
            }else{
               instances = new ArrayList<Instance>();
            }
            instances.add(piDataSet.getDataset().get(i));
            fractions.put(piDataSet.getDataset().get(i).classValue(),instances);
        }
        for(Double fraction: fractions.keySet()){
            Instances instances = new Instances(fraction.toString(),piDataSet.getAtts(),fractions.get(fraction).size());
            instances.setClassIndex(piDataSet.getAtts().size()-1);
            instances.addAll(fractions.get(fraction));
            instanceFraction.put(fraction,instances);
        }
        return instanceFraction;
    }

    //this method generate some pI fractions, remove specific outliers in fraction and return
    //full dataset whit outliers removed
    private static List<Instance> removeOutliers (Map<Double, Instances> fractions, int attributeIndex){
        List<Instance> resultInstances =  new ArrayList<Instance>();
        for(Double fraction: fractions.keySet()){
            Instances instances = fractions.get(fraction);
            double SD = getSdClassAttribute(instances, attributeIndex);
            for (int i = 0; i < instances.numInstances(); i++){
                if(Math.abs(instances.get(i).value(attributeIndex) - instances.meanOrMode(instances.attribute(attributeIndex))) >= 2*SD){  //selection criteria
                    instances.delete(i);
                }else{
                    resultInstances.add(instances.get(i));
                }
            }
        }
        return resultInstances;
    }

    *//**
     * This method compute SD for a selected Attribute
     * @param instances
     * @param attributeIndex
     * @return
     *//*
    private static double getSdClassAttribute (Instances instances, int attributeIndex){
            double var = instances.variance(instances.attribute(attributeIndex));//getting variance for attribute with index 1
            return Math.sqrt(var);
     }*/
}
