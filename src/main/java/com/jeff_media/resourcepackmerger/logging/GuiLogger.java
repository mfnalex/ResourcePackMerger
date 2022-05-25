package com.jeff_media.resourcepackmerger.logging;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiLogger implements Logger {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final ConsoleLogger LOGGER = new ConsoleLogger();
    private final JTextArea area;

    public GuiLogger(JTextArea area) {
        this.area = area;
    }

    private void log(String level, String text) {
        Date date = new Date();
        String message = String.format("[%s] [%s] %s%s", SIMPLE_DATE_FORMAT.format(date), level, text, System.lineSeparator());
        area.append(message);
        area.setCaretPosition(area.getText().length());
    }


    @Override
    public void info(String text) {
        log("INFO",text);
        LOGGER.info(text);
    }

    @Override
    public void debug(String text) {
        //log("DEBUG", text);
        LOGGER.debug(text);
    }

    @Override
    public void error(String text) {
        log("ERROR", text);
        LOGGER.error(text);
    }

    @Override
    public void warn(String text) {
        log("WARN", text);
        LOGGER.warn(text);
    }
}
