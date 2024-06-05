package com.jeff_media.resourcepackmerger.logging;

public interface Logger {
    void info(String text);
    void debug(String text);
    void error(String text);
    void warn(String text);
    void error(String text, Throwable t);
}
