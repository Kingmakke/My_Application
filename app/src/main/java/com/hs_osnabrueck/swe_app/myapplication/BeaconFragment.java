package com.hs_osnabrueck.swe_app.myapplication;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.adapter.DeviceListAdapter;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleScanner;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleUtils;
import com.hs_osnabrueck.swe_app.myapplication.interfaces.BleSearchResponse;

public class BeaconFragment extends Fragment implements BleSearchResponse{

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;

    private MainActivity main;
    private View rootView;
    private Button scan;
    private TextView noDevice;
    private ListView deviceList;
    private LayoutInflater inflater;
    private ViewGroup container;
    private DeviceListAdapter adapter;
    private BluetoothAdapter btAdapter = null;
    private BleScanner scanner;

    public BeaconFragment() {}

    public void init(){
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //TODO  zum Testen auf dem Emulator auskommentieren
        //-----von-----
        final int bleStatus = BleUtils.getBleStatus(getActivity().getBaseContext());
        Intent enableIntent;
        switch (bleStatus) {
            case BleUtils.STATUS_BLE_NOT_AVAILABLE:
                enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
                //ErrorDialog.newInstance(R.string.dialog_error_no_ble).show(getFragmentManager(), ErrorDialog.TAG);
                //return;
            case BleUtils.STATUS_BLUETOOTH_NOT_AVAILABLE:
                enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
                // ErrorDialog.newInstance(R.string.dialog_error_no_bluetooth).show(getFragmentManager(), ErrorDialog.TAG);
                // return;
            default:
                btAdapter = main.getBtAdapter();
        }
        //-----bis-----

        deviceList = (ListView)rootView.findViewById(R.id.homescreen_device_list);

        noDevice = (TextView)rootView.findViewById(R.id.homescreen_no_beacon);

        scan = (Button)rootView.findViewById(R.id.homescreen_scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scan.getText().toString().compareTo("Scan") == 0) {
                    scan.setText("Stop");
                    if (btAdapter != null && !btAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT_SCAN);
                    } else {
                        findBeacon();
                    }
                } else if (scan.getText().toString().compareTo("Stop") == 0) {
                    scan.setText("Scan");
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                        //noinspection deprecation
                        btAdapter.stopLeScan(scanner.getLeScanCallback());
                    }else{
                        btAdapter.getBluetoothLeScanner().stopScan(scanner.getScanCallback());
                    }
                }


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        main.setPos(3);

        init();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT_SCAN && resultCode == Activity.RESULT_OK) {
            findBeacon();
        }
    }

    public void findBeacon(){
        scanner = new BleScanner();
        main.setBleScanner(scanner);
        scanner.bleSearchResponse = this;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //noinspection deprecation
            btAdapter.startLeScan(scanner.getLeScanCallback());
        }else{
            btAdapter.getBluetoothLeScanner().startScan(scanner.getScanCallback());
        }
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

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

        }/*
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
        }*/
    }
}
