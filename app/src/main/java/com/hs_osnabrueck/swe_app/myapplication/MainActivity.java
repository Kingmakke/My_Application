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
 *
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
     *
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
     *
     * @param position
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
     *
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        chooseFragment(position);

    }

    /**
     *
     * @param mTitle
     */
    public void restoreActionBar(String mTitle) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     *
     * @return
     */
    public Vector<Beacon> getBeacons() {
        return beacons;
    }

    /**
     *
     * @param beacon
     */
    public void addBeacon(Beacon beacon) {
        beacons.addElement(beacon);
    }

    /**
     *
     * @param beacons
     */
    public void setBeacons(Vector<Beacon> beacons) {
        this.beacons = beacons;
    }

    /**
     *
     * @return
     */
    public Vector<Event> getEventliste() {
        return eventliste;
    }

    /**
     *
     * @param veranstaltung
     */
    public void addEvent(Event veranstaltung) {
        eventliste.addElement(veranstaltung);
    }

    /**
     *
     * @param veranstaltungsliste
     */
    public void setEventliste(Vector<Event> veranstaltungsliste) {
        this.eventliste = veranstaltungsliste;
    }

    /**
     *
     * @return
     */
    public Vector<POI> getPoiliste() {
        return poiliste;
    }

    /**
     *
     * @param poiliste
     */
    public void setPoiliste(Vector<POI> poiliste) {
        this.poiliste = poiliste;
    }

    /**
     *
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
     *
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
     *
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
     *
     * @return
     */
    public int getPos() {
        return pos;
    }

    /**
     *
     * @param pos
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     *
     * @return
     */
    public int getPos_old() {return pos_old;}

    /**
     *
     * @param pos_old
     */
    public void setPos_old(int pos_old) {this.pos_old = pos_old;}

    /**
     *
     * @return
     */
    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    /**
     *
     * @return
     */
    public BleScanner getBleScanner() {
        return bleScanner;
    }

    /**
     *
     * @param bleScanner
     */
    public void setBleScanner(BleScanner bleScanner) {
        this.bleScanner = bleScanner;
    }

    /**
     *
     * @return
     */
    public Beacon getBeacon() {
        return beacon;
    }

    /**
     *
     * @param beacon
     */
    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    /**
     *
     * @return
     */
    public Intent getMyIntent() {
        return intent;
    }

    /**
     *
     * @param intent
     */
    public void setMyIntent(Intent intent) {
        this.intent = intent;
    }

    /**
     *
     * @return
     */
    public boolean isBackgroundScanning() {
        return backgroundScanning;
    }

    /**
     *
     * @param backgroundScanning
     */
    public void setBackgroundScanning(boolean backgroundScanning) {
        this.backgroundScanning = backgroundScanning;
    }

    /**
     *
     * @return
     */
    public String getCourse() {
        return course;
    }

    /**
     *
     * @param course
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     *
     * @return
     */
    public String getInstitut() {
        return institut;
    }

    /**
     *
     * @param institut
     */
    public void setInstitut(String institut) {
        this.institut = institut;
    }

    /**
     *
     * @return
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
     *
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
     *
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
