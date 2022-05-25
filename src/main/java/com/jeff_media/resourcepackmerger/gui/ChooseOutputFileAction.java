package com.jeff_media.resourcepackmerger.gui;

import com.jeff_media.resourcepackmerger.ResourcePackFileFilter;
import com.jeff_media.resourcepackmerger.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ChooseOutputFileAction implements ActionListener {
    private final GUI gui;

    public ChooseOutputFileAction(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        /*JFileChooser fileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
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
        };*/
        JFileChooser fileChooser = new JFileChooser();
        if (gui.buttonOutputFile.getText() != null) {
            File output = new File(gui.labelOutputFileValue.getText());
            File directory = output.getParentFile();
            if (!output.exists()) directory = output.getParentFile();
            if (directory.isDirectory()) {
                fileChooser.setCurrentDirectory(directory);
                fileChooser.setSelectedFile(output);
            }
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setFileFilter(new ResourcePackFileFilter());
            int result = fileChooser.showSaveDialog(gui);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = fileChooser.getSelectedFile();
                gui.labelOutputFileValue.setText(Utils.asZipFileName(selected.getAbsolutePath()));
            }
        }

    }
}
