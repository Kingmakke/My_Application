package com.hs_osnabrueck.swe_app.myapplication.server;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.hs_osnabrueck.swe_app.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * class to get data from the server
 */
public class HttpGet extends AsyncTask<String, Void, JSONObject> {

    static private final int CONNECTION_TIMEOUT = 10000;

    public AsyncResponse asyncResponse = null;
    private ProgressDialog dialog;
    private Context context;


    public HttpGet(Context context){
        this.context = context;
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context, R.style.MyTheme);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * is called when the AsyncTask starts
     * @param urls
     * @return the requested server
     */
    @Override
    protected JSONObject doInBackground(String... urls) {

        return requestServer(urls[0]);
    }

    /**
     * onPostExecute displays the results of the AsyncTask.
     * @param result
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        dialog.dismiss();
        if(result == null){
            dialog();
        }else{
            asyncResponse.processFinish(result);
        }

    }

    /**
     * get resonsetext from server
     * @param inStream
     * @return scanner
     */
    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    /**
     * if everything is okay, data is called from the server
     * @param url
     * @return
     */
    public JSONObject requestServer(String url){

        HttpURLConnection urlConnection = null;

        try {

            URL urlToRequest = new URL(url);

            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(10000);

            // handle issues
            /*int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }*/


            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new JSONObject(getResponseText(in));


        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {

            dialog();


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

    /**
     * dialog is shown when requestServer() throws an exception and gives the user feedback
     */
    public void dialog(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set title

            // set dialog message
            alertDialogBuilder
                    .setTitle("Verbindungstimeout")
                    .setMessage("Diese App benötigt eine aktive Internetverbindung!")
                    .setCancelable(false)
                    .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
}
