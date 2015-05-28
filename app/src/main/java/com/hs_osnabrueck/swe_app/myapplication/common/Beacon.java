package com.hs_osnabrueck.swe_app.myapplication.common;

public class Beacon {

    private String name;
    private String id;
    private int rssi;

    public Beacon(String name, String id, int rssi){
        this.name = name;
        this.id = id;
        this.rssi = rssi;
    }

    public Beacon(Beacon beacon){
        this.name = beacon.name;
        this.id = beacon.id;
        this.rssi = beacon.rssi;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
