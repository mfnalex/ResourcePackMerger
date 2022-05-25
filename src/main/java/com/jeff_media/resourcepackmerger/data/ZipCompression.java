package com.jeff_media.resourcepackmerger.data;

import net.lingala.zip4j.model.enums.CompressionLevel;

public enum ZipCompression {

    NONE("None", "Level 0, no compression", CompressionLevel.NO_COMPRESSION),
    FASTEST("Fastest","Level 1 Deflate compression", CompressionLevel.FASTEST),
    FASTER("Faster","Level 2 Deflate compression", CompressionLevel.FASTER),
    FAST("Fast","Level 3 Deflate compression", CompressionLevel.FAST),
    MEDIUM_FAST("Medium Fast","Level 4 Deflate compression", CompressionLevel.MEDIUM_FAST),
    NORMAL("Normal","Level 5 Deflate compression", CompressionLevel.NORMAL),
    HIGHER("Higher","Level 6 Deflate compression", CompressionLevel.HIGHER),
    MAXIMUM("Maximum","Level 7 Deflate compression", CompressionLevel.MAXIMUM),
    PRE_ULTRA("Pre Ultra","Level 8 Deflate compression", CompressionLevel.PRE_ULTRA),
    ULTRA("Ultra", "Level 9 Deflate compression", CompressionLevel.ULTRA);

    private final String prettyName;
    private final String description;
    private final CompressionLevel level;

    ZipCompression(String prettyName, String description, CompressionLevel level) {
        this.prettyName = prettyName;
        this.description = description;
        this.level = level;
    }

    public String getNameInMenu() {
        return prettyName + " - " + description;
    }

    public CompressionLevel getLevel() {
        return level;
    }
}
