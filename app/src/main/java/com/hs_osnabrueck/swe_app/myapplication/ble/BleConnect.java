package com.hs_osnabrueck.swe_app.myapplication.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.sensors.TiTemperatureSensor;

import java.util.List;

public class BleConnect {

    private TiTemperatureSensor temperatureSensor = new TiTemperatureSensor();
    private BluetoothGatt bluetoothGatt;
    private String temperature;
    private BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            Log.e("debug","onCharacteristicChanged");
            // this will get called anytime you perform a read or write characteristic operation
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            Log.e("debug","onConnectionStateChange");
            // this will get called when a device connects or disconnects
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            Log.e("debug","onServicesDiscovered");
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call
        }
    };

    public BleConnect(Context context, Beacon beacon) {
        bluetoothGatt = beacon.getBluetoothDevice().connectGatt(context, false, btleGattCallback);
        if(bluetoothGatt.discoverServices()){
            Log.e("debug","ja");
        }else{
            Log.e("debug","nein");
        }
        if (beacon.getName().contains("SensorTag")){
            sensorTag(bluetoothGatt);
        }else if(beacon.getName().contains("estimote")){
            estimote(bluetoothGatt);
        }
    }

    private void sensorTag(BluetoothGatt bluetoothGatt){

        List<BluetoothGattService> service = bluetoothGatt.getServices();
        Log.e("debug", String.valueOf(service.size()));
        for(int i = 0; i < service.size(); i++){
            Log.e("UUID_"+ i,String.valueOf(service.get(i).getUuid()));

        }

        bluetoothGatt.disconnect();
        bluetoothGatt.close();

                //.getService(UUID.fromString(temperatureSensor.getUUID_SERVICE()));
        /*BluetoothGattCharacteristic config = service.getCharacteristic(UUID.fromString(temperatureSensor.getUUID_CONFIG()));
        byte[] data = config.getValue();
        temperature = data.toString();*/
    }

    private void estimote(BluetoothGatt bluetoothGatt){
        List<BluetoothGattService> service = bluetoothGatt.getServices();
        Log.e("debug",String.valueOf(service.size()));
        for(int i = 0; i < service.size(); i++){
            Log.e("UUID_"+ i,String.valueOf(service.get(i).getUuid()));

        }
    }

    public String getTemperature() {
        return temperature;
    }
}
