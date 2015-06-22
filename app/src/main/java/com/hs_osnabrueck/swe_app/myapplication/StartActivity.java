package com.hs_osnabrueck.swe_app.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

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
