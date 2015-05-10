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
    private Vector<Beacon> beacons = new Vector<Beacon>();
    private Vector<Veranstaltung> veranstaltungsliste = new Vector<Veranstaltung>();

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

    /*    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
            case 8:
                mTitle = getString(R.string.title_section8);
                break;
        }
    }*/

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
