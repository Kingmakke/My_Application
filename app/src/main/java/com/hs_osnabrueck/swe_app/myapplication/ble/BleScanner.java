package com.hs_osnabrueck.swe_app.myapplication.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.widget.ListView;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.MainActivity;
import com.hs_osnabrueck.swe_app.myapplication.adapter.DeviceListAdapter;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.interfaces.BleSearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to define a BLEScanner object
 */
public class BleScanner{

    private DeviceListAdapter adapter;
    private List<Beacon> beaconList = new ArrayList<>();
    private ListView deviceList;
    private TextView noDevice;
    private LeScanCallback leScanCallback;
    private ScanCallback scanCallback;
    private MainActivity main;

    public BleSearchResponse bleSearchResponse = null;

    /**
     * Constructor for the BLEScanner
     */
    @SuppressLint("NewApi")
    public BleScanner() {
        this.deviceList = deviceList;
        this.main = main;
        this.noDevice = noDevice;
        this.leScanCallback = new LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                bleSearch(device, rssi);
            }
        };
        this.scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
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

    /**
     * calls the Interfaces method with the same parameters
     * @param device the bluetooth device
     * @param rssi signal strength of the beacon
     */
    private void bleSearch(BluetoothDevice device, int rssi){
        bleSearchResponse.beaconFound(device, rssi);
    }

    /**
     * Getter for leScanCallback
     * @return leScanCallback
     */
    public LeScanCallback getLeScanCallback(){
        return leScanCallback;
    }

    /**
     * Getter for scanCallback
     * @return scanCallback
     */
    public ScanCallback getScanCallback(){
        return scanCallback;
    }

}
