package com.hs_osnabrueck.swe_app.myapplication.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

public class BleScanner2 implements BluetoothAdapter.LeScanCallback{

    private BluetoothAdapter bluetoothAdapter = null;

    private Beacon beacon = null;

    public BleScanner2(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if(device.getName().contains("SensorTag") || device.getName().contains("estimote")){
            if(beacon == null){
                beacon = new Beacon(device, rssi);
            }else if(beacon.getRssi() < rssi){
                beacon = new Beacon(device, rssi);
            }
        }
    }

    public Beacon getBeacon() {
        return beacon;
    }
}
