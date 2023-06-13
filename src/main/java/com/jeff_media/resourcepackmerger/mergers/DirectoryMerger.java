package com.jeff_media.resourcepackmerger.mergers;

import com.jeff_media.resourcepackmerger.ResourcePackMerger;
import com.jeff_media.resourcepackmerger.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class DirectoryMerger {

    final File[] inputFiles;
    final File outputFile;

    public DirectoryMerger(File outputFile, File... inputFiles) {
        this.outputFile = outputFile;
        this.inputFiles = inputFiles;
    }

    public boolean merge() {
        if(!outputFile.isDirectory() && !outputFile.mkdirs()) {
            throw new RuntimeException("[Error 2] Could not create directory " + outputFile.getAbsolutePath());
        }
        for(File directory : inputFiles) {
            //try {
                //ResourcePackMerger.LOGGER.info("Copying " + directory.getAbsolutePath() + " to " + outputFile.getAbsolutePath());
                for(File file : Arrays.stream(directory.listFiles()).sorted((o1, o2) -> Boolean.compare(o1.isDirectory(),o2.isDirectory())).collect(Collectors.toList())) {
                    File newFile = new File(outputFile, file.getName());
                    if(file.isDirectory()) {
                        ResourcePackMerger.getLogger().info("Copying Directory " + file.getAbsolutePath() + " to " + newFile.getAbsolutePath());
                        newFile.mkdirs();
                        new DirectoryMerger(newFile, file).merge();
                    } else {
                        ResourcePackMerger.getLogger().info(" |- Copying File " + file.getAbsolutePath() + " to " + newFile.getAbsolutePath());
                        try {
                            if(!newFile.exists()) {
                                Files.copy(file.toPath(), newFile.toPath());
                            } else {
                                if(Utils.isJsonFile(newFile)) {
                                    ResourcePackMerger.getLogger().info("   |- File already exists, trying to merge JSON");
                                    JsonMerger.merge(newFile, file);
                                } else {
                                    ResourcePackMerger.getLogger().info("   |- File already exists, skipping");
                                }
                            }
                        }  (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            /*} catch (IOException e) {
                e.printStackTrace();
                return false;
            }*/
        }
        return true;
    }

    public void checkPrerequisites() throws RuntimeException {
        for(File file : inputFiles) {
            if(!file.isDirectory()) {
                throw new RuntimeException("Not a directory: " + file.getAbsolutePath());
            }
        }

        if(outputFile.exists()) {
            if(!outputFile.isDirectory()) {
                throw new RuntimeException("Not a directory: " + outputFile.getAbsolutePath());
            }
            /*if(!outputFile.mkdirs()) {
                throw new RuntimeException("[Error 3] Could not create directory: " + outputFile.getAbsolutePath());
            }*/
            if(Objects.requireNonNull(outputFile.list()).length > 0) {
                throw new RuntimeException("Directory is not empty: " + outputFile.getAbsolutePath());
            }
        }
    }
}
