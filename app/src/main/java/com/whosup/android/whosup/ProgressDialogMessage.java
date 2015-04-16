package com.whosup.android.whosup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Nuno on 16/04/2015.
 */
public class ProgressDialogMessage extends AsyncTask<String, String, String> {

    private final Activity activity;
    private final String message;
    private final String ok, fail;
    private ProgressDialog pDialog;

    public ProgressDialogMessage(Activity activity, String message, String ok, String fail){
        this.activity=activity;
        this.message=message;
        this.ok=ok;
        this.fail=fail;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        return ok;

    }

    protected void onPostExecute(String s) {
        // dismiss the dialog once product deleted
        pDialog.dismiss();
        if (s.equals(fail)){
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        }
        if (s.equals(ok)){
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            Intent i = new Intent(activity.getApplicationContext(), LoginActivity.class);
            activity.startActivity(i);
            activity.finish();
        }

    }
}
