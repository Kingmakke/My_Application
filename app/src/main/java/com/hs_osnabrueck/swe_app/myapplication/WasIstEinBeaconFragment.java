package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hs_osnabrueck.swe_app.myapplication.server.HttpConnection;

public class WasIstEinBeaconFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private Button update;
    private String urlUpdate = "http://131.173.110.133:443/api/updatePOIHumidity";
    private TextView result;

    public WasIstEinBeaconFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_was_ist_ein_beacon, container, false);
        main.setPos(6);

        result = (TextView) rootView.findViewById(R.id.wiebscreen_result);

        update = (Button)rootView.findViewById(R.id.wiebscreen_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {*/
                        HttpConnection connectionEvents = new HttpConnection();
                        connectionEvents.execute(urlUpdate, "BeaconID-G1", String.valueOf(40.40));
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(connectionEvents.getResultHttpConnection() != null){
                            result.setText(connectionEvents.getResultHttpConnection().toString());
                        }else{
                            result.setText("null");
                        }



                   /* }
                });*/



            }
        });

        return rootView;
    }
    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }
}
