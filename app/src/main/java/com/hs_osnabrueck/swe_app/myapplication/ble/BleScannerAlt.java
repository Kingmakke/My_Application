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

public class BleScannerAlt {

    private TextView beaconinfo;
    private Beacon beacon;
    private BluetoothAdapter.LeScanCallback leScanCallback;
    private ScanCallback scanCallback;


    @SuppressLint("NewApi")
    public BleScannerAlt(final TextView beaconinfo, final Beacon beacon) {
        this.beaconinfo = beaconinfo;
        this.beacon = beacon;
        this.leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                Log.e("debug", "scanApi20-");
                bleSearch(device, rssi);
            }
        };
        this.scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Log.e("debug", "scanApi21+");
                bleSearch(result.getDevice(), result.getRssi());
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

    private void bleSearch(BluetoothDevice device, int rssi){
        this.beacon = new Beacon(device, rssi);
        if ((device.getName().contains("SensorTag") || device.getName().contains("estimote"))
                && ((beacon.getRssi() < rssi) || device.getAddress().compareTo(beacon.getId()) == 0) ) {

            beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());

            if (device.getName().contains("SensorTag")) {
                beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
            } else if (device.getName().contains("estimote")) {
                beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
            }
        }
        if(beacon.getBluetoothDevice() == null) {
            beaconinfo.setText("Es wurde kein Beacon gefunden");
            //beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
        }
    }

    public BluetoothAdapter.LeScanCallback getLeScanCallback(){
        return leScanCallback;
    }

    public ScanCallback getScanCallback(){
        return scanCallback;
    }

    public Beacon getBeacon() {
        return beacon;
    }
}
