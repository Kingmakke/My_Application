package com.hs_osnabrueck.swe_app.myapplication.server;

import android.os.AsyncTask;

import com.hs_osnabrueck.swe_app.myapplication.interfaces.AsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * class to PUT data to the server
 */
public class HttpPut extends AsyncTask<String, Void, JSONObject> {

    static private final int CONNECTION_TIMEOUT = 10000;

    public AsyncResponse asyncResponse = null;
    //private ProgressDialog dialog;
    //private Context context;


    public HttpPut(){
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * is called when the AsyncTask starts
     * @param urls
     * @return jsonObject
     */
    @Override
    protected JSONObject doInBackground(String... urls) {
        return requestServer(urls[0], urls[1], urls[2]);
    }

    /**
     * onPostExecute displays the results of the AsyncTask.
     * @param result
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        //dialog.dismiss();
        asyncResponse.processFinish(result);
    }

    /**
     * get responsetext from server
     * @param inStream
     * @return scanner
     */
    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    /**
     * send the humidity value to the server
     * @param url the server
     * @param beaconID the beacon id
     * @param humidity humidity of the beacon
     * @return the JSONObject
     */
    public JSONObject requestServer(String url, String beaconID, String humidity){

        HttpURLConnection urlConnection = null;

        try {

            URL urlToRequest = new URL(url);

            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            //urlConnection.setReadTimeout(10000);

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

            JSONObject json = new JSONObject();
            json.put("beaconID",beaconID);
            json.put("humidity", humidity);

            out.write(json.toString());
            out.close();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}

