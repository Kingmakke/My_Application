package com.hs_osnabrueck.swe_app.myapplication.server;

import org.json.JSONObject;

public interface AsyncResponse {
    void processFinish(JSONObject output);
}
