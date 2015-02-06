
package org.jomics.isoelectricpoint.sequence.isoelectricpoint.cofactorAdjacentpI;

import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.IsoelectricPointMethod;
import org.jomics.isoelectricpoint.utils.Constants;

import java.util.List;
import java.util.Map;

/**
 * CofactorAdjacentpI compute the isoelectric point using the pk values of the AA in the sequence.
 * The pk values are in a precomputed matrix where the pk for each AA depends od the AA adjacent in
 * the sequence. The method was developed by Cargile and cols:
 *   - CARGILE: Cargile BJ, Sevinsky JR, Essader AS, Eu JP, Stephenson Jr JL. Electrophoresis 2008;29:2768–78.
 *
 * @author yperez
 */
public class CofactorAdjacentpI implements IsoelectricPointMethod {

    static double pIAdjust_H = 4.468244;

    private static double[][] pIAdjust_E_Nterm = new double[3][26];
    private static double[][] pIAdjust_E_Cterm = new double[3][26];
    private static double[][] pIAdjust_D_Nterm = new double[3][26];
    private static double[][] pIAdjust_D_Cterm = new double[3][26];
    private static double[][] pIAdjust_Cterminus = new double[3][26];

    private static volatile CofactorAdjacentpI instance = null;

    public static CofactorAdjacentpI getInstance(){
        if (instance == null) {
            synchronized (CofactorAdjacentpI .class){
                if (instance == null) {
                    instance = new CofactorAdjacentpI();
                }
            }
        }
        return instance;
    }

    /*
     * Constructor of CofactorAdjacentpI needs to read from resources the cofactor for each amino acids
     * in the sequence.
     */

    private CofactorAdjacentpI(){
        pIAdjust_E_Nterm = Constants.pICofactorAdjust_E_Nterm;
        pIAdjust_E_Cterm = Constants.pICofactorAdjust_E_Cterm;
        pIAdjust_D_Nterm = Constants.pICofactorAdjust_D_Nterm;
        pIAdjust_D_Cterm = Constants.pICofactorAdjust_D_Cterm;
        pIAdjust_Cterminus = Constants.pICofactorAdjust_Cterm;
    }

    @Override
    public Double computePI(List<AminoAcid> sequence){

        double OldPI = 10.0;
        double HiPI = 14.0;
        double LowPI = 0.0;
        double C_PI = 7.0;
        double PItemp = 0;

        while (Math.abs(C_PI - OldPI) > .001){

            PItemp = 0;
            for (int i = 0; i < sequence.size(); i++)
                PItemp += GetPIValuesForAABjellvist(sequence.get(i).getOneLetterCode(), C_PI, i, sequence.size() - 1,sequence);

            PItemp += GetPIValuesForTermBjellvist(sequence.get(sequence.size() - 1).getOneLetterCode(), C_PI, 1,sequence);
            PItemp += GetPIValuesForTermBjellvist(sequence.get(0).getOneLetterCode(), C_PI,0,sequence);
            OldPI = C_PI;
            if (PItemp > 0){
                C_PI = (C_PI + LowPI) / 2.0;
                HiPI = OldPI;
            }else{
                C_PI = (C_PI + HiPI) / 2.0;
                LowPI = OldPI;
            }
        }
        return C_PI;
    }

    @Override
    public Double computeChargeAtpH(List<AminoAcid> sequence, Double pH) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Double, Double> computeStepMethodPI(List<AminoAcid> sequence) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private double GetPIValuesForAABjellvist(char LL, double Old_PI, int position, int last, List<AminoAcid> sequence){

        if (LL == 'C'){
            if (position == 0){
                return 1.0 / (1.0 + Math.pow(10, (8.0 - Old_PI)));
            }else if (position == last){
                return 1.0 / (1.0 + Math.pow(10, (9.0 - Old_PI)));
            }else{
                return 1.0 / (1.0 + Math.pow(10, (8.28 -Old_PI)));
            }
        }

        if (LL == 'D'){
            return 1.0 / (1.0 + Math.pow(10, (3.945 + GetComplexPIValue(LL, position, last,sequence) - Old_PI)));
        }

        if (LL == 'E'){
            return 1.0 / (1.0 + Math.pow(10, (4.38 + GetComplexPIValue(LL, position, last, sequence) - Old_PI)));
        }

        if (LL == 'Y'){
            if (position == 0){
                return 1.0 / (1.0 + Math.pow(10, (9.84 - Old_PI)));
            }else if (position == last){
                return 1.0 / (1.0 + Math.pow(10, (10.34 - Old_PI)));
            }else{
                return 1.0 / (1.0 + Math.pow(10, (9.84 - Old_PI)));
            }
        }

        if (LL == 'H'){
            if (position == 0){
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 4.96)));
            }else if (position == last){
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.89)));
            }else{
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - pIAdjust_H)));
            }
        }

        if (LL == 'K'){
            if (position == 0){
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 10.3)));
            }else if (position == last){
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 10.3)));
            }else{
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 9.8)));
            }
        }

        if (LL == 'R'){
            if (position == 0){
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 10.8)));
            }else if (position == last){
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 10.8)));
            }else{
                return -1.0 / (1.0 + Math.pow(10, (Old_PI - 12.0)));
            }
        }

        return 0;
    }

    private double GetPIValuesForTermBjellvist(char LL, double Old_PI, int type, List<AminoAcid> sequence){

        if (type == 0){

            if (LL == 'A') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.58)));

            if (LL == 'C') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 8.12)));

            if (LL == 'D') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.7)));

            if (LL == 'E') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.19)));

            if (LL == 'F') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.96)));

            if (LL == 'G') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.50)));

            if (LL == 'H') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.18)));

            if (LL == 'I') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.48)));

            if (LL == 'K') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.67)));

            if (LL == 'L') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.46)));

            if (LL == 'M') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.980)));

            if (LL == 'N') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.22)));

            if (LL == 'P') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 8.36)));

            if (LL == 'Q') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.73)));

            if (LL == 'R') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.76)));

            if (LL == 'S') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.86)));

            if (LL == 'T') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.02)));

            if (LL == 'V') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.440)));

            if (LL == 'W') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 7.11)));

            if (LL == 'Y') return -1.0 / (1.0 + Math.pow(10, (Old_PI - 6.83)));
        }

        if (type == 1){
            return 1.0 / (1.0 + Math.pow(10, (3.4 + GetComplexPIValue('-', 0, sequence.size() -1,sequence) - Old_PI)));
        }

        return 0;

    }

    private double GetComplexPIValue(char LL, int position, int last, List<AminoAcid> sequence){

        double value, PIModifier;

        PIModifier = 0;
        ////z = Nterm  B = Cterm

        if (LL == 'E'){               /// position 0 = right next to   ////position 1 = one aa away
            if ((position - 3) >= 0){
                PIModifier += pIAdjust_E_Nterm[2][ConvertAAtoNumber(sequence.get(position - 3).getOneLetterCode())];
                PIModifier += pIAdjust_E_Nterm[1][ConvertAAtoNumber(sequence.get(position - 2).getOneLetterCode())];
                PIModifier += pIAdjust_E_Nterm[0][ConvertAAtoNumber(sequence.get(position - 1).getOneLetterCode())];

                if ((position - 3) == 0)
                    PIModifier += pIAdjust_E_Nterm[0][1];
            }else if ((position - 2) >= 0){
                PIModifier += pIAdjust_E_Nterm[2][25];
                PIModifier += pIAdjust_E_Nterm[1][ConvertAAtoNumber(sequence.get(position - 2).getOneLetterCode())];
                PIModifier += pIAdjust_E_Nterm[0][ConvertAAtoNumber(sequence.get(position - 1).getOneLetterCode())];
            }else if ((position - 1) >= 0){
                PIModifier += pIAdjust_E_Nterm[1][25];
                PIModifier += pIAdjust_E_Nterm[0][ConvertAAtoNumber(sequence.get(position - 1).getOneLetterCode())];
            }else{
                PIModifier += pIAdjust_E_Nterm[0][25];
            }

            if ((position + 3) <= last){
                PIModifier += pIAdjust_E_Cterm[2][ConvertAAtoNumber(sequence.get(position + 3).getOneLetterCode())];
                PIModifier += pIAdjust_E_Cterm[1][ConvertAAtoNumber(sequence.get(position + 2).getOneLetterCode())];
                PIModifier += pIAdjust_E_Cterm[0][ConvertAAtoNumber(sequence.get(position + 1).getOneLetterCode())];
            }else if ((position + 2) <= last){
                PIModifier += pIAdjust_E_Cterm[2][1];
                PIModifier += pIAdjust_E_Cterm[1][ConvertAAtoNumber(sequence.get(position + 2).getOneLetterCode())];
                PIModifier += pIAdjust_E_Cterm[0][ConvertAAtoNumber(sequence.get(position + 1).getOneLetterCode())];
            }else if ((position + 1) <= last){
                PIModifier += pIAdjust_E_Cterm[1][1];
                PIModifier += pIAdjust_E_Cterm[0][ConvertAAtoNumber(sequence.get(position + 1).getOneLetterCode())];
            }else{
                PIModifier += pIAdjust_E_Cterm[0][1];
            }
        }

        if (LL == 'D'){   /// position 0 = right next to
            if ((position - 3) >= 0){
                PIModifier += pIAdjust_D_Nterm[2][ConvertAAtoNumber(sequence.get(position - 3).getOneLetterCode())];
                PIModifier += pIAdjust_D_Nterm[1][ConvertAAtoNumber(sequence.get(position - 2).getOneLetterCode())];
                PIModifier += pIAdjust_D_Nterm[0][ConvertAAtoNumber(sequence.get(position - 1).getOneLetterCode())];

                if ((position - 3) == 0)
                    PIModifier += pIAdjust_D_Nterm[0][1];
            } else if ((position - 2) >= 0){
                PIModifier += pIAdjust_D_Nterm[2][25];
                PIModifier += pIAdjust_D_Nterm[1][ConvertAAtoNumber(sequence.get(position - 2).getOneLetterCode())];
                PIModifier += pIAdjust_D_Nterm[0][ConvertAAtoNumber(sequence.get(position - 1).getOneLetterCode())];
            }else if ((position - 1) >= 0){
                PIModifier += pIAdjust_D_Nterm[1][25];
                PIModifier += pIAdjust_D_Nterm[0][ConvertAAtoNumber(sequence.get(position - 1).getOneLetterCode())];
            }else{
                PIModifier += pIAdjust_D_Nterm[0][25];
            }

            if ((position + 3) <= last) {
                PIModifier += pIAdjust_D_Cterm[2][ConvertAAtoNumber(sequence.get(position + 2).getOneLetterCode())];
                PIModifier += pIAdjust_D_Cterm[1][ConvertAAtoNumber(sequence.get(position + 2).getOneLetterCode())];
                PIModifier += pIAdjust_D_Cterm[0][ConvertAAtoNumber(sequence.get(position + 1).getOneLetterCode())];
            }else if ((position + 2) <= last){
                PIModifier += pIAdjust_D_Cterm[2][1];
                PIModifier += pIAdjust_D_Cterm[1][ConvertAAtoNumber(sequence.get(position + 2).getOneLetterCode())];
                PIModifier += pIAdjust_D_Cterm[0][ConvertAAtoNumber(sequence.get(position + 1).getOneLetterCode())];
            }else if ((position + 1) <= last){
                PIModifier += pIAdjust_D_Cterm[1][1];
                PIModifier += pIAdjust_D_Cterm[0][ConvertAAtoNumber(sequence.get(position + 1).getOneLetterCode())];
            }else{
                PIModifier += pIAdjust_D_Cterm[0][1];
            }
        }

        if (LL == '-'){  //Cterm
            PIModifier += pIAdjust_Cterminus[2][ConvertAAtoNumber(sequence.get(last - 2).getOneLetterCode())];
            PIModifier += pIAdjust_Cterminus[1][ConvertAAtoNumber(sequence.get(last - 1).getOneLetterCode())];
            PIModifier += pIAdjust_Cterminus[0][ConvertAAtoNumber(sequence.get(last).getOneLetterCode())];
        }
        return PIModifier;

    }

    private int ConvertAAtoNumber(char LL){

        if (LL == 'A') return 0;

        if (LL == 'B') return 1;

        if (LL == 'C') return 2;

        if (LL == 'D') return 3;

        if (LL == 'E') return 4;

        if (LL == 'F') return 5;

        if (LL == 'G') return 6;

        if (LL == 'H') return 7;

        if (LL == 'I') return 8;

        if (LL == 'J') return 9;

        if (LL == 'K') return 10;

        if (LL == 'L') return 11;

        if (LL == 'M') return 12;

        if (LL == 'N') return 13;

        if (LL == 'O') return 14;

        if (LL == 'P') return 15;

        if (LL == 'Q') return 16;

        if (LL == 'R') return 17;

        if (LL == 'S') return 18;

        if (LL == 'T') return 19;

        if (LL == 'U') return 20;

        if (LL == 'V') return 21;

        if (LL == 'W') return 22;

        if (LL == 'X') return 23;

        if (LL == 'Y') return 24;

        if (LL == 'Z') return 25;


        return -1;
    }

}