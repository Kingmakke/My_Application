package com.hs_osnabrueck.swe_app.myapplication.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.sensors.BarometerCalibrationCoefficients;
import com.hs_osnabrueck.swe_app.myapplication.sensors.Point3D;
import com.hs_osnabrueck.swe_app.myapplication.sensors.Sensor;
import com.hs_osnabrueck.swe_app.myapplication.sensors.TiAccelerometerSensor;
import com.hs_osnabrueck.swe_app.myapplication.sensors.TiHumiditySensor;
import com.hs_osnabrueck.swe_app.myapplication.sensors.TiIRTemperatureSensor;
import com.hs_osnabrueck.swe_app.myapplication.sensors.TiPressureSensor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BleConnect {

    private List<BluetoothGattService> services = new ArrayList<>();
    private List<BluetoothGattCharacteristic> config = new ArrayList<>();
    private List<BluetoothGattCharacteristic> data = new ArrayList<>();

    private static final double PA_PER_METER = 12.0;
    private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");

    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            Log.e("debug","onCharacteristicChanged");
            byte[] data = characteristic.getValue();
            if(characteristic.getUuid().toString().equals(TiIRTemperatureSensor.getUUID_DATA())){
                Point3D p = Sensor.IR_TEMPERATURE.convert(data);
                Log.e("Temp", String.valueOf(p.x));
                Log.e("IR Temp", String.valueOf(p.y));
            }else if(characteristic.getUuid().toString().equals(TiPressureSensor.getUUID_DATA())){
                Point3D p = Sensor.BAROMETER.convert(data);
                double h = (p.x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / PA_PER_METER;
                h = (double) Math.round(-h * 10.0) / 10.0;

                Log.e("Bar1", decimal.format(p.x / 100.0f));
                Log.e("Bar2", String.valueOf(h));
            }else if(characteristic.getUuid().toString().equals(TiHumiditySensor.getUUID_DATA())){
                Point3D p = Sensor.HUMIDITY.convert(data);
                Log.e("Hum", String.valueOf(p.x));
            }else if(characteristic.getUuid().toString().equals(TiAccelerometerSensor.getUUID_DATA())){
                Point3D p = Sensor.ACCELEROMETER.convert(data);
                Log.e("Acc1", String.valueOf(p.x));
                Log.e("Acc2", String.valueOf(p.y));
                Log.e("Acc3", String.valueOf(p.z));
                gatt.disconnect();
            }


            //gatt.disconnect();
            //gatt.close();
            // this will get called anytime you perform a read or write characteristic operation
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            Log.e("debug", "onConnectionStateChange");
            // this will get called when a device connects or disconnects
            if(newState == BluetoothProfile.STATE_CONNECTED){
                gatt.discoverServices();
            }else{
                gatt.close();
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            Log.e("debug","onServicesDiscovered");
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call
            if (gatt.getDevice().getName().contains("SensorTag")){
                sensorTag(gatt);
            }else if(gatt.getDevice().getName().contains("estimote")){
                estimote(gatt);
            }else{
                //TODO unknown Device
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            Log.e("debug", "onCharacteristicWrite");
            BluetoothGattDescriptor descriptor;

            for(int i = 0; i < config.size(); i++){
                Log.e("debug", "data for " + i);
                //gatt.setCharacteristicNotification(data.get(i), true);
                descriptor = data.get(i).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
            }
        }

    };

    public BleConnect(Context context, Beacon beacon) {
        bluetoothGatt = beacon.getBluetoothDevice().connectGatt(context, false, btleGattCallback);
    }

    private void sensorTag(BluetoothGatt bluetoothGatt){
        services.add(bluetoothGatt.getService(UUID.fromString(TiIRTemperatureSensor.getUUID_SERVICE())));
        services.add(bluetoothGatt.getService(UUID.fromString(TiHumiditySensor.getUUID_SERVICE())));
        services.add(bluetoothGatt.getService(UUID.fromString(TiPressureSensor.getUUID_SERVICE())));
        services.add(bluetoothGatt.getService(UUID.fromString(TiAccelerometerSensor.getUUID_SERVICE())));

        config.add(services.get(0).getCharacteristic(UUID.fromString(TiIRTemperatureSensor.getUUID_CONFIG())));
        config.add(services.get(1).getCharacteristic(UUID.fromString(TiHumiditySensor.getUUID_CONFIG())));
        config.add(services.get(2).getCharacteristic(UUID.fromString(TiPressureSensor.getUUID_CONFIG())));
        config.add(services.get(3).getCharacteristic(UUID.fromString(TiAccelerometerSensor.getUUID_CONFIG())));

        data.add(services.get(0).getCharacteristic(UUID.fromString(TiIRTemperatureSensor.getUUID_DATA())));
        data.add(services.get(1).getCharacteristic(UUID.fromString(TiHumiditySensor.getUUID_DATA())));
        data.add(services.get(2).getCharacteristic(UUID.fromString(TiPressureSensor.getUUID_DATA())));
        data.add(services.get(3).getCharacteristic(UUID.fromString(TiAccelerometerSensor.getUUID_DATA())));
        byte[] value = new byte[1];
        value[0] = (byte) 1;
        for(int i = 0; i < config.size(); i++){
            Log.e("debug", "config for " + i);
            //bluetoothGatt.setCharacteristicNotification(config.get(i), true);
            config.get(i).setValue(value);
            bluetoothGatt.writeCharacteristic(config.get(i));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
    }

    private void estimote(BluetoothGatt bluetoothGatt){

        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString("b9401000-f5f8-466e-aff9-25556b57fe6d"));
        if(service == null){
            Log.e("debug","service null");
        }

        for(int i = 0; i < service.getCharacteristics().size(); i++){
            Log.e("debug",String.valueOf(service.getCharacteristics().get(i).getUuid()));
        }
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("b9401004-f5f8-466e-aff9-25556b57fe6d"));


        bluetoothGatt.readCharacteristic(characteristic);

    }

}
