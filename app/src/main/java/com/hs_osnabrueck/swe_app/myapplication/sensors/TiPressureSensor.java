package com.hs_osnabrueck.swe_app.myapplication.sensors;

/**
 * SensorTag Pressure class
 */
public class TiPressureSensor{

    private static final String UUID_SERVICE = "f000aa40-0451-4000-b000-000000000000";
    private static final String UUID_DATA = "f000aa41-0451-4000-b000-000000000000";
    private static final String UUID_CONFIG = "f000aa42-0451-4000-b000-000000000000";
    private static final String UUID_BAR_CALI = "f000aa43-0451-4000-b000-000000000000";

    /**
     * getter for the Config UUID
     * @return UUID_CONFIG
     */
    public static String getUUID_CONFIG() {
        return UUID_CONFIG;
    }

    /**
     * getter for the Data UUID
     * @return UUID_DATA
     */
    public static String getUUID_DATA() {
        return UUID_DATA;
    }

    /**
     * getter for the Service UUID
     * @return UUID_SERVICE
     */
    public static String getUUID_SERVICE() {
        return UUID_SERVICE;
    }

    /**
     * getter for the Calibration UUID
     * @return UUID_BAR_CALI
     */
    public static String getUUID_BAR_CALI() {
        return UUID_BAR_CALI;
    }
}
