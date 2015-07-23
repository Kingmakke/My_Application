package com.hs_osnabrueck.swe_app.myapplication.sensors;

/**
 * SensorTag Accelerometer class
 */
public class TiAccelerometerSensor {

    private static final String UUID_SERVICE = "f000aa10-0451-4000-b000-000000000000";
    private static final String UUID_DATA = "f000aa11-0451-4000-b000-000000000000";
    private static final String UUID_CONFIG = "f000aa12-0451-4000-b000-000000000000";
    private static final String UUID_PERIOD = "f000aa13-0451-4000-b000-000000000000";

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
     * getter for the Period UUID
     * @return UUID_PERIOD
     */
    public static String getUUID_PERIOD() {
        return UUID_PERIOD;
    }

    /**
     * getter for the Service UUID
     * @return UUID_SERVICE
     */
    public static String getUUID_SERVICE() {
        return UUID_SERVICE;
    }
}
