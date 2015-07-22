package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hs_osnabrueck.swe_app.myapplication.ble.BleConnect;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleScanner;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;

/**
 *
 */
public class WasIstEinePalmeFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;
    private static final long SCAN_PERIOD = 1000;

    private View rootView;
    private Button download;
    private LayoutInflater inflater;
    private ViewGroup container;
    private MainActivity main;

    private Beacon beacon;
    private BluetoothAdapter btAdapter = null;
    private BleScanner scanner;
    private BleConnect bleConnect;

    public WasIstEinePalmeFragment() {}

    /**
     *
     */
    public void init(){
        rootView = inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_wiep, container, false);

        download = (Button)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.einkaufswagenscreen_download_button);
        if(main.getInstitut().equals(getResources().getStringArray(R.array.intitut_array)[0])){
            download.setBackgroundColor(main.getResources().getColor(R.color.normal));
        }else if(main.getInstitut().equals(getResources().getStringArray(R.array.intitut_array)[1])){
            download.setBackgroundColor(main.getResources().getColor(R.color.normal_uni));
        }else{
            download.setBackgroundColor(main.getResources().getColor(R.color.normal_int));
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=osnabrueck.greencity"));
                startActivity(intent);

            }
        });
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;

        main.setPos(4);

        init();

        return rootView;
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
}
