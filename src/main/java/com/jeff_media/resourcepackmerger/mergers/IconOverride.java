package com.jeff_media.resourcepackmerger.mergers;

import com.jeff_media.resourcepackmerger.ResourcePackMerger;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class IconOverride {

    public static void apply(String iconPath, File targetFolder) {
        if(iconPath == null || iconPath.isEmpty()) return;

        File icon = new File(iconPath);
        if(!icon.exists()) {
            ResourcePackMerger.getLogger().warn("Icon file "+iconPath+" does not exist.");
            return;
        }

        File targetFile = new File(targetFolder, "pack.png");
        if(targetFile.exists()) {
            targetFile.delete();
        }
        try {
            FileUtils.copyFile(icon, targetFile);
        } catch (Exception e) {
            ResourcePackMerger.getLogger().error("Error while copying icon file to target folder: ",e);
        }
    }

}
