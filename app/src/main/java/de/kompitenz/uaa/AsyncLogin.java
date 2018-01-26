package de.kompitenz.uaa;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by User on 02-Oct-17.
 */
class AsyncLogin extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;

    protected String doInBackground(String... string) {

        Log.d("Async Login Email", string[0]);
        Log.d("Async Login Password", string[1]);

        return null;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}

