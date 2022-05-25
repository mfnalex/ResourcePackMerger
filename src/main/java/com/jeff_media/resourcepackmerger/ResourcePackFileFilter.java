package com.jeff_media.resourcepackmerger;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Locale;

public class ResourcePackFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || (f.isFile() && f.getAbsolutePath().toLowerCase(Locale.ROOT).endsWith(".zip"));
    }

    @Override
    public String getDescription() {
        return ".zip files and directories";
    }
}
