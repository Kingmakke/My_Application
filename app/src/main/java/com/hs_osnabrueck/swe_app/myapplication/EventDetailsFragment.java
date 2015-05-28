package com.hs_osnabrueck.swe_app.myapplication;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventDetailsFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private TextView title, date, location, description;
    private Bundle bundle;

    public EventDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        bundle = getArguments();

        main.setPos(8);
        main.setPos_old(bundle.getInt("pos"));

        title = (TextView)rootView.findViewById(R.id.event_details_title);
        title.setText(bundle.getString("title"));

        date = (TextView)rootView.findViewById(R.id.event_details_date);
        date.setText(bundle.getString("date"));

        location = (TextView)rootView.findViewById(R.id.event_details_location);
        location.setText(bundle.getString("location"));

        description = (TextView)rootView.findViewById(R.id.event_details_description);
        description.setText(bundle.getString("description"));

        return rootView;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

}
