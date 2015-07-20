package com.hs_osnabrueck.swe_app.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.hs_osnabrueck.swe_app.myapplication.ble.BleScanner;
import com.hs_osnabrueck.swe_app.myapplication.ble.BleUtils;
import com.hs_osnabrueck.swe_app.myapplication.common.Beacon;
import com.hs_osnabrueck.swe_app.myapplication.common.Building;
import com.hs_osnabrueck.swe_app.myapplication.common.Event;
import com.hs_osnabrueck.swe_app.myapplication.common.POI;
import com.hs_osnabrueck.swe_app.myapplication.server.AsyncResponse;
import com.hs_osnabrueck.swe_app.myapplication.server.HttpGet;
import com.hs_osnabrueck.swe_app.myapplication.services.BleSearchService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AsyncResponse {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Vector<Beacon> beacons = new Vector<>();
    private Vector<Event> eventliste = new Vector<>();
    private Vector<POI> poiliste = new Vector<>();
    private Vector<Building> buildingliste = new Vector<>();
    private String urlAllPOI = "http://131.173.110.133:443/api/all/POIs";
    private String urlEvents = "http://131.173.110.133:443/api/events";
    private String urlBuildings = "http://131.173.110.133:443/api/buildings/all";

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_BT_SCAN = 1;

    private boolean backgroundScanning = false;

    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String DESRCIPTION = "description";
    private static final String CONTENT = "content";
    private static final String CATEGORY = "category";
    private static final String DATE = "pubDate";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String BEACONID = "beaconID";
    private static final String POIID = "poiID";
    private static final String NAME = "name";
    private static final String POIS = "pois";
    private static final String EVENTS = "feeds";
    private static final String BUILDINGS = "buildings";
    private static final String BUILDINGID = "buildingID";
    private static final String INSTITUT = "isBuildingBelongingTo";

    private Intent intent;

    private int pos;
    private int pos_old;

    private BluetoothAdapter btAdapter = null;
    private BleScanner bleScanner;
    private Beacon beacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.normal)));
        getSupportActionBar().setElevation(0);

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("institut", getIntent().getStringExtra("institut"));
        editor.putString("course", getIntent().getStringExtra("course"));
        editor.apply();

        intent = new Intent(getBaseContext(), BleSearchService.class);
        btAdapter = BleUtils.getBluetoothAdapter(getBaseContext());
        bleScanner = new BleScanner();

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        HttpGet httpGetPOI = new HttpGet(this);
        httpGetPOI.execute(urlAllPOI);
        httpGetPOI.asyncResponse = this;

        HttpGet httpGetEvents = new HttpGet(this);
        httpGetEvents.execute(urlEvents);
        httpGetEvents.asyncResponse = this;

        chooseFragment(getIntent().getIntExtra("pos", 0));
    }


    public void chooseFragment(int position){FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new EventFragment(), "0")
                        .commit();
                restoreActionBar(getString(R.string.Versanstaltungsscreen));
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new KarteFragment(), "1")
                        .commit();
                restoreActionBar(getString(R.string.Kartenscreen));
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new BuildingsFragment(), "2")
                        .commit();
                restoreActionBar(getString(R.string.Buildingsscreen));
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new BeaconFragment(), "3")
                        .commit();
                restoreActionBar(getString(R.string.Beaconsuchescreen));
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new PalmenFragment(), "4")
                        .commit();
                restoreActionBar(getString(R.string.Palmenscreen));
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new WasIstEinBeaconFragment(), "5")
                        .commit();
                restoreActionBar(getString(R.string.WieBscreen));
                break;
            case 6:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new EinstellungenFragment(), "6")
                        .commit();
                restoreActionBar(getString(R.string.Einstellungsscreen));
                break;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        chooseFragment(position);

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

    public void addPOIs(JSONArray jsonArray){
        poiliste.removeAllElements();
        try {
            //JSONArray jsonArray = json.getJSONArray("pois");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                poiliste.add(new POI(
                        jsonObject.getString(BEACONID),
                        jsonObject.getString(DESRCIPTION),
                        Double.valueOf(jsonObject.getString(LATITUDE)),
                        Double.valueOf(jsonObject.getString(LONGITUDE)),
                        Integer.valueOf(jsonObject.getString(POIID)),
                        jsonObject.getString(NAME),
                        jsonObject.getString(INSTITUT)
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addEvents(JSONArray jsonArray){
        eventliste.removeAllElements();
        try {
            Vector<Event> eventListeReverse = new Vector<>();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                eventListeReverse.add(new Event(
                                jsonObject.getString(CATEGORY),
                                jsonObject.getString(DATE),
                                jsonObject.getString(DESRCIPTION),
                                jsonObject.getString(LINK),
                                jsonObject.getString(TITLE)
                        )
                );
            }
            for(int i = eventListeReverse.size()-1; i >= 0 ;  i--){
                eventliste.add(eventListeReverse.elementAt(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//TODO mit serverleuten sprechen
/*
    public void addBuildings(JSONArray jsonArray){
        poiliste.removeAllElements();
        try {
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                buildingliste.add(new Building(
                                Integer.valueOf(jsonObject.getString(BUILDINGID)),
                                jsonObject.getString(NAME),
                                jsonObject.getString(DESRCIPTION),
                                Double.valueOf(jsonObject.getString(LATITUDE)),
                                Double.valueOf(jsonObject.getString(LONGITUDE))
                        )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
*/
    @Override
    public void onBackPressed() {
        if(mNavigationDrawerFragment.isVisible()){
            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
        }else if(pos == 0){
            finish();
        }else if(pos == 9 && pos_old == 3){
            Fragment fragment = new BeaconFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, "3");
            restoreActionBar(getString(R.string.Beaconsuchescreen));
            fragmentTransaction.commit();
        }else{
            Fragment fragment = new EventFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, "0");
            restoreActionBar(getString(R.string.Versanstaltungsscreen));
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

    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    public BleScanner getBleScanner() {
        return bleScanner;
    }

    public void setBleScanner(BleScanner bleScanner) {
        this.bleScanner = bleScanner;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public Intent getMyIntent() {
        return intent;
    }

    public void setMyIntent(Intent intent) {
        this.intent = intent;
    }

    public boolean isBackgroundScanning() {
        return backgroundScanning;
    }

    public void setBackgroundScanning(boolean backgroundScanning) {
        this.backgroundScanning = backgroundScanning;
    }

    @Override
    public void processFinish(JSONObject output) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        try {
            addEvents(output.getJSONArray(EVENTS));
            if(fragmentManager.findFragmentById(R.id.container).getTag().equals("0")){
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new EventFragment(), "0");
                restoreActionBar(getString(R.string.Versanstaltungsscreen));
                fragmentTransaction.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addPOIs(output.getJSONArray(POIS));
            if(fragmentManager.findFragmentById(R.id.container).getTag().equals("1")){
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new KarteFragment(), "1");
                restoreActionBar(getString(R.string.Kartenscreen));
                fragmentTransaction.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*try {
            addBuildings(output.getJSONArray(BUILDINGS));
            if(fragmentManager.findFragmentById(R.id.container).getTag().equals("2")){
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new BeaconFragment(), "2");
                restoreActionBar(getString(R.string.Buildingsscreen));
                fragmentTransaction.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(backgroundScanning && btAdapter != null && bleScanner != null){
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(intent != null){
            stopService(intent);
        }
    }
}
