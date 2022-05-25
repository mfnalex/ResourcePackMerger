/*
 * Created by JFormDesigner on Wed May 25 16:57:24 CEST 2022
 */

package com.jeff_media.resourcepackmerger.gui;

import com.jeff_media.resourcepackmerger.ResourcePackFileFilter;
import com.jeff_media.resourcepackmerger.ResourcePackMerger;
import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;
import com.jeff_media.resourcepackmerger.logging.GuiLogger;
import com.jeff_media.resourcepackmerger.mergers.McMetaMerger;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author unknown
 */
public class GUI extends JFrame {
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Alexander Majka
    private JLabel labelName;
    private JTextField fieldName;
    private JLabel labelFormat;
    private JComboBox<ResourcePackVersion> fieldFormat;
    private JLabel labelFiles;
    private JLabel labelOutputFile;
    private JLabel labelOutputFileValue;
    private JScrollPane scrollPanelListFiles;
    private JList<File> listFiles;
    private JPanel fileButtons;
    private JButton buttonAddFile;
    private JButton buttonMoveUp;
    private JButton buttonMoveDown;
    private JButton buttonDeleteFile;
    private JScrollPane scollPanelLog;
    private JTextArea log;
    private JButton buttonStart;
    private DefaultListModel<File> listFilesModel;
    private JProgressBar progressBar;
    private JButton buttonOutputFile;
    private File lastFile = null;

    public GUI() {
        initComponents();
        ResourcePackMerger.setLogger(new GuiLogger(log));
        initBehaviour();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Alexander Majka
        labelName = new JLabel();
        fieldName = new JTextField();
        labelFormat = new JLabel();
        fieldFormat = new JComboBox<>();
        labelFiles = new JLabel();
        labelOutputFile = new JLabel();
        labelOutputFileValue = new JLabel();
        scrollPanelListFiles = new JScrollPane();
        listFilesModel = new DefaultListModel<>();
        listFiles = new JList(listFilesModel);
        fileButtons = new JPanel();
        buttonAddFile = new JButton();
        buttonMoveUp = new JButton();
        buttonMoveDown = new JButton();
        buttonDeleteFile = new JButton();
        scollPanelLog = new JScrollPane();
        log = new JTextArea();
        buttonStart = new JButton();
        progressBar = new JProgressBar();
        buttonOutputFile = new JButton();
        //======== this ========
        int height = 0;
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("hidemode 3",
                // columns
                "[fill]" + "[fill]" + "[fill]",
                // rows
                "[]" + "[]" + "[]" + "[]" + "[]" + "[]" + "[]"));

        //---- labelName ----
        labelName.setText("Description");
        contentPane.add(labelName, "cell 0 " + height);

        //---- fieldName ----
        fieldName.setText("My Custom Resource Pack");
        contentPane.add(fieldName, "cell 1 " + height);

        height++;

        //---- labelFormat ----
        labelFormat.setText("Resource Pack Format");
        contentPane.add(labelFormat, "cell 0 " + height);
        contentPane.add(fieldFormat, "cell 1 " + height);

        height++;

        labelOutputFile.setText("Output file");
        contentPane.add(labelOutputFile, "cell 0 " + height);
        labelOutputFileValue.setText(new File("merged-resourcepack.zip").getAbsolutePath());
        contentPane.add(labelOutputFileValue, "cell 1 " + height);
        buttonOutputFile.setText("Change");
        contentPane.add(buttonOutputFile, "cell 2 " + height);

        height++;

        //---- labelFiles ----
        labelFiles.setText("Resource Packs");
        contentPane.add(labelFiles, "cell 0 " + height);

        //======== scrollPane1 ========
        {
            scrollPanelListFiles.setViewportView(listFiles);
        }
        contentPane.add(scrollPanelListFiles, "cell 1 " + height);

        //======== fileButtons ========

            fileButtons.setLayout(new MigLayout("hidemode 3",
                    // columns
                    "[fill]",
                    // rows
                    "[]" + "[]" + "[]" + "[]" + "[]"));

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
        
        contentPane.add(fileButtons, "cell 2 " + height);


        height++;

            scollPanelLog.setViewportView(log);

        contentPane.add(scollPanelLog, "cell 0 " + height +" 3 1,hmin 100");

        height++;

        contentPane.add(progressBar, "cell 0 " + height + " 3 1");

        height++;

        buttonStart.setText("Start");
        contentPane.add(buttonStart, "cell 0 " + height + " 3 1");
        pack();
        setLocationRelativeTo(getOwner());

        // JFormDesigner - End of component initialization  //GEN-END:initComponents

    }

    private void initBehaviour() {
        Arrays.stream(ResourcePackVersion.values()).forEachOrdered(version -> fieldFormat.addItem(version));
        fieldFormat.setRenderer(new ResourcePackVersionListRenderer());
        fieldFormat.setSelectedIndex(fieldFormat.getItemCount() - 1);


        Arrays.stream(ResourcePackVersion.values()).forEachOrdered(version -> fieldFormat.addItem(version));
        fieldFormat.setRenderer(new ResourcePackVersionListRenderer());
        fieldFormat.setSelectedIndex(fieldFormat.getItemCount() - 1);

        ResourcePackMerger.getLogger().info("Waiting for input. Add at least one resource pack, then click on \"Start\".");
        buttonAddFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new ResourcePackFileFilter());
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if(lastFile != null && lastFile.exists() && lastFile.isDirectory()) {
                fileChooser.setCurrentDirectory(lastFile);
            }
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                lastFile = file.getParentFile();
                listFilesModel.addElement(file);
                updateFileButtons();
                if(listFiles.getSelectedIndex() == -1) {
                    listFiles.setSelectedIndex(0);
                }
            }
        });
        listFiles.addListSelectionListener(__ -> updateFileButtons());
        buttonMoveDown.addActionListener(__ -> moveFileInList(1));
        buttonMoveUp.addActionListener(__ -> moveFileInList(-1));
        buttonDeleteFile.addActionListener(__ -> {
            listFilesModel.remove(listFiles.getSelectedIndex());
            updateFileButtons();
        });
        buttonOutputFile.addActionListener(__ -> {

            JFileChooser fileChooser = new JFileChooser() {
                @Override
                public void approveSelection() {
                    File f = getSelectedFile();
                    if (f.exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(this,
                                "The file exists, overwrite?", "Existing file",
                                JOptionPane.YES_NO_CANCEL_OPTION);
                        switch (result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                cancelSelection();
                                return;
                            default:
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };
            if(buttonOutputFile.getText() != null) {
                File output = new File(labelOutputFileValue.getText());
                File directory = output.getParentFile();
                if(!output.exists()) directory = output.getParentFile();
                if(directory.isDirectory()) {
                    fileChooser.setCurrentDirectory(directory);
                    fileChooser.setSelectedFile(output);
                }
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setFileFilter(new ResourcePackFileFilter());
                int result = fileChooser.showSaveDialog(this);
                if(result == JFileChooser.APPROVE_OPTION) {
                    File selected = fileChooser.getSelectedFile();
                    labelOutputFileValue.setText(selected.getAbsolutePath());
                }
            }
        });
        buttonStart.addActionListener(__ -> {
            progressBar.setIndeterminate(true);
            java.util.List<String> params = new ArrayList<>();
            Arrays.stream(listFilesModel.toArray()).map(object -> ((File)object).getAbsolutePath()).forEachOrdered(params::add);
            params.add(labelOutputFile.getText());
            new Thread(() -> {
                long start = System.currentTimeMillis();
                try {
                    ResourcePackMerger.main(params.toArray(new String[0]));
                    File mcMetaFile = new File(labelOutputFileValue.getText(),"pack.mcmeta");
                    if(mcMetaFile.exists()) {
                        ResourcePackMerger.getLogger().info("Adjusting pack.mcmeta file");
                        McMetaMerger.apply(mcMetaFile, fieldName.getText(), fieldFormat.getItemAt(fieldFormat.getSelectedIndex()));
                    }
                } catch (Throwable t) {
                    ResourcePackMerger.getLogger().error(t.getMessage());
                }
                progressBar.setIndeterminate(false);
                long end = System.currentTimeMillis();
                ResourcePackMerger.getLogger().info("Finished task in " + TimeUnit.SECONDS.convert(end - start, TimeUnit.MILLISECONDS) + " seconds.");
            }).start();

        });
        progressBar.setIndeterminate(false);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    }

    private void moveFileInList(int offset) {
        int selected = listFiles.getSelectedIndex();
        File current = listFilesModel.remove(selected);
        listFilesModel.add(selected + offset, current);
        listFiles.setSelectedIndex(selected + offset);
        updateFileButtons();
    }

    private void updateFileButtons() {
        boolean enabled = true;
        if (listFilesModel.isEmpty() || listFiles.getSelectedIndex() == -1) {
            enabled = false;
        }
        boolean moveUpAllowed = enabled;
        boolean moveDownAllowed = enabled;
        if(listFiles.getSelectedIndex() < 1) {
            moveUpAllowed = false;
        }
        if(listFiles.getSelectedIndex() >= listFilesModel.size() -1) {
            moveDownAllowed = false;
        }
        buttonDeleteFile.setEnabled(enabled);
        buttonMoveUp.setEnabled(moveUpAllowed);
        buttonMoveDown.setEnabled(moveDownAllowed);
        buttonStart.setEnabled(enabled);
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
