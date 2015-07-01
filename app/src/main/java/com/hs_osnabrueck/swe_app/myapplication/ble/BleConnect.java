package com.hs_osnabrueck.swe_app.myapplication.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.AchievementFragment;
import com.hs_osnabrueck.swe_app.myapplication.R;
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

public class BleConnect{

    private List<BluetoothGattService> services = new ArrayList<>();
    private List<BluetoothGattCharacteristic> config = new ArrayList<>();
    private List<BluetoothGattCharacteristic> data = new ArrayList<>();
    private String temperatur, iR_Temperature, humidity, pressure_1, pressure_2, acc_1, acc_2, acc_3;

    private AchievementFragment fragment;
    private static final double PA_PER_METER = 12.0;
    private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            Log.e("debug","onCharacteristicChanged");
            byte[] data = characteristic.getValue();
            if(characteristic.getUuid().toString().equals(TiIRTemperatureSensor.getUUID_DATA())){
                Point3D p = Sensor.IR_TEMPERATURE.convert(data);
                Log.e("Temp", String.valueOf(p.x));
                Log.e("IR Temp", String.valueOf(p.y));
                temperatur = decimal.format(p.y);
                iR_Temperature = decimal.format(p.x);

                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_IR_temperature);
                        tv1.setText("IR Temperatur: " + iR_Temperature + " \u00B0C");
                        TextView tv2 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_temperature);
                        tv2.setText("IR Temperatur: " + temperatur + " \u00B0C");
                    }
                });


                //gatt.setCharacteristicNotification(characteristic, false);
                byte[] value = new byte[1];
                value[0] = (byte) 1;

                gatt.setCharacteristicNotification(config.get(1), true);
                config.get(1).setValue(value);
                gatt.writeCharacteristic(config.get(1));
            }else if(characteristic.getUuid().toString().equals(TiHumiditySensor.getUUID_DATA())){
                Point3D p = Sensor.HUMIDITY.convert(data);
                Log.e("Hum", String.valueOf(p.x));
                humidity = decimal.format(p.x);
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_humidity);
                        tv1.setText("Humidity: " + humidity + " %rH");

                    }
                });

                byte[] value = new byte[1];
                value[0] = (byte) 1;

                gatt.setCharacteristicNotification(config.get(2), true);
                config.get(2).setValue(value);
                gatt.writeCharacteristic(config.get(2));
                //gatt.setCharacteristicNotification(characteristic, false);
            }else if(characteristic.getUuid().toString().equals(TiPressureSensor.getUUID_DATA())){
                Point3D p = Sensor.BAROMETER.convert(data);
                double h = (p.x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / PA_PER_METER;
                h = (double) Math.round(-h * 10.0) / 10.0;

                Log.e("Bar1", decimal.format(p.x / 100.0f));
                Log.e("Bar2", String.valueOf(h));
                pressure_1 = decimal.format(p.x);
                pressure_2 = decimal.format(h);
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_pressure);
                        tv1.setText("Druck: " + pressure_1 + " mBar, " + pressure_2 + " m");

                    }
                });
                //gatt.setCharacteristicNotification(characteristic, false);
                byte[] value = new byte[1];
                value[0] = (byte) 1;

                gatt.setCharacteristicNotification(config.get(3), true);
                config.get(3).setValue(value);
                gatt.writeCharacteristic(config.get(3));
            }else if(characteristic.getUuid().toString().equals(TiAccelerometerSensor.getUUID_DATA())){
                Point3D p = Sensor.ACCELEROMETER.convert(data);
                Log.e("Acc1", String.valueOf(p.x));
                Log.e("Acc2", String.valueOf(p.y));
                Log.e("Acc3", String.valueOf(p.z));
                acc_1 = decimal.format(p.x);
                acc_2 = decimal.format(p.y);
                acc_3 = decimal.format(p.z);
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_accelerometer);
                        tv1.setText("X: " + acc_1 + " G, Y: " + acc_2 + " G, Z: " + acc_3 + " G");

                    }
                });
                //gatt.setCharacteristicNotification(characteristic, false);
                byte[] value = new byte[1];
                value[0] = (byte) 1;

                gatt.setCharacteristicNotification(config.get(0), true);
                config.get(0).setValue(value);
                gatt.writeCharacteristic(config.get(0));
            }else{
                Log.e("debug", "super");
                //TODO
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
            BluetoothGattDescriptor descriptor = null;

            if(characteristic.getUuid().toString().equals(TiIRTemperatureSensor.getUUID_CONFIG())){
                gatt.setCharacteristicNotification(data.get(0), true);
                descriptor = data.get(0).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            }else if(characteristic.getUuid().toString().equals(TiHumiditySensor.getUUID_CONFIG())){
                gatt.setCharacteristicNotification(data.get(1), true);
                descriptor = data.get(1).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            }else if(characteristic.getUuid().toString().equals(TiPressureSensor.getUUID_CONFIG())){
                gatt.setCharacteristicNotification(data.get(2), true);
                descriptor = data.get(2).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            }else if(characteristic.getUuid().toString().equals(TiAccelerometerSensor.getUUID_CONFIG())){
                gatt.setCharacteristicNotification(data.get(3), true);
                descriptor = data.get(3).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            }else{
                gatt.setCharacteristicNotification(data.get(3), true);
                descriptor = gatt.getService(UUID.fromString("b9401000-f5f8-466e-aff9-25556b57fe6d")).getCharacteristic(UUID.fromString("b9401001-f5f8-466e-aff9-25556b57fe6d")).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                //TODO
            }
            if(descriptor != null){
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }
        }

    };

    public BleConnect(final AchievementFragment fragment, Beacon beacon) {
        this.fragment = fragment;
        bluetoothGatt = beacon.getBluetoothDevice().connectGatt(fragment.getActivity().getBaseContext(), false, bluetoothGattCallback);
    }

    public void bleDisconnect(){
        bluetoothGatt.disconnect();
    }

    private void sensorTag(BluetoothGatt gatt){
        services.add(gatt.getService(UUID.fromString(TiIRTemperatureSensor.getUUID_SERVICE())));
        services.add(gatt.getService(UUID.fromString(TiHumiditySensor.getUUID_SERVICE())));
        services.add(gatt.getService(UUID.fromString(TiPressureSensor.getUUID_SERVICE())));
        services.add(gatt.getService(UUID.fromString(TiAccelerometerSensor.getUUID_SERVICE())));

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

        for(int i = 0; i < gatt.getServices().size(); i++){
            Log.e("debug",gatt.getServices().get(i).getUuid().toString());
        }

        gatt.setCharacteristicNotification(config.get(0), true);
        config.get(0).setValue(value);
        gatt.writeCharacteristic(config.get(0));

    }

    private void estimote(BluetoothGatt gatt){

        for(int i = 0; i < gatt.getServices().size(); i++){
            Log.e("debug",gatt.getServices().get(i).getUuid().toString());
        }
        BluetoothGattService service = gatt.getService(UUID.fromString("b9401000-f5f8-466e-aff9-25556b57fe6d"));

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("b9401002-f5f8-466e-aff9-25556b57fe6d"));

        gatt.setCharacteristicNotification(characteristic, true);

        byte[] value = new byte[1];
        value[0] = (byte) 1;
        characteristic.setValue(value);

        gatt.writeCharacteristic(characteristic);

    }

}
