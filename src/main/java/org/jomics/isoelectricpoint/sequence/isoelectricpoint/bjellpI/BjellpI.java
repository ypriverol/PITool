package org.jomics.isoelectricpoint.sequence.isoelectricpoint.bjellpI;

import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.IsoelectricPointMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Bjell Method compute the isoelectric point using the pk values of the AA and the position
 * of those AA in the sequence. If the AA is in the N- or C- in the sequence the pk value is different
 * of the pk when the AA is in any other position in the sequence. For different sets can be used in
 * this method:
 *   BJELL      -  Bjellqvist B, Hughes GJ, Pasquali C, Paquet N, Ravier F, Sanchez JC. Electrophoresis 1993;14:1023–31.
 *   SKOOG      -
 *   EXPASY     -
 *   CALIBRATED -  Gauci S, van Breukelen B, Lemeer SM, Krijgsveld J, Heck A J. Proteomics 2008;8:4898–906.
 *
 * @author yperez
 */
public class BjellpI implements IsoelectricPointMethod {


    public static String BJELL_PKMETHOD      = "BJELL";

    public static String SKOOG_PKMETHOD      = "SKOOG";

    public static String EXPASY_PKMETHOD     = "EXPASY";

    public static String CALIBRATED_PKMETHOD = "CALIBRATED";

    private static String defaultPkMethod = CALIBRATED_PKMETHOD;     // use by default calibrated.

    private static Map<AminoAcid,Double>  Cterm_Pk     = new HashMap<AminoAcid, Double>(); // pk at CTerm position Bjell
    private static Map<AminoAcid,Double>  Nterm_Pk     = new HashMap<AminoAcid, Double>(); // pk at the NTerm position
    private static Map<AminoAcid, Double> sideGroup_Pk = new HashMap<AminoAcid, Double>(); // in oder position

    private double FoRmU = 0.0D;

    private double pH = -1.0D;

    private static volatile BjellpI instance = null;

    private BjellpI(String pkMethod, double pHforCharge) {
        setPkMethod(pkMethod);
        initMap();
    }

    public static BjellpI getInstance(String pkMethod, Double pHforCharge){
        if (instance == null) {
            synchronized (BjellpI .class){
                if (instance == null) {
                    instance = new BjellpI(pkMethod,pHforCharge);
                }
            }
        }
        return instance;
    }

    private static void initMap(){
        if(defaultPkMethod == BJELL_PKMETHOD){
            Cterm_Pk.clear();
            Nterm_Pk.clear();
            sideGroup_Pk.clear();
            Cterm_Pk.put(AminoAcid.A, Double.valueOf(2.35D));
            Cterm_Pk.put(AminoAcid.R, Double.valueOf(2.17D));
            Cterm_Pk.put(AminoAcid.N, Double.valueOf(2.02D));
            Cterm_Pk.put(AminoAcid.D, Double.valueOf(2.09D));
            Cterm_Pk.put(AminoAcid.C, Double.valueOf(1.71D));
            Cterm_Pk.put(AminoAcid.E, Double.valueOf(2.19D));
            Cterm_Pk.put(AminoAcid.Q, Double.valueOf(2.17D));
            Cterm_Pk.put(AminoAcid.G, Double.valueOf(2.34D));
            Cterm_Pk.put(AminoAcid.H, Double.valueOf(1.82D));
            Cterm_Pk.put(AminoAcid.I, Double.valueOf(2.36D));
            Cterm_Pk.put(AminoAcid.L, Double.valueOf(2.36D));
            Cterm_Pk.put(AminoAcid.K, Double.valueOf(2.18D));
            Cterm_Pk.put(AminoAcid.M, Double.valueOf(2.28D));
            Cterm_Pk.put(AminoAcid.F, Double.valueOf(1.83D));
            Cterm_Pk.put(AminoAcid.P, Double.valueOf(1.99D));
            Cterm_Pk.put(AminoAcid.S, Double.valueOf(2.21D));
            Cterm_Pk.put(AminoAcid.T, Double.valueOf(2.63D));
            Cterm_Pk.put(AminoAcid.W, Double.valueOf(2.38D));
            Cterm_Pk.put(AminoAcid.Y, Double.valueOf(2.2D));
            Cterm_Pk.put(AminoAcid.V, Double.valueOf(2.32D));

            Nterm_Pk.put(AminoAcid.A, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.R, Double.valueOf(6.76D));
            Nterm_Pk.put(AminoAcid.N, Double.valueOf(7.22D));
            Nterm_Pk.put(AminoAcid.D, Double.valueOf(7.7D));
            Nterm_Pk.put(AminoAcid.C, Double.valueOf(8.119999999999999D));
            Nterm_Pk.put(AminoAcid.E, Double.valueOf(7.19D));
            Nterm_Pk.put(AminoAcid.Q, Double.valueOf(6.73D));
            Nterm_Pk.put(AminoAcid.G, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.H, Double.valueOf(7.18D));
            Nterm_Pk.put(AminoAcid.I, Double.valueOf(7.48D));
            Nterm_Pk.put(AminoAcid.L, Double.valueOf(7.46D));
            Nterm_Pk.put(AminoAcid.K, Double.valueOf(6.67D));
            Nterm_Pk.put(AminoAcid.M, Double.valueOf(6.98D));
            Nterm_Pk.put(AminoAcid.F, Double.valueOf(6.96D));
            Nterm_Pk.put(AminoAcid.P, Double.valueOf(8.359999999999999D));
            Nterm_Pk.put(AminoAcid.S, Double.valueOf(6.86D));
            Nterm_Pk.put(AminoAcid.T, Double.valueOf(7.02D));
            Nterm_Pk.put(AminoAcid.W, Double.valueOf(7.11D));
            Nterm_Pk.put(AminoAcid.Y, Double.valueOf(6.83D));
            Nterm_Pk.put(AminoAcid.V, Double.valueOf(7.44D));

            sideGroup_Pk.put(AminoAcid.R, Double.valueOf(-12.5D));
            sideGroup_Pk.put(AminoAcid.D, Double.valueOf(4.07D));
            sideGroup_Pk.put(AminoAcid.C, Double.valueOf(8.279999999999999D));
            sideGroup_Pk.put(AminoAcid.E, Double.valueOf(4.45D));
            sideGroup_Pk.put(AminoAcid.H, Double.valueOf(-6.08D));
            sideGroup_Pk.put(AminoAcid.K, Double.valueOf(-9.800000000000001D));
            sideGroup_Pk.put(AminoAcid.Y, Double.valueOf(9.84D));

        }else if(defaultPkMethod == SKOOG_PKMETHOD){
            Cterm_Pk.put(AminoAcid.A, Double.valueOf(2.35D));
            Cterm_Pk.put(AminoAcid.R, Double.valueOf(2.17D));
            Cterm_Pk.put(AminoAcid.N, Double.valueOf(2.02D));
            Cterm_Pk.put(AminoAcid.D, Double.valueOf(2.09D));
            Cterm_Pk.put(AminoAcid.C, Double.valueOf(1.71D));
            Cterm_Pk.put(AminoAcid.E, Double.valueOf(2.19D));
            Cterm_Pk.put(AminoAcid.Q, Double.valueOf(2.17D));
            Cterm_Pk.put(AminoAcid.G, Double.valueOf(2.34D));
            Cterm_Pk.put(AminoAcid.H, Double.valueOf(1.82D));
            Cterm_Pk.put(AminoAcid.I, Double.valueOf(2.36D));
            Cterm_Pk.put(AminoAcid.L, Double.valueOf(2.36D));
            Cterm_Pk.put(AminoAcid.K, Double.valueOf(2.18D));
            Cterm_Pk.put(AminoAcid.M, Double.valueOf(2.28D));
            Cterm_Pk.put(AminoAcid.F, Double.valueOf(1.83D));
            Cterm_Pk.put(AminoAcid.P, Double.valueOf(1.99D));
            Cterm_Pk.put(AminoAcid.S, Double.valueOf(2.21D));
            Cterm_Pk.put(AminoAcid.T, Double.valueOf(2.63D));
            Cterm_Pk.put(AminoAcid.W, Double.valueOf(2.38D));
            Cterm_Pk.put(AminoAcid.Y, Double.valueOf(2.2D));
            Cterm_Pk.put(AminoAcid.V, Double.valueOf(2.32D));

            Nterm_Pk.put(AminoAcid.A, Double.valueOf(9.69D));
            Nterm_Pk.put(AminoAcid.R, Double.valueOf(9.039999999999999D));
            Nterm_Pk.put(AminoAcid.N, Double.valueOf(8.800000000000001D));
            Nterm_Pk.put(AminoAcid.D, Double.valueOf(9.82D));
            Nterm_Pk.put(AminoAcid.C, Double.valueOf(10.779999999999999D));
            Nterm_Pk.put(AminoAcid.E, Double.valueOf(9.76D));
            Nterm_Pk.put(AminoAcid.Q, Double.valueOf(9.130000000000001D));
            Nterm_Pk.put(AminoAcid.G, Double.valueOf(9.6D));
            Nterm_Pk.put(AminoAcid.H, Double.valueOf(9.17D));
            Nterm_Pk.put(AminoAcid.I, Double.valueOf(9.68D));
            Nterm_Pk.put(AminoAcid.L, Double.valueOf(9.6D));
            Nterm_Pk.put(AminoAcid.K, Double.valueOf(8.949999999999999D));
            Nterm_Pk.put(AminoAcid.M, Double.valueOf(9.210000000000001D));
            Nterm_Pk.put(AminoAcid.F, Double.valueOf(9.130000000000001D));
            Nterm_Pk.put(AminoAcid.P, Double.valueOf(10.6D));
            Nterm_Pk.put(AminoAcid.S, Double.valueOf(9.15D));
            Nterm_Pk.put(AminoAcid.T, Double.valueOf(10.43D));
            Nterm_Pk.put(AminoAcid.W, Double.valueOf(9.390000000000001D));
            Nterm_Pk.put(AminoAcid.Y, Double.valueOf(9.109999999999999D));
            Nterm_Pk.put(AminoAcid.V, Double.valueOf(9.619999999999999D));

            sideGroup_Pk.put(AminoAcid.R, Double.valueOf(-12.48D));
            sideGroup_Pk.put(AminoAcid.D, Double.valueOf(3.86D));
            sideGroup_Pk.put(AminoAcid.C, Double.valueOf(8.33D));
            sideGroup_Pk.put(AminoAcid.E, Double.valueOf(4.25D));
            sideGroup_Pk.put(AminoAcid.H, Double.valueOf(-6));//tenia integer
            sideGroup_Pk.put(AminoAcid.K, Double.valueOf(-10.529999999999999D));
            sideGroup_Pk.put(AminoAcid.Y, Double.valueOf(10.07D));

        } else if( defaultPkMethod == EXPASY_PKMETHOD){

            Cterm_Pk.put(AminoAcid.A, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.R, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.N, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.D, Double.valueOf(4.55D));
            Cterm_Pk.put(AminoAcid.C, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.E, Double.valueOf(4.75D));
            Cterm_Pk.put(AminoAcid.Q, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.G, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.H, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.I, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.L, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.K, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.M, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.F, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.P, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.S, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.T, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.W, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.Y, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.V, Double.valueOf(3.55D));

            Nterm_Pk.put(AminoAcid.A, Double.valueOf(7.59D));
            Nterm_Pk.put(AminoAcid.R, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.N, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.D, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.C, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.E, Double.valueOf(7.7D));
            Nterm_Pk.put(AminoAcid.Q, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.G, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.H, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.I, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.L, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.K, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.M, Double.valueOf(7.0D));
            Nterm_Pk.put(AminoAcid.F, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.P, Double.valueOf(8.359999999999999D));
            Nterm_Pk.put(AminoAcid.S, Double.valueOf(6.93D));
            Nterm_Pk.put(AminoAcid.T, Double.valueOf(6.82D));
            Nterm_Pk.put(AminoAcid.W, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.Y, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.V, Double.valueOf(7.44D));

            sideGroup_Pk.put(AminoAcid.R, Double.valueOf(-12.0D));
            sideGroup_Pk.put(AminoAcid.D, Double.valueOf(4.05D));
            sideGroup_Pk.put(AminoAcid.C, Double.valueOf(9.0D));
            sideGroup_Pk.put(AminoAcid.E, Double.valueOf(4.45D));
            sideGroup_Pk.put(AminoAcid.H, Double.valueOf(-5.98D));
            sideGroup_Pk.put(AminoAcid.K, Double.valueOf(-10.0D));
            sideGroup_Pk.put(AminoAcid.Y, Double.valueOf(10.0D));

        }else{

            Cterm_Pk.put(AminoAcid.A, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.R, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.N, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.D, Double.valueOf(4.55D));
            Cterm_Pk.put(AminoAcid.C, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.E, Double.valueOf(4.75D));
            Cterm_Pk.put(AminoAcid.Q, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.G, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.H, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.I, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.L, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.K, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.M, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.F, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.P, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.S, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.T, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.W, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.Y, Double.valueOf(3.55D));
            Cterm_Pk.put(AminoAcid.V, Double.valueOf(3.55D));

            Nterm_Pk.put(AminoAcid.A, Double.valueOf(7.59D));
            Nterm_Pk.put(AminoAcid.R, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.N, Double.valueOf(6.7D));
            Nterm_Pk.put(AminoAcid.D, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.C, Double.valueOf(6.5D));
            Nterm_Pk.put(AminoAcid.E, Double.valueOf(7.7D));
            Nterm_Pk.put(AminoAcid.Q, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.G, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.H, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.I, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.L, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.K, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.M, Double.valueOf(7.0D));
            Nterm_Pk.put(AminoAcid.F, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.P, Double.valueOf(8.359999999999999D));
            Nterm_Pk.put(AminoAcid.S, Double.valueOf(6.93D));
            Nterm_Pk.put(AminoAcid.T, Double.valueOf(6.82D));
            Nterm_Pk.put(AminoAcid.W, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.Y, Double.valueOf(7.5D));
            Nterm_Pk.put(AminoAcid.V, Double.valueOf(7.44D));

            sideGroup_Pk.put(AminoAcid.R, Double.valueOf(-12.0D));
            sideGroup_Pk.put(AminoAcid.D, Double.valueOf(4.05D));
            sideGroup_Pk.put(AminoAcid.C, Double.valueOf(9.0D));
            sideGroup_Pk.put(AminoAcid.E, Double.valueOf(4.45D));
            sideGroup_Pk.put(AminoAcid.H, Double.valueOf(-5.98D));
            sideGroup_Pk.put(AminoAcid.K, Double.valueOf(-10.0D));
            sideGroup_Pk.put(AminoAcid.Y, Double.valueOf(10.0D));

        }

    }

    /* This function set an specific set of pk values */

    public static void setPkMethod(String pkMethod){
        if(pkMethod.equalsIgnoreCase(BJELL_PKMETHOD))
            defaultPkMethod  = BJELL_PKMETHOD;
        else if(pkMethod.equalsIgnoreCase(SKOOG_PKMETHOD))
            defaultPkMethod  = SKOOG_PKMETHOD;
        else if(pkMethod.equalsIgnoreCase(EXPASY_PKMETHOD))
            defaultPkMethod  = EXPASY_PKMETHOD;
        else defaultPkMethod = CALIBRATED_PKMETHOD;
    }

    @Override
    public Double computePI(List<AminoAcid> sequence){
        return calculate(sequence);
    }


    @Override
    public Double computeChargeAtpH(List<AminoAcid> sequence, Double pH){
        return getcharge(sequence, Nterm_Pk, Cterm_Pk, sideGroup_Pk, pH);
    }

    @Override
    public Map<Double, Double> computeStepMethodPI(List<AminoAcid> sequence) {

        double charge;
        double pHLocal  = 0.0;         //Starting point pI = 0.0 - to generate pH graph
        double step = 0.05;

        pHLocal  = 0.0;                //Starting point pI = 0.0 - to generate pH graph
        int i = 0;
        Map<Double, Double> newCoor = new TreeMap<Double, Double>();
        do{
            charge = getcharge(sequence, Nterm_Pk, Cterm_Pk, sideGroup_Pk, pHLocal);
            pHLocal += step;
            newCoor.put(new Double(pHLocal),new Double(charge));
            i++;
        }while(pHLocal < 14.0);

        return newCoor;
    }

    /* Some considerations about isoelectric point, when the methylation
    * is consider for the SequenceAA then the AA Aspartic (D),
    * and Glutamic (E) must be removed.
    */

    private double calculate(List<AminoAcid> sequence){
        double piInt = 0.5D;

        /* This algorithm used this strategy:
        * Take an of step = 0.5 and make a loop while
        * the charge of the SequenceAA was >= 0.0 then take
        * the last value of the charge and the last value
        * of ph to make an exaustive step with and step of 0.0001.
        */


        do {
            if (getcharge(sequence, Nterm_Pk, Cterm_Pk, sideGroup_Pk, this.pH) < 0.0D) break;
            this.pH += piInt;

        }while (this.pH <= 14.0D);

        this.pH -= piInt;

        piInt = 0.001D; // take a short step to compute the pI in this range.
        // this algorithm is very exaustive because the error
        // in the algorithm is in the 3t decimal of the number.

        double getpI = 0.0;
        do {
            getpI = getcharge(sequence, Nterm_Pk, Cterm_Pk, sideGroup_Pk, this.pH);
            if (getpI < 0.0D){
                this.pH -= piInt;
                break ;
            }
            this.pH += piInt;

        }while (this.pH <= 14.0D);

        if (getpI >= 0.0D) this.pH = 100.001D; // this is an unreal value.

        double pHround = Math.round(this.pH * 100.0D);

        this.pH = -1.0D;

        return (pHround / 100.0D);
    }


    /* This function compute the charge to a SequenceAA,
     * take to account the composition of the SequenceAA. Each phosphorylation
     * was represented as (p), the acethylation of the N-term residue
     * was represented as (a), the methionine oxidation of a residue
     * was represented as (o) (the effect of this modification in the pI
     * value is minimal), the metionine (M) oxidation in the N-term
     * was represented as (m).
     */

    private Double getcharge(List<AminoAcid> seq, Map<AminoAcid,Double> AApI_n, Map<AminoAcid,Double> AApI_c, Map<AminoAcid,Double> AApI_side, Double PH){
        AminoAcid sideAA = null; //281
        double pHpK = 0.0D;
        this.FoRmU = 0.0D;

        pHpK = PH - Double.valueOf(AApI_n.get(seq.get(0))).doubleValue();
        this.FoRmU += 1.0D / (1.0D + Math.pow(10.0D, pHpK));

        pHpK = Double.valueOf(AApI_c.get(seq.get(seq.size()-1))).doubleValue() - PH;
        this.FoRmU += -1.0D / (1.0D + Math.pow(10.0D, pHpK));

        for (int i = 0; i < seq.size(); ++i) {
            AminoAcid AA = seq.get(i);
            if (AApI_side.containsKey(AA)){
                double valuepK = Double.valueOf(AApI_side.get(AA)).doubleValue();
                if (valuepK < 0.0D) {
                    pHpK = PH + valuepK;
                    this.FoRmU += 1.0D / (1.0D + Math.pow(10.0D, pHpK));
                } else {
                    pHpK = valuepK - PH;
                    this.FoRmU += -1.0D / (1.0D + Math.pow(10.0D, pHpK));
                }
            }
        }
        return this.FoRmU;
    }

    public double getCTermSelected(AminoAcid AA){
        return Cterm_Pk.get(AA);
    }

    public double getNTermSelected(AminoAcid AA){
        return Nterm_Pk.get(AA);
    }



}
