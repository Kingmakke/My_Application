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


public class FirstActivity extends Activity {

    private Button start;
    private Spinner institut, course;
    ArrayList<String> courseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_first);

        course = (Spinner) findViewById(R.id.firstscreen_course);

        institut = (Spinner) findViewById(R.id.firstscreen_institut);

        institut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        courseList.clear();
                        courseList.add("Medieninformatik");
                        courseList.add("Technische - Informatik");
                        course.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        courseList.clear();
                        courseList.add("??");
                        course.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        courseList.clear();
                        courseList.add("--");
                        course.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        break;
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, courseList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                course.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        start = (Button)findViewById(R.id.first_activity_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString("insitut", institut.getSelectedItem().toString());
                editor.putString("course", course.getSelectedItem().toString());
                editor.apply();

                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
