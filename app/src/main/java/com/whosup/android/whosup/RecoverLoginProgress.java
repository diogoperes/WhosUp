package com.whosup.android.whosup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nuno on 16/04/2015.
 */
public class RecoverLoginProgress extends AsyncTask<String, String, String> {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String RECOVER_LOGIN_URL = "http://whosup.host22.com/recover_login.php";
    private final Activity activity;
    private final String message;
    private final String ok, fail;
    private final String eMail;
    private ProgressDialog pDialog;

    public RecoverLoginProgress(Activity activity, String message, String ok, String fail, String email){
        this.activity=activity;
        this.message=message;
        this.eMail=email;
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
        int success;
        JSONParser jsonParser = new JSONParser();
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", eMail));

            Log.d("request!", "starting");
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(
                    RECOVER_LOGIN_URL, "POST", params);

            // check your log for json response
            Log.d("Recovery attempt", json.toString());

            // json success tag
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                Log.d("Recovery login Successful!", json.toString());
                return ok;
            }else{
                Log.d("Recovery login Failure!", json.getString(TAG_MESSAGE));
                return fail;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    protected void onPostExecute(String s) {
        // dismiss the dialog once product deleted
        pDialog.dismiss();
        if (s.equals(fail)){
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        }
        if (s.equals(ok)){
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            activity.finish();
        }

    }
}
