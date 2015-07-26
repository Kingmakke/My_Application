package com.hs_osnabrueck.swe_app.myapplication.interfaces;

import org.json.JSONObject;

/**
 * Interface for the Server Response
 */
public interface AsyncResponse {
    /**
     *
     * @param output the Server Response
     */
    void processFinish(JSONObject output);
}
