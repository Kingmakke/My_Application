package com.hs_osnabrueck.swe_app.myapplication.interfaces;


import android.bluetooth.BluetoothDevice;

public interface BleSearchResponse {
    void beaconFound(BluetoothDevice device, int rssi);
}
