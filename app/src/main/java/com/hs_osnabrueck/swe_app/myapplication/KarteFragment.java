package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hs_osnabrueck.swe_app.myapplication.adapter.MyInfoWindowAdapter;

/**
 *
 */
public class KarteFragment extends Fragment{//} implements LocationListener{

    private final int REQUEST_ENABLE_GPS = 0;

    private LayoutInflater inflater;
    private ViewGroup container;
    private Bundle savedInstanceState;
    private GoogleMap googleMap;
    private MapView mMapView;
    private View rootView;
    private MainActivity main;
    private MarkerOptions markerOptions;

    private LocationManager locationManager;
    final private double latitude_hs = 52.283127;
    final private double longitude_hs = 8.023978;
    final private double latitude_uni = 52.271098;
    final private double longitude_uni = 8.044795;
    private double latitude;
    private double longitude;
    private double latitudeButton;
    private double longitudeButton;
    private Bundle bundle;
    private String name = "";

    public KarteFragment() {}

    /**
     *
     */
    public void initMap(){

        rootView = inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_karte, container, false);

        bundle = getArguments();

        if(main.getInstitut().equals(getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.intitut_array)[0])){
            latitude = latitude_hs;
            longitude = longitude_hs;
            latitudeButton = latitude_hs;
            longitudeButton = longitude_hs;
        }else if(main.getInstitut().equals(getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.intitut_array)[1])){
            latitude = latitude_uni;
            longitude = longitude_uni;
            latitudeButton = latitude_uni;
            longitudeButton = longitude_uni;
        }else{
            //TODO Ort f�r Studieninteressierte
            latitude = latitude_uni;
            longitude = longitude_uni;
            latitudeButton = latitude_uni;
            longitudeButton = longitude_uni;
        };
        if(bundle != null){
            latitude = bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");
            name = bundle.getString("name", "AA");
        }


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
            alertDialogBuilder
                    .setMessage(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.kartescreen_GPS_deaktiviert))
                    .setCancelable(false)
                    .setPositiveButton(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.kartescreen_GPS_aktivieren_ja),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent enableIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(enableIntent, REQUEST_ENABLE_GPS);
                                }
                            });
            alertDialogBuilder.setNegativeButton(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.kartescreen_GPS_aktivieren_nein),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        mMapView = (MapView) rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.kartescreen_map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        for(int i = 0; i < main.getPoiliste().size(); i++) {
            markerOptions = new MarkerOptions()
                    .position(new LatLng(main.getPoiliste().elementAt(i).getGps_latitude(), main.getPoiliste().elementAt(i).getGps_longitude()))
                    .title(main.getPoiliste().elementAt(i).getName())
                    .snippet(main.getPoiliste().elementAt(i).getDescription());
            if(main.getPoiliste().elementAt(i).getInstitut().equals("H")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(com.hs_osnabrueck.swe_app.myapplication.R.drawable.marker_hs));
            }else if(main.getPoiliste().elementAt(i).getInstitut().equals("U")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(com.hs_osnabrueck.swe_app.myapplication.R.drawable.marker_uni));
            }else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(com.hs_osnabrueck.swe_app.myapplication.R.drawable.marker2));
            }
            if(name.equals(main.getPoiliste().elementAt(i).getName())){
                Marker marker = googleMap.addMarker(markerOptions);
                marker.showInfoWindow();
            }else{
                googleMap.addMarker(markerOptions);
            }
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(inflater));
        /*
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });
        */
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            /**
             *
             * @param marker
             * @return
             */
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.showInfoWindow();

                return false;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            /**
             *
             * @param marker
             */
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
        // inflate and return the layout

        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;

        setHasOptionsMenu(true);

        main.setPos(1);

        initMap();

        return rootView;
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     *
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_GPS && resultCode == Activity.RESULT_OK) {

        }
    }

    /**
     *
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.menu.karte, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case com.hs_osnabrueck.swe_app.myapplication.R.id.menu_karte_campus:
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitudeButton, longitudeButton)).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}