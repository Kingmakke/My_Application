package com.hs_osnabrueck.swe_app.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.interfaces.BleSearchResponse;
import com.hs_osnabrueck.swe_app.myapplication.adapter.DeviceListAdapter;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleUtils;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

/**
 *
 */
public class BeaconFragment extends Fragment implements BleSearchResponse {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;

    private MainActivity main;
    private View rootView;
    //private Button scan;
    private TextView noDevice;
    private ListView deviceList;
    private LayoutInflater inflater;
    private ViewGroup container;
    private DeviceListAdapter adapter;

    public BeaconFragment() {}

    /**
     *
     */
    public void init(){
        rootView = inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_home, container, false);

        final int bleStatus = BleUtils.getBleStatus(getActivity().getBaseContext());
        Intent enableIntent;
        switch (bleStatus) {
            case BleUtils.STATUS_BLE_NOT_AVAILABLE:
                enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
            case BleUtils.STATUS_BLUETOOTH_NOT_AVAILABLE:
                enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        deviceList = (ListView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.homescreen_device_list);

        noDevice = (TextView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.homescreen_no_beacon);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        main.setPos(3);
        main.getBeacons().clear();

        init();

        return rootView;
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        if (main.getBtAdapter() != null && !main.getBtAdapter().isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT_SCAN);
        } else {
            findBeacon();
        }
    }

    /**
     *
     */
    @SuppressLint("NewApi")
    @Override
    public void onStop() {
        super.onStop();
        if (main.getBtAdapter() != null && main.getBleScanner() != null && main.getBtAdapter().isEnabled()) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //noinspection deprecation
                main.getBtAdapter().stopLeScan(main.getBleScanner().getLeScanCallback());
            }else{
                main.getBtAdapter().getBluetoothLeScanner().stopScan(main.getBleScanner().getScanCallback());
            }
        }
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT_SCAN && resultCode == Activity.RESULT_OK) {
            findBeacon();
        }else{
            Fragment fragment = new EventFragment();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, fragment, "0");
            main.restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Versanstaltungsscreen));
            fragmentTransaction.commit();
        }
    }

    /**
     *
     */
    public void findBeacon(){
        main.getBleScanner().bleSearchResponse = this;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //noinspection deprecation
            main.getBtAdapter().startLeScan(main.getBleScanner().getLeScanCallback());
        }else{
            main.getBtAdapter().getBluetoothLeScanner().startScan(main.getBleScanner().getScanCallback());
        }
    }

    /**
     *
     * @param activity
     */
    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

    /**
     *
     * @param device
     * @param rssi
     */
    @Override
    public void beaconFound(BluetoothDevice device, int rssi) {
        Boolean update = false;
        for(int i = 0; i < main.getBeacons().size(); i++){
            main.getBeacons().get(i).raiseCounter();

            if(main.getBeacons().get(i).getBluetoothDevice().getAddress().equals(device.getAddress())){
                main.getBeacons().get(i).setRssi(rssi);
                main.getBeacons().get(i).resetCounter();
                update = true;
                continue;
            }
            if(main.getBeacons().get(i).getCounter() > 10){
                main.getBeacons().remove(i);
                i--;
                continue;
            }

        }
        if(!update){
            main.getBeacons().add(new Beacon(device, rssi));
        }

        adapter = new DeviceListAdapter(main.getBeacons(), main);
        deviceList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(main.getBeacons().isEmpty()){
            noDevice.setVisibility(View.VISIBLE);
        }else{
            noDevice.setVisibility(View.INVISIBLE);
        }
    }
}
