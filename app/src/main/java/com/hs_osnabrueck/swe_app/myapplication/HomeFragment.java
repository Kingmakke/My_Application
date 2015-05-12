package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;
    private static final long SCAN_PERIOD = 1000;

    private MainActivity main;
    private View rootView;
    private Button scan;
    private TextView score, rank, beaconinfo;
    private LayoutInflater inflater;
    private ViewGroup container;

    private Beacon beacon = new Beacon("", "", -120);
    private BleScanner scanner;
    private BluetoothAdapter btAdapter = null;
    private Boolean btActive = true;

    public HomeFragment() {}

    public void initVeranstaltung(){
        MyArrayAdapter listAdapter = new MyArrayAdapter(rootView.getContext(), android.R.layout.simple_list_item_1);
        final List<Integer> veranstaltungsitems = new ArrayList<>();
        final List<Integer> dateitems = new ArrayList<>();
        dateitems.add(0);
        int veranstaltungsanzahl;
        if(main.getVeranstaltungsliste().size() > 3){
            veranstaltungsanzahl = 3;
        }else{
            veranstaltungsanzahl = main.getVeranstaltungsliste().size();
        }
        for(int i = 0; i < veranstaltungsanzahl; i++){

            if(!listAdapter.isEmpty()){
                if(listAdapter.getItem(dateitems.size() - 1).compareTo(main.getVeranstaltungsliste().elementAt(i).getDate()) != 0){
                    listAdapter.addDate(main.getVeranstaltungsliste().elementAt(i).getDate());
                }
            }else{
                listAdapter.addDate(main.getVeranstaltungsliste().elementAt(i).getDate());
                dateitems.add(listAdapter.getCount()-1);
            }
            listAdapter.addVeranstaltung(main.getVeranstaltungsliste().elementAt(i).getName(), main.getVeranstaltungsliste().elementAt(i).getDescription());
            veranstaltungsitems.add(listAdapter.getCount()-1);

        }
        ListView veranstaltungsListView = (ListView)rootView.findViewById(R.id.homescreen_veranstaltungsliste);
        veranstaltungsListView.setAdapter(listAdapter);
        veranstaltungsListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int pos,
                                    long arg3) {

                if(veranstaltungsitems.contains(pos)){
                    view.setBackgroundColor(getResources().getColor(R.color.grey_light));
                    beaconinfo.setText(getString(R.string.homescreen_kein_Beacon));
                    beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
                }
            }

        });
    }

    public void init() {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        score = (TextView) rootView.findViewById(R.id.homescreen_score);
        score.setText("Score: 20");

        rank = (TextView) rootView.findViewById(R.id.homescreen_rank);
        rank.setText("Rank: 2");

    }

    public void initBeacon(){

        //TODO  zum Testen auf dem Emulator auskommentieren
        //-----von-----
        final int bleStatus = BleUtils.getBleStatus(getActivity().getBaseContext());
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
        }
        //-----bis-----

        beaconinfo = (TextView)rootView.findViewById(R.id.homescreen_beaconinfo);
        beaconinfo.setBackgroundResource(R.drawable.cell_shape_beaconinfo);
        beaconinfo.setGravity(Gravity.CENTER_VERTICAL);
        beaconinfo.setCompoundDrawablePadding(50);
        if(beacon.getName().compareTo("SensorTag")==0) {
            beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
            beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
        }else if(beacon.getName().compareTo("estimote")==0){
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
        scanner = new BleScanner(btAdapter, new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                if(rssi > beacon.getRssi() ){
                    beacon = new Beacon(device.getName(), device.getAddress(), rssi);
                }
            }
        });
        scanner.setScanPeriod(SCAN_PERIOD);
        scanner.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanner.stop();
        //-----bis-----
        scan = (Button)rootView.findViewById(R.id.homescreen_scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btAdapter != null && !btAdapter.isEnabled()) {
                    btActive = false;
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT_SCAN);
                } else {
                    btActive = true;
                    findBeacon(btActive);
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        init();
        initBeacon();
        initVeranstaltung();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT_SCAN && resultCode == Activity.RESULT_OK) {
            btActive = true;
            findBeacon(btActive);
        }else if(requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            btActive = true;
        }
    }

    public void findBeacon(Boolean btActive){
        if(btActive){
            scanner.start();
            try {
                Thread.sleep(SCAN_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scanner.stop();
            if(beacon.getName().compareTo("SensorTag")==0) {
                beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sensortag, 0, 0, 0);
                beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
            }else if(beacon.getName().compareTo("estimote")==0){
                beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
                beaconinfo.setText(beacon.getName() + "\n" + beacon.getId() + "\n" + beacon.getRssi());
            }else{
                beaconinfo.setText(getString(R.string.homescreen_kein_Beacon));
                beaconinfo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.estimote, 0, 0, 0);
            }
        }
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }
}
