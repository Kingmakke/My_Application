package com.hs_osnabrueck.swe_app.myapplication.server;

import android.os.AsyncTask;
import android.util.Log;

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

public class HttpConnection extends AsyncTask<String, Void, Void> {

    static private final int CONNECTION_TIMEOUT = 10000;

    private JSONObject resultHttpConnection;
    private Boolean executed = false;
    private String humidity;

    @Override
    protected Void doInBackground(String... urls) {

        if(!urls[1].equals("GET")){
            Log.e("debug", "put1");
            humidity = urls[2];
        }
        requestServer(urls[0], urls[1]);
        return null;
    }

    // onPostExecute displays the results of the AsyncTask.
    /*@Override
    protected void onPostExecute(String result) {
        Log.e("debug post", result);

        if(result != null){
            result.replace("\\\\","");
            resultHttpConnection = result;
        }else{
            resultHttpConnection = "";
        }
        executed = true;
    }*/

    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }


    public void requestServer(String url, String task){

        HttpURLConnection urlConnection = null;

        try {

            URL urlToRequest = new URL(url);

            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            //urlConnection.setReadTimeout(10000);

            // handle issues
            /*int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }*/

            if(!task.equals("GET")){
                Log.e("debug", "put2");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                Log.e("debug", urlConnection.getRequestMethod());

                //urlConnection.connect();
                OutputStreamWriter out = new OutputStreamWriter(
                        urlConnection.getOutputStream());

                JSONObject json = new JSONObject();
                json.put("beaconID",task);
                json.put("humidity", humidity);

                out.write(json.toString());
                out.close();
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                resultHttpConnection = new JSONObject(getResponseText(in));
                executed = true;

                //return new JSONObject(getResponseText(in));
            }else if(task.equals("GET")){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                resultHttpConnection = new JSONObject(getResponseText(in));
                executed = true;
                //return new JSONObject(getResponseText(in));
            }

        } catch (MalformedURLException e) {
            // URL is invalid
            executed = true;
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            executed = true;
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            executed = true;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            executed = true;
        }

        //return null;

    }

    public JSONObject getResultHttpConnection() {
        return resultHttpConnection;

    }

    public Boolean isExecuted() {
        return executed;
    }
}
