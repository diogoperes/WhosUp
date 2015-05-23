package com.whosup.android.whosup;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.AttemptLogin;
import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.Utility;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    Button buttonLogin;
    TextView registerScreen, recoveryScreen;
    EditText  pass;
    AutoCompleteTextView email=null;
    private Toast toast;
    ConnectionDetector cd;





    //php login script location:

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/login.php";

    //testing on Emulator:



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.activity_login);
        registerScreen = (TextView) findViewById(R.id.signUp);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        recoveryScreen = (TextView) findViewById(R.id.passres);
        buttonLogin = (Button) findViewById(R.id.login);

        Account[] accounts = AccountManager.get(this).getAccounts();
        Set<String> emailSet = new HashSet<String>();
        for (Account account : accounts) {
            if (EMAIL_PATTERN.matcher(account.name).matches()) {
                emailSet.add(account.name);
            }
        }
        email.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check Connection
                cd= new ConnectionDetector(getApplicationContext());
                if(cd.isConnectingToInternet()) {
                    String e = email.getText().toString();
                    if(e.equals("") || !e.contains("@") || !e.contains(".")){
                        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                            toast = Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }else{
                        new AttemptLogin(LoginActivity.this, e, pass.getText().toString(), false).execute();
                    }
                }else{

                    if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                        toast = Toast.makeText(getApplicationContext(), R.string.noInternetConnection, Toast.LENGTH_LONG);
                        toast.show();
                    }

                }
            }
        });

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }
        });

        recoveryScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), ForgotLoginActivity.class);
                startActivity(i);
            }
        });
    }
}
