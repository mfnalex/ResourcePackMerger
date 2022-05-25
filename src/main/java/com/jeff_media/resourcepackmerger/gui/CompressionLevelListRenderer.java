package com.jeff_media.resourcepackmerger.gui;

import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;
import com.jeff_media.resourcepackmerger.data.ZipCompression;
import net.lingala.zip4j.model.enums.CompressionLevel;

import javax.swing.*;
import java.awt.*;

public class CompressionLevelListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof ZipCompression) {
            value = ((ZipCompression)value).getNameInMenu();
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
