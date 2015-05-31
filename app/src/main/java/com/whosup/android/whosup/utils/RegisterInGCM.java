package com.whosup.android.whosup.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.whosup.android.whosup.RegisterActivity;
import com.whosup.android.whosup.utils.SPreferences;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RegisterInGCM {

    // Resgistration Id from GCM
    private static final String PREF_GCM_REG_ID = "PREF_GCM_REG_ID";
    private SharedPreferences prefs;
    // Your project number and web server url. Please change below.
    private static final String GCM_SENDER_ID = "549552969875";
    private static final String WEB_SERVER_URL = "http://whosup.host22.com/register_user.php";

    GoogleCloudMessaging gcm;



    private static final int ACTION_PLAY_SERVICES_DIALOG = 100;
    protected static final int MSG_REGISTER_WITH_GCM = 101;
    protected static final int MSG_REGISTER_WEB_SERVER = 102;
    protected static final int MSG_REGISTER_WEB_SERVER_SUCCESS = 103;
    protected static final int MSG_REGISTER_WEB_SERVER_FAILURE = 104;
    private String gcmRegId;
    Context context;
    RegisterActivity mRegisterActivity;

    public RegisterInGCM(Context context, RegisterActivity mRegisterActivity){
        this.context=context;
        this.mRegisterActivity= mRegisterActivity;

        if (isGoogelPlayInstalled()) {
            clearSharedPreferences();
            gcm = GoogleCloudMessaging.getInstance(context);

            // Read saved registration id from shared preferences.
            gcmRegId = getSharedPreferences().getString(PREF_GCM_REG_ID, "");

            if (TextUtils.isEmpty(gcmRegId)) {
                handler.sendEmptyMessage(MSG_REGISTER_WITH_GCM);
            } else {

                Toast.makeText(context, "Already registered with GCM", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private boolean isGoogelPlayInstalled() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mRegisterActivity,
                        ACTION_PLAY_SERVICES_DIALOG).show();
            } else {
                Toast.makeText(context,
                        "Google Play Service is not installed. Cannot receive notifications",
                        Toast.LENGTH_SHORT).show();

            }
            return false;
        }
        return true;

    }

    private void clearSharedPreferences() {
        SharedPreferences preferences = context.getSharedPreferences("AndroidSRCDemo", 0);
        preferences.edit().clear().apply();
    }

    private SharedPreferences getSharedPreferences() {
        if (prefs == null) {
            prefs = context.getSharedPreferences(
                    "AndroidSRCDemo", Context.MODE_PRIVATE);
        }
        return prefs;
    }

    public void saveInSharedPref(String result) {

        Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_GCM_REG_ID, result);
        editor.apply();
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_WITH_GCM:
                    new GCMRegistrationTask().execute();
                    break;
                case MSG_REGISTER_WEB_SERVER:
                    new WebServerRegistrationTask().execute();
                    break;
                case MSG_REGISTER_WEB_SERVER_SUCCESS:
                    Toast.makeText(context,
                            "registered with web server", Toast.LENGTH_LONG).show();
                    break;
                case MSG_REGISTER_WEB_SERVER_FAILURE:
                    Toast.makeText(context,
                            "registration with web server failed",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private class GCMRegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            if (gcm == null && isGoogelPlayInstalled()) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            try {
                if (gcm != null) {
                    gcmRegId = gcm.register(GCM_SENDER_ID);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

            return gcmRegId;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(context, "registered with notifications",
                        Toast.LENGTH_LONG).show();

                saveInSharedPref(result);
                handler.sendEmptyMessage(MSG_REGISTER_WEB_SERVER);
            }
        }

    }

    private class WebServerRegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL(WEB_SERVER_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(MSG_REGISTER_WEB_SERVER_FAILURE);
            }
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("regId", gcmRegId);
            dataMap.put("username", SPreferences.getInstance().getLoginUsername(context));
            System.out.println("REG ID: " + dataMap.get("regId"));
            System.out.println("USERNAME: " + dataMap.get("username"));

            StringBuilder postBody = new StringBuilder();
            Iterator iterator = dataMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry param = (Entry) iterator.next();
                postBody.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    postBody.append('&');
                }
            }
            String body = postBody.toString();
            byte[] bytes = body.getBytes();

            HttpURLConnection conn = null;
            try {
                assert url != null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setFixedLengthStreamingMode(bytes.length);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                OutputStream out = conn.getOutputStream();
                out.write(bytes);
                out.close();

                int status = conn.getResponseCode();
                if (status == 200) {
                    // Request success
                    handler.sendEmptyMessage(MSG_REGISTER_WEB_SERVER_SUCCESS);
                } else {
                    throw new IOException("Request failed with error code "
                            + status);
                }
            } catch (IOException io) {
                io.printStackTrace();
                handler.sendEmptyMessage(MSG_REGISTER_WEB_SERVER_FAILURE);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }
    }

}