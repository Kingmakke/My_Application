package com.hs_osnabrueck.swe_app.myapplication.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.R;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

public class BleScanner2{

    private BluetoothAdapter bluetoothAdapter = null;

    private Beacon beacon;
    private TextView beaconinfo;

    public BleScanner2(final TextView beaconinfo, final Beacon beacon) {
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

                //Log.e("debug","lescan");
                if ((device.getName().contains("SensorTag") || device.getName().contains("estimote")) && (beacon.getRssi() < rssi || device.getAddress().compareTo(beacon.getId()) == 0) ) {

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
    public Beacon getBeacon() {
        return beacon;
    }
}
