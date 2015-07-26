package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Settings Fragment
 */
public class EinstellungenFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT_SCAN = 1;

    private View rootView;
    private MainActivity main;
    private Switch toggleBleSearch;
    private Spinner institut, course;
    private ArrayList<String> courseList = new ArrayList<>();
    private String version;
    private TextView ver;
    private View divider2;

    public EinstellungenFragment() {}

    /**
     * defines how the settings fragment looks like, including toggle for background search
     */
    public void init() {

        try {
            version = main.getBaseContext().getPackageManager().getPackageInfo(main.getBaseContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            version = "";
        }

        course = (Spinner) rootView.findViewById(R.id.einstellungsscreen_course);

        institut = (Spinner) rootView.findViewById(R.id.einstellungsscreen_institut);

        //divider1 = (View)rootView.findViewById(R.id.divider1);
        divider2 = (View)rootView.findViewById(R.id.divider2);
        //divider3 = (View)rootView.findViewById(R.id.divider3);

        institut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * defines the spinners and saves the institute and course when the site is left
             * @param parent
             * @param view
             * @param position 0 for Hochschule, 1 for Uni, 2 for Studieninteressierte
             * @param id
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 60, getResources().getDisplayMetrics());
                switch (position) {
                    case 0:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_hochschule_array));
                        course.setVisibility(View.VISIBLE);
                        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 60, getResources().getDisplayMetrics());
                        course.getLayoutParams().height = px;
                        divider2.getLayoutParams().height = 1;
                        main.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal)));
                        main.getmNavigationDrawerFragment().getmDrawerListView().setSelector(R.drawable.drawer_list_selector_hs);
                        break;

                    case 1:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_uni_array));
                        course.setVisibility(View.VISIBLE);
                        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 60, getResources().getDisplayMetrics());
                        course.getLayoutParams().height = px;
                        divider2.getLayoutParams().height = 1;
                        main.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal_uni)));
                        main.getmNavigationDrawerFragment().getmDrawerListView().setSelector(R.drawable.drawer_list_selector_uni);
                        break;

                    case 2:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_studieninteressierte_array));
                        course.setVisibility(View.INVISIBLE);
                        course.getLayoutParams().height = 0;
                        divider2.getLayoutParams().height = 0;
                        main.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.hs_osnabrueck.swe_app.myapplication.R.color.normal_int)));
                        main.getmNavigationDrawerFragment().getmDrawerListView().setSelector(R.drawable.drawer_list_selector_int);
                        break;

                    default:
                        break;
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(main.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
                course.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        for (int i = 0; i < getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.intitut_array).length; i++) {
            if (main.getInstitut().equals(getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.intitut_array)[i])) {
                institut.setSelection(i, true);
                ArrayAdapter<String> dataAdapter;
                switch (i) {
                    case 0:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_hochschule_array));
                        course.setVisibility(View.VISIBLE);
                        dataAdapter = new ArrayAdapter<String>(main.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
                        course.setAdapter(dataAdapter);
                        for (int j = 0; j < courseList.size(); j++) {
                            if (main.getCourse().equals(courseList.get(j))) {
                                final int pos = j;
                                course.post(new Runnable() {
                                    public void run() {
                                        course.setSelection(pos);
                                    }
                                });
                            }
                        }
                        break;

                    case 1:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_uni_array));
                        course.setVisibility(View.VISIBLE);
                        dataAdapter = new ArrayAdapter<String>(main.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
                        course.setAdapter(dataAdapter);
                        for (int j = 0; j < courseList.size(); j++) {
                            if (main.getCourse().equals(courseList.get(j))) {
                                final int pos = j;
                                course.post(new Runnable() {
                                    public void run() {
                                        course.setSelection(pos);
                                    }
                                });
                            }
                        }
                        break;

                    case 2:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_studieninteressierte_array));
                        course.setVisibility(View.VISIBLE);
                        dataAdapter = new ArrayAdapter<String>(main.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
                        course.setAdapter(dataAdapter);
                        for (int j = 0; j < courseList.size(); j++) {
                            if (main.getCourse().equals(courseList.get(j))) {
                                final int pos = j;
                                course.post(new Runnable() {
                                    public void run() {
                                        course.setSelection(pos);
                                        course.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }
                        break;
                }
            }
        }

        toggleBleSearch = (Switch) rootView.findViewById(R.id.einstellungsscreen_toggle);
        //set the switch to ON

        if (!main.getBtAdapter().isEnabled()) {
            main.setBackgroundScanning(false);
        }/*else{
            main.setBackgroundScanning(prefs.getBoolean("scanning", false));
        }*/

        toggleBleSearch.setChecked(main.isBackgroundScanning());
        //attach a listener to check for changes in state

        toggleBleSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (main.getBtAdapter() != null && !main.getBtAdapter().isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT_SCAN);
                    } else {
                        main.setBackgroundScanning(true);
                    }
                } else {
                    main.setBackgroundScanning(false);
                }
            }
        });

        ver = (TextView)rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.einstellungsscreen_version);
        ver.setText("v" + version);
        ver.setVisibility(View.INVISIBLE);

    }

    /**
     * gets called when the the fragment loads
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_einstellungen, container, false);

        //SharedPreferences prefs = main.getPreferences(main.MODE_PRIVATE);

        main.setPos(6);

        return rootView;
    }

    /**
     * gets called when the the fragment loads
     */
    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    /**
     * saves the institute and course when the app is minimized
     */
    @Override
    public void onPause() {
        super.onPause();
        main.setInstitut(institut.getSelectedItem().toString());
        main.setCourse(course.getSelectedItem().toString());
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

    /**
     * reads the state of the toggleButton when fragment is loaded and shows the toggleButton in its actual state
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT_SCAN && resultCode == Activity.RESULT_OK) {
            main.setBackgroundScanning(true);
            toggleBleSearch.setChecked(main.isBackgroundScanning());
        }else{
            main.setBackgroundScanning(false);
            toggleBleSearch.setChecked(main.isBackgroundScanning());
        }
    }
}
