package org.jomics.isoelectricpoint.utils;

import weka.core.Utils;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Enrique
 * Date: 22/01/15
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */

//This class model a electrophoretical fraction. It is a container
// of basic information such as: experimental and theoretical
// Isoelectric point values and percent outliers.

public class ElectrophoreticFraction {

    //fields

    private static ArrayList<Double> experimental_pi_values = new ArrayList<Double>();

    private static ArrayList<Double> theoretical_pi_values = new ArrayList<Double>();

    private double min_pi_value;

    private double max_pi_value;

    private int count_elements;

    private double mean_pi_experimental;

    private double mean_pi_theoretical;

    private double SD_PI_TEO;

    private double percent_outliers;



    //constructor
    public ElectrophoreticFraction(ArrayList<Double> pi_exp_list, ArrayList<Double> pi_teo_list, double min_pI_fraction, double max_pI_fraction){

            try {
                if(!(pi_exp_list.size() == pi_teo_list.size()) && (pi_exp_list.isEmpty()|| pi_teo_list.isEmpty()));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        this.setPiFractionValues(pi_exp_list, pi_teo_list,  min_pI_fraction, max_pI_fraction);
        this.max_pi_value = max_pI_fraction;
        this.min_pi_value = min_pI_fraction;
        this.count_elements = experimental_pi_values.size();
        this.mean_pi_experimental = Statistical.getMeanValue(experimental_pi_values);
        this.mean_pi_theoretical = Statistical.getMeanValue(theoretical_pi_values);
        this.SD_PI_TEO = Statistical.getStandardDeviationValue(theoretical_pi_values);
        this.percent_outliers = computeOutliersPercent(theoretical_pi_values, mean_pi_theoretical, SD_PI_TEO);
    }

    //methods

    private void setPiFractionValues (ArrayList<Double> pI_experimental, ArrayList<Double> pI_theoretical, double min_pI_fraction, double max_pI_fraction){
                  ArrayList<Double> piFractionExperimentalValues = new ArrayList<Double>();
                  ArrayList<Double> piFractionTheoreticalValues = new ArrayList<Double>();
                  for(int index = 0; index < pI_experimental.size(); index++){
                     if (pI_experimental.get(index).doubleValue() > min_pI_fraction && pI_experimental.get(index).doubleValue() <= max_pI_fraction){
                        piFractionExperimentalValues.add(pI_experimental.get(index).doubleValue());
                        piFractionTheoreticalValues.add(pI_theoretical.get(index).doubleValue());
                     }

                  }
                experimental_pi_values = piFractionExperimentalValues;
                theoretical_pi_values  = piFractionTheoreticalValues;
    }

    public double getPiExpFractionMean () {
        return this.mean_pi_experimental;
    }

    public double getPiTeoFractionMean () {
        return this.mean_pi_theoretical;
    }

    public int getCountElements (){
        return this.count_elements;
    }

    public double getOutliersPercent(){
        return  this.percent_outliers;
    }

    public double getStandardDev(){
        return this.SD_PI_TEO;
    }


    //outliers detection following any criteria
    private double computeOutliersPercent (ArrayList<Double> pi_teo, double pi_mean_teo, double SD){
        int outliers = 0; //count of outliers
        for (Double pi : pi_teo){
            if(Math.abs(pi_mean_teo - pi) >= 2*SD){  //selection criteria
               outliers++;
            }
        }
        if(!(pi_teo.size()==0)) {
            //Double outlier_percent = (outliers/pi_teo.size())*100.0;
            return outliers;
        }
        else {
            return 0.0;
        }
    }

}
