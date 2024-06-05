package com.jeff_media.resourcepackmerger.gui;

import com.jeff_media.resourcepackmerger.ResourcePackMerger;
import com.jeff_media.resourcepackmerger.Utils;
import com.jeff_media.resourcepackmerger.data.ZipCompression;
import com.jeff_media.resourcepackmerger.mergers.IconOverride;
import com.jeff_media.resourcepackmerger.mergers.McMetaMerger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class StartButtonAction implements ActionListener {

    private final GUI gui;

    public StartButtonAction(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Path zipFilePath = Paths.get(Utils.asZipFileName(gui.labelOutputFileValue.getText()));
        File zipFile = zipFilePath.toFile();
        String zipFilePathName = zipFile.getAbsolutePath();
        File tempFolder = Utils.getTempFolder();

        if(gui.listFiles.getModel().getSize() == 0) {
            ResourcePackMerger.getLogger().error("Please add at least one resource pack file.");
            return;
        }


        boolean deleteOld = false;
        if (zipFile.exists()) {
            int result = JOptionPane.showConfirmDialog(gui, "The file " + zipFilePathName + " already exists. Do you want to overwrite it?", "Existing file", JOptionPane.YES_NO_OPTION);
            switch (result) {
                case JOptionPane.YES_OPTION:
                    deleteOld = true;
                    break;
                case JOptionPane.NO_OPTION:
                    return;
            }
        }

        gui.progressBar.setIndeterminate(true);
        java.util.List<String> params = new ArrayList<>();
        Arrays.stream(gui.listFilesModel.toArray()).map(object -> ((File) object).getAbsolutePath()).forEachOrdered(params::add);
        params.add(tempFolder.getAbsolutePath());
        boolean finalDeleteOld = deleteOld;
        new Thread(() -> {
            long start = System.currentTimeMillis();

            if(finalDeleteOld) {
                try {
                    ResourcePackMerger.getLogger().info("Deleting old file " + zipFilePathName);
                    Utils.delete(zipFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (zipFile.exists()) {
                    ResourcePackMerger.getLogger().error("Could not delete existing file " + zipFilePathName + ". Please delete it manually or choose another output location.");
                    return;
                }
            }


            try {
                ResourcePackMerger.main(params.toArray(new String[0]));
                File mcMetaFile = new File(tempFolder, "pack.mcmeta");
                if (mcMetaFile.exists()) {
                    ResourcePackMerger.getLogger().info("Adjusting pack.mcmeta file");
                    McMetaMerger.apply(mcMetaFile, gui.fieldName.getText(), gui.fieldFormat.getItemAt(gui.fieldFormat.getSelectedIndex()));
                }

                if(!gui.labelIconValue.getText().isEmpty() && !gui.labelIconValue.getText().equals(GUI.NO_FILE_SELECTED)) {
                    ResourcePackMerger.getLogger().info("Applying icon override");
                    IconOverride.apply(gui.labelIconValue.getText(), tempFolder);
                }

            } catch (Throwable t) {
                ResourcePackMerger.getLogger().error(t.getMessage());
                return;
            }

                ResourcePackMerger.getLogger().info("Zipping resource pack (Deflate Level " + (((ZipCompression)gui.fieldCompression.getSelectedItem()).getLevel().getLevel()) + ")");
                Utils.zipDirectory(tempFolder, zipFile, (((ZipCompression)gui.fieldCompression.getSelectedItem()).getLevel()));
                ResourcePackMerger.getLogger().info("Deleting work directories");
                ResourcePackMerger.deleteWorkDirectories();


            gui.progressBar.setIndeterminate(false);
            long end = System.currentTimeMillis();
            ResourcePackMerger.getLogger().info("Finished task in " + TimeUnit.SECONDS.convert(end - start, TimeUnit.MILLISECONDS) + " seconds.");
        }).start();

    }
}
