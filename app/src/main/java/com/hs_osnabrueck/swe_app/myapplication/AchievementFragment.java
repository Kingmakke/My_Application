package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hs_osnabrueck.swe_app.myapplication.ble.BleConnect;

public class AchievementFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private Bundle bundle;
    private BleConnect bleConnect;
    //public TextView temperature, iR_Temperature, humidity, pressure, accelerometer;

    public AchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_achievement, container, false);

        bundle = getArguments();

        main.setPos(5);
        main.setPos_old(bundle.getInt("pos"));

        bleConnect = new BleConnect(this, main.getBeacon());

        /*temperature = (TextView)rootView.findViewById(R.id.achievementscreen_temperature);
        temperature.setText("Temperatur: " + bleConnect.temperatur);
        iR_Temperature = (TextView)rootView.findViewById(R.id.achievementscreen_IR_temperature);
        iR_Temperature.setText("Temperatur: " + bleConnect.iR_Temperature);
        humidity = (TextView)rootView.findViewById(R.id.achievementscreen_humidity);
        pressure = (TextView)rootView.findViewById(R.id.achievementscreen_pressure);
        accelerometer = (TextView)rootView.findViewById(R.id.achievementscreen_accelerometer);
*/



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
        bleConnect.bleDisconnect();
    }
}