package com.hs_osnabrueck.swe_app.myapplication;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.Vector;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Vector<Beacon> beacons = new Vector<>();
    private Vector<Veranstaltung> veranstaltungsliste = new Vector<>();
    private Vector<POI> poiliste = new Vector<>();
    private String urlServer = "http://131.173.110.133:443/api/";
    private String urlAllPOI = "http://131.173.110.133:443/api/all/POIs";


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

        veranstaltungsliste.add(new Veranstaltung("2015-5-20", "Test Veranstaltung", "testtesttesttesttesttesttesttesttesttest"));

        HttpConnection connection = new HttpConnection();
        connection.execute(urlAllPOI, "put");
        while(!connection.isExecuted()){
            //Log.e("debug executed", String.valueOf(connection.isExecuted()));
        }
        addPOIs(connection.getResultHttpConnection());


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
                        .replace(R.id.container, new VeranstaltungenFragment())
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

    public Vector<Veranstaltung> getVeranstaltungsliste() {
        return veranstaltungsliste;
    }

    public void addVeranstaltung(Veranstaltung veranstaltung) {
        veranstaltungsliste.addElement(veranstaltung);
    }

    public void setVeranstaltungsliste(Vector<Veranstaltung> veranstaltungsliste) {
        this.veranstaltungsliste = veranstaltungsliste;
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
            json = json.replaceAll("\\},\\{", "\\};\\{");
            String[] temp = json.split(";");
            poiliste.removeAllElements();
            for(int i = 0; i < temp.length; i++){
                poiliste.add(new POI(temp[i]));
            }
        }


    }

}
