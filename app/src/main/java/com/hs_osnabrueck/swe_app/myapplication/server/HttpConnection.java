package com.hs_osnabrueck.swe_app.myapplication.server;

import android.os.AsyncTask;

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

    private String resultHttpConnection = "";
    private Boolean executed = false;

    @Override
    protected Void doInBackground(String... urls) {
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


    public void requestServer(String url, String put){

        HttpURLConnection urlConnection = null;

        try {

            URL urlToRequest = new URL(url);

            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            //urlConnection.setReadTimeout(10000);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            if(put.compareTo("put") != 0){
                urlConnection.setRequestMethod("PUT");
                OutputStreamWriter out = new OutputStreamWriter(
                        urlConnection.getOutputStream());
                out.write(put);
                out.close();
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                String tmp = getResponseText(in);
                resultHttpConnection = tmp;
                executed = true;
                //return new JSONObject(getResponseText(in)).toString();
            }else{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String tmp = getResponseText(in);
                resultHttpConnection = tmp;
                executed = true;
                //return tmp;
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
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            executed = true;
        }

        //return null;

    }

    public String getResultHttpConnection() {
        return resultHttpConnection;
    }

    public Boolean isExecuted() {
        return executed;
    }
}
