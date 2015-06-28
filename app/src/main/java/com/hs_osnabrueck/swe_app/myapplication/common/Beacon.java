package com.hs_osnabrueck.swe_app.myapplication.common;

import android.bluetooth.BluetoothDevice;

public class Beacon {

    private BluetoothDevice bluetoothDevice;
    private int rssi;
    private int counter;

    public Beacon(BluetoothDevice bluetoothDevice, int rssi){
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
        this.counter = 0;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi){
        this.rssi = rssi;
    }

    public int getCounter(){
        return counter;
    }

    public void raiseCounter(){
        this.counter++;
    }

    public void resetCounter(){
        this.counter = 0;
    }

    public String getId() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
}
