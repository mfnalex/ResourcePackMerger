package com.jeff_media.resourcepackmerger.gui;

import com.jeff_media.resourcepackmerger.ResourcePackFileFilter;
import com.jeff_media.resourcepackmerger.ResourcePackMerger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AddFileAction implements ActionListener {

    private final GUI gui;

    public AddFileAction(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResourcePackMerger.getLogger().debug("AddFileAction#actionPerformed(ActionEvent)");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new ResourcePackFileFilter());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (gui.lastFile != null && gui.lastFile.exists() && gui.lastFile.isDirectory()) {
            fileChooser.setCurrentDirectory(gui.lastFile);
        }
        int returnValue = fileChooser.showOpenDialog(gui);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            gui.lastFile = file.getParentFile();
            gui.listFilesModel.addElement(file);
            gui.updateFileButtons();
            if (gui.listFiles.getSelectedIndex() == -1) {
                gui.listFiles.setSelectedIndex(0);
            }
        }
    }
}
