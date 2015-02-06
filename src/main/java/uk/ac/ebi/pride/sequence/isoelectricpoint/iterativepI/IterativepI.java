package uk.ac.ebi.pride.sequence.isoelectricpoint.iterativepI;

import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import uk.ac.ebi.pride.sequence.isoelectricpoint.IsoelectricPointMethod;
import uk.ac.ebi.pride.sequence.isoelectricpoint.bjellpI.BjellpI;

import java.util.*;


/**
 * The isoelectric point is developed using the Henderson-Hasselbach and different pk Sets
 * depending of the protocol settings:
 *   - SILLERO:
 *   - EMBOSS:
 *   - SOLOMON:
 *   - RODWELL:
 *   - PATRICKIOS:
 *   - RICHARD:
 *   - LEHNINGER:
 *   - GRIMSLEY:
 *   - TOSELAND
 *   - THURLKILL
 *
 * @author yperez
 */
public class IterativepI implements IsoelectricPointMethod {

    public static String SILLERO_PKMETHOD    = "SILLERO";

    public static String EMBOSS_PKMETHOD     = "EMBOSS";

    public static String SOLOMON_PKMETHOD    = "SOLOMON";

    public static String RODEWELL_PKMETHOD   = "RODEWELL";

    public static String PATRICKIOS_PKMETHOD = "PATRICKIOS";

    public static String RICHARD_PKMETHOD    = "RICHARD";

    public static String LEHNINGER_PKMETHOD  = "LEHNINGER";

    public static String GRIMSLEY_PKMETHOD  = "GRIMSLEY";

    public static String TOSELAND_PKMETHOD  = "TOSELAND";

    public static String THURLKILL_PKMETHOD  = "THURLKILL";

    private static String defaultPK = EMBOSS_PKMETHOD;     // select the pk set to compute the pI

    private static Double CTerm_Pk = Double.valueOf(3.60D);

    private static Double NTerm_Pk = Double.valueOf(8.60D);

    private double pHforCharge = 8.0;         // pH for an specific value of charge.

    private double step = 3.5;                // step of the iterative method.
    

    private static Map<AminoAcid, Double> pKIterative  = new HashMap<AminoAcid, Double>();  // The hash map of ionizable groups.

    private static volatile IterativepI instance = null;


    public static IterativepI getInstance(String pkMethod, Double pHforCharge){
        if (instance == null) {
            synchronized (BjellpI.class){
                if (instance == null) {
                    instance = new IterativepI(pkMethod,pHforCharge);
                }
            }
        }
        return instance;
    }


    private IterativepI(String pkSet, Double PHforCharge) {
        setPHforCharge(PHforCharge);
        setPKIterative(pkSet);
    }

    /*  Isoelectric point prediction Iterative */
    @Override
    public Double computePI(List<AminoAcid> sequence){
        return calculate(sequence);
    }

    @Override
    public Double computeChargeAtpH(List<AminoAcid> sequence, Double pH){

        Map<List<AminoAcid>, Double> charges = new HashMap<List<AminoAcid>, Double>();

        List<AminoAcid> AAAcid  = new ArrayList<AminoAcid>(){ // Ionizable acid group
            {
                add(AminoAcid.K); add(AminoAcid.R);add(AminoAcid.H);
            }
        };
        List<AminoAcid> AABasid = new ArrayList<AminoAcid>(){ // Ionizable basic group
            {
                add(AminoAcid.D); add(AminoAcid.E); add(AminoAcid.C); add(AminoAcid.Y);
            }
        };


        Double charge = 0.0;
        charge += pcharge(pH, NTerm_Pk);
        charge -= pcharge(CTerm_Pk,pH);

        for (int i = 0; i < sequence.size(); i++){
            AminoAcid aa = sequence.get(i);
            if((AAAcid.contains(aa)) /*&& (aaContribution(aa, null, null) == 1)*/){
                charge += pcharge(pH, pKIterative.get(aa));
            }else if((AABasid.contains(aa)) /*&& (aaContribution(aa, null, null) == 1)*/){
                charge -= pcharge(pKIterative.get(aa),pH);
            }
        }
        return charge;
    }

    @Override
    public Map<Double,Double> computeStepMethodPI(List<AminoAcid> sequence){

        double charge;
        double pH  = 0.0;         //Starting point pI = 0.0 - to generate pH graph
        this.step = 0.05;
        pH  = 0.0;         //Starting point pI = 0.0 - to generate pH graph

        int i = 0;
        Map<Double, Double> newCoor = new TreeMap<Double, Double>();
        do{
            charge = chargeAtpH(sequence, pH);
            pH += this.step;
            newCoor.put(new Double(pH),new Double(charge));
            i++;
        }while(pH < 14.0);
        return newCoor;
    }


    public void setPHforCharge(double pHforCharge) {
        this.pHforCharge = pHforCharge;
    }

    /* Select the pk set to compute the isoelectric point for SequenceAA.
     * Rodewell, Sillero, Solomon, EMBOSS, Patrickios.
     */

    public void setPKIterative(String pkSet) {
        pKIterative.clear();
        defaultPK = pkSet;
        if(defaultPK == RODEWELL_PKMETHOD){
            CTerm_Pk = Double.valueOf(3.10D);
            NTerm_Pk = Double.valueOf(8.0D);
            pKIterative.put(AminoAcid.D, Double.valueOf(3.86D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.250D));
            pKIterative.put(AminoAcid.K, Double.valueOf(11.50D));
            pKIterative.put(AminoAcid.R, Double.valueOf(11.5D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.0D));
            pKIterative.put(AminoAcid.C, Double.valueOf(8.33D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(10.07D));
        }else if (defaultPK == SILLERO_PKMETHOD){
            CTerm_Pk = Double.valueOf(3.20D);
            NTerm_Pk = Double.valueOf(8.20D);
            pKIterative.put(AminoAcid.D, Double.valueOf(4.0D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.50D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.40D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.0D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.4D));
            pKIterative.put(AminoAcid.C, Double.valueOf(9.0D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(10.0D));
        }else if(defaultPK == SOLOMON_PKMETHOD){
            CTerm_Pk = Double.valueOf(2.40D);
            NTerm_Pk = Double.valueOf(9.60D);
            pKIterative.put(AminoAcid.D, Double.valueOf(3.9D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.30D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.50D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.5D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.0D));
            pKIterative.put(AminoAcid.C, Double.valueOf(8.3D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(10.1D));
        }else if(defaultPK == PATRICKIOS_PKMETHOD){
            CTerm_Pk = Double.valueOf(2.40D);
            NTerm_Pk = Double.valueOf(9.60D);
            pKIterative.put(AminoAcid.D, Double.valueOf(4.2D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.20D));
            pKIterative.put(AminoAcid.K, Double.valueOf(11.20D));
            pKIterative.put(AminoAcid.R, Double.valueOf(11.2D));
            pKIterative.put(AminoAcid.H, Double.valueOf(0.0D));
            pKIterative.put(AminoAcid.C, Double.valueOf(0.0D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(0.0D));
        }else if(defaultPK == RICHARD_PKMETHOD){
            CTerm_Pk = Double.valueOf(3.20D);
            NTerm_Pk = Double.valueOf(8.20D);
            pKIterative.put(AminoAcid.D, Double.valueOf(4.0D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.50D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.40D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.00D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.4D));
            pKIterative.put(AminoAcid.C, Double.valueOf(9.0D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(10.0D));
        }else if(defaultPK == LEHNINGER_PKMETHOD){
            CTerm_Pk = Double.valueOf(2.34D);
            NTerm_Pk = Double.valueOf(9.69D);
            pKIterative.put(AminoAcid.D, Double.valueOf(3.86D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.25D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.50D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.4D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.0D));
            pKIterative.put(AminoAcid.C, Double.valueOf(8.33D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(10.0D));
        }else if(defaultPK == GRIMSLEY_PKMETHOD){
            CTerm_Pk = Double.valueOf(3.3D);
            NTerm_Pk = Double.valueOf(7.7D);
            pKIterative.put(AminoAcid.D, Double.valueOf(3.5D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.2D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.50D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.0D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.6D));
            pKIterative.put(AminoAcid.C, Double.valueOf(6.8D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(10.3D));
        }else if(defaultPK == TOSELAND_PKMETHOD){
            CTerm_Pk = Double.valueOf(3.19D);
            NTerm_Pk = Double.valueOf(8.71D);
            pKIterative.put(AminoAcid.D, Double.valueOf(3.6D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.29D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.45D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.0D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.33D));
            pKIterative.put(AminoAcid.C, Double.valueOf(6.87D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(9.61D));
        }else if(defaultPK == THURLKILL_PKMETHOD){
            CTerm_Pk = Double.valueOf(3.67D);
            NTerm_Pk = Double.valueOf(8.00D);
            pKIterative.put(AminoAcid.D, Double.valueOf(3.67D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.25D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.40D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.0D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.54D));
            pKIterative.put(AminoAcid.C, Double.valueOf(8.55D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(9.84D));
        }else{
            CTerm_Pk = Double.valueOf(3.60D);
            NTerm_Pk = Double.valueOf(8.60D);
            pKIterative.put(AminoAcid.D, Double.valueOf(3.90D));
            pKIterative.put(AminoAcid.E, Double.valueOf(4.10D));
            pKIterative.put(AminoAcid.K, Double.valueOf(10.80D));
            pKIterative.put(AminoAcid.R, Double.valueOf(12.5D));
            pKIterative.put(AminoAcid.H, Double.valueOf(6.5D));
            pKIterative.put(AminoAcid.C, Double.valueOf(8.5D));
            pKIterative.put(AminoAcid.Y, Double.valueOf(10.1D));
        }
    }

    /**
     * This function evaluate the contribution of the amino acid modification to
     * the isoelectric point, the function take the value of an specific AA
     * and compute the contribution to the isoelectric point with modifications.
     *
     * @return
     */
    

    private double pcharge(double pH, double pk) {
        double val = Math.pow(10, pH - pk);
        val = (double) 1/(1 + val);
        return val;
    }


    
    public double chargeAtpH(List<AminoAcid> sequence, Double pH){

        List<AminoAcid> AAAcid  = new ArrayList<AminoAcid>(){ // Ionizable acid group
            {
                add(AminoAcid.K); add(AminoAcid.R);add(AminoAcid.H);
            }
        };
        List<AminoAcid> AABasid = new ArrayList<AminoAcid>(){ // Ionizable basic group
            {
                add(AminoAcid.D); add(AminoAcid.E); add(AminoAcid.C); add(AminoAcid.Y);
            }
        };

        double charge = 0.0;

        charge += pcharge(pH, NTerm_Pk);
        charge -= pcharge(CTerm_Pk,pH);

        for (int i = 0; i < sequence.size(); i++){
            AminoAcid aa = sequence.get(i);
            if((AAAcid.contains(aa))){
                charge += pcharge(pH, pKIterative.get(aa));
            }else if((AABasid.contains(aa))){
                charge -= pcharge(pKIterative.get(aa),pH);
            }
        }
        return charge;
    }

    private double calculate(List<AminoAcid> sequence){

        double charge = 0.0;
        double error;
        double pH  = 6.5;         //Starting point pI = 6.5 - theoretically it should be 7, but
                                  //average protein pI is 6.5 so we increase the probability.
        double lastCharge = 0;
        double gamma      = 0.00001;
        this.step = 3.5;

        do {
            charge = chargeAtpH(sequence, pH);
            if( charge > 0){
                pH += this.step;
            }else{
                pH -=  this.step;
            }
            this.step = this.step/2;
            error= Math.abs(charge-lastCharge);
            lastCharge = charge;
        }while(error > gamma);

        return pH;
    }
}
