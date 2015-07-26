package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * StartActivity class, defines whether FirstActivity or MainActivity is called
 */
public class StartActivity extends Activity {

    private SharedPreferences prefs;

    /**
     * gets called when the App loads
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        prefs = getPreferences(MODE_PRIVATE);

        Intent intent;
        if (prefs.getBoolean("condition",false)) {
            intent = new Intent(this, MainActivity.class);
        } else {
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putBoolean("condition", true);
            editor.apply();
            intent = new Intent(this, FirstActivity.class);
        }
        startActivity(intent);
        finish();

    }
}
