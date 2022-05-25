package com.jeff_media.resourcepackmerger;

import com.jeff_media.resourcepackmerger.gui.GUI;
import com.jeff_media.resourcepackmerger.logging.ConsoleLogger;
import com.jeff_media.resourcepackmerger.mergers.DirectoryMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

public class ResourcePackMerger {

    private static com.jeff_media.resourcepackmerger.logging.Logger logger = new ConsoleLogger();

    public static com.jeff_media.resourcepackmerger.logging.Logger getLogger() {
        return logger;
    }

    public static void setLogger(com.jeff_media.resourcepackmerger.logging.Logger logger) {
        ResourcePackMerger.logger = logger;
    }

    public static void main(String[] args) {
        if(args.length == 0) {
            GUI gui = new GUI();
            gui.setVisible(true);
            return;
        }
        if(args.length < 3) {
            throw new IllegalArgumentException("Usage: <resourcepack1> <resourcepack2> ... <outputfile>");
        }

        String outputFileName = args[args.length-1];
        String[] inputFileNames = Utils.removeLastElement(args);

        File outputFile = new File(outputFileName);
        File[] inputFiles = Arrays.stream(inputFileNames).map(s -> {
            File file = new File(s);
            Utils.throwIfNotAFile(file);
            return new File(s);
        }).toArray(File[]::new);
        for(int i = 0; i < inputFiles.length; i++) {
            File file = inputFiles[i];
            if(file.getName().toLowerCase(Locale.ROOT).endsWith(".zip")) {
                inputFiles[i] = Utils.extractZip(file);
            }
        }

        DirectoryMerger merger = new DirectoryMerger(outputFile, inputFiles);
        merger.checkPrerequisites();
        if(merger.merge()) {
            logger.info("Merging complete! Result: " + outputFile.getAbsolutePath());
        } else {
            logger.error("Could not merge resource packs!");
        }
    }
}
