package com.whosup.android.whosup.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.whosup.android.whosup.LoginActivity;
import com.whosup.android.whosup.MainActivity;
import com.whosup.android.whosup.R;
import com.whosup.android.whosup.SplashScreenActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AttemptLogin extends AsyncTask<String, String, String> {
    private static final String LOGIN_URL = "http://whosup.host22.com/login.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private String eMail, password;
    private Activity a=null;
    private ProgressDialog pDialog=null;
    private boolean splash=false;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    public AttemptLogin(Activity a, String eMail, String password, boolean splash){
        this.splash=splash;
        this.eMail=eMail;
        this.password=password;
        this.a=a;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    boolean failure = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog=null;
        if(!splash){
            pDialog = new ProgressDialog(a);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... args) {
        // TODO Auto-generated method stub
        // Check for success tag
        int success;
        if(!splash) password = Utility.MD5(password);
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", eMail));
            params.add(new BasicNameValuePair("password", password));

            Log.d("request!", "starting");
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(
                    LOGIN_URL, "POST", params);

            // check your log for json response
            Log.d("Login attempt", json.toString());

            // json success tag
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                Log.d("Login Successful!", json.toString());
                if(!splash)SPreferences.getInstance().saveLogin(a.getApplicationContext(), eMail, password);
                Intent i = new Intent(a, MainActivity.class);
                a.finish();
                a.startActivity(i);
                return json.getString(TAG_MESSAGE);
            }else{
                Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                if(splash){
                    Intent i = new Intent(a.getApplicationContext(), LoginActivity.class);
                    a.finish();
                    a.startActivity(i);
                }
                return json.getString(TAG_MESSAGE);

            }
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once product deleted
        if(pDialog!=null) pDialog.dismiss();
        if (file_url != null){
            if(!splash)Toast.makeText(a, file_url, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(a, R.string.noConnection, Toast.LENGTH_LONG).show();
            if(splash){
                LoginActivity login = new LoginActivity();
                Intent i = new Intent(a.getApplicationContext(), login.getClass());
                a.finish();
                a.startActivity(i);
            }
        }


    }

}
