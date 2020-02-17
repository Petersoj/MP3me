package net.jacobpeterson.util;

import java.io.File;

public class OSUtil {

    private static OS os = OS.UNKNOWN;

    static {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            os = OS.MAC;
        } else if (osName.contains("windows")) {
            os = OS.WINDOWS;
        } else if (osName.contains("linux")) {
            os = OS.LINUX;
        }
    }

    public static File getApplicationDataDirectory() {
        if (os == OS.MAC) {
            return new File(System.getProperty("user.home"), "Library/Application Support/");
        } else if (os == OS.WINDOWS) {
            return new File(System.getenv("APPDATA"));
        } else {
            return new File(System.getProperty("user.home"));
        }
    }

    public static OS getOS() {
        return os;
    }

    enum OS {
        MAC,
        WINDOWS,
        LINUX,
        UNKNOWN
    }
}
