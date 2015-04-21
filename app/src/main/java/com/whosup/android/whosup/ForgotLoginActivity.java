package com.whosup.android.whosup;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgotLoginActivity extends Activity{

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String RECOVER_LOGIN_URL = "http://whosup.host22.com/recover_login.php";

    Button buttonLogin;
    TextView buttonRecover, loginscreen;
    EditText email;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    public static final String ok = "Password retrieved. Please check your email.";
    public static final String fail = "Password could not be retrieved. Please try again";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.activity_forgot_login);
        loginscreen = (TextView) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        buttonRecover = (Button) findViewById(R.id.retrieveLogin);


        buttonRecover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                if(e.equals("") || !e.contains("@") || !e.contains(".")){
                    Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                    return;
                }
                new AttemptRecoveryLogin().execute();

            }
        });

        // Listening to register new account link
        loginscreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                finish();
            }
        });
    }

    class AttemptRecoveryLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotLoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String eMail = email.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", eMail));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        RECOVER_LOGIN_URL, "POST", params);

                Log.d("Recovery login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Recovery login successful!", json.toString());
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
            if (s.equals(fail)) {
                Toast.makeText(ForgotLoginActivity.this, s, Toast.LENGTH_LONG).show();
            }
            if (s.equals(ok)) {
                Toast.makeText(ForgotLoginActivity.this, s, Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

}
