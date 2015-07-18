package com.hs_osnabrueck.swe_app.myapplication.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.hs_osnabrueck.swe_app.myapplication.MainActivity;
import com.hs_osnabrueck.swe_app.myapplication.R;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleScanner;
import com.hs_osnabrueck.swe_app.myapplication.interfaces.BleSearchResponse;

public class BleSearchService extends Service implements BleSearchResponse{

    private BleScanner scanner;
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        this.intent = intent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanner = new BleScanner();
                scanner.bleSearchResponse = BleSearchService.this;
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                    //noinspection deprecation
                    btAdapter.startLeScan(scanner.getLeScanCallback());
                }else{
                    btAdapter.getBluetoothLeScanner().startScan(scanner.getScanCallback());
                }
            }
        }).start();


        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //noinspection deprecation
            btAdapter.stopLeScan(scanner.getLeScanCallback());
        }else{
            btAdapter.getBluetoothLeScanner().stopScan(scanner.getScanCallback());
        }
    }

    @Override
    public void beaconFound(BluetoothDevice device, int rssi) {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("pos", 3);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                myIntent,
                0);

        String devicename;
        if(device.getName() == null){
            devicename = "unknown";
        }else{
            devicename = device.getName();
        }
        Notification notification  = new Notification.Builder(this)
                .setContentTitle("Beacon gefunden")
                .setContentText(devicename)
                .setSmallIcon(R.drawable.ic_palme)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000})
                //.setLights(Color.RED, 3000, 3000)
                //.setPriority(Notification.PRIORITY_DEFAULT)
                //.setSound(Uri.parse(""))
                //.addAction(R.drawable.icon, "Call", pIntent)
                //.addAction(R.drawable.icon, "More", pIntent)
                //.addAction(R.drawable.icon, "And more", pIntent)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }
}
