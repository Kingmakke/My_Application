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

/**
 * MainActivity Fragment
 */
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
    private String institut;
    private String course;
    private int pos;
    private int pos_old;

    private BluetoothAdapter btAdapter = null;
    private BleScanner bleScanner;
    private Beacon beacon;

    /**
     * sets basic functionality for the app to work
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(com.hs_osnabrueck.swe_app.myapplication.R.layout.activity_main);


        getSupportActionBar().setElevation(0);

        intent = new Intent(getBaseContext(), BleSearchService.class);
        btAdapter = BleUtils.getBluetoothAdapter(getBaseContext());
        bleScanner = new BleScanner();

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(com.hs_osnabrueck.swe_app.myapplication.R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(com.hs_osnabrueck.swe_app.myapplication.R.id.navigation_drawer, (DrawerLayout) findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.drawer_layout));

        HttpGet httpGetPOI = new HttpGet(this);
        httpGetPOI.execute(urlAllPOI);
        httpGetPOI.asyncResponse = this;

        HttpGet httpGetEvents = new HttpGet(this);
        httpGetEvents.execute(urlEvents);
        httpGetEvents.asyncResponse = this;

        chooseFragment(getIntent().getIntExtra("pos", 0));
    }

    /**
     * choose one of the seven fragments which should be shown in the Activity
     * @param position position of the desired fragment
     */
    public void chooseFragment(int position){FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new EventFragment(), "0")
                        .commit();
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Versanstaltungsscreen));
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new KarteFragment(), "1")
                        .commit();
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Kartenscreen));
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new BuildingsFragment(), "2")
                        .commit();
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Buildingsscreen));
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new BeaconFragment(), "3")
                        .commit();
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Beaconsuchescreen));
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new WasIstEinePalmeFragment(), "4")
                        .commit();
                restoreActionBar(getString(R.string.WiePscreen));
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new WasIstEinBeaconFragment(), "5")
                        .commit();
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.WieBscreen));
                break;
            case 6:
                fragmentManager.beginTransaction()
                        .replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new EinstellungenFragment(), "6")
                        .commit();
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Einstellungsscreen));
                break;
        }
    }

    /**
     * defines what happens when a menu entry is clicked (change the fragment)
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        chooseFragment(position);

    }

    /**
     * restores the actionbar after the fragment has changed
     * @param mTitle title of the new displayed fragment
     */
    public void restoreActionBar(String mTitle) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * returns the beacons
     * @return beacons
     */
    public Vector<Beacon> getBeacons() {
        return beacons;
    }

    /**
     * add a beacon to the Beacons vector
     * @param beacon
     */
    public void addBeacon(Beacon beacon) {
        beacons.addElement(beacon);
    }

    /**
     * sets the beacons vector
     * @param beacons
     */
    public void setBeacons(Vector<Beacon> beacons) {
        this.beacons = beacons;
    }

    /**
     * returns the events vector
     * @return eventliste
     */
    public Vector<Event> getEventliste() {
        return eventliste;
    }

    /**
     * add event to the events vector
     * @param veranstaltung
     */
    public void addEvent(Event veranstaltung) {
        eventliste.addElement(veranstaltung);
    }

    /**
     * sets the events vector
     * @param veranstaltungsliste
     */
    public void setEventliste(Vector<Event> veranstaltungsliste) {
        this.eventliste = veranstaltungsliste;
    }

    /**
     * returs the Points of Interest vector
     * @return poiliste
     */
    public Vector<POI> getPoiliste() {
        return poiliste;
    }

    /**
     * sets the Points of Interest vector
     * @param poiliste
     */
    public void setPoiliste(Vector<POI> poiliste) {
        this.poiliste = poiliste;
    }

    /**
     * adds POIs from the jsonArray from the HTTPGet to the poiliste
     * @param jsonArray
     */
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

    /**
     * adds events from the jsonArray from the HTTPGet to the eventliste
     * @param jsonArray
     */
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
                                jsonObject.getString(TITLE),
                                jsonObject.getString(CONTENT)
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

    /**
     * defines what happens when the back button is pressed. Pressed on the 'home' screen closes the app
     */
    @Override
    public void onBackPressed() {
        if(mNavigationDrawerFragment.isVisible()){
            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
        }else if(pos == 0){
            finish();
        }else if(pos == 9 && pos_old == 3){
            Fragment fragment = new BeaconFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, fragment, "3");
            restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Beaconsuchescreen));
            fragmentTransaction.commit();
        }else{
            Fragment fragment = new EventFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, fragment, "0");
            restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Versanstaltungsscreen));
            fragmentTransaction.commit();
        }
    }

    /**
     * returns the position of the fragment
     * @return pos
     */
    public int getPos() {
        return pos;
    }

    /**
     * sets the position of the fragment
     * @param pos
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * returns the position of the fragment
     * @return pos_old
     */
    public int getPos_old() {return pos_old;}

    /**
     * sets the position of the fragment
     * @param pos_old
     */
    public void setPos_old(int pos_old) {this.pos_old = pos_old;}

    /**
     * returns the BluetoothAdapter
     * @return btAdapter
     */
    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    /**
     * returns the BleScanner
     * @return bleScanner
     */
    public BleScanner getBleScanner() {
        return bleScanner;
    }

    /**
     * sets the BleScanner
     * @param bleScanner
     */
    public void setBleScanner(BleScanner bleScanner) {
        this.bleScanner = bleScanner;
    }

    /**
     * returns the Beacon
     * @return beacon
     */
    public Beacon getBeacon() {
        return beacon;
    }

    /**
     * sets the Beacon
     * @param beacon
     */
    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    /**
     * returns the intent
     * @return intent
     */
    public Intent getMyIntent() {
        return intent;
    }

    /**
     * sets the intent
     * @param intent
     */
    public void setMyIntent(Intent intent) {
        this.intent = intent;
    }

    /**
     * returns whether backgroundScanning is enabled or not
     * @return backgroundScanning
     */
    public boolean isBackgroundScanning() {
        return backgroundScanning;
    }

    /**
     * sets backgroundScanning true or false
     * @param backgroundScanning
     */
    public void setBackgroundScanning(boolean backgroundScanning) {
        this.backgroundScanning = backgroundScanning;
    }

    /**
     * returns the course
     * @return course
     */
    public String getCourse() {
        return course;
    }

    /**
     * sets the course
     * @param course
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * returns the institute
     * @return institut
     */
    public String getInstitut() {
        return institut;
    }

    /**
     * sets the institute
     * @param institut
     */
    public void setInstitut(String institut) {
        this.institut = institut;
    }

    /**
     * returns the NavigationDrawerFragment
     * @return mNavigationDrawerFragment
     */
    public NavigationDrawerFragment getmNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

    /**
     *
     * @param output
     */
    @Override
    public void processFinish(JSONObject output) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        try {
            addEvents(output.getJSONArray(EVENTS));
            if(fragmentManager.findFragmentById(com.hs_osnabrueck.swe_app.myapplication.R.id.container).getTag().equals("0")){
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new EventFragment(), "0");
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Versanstaltungsscreen));
                fragmentTransaction.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addPOIs(output.getJSONArray(POIS));
            if(fragmentManager.findFragmentById(com.hs_osnabrueck.swe_app.myapplication.R.id.container).getTag().equals("1")){
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(com.hs_osnabrueck.swe_app.myapplication.R.id.container, new KarteFragment(), "1");
                restoreActionBar(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Kartenscreen));
                fragmentTransaction.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * saves preferences in file and starts backgroundScanning (if enabled) if app is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("settings",MODE_PRIVATE).edit();
        editor.putString("institut", institut);
        editor.putString("course", course);
        editor.putBoolean("scanning", backgroundScanning);
        editor.commit();
        if(backgroundScanning && btAdapter != null && bleScanner != null){
            startService(intent);
        }
    }

    /**
     * loads preferences from file and stops backgroundScanning (if enabled) when app is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        institut = prefs.getString("institut", "Hochschule");
        course = prefs.getString("course", "Medieninformatik");
        backgroundScanning = prefs.getBoolean("scanning", false);
        if(institut.equals(getResources().getStringArray(R.array.intitut_array)[0])){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal)));
        }else if(institut.equals(getResources().getStringArray(R.array.intitut_array)[1])){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal_uni)));
        }else{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal_int)));
        }
        if(intent != null && btAdapter != null && btAdapter.isEnabled()){
            stopService(intent);
        }else{
            backgroundScanning = false;
        }
    }
}
