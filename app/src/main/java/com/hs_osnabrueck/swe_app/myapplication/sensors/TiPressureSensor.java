package com.hs_osnabrueck.swe_app.myapplication.sensors;

/**
 *
 */
public class TiPressureSensor{

    private static final String UUID_SERVICE = "f000aa40-0451-4000-b000-000000000000";
    private static final String UUID_DATA = "f000aa41-0451-4000-b000-000000000000";
    private static final String UUID_CONFIG = "f000aa42-0451-4000-b000-000000000000";
    private static final String UUID_BAR_CALI = "f000aa43-0451-4000-b000-000000000000";

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

    /**
     *
     * @return
     */
    public static String getUUID_BAR_CALI() {
        return UUID_BAR_CALI;
    }
}
