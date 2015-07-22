package com.hs_osnabrueck.swe_app.myapplication.interfaces;


import android.bluetooth.BluetoothDevice;

/**
 * Interface for the BLE search
 */
public interface BleSearchResponse {
    /**
     *
     * @param device the bluetooth device
     * @param rssi signal strength of the beacon
     */
    void beaconFound(BluetoothDevice device, int rssi);
}
