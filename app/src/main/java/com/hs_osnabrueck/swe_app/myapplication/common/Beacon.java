package com.hs_osnabrueck.swe_app.myapplication.common;

import android.bluetooth.BluetoothDevice;

public class Beacon {

    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public Beacon(BluetoothDevice bluetoothDevice, int rssi){
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
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
