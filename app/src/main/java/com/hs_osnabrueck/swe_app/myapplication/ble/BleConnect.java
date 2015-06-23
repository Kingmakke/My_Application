package com.hs_osnabrueck.swe_app.myapplication.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.sensors.TiTemperatureSensor;

import java.util.UUID;

public class BleConnect {

    private TiTemperatureSensor temperatureSensor;
    private BluetoothGatt bluetoothGatt;
    private String temperature;
    private BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

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
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call
        }
    };

    public BleConnect(Context context, Beacon beacon) {
        bluetoothGatt = beacon.getBluetoothDevice().connectGatt(context, false, btleGattCallback);
        if(beacon.getName().contains("SensorTag")){
            sensorTag(bluetoothGatt);
        }else if(beacon.getName().contains("estimote")){
            estimote(bluetoothGatt);
        }
    }

    private void sensorTag(BluetoothGatt bluetoothGatt){
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(temperatureSensor.getUUID_SERVICE()));
        BluetoothGattCharacteristic config = service.getCharacteristic(UUID.fromString(temperatureSensor.getUUID_CONFIG()));
        byte[] data = config.getValue();
        temperature = data.toString();
    }

    private void estimote(BluetoothGatt bluetoothGatt){

    }

    public String getTemperature() {
        return temperature;
    }
}
