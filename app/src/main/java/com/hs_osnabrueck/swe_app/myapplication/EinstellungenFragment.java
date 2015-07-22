package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 *
 */
public class EinstellungenFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT_SCAN = 1;

    private View rootView;
    private MainActivity main;
    private Switch toggleBleSearch;
    private Spinner institut, course;
    private ArrayList<String> courseList = new ArrayList<>();
    private String version;
    TextView ver;

    public EinstellungenFragment() {}

    /**
     *
     */
    public void init() {

        try {
            version = main.getBaseContext().getPackageManager().getPackageInfo(main.getBaseContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            version = "";
        }

        course = (Spinner) rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.firstscreen_course);

        institut = (Spinner) rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.firstscreen_institut);

        institut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_hochschule_array));
                        course.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_uni_array));
                        course.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        courseList.clear();
                        Collections.addAll(courseList, getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_studieninteressierte_array));
                        course.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        break;
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(main.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
                course.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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


        toggleBleSearch = (Switch) rootView.findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.einstellungsscreen_toggle);
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
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(com.hs_osnabrueck.swe_app.myapplication.R.layout.fragment_einstellungen, container, false);

        //SharedPreferences prefs = main.getPreferences(main.MODE_PRIVATE);

        main.setPos(6);

        return rootView;
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    /**
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        main.setInstitut(institut.getSelectedItem().toString());
        main.setCourse(course.getSelectedItem().toString());
    }

    /**
     *
     * @param activity
     */
    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
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
        if(requestCode == REQUEST_ENABLE_BT_SCAN && resultCode == Activity.RESULT_OK) {
            main.setBackgroundScanning(true);
            toggleBleSearch.setChecked(main.isBackgroundScanning());
        }else{
            main.setBackgroundScanning(false);
            toggleBleSearch.setChecked(main.isBackgroundScanning());
        }
    }
}
