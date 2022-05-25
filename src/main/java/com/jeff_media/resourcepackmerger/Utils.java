package com.jeff_media.resourcepackmerger;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static <T> T[] removeLastElement(T[] array) {
        T[] shortArray = (T[]) Array.newInstance(array.getClass().getComponentType(),array.length-1);
        System.arraycopy(array,0,shortArray, 0, array.length-1);
        return shortArray;
    }

    public static void throwIfNotAFile(File file) {
        if(!file.exists()) throw new RuntimeException(new FileNotFoundException(file.getAbsolutePath()));
    }

    public static File extractZip(File file) {
        try {
            File temp = Files.createTempDirectory("reource-pack-merger").toFile();
            temp.mkdirs();
            if(!temp.exists()) {
                throw new RuntimeException("Could not create directory: " + temp.getAbsolutePath());
            }
            ResourcePackMerger.getLogger().info("Extracting ZIP File " + file.getAbsolutePath() + " to " + temp.getAbsolutePath());
            ZipFile zipFile = new ZipFile(file);
            zipFile.extractAll(temp.getAbsolutePath());
            temp.deleteOnExit();
            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void print(Map<?,?> map) {
        map.forEach((key, value) -> ResourcePackMerger.getLogger().debug(key + " -> " + value));
    }

    public static boolean isJsonFile(File file) {
        return file.getAbsolutePath().toLowerCase(Locale.ROOT).endsWith(".json");
    }
}
