package com.hs_osnabrueck.swe_app.myapplication.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.sensors.TiTemperatureSensor;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class BleConnect {

    private TiTemperatureSensor temperatureSensor = new TiTemperatureSensor();
    private BluetoothGatt bluetoothGatt;
    private String temperature;
    List<BluetoothGattService> service;
    private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
    private BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            Log.e("debug","onCharacteristicChanged");
            // this will get called anytime you perform a read or write characteristic operation
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            Log.e("debug", "onConnectionStateChange");
            // this will get called when a device connects or disconnects
            if(newState == BluetoothProfile.STATE_CONNECTED){
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            Log.e("debug","onServicesDiscovered");
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call
            service = gatt.getServices();
            Log.e("debug", String.valueOf(service.size()));
            for(int i = 0; i < service.size(); i++){
                Log.e("UUID_"+ i,String.valueOf(service.get(i).getUuid()));
            }
            if (gatt.getDevice().getName().contains("SensorTag")){
                sensorTag(gatt);
            }else if(gatt.getDevice().getName().contains("estimote")){
                estimote(gatt);
            }else{
                //TODO unknown Device
            }
        }
    };

    public BleConnect(Context context, Beacon beacon) {
        bluetoothGatt = beacon.getBluetoothDevice().connectGatt(context, false, btleGattCallback);
    }

    private void sensorTag(BluetoothGatt bluetoothGatt){

        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(temperatureSensor.getUUID_SERVICE()));

        BluetoothGattCharacteristic config = service.getCharacteristic(UUID.fromString(temperatureSensor.getUUID_CONFIG()));

        byte[] data = config.getValue();
        String str = new String(data);
        Log.e("debug_2", str);

        bluetoothGatt.disconnect();
        bluetoothGatt.close();
    }

    private void estimote(BluetoothGatt bluetoothGatt){

        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString("b9401000-f5f8-466e-aff9-25556b57fe6d"));
        BluetoothGattCharacteristic config = service.getCharacteristic(UUID.fromString("b9402000-f5f8-466e-aff9-25556b57fe6d"));

        byte[] data = config.getValue();
        String str = new String(data);
        Log.e("debug_2", str);

        bluetoothGatt.disconnect();
        bluetoothGatt.close();
    }

    public String getTemperature() {
        return temperature;
    }
}
