package com.hs_osnabrueck.swe_app.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EinkaufswagenFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;
    private static final long SCAN_PERIOD = 1000;

    private View rootView;
    private Button scan;
    private TextView beaconinfo;
    private LayoutInflater inflater;
    private ViewGroup container;

    private Beacon beacon = new Beacon("", "", -100);

    private BluetoothAdapter btAdapter = null;
    private Boolean btActive = true;

    public EinkaufswagenFragment() {}

    public void initBeacon(){
        rootView = inflater.inflate(R.layout.fragment_einkaufswagen, container, false);

        //TODO  zum Testen auf dem Emulator auskommentieren
        //-----von-----
        /*final int bleStatus = BleUtils.getBleStatus(getActivity().getBaseContext());
        switch (bleStatus) {
            case BleUtils.STATUS_BLE_NOT_AVAILABLE:
                btActive = false;
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
                //ErrorDialog.newInstance(R.string.dialog_error_no_ble).show(getFragmentManager(), ErrorDialog.TAG);
                //return;
            case BleUtils.STATUS_BLUETOOTH_NOT_AVAILABLE:
                btActive = false;
                Intent enableIntent2 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent2,REQUEST_ENABLE_BT);
                // ErrorDialog.newInstance(R.string.dialog_error_no_bluetooth).show(getFragmentManager(), ErrorDialog.TAG);
                // return;
            default:
                btAdapter = BleUtils.getBluetoothAdapter(rootView.getContext());
        }*/
        //-----bis-----

        /*  alt
        btManager = (BluetoothManager)getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }
        */

        beaconinfo = (TextView)rootView.findViewById(R.id.einkaufswagenscreen_beaconinfo);
        beaconinfo.setBackgroundResource(R.drawable.cell_shape_beaconinfo);
        beaconinfo.setGravity(Gravity.CENTER_VERTICAL);
        beaconinfo.setCompoundDrawablePadding(50);
        if(beacon.getName().compareTo("SensorTag")==0) {
            beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
            beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
        }else if(beacon.getName().compareTo("Estimote")==0){
            beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
            beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
        }else{
            beaconinfo.setText(getString(R.string.homescreen_kein_Beacon));
            beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
        }

        beaconinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                // auf Karte wechseln
            }
        });

        //TODO  zum Testen auf dem Emulator auskommentieren
        //-----von-----
        /*scanner = new BleScanner(btAdapter, new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                if(rssi > beacon.getRssi() ){
                    beacon = new Beacon(device.getName(), device.getAddress(), rssi);
                }
            }
        });
        scanner.setScanPeriod(SCAN_PERIOD);*/
        //-----bis-----
        scan = (Button)rootView.findViewById(R.id.einkaufswagenscreen_scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btAdapter != null && !btAdapter.isEnabled()) {
                    btActive = false;
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT_SCAN);
                }
                /* alt
                bs = new BeaconSearch();
                new Timer().execute();
                btAdapter.startLeScan(bs.getLeScanCallback());
                beacon = new Beacon(bs.getBeacon());
                */
                //findBeacon(btActive);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        initBeacon();

        return rootView;
    }
}
