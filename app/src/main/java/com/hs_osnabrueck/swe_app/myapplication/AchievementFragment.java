package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AchievementFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private Bundle bundle;

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

        TextView temperature = (TextView)rootView.findViewById(R.id.achievementscreen_temperature);
        temperature.setText(bundle.getString("temperature"));

        return rootView;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }
}