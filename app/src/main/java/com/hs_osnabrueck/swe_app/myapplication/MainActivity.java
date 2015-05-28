package com.hs_osnabrueck.swe_app.myapplication;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.common.Event;
import com.hs_osnabrueck.swe_app.myapplication.common.POI;
import com.hs_osnabrueck.swe_app.myapplication.server.HttpConnection;

import java.util.Vector;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Vector<Beacon> beacons = new Vector<>();
    private Vector<Event> eventliste = new Vector<>();
    private Vector<POI> poiliste = new Vector<>();
    private String urlServer = "http://131.173.110.133:443/api";
    private String urlAllPOI = "http://131.173.110.133:443/api/all/POIs";
    private String urlEvents = "http://131.173.110.133:443/api/events";

    private int pos;
    private int pos_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.normal)));

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        HttpConnection connectionPOI = new HttpConnection();
        connectionPOI.execute(urlAllPOI, "put");
        while(!connectionPOI.isExecuted()) {

        }
        addPOIs(connectionPOI.getResultHttpConnection());
        HttpConnection connectionEvents = new HttpConnection();
        connectionEvents.execute(urlEvents, "put");
        while(!connectionEvents.isExecuted()){
        }
        addEvents(connectionEvents.getResultHttpConnection());


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section1));
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new KarteFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section2));
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new EventFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section3));
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new EinkaufswagenFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section4));
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new SpielstatistikFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section5));
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AchievementFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section6));
                break;
            case 6:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new WasIstEinBeaconFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section7));
                break;
            case 7:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new EinstellungenFragment())
                        .commit();
                restoreActionBar(getString(R.string.title_section8));
                break;
        }
    }

    public void restoreActionBar(String mTitle) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public Vector<Beacon> getBeacons() {
        return beacons;
    }

    public void addBeacon(Beacon beacon) {
        beacons.addElement(beacon);
    }

    public void setBeacons(Vector<Beacon> beacons) {
        this.beacons = beacons;
    }

    public Vector<Event> getEventliste() {
        return eventliste;
    }

    public void addEvent(Event veranstaltung) {
        eventliste.addElement(veranstaltung);
    }

    public void setEventliste(Vector<Event> veranstaltungsliste) {
        this.eventliste = veranstaltungsliste;
    }

    public Vector<POI> getPoiliste() {
        return poiliste;
    }

    public void setPoiliste(Vector<POI> poiliste) {
        this.poiliste = poiliste;
    }

    public void addPOIs(String json){
        if(!json.isEmpty()){
            json = json.substring(1,json.length()-1);
            //json = json.replaceAll("\\},\\{", "\\};\\{");
            String[] temp = json.split("\\},\\{");
            poiliste.removeAllElements();
            for(int i = 0; i < temp.length; i++){
                poiliste.add(new POI(temp[i]));
            }
        }
    }

    public void addEvents(String json){
        if(!json.isEmpty()){
            json = json.substring(json.indexOf('[') + 1, json.indexOf(']') - 1);
            String[] temp = json.split("\\},\\{");
            eventliste.removeAllElements();
            for(int i = 0; i < temp.length; i++){
                eventliste.add(new Event(temp[i]));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(pos == 0){
            finish();
        }else if(pos == 8 && pos_old == 2){
            Fragment fragment = new EventFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }else{
            Fragment fragment = new HomeFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos_old() {return pos_old;}

    public void setPos_old(int pos_old) {this.pos_old = pos_old;}
}
