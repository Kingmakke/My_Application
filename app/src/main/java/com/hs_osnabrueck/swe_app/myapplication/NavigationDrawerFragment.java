package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hs_osnabrueck.swe_app.myapplication.adapter.MyDrawerAdapter;
import com.hs_osnabrueck.swe_app.myapplication.common.NavItem;

import java.util.ArrayList;

/**
 * NavigationDrawer Fragment
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private MainActivity main;

    ArrayList<NavItem> mNavItems = new ArrayList<>();

    public NavigationDrawerFragment() {
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    /**
     * Indicate that this fragment would like to influence the set of actions in the action bar.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * adds navigation items to the menu list
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_navigation_drawer, container, false);
        SharedPreferences prefs = main.getSharedPreferences("settings", main.MODE_PRIVATE);
        if(prefs.getString("institut", "Hochschule").equals(getResources().getStringArray(R.array.intitut_array)[0])){
            mDrawerListView.setSelector(R.drawable.drawer_list_selector_hs);
        }else if(prefs.getString("institut", "Hochschule").equals(getResources().getStringArray(R.array.intitut_array)[1])){
            mDrawerListView.setSelector(R.drawable.drawer_list_selector_uni);
        }else{
            mDrawerListView.setSelector(R.drawable.drawer_list_selector_int);
        }
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        //TODO andere Icons
        mNavItems.add(new NavItem(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Versanstaltungsscreen), com.hs_osnabrueck.swe_app.myapplication.R.drawable.ic_action_go_to_today));
        mNavItems.add(new NavItem(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Kartenscreen), com.hs_osnabrueck.swe_app.myapplication.R.drawable.ic_action_map));
        mNavItems.add(new NavItem(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Buildingsscreen), com.hs_osnabrueck.swe_app.myapplication.R.drawable.ic_menu_home));
        mNavItems.add(new NavItem(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Beaconsuchescreen), com.hs_osnabrueck.swe_app.myapplication.R.drawable.ic_action_search));
        mNavItems.add(new NavItem(getString(R.string.WiePscreen), com.hs_osnabrueck.swe_app.myapplication.R.drawable.ic_palme));
        mNavItems.add(new NavItem(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.WieBscreen), com.hs_osnabrueck.swe_app.myapplication.R.drawable.ic_action_about));
        mNavItems.add(new NavItem(getString(com.hs_osnabrueck.swe_app.myapplication.R.string.Einstellungsscreen), com.hs_osnabrueck.swe_app.myapplication.R.drawable.ic_action_settings));

        MyDrawerAdapter myAdapter = new MyDrawerAdapter(getActionBar().getThemedContext(), mNavItems );
        mDrawerListView.setAdapter(myAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }
/*
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }
*/
    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(com.hs_osnabrueck.swe_app.myapplication.R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                com.hs_osnabrueck.swe_app.myapplication.R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                com.hs_osnabrueck.swe_app.myapplication.R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                /*if (!isAdded()) {
                    return;
                }
                */
                //getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            /**
             *
             * @param drawerView
             */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                //getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * mark the menu entry as selected
     * @param position of the desired fragment
     */
    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        main = (MainActivity)activity;
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    /**
     * Forward the new configuration the drawer toggle component.
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == com.hs_osnabrueck.swe_app.myapplication.R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @return
     */
    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    /**
     * returns the DrawerListView
     * @return mDrawerListView
     */
    public ListView getmDrawerListView() {
        return mDrawerListView;
    }
}
