package org.jomics.isoelectricpoint.io;

/**
 * Created with IntelliJ IDEA.
 * User: CEBIO3
 * Date: 8/06/14
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */

import jxl.*;
import jxl.write.*;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriterXlsFile {
    private File file;
    private JTable table;
    private String tabName;
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

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public WriterXlsFile(JTable table, File file, String tabName, float pH){
        if (file.getAbsolutePath().contains(".xls")) {
            this.file = new File(file.getAbsolutePath());
        } else {
            this.file = new File(file.getAbsolutePath() + ".xls");
        }

        this.table   = table;
        this.tabName = tabName;
        this.pH      = pH;
    }


    public boolean export(){
        try{
            DataOutputStream out=new DataOutputStream(new FileOutputStream(file));
            WritableWorkbook w = Workbook.createWorkbook(out);
            WritableSheet sheet = w.createSheet(tabName, 0);

            sheet.addCell(new Label(0, 0, "Sequence"));
            sheet.addCell(new Label(1, 0, "Isoelectric Point"));
            sheet.addCell(new Label(2, 0, "Charge at pH"));
            sheet.addCell(new Label(3, 0, "pH"));
            sheet.addCell(new Label(3, 1, String.valueOf(String.valueOf(this.pH))));

            for(int i = 0; i < table.getRowCount(); i++){
                for(int j = 1; j < table.getColumnCount();j++){
                    Object objeto = table.getValueAt(i,j);

                    if (isDouble(String.valueOf(objeto))){
                        sheet.addCell(new jxl.write.Number(j-1, i+1, Double.parseDouble(String.valueOf(objeto))));
                    }
                    else{
                        sheet.addCell(new Label(j-1 , i+1, String.valueOf(objeto)));
                    }
                }
            }
            w.write();
            w.close();
            out.close();
            return true;
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch(WriteException ex){
            ex.printStackTrace();
        }
        return false;
    }


    public boolean isDouble(String num) {
        try {
            Double.valueOf(num);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
