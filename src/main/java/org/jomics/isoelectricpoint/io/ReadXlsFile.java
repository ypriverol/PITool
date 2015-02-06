package org.jomics.isoelectricpoint.io;

/**
 * Created with IntelliJ IDEA.
 * User: CEBIO3
 * Date: 7/06/14
 * Time: 18:55
 * To change this template use File | Settings | File Templates.
 */

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

public class ReadXlsFile {
    private String filename = null;
    private File file = null;
    private float pH;

    public float getpH() {
        return pH;
    }

    public void setpH(float pH) {
        this.pH = pH;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @param aFileName full name of an existing, readable file.
     */
    public ReadXlsFile(String aFileName) {
        this.file = new File(aFileName);
    }

    public void read() throws IOException, BiffException {
        Workbook wb = Workbook.getWorkbook(this.file);
        Sheet sheet = wb.getSheet(0);
        int rows = sheet.getRows();
        PeptideTxtReader peptidetxtreader = new PeptideTxtReader();

        for(int row = 0; row < rows; row++) {
            String sequence = sheet.getCell(0, row).getContents().toString(),
                    pi = sheet.getCell(1, row).getContents(),
                    charge = sheet.getCell(2, row).getContents();
            double PI, Charge;

            System.out.println("Peptide " + sequence + " with pI: " + pi + " and charge: " + charge);

            if (pi.isEmpty() | pi.trim().equals("null"))
            { PI = 0.0; } else { PI = Double.valueOf(pi).doubleValue(); }
            //{ PI = 0.0; } else { PI = Double.parseDouble(pi); }

            if (charge.isEmpty() | charge.trim().equals("null"))
            { Charge = 0.0; } else {Charge = Double.valueOf(charge).doubleValue();}

            //peptidetxtreader.peplist.add(sequence);
            //peptidetxtreader.pilist.add(PI);
            //peptidetxtreader.chargeAtPHlist.add(Charge);

        }
        /*
        String ph = processLine(sheet.getCell(3, 1).getContents());

        if (ph.isEmpty() | ph.trim().equals("null"))
        { this.pH = 0; } else { this.pH = Float.valueOf(ph).floatValue(); }
        */
        //return peptidetxtreader;
    }

    private String processLine(String nextLine) {
        return nextLine.trim();
    }
}
