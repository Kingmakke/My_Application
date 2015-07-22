package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 */
public class FirstActivity extends Activity {

    private Button start;
    private Spinner institut, course;
    private ArrayList<String> courseList = new ArrayList<>();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(com.hs_osnabrueck.swe_app.myapplication.R.layout.activity_first);

        course = (Spinner) findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.firstscreen_course);

        institut = (Spinner) findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.firstscreen_institut);

        institut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array;
                switch (position) {
                    case 0:
                        courseList.clear();
                        //array = getResources().getStringArray(com.hs_osnabrueck.swe_app.myapplication.R.array.courseliste_hochschule_array);
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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, courseList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                course.setAdapter(dataAdapter);
            }

            /**
             *
             * @param parent
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        start = (Button)findViewById(com.hs_osnabrueck.swe_app.myapplication.R.id.first_activity_button);
        start.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
                editor.putString("institut", institut.getSelectedItem().toString());
                editor.putString("course", course.getSelectedItem().toString());
                editor.commit();

                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                //intent.putExtra("institut", institut.getSelectedItem().toString());
                //intent.putExtra("course", course.getSelectedItem().toString());
                startActivity(intent);
                finish();
            }
        });
    }

}
