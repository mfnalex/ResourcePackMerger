package com.jeff_media.resourcepackmerger;

import com.jeff_media.resourcepackmerger.gui.GUI;
import com.jeff_media.resourcepackmerger.logging.ConsoleLogger;
import com.jeff_media.resourcepackmerger.logging.Logger;
import com.jeff_media.resourcepackmerger.mergers.DirectoryMerger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ResourcePackMerger {

    private static final Properties PROPERTIES = new Properties();
    public static final java.util.List<File> TEMP_WORK_DIRECTORIES = new ArrayList<>();

    public static Logger getLogger() {
        return logger;
    }
    private static Logger logger = new ConsoleLogger();

    public static void setLogger(Logger logger) {
        ResourcePackMerger.logger = logger;
    }

    public static void main(String[] args) {
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (args.length == 0) {
            GUI gui = new GUI();
            gui.setTitle("Resource Pack Merger");
            gui.setIconImages(Utils.getIcons());
//            gui.setMinimumSize(new Dimension(1800,900));
            gui.setVisible(true);
            return;
        }
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: <resourcepack1> <resourcepack2> ... <outputfile>");
        }

        String outputFileName = args[args.length - 1];
        String[] inputFileNames = Utils.removeLastElement(args);

        File outputFile = Paths.get(outputFileName).toFile();
        File[] inputFiles = Arrays.stream(inputFileNames).map(s -> {
            File file = new File(s);
            Utils.throwIfNotAFile(file);
            return new File(s);
        }).toArray(File[]::new);
        for (int i = 0; i < inputFiles.length; i++) {
            File file = inputFiles[i];
            if (file.getName().toLowerCase(Locale.ROOT).endsWith(".zip")) {
                inputFiles[i] = Utils.extractZip(file);
            }
        }

        DirectoryMerger merger = new DirectoryMerger(outputFile, inputFiles);
        merger.checkPrerequisites();
        if (merger.merge()) {
            logger.info("Merging complete! Result: " + outputFile.getAbsolutePath());
        } else {
            logger.error("Could not merge resource packs!");
        }
    }

    private static void loadProperties() throws IOException {
        PROPERTIES.load(ResourcePackMerger.class.getClassLoader().getResourceAsStream("project.properties"));
    }

    public static String getVersion() {
        return PROPERTIES.getProperty("version","<unknown>");
    }

    public static void deleteWorkDirectories() {
        ListIterator<File> it = TEMP_WORK_DIRECTORIES.listIterator();
        while(it.hasNext()) {
            File file = it.next();
            try {
                Utils.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file.exists()) {
                getLogger().warn("Could not delete work directory " + file.getAbsolutePath());
            } else {
                it.remove();
            }
        }
    }
}
