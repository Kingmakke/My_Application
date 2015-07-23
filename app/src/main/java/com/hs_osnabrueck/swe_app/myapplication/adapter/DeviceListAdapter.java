package com.hs_osnabrueck.swe_app.myapplication.adapter;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.MainActivity;
import com.hs_osnabrueck.swe_app.myapplication.PalmeFragment;
import com.hs_osnabrueck.swe_app.myapplication.R;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

import java.util.List;
import java.util.UUID;

/**
 * ListAdapter for Bluetooth devices
 */
public class DeviceListAdapter extends BaseAdapter {
    private List<Beacon> devices;
    private LayoutInflater inflater;
    private MainActivity main;
    public static UUID UART_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");

    /**
     * DeviceListAdapter class
     * @param devices list of found beacons
     * @param main MainActivity
     */
    public DeviceListAdapter(List<Beacon> devices, MainActivity main) {
        this.inflater = LayoutInflater.from(main.getBaseContext());
        this.devices = devices;
        this.main = main;
    }

    /**
     * returns the size of the list of found beacons
     * @return the size of the device list
     */
    public int getCount() {
        return devices.size();
    }

    /**
     * gets the item of the device list at a given position
     * @param position desired position
     * @return the item of a given position
     */
    public Object getItem(int position) {
        return devices.get(position);
    }

    /**
     * gives the itemID
     * @param position position of the item
     * @return position
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * returns the amount of different types in the view
     * @return 1
     */
    @Override
    public int getViewTypeCount(){
        return 1;
    }

    /**
     * returns the view and how the screen looks like (e.g. icon and name of the beacon)
     * @param position position of the desired item
     * @param convertView
     * @param parent
     * @return the view
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewGroup vg;

        if (convertView != null) {
            vg = (ViewGroup) convertView;
        } else {
            vg = (ViewGroup) inflater.inflate(R.layout.element_device, null);
        }

        Beacon deviceInfo = devices.get(position);
        BluetoothDevice device = deviceInfo.getBluetoothDevice();
        int rssi = deviceInfo.getRssi();
        String name = device.getName();
        if (name == null) {
            name = new String("Unknown device");
        }

        String descr = name + "\n" + device.getAddress() + "\nRssi: " + rssi + " dBm";
        ((TextView) vg.findViewById(R.id.device_discription)).setText(descr);
        Button bv = (Button)vg.findViewById(R.id.device_connect);
        ImageView iv = (ImageView)vg.findViewById(R.id.device_image);
        if (name.equals("SensorTag")) {
            iv.setImageResource(R.drawable.sensortag);
            bv.setVisibility(View.VISIBLE);
        }else if(name.contains("estimote")) {
            iv.setImageResource(R.drawable.estimote);
            bv.setVisibility(View.INVISIBLE);
        }else if(name.equals("CC2650 SensorTag")){
            iv.setImageResource(R.drawable.sensortag2);
            bv.setVisibility(View.INVISIBLE);
        /*}else if(device.getUuids().toString().equals(UART_UUID)){
            iv.setImageResource(R.drawable.core);
            bv.setVisibility(View.INVISIBLE);*/
        }else {
            iv.setImageResource(R.drawable.unknown);
            bv.setVisibility(View.INVISIBLE);
        }

        bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                    //noinspection deprecation
                    main.getBtAdapter().stopLeScan(main.getBleScanner().getLeScanCallback());
                }else{
                    main.getBtAdapter().getBluetoothLeScanner().stopScan(main.getBleScanner().getScanCallback());
                }

                Beacon beacon = devices.get(position);
                if (beacon.getBluetoothDevice() != null) {
                    main.setBeacon(beacon);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", 3);
                    Fragment fragment = new PalmeFragment();
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager = main.getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment, "9");
                    main.restoreActionBar(main.getString(R.string.Beaconinfoscreen));
                    fragmentTransaction.commit();
                }
            }
        });

        return vg;
    }

}