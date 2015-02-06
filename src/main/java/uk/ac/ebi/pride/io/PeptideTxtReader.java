package uk.ac.ebi.pride.io;

/**
 * Created with IntelliJ IDEA.
 * User: guillermo
 * Date: 30/05/14
 * Time: 23:56
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class PeptideTxtReader {
    //Fields
    public ArrayList<String> peplist;
    public ArrayList<Double> pilist;
    public ArrayList<Double> chargeAtPHlist;

    public int cont_G=0, cont_A=0, cont_S=0, cont_P=0, cont_V=0, cont_T=0, cont_C=0, cont_I=0, cont_L=0, cont_N=0,
            cont_D=0, cont_Q=0, cont_K=0, cont_E=0, cont_M=0, cont_H=0, cont_F=0, cont_R=0, cont_Y=0, cont_W=0;
    private int counter = 0;

    //Constructor
    public PeptideTxtReader() {
    }

    public void readFile(String inFileName, String separator){
        BufferedReader br;
        try {
            br = new BufferedReader( new FileReader(inFileName) );
            String line = null;
            peplist = new ArrayList<String>();
            pilist = new ArrayList<Double>();

            while((line = br.readLine()) != null){
                //String[] tmpwords = line.split(",");
                String[] tmpwords = line.split(separator);
                    this.peplist.add(tmpwords[0]);
                    this.pilist.add(Double.parseDouble(tmpwords[1]));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            JOptionPane.showMessageDialog(null, "Invalid file format");
            throw new ArrayIndexOutOfBoundsException("Exception for incorrect file format (i.e incorrect peptide and isoelectric point separator)");
        }
    }

    /*public ArrayList<Integer> compositionAA(String peptideseq){
        String pepseq = peptideseq;
        char[] AAs = pepseq.toCharArray();

        // amino acids counters
        cont_G=0; cont_A=0; cont_S=0; cont_P=0; cont_V=0; cont_T=0; cont_C=0; cont_I=0; cont_L=0; cont_N=0;
        cont_D=0; cont_Q=0; cont_K=0; cont_E=0; cont_M=0; cont_H=0; cont_F=0; cont_R=0; cont_Y=0; cont_W=0;
        ArrayList<Integer> AAcounters = new ArrayList<Integer>();

        Character[] characters = new Character[AAs.length];
        char aminoacid;
        for (int i = 0; i < AAs.length; i++) {
            characters[i]=AAs[i];

            aminoacid = AAs[i];

            switch (aminoacid) {
                case 'G':  this.cont_G++;
                    break;
                case 'A':  this.cont_A++;
                    break;
                case 'S':  this.cont_S++;
                    break;
                case 'P':  this.cont_P++;
                    break;
                case 'V':  this.cont_V++;
                    break;
                case 'T':  this.cont_T++;
                    break;
                case 'C':  this.cont_C++;
                    break;
                case 'I':  this.cont_I++;
                    break;
                case 'L':  this.cont_L++;
                    break;
                case 'N':  this.cont_N++;
                    break;
                case 'D':  this.cont_D++;
                    break;
                case 'Q':  this.cont_Q++;
                    break;
                case 'K':  this.cont_K++;
                    break;
                case 'E':  this.cont_E++;
                    break;
                case 'M':  this.cont_M++;
                    break;
                case 'H':  this.cont_H++;
                    break;
                case 'F':  this.cont_F++;
                    break;
                case 'R':  this.cont_R++;
                    break;
                case 'Y':  this.cont_Y++;
                    break;
                case 'W':  this.cont_W++;
                    break;// etc etc
                default: aminoacid = 'Z';
                    break;
            }
        }
        AAcounters.add(this.cont_G);
        AAcounters.add(this.cont_A);
        AAcounters.add(this.cont_S);
        AAcounters.add(this.cont_P);
        AAcounters.add(this.cont_V);
        AAcounters.add(this.cont_T);
        AAcounters.add(this.cont_C);
        AAcounters.add(this.cont_I);
        AAcounters.add(this.cont_L);
        AAcounters.add(this.cont_N);
        AAcounters.add(this.cont_D);
        AAcounters.add(this.cont_Q);
        AAcounters.add(this.cont_K);
        AAcounters.add(this.cont_E);
        AAcounters.add(this.cont_M);
        AAcounters.add(this.cont_H);
        AAcounters.add(this.cont_F);
        AAcounters.add(this.cont_R);
        AAcounters.add(this.cont_Y);
        AAcounters.add(this.cont_W);

        return AAcounters;
    }*/

    public void writeCsvFile(String outFileName, String peptideseq, ArrayList<Integer> AAcounters){
        try{
            FileWriter writer = new FileWriter(outFileName, true);

            if (this.counter == 0){
                writer.append("Peptide,G,A,S,P,V,T,C,I,L,N,D,Q,K,E,M,H,F,R,Y,W");
                writer.append('\n');
                counter++;
            }

            writer.append(peptideseq+',');
            for(int i=0; i<AAcounters.size(); i++){
                if(i == AAcounters.size()-1){
                    writer.append(Integer.toString(AAcounters.get(i)));
                }
                else{
                    writer.append(Integer.toString(AAcounters.get(i)) + ",");
                }
            }
            writer.append('\n');

            writer.flush();
            writer.close();
        }
        catch(Exception exept){
            exept.printStackTrace();
        }
    }

    public void writePepListpI(String outFileName, JTable table){
        try{
            FileWriter writerpeplist = new FileWriter(outFileName, true); //Esto es agregado para la lista de peptidos
            /*
            writerpeplist.append(peptideseq + ',' +
                                 Integer.toString((int)(Math.random()*(9-2+1)+2)) + ',' +
                                 Double.toString((int)(Math.random()*(9-2+1)+2)) + ',');
            */
            for(int i = 0; i < table.getRowCount(); i++){
                for(int j = 0; j < table.getColumnCount();j++){
                    //Object objeto = table.getValueAt(i,j);

                    if(j == table.getColumnCount()-1){
                        writerpeplist.append(String.valueOf(table.getValueAt(i,j)));
                    }
                    else{
                        writerpeplist.append(String.valueOf(table.getValueAt(i,j)) + ',');
                    }
                }
                writerpeplist.append("\n");
            }


            writerpeplist.append('\n');

            writerpeplist.flush();
            writerpeplist.close();
        }
        catch(Exception exept){
            exept.printStackTrace();
        }
    }
}
