package com.jeff_media.resourcepackmerger.logging;

import org.slf4j.LoggerFactory;

public class ConsoleLogger implements Logger {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConsoleLogger.class);


    @Override
    public void info(String text) {
        logger.info(text);
    }

    @Override
    public void debug(String text) {
        logger.debug(text);
    }

    @Override
    public void error(String text) {
        logger.error(text);
    }

    @Override
    public void warn(String text) {
        logger.warn(text);
    }

    @Override
    public void error(String text, Throwable t) {
        logger.error(text, t);
    }
}
