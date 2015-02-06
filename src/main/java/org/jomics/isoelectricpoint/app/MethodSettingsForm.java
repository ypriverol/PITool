/*
 * Created by JFormDesigner on Thu Jan 29 09:27:12 PST 2015
 */

package org.jomics.isoelectricpoint.app;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * @author User #2
 */
public class MethodSettingsForm extends JDialog implements ActionListener{

    private boolean filledsettings = false; // Flag variable to know if the setting were filled properly

    private HashMap<String, Object> settings = new HashMap<String, Object>();

    public MethodSettingsForm(){
        initComponents();
        fillComboBoxes(0);
    }
    
    public MethodSettingsForm(HashMap<String, Object> parameters) {
        initComponents();
        settings.putAll(parameters);
        this.setSettings();
    }

    private void setSettings(){
        // This method set the properties of the components of the form
        fillComboBoxes(Integer.parseInt(settings.get("method").toString()));
        if (settings.containsKey("pkSetIndex")){
            pKSetComboBox.setSelectedIndex(Integer.parseInt(settings.get("pkSetIndex").toString()));
        } else if (settings.containsKey("pkSetElement")){
            pKSetComboBox.setSelectedItem(settings.get("pkSetElement"));
        }
        phFormattedTextField.setText(settings.get("ph").toString());

        if (settings.containsKey("Sigma") & settings.containsKey("CFactor") & settings.containsKey("TrainingData")){
            sigmaFormattedTextField.setText(settings.get("Sigma").toString());
            cfactorFormattedTextField.setText(settings.get("CFactor").toString());
            TrainingdataComboBox.setSelectedIndex(Integer.parseInt(settings.get("TrainingData").toString())); // TENGO DUDAS EN LO QUE TIENE QUE TENER ESTE COMBOBOX
        }
    }

    public HashMap<String, Object> getSettings() {
        return this.settings;
    }
    
    public boolean isFilledSettings(){
        return filledsettings;
    }

    private void fillSettings(int method){
        // This method fill the parameters for the method selected by user


        switch (method){
            case 0:
                if(phFormattedTextField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter a pH value for charge.");
                }else{
                    Double ph = 0.0;
                    try{
                        ph = Double.parseDouble(phFormattedTextField.getText());
                       }
                    catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Invalid number");
                      }

                    //check correct double value
                    if(ph < 0.0 || ph > 14.0){
                        JOptionPane.showMessageDialog(null, "Please, pH value for charge is out of range.");
                    } else{
                        settings.put("ph", Double.valueOf(phFormattedTextField.getText()));
                        settings.put("pkSetElement", pKSetComboBox.getSelectedItem().toString());
                        this.filledsettings = true;
                        setVisible(false);
                    }
                }

                settings.put("method", 0);
                break;

            case 1:
                if(phFormattedTextField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter a pH value for charge.");
                }else{

                    Double ph = 0.0;
                    try{
                        ph = Double.parseDouble(phFormattedTextField.getText());
                    }
                    catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Invalid number");
                    }

                    if(ph < 0.0 || ph > 14.0){
                        JOptionPane.showMessageDialog(null, "Please, pH value for charge is out of range.");
                    } else{
                        settings.put("ph", Double.valueOf(phFormattedTextField.getText()));
                        settings.put("pkSetElement", pKSetComboBox.getSelectedItem().toString());
                        this.filledsettings = true;
                        setVisible(false);
                    }
                }

                settings.put("method", 1);
                break;
            case 2:
                if(phFormattedTextField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter a pH value for charge.");
                }else{

                    Double ph = 0.0;
                    try{
                        ph = Double.parseDouble(phFormattedTextField.getText());
                    }
                    catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Invalid number");
                    }

                    if(ph < 0.0 || ph > 14.0){
                        JOptionPane.showMessageDialog(null, "Please, pH value for charge is out of range.");
                    } else{
                        settings.put("ph", Double.valueOf(phFormattedTextField.getText()));
                        this.filledsettings = true;
                        setVisible(false);
                    }
                }

                settings.put("method", 2);
                break;
            case 3:
                if(sigmaFormattedTextField.getText().equals("") || cfactorFormattedTextField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please, make sure to introduce the \"Sigma\" and \"C Factor\" values.");
                }else{

                    Double sigma = 0.0;
                    Double c_factor = 0.0;

                    try{
                        sigma = Double.parseDouble(sigmaFormattedTextField.getText());
                        c_factor = Double.parseDouble(cfactorFormattedTextField.getText());
                    }
                    catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Invalid number");
                    }

                    if((sigma < 0.01 || sigma > 1.0) || (c_factor <= 1.0 && c_factor >= 100.0)){
                        JOptionPane.showMessageDialog(null, "Please, make sure to introduce the valid \"Sigma\" and \"C Factor\" \n values.  Maybe, one of these values is out of range.");
                    } else{
                        settings.put("Sigma", Double.valueOf(sigmaFormattedTextField.getText()));
                        settings.put("CFactor", Double.valueOf(cfactorFormattedTextField.getText()));
                        this.filledsettings = true;
                        setVisible(false);
                    }
                }

                settings.put("method", 3);
        }
    }
    
    public void fillComboBoxes(int method){
        // This method fill the ComboBox with the pK sets used to compute the pI
        switch (method){
            case 0:
                pKSetComboBox.removeAllItems();
                TrainingdataComboBox.removeAllItems();

                //Here you should fill the pKSetComboBox with elements for the Iterative pI Method
                pKSetComboBox.addItem("Lehninger");
                pKSetComboBox.addItem("Rodewell");
                pKSetComboBox.addItem("Sillero");
                pKSetComboBox.addItem("Solomon");
                pKSetComboBox.addItem("EMBOSS");
                pKSetComboBox.addItem("Patrickios");
                pKSetComboBox.addItem("Richard");
                pKSetComboBox.addItem("Grimsley");
                pKSetComboBox.addItem("Toseland");
                pKSetComboBox.addItem("Thurlkill");
                settings.put("method", 0);
                break;

            case 1:
                pKSetComboBox.removeAllItems();
                TrainingdataComboBox.removeAllItems();

                //Here you should fill the pKSetComboBox with elements for the Bjell pI Method
                pKSetComboBox.addItem("Bjell");
                pKSetComboBox.addItem("Skoog");
                pKSetComboBox.addItem("Calibrated");
                pKSetComboBox.addItem("Expasy");
                settings.put("method", 1);
                break;
            case 2:
                pKSetComboBox.removeAllItems();
                TrainingdataComboBox.removeAllItems();

                //The Cofactor Adjacent pI Method doesn't use pkSet Value
                settings.put("method", 2);
                break;
            case 3:
                pKSetComboBox.removeAllItems();
                TrainingdataComboBox.removeAllItems();

                //Here you should fill the TrainingDataComboBox with elements
                TrainingdataComboBox.addItem("Default");
                settings.put("method", 3);
        }
    }

    public boolean isValidDigit(char vChar){
        //This method verifies if a character is digit.
        //This is useful during the user is entering the pkSet, Sigma and Cofactor parameters.
        boolean validdigit = true;
        if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE) || (vChar == KeyEvent.VK_PERIOD))) {
            validdigit = false;
        }
        return validdigit;
    }

    public void actionPerformed(ActionEvent event){}
    
    private void iteracRadioButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(this.iteracRadioButton.isSelected()){

            //Set inactivated the RadioButtons
            this.svmRadioButton.setSelected(false);
            this.bjellRadioButton.setSelected(false);
            this.cofactorRadioButton.setSelected(false);

            //Set enable the Parameters related with pI-SVM method
            this.pKSetLabel.setEnabled(true);
            this.pKSetComboBox.setEnabled(true);
            this.pHForChargeLabel.setEnabled(true);
            this.phFormattedTextField.setEnabled(true);
            this.SigmaLabel.setEnabled(false);
            this.sigmaFormattedTextField.setEnabled(false);
            this.sigmaFormattedTextField.setText("");
            this.CFactorLabel.setEnabled(false);
            this.cfactorFormattedTextField.setEnabled(false);
            this.cfactorFormattedTextField.setText("");
            this.AnotherParamLabel.setEnabled(false);
            this.TrainingdataComboBox.setEnabled(false);
        }
    }

    private void iteracRadioButtonItemStateChanged(ItemEvent event) {
        // TODO add your code here
        if (event.getStateChange() == ItemEvent.SELECTED) {
            fillComboBoxes(0);
        }
    }

    private void bjellRadioButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(this.bjellRadioButton.isSelected()){

            //Set inactivated the RadioButtons
            this.svmRadioButton.setSelected(false);
            this.iteracRadioButton.setSelected(false);
            this.cofactorRadioButton.setSelected(false);

            //Set enable the Parameters related with pI-SVM method
            this.pKSetLabel.setEnabled(true);
            this.pKSetComboBox.setEnabled(true);
            this.pHForChargeLabel.setEnabled(true);
            this.phFormattedTextField.setEnabled(true);
            this.SigmaLabel.setEnabled(false);
            this.sigmaFormattedTextField.setEnabled(false);
            this.sigmaFormattedTextField.setText("");
            this.CFactorLabel.setEnabled(false);
            this.cfactorFormattedTextField.setEnabled(false);
            this.cfactorFormattedTextField.setText("");
            this.AnotherParamLabel.setEnabled(false);
            this.TrainingdataComboBox.setEnabled(false);
        }
    }

    private void bjellRadioButtonItemStateChanged(ItemEvent event) {
        // TODO add your code here
        if (event.getStateChange() == ItemEvent.SELECTED) {
            fillComboBoxes(1);
        }
    }

    private void cofactorRadioButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(this.cofactorRadioButton.isSelected()){

            //Set inactivated the RadioButtons
            this.svmRadioButton.setSelected(false);
            this.bjellRadioButton.setSelected(false);
            this.iteracRadioButton.setSelected(false);

            //Set enable the Parameters related with pI-SVM method
            this.pKSetLabel.setEnabled(false);
            this.pKSetComboBox.setEnabled(false);
            this.pHForChargeLabel.setEnabled(true);
            this.phFormattedTextField.setEnabled(true);
            this.SigmaLabel.setEnabled(false);
            this.sigmaFormattedTextField.setEnabled(false);
            this.sigmaFormattedTextField.setText("");
            this.CFactorLabel.setEnabled(false);
            this.cfactorFormattedTextField.setEnabled(false);
            this.cfactorFormattedTextField.setText("");
            this.AnotherParamLabel.setEnabled(false);
            this.TrainingdataComboBox.setEnabled(false);
        }
    }

    private void cofactorRadioButtonItemStateChanged(ItemEvent event) {
        // TODO add your code here
        if (event.getStateChange() == ItemEvent.SELECTED) {
            fillComboBoxes(2);
        }
    }

    private void pKSetComboBoxItemStateChanged(ItemEvent e) {
        // TODO add your code here
        settings.put("pkSetIndex", (int) pKSetComboBox.getSelectedIndex());
        settings.put("pkSetElement", (String) pKSetComboBox.getSelectedItem());
    }

    private void sigmaFormattedTextFieldKeyTyped(KeyEvent event) {
        // TODO add your code here
        char vChar = event.getKeyChar();
        if (!this.isValidDigit(vChar)){
            event.consume();
        }
    }

    private void phFormattedTextFieldKeyTyped(KeyEvent event) {
        // TODO add your code here
        char vChar = event.getKeyChar();
        if (!this.isValidDigit(vChar)){
            event.consume();
        }
    }

    private void cfactorFormattedTextFieldKeyTyped(KeyEvent event) {
        // TODO add your code here
        char vChar = event.getKeyChar();
        if (!this.isValidDigit(vChar)){
            event.consume();
        }
    }

    private void svmRadioButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(this.svmRadioButton.isSelected()){

            //Set inactivated the RadioButtons
            this.iteracRadioButton.setSelected(false);
            this.bjellRadioButton.setSelected(false);
            this.cofactorRadioButton.setSelected(false);

            //Set enable the Parameters related with pI-SVM method
            this.pKSetLabel.setEnabled(false);
            this.pKSetComboBox.setEnabled(false);
            this.pHForChargeLabel.setEnabled(false);
            this.phFormattedTextField.setEnabled(false);
            this.phFormattedTextField.setText("");
            this.SigmaLabel.setEnabled(true);
            this.sigmaFormattedTextField.setEnabled(true);
            this.CFactorLabel.setEnabled(true);
            this.cfactorFormattedTextField.setEnabled(true);
            this.AnotherParamLabel.setEnabled(true);
            this.TrainingdataComboBox.setEnabled(true);
        }
    }

    private void svmRadioButtonItemStateChanged(ItemEvent event) {
        // TODO add your code here
        if (event.getStateChange() == ItemEvent.SELECTED) {
            fillComboBoxes(3);
        }
    }

    private void OkButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        boolean selectedSVM = true;
        if(this.iteracRadioButton.isSelected()){
            fillSettings(0);
            //this.setVisible(false);
        }else if(this.bjellRadioButton.isSelected()){
            fillSettings(1);
            //this.setVisible(false);
        }else if(this.cofactorRadioButton.isSelected()){
            fillSettings(2);
            //this.setVisible(false);
        }else{
            fillSettings(3);
            //this.setVisible(false);
        }
    }

    private void CancelButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        //this.dispose();
        //this.setAlwaysOnTop(false);
        this.setVisible(false);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        iteracRadioButton = new JRadioButton();
        bjellRadioButton = new JRadioButton();
        cofactorRadioButton = new JRadioButton();
        MethSettngsPanel = new JPanel();
        pKSetComboBox = new JComboBox();
        pKSetLabel = new JLabel();
        pHForChargeLabel = new JLabel();
        CFactorLabel = new JLabel();
        SigmaLabel = new JLabel();
        AnotherParamLabel = new JLabel();
        separator1 = new JSeparator();
        TrainingdataComboBox = new JComboBox();
        sigmaFormattedTextField = new JFormattedTextField();
        phFormattedTextField = new JFormattedTextField();
        cfactorFormattedTextField = new JFormattedTextField();
        svmRadioButton = new JRadioButton();
        OkButton = new JButton();
        CancelButton = new JButton();

        //======== this ========
        setModal(true);
        setTitle("Methods Settings");
        setResizable(false);
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Select method"));

            //---- iteracRadioButton ----
            iteracRadioButton.setText("Interactive pI.");
            iteracRadioButton.setSelected(true);
            iteracRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    iteracRadioButtonActionPerformed(e);
                }
            });
            iteracRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    iteracRadioButtonItemStateChanged(e);
                }
            });

            //---- bjellRadioButton ----
            bjellRadioButton.setText("Bjell");
            bjellRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bjellRadioButtonActionPerformed(e);
                }
            });
            bjellRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    bjellRadioButtonItemStateChanged(e);
                }
            });

            //---- cofactorRadioButton ----
            cofactorRadioButton.setText("Cofactor Adjacent pI.");
            cofactorRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cofactorRadioButtonActionPerformed(e);
                }
            });
            cofactorRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    cofactorRadioButtonItemStateChanged(e);
                }
            });

            //======== MethSettngsPanel ========
            {
                MethSettngsPanel.setBorder(new TitledBorder("Method Settings"));

                //---- pKSetComboBox ----
                pKSetComboBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        pKSetComboBoxItemStateChanged(e);
                    }
                });

                //---- pKSetLabel ----
                pKSetLabel.setText("pK Set");

                //---- pHForChargeLabel ----
                pHForChargeLabel.setText("Set pH");

                //---- CFactorLabel ----
                CFactorLabel.setText("C Factor");
                CFactorLabel.setEnabled(false);

                //---- SigmaLabel ----
                SigmaLabel.setText("Sigma");
                SigmaLabel.setEnabled(false);

                //---- AnotherParamLabel ----
                AnotherParamLabel.setText("Training Data");
                AnotherParamLabel.setEnabled(false);

                //---- sigmaFormattedTextField ----
                sigmaFormattedTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        sigmaFormattedTextFieldKeyTyped(e);
                    }
                });

                //---- phFormattedTextField ----
                phFormattedTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        phFormattedTextFieldKeyTyped(e);
                    }
                });

                //---- cfactorFormattedTextField ----
                cfactorFormattedTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        cfactorFormattedTextFieldKeyTyped(e);
                    }
                });

                GroupLayout MethSettngsPanelLayout = new GroupLayout(MethSettngsPanel);
                MethSettngsPanel.setLayout(MethSettngsPanelLayout);
                MethSettngsPanelLayout.setHorizontalGroup(
                    MethSettngsPanelLayout.createParallelGroup()
                        .addGroup(MethSettngsPanelLayout.createSequentialGroup()
                            .addGroup(MethSettngsPanelLayout.createParallelGroup()
                                .addGroup(MethSettngsPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(SigmaLabel)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sigmaFormattedTextField, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(CFactorLabel)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cfactorFormattedTextField, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                                    .addGap(22, 22, 22)
                                    .addComponent(AnotherParamLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(TrainingdataComboBox, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                                .addGroup(MethSettngsPanelLayout.createSequentialGroup()
                                    .addGap(60, 60, 60)
                                    .addComponent(pKSetLabel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                    .addGap(1, 1, 1)
                                    .addComponent(pKSetComboBox, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
                                    .addGap(32, 32, 32)
                                    .addComponent(pHForChargeLabel)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(phFormattedTextField, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(MethSettngsPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(separator1)))
                            .addContainerGap())
                );
                MethSettngsPanelLayout.setVerticalGroup(
                    MethSettngsPanelLayout.createParallelGroup()
                        .addGroup(MethSettngsPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(MethSettngsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(pKSetComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(pHForChargeLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addComponent(pKSetLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addComponent(phFormattedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(8, 8, 8)
                            .addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(MethSettngsPanelLayout.createParallelGroup()
                                .addGroup(MethSettngsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(SigmaLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CFactorLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sigmaFormattedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cfactorFormattedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(MethSettngsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(TrainingdataComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(AnotherParamLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            }

            //---- svmRadioButton ----
            svmRadioButton.setText("pI-SVM");
            svmRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    svmRadioButtonActionPerformed(e);
                }
            });
            svmRadioButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    svmRadioButtonItemStateChanged(e);
                }
            });

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(iteracRadioButton, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bjellRadioButton, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cofactorRadioButton, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(svmRadioButton)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(MethSettngsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(iteracRadioButton)
                            .addComponent(bjellRadioButton)
                            .addComponent(cofactorRadioButton)
                            .addComponent(svmRadioButton))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MethSettngsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
            );
        }

        //---- OkButton ----
        OkButton.setText("OK");
        OkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OkButtonActionPerformed(e);
            }
        });

        //---- CancelButton ----
        CancelButton.setText("CANCEL");
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CancelButtonActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createParallelGroup()
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGap(0, 12, Short.MAX_VALUE)
                        .addGroup(contentPaneLayout.createParallelGroup()
                            .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(344, 344, 344)
                                .addComponent(OkButton)
                                .addGap(18, 18, 18)
                                .addComponent(CancelButton)))
                        .addGap(0, 12, Short.MAX_VALUE)))
                .addGap(0, 539, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createParallelGroup()
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGap(0, 10, Short.MAX_VALUE)
                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(contentPaneLayout.createParallelGroup()
                            .addComponent(OkButton)
                            .addComponent(CancelButton))
                        .addGap(0, 10, Short.MAX_VALUE)))
                .addGap(0, 219, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JRadioButton iteracRadioButton;
    private JRadioButton bjellRadioButton;
    private JRadioButton cofactorRadioButton;
    private JPanel MethSettngsPanel;
    private JComboBox pKSetComboBox;
    private JLabel pKSetLabel;
    private JLabel pHForChargeLabel;
    private JLabel CFactorLabel;
    private JLabel SigmaLabel;
    private JLabel AnotherParamLabel;
    private JSeparator separator1;
    private JComboBox TrainingdataComboBox;
    private JFormattedTextField sigmaFormattedTextField;
    private JFormattedTextField phFormattedTextField;
    private JFormattedTextField cfactorFormattedTextField;
    private JRadioButton svmRadioButton;
    private JButton OkButton;
    private JButton CancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
