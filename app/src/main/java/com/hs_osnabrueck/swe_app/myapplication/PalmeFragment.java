package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.ble.BleConnect;
import com.hs_osnabrueck.swe_app.myapplication.server.AsyncResponse;
import com.hs_osnabrueck.swe_app.myapplication.server.HttpPut;

import org.json.JSONObject;

/**
 * Palm Fragment
 */
public class PalmeFragment extends Fragment implements AsyncResponse{

    private View rootView;
    private MainActivity main;
    private Bundle bundle;
    private BleConnect bleConnect;
    private Button update;

    private String urlUpdate = "http://131.173.110.133:443/api/updatePOIHumidity";
    public TextView humidity;


    public PalmeFragment() {}

    /**
     * gets called when the the fragment loads
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_palme, container, false);
        bundle = getArguments();

        main.setPos_old(bundle.getInt("pos"));

        main.setPos(9);

        bleConnect = new BleConnect(this, main.getBeacon());

        update = (Button)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.palmescreen_update);
        if(main.getInstitut().equals(getResources().getStringArray(R.array.intitut_array)[0])){
            update.setBackgroundColor(main.getResources().getColor(R.color.normal));
        }else if(main.getInstitut().equals(getResources().getStringArray(R.array.intitut_array)[1])){
            update.setBackgroundColor(main.getResources().getColor(R.color.normal_uni));
        }else{
            update.setBackgroundColor(main.getResources().getColor(R.color.normal_int));
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(humidity != null){
                    HttpPut connectionEvents = new HttpPut();
                    connectionEvents.asyncResponse = PalmeFragment.this;
                    //TODO humidity.getText() liefert nicht nur den wert
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

    /**
     * when app is minimized, bluetooth connection will be disconnected
     */
    @Override
    public void onPause(){
        super.onPause();
        bleConnect.bleDisconnect(main.getBeacon());
    }

    /**
     * when the app is closed, bluetooth connection will be disconnected
     */
    @Override
    public void onStop(){
        super.onStop();
        bleConnect.bleDisconnect(main.getBeacon());
    }

    /**
     * is called when the fragment is loaded
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bleConnect = new BleConnect(this, main.getBeacon());
    }

    @Override
    public void processFinish(JSONObject output) {

    }

    /**
     * returns the main
     * @return main
     */
    public MainActivity getMain() {
        return main;
    }
}