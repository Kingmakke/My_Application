package com.hs_osnabrueck.swe_app.myapplication.beacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.hs_osnabrueck.swe_app.myapplication.ble.BleScanner;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

public class BeaconSearch {

    private BluetoothAdapter.LeScanCallback leScanCallback;
    //private BluetoothGattCallback btleGattCallback = null;
    private BleScanner scanner;
    private Beacon beacon;

    public BeaconSearch(){

        leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

                if( (beacon != null) && (rssi < beacon.getRssi()) ){
                    beacon = new Beacon(device.getName(), device.getAddress(), rssi);
                }else{
                    beacon = new Beacon(device.getName(), device.getAddress(), rssi);
                }


            }
        };
        beacon = new Beacon("","",0);
/*
        btleGattCallback = new BluetoothGattCallback() {

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                // this will get called anytime you perform a read or write characteristic operation
            }

            @Override
            public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
                // this will get called when a device connects or disconnects
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
                // this will get called after the client initiates a            BluetoothGatt.discoverServices() call
            }
        };*/


    }

    public BluetoothAdapter.LeScanCallback getLeScanCallback() {
        return leScanCallback;
    }

    public Beacon getBeacon() {
        return beacon;
    }

/*
    scan = (Button)rootView.findViewById(R.id.scan);
    scan.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View v) {
            btAdapter.startLeScan(leScanCallback);
        }
    });

    stop = (Button)rootView.findViewById(R.id.stop);
    stop.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View v) {
            btAdapter.stopLeScan(leScanCallback);
        }
    });*/

}
