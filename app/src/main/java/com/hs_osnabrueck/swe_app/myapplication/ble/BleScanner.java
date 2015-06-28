package com.hs_osnabrueck.swe_app.myapplication.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.MainActivity;
import com.hs_osnabrueck.swe_app.myapplication.adapter.DeviceListAdapter;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

import java.util.ArrayList;
import java.util.List;

public class BleScanner{

    private DeviceListAdapter adapter;
    private List<Beacon> beaconList = new ArrayList<>();
    private ListView deviceList;
    private TextView noDevice;
    private LeScanCallback leScanCallback;
    private ScanCallback scanCallback;
    private MainActivity main;


    @SuppressLint("NewApi")
    public BleScanner(final ListView deviceList, MainActivity main, final TextView noDevice) {
        this.deviceList = deviceList;
        this.main = main;
        this.noDevice = noDevice;
        this.leScanCallback = new LeScanCallback() {
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
        Boolean update = false;
        for(int i = 0; i < beaconList.size(); i++){
            beaconList.get(i).raiseCounter();

            if(beaconList.get(i).getBluetoothDevice().getAddress().equals(device.getAddress())){
                beaconList.get(i).setRssi(rssi);
                beaconList.get(i).resetCounter();
                update = true;
                continue;
            }
            if(beaconList.get(i).getCounter() > 10){
                beaconList.remove(i);
                i--;
                continue;
            }

        }
        if(!update){
            beaconList.add(new Beacon(device, rssi));
        }
        adapter = new DeviceListAdapter(beaconList, main);
        deviceList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(beaconList.isEmpty()){
            noDevice.setVisibility(View.VISIBLE);
        }else{
            noDevice.setVisibility(View.INVISIBLE);
        }

    }

    public LeScanCallback getLeScanCallback(){
        return leScanCallback;
    }

    public ScanCallback getScanCallback(){
        return scanCallback;
    }

}
