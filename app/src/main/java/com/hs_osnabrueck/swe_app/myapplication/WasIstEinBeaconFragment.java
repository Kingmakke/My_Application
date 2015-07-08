package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WasIstEinBeaconFragment extends Fragment {

    private View rootView;
    private MainActivity main;

    public WasIstEinBeaconFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_was_ist_ein_beacon, container, false);
        main.setPos(5);

        return rootView;
    }
    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }
}
