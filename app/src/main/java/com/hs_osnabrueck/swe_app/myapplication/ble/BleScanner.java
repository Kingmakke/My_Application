package com.hs_osnabrueck.swe_app.myapplication.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.R;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

import java.util.List;

public class BleScanner{

    private Beacon beacon;
    private TextView beaconinfo;

    public BleScanner(final TextView beaconinfo, final Beacon beacon) {
        this.beaconinfo = beaconinfo;
        this.beacon = beacon;
    }

/*
    if(beacon.getName().contains("SensorTag")) {
        beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
        beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
    }else if(beacon.getName().contains("estimote")){

    }else{

    }*/

    public BluetoothAdapter.LeScanCallback getLeScanCallback() {
        return new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                Log.e("debug", "scanApi20-");
                if ((device.getName().contains("SensorTag") || device.getName().contains("estimote"))
                        && (beacon.getRssi() < rssi || device.getAddress().compareTo(beacon.getId()) == 0) ) {

                        beacon = new Beacon(device, rssi);
                        beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());

                        if (device.getName().contains("SensorTag")) {
                            beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
                        } else if (device.getName().contains("estimote")) {
                            beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
                        }

                }else if(beacon.getBluetoothDevice() == null) {
                    beaconinfo.setText("Es wurde kein Beacon gefunden");
                    //beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
                }
            }
        };
    }

    @SuppressLint("NewApi")
    public ScanCallback getScanCallback(){
        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Log.e("debug", "scanApi21+");
                if ((result.getDevice().getName().contains("SensorTag") || result.getDevice().getName().contains("estimote"))
                        && (beacon.getRssi() < result.getRssi() || result.getDevice().getAddress().compareTo(beacon.getId()) == 0) ) {

                    beacon = new Beacon(result.getDevice(), result.getRssi());
                    beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());

                    if (result.getDevice().getName().contains("SensorTag")) {
                        beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
                    } else if (result.getDevice().getName().contains("estimote")) {
                        beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
                    }

                }else if(beacon.getBluetoothDevice() == null) {
                    beaconinfo.setText("Es wurde kein Beacon gefunden");
                    //beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };
    }

    public Beacon getBeacon() {
        return beacon;
    }
}
