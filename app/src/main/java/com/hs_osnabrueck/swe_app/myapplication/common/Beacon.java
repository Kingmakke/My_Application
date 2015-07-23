package com.hs_osnabrueck.swe_app.myapplication.common;

import android.bluetooth.BluetoothDevice;

/**
 * class which defines how a beacon object looks like
 */
public class Beacon {

    private BluetoothDevice bluetoothDevice;
    private int rssi;
    private int counter;

    /**
     * constructor of the beacon
     * @param bluetoothDevice a specific beacon
     * @param rssi signal strength of the beacon
     */
    public Beacon(BluetoothDevice bluetoothDevice, int rssi){
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
        this.counter = 0;
    }

    /**
     * returns the signal strength
     * @return rssi
     */
    public int getRssi() {
        return rssi;
    }

    /**
     * sets the rssi
     * @param rssi the signal strength
     */
    public void setRssi(int rssi){
        this.rssi = rssi;
    }

    /**
     * returns the counter
     * @return counter
     */
    public int getCounter(){
        return counter;
    }

    /**
     * increases the counter by one
     */
    public void raiseCounter(){
        this.counter++;
    }

    /**
     * resets the counter to 0
     */
    public void resetCounter(){
        this.counter = 0;
    }

    /**
     * returns the device id
     * @return the bluetooth device address
     */
    public String getId() {
        return bluetoothDevice.getAddress();
    }

    /**
     * returns the device name
     * @return the bluetooteh device name
     */
    public String getName() {
        return bluetoothDevice.getName();
    }

    /**
     * gives the bluetooth device
     * @return the bluetooth device
     */
    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
}
