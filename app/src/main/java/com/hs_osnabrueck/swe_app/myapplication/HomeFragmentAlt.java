package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.adapter.MyArrayAdapter;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleConnect;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleScanner;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleUtils;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentAlt extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;

    private MainActivity main;
    private View rootView;
    private Button scan;
    private TextView score, rank, beaconinfo;
    private LayoutInflater inflater;
    private ViewGroup container;

    private Beacon beacon;
    private BleScanner scanner;
    private BluetoothAdapter btAdapter = null;
    private BleConnect bleConnect;

    public HomeFragmentAlt() {}

    public void initEvent(){
        MyArrayAdapter listAdapter = new MyArrayAdapter(rootView.getContext(), android.R.layout.simple_list_item_1);
        final List<Integer> eventitems = new ArrayList<>();
        final List<Integer> dateitems = new ArrayList<>();
        dateitems.add(0);
        int veranstaltungsanzahl;
        if(main.getEventliste().size() > 3){
            veranstaltungsanzahl = 3;
        }else{
            veranstaltungsanzahl = main.getEventliste().size();
        }
        for(int i = 0; i < veranstaltungsanzahl; i++){

            if(!listAdapter.isEmpty()){
                if(listAdapter.getItem(dateitems.get(dateitems.size() - 1)).compareTo(main.getEventliste().elementAt(i).getDate()) != 0){
                    listAdapter.addDate(main.getEventliste().elementAt(i).getDate());
                }
            }else{
                listAdapter.addDate(main.getEventliste().elementAt(i).getDate());
                dateitems.add(listAdapter.getCount()-1);
            }
            listAdapter.addVeranstaltung(main.getEventliste().elementAt(i).getName(), main.getEventliste().elementAt(i).getDescription());
            eventitems.add(listAdapter.getCount()-1);

        }
        ListView veranstaltungsListView = (ListView)rootView.findViewById(R.id.homescreen_veranstaltungsliste);
        veranstaltungsListView.setAdapter(listAdapter);
        veranstaltungsListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int pos,
                                    long arg3) {

                if(eventitems.contains(pos)){
                    Bundle bundle = new Bundle();
                    bundle.putString("title", main.getEventliste().elementAt(eventitems.indexOf(pos)).getTitle());
                    bundle.putString("date", main.getEventliste().elementAt(eventitems.indexOf(pos)).getDate());
                    bundle.putString("location",main.getEventliste().elementAt(eventitems.indexOf(pos)).getDescription());
                    bundle.putString("description", main.getEventliste().elementAt(eventitems.indexOf(pos)).getContent());
                    bundle.putInt("pos", 0);
                    Fragment fragment = new EventDetailsFragment();
                    fragment.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    main.restoreActionBar(getString(R.string.title_section10));
                    fragmentTransaction.commit();
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
        //-----bis-----
/*
        beaconinfo = (TextView)rootView.findViewById(R.id.homescreen_beaconinfo);
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

        beaconinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //beacon = scanner.getBeacon();
                if (beacon.getBluetoothDevice() != null) {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                        //noinspection deprecation
                        btAdapter.stopLeScan(scanner.getLeScanCallback());
                    }else{
                        btAdapter.getBluetoothLeScanner().stopScan(scanner.getScanCallback());
                    }
                    bleConnect = new BleConnect(main.getBaseContext(), beacon);
                    Bundle bundle = new Bundle();
                    //bundle.putString("temperature", bleConnect.getTemperature());
                    //bundle.putString("date", main.getEventliste().elementAt(eventitems.indexOf(pos)).getDate());
                    //bundle.putString("location",main.getEventliste().elementAt(eventitems.indexOf(pos)).getDescription());
                    //bundle.putString("description", main.getEventliste().elementAt(eventitems.indexOf(pos)).getContent());
                    bundle.putInt("pos", 0);
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
        scan = (Button)rootView.findViewById(R.id.homescreen_scan_button);
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
                        Log.e("debug", "stop");
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

        main.setPos(0);

        init();
        initBeacon();
        initEvent();

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
        //scanner = new BleScanner(beaconinfo, beacon);
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

}
