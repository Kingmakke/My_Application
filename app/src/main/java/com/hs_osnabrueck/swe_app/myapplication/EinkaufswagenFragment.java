package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.ble.BleConnect;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleScanner;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleUtils;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

public class EinkaufswagenFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;
    private static final long SCAN_PERIOD = 1000;

    private View rootView;
    private Button scan, download;
    private TextView beaconinfo;
    private LayoutInflater inflater;
    private ViewGroup container;
    private MainActivity main;

    private Beacon beacon;
    private BluetoothAdapter btAdapter = null;
    private BleScanner scanner;
    private BleConnect bleConnect;

    public EinkaufswagenFragment() {}

    public void init(){
        rootView = inflater.inflate(R.layout.fragment_einkaufswagen, container, false);

        download = (Button)rootView.findViewById(R.id.einkaufswagenscreen_download_button);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=osnabrueck.greencity"));
                startActivity(intent);

            }
        });
    }

    public void initBeacon(){


        //TODO  zum Testen auf dem Emulator auskommentieren
        //-----von-----
        final int bleStatus = BleUtils.getBleStatus(getActivity().getBaseContext());
        switch (bleStatus) {
            case BleUtils.STATUS_BLE_NOT_AVAILABLE:
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
                //ErrorDialog.newInstance(R.string.dialog_error_no_ble).show(getFragmentManager(), ErrorDialog.TAG);
                //return;
            case BleUtils.STATUS_BLUETOOTH_NOT_AVAILABLE:
                Intent enableIntent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent2,REQUEST_ENABLE_BT);
                // ErrorDialog.newInstance(R.string.dialog_error_no_bluetooth).show(getFragmentManager(), ErrorDialog.TAG);
                // return;
            default:
                btAdapter = BleUtils.getBluetoothAdapter(rootView.getContext());
        }


        beaconinfo = (TextView)rootView.findViewById(R.id.einkaufswagenscreen_beaconinfo);
        beaconinfo.setGravity(Gravity.CENTER_VERTICAL);
        beaconinfo.setCompoundDrawablePadding(50);
        if(beacon != null) {
            if (beacon.getName().contains("SensorTag")) {
                beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
                beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
            } else if (beacon.getName().contains("estimote")) {
                beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
                beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
            }
        }else{
            beaconinfo.setText(getString(R.string.homescreen_kein_Beacon));
            beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
        }
/*
        beaconinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beacon = scanner.getBeacon();
                if (beacon.getBluetoothDevice() != null) {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                        //noinspection deprecation
                        btAdapter.stopLeScan(scanner.getLeScanCallback());
                    }else{
                        btAdapter.getBluetoothLeScanner().stopScan(scanner.getScanCallback());
                    }
                    bleConnect = new BleConnect(main.getBaseContext(), beacon);
                    Bundle bundle = new Bundle();
                    bundle.putString("temperature", bleConnect.getTemperature());
                    //bundle.putString("date", main.getEventliste().elementAt(eventitems.indexOf(pos)).getDate());
                    //bundle.putString("location",main.getEventliste().elementAt(eventitems.indexOf(pos)).getDescription());
                    //bundle.putString("description", main.getEventliste().elementAt(eventitems.indexOf(pos)).getContent());
                    bundle.putInt("pos", 3);
                    Fragment fragment = new AchievementFragment();
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    main.restoreActionBar(getString(R.string.title_section9));
                    fragmentTransaction.commit();
                }else{
                    //TODO ?
                }
                //TODO
                // auf Karte wechseln
            }
        });
*/
        scan = (Button)rootView.findViewById(R.id.einkaufswagenscreen_scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scan.getText().toString().compareTo("Scan") == 0) {
                    scan.setText("Stop");
                    beacon = new Beacon(null, -120);
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

    public void findBeacon(){
        //scanner = new BleScanner(beaconinfo, beacon);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //noinspection deprecation
            btAdapter.startLeScan(scanner.getLeScanCallback());
        }else{
            btAdapter.getBluetoothLeScanner().startScan(scanner.getScanCallback());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT_SCAN && resultCode == Activity.RESULT_OK) {
            findBeacon();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        main.setPos(3);

        init();

        initBeacon();

        return rootView;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }
}
