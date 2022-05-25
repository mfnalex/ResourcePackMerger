package com.jeff_media.resourcepackmerger.gui;

import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;

import javax.swing.*;
import java.awt.*;

public class ResourcePackVersionListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof ResourcePackVersion) {
            value = ((ResourcePackVersion)value).getNameInMenu();
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
