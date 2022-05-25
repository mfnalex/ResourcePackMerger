package com.jeff_media.resourcepackmerger.data;


import java.util.Objects;

/**
 * Provides version comparing methods
 */
public final class McVersion {

    private final Integer major, minor, patch;

    public McVersion(Integer major, Integer minor, Integer patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public McVersion(String string) {
        String[] split = string.split("\\.");
        this.major = Integer.parseInt(split[0]);
        this.minor = Integer.parseInt(split[1]);
        this.patch = split.length > 2 ? Integer.parseInt(split[2]) : 0;
    }

    /**
     * Returns the Major version of the MC version, e.g. 1 for 1.16.5
     *
     * @return Major version of the MC version
     */
    public int getMajor() {
        return major;
    }

    /**
     * Returns the Minor version of the MC version, e.g. 16 for 1.16.5
     *
     * @return Minor version of the MC version
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns the Patch version of the MC version, e.g. 5 for 1.16.5
     *
     * @return Patch version of the MC version
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Checks whether the currently running MC version is at least the given version. For example, isAtLeast(1.16.4) will return true for all versions including and above 1.16.4
     *
     * @param major Major version
     * @param minor Minor version
     * @param patch Patch version
     * @return true when the currently running MC version is at least the given version, otherwise false
     */
    public boolean isAtLeast(final int major, final int minor, final int patch) {
        if (major > getMajor()) {
            return false;
        }
        if (getMajor() > major) {
            return true;
        }
        if (minor > getMinor()) {
            return false;
        }
        if (getMinor() > minor) {
            return true;
        }
        return getPatch() >= patch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        McVersion mcVersion = (McVersion) o;
        return Objects.equals(major, mcVersion.major) && Objects.equals(minor, mcVersion.minor) && Objects.equals(patch, mcVersion.patch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    @Override
    public String toString() {
        if(patch == 0) {
            return major + "." + minor;
        } else {
            return major + "." + minor + "." + patch;
        }
    }
}

