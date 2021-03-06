package com.hs_osnabrueck.swe_app.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.hs_osnabrueck.swe_app.myapplication.R;

/**
 * InfoWindowAdapter class for Google Maps
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private LayoutInflater inflater;

    /**
     * Constructor which sets the inflater variable
     * @param inflater
     */
    public MyInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     *
     * @param marker
     * @return view
     */
    @Override
    public View getInfoContents(final Marker marker) {
        View view = inflater.inflate(R.layout.info_window_layout, null);
        TextView tv1 = (TextView)view.findViewById(R.id.info_window_title);
        tv1.setText(marker.getTitle());
        TextView tv2 = (TextView)view.findViewById(R.id.info_window_description);
        tv2.setText(marker.getSnippet());

        return view;
    }
}
