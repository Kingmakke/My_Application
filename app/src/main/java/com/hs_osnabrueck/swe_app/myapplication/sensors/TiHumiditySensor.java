package com.hs_osnabrueck.swe_app.myapplication.sensors;

/**
 *
 */
public class TiHumiditySensor {

    private static final String UUID_SERVICE = "f000aa20-0451-4000-b000-000000000000";
    private static final String UUID_DATA = "f000aa21-0451-4000-b000-000000000000";
    private static final String UUID_CONFIG = "f000aa22-0451-4000-b000-000000000000";

    /**
     *
     * @return
     */
    public static String getUUID_CONFIG() {
        return UUID_CONFIG;
    }

    /**
     *
     * @return
     */
    public static String getUUID_DATA() {
        return UUID_DATA;
    }

    /**
     *
     * @return
     */
    public static String getUUID_SERVICE() {
        return UUID_SERVICE;
    }
}
