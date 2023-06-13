package com.jeff_media.resourcepackmerger.data;

public enum ResourcePackVersion {
    MC_1_6(1, "1.6.1", "1.8.9"),
    MC_1_9(2, "1.9", "1.10.2"),
    MC_1_11(3,"1.11","1.12.2"),
    MC_1_13(4,"1.13","1.14.4"),
    MC_1_15(5,"1.15","1.16.1"),
    MC_1_16(6,"1.16.2","1.16.5"),
    MC_1_17(7,"1.17","1.17.1"),
    MC_1_18(8,"1.18","1.18.2"),
    MC_1_19(9,"1.19","1.19.2"),
    MC_1_19_22w42a(11,"22w42a","22w44a"),
    MC_1_19_3(12, "22w45a", "1.19.3"),
    MC_1_19_4(13, "1.19.4", "1.19.4"),
    MC_1_20_23w14a(14, "23w14a", "23w16a"),
    MC_1_20(15, "1.20", "1.20");

    private final int format;
    private final String nameInMenu;

    ResourcePackVersion(int format, String lowestVersion, String highestVersion) {
        this.format = format;

        String supportedVersions = lowestVersion.toString();
        if(!highestVersion.equals(lowestVersion)) supportedVersions += " - " + highestVersion.toString();
        this.nameInMenu = format + " (MC " + supportedVersions + ")";
    }

    public String getNameInMenu() {
        return nameInMenu;
    }

    public int getFormat() {
        return format;
    }

    public static ResourcePackVersion byFormat(int format) {
        for(ResourcePackVersion version : values()) {
            if(version.format == format) return version;
        }
        throw new IllegalArgumentException();
    }
}
