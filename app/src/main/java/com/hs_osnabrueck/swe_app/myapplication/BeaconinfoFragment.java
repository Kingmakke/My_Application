package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.ble.BleConnect;
import com.hs_osnabrueck.swe_app.myapplication.server.HttpConnection;

public class BeaconinfoFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private Bundle bundle;
    private BleConnect bleConnect;
    private Button update;

    private String urlUpdate = "http://131.173.110.133:443/api/updatePOIHumidity";
    public TextView temperature, iR_Temperature, humidity, pressure, accelerometer, result;


    public BeaconinfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_achievement, container, false);
        bundle = getArguments();

        main.setPos_old(bundle.getInt("pos"));

        main.setPos(9);


        bleConnect = new BleConnect(this, main.getBeacon());

        update = (Button)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.achievementscreen_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(humidity != null){
                    HttpConnection connectionEvents = new HttpConnection(getActivity().getBaseContext());
                    connectionEvents.execute(urlUpdate, main.getBeacon().getId(), humidity.getText().toString());

                }
            }
        });



        return rootView;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("debug", "pause");
        bleConnect.bleDisconnect(main.getBeacon());
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e("debug", "stop");
        bleConnect.bleDisconnect(main.getBeacon());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bleConnect = new BleConnect(this, main.getBeacon());
    }
}