package com.whosup.android.whosup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RegisterActivity extends Activity {

    EditText username,email,password,confirmPassword, firstName, lastName;
    CheckBox terms;
    Boolean checkedGender, checkedTerms;
    Toast toast;
    ConnectionDetector cd;







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        Button button_register_new_account = (Button) findViewById(R.id.button_register_new_account);
        username=(EditText)findViewById(R.id.editTextUserName);
        email=(EditText)findViewById(R.id.editTextUserEmail);
        password=(EditText)findViewById(R.id.editTextPassword);
        confirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
        firstName=(EditText)findViewById(R.id.editTextFirstName);
        lastName=(EditText)findViewById(R.id.editTextLastName);
        terms = (CheckBox) findViewById(R.id.agree_terms_and_conditions);


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedTerms = ((RadioButton) v).isChecked();
            }
        });
        terms.setLongClickable(true);
        terms.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(i);
                return false;
            }
        });



        button_register_new_account.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //checkConnection
                cd= new ConnectionDetector(getApplicationContext());
                if(!cd.isConnectingToInternet()) {
                    if (toast == null ||toast.getView().getWindowVisibility() != View.VISIBLE) {
                        toast = Toast.makeText(getApplicationContext(), R.string.noInternetConnection, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    return;
                }

                if(!allFieldsOk()) {
                    return;

                }else{
                    // Save the Data in Database
                    //loginDataBaseAdapter.insertEntry(userName, password);
                    Toast.makeText(getApplicationContext(), R.string.account_successfully_created, Toast.LENGTH_LONG).show();
                    finish();
                }


            }
        });



        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        checkedGender = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checkedGender)
                    // Pirates are the best
                    break;
            case R.id.radio_female:
                if (checkedGender)
                    // Ninjas rule
                    break;
        }
    }

    public Boolean allFieldsOk(){
        String usernameStr=username.getText().toString();
        String emailStr=email.getText().toString();
        String passwordStr=password.getText().toString();
        String confirmPasswordStr=confirmPassword.getText().toString();
        String firstNameStr=firstName.getText().toString();
        String lastNameStr=lastName.getText().toString();




        // check if any of the fields are vaccant
        if(usernameStr.equals("")||passwordStr.equals("")||confirmPasswordStr.equals("")||
                emailStr.equals("") || firstNameStr.equals("") || lastNameStr.equals("") || !checkedGender)
        {
            toast.makeText(getApplicationContext(), R.string.please_insert_all_fields, Toast.LENGTH_LONG).show();
            return false;
        }
        //See if username is valid
        String pattern= "^[a-zA-Z0-9]*$";
        if(usernameStr.matches(pattern)){
            toast.makeText(getApplicationContext(), R.string.invalid_username, Toast.LENGTH_LONG).show();
            return false;
        }

        //check if email is valid
        if(!usernameStr.contains("@"))
        {
            toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
            return false;
        }
        // check if both password matches
        if(!password.equals(confirmPassword))
        {
            toast.makeText(getApplicationContext(), R.string.password_does_not_match, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
