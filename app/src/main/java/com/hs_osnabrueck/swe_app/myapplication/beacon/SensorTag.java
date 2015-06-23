package com.hs_osnabrueck.swe_app.myapplication.beacon;

import java.util.UUID;

public class SensorTag {

    private UUID uuid;
    private String temperature;
    private String irTemperature;
    private String accelerometer;
    private String humidity;
    private String barometer;

    public SensorTag(String accelerometer, String barometer, String humidity, String irTemperature, String temperature, UUID uuid) {
        this.accelerometer = accelerometer;
        this.barometer = barometer;
        this.humidity = humidity;
        this.irTemperature = irTemperature;
        this.temperature = temperature;
        this.uuid = uuid;
    }
}
