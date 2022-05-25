package com.jeff_media.resourcepackmerger;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static <T> T[] removeLastElement(T[] array) {
        T[] shortArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
        System.arraycopy(array, 0, shortArray, 0, array.length - 1);
        return shortArray;
    }

    public static void throwIfNotAFile(File file) {
        if (!file.exists()) throw new RuntimeException(new FileNotFoundException(file.getAbsolutePath()));
    }

    public static File extractZip(File file) {
        try {
            File temp = getTempFolder();
            temp.mkdirs();
            if (!temp.exists()) {
                throw new RuntimeException("[Error 1] Could not create directory: " + temp.getAbsolutePath());
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

    public static File getTempFolder() {
        try {
            File temp = Files.createTempDirectory("resource-pack-merger-").toFile();
            ResourcePackMerger.TEMP_WORK_DIRECTORIES.add(temp);
            temp.deleteOnExit();
            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void print(Map<?, ?> map) {
        map.forEach((key, value) -> ResourcePackMerger.getLogger().debug(key + " -> " + value));
    }

    private static final String[] ENDINGS = {".json",".mcmeta"};
    public static boolean isJsonFile(File file) {
        try {
            new ObjectMapper().readValue(file, Map.class);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public static ArrayList<Image> getIcons() {
        ArrayList<Image> list = new ArrayList<>();
        for (String filename : new String[]{"logo_32_32.png", "logo_64_64.png"}) {
            try {
                Image image = ImageIO.read(ResourcePackMerger.class.getResourceAsStream("/" + filename));
                list.add(image);
            } catch (IOException ignored) {

            }
        }
        return list;
    }

    public static BufferedImage getLogo() {
        try {
            BufferedImage picture = ImageIO.read(ResourcePackMerger.class.getResourceAsStream("/logo_64_64.png"));
            return picture;
        } catch (IOException ignored) {
            throw new RuntimeException(ignored);
        }
    }

    public static void delete(File file) throws IOException {
        if (!file.exists()) return;
        if (!file.isDirectory()) {
            if (!file.delete()) {
                Files.delete(file.toPath());
            }
        } else {
            for (File child : file.listFiles()) {
                delete(child);
            }
            if (!file.delete()) {
                Files.delete(file.toPath());
            }
        }
    }

    public static File zipDirectory(File inputFolder, File zipFile, CompressionLevel level) {
        ZipParameters params = new ZipParameters();
        params.setIncludeRootFolder(false);
        params.setCompressionLevel(level);
        ZipFile zip = new ZipFile(zipFile);
        try {
            zip.addFolder(inputFolder, params);
        } catch (ZipException e) {
            e.printStackTrace();
            return null;
        }
        return zipFile;
    }

    public static String asZipFileName(String text) {
        if(text.toLowerCase(Locale.ROOT).endsWith(".zip")) return text;
        return text + ".zip";
    }

    public static String asFolderFileName(String text) {
        if(text.toLowerCase(Locale.ROOT).endsWith(".zip")) return text.substring(0,text.length() - ".zip".length());
        return text;
    }
}
