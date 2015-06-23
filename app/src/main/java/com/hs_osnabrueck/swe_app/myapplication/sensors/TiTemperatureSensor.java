package com.hs_osnabrueck.swe_app.myapplication.sensors;

public class TiTemperatureSensor {

    private final String UUID_SERVICE = "f000aa00-0451-4000-b000-000000000000";
    private final String UUID_DATA = "f000aa01-0451-4000-b000-000000000000";
    private final String UUID_CONFIG = "f000aa02-0451-4000-b000-000000000000";

    public TiTemperatureSensor() {
    }

    public String getUUID_CONFIG() {
        return UUID_CONFIG;
    }

    public String getUUID_DATA() {
        return UUID_DATA;
    }

    public String getUUID_SERVICE() {
        return UUID_SERVICE;
    }
}
