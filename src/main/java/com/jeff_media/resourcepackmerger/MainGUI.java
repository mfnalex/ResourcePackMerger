/*
 * Created by JFormDesigner on Wed May 25 16:57:24 CEST 2022
 */

package com.jeff_media.resourcepackmerger;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;
import com.jeff_media.resourcepackmerger.gui.ResourcePackVersionListRenderer;
import com.jeff_media.resourcepackmerger.logging.GuiLogger;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class MainGUI extends JFrame {
    public MainGUI() {
        initComponents();
    }

    public JComboBox getFieldFormat() {
        return fieldFormat;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Alexander Majka
        labelName = new JLabel();
        fieldName = new JTextField();
        labelFormat = new JLabel();
        fieldFormat = new JComboBox();
        labelFiles = new JLabel();
        scrollPane1 = new JScrollPane();
        listFiles = new JList();
        fileButtons = new JPanel();
        buttonAddFile = new JButton();
        buttonMoveUp = new JButton();
        buttonMoveDown = new JButton();
        buttonDeleteFile = new JButton();
        scrollPane2 = new JScrollPane();
        textArea1 = new JTextArea();
        buttonStart = new JButton();
        progressBar1 = new JProgressBar();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- labelName ----
        labelName.setText("Description");
        contentPane.add(labelName, "cell 0 0");

        //---- fieldName ----
        fieldName.setText("My Custom Resource Pack");
        contentPane.add(fieldName, "cell 1 0");

        //---- labelFormat ----
        labelFormat.setText("Resource Pack Format");
        contentPane.add(labelFormat, "cell 0 1");
        contentPane.add(fieldFormat, "cell 1 1");

        //---- labelFiles ----
        labelFiles.setText("Resource Packs");
        contentPane.add(labelFiles, "cell 0 2");

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(listFiles);
        }
        contentPane.add(scrollPane1, "cell 1 2");

        //======== fileButtons ========
        {
            fileButtons.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing.
            border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e", javax. swing. border. TitledBorder. CENTER
            , javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069al\u006fg" ,java .awt .Font
            .BOLD ,12 ), java. awt. Color. red) ,fileButtons. getBorder( )) ); fileButtons. addPropertyChangeListener (
            new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062or\u0064er"
            .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
            fileButtons.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- buttonAddFile ----
            buttonAddFile.setText("Add...");
            fileButtons.add(buttonAddFile, "cell 0 0");

            //---- buttonMoveUp ----
            buttonMoveUp.setText("Move Up");
            buttonMoveUp.setEnabled(false);
            fileButtons.add(buttonMoveUp, "cell 0 1");

            //---- buttonMoveDown ----
            buttonMoveDown.setText("Move Down");
            buttonMoveDown.setEnabled(false);
            fileButtons.add(buttonMoveDown, "cell 0 2");

            //---- buttonDeleteFile ----
            buttonDeleteFile.setText("Delete");
            buttonDeleteFile.setEnabled(false);
            fileButtons.add(buttonDeleteFile, "cell 0 3");
        }
        contentPane.add(fileButtons, "cell 2 2");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(textArea1);
        }
        contentPane.add(scrollPane2, "cell 0 3 3 1,hmin 30");

        //---- buttonStart ----
        buttonStart.setText("Start");
        contentPane.add(buttonStart, "cell 0 4 3 1");
        contentPane.add(progressBar1, "cell 0 5 3 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        Arrays.stream(ResourcePackVersion.values()).forEachOrdered(version -> fieldFormat.addItem(version));
        fieldFormat.setRenderer(new ResourcePackVersionListRenderer());
        fieldFormat.setSelectedIndex(fieldFormat.getItemCount()-1);

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Alexander Majka
    private JLabel labelName;
    private JTextField fieldName;
    private JLabel labelFormat;
    private JComboBox fieldFormat;
    private JLabel labelFiles;
    private JScrollPane scrollPane1;
    private JList listFiles;
    private JPanel fileButtons;
    private JButton buttonAddFile;
    private JButton buttonMoveUp;
    private JButton buttonMoveDown;
    private JButton buttonDeleteFile;
    private JScrollPane scrollPane2;
    private JTextArea textArea1;
    private JButton buttonStart;
    private JProgressBar progressBar1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
