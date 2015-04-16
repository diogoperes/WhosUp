package com.whosup.android.whosup;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class ForgotLoginActivity extends Activity {

    Button buttonLogin;
    TextView buttonRecover, loginscreen;
    EditText inputEmail;
    private ProgressDialog pDialog;
    private  final String ok = "Login retrieved. Please check your email.";
    private final String fail = "Login could not be retrieved. Please try again";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.activity_forgot_login);
        loginscreen = (TextView) findViewById(R.id.login);
        inputEmail = (EditText) findViewById(R.id.email);
        buttonRecover = (Button) findViewById(R.id.retrieveLogin);


        buttonRecover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                if(email.equals("") || !email.contains("@") || !email.contains(".")){
                    Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                    return;
                }
                new RecoveryLogin().execute();

            }
        });

        // Listening to register new account link
        loginscreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    class RecoveryLogin extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotLoginActivity.this);
            pDialog.setMessage("Retrieving your login. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //verificação à BD
                String r = fail;
            return r;

        }

        protected void onPostExecute(String s) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (s.equals(fail)){
                Toast.makeText(ForgotLoginActivity.this, s, Toast.LENGTH_LONG).show();
            }
            if (s.equals(ok)){
                Toast.makeText(ForgotLoginActivity.this, s, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }

        }
    }


}
