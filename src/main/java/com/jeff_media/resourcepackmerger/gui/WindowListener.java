package com.jeff_media.resourcepackmerger.gui;

import java.awt.event.WindowEvent;

public class WindowListener implements java.awt.event.WindowListener {
    private final GUI gui;

    public WindowListener(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        gui.saveConfig();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        gui.saveConfig();
    }
}
