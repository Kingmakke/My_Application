package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.hs_osnabrueck.swe_app.myapplication.services.BleSearchService;

import java.util.ArrayList;

public class EinstellungenFragment extends Fragment {

    private View rootView;
    private MainActivity main;
    private Switch toggleBleSearch;
    private Spinner institut, course;
    private ArrayList<String> courseList = new ArrayList<>();
    private Boolean scanning;

    public EinstellungenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_einstellungen, container, false);

        SharedPreferences prefs = main.getPreferences(main.MODE_PRIVATE);


        main.setPos(6);

        course = (Spinner) rootView.findViewById(R.id.firstscreen_course);

        institut = (Spinner) rootView.findViewById(R.id.firstscreen_institut);

        institut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array;
                switch (position) {
                    case 0:
                        courseList.clear();
                        array = getResources().getStringArray(R.array.courseliste_hochschule_array);
                        for (int i = 0; i < array.length; i++) {
                            courseList.add(array[i]);
                        }
                        course.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        courseList.clear();
                        array = getResources().getStringArray(R.array.courseliste_uni_array);
                        for (int i = 0; i < array.length; i++) {
                            courseList.add(array[i]);
                        }
                        course.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        courseList.clear();
                        array = getResources().getStringArray(R.array.courseliste_studieninteressierte_array);
                        for (int i = 0; i < array.length; i++) {
                            courseList.add(array[i]);
                        }
                        course.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        break;
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(main.getBaseContext(), android.R.layout.simple_spinner_item, courseList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                course.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int position = 0;
        for(int i = 0; i < getResources().getStringArray(R.array.intitut_array).length; i++){
            if( prefs.getString("institut", "Hochschule").equals(getResources().getStringArray(R.array.intitut_array)[i])){
                institut.setSelection(i);
                position = i;
            }
        }

        switch (position) {
            case 0:
                for(int i = 0; i < getResources().getStringArray(R.array.courseliste_hochschule_array).length; i++){
                    if( prefs.getString("course", "Medieninformatik").equals(getResources().getStringArray(R.array.courseliste_hochschule_array)[i])){
                        course.setSelection(i);
                    }
                }
                break;

            case 1:
                for(int i = 0; i < getResources().getStringArray(R.array.courseliste_uni_array).length; i++){
                    if( prefs.getString("course", "Jura").equals(getResources().getStringArray(R.array.courseliste_uni_array)[i])){
                        Log.e("debug", String.valueOf(i));
                        course.setSelection(i);
                    }
                }
                break;

            case 2:
                for(int i = 0; i < getResources().getStringArray(R.array.courseliste_studieninteressierte_array).length; i++){
                    if( prefs.getString("course", "--").equals(getResources().getStringArray(R.array.courseliste_studieninteressierte_array)[i])){
                        course.setSelection(i);
                        course.setVisibility(View.INVISIBLE);
                    }
                }
                break;
        }


        toggleBleSearch = (Switch)rootView.findViewById(R.id.einstellungsscreen_toggle);
        //set the switch to ON
        scanning = prefs.getBoolean("scanning", false);
        toggleBleSearch.setChecked(scanning);
        //attach a listener to check for changes in state
        final Intent intent = new Intent(main.getBaseContext(), BleSearchService.class);
        toggleBleSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    main.startService(intent);
                    scanning = true;
                } else {
                    main.stopService(intent);
                    scanning = false;
                }

            }
        });


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = main.getPreferences(main.MODE_PRIVATE).edit();
        editor.putString("institut", institut.getSelectedItem().toString());
        editor.putString("course", course.getSelectedItem().toString());
        editor.putBoolean("scanning", scanning);
        editor.apply();
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach(activity);
        main = (MainActivity)activity;
    }

}
