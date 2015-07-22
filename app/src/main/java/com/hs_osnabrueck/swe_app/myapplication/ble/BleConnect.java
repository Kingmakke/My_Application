package com.hs_osnabrueck.swe_app.myapplication.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.widget.TextView;

import com.estimote.sdk.cloud.model.BeaconInfo;
import com.estimote.sdk.connection.BeaconConnection;
import com.estimote.sdk.exception.EstimoteDeviceException;
import com.hs_osnabrueck.swe_app.myapplication.BeaconinfoFragment;
import com.hs_osnabrueck.swe_app.myapplication.R;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
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

/**
 * Class that connects to a Beacon, reads the wanted sensors and disconnects afterwards
 */
public class BleConnect{

    private List<BluetoothGattService> services = new ArrayList<>();
    private List<BluetoothGattCharacteristic> config = new ArrayList<>();
    private List<BluetoothGattCharacteristic> data = new ArrayList<>();
    private String temperatur, iR_Temperature, humidity, pressure_1, pressure_2, acc_1, acc_2, acc_3;
    private boolean calibrated = false;

    private BeaconConnection beaconConnection;
    private BeaconConnection.ConnectionCallback connectionCallback = new BeaconConnection.ConnectionCallback() {
        @Override
        public void onAuthenticated(BeaconInfo beaconInfo) {

        }

        @Override
        public void onAuthenticationError(EstimoteDeviceException e) {
        }

        @Override
        public void onDisconnected() {

        }
    };

    byte[] value = {(byte) 1};
    private BeaconinfoFragment fragment;
    private static final double PA_PER_METER = 12.0;
    private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        /**
         * gets the data from the sensors one by one prints them and calls the next sensor
         * @param gatt GATT client
         * @param characteristic characteristic which we want to get from the SensorTag
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            byte[] sensorData = characteristic.getValue();
            if(characteristic.getUuid().toString().equals(TiIRTemperatureSensor.getUUID_DATA())){
                Point3D p = Sensor.IR_TEMPERATURE.convert(sensorData);
                temperatur = decimal.format(p.x);
                iR_Temperature = decimal.format(p.y);

                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment.getView() != null) {
                            TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_IR_temperature);
                            tv1.setText("IR Temperatur: " + iR_Temperature + " \u00B0C");
                            TextView tv2 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_temperature);
                            tv2.setText("Temperatur: " + temperatur + " \u00B0C");
                        }
                    }
                });


                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(0), false);

                gatt.setCharacteristicNotification(config.get(1), true);
                config.get(1).setValue(value);
                gatt.writeCharacteristic(config.get(1));
            }else if(characteristic.getUuid().toString().equals(TiHumiditySensor.getUUID_DATA())){
                Point3D p = Sensor.HUMIDITY.convert(sensorData);
                humidity = decimal.format(p.x);
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment.getView() != null) {
                            TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_humidity);
                            tv1.setText("Humidity: " + humidity + " %rH");
                        }
                    }
                });
                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(1), false);

                gatt.setCharacteristicNotification(config.get(2), true);
                config.get(2).setValue(value);
                gatt.writeCharacteristic(config.get(2));


            }else if(characteristic.getUuid().toString().equals(TiPressureSensor.getUUID_DATA())){
                Point3D p = Sensor.BAROMETER.convert(sensorData);
                //double h = (p.x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / PA_PER_METER;
                double h = p.x / PA_PER_METER;
                h = (double) Math.round(-h * 10.0) / 10.0;

                pressure_1 = decimal.format(p.x);
                pressure_2 = decimal.format(h);
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment.getView() != null) {
                            TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_pressure);
                            tv1.setText("Druck: " + pressure_1 + " mBar, " + pressure_2 + " m");
                        }
                    }
                });
                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(2), false);

                gatt.setCharacteristicNotification(config.get(3), true);
                config.get(3).setValue(value);
                gatt.writeCharacteristic(config.get(3));
            }else if(characteristic.getUuid().toString().equals(TiAccelerometerSensor.getUUID_DATA())){
                Point3D p = Sensor.ACCELEROMETER.convert(sensorData);
                acc_1 = decimal.format(p.x);
                acc_2 = decimal.format(p.y);
                acc_3 = decimal.format(p.z);
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment.getView() != null){
                            TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_accelerometer);
                            tv1.setText("X: " + acc_1 + " G, Y: " + acc_2 + " G, Z: " + acc_3 + " G");
                        }


                    }
                });
                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(3), false);

                gatt.setCharacteristicNotification(config.get(0), true);
                config.get(0).setValue(value);
                gatt.writeCharacteristic(config.get(0));
            }else{
                //TODO
            }
        }

        /**
         * this will get called when a device connects or disconnects
         * @param gatt GATT client
         * @param status
         * @param newState new state of bluetooth connection
         */
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if(newState == BluetoothProfile.STATE_CONNECTED){
                gatt.discoverServices();
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                gatt.close();
            }
        }

        /**
         * this will get called after the client initiates a BluetoothGatt.discoverServices() call
         * @param gatt GATT client invoked discoverServices()
         * @param status
         */
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            if (gatt.getDevice().getName().contains("SensorTag")){
                sensorTag(gatt);
            }else{
                //TODO
            }
        }

        /**
         * This method gets called when characteristics are written and sets the descriptor for SensorTags sensors
         * to enable notifications.
         * @param gatt bluetoothGatt
         * @param characteristic the written characteristic
         * @param status
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
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
                //TODO
            }
            if(descriptor != null){
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }
        }

    };

    /**
     * Constructor that gets called with a Beacon object. Is the Beacon is a SensorTag the bluetoothGatt variable will be set,
     * else if the Beacon is an Estimote the beaconConnection variable will be set
     * @param fragment fragment on which the results will be shown
     * @param beacon beacon we have found and want to connect with
     */
    public BleConnect(final BeaconinfoFragment fragment, Beacon beacon) {
        this.fragment = fragment;
        if(beacon.getBluetoothDevice().getName().contains("SensorTag")){
            bluetoothGatt = beacon.getBluetoothDevice().connectGatt(fragment.getActivity().getBaseContext(), false, bluetoothGattCallback);
        }else if(beacon.getBluetoothDevice().getName().contains("estimote")){
            beaconConnection = new BeaconConnection(fragment.getActivity().getBaseContext(), beacon.getId(), connectionCallback);
            estimote(beaconConnection);
        }

    }

    /**
     * Disconnects from the current connected beacon
     * @param beacon beacon we are connected with
     */
    public void bleDisconnect(Beacon beacon){
        if(beacon.getBluetoothDevice().getName().contains("SensorTag")){
            bluetoothGatt.disconnect();
        }else if(beacon.getBluetoothDevice().getName().contains("estimote")){
            beaconConnection.close();
        }
    }

    /**
     * This method gets called for SensorTags. First services are read and saved in a list.
     * Then config and data characteristics of the service are saved in a list.
     * Afterwards the sensor will be enabled.
     * @param gatt
     */
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


        gatt.setCharacteristicNotification(config.get(0), true);
        config.get(0).setValue(value);
        gatt.writeCharacteristic(config.get(0));

    }

    /**
     * This method is called for estimote Beacons and sets the TextViews
     * @param connection
     */
    private void estimote(final BeaconConnection connection){

        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fragment.getView() != null) {
                    TextView tv1 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_temperature);
                    tv1.setText("Proximity UUID: " + connection.proximityUuid());
                    TextView tv2 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_IR_temperature);
                    tv2.setText("Minor ID: " + connection.minor());
                    TextView tv3 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_humidity);
                    tv3.setText("Major ID: " + connection.major());
                    TextView tv4 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_pressure);
                    tv4.setText("Hardware Version: " + connection.getHardwareVersion() + "\n"
                            + "Sofware Version: " + connection.getSoftwareVersion() + "\n"
                            + "MAC: " + connection.getMacAddress());
                    TextView tv5 = (TextView) fragment.getView().findViewById(R.id.achievementscreen_accelerometer);
                    tv5.setText("Batterie: " + connection.getBatteryPercent());
                }
            }
        });

    }

}
