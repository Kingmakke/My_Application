package com.hs_osnabrueck.swe_app.myapplication.ble;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estimote.sdk.cloud.model.BeaconInfo;
import com.estimote.sdk.connection.BeaconConnection;
import com.estimote.sdk.exception.EstimoteDeviceException;
import com.hs_osnabrueck.swe_app.myapplication.PalmeFragment;
import com.hs_osnabrueck.swe_app.myapplication.R;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.sensors.Point3D;
import com.hs_osnabrueck.swe_app.myapplication.sensors.Sensor;
import com.hs_osnabrueck.swe_app.myapplication.sensors.SensorTagGatt;
import com.hs_osnabrueck.swe_app.myapplication.sensors.Spark;

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

    // UUID for the BTLE client characteristic which is necessary for notifications.

    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;

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
    private PalmeFragment fragment;
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
            if(characteristic.getUuid().equals(SensorTagGatt.UUID_IRT_DATA)){
                Point3D p = Sensor.IR_TEMPERATURE.convert(sensorData);
                temperatur = decimal.format(p.x);
                iR_Temperature = decimal.format(p.y);

                /*fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment.getView() != null) {

                        }
                    }
                });*/


                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(0), false);

                gatt.setCharacteristicNotification(config.get(1), true);
                config.get(1).setValue(value);
                gatt.writeCharacteristic(config.get(1));
            }else if(characteristic.getUuid().equals(SensorTagGatt.UUID_HUM_DATA)){
                Point3D p = Sensor.HUMIDITY.convert(sensorData);
                humidity = decimal.format(p.x);
                final float hum = (float)p.x;
                try{
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(fragment.getView() != null) {
                                TextView tv1 = (TextView) fragment.getView().findViewById(R.id.palmescreen_humidity);
                                tv1.setText("Feuchtigkeitswert: " + humidity + " %rH");
                                ImageView image1 = (ImageView)fragment.getView().findViewById(R.id.palmescreen_imageView1);
                                ImageView image2 = (ImageView)fragment.getView().findViewById(R.id.palmescreen_imageView2);

                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(image1.getWidth(), image1.getHeight());
                                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                params.topMargin = 10;
                                params.addRule(RelativeLayout.VISIBLE);
                                params.addRule(RelativeLayout.BELOW,tv1.getId());
                                image2.setLayoutParams(params);
                                Bitmap tempBitmap = Bitmap.createBitmap(image1.getWidth(), image1.getHeight(), Bitmap.Config.RGB_565);
                                Canvas canvas = new Canvas(tempBitmap);
                                Paint paint = new Paint();
                                paint.setColor(fragment.getResources().getColor(R.color.normal));
                                paint.setStyle(Paint.Style.FILL);
                                canvas.drawRect(0, image1.getHeight(), image1.getWidth(), image1.getHeight() * (100 - hum) / 100, paint);
                                image2.setImageDrawable(new BitmapDrawable(fragment.getResources(), tempBitmap));
                            }
                        }
                    });
                }catch(NullPointerException e){
                    gatt.disconnect();
                }
                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(1), false);

                gatt.setCharacteristicNotification(config.get(2), true);
                config.get(2).setValue(value);
                gatt.writeCharacteristic(config.get(2));


            }else if(characteristic.getUuid().equals(SensorTagGatt.UUID_BAR_DATA)){
                Point3D p = Sensor.BAROMETER.convert(sensorData);
                //double h = (p.x - BarometerCalibrationCoefficients.INSTANCE.heightCalibration) / PA_PER_METER;
                double h = p.x / PA_PER_METER;
                h = (double) Math.round(-h * 10.0) / 10.0;

                pressure_1 = decimal.format(p.x);
                pressure_2 = decimal.format(h);
                /*fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment.getView() != null) {

                        }
                    }
                });*/
                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(2), false);

                gatt.setCharacteristicNotification(config.get(3), true);
                config.get(3).setValue(value);
                gatt.writeCharacteristic(config.get(3));
            }else if(characteristic.getUuid().equals(SensorTagGatt.UUID_ACC_DATA)){
                Point3D p = Sensor.ACCELEROMETER.convert(sensorData);
                acc_1 = decimal.format(p.x);
                acc_2 = decimal.format(p.y);
                acc_3 = decimal.format(p.z);
                /*fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fragment.getView() != null){

                        }
                    }
                });*/
                //gatt.setCharacteristicNotification(characteristic, false);
                //gatt.setCharacteristicNotification(data.get(3), false);

                gatt.setCharacteristicNotification(config.get(0), true);
                config.get(0).setValue(value);
                gatt.writeCharacteristic(config.get(0));
            }else if(gatt.getDevice().getName().toString().equals("Spark")){

                humidity = characteristic.getStringValue(0);
                final float hum = Float.valueOf(humidity);
                try {
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (fragment.getView() != null) {
                                TextView tv1 = (TextView) fragment.getView().findViewById(R.id.palmescreen_humidity);
                                tv1.setText("Feuchtigkeitswert: " + humidity + " %rH");
                                ImageView image1 = (ImageView) fragment.getView().findViewById(R.id.palmescreen_imageView1);
                                ImageView image2 = (ImageView) fragment.getView().findViewById(R.id.palmescreen_imageView2);

                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(image1.getWidth(), image1.getHeight());
                                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                params.topMargin = 10;
                                params.addRule(RelativeLayout.VISIBLE);
                                params.addRule(RelativeLayout.BELOW, tv1.getId());
                                image2.setLayoutParams(params);
                                Bitmap tempBitmap = Bitmap.createBitmap(image1.getWidth(), image1.getHeight(), Bitmap.Config.RGB_565);
                                Canvas canvas = new Canvas(tempBitmap);
                                Paint paint = new Paint();
                                paint.setColor(fragment.getResources().getColor(R.color.normal));
                                paint.setStyle(Paint.Style.FILL);
                                canvas.drawRect(0, image1.getHeight(), image1.getWidth(), image1.getHeight() * (100 - hum) / 100, paint);
                                image2.setImageDrawable(new BitmapDrawable(fragment.getResources(), tempBitmap));
                            }
                        }
                    });
                }catch(NullPointerException e){
                    gatt.disconnect();
                }
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
            }else if(gatt.getDevice().getName().toString().equals("Spark")){
                tx = gatt.getService(Spark.UART_UUID).getCharacteristic(Spark.TX_UUID);
                rx = gatt.getService(Spark.UART_UUID).getCharacteristic(Spark.RX_UUID);
                // Setup notifications on RX characteristic changes (i.e. data received).
                // First call setCharacteristicNotification to enable notification.
                if (!gatt.setCharacteristicNotification(rx, true)) {
                }
                // Next update the RX characteristic's client descriptor to enable notifications.
                if (rx.getDescriptor(Spark.CLIENT_UUID) != null) {
                    BluetoothGattDescriptor desc = rx.getDescriptor(Spark.CLIENT_UUID);
                    desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    if (!gatt.writeDescriptor(desc)) {
                    }
                }

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

            if(characteristic.getUuid().equals(SensorTagGatt.UUID_IRT_CONF)){
                gatt.setCharacteristicNotification(data.get(0), true);
                descriptor = data.get(0).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            }else if(characteristic.getUuid().equals(SensorTagGatt.UUID_HUM_CONF)){
                gatt.setCharacteristicNotification(data.get(1), true);
                descriptor = data.get(1).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            }else if(characteristic.getUuid().equals(SensorTagGatt.UUID_BAR_CONF)){
                gatt.setCharacteristicNotification(data.get(2), true);
                descriptor = data.get(2).getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            }else if(characteristic.getUuid().equals(SensorTagGatt.UUID_ACC_CONF)){
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
    public BleConnect(final PalmeFragment fragment, Beacon beacon) {
        this.fragment = fragment;
        if(beacon.getBluetoothDevice().getName().contains("SensorTag")){
            bluetoothGatt = beacon.getBluetoothDevice().connectGatt(fragment.getActivity().getBaseContext(), false, bluetoothGattCallback);
        }else if(beacon.getBluetoothDevice().getName().contains("estimote")){
            beaconConnection = new BeaconConnection(fragment.getActivity().getBaseContext(), beacon.getId(), connectionCallback);
            estimote(beaconConnection);
        }else if(beacon.getBluetoothDevice().getName().toString().equals("Spark")){
            bluetoothGatt = beacon.getBluetoothDevice().connectGatt(fragment.getActivity().getBaseContext(), false, bluetoothGattCallback);
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
        }else if(beacon.getBluetoothDevice().getName().toString().equals("Spark")){
            bluetoothGatt.disconnect();
        }
    }

    /**
     * This method gets called for SensorTags. First services are read and saved in a list.
     * Then config and data characteristics of the service are saved in a list.
     * Afterwards the sensor will be enabled.
     * @param gatt
     */
    private void sensorTag(BluetoothGatt gatt){
        services.add(gatt.getService(SensorTagGatt.UUID_IRT_SERV));
        services.add(gatt.getService(SensorTagGatt.UUID_HUM_SERV));
        services.add(gatt.getService(SensorTagGatt.UUID_BAR_SERV));
        services.add(gatt.getService(SensorTagGatt.UUID_ACC_SERV));

        config.add(services.get(0).getCharacteristic(SensorTagGatt.UUID_IRT_CONF));
        config.add(services.get(1).getCharacteristic(SensorTagGatt.UUID_HUM_CONF));
        config.add(services.get(2).getCharacteristic(SensorTagGatt.UUID_BAR_CONF));
        config.add(services.get(3).getCharacteristic(SensorTagGatt.UUID_ACC_CONF));

        data.add(services.get(0).getCharacteristic(SensorTagGatt.UUID_IRT_DATA));
        data.add(services.get(1).getCharacteristic(SensorTagGatt.UUID_HUM_DATA));
        data.add(services.get(2).getCharacteristic(SensorTagGatt.UUID_BAR_DATA));
        data.add(services.get(3).getCharacteristic(SensorTagGatt.UUID_ACC_DATA));


        gatt.setCharacteristicNotification(config.get(1), true);
        config.get(1).setValue(value);
        gatt.writeCharacteristic(config.get(1));

    }

    /**
     * This method is called for estimote Beacons and sets the TextViews
     * @param connection
     */
    private void estimote(final BeaconConnection connection) {

        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fragment.getView() != null) {


                }
            }
        });
    }
}
