package org.jomics.isoelectricpoint.app;

import java.beans.*;
import javax.swing.border.*;

import com.xeiam.xchart.*;
import org.jomics.isoelectricpoint.io.PeptideTxtReader;

import org.jomics.isoelectricpoint.io.*;
import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.bjellpI.BjellpI;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.cofactorAdjacentpI.CofactorAdjacentpI;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.iterativepI.IterativepI;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.svmpI.SvmpI;
import org.jomics.isoelectricpoint.utils.ElectrophoreticFraction;
import org.jomics.isoelectricpoint.utils.SequenceUtils;
import org.jomics.isoelectricpoint.utils.Statistical;
import weka.core.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.swing.GroupLayout;

/**
 * @author enrique
 */
public class IsoelectricPointTool extends JFrame implements ActionListener {

    //fields
    private PeptideTxtReader peptidetxtreader;
    private MethodSettingsForm methodsform = new MethodSettingsForm();


    //constructor
    public IsoelectricPointTool() {

        super("IsoelectricPointPredictor"); //Window Title.

        this.initComponents();
        this.setResizable(false); // Set window size for non-variable
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close.
        this.setVisible(true);

        this.computeStatisticalButton.setEnabled(false);
        this.plotDispersionButton.setEnabled(false);
        this.plotDistributionButton.setEnabled(false);
        this.plotFractionButton.setEnabled(false);
        this.checkBox1.setEnabled(false);
        this.checkBox2.setEnabled(false);
    }


    //methods

    public static void main(String[] args) throws IOException {
        // write your code here
        IsoelectricPointTool pICalculator = new IsoelectricPointTool();
    }

    private void importExcelMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
        // This method allows to import to *.xls files
        JFileChooser filechooser = new JFileChooser();
        int returnVal = filechooser.showOpenDialog(IsoelectricPointTool.this);

        if (returnVal == JFileChooser.APPROVE_OPTION){
            try {
                String tmpFileName = filechooser.getSelectedFile().getPath();
                System.out.println(tmpFileName);

                //this is where a real application would open the file.
                peptidetxtreader = new PeptideTxtReader();
                ReadXlsFile xlsreader = new ReadXlsFile(tmpFileName);
                xlsreader.read();
                //peptidetxtreader = xlsreader.read();

            } catch (Exception exept) {
                exept.printStackTrace();
            }
            if(peptidetxtreader.peplist.isEmpty()){
                JOptionPane.showConfirmDialog(null, "The file is Empty", "Empty file", JOptionPane.INFORMATION_MESSAGE);
            }else{
                DefaultTableModel dm = (DefaultTableModel) table2.getModel();
                this.fillDataTable(dm, peptidetxtreader.peplist, peptidetxtreader.pilist);
                this.clearTableButton.setEnabled(true);

                this.exportMenu.setEnabled(true);
                this.exportCsvMenuItem.setEnabled(true);
                this.exportExcelMenuItem.setEnabled(true);
                this.methodSettingsMenuItem.setEnabled(true);


            }
        }
    }

    private void exportCsvMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
        // This method allows to export to *.csv files
        JFileChooser filechooser = new JFileChooser();
        int returnVal = filechooser.showSaveDialog(IsoelectricPointTool.this);

        if (returnVal == JFileChooser.APPROVE_OPTION){
            try {
                String tmpFileName = filechooser.getSelectedFile().getPath();
                System.out.println(tmpFileName);

                //this is where a real application would open the file.
                peptidetxtreader = new PeptideTxtReader();
                peptidetxtreader.writePepListpI(tmpFileName, this.table2);

            } catch (Exception exept) {
                exept.printStackTrace();
            }
        }
    }

    private void exitMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
        this.dispose();
        //this.setVisible(false);
    }

    private void methodSettingsMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
        // This method visualize the Method Settings Form
        methodsform = new MethodSettingsForm();
        this.computeTheoreticalPiButton.setEnabled(true);
        methodsform.setVisible(true);
    }


    private void clearTableButtonActionPerformed(ActionEvent e) {

        // TODO add your code here
        DefaultTableModel sequencesTableModel = (DefaultTableModel) table2.getModel();
        this.clearDataTable(sequencesTableModel);

        this.exportMenu.setEnabled(false);
        this.exportCsvMenuItem.setEnabled(false);
        this.exportExcelMenuItem.setEnabled(false);
        this.methodSettingsMenuItem.setEnabled(false);
        this.computeStatisticalButton.setEnabled(false);
        this.plotDispersionButton.setEnabled(false);
        this.plotDistributionButton.setEnabled(false);
        this.plotFractionButton.setEnabled(false);

        //removing graph if SequenceTable is empty
        this.panel2.removeAll();
        this.panel4.removeAll();
        this.panel6.removeAll();

        //clear correlation and standard deviation values
        this.formattedTextField1.removeAll();
        this.formattedTextField2.removeAll();

        //disable Tabs if SequenceTable is empty
        this.TabbedPane.setEnabledAt(1,false);
        this.TabbedPane.setEnabledAt(2,false);
        this.TabbedPane.setEnabledAt(3,false);
        this.TabbedPane.setEnabledAt(4,false);
    }

    private void computeTheoreticalPiButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        // This method compute Theoretical Isoelectric Point after the method settings were fixed
        HashMap<String, Object> methodsettings = new HashMap<String, Object>();
        methodsettings = methodsform.getSettings();
        System.out.println(methodsform.isFilledSettings());

        if(!methodsform.isFilledSettings()){
            JOptionPane.showMessageDialog(null, "You must fill the method parameters.");
        }

        else{

            if(methodsettings.containsKey("method")){
                if(Integer.parseInt(methodsettings.get("method").toString())==0){
                    JOptionPane.showMessageDialog(null, "This is the \"Interactive Method\".");
                    IterativepI iterativepi = IterativepI.getInstance(methodsettings.get("pkSetElement").toString(), Double.parseDouble(methodsettings.get("ph").toString()));
                    for(int row=0; row<table2.getRowCount();row++){
                        String sequence = (String)table2.getValueAt(row,0);
                        if(sequence != null){
                            table2.setValueAt((iterativepi.computePI(SequenceUtils.getSequence(sequence))),row, 2);
                        }
                    }
                }else if(Integer.parseInt(methodsettings.get("method").toString())==1){
                    JOptionPane.showMessageDialog(null, "This is the \"BJell Method\".");
                    BjellpI bjellpi = BjellpI.getInstance(methodsettings.get("pkSetElement").toString(), Double.parseDouble(methodsettings.get("ph").toString()));
                    for(int row=0; row<table2.getRowCount();row++){
                        String sequence = (String)table2.getValueAt(row,0);
                        if(sequence != null){
                            table2.setValueAt((bjellpi.computePI(SequenceUtils.getSequence(sequence))),row, 2);
                        }
                    }


                }else if(Integer.parseInt(methodsettings.get("method").toString())==2){
                    JOptionPane.showMessageDialog(null, "This is the \"Cofactor Method\".");
                    CofactorAdjacentpI cofactoradjacentpi = CofactorAdjacentpI.getInstance();
                    for(int row=0; row<table2.getRowCount();row++){
                        String sequence = (String)table2.getValueAt(row,0);
                        if(sequence != null){
                            table2.setValueAt((cofactoradjacentpi.computePI(SequenceUtils.getSequence(sequence))),row, 2);
                        }
                    }


                }else{
                    JOptionPane.showMessageDialog(null, "This is the \"pI-SVM Method\".");

                    HashMap<List<AminoAcid>, Double> mapSequences = new HashMap<List<AminoAcid>, Double>();
                    SvmpI calculator = null;

                    //fill Map Sequences (sequence ---> experimental pi)
                    for(int row=0; row<table2.getRowCount();row++){
                        String sequence = (String)table2.getValueAt(row,0);
                        Double exp_pi = (Double)table2.getValueAt(row,1);
                        if(sequence != null){
                           mapSequences.put(SequenceUtils.getSequence(sequence), exp_pi);
                        }
                    }

                    //Build and training SVM-pI algorithm. For training is used de experimental data coming
                    try {
                        calculator = SvmpI.getInstance(mapSequences, false);
                        //set Sigma and Complexity parameters
                        //calculator.SIGMA = Double.parseDouble(methodsettings.get("Sigma").toString());
                        //calculator.C_FACTOR = Double.parseDouble(methodsettings.get("CFactor").toString());

                    } catch (Exception e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    //filling main table with theoretical pI values
                    for(int row=0; row<table2.getRowCount();row++){
                        Double svmPI = null;
                        String sequence = (String)table2.getValueAt(row,0);
                        if (calculator != null && sequence != null) {
                            try {
                                svmPI = calculator.computePI(SequenceUtils.getSequence(sequence));
                            } catch (Exception e1) {
                                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                        table2.setValueAt(svmPI,row, 2);
                    }

                }
            } else{
                System.out.println("Not method selected");
            }

        }

         //
         this.clearTableButton.setEnabled(true);
         this.computeStatisticalButton.setEnabled(true);
         this.plotDispersionButton.setEnabled(true);
         this.plotDistributionButton.setEnabled(true);
         this.checkBox1.setEnabled(true);
         this.checkBox1.setSelected(true);
         this.checkBox2.setEnabled(true);
         //this.plotFractionButton.setEnabled(true);

         //To enable tabs
         this.TabbedPane.setEnabledAt(1,true);
         this.TabbedPane.setEnabledAt(2,true);
         this.TabbedPane.setEnabledAt(3,true);
         this.TabbedPane.setEnabledAt(4,true);
    }

    private void importCSVMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
        // This method allows import *.csv files
        JFileChooser filechooser = new JFileChooser();
        int returnVal = filechooser.showOpenDialog(IsoelectricPointTool.this);

        if (returnVal == JFileChooser.APPROVE_OPTION){
            try {
                String tmpFileName = filechooser.getSelectedFile().getPath();
                System.out.println(tmpFileName);

                //this is where a real application would open the file.
                peptidetxtreader = new PeptideTxtReader();
                peptidetxtreader.readFile(tmpFileName, ",");

            } catch (Exception exept) {
                exept.printStackTrace();
            }
            if(peptidetxtreader.peplist.isEmpty()){
                JOptionPane.showConfirmDialog(null, "The file is Empty", "Empty file", JOptionPane.INFORMATION_MESSAGE);
            }else{
                DefaultTableModel dm = (DefaultTableModel) table2.getModel();
                this.fillDataTable(dm, peptidetxtreader.peplist, peptidetxtreader.pilist);

                this.clearTableButton.setEnabled(true);
                this.exportMenu.setEnabled(true);
                this.exportCsvMenuItem.setEnabled(true);
                this.exportExcelMenuItem.setEnabled(true);
                this.methodSettingsMenuItem.setEnabled(true);

            }
        }
    }

    private void importTSVMenuItemActionPerformed(ActionEvent e){
        // TODO add your code here
        // This method allows import *.txt files
        JFileChooser filechooser = new JFileChooser();
        int returnVal = filechooser.showOpenDialog(IsoelectricPointTool.this);

        if (returnVal == JFileChooser.APPROVE_OPTION){
            String tmpFileName = filechooser.getSelectedFile().getPath();
            System.out.println(tmpFileName);

            //this is where a real application would open the file.
            peptidetxtreader = new PeptideTxtReader();
            peptidetxtreader.readFile(tmpFileName, "\t");

            if(peptidetxtreader.peplist.isEmpty()){
                JOptionPane.showConfirmDialog(null, "The file is Empty", "Empty file", JOptionPane.INFORMATION_MESSAGE);
            }else{
                DefaultTableModel dm = (DefaultTableModel) table2.getModel();
                this.fillDataTable(dm, peptidetxtreader.peplist, peptidetxtreader.pilist);

                this.clearTableButton.setEnabled(true);
                this.exportMenu.setEnabled(true);
                this.exportCsvMenuItem.setEnabled(true);
                this.exportExcelMenuItem.setEnabled(true);
                this.methodSettingsMenuItem.setEnabled(true);

            }
        }
    }

    private void plotDispersionButtonActionPerformed(ActionEvent e) {
        // TODO add your code here

        panel2.removeAll();

        ArrayList<Double> pi_experiemntal_array = new ArrayList<Double>();
        ArrayList<Double> pi_theoretical_array = new ArrayList<Double>();

        for(int i = 0; i < table2.getRowCount()-1; i++){

            Double pi_experimental = (Double)table2.getValueAt(i, 1);
            Double pi_theoretical =  (Double)table2.getValueAt(i,2);

                 pi_experiemntal_array.add(pi_experimental.doubleValue());
                 pi_theoretical_array.add(pi_theoretical.doubleValue());

        }

        // Create Chart
        Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Scatter).width(800).height(600).title("Isoelectric Point Dispersion").xAxisTitle("Experimental Isoelectric Point").yAxisTitle("Theoretical Isoelectric Point").build();

        // Series
        chart.addSeries("Sequence", pi_experiemntal_array, pi_theoretical_array);
        XChartPanel chartpanel = new XChartPanel(chart);
        chartpanel.setAutoscrolls(true);
        chartpanel.setBackground(Color.WHITE);

        panel2.setLayout(new BorderLayout());
        panel2.add(chartpanel, BorderLayout.CENTER);
        panel2.validate();

        //computing and showing correlation and standard deviation
        this.formattedTextField1.setValue(Statistical.getStandardDeviationValue(pi_theoretical_array));
        this.formattedTextField2.setValue(Statistical.getCorrelation(pi_theoretical_array, pi_experiemntal_array));
    }

    private void plotDistributionButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        panel4.removeAll();

        ArrayList<Double> pI_experimental_set = new ArrayList<Double>();
        ArrayList<Double> pI_theoretical_set = new ArrayList<Double>();

        for(int i = 0; i < table2.getRowCount()-1; i++){

            Double pi_experimental = (Double)table2.getValueAt(i, 1);
            Double pi_theoretical =  (Double)table2.getValueAt(i,2);

            pI_experimental_set.add(pi_experimental);
            pI_theoretical_set.add(pi_theoretical);
        }

        // Create Chart
        Chart chart = new ChartBuilder().chartType(StyleManager.ChartType.Bar).width(800).height(600).title("Isoelectric Point Distribution").xAxisTitle("Isoelectric Point").yAxisTitle("N. of elements").build();

        //fitting theoretical and experimental axis range
        double min_experimental_pi = Statistical.getMinValue(pI_experimental_set);
        double max_experimental_pi = Statistical.getMaxValue(pI_experimental_set);
        //double min_theoretical_pi = Statistical.getMinValue(pI_theoretical_set);
        //double max_theoretical_pi = Statistical.getMaxValue(pI_theoretical_set);

        Histogram histogram1 = new Histogram(pI_experimental_set, 100, min_experimental_pi, max_experimental_pi);  //settings limits of the graph
        Histogram histogram2 = new Histogram(pI_theoretical_set, 100, min_experimental_pi, max_experimental_pi);

        if (this.checkBox1.isSelected()){
            chart.addSeries("Experimental pI", histogram1.getxAxisData(), histogram1.getyAxisData());
        }

        if (this.checkBox2.isSelected()){
            chart.addSeries("Theoretical pI", histogram2.getxAxisData(), histogram2.getyAxisData());
        }

       // Customize Chart
        chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.OutsideE);
        chart.getStyleManager().setBarWidthPercentage(.96);
        chart.getStyleManager().setBarsOverlapped(true);


        XChartPanel chartpanel = new XChartPanel(chart);
        chartpanel.setAutoscrolls(true);
        chartpanel.setBackground(Color.WHITE);

        panel4.setLayout(new BorderLayout());
        panel4.add(chartpanel, BorderLayout.CENTER);
        panel4.validate();
    }

    private void plotFractionButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        // generates data
        panel6.removeAll();

        ArrayList<Double> mean_pi_exp_values = new ArrayList<Double>();
        ArrayList<Double> mean_pi_teo_values = new ArrayList<Double>();
        ArrayList<Double> error_pi_teo_values = new ArrayList<Double>();

        for(int i = 0; i < table1.getRowCount(); i++){

            Double mean_exp = (Double)table1.getValueAt(i, 2);
            Double mean_teo =  (Double)table1.getValueAt(i,3);
            Double standard_dev_teo =  (Double)table1.getValueAt(i,4);

            mean_pi_exp_values.add(mean_exp.doubleValue());
            mean_pi_teo_values.add(mean_teo.doubleValue());
            error_pi_teo_values.add(standard_dev_teo);
        }

        // Create Chart
        Chart chart = new ChartBuilder().width(800).height(600).title("Fraction analysis").xAxisTitle("Experimental pI").yAxisTitle("Theoretical pI").chartType(StyleManager.ChartType.Scatter).build();

        // Customize Chart
        chart.getStyleManager().setChartTitleVisible(true);
        chart.getStyleManager().setLegendVisible(true);
        chart.getStyleManager().setAxisTitlesVisible(true);
        //chart.getStyleManager().setXAxisMin(3.0);
        //chart.getStyleManager().setXAxisMax(10.0);
        //chart.getStyleManager().setYAxisMin(3.0);
        //chart.getStyleManager().setYAxisMax(10.0);

        // Series
        Series series = chart.addSeries("fraction", mean_pi_exp_values, mean_pi_teo_values, error_pi_teo_values);
        series.setMarkerColor(Color.BLUE);
        series.setMarker(SeriesMarker.SQUARE);

        //Series
        XChartPanel chartpanel = new XChartPanel(chart);
        chartpanel.setAutoscrolls(true);
        chartpanel.setBackground(Color.WHITE);
        chartpanel.doLayout();

        panel6.setLayout(new BorderLayout());
        panel6.add(chartpanel, BorderLayout.CENTER);
        panel6.validate();
    }

    private void computeStatisticalButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        ArrayList<Double> xData = new ArrayList<Double>();
        ArrayList<Double> yData = new ArrayList<Double>();

        for(int i = 1; i < table2.getRowCount()-1; i++){

            Double pi_experimental = (Double)table2.getValueAt(i, 1);
            Double pi_theoretical =  (Double)table2.getValueAt(i,2);

            xData.add(pi_experimental.doubleValue());
            yData.add(pi_theoretical.doubleValue());

        }

        double start_pi = 3.0;
        double end_pi   = 10;
        double step_pi  = 0.2;

        DefaultTableModel dm = (DefaultTableModel) table1.getModel();
        dm.getDataVector().removeAllElements();
        ElectrophoreticFraction fraction;
        while (start_pi <= end_pi){
            fraction = new ElectrophoreticFraction(xData,yData,start_pi,start_pi+step_pi);
            if(fraction.getCountElements()>=2){
                Object[] tmpData = new Object[]{(String.valueOf(Utils.roundDouble(start_pi, 3)).concat("-").concat(String.valueOf(Utils.roundDouble(start_pi + step_pi, 3))))
                                                ,fraction.getCountElements(),fraction.getPiExpFractionMean(),fraction.getPiTeoFractionMean(),fraction.getStandardDev(),fraction.getOutliersPercent()};
                dm.addRow(tmpData);
            }
            start_pi+=step_pi;
        }

        //to enable operation-dependent
        this.plotFractionButton.setEnabled(true);
    }

    private void checkBox1ActionPerformed(ActionEvent e) {
        // TODO add your code here
       if(!checkBox1.isSelected()){
           if(!checkBox2.isSelected()){
               checkBox2.setSelected(true);
           }
       }
    }

    private void checkBox2ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(!checkBox2.isSelected()){
            if(!checkBox1.isSelected()){
                checkBox1.setSelected(true);
            }
        }
    }

    private void menu1ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void calculatepIMenuItemActionPerformed(ActionEvent e) {
        // TODO add your code here
    }


    private void panel2PropertyChange(PropertyChangeEvent e) {

        // TODO add your code here
        //Showing default empty chart
        // Create Chart

        Chart chart = new ChartBuilder().width(800).height(600).title("Isoelectric Point Dispersion").xAxisTitle("Experimental Isoelectric Point").yAxisTitle("Theoretical Isoelectric Point").build();

        //empty data
        double [] x_data = {0.0,0.0};
        double [] y_data = {0.0,0.0};

        // Series
        chart.addSeries("Sequence", x_data, y_data);
        chart.getStyleManager().setXAxisMin(0.0);
        chart.getStyleManager().setXAxisMax(14.0);
        chart.getStyleManager().setYAxisMin(0.0);
        chart.getStyleManager().setYAxisMax(14.0);

        XChartPanel chartpanel = new XChartPanel(chart);
        chartpanel.setAutoscrolls(true);
        chartpanel.setBackground(Color.WHITE);

        panel2.setLayout(new BorderLayout());
        panel2.add(chartpanel, BorderLayout.CENTER);
        panel2.validate();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        menuBar1 = new JMenuBar();
        fileMenu = new JMenu();
        importMenu = new JMenu();
        importTxtFileMenu = new JMenu();
        importTSVMenuItem = new JMenuItem();
        importCSVMenuItem = new JMenuItem();
        importExcelMenuItem = new JMenuItem();
        importMzidentmlMenuItem = new JMenuItem();
        exportMenu = new JMenu();
        exportCsvMenuItem = new JMenuItem();
        exportExcelMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        processMenu = new JMenu();
        methodSettingsMenuItem = new JMenuItem();
        helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();
        TabbedPane = new JTabbedPane();
        SequencePanel = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        computeTheoreticalPiButton = new JButton();
        clearTableButton = new JButton();
        StatisticalPanel = new JPanel();
        computeStatisticalButton = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel1 = new JPanel();
        plotDispersionButton = new JButton();
        panel2 = new JPanel();
        label1 = new JLabel();
        formattedTextField1 = new JFormattedTextField();
        formattedTextField2 = new JFormattedTextField();
        label2 = new JLabel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        plotDistributionButton = new JButton();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        panel5 = new JPanel();
        panel6 = new JPanel();
        plotFractionButton = new JButton();

        //======== this ========
        setResizable(false);
        Container contentPane = getContentPane();

        //======== menuBar1 ========
        {

            //======== fileMenu ========
            {
                fileMenu.setText("File");
                fileMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menu1ActionPerformed(e);
                    }
                });

                //======== importMenu ========
                {
                    importMenu.setText("Import Data");

                    //======== importTxtFileMenu ========
                    {
                        importTxtFileMenu.setText("TXT File");

                        //---- importTSVMenuItem ----
                        importTSVMenuItem.setText("Tab Separated Value File");
                        importTSVMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                importTSVMenuItemActionPerformed(e);
                            }
                        });
                        importTxtFileMenu.add(importTSVMenuItem);

                        //---- importCSVMenuItem ----
                        importCSVMenuItem.setText("Coma Separated Value File");
                        importCSVMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                importCSVMenuItemActionPerformed(e);
                            }
                        });
                        importTxtFileMenu.add(importCSVMenuItem);
                    }
                    importMenu.add(importTxtFileMenu);

                    //---- importExcelMenuItem ----
                    importExcelMenuItem.setText("XLS File");
                    importExcelMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            importExcelMenuItemActionPerformed(e);
                        }
                    });
                    importMenu.add(importExcelMenuItem);

                    //---- importMzidentmlMenuItem ----
                    importMzidentmlMenuItem.setText("mzidentML File");
                    importMenu.add(importMzidentmlMenuItem);
                }
                fileMenu.add(importMenu);

                //======== exportMenu ========
                {
                    exportMenu.setText("Export Data");
                    exportMenu.setEnabled(false);

                    //---- exportCsvMenuItem ----
                    exportCsvMenuItem.setText("CSV File");
                    exportCsvMenuItem.setEnabled(false);
                    exportCsvMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            exportCsvMenuItemActionPerformed(e);
                        }
                    });
                    exportMenu.add(exportCsvMenuItem);

                    //---- exportExcelMenuItem ----
                    exportExcelMenuItem.setText("XLS File");
                    exportExcelMenuItem.setEnabled(false);
                    exportMenu.add(exportExcelMenuItem);
                }
                fileMenu.add(exportMenu);
                fileMenu.addSeparator();

                //---- exitMenuItem ----
                exitMenuItem.setText("Exit");
                exitMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        exitMenuItemActionPerformed(e);
                    }
                });
                fileMenu.add(exitMenuItem);
            }
            menuBar1.add(fileMenu);

            //======== processMenu ========
            {
                processMenu.setText("Process");

                //---- methodSettingsMenuItem ----
                methodSettingsMenuItem.setText("Methods Settings");
                methodSettingsMenuItem.setEnabled(false);
                methodSettingsMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        calculatepIMenuItemActionPerformed(e);
                        methodSettingsMenuItemActionPerformed(e);
                    }
                });
                processMenu.add(methodSettingsMenuItem);
            }
            menuBar1.add(processMenu);

            //======== helpMenu ========
            {
                helpMenu.setText("Help");

                //---- aboutMenuItem ----
                aboutMenuItem.setText("About");
                helpMenu.add(aboutMenuItem);
            }
            menuBar1.add(helpMenu);
        }
        setJMenuBar(menuBar1);

        //======== TabbedPane ========
        {

            //======== SequencePanel ========
            {

                //======== scrollPane2 ========
                {

                    //---- table2 ----
                    table2.setModel(new DefaultTableModel(
                        new Object[][] {
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                        },
                        new String[] {
                            "Sequence", "Experimental Isoelectric Point", "Theorical Isoelectric Point", "Charge at pH"
                        }
                    ) {
                        Class<?>[] columnTypes = new Class<?>[] {
                            String.class, Double.class, Double.class, Double.class
                        };
                        boolean[] columnEditable = new boolean[] {
                            false, false, false, false
                        };
                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                            return columnTypes[columnIndex];
                        }
                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return columnEditable[columnIndex];
                        }
                    });
                    table2.setCellSelectionEnabled(true);
                    scrollPane2.setViewportView(table2);
                }

                //---- computeTheoreticalPiButton ----
                computeTheoreticalPiButton.setText("Compute");
                computeTheoreticalPiButton.setEnabled(false);
                computeTheoreticalPiButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        computeTheoreticalPiButtonActionPerformed(e);
                    }
                });

                //---- clearTableButton ----
                clearTableButton.setText("Clear Table");
                clearTableButton.setEnabled(false);
                clearTableButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        clearTableButtonActionPerformed(e);
                    }
                });

                GroupLayout SequencePanelLayout = new GroupLayout(SequencePanel);
                SequencePanel.setLayout(SequencePanelLayout);
                SequencePanelLayout.setHorizontalGroup(
                    SequencePanelLayout.createParallelGroup()
                        .addGroup(SequencePanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(SequencePanelLayout.createParallelGroup()
                                .addGroup(SequencePanelLayout.createSequentialGroup()
                                    .addComponent(computeTheoreticalPiButton)
                                    .addGap(533, 533, 533)
                                    .addComponent(clearTableButton))
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE))
                            .addContainerGap())
                );
                SequencePanelLayout.setVerticalGroup(
                    SequencePanelLayout.createParallelGroup()
                        .addGroup(SequencePanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addGroup(SequencePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(computeTheoreticalPiButton)
                                .addComponent(clearTableButton))
                            .addContainerGap())
                );
            }
            TabbedPane.addTab("Sequences", SequencePanel);

            //======== StatisticalPanel ========
            {

                //---- computeStatisticalButton ----
                computeStatisticalButton.setText("Compute Stats Fraction");
                computeStatisticalButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        computeStatisticalButtonActionPerformed(e);
                    }
                });

                //======== scrollPane1 ========
                {

                    //---- table1 ----
                    table1.setModel(new DefaultTableModel(
                        new Object[][] {
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                            {null, null, null, null, null, null},
                        },
                        new String[] {
                            "Fraction", "Number of elements", "pI_mean exp", "pI_mean teo", "Standard Dev", "Outliers count"
                        }
                    ) {
                        Class<?>[] columnTypes = new Class<?>[] {
                            Object.class, Integer.class, Double.class, Double.class, Double.class, Double.class
                        };
                        boolean[] columnEditable = new boolean[] {
                            false, false, false, false, false, false
                        };
                        @Override
                        public Class<?> getColumnClass(int columnIndex) {
                            return columnTypes[columnIndex];
                        }
                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return columnEditable[columnIndex];
                        }
                    });
                    table1.setColumnSelectionAllowed(true);
                    table1.setGridColor(new Color(204, 204, 204));
                    scrollPane1.setViewportView(table1);
                }

                GroupLayout StatisticalPanelLayout = new GroupLayout(StatisticalPanel);
                StatisticalPanel.setLayout(StatisticalPanelLayout);
                StatisticalPanelLayout.setHorizontalGroup(
                    StatisticalPanelLayout.createParallelGroup()
                        .addGroup(StatisticalPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(StatisticalPanelLayout.createParallelGroup()
                                .addGroup(StatisticalPanelLayout.createSequentialGroup()
                                    .addComponent(computeStatisticalButton)
                                    .addGap(0, 566, Short.MAX_VALUE))
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE))
                            .addContainerGap())
                );
                StatisticalPanelLayout.setVerticalGroup(
                    StatisticalPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, StatisticalPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(computeStatisticalButton)
                            .addContainerGap())
                );
            }
            TabbedPane.addTab("Statisticals", StatisticalPanel);
            TabbedPane.setEnabledAt(1, false);

            //======== panel1 ========
            {

                //---- plotDispersionButton ----
                plotDispersionButton.setText("Get Plot");
                plotDispersionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        plotDispersionButtonActionPerformed(e);
                    }
                });

                //======== panel2 ========
                {
                    panel2.setBorder(new LineBorder(Color.black, 1, true));
                    panel2.addPropertyChangeListener("visible", new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent e) {
                            panel2PropertyChange(e);
                        }
                    });

                    GroupLayout panel2Layout = new GroupLayout(panel2);
                    panel2.setLayout(panel2Layout);
                    panel2Layout.setHorizontalGroup(
                        panel2Layout.createParallelGroup()
                            .addGap(0, 735, Short.MAX_VALUE)
                    );
                    panel2Layout.setVerticalGroup(
                        panel2Layout.createParallelGroup()
                            .addGap(0, 412, Short.MAX_VALUE)
                    );
                }

                //---- label1 ----
                label1.setText("Standard Deviation:");

                //---- formattedTextField1 ----
                formattedTextField1.setEditable(false);
                formattedTextField1.setBackground(Color.white);

                //---- formattedTextField2 ----
                formattedTextField2.setEditable(false);
                formattedTextField2.setBackground(Color.white);

                //---- label2 ----
                label2.setText("Correlation:");

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(plotDispersionButton)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
                            .addComponent(label2)
                            .addGap(4, 4, 4)
                            .addComponent(formattedTextField2, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                            .addGap(48, 48, 48)
                            .addComponent(label1)
                            .addGap(4, 4, 4)
                            .addComponent(formattedTextField1, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                            .addGap(137, 137, 137))
                        .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                            .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(formattedTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label1)
                                    .addComponent(formattedTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label2))
                                .addComponent(plotDispersionButton))
                            .addContainerGap())
                );
            }
            TabbedPane.addTab("Dispersion Plot", panel1);
            TabbedPane.setEnabledAt(2, false);

            //======== panel3 ========
            {

                //======== panel4 ========
                {
                    panel4.setBorder(new LineBorder(Color.black, 1, true));
                    panel4.setPreferredSize(new Dimension(737, 418));

                    GroupLayout panel4Layout = new GroupLayout(panel4);
                    panel4.setLayout(panel4Layout);
                    panel4Layout.setHorizontalGroup(
                        panel4Layout.createParallelGroup()
                            .addGap(0, 735, Short.MAX_VALUE)
                    );
                    panel4Layout.setVerticalGroup(
                        panel4Layout.createParallelGroup()
                            .addGap(0, 416, Short.MAX_VALUE)
                    );
                }

                //---- plotDistributionButton ----
                plotDistributionButton.setText("Get Plot");
                plotDistributionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        plotDistributionButtonActionPerformed(e);
                    }
                });

                //---- checkBox1 ----
                checkBox1.setText("Experimental pI distribution");
                checkBox1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        checkBox1ActionPerformed(e);
                    }
                });

                //---- checkBox2 ----
                checkBox2.setText("Theoretical pI distribution");
                checkBox2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        checkBox2ActionPerformed(e);
                    }
                });

                GroupLayout panel3Layout = new GroupLayout(panel3);
                panel3.setLayout(panel3Layout);
                panel3Layout.setHorizontalGroup(
                    panel3Layout.createParallelGroup()
                        .addGroup(panel3Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(plotDistributionButton)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(checkBox1)
                            .addGap(72, 72, 72)
                            .addComponent(checkBox2)
                            .addGap(88, 88, 88))
                        .addComponent(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                panel3Layout.setVerticalGroup(
                    panel3Layout.createParallelGroup()
                        .addGroup(panel3Layout.createSequentialGroup()
                            .addComponent(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panel3Layout.createParallelGroup()
                                .addGroup(panel3Layout.createSequentialGroup()
                                    .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(checkBox2)
                                        .addComponent(checkBox1))
                                    .addGap(9, 9, 9))
                                .addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                                    .addComponent(plotDistributionButton)
                                    .addContainerGap())))
                );
            }
            TabbedPane.addTab("Distribution Plot", panel3);
            TabbedPane.setEnabledAt(3, false);

            //======== panel5 ========
            {

                //======== panel6 ========
                {
                    panel6.setBorder(LineBorder.createBlackLineBorder());
                    panel6.setForeground(Color.gray);
                    panel6.setAutoscrolls(true);

                    GroupLayout panel6Layout = new GroupLayout(panel6);
                    panel6.setLayout(panel6Layout);
                    panel6Layout.setHorizontalGroup(
                        panel6Layout.createParallelGroup()
                            .addGap(0, 735, Short.MAX_VALUE)
                    );
                    panel6Layout.setVerticalGroup(
                        panel6Layout.createParallelGroup()
                            .addGap(0, 418, Short.MAX_VALUE)
                    );
                }

                //---- plotFractionButton ----
                plotFractionButton.setText("Get Plot");
                plotFractionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        plotFractionButtonActionPerformed(e);
                    }
                });

                GroupLayout panel5Layout = new GroupLayout(panel5);
                panel5.setLayout(panel5Layout);
                panel5Layout.setHorizontalGroup(
                    panel5Layout.createParallelGroup()
                        .addComponent(panel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel5Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(plotFractionButton)
                            .addContainerGap(653, Short.MAX_VALUE))
                );
                panel5Layout.setVerticalGroup(
                    panel5Layout.createParallelGroup()
                        .addGroup(panel5Layout.createSequentialGroup()
                            .addComponent(panel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(plotFractionButton)
                            .addGap(11, 11, 11))
                );
            }
            TabbedPane.addTab("Fractions Plot", panel5);
            TabbedPane.setEnabledAt(4, false);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(TabbedPane)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(TabbedPane)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JMenuBar menuBar1;
    private JMenu fileMenu;
    private JMenu importMenu;
    private JMenu importTxtFileMenu;
    private JMenuItem importTSVMenuItem;
    private JMenuItem importCSVMenuItem;
    private JMenuItem importExcelMenuItem;
    private JMenuItem importMzidentmlMenuItem;
    private JMenu exportMenu;
    private JMenuItem exportCsvMenuItem;
    private JMenuItem exportExcelMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu processMenu;
    private JMenuItem methodSettingsMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JTabbedPane TabbedPane;
    private JPanel SequencePanel;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton computeTheoreticalPiButton;
    private JButton clearTableButton;
    private JPanel StatisticalPanel;
    private JButton computeStatisticalButton;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel1;
    private JButton plotDispersionButton;
    private JPanel panel2;
    private JLabel label1;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField formattedTextField2;
    private JLabel label2;
    private JPanel panel3;
    private JPanel panel4;
    private JButton plotDistributionButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JPanel panel5;
    private JPanel panel6;
    private JButton plotFractionButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void actionPerformed(ActionEvent event){}

    public void fillDataTable(DefaultTableModel dm, ArrayList<String> sequences, ArrayList<Double> isoelectricpoint){
        String seq;
        Double calcPI = 0.0;
        Double chargeAtPH = 0.0;
        int i=0;

        dm.getDataVector().removeAllElements();
        if(sequences.size()==isoelectricpoint.size()){
            for (Double expPI:isoelectricpoint){
                seq = sequences.get(i);
                Object[] tmpData = {seq, expPI, calcPI, chargeAtPH};
                dm.addRow(tmpData);
                i++;
            }
            Object[] last = null;
            dm.addRow(last);
        }
        else{
            System.out.println("ERROR: The peptide sequence array do not have the same size of isoelectric point array.");
        }
    }


    public void clearDataTable(DefaultTableModel dm){
        // This method clears the table filled after compute the Theorical Isoelectric Point
        this.computeTheoreticalPiButton.setEnabled(false);
        this.clearTableButton.setEnabled(false);
        table2.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                },
                new String[] {
                        "Sequence", "Experimental Isoelectric Point", "Calculated Isoelectric Point", "Charge at pH"
                })
                    {
                       Class<?>[] columnTypes = new Class<?>[] {
                         String.class, Double.class, Double.class, Double.class
                    };
                         boolean[] columnEditable = new boolean[] {
                    };
                     @Override
                       public Class<?> getColumnClass(int columnIndex) {
                       return columnTypes[columnIndex];
                     }
                       @Override
                         public boolean isCellEditable(int rowIndex, int columnIndex) {
                         //return columnEditable[columnIndex];
                         return false;
                       }
                });
                       table2.setCellSelectionEnabled(true);
                       scrollPane2.setViewportView(table2);


        }
    }



