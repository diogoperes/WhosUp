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

public class ForgotLoginActivity extends Activity{

    Button buttonLogin;
    TextView buttonRecover, loginscreen;
    EditText inputEmail;
    private ProgressDialog pDialog;
    public static final String ok = "Login retrieved. Please check your email.";
    public static final String fail = "Login could not be retrieved. Please try again";

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
                new ProgressDialogMessage(ForgotLoginActivity.this, "Retrieving your login. Please wait...", ok, fail).execute();

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

}
