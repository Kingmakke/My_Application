package com.hs_osnabrueck.swe_app.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private LayoutInflater inflater;

    public MyInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = inflater.inflate(R.layout.info_window_layout, null);
        ImageView image = (ImageView)view.findViewById(R.id.info_window_image);
        //image.setImageURI(Uri.parse("https://www.hs-osnabrueck.de/uploads/pics/Haste-2013.jpg"));

        TextView tv1 = (TextView)view.findViewById(R.id.info_window_title);
        tv1.setText(marker.getTitle());
        TextView tv2 = (TextView)view.findViewById(R.id.info_window_description);
        tv2.setText(marker.getSnippet());

        return view;
    }
}
