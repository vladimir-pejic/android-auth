package de.kompitenz.uaa;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 01-Oct-17.
 */
public class AsyncContracts extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        // Do stuff before the operation
    }


    protected InputStream doInBackground(){
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:8000/api/contracts/index");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            assert url != null;
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.e("ERROR: ", "################# URL CONNECTION FAILED #################");
            e.printStackTrace();
        }

        try {
            InputStream in = null;
            try {
                assert urlConnection != null;
                in = new BufferedInputStream(urlConnection.getInputStream());
                return in;
            } catch (IOException e) {
                Log.e("ERROR: ", "################# GET INPUT STREAM FAILED #################");
                e.printStackTrace();
            }
            Log.d(String.valueOf(in), "################# END OF STREAM #################");
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return null;
    }



    protected String onPostExecute(String result) {
        return result;
    }

}
