package uk.ac.ebi.pride.utils;

import weka.core.Utils;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Enrique
 * Date: 26/01/15
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class Statistical {

    Statistical(){
    }

    public static double getMeanValue(ArrayList<Double> array_list){
        double [] primitive_array;
        primitive_array = convertArrayListToPrimitiveArray(array_list);
        return Utils.mean(primitive_array);
    }

    public static double getMaxValue(ArrayList<Double> array_list){
        double [] primitive_array;
        primitive_array = convertArrayListToPrimitiveArray(array_list);
        return primitive_array[Utils.maxIndex(primitive_array)];
    }

    public static double getMinValue(ArrayList<Double> array_list){
        double [] primitive_array;
        primitive_array = convertArrayListToPrimitiveArray(array_list);
        return primitive_array[Utils.minIndex(primitive_array)];
    }

    public static double getCorrelation(ArrayList<Double> array_list1, ArrayList<Double> array_list2){
        double [] primitive_array1;
        double [] primitive_array2;
        primitive_array1 = convertArrayListToPrimitiveArray(array_list1);
        primitive_array2 = convertArrayListToPrimitiveArray(array_list2);
        return Utils.correlation(primitive_array1, primitive_array2, getNumberOfElements(array_list1));
    }

    public static double getVarianceValue(ArrayList<Double> array_list){
        double [] primitive_array;
        primitive_array = convertArrayListToPrimitiveArray(array_list);
        return Utils.variance(primitive_array);
    }

    public static double getStandardDeviationValue(ArrayList<Double> array_list){
        //double [] primitive_array;
        //primitive_array = convertArrayListToPrimitiveArray(array_list);
        return Math.sqrt(getVarianceValue(array_list));
    }

    public static double getStandardDeviationValue(double [] array_double){
        return Math.sqrt(Utils.variance(array_double));
    }

    public static int getNumberOfElements(ArrayList<Double> array_list){
        return array_list.size();
    }

    public static double [] convertArrayListToPrimitiveArray(ArrayList<Double> array_list){
        double  [] primitive_array = new double [array_list.size()];
        int i = 0;
        for(double doubleValue:array_list){
            primitive_array [i] = doubleValue;
            i++;
        }
        return primitive_array;
    }
}
