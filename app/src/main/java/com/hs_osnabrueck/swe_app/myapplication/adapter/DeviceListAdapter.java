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

import com.hs_osnabrueck.swe_app.myapplication.AchievementFragment;
import com.hs_osnabrueck.swe_app.myapplication.MainActivity;
import com.hs_osnabrueck.swe_app.myapplication.R;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleConnect;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
    private List<Beacon> devices;
    private LayoutInflater inflater;
    private MainActivity main;

    public DeviceListAdapter(List<Beacon> devices, MainActivity main) {
        this.inflater = LayoutInflater.from(main.getBaseContext());
        this.devices = devices;
        this.main = main;
    }

    public int getCount() {
        return devices.size();
    }

    public Object getItem(int position) {
        return devices.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

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

        ImageView iv = (ImageView)vg.findViewById(R.id.device_image);
        if (name.contains("SensorTag")) {
            iv.setImageResource(R.drawable.sensortag);
        }else if(name.contains("estimote")) {
            iv.setImageResource(R.drawable.estimote);
        }else {
            iv.setImageResource(R.drawable.unknown);
        }

        Button bv = (Button)vg.findViewById(R.id.device_connect);
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
                    BleConnect bleConnect = new BleConnect(main.getBaseContext(), beacon);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("temperature", bleConnect.getTemperature());
                    //bundle.putString("date", main.getEventliste().elementAt(eventitems.indexOf(pos)).getDate());
                    //bundle.putString("location",main.getEventliste().elementAt(eventitems.indexOf(pos)).getDescription());
                    //bundle.putString("description", main.getEventliste().elementAt(eventitems.indexOf(pos)).getContent());
                    bundle.putInt("pos", 0);
                    Fragment fragment = new AchievementFragment();
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager = main.getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    main.restoreActionBar(main.getString(R.string.title_section9));
                    fragmentTransaction.commit();
                }else{
                    //TODO ?
                }
                //TODO
                // auf Karte wechseln

            }
        });

        return vg;
    }

}