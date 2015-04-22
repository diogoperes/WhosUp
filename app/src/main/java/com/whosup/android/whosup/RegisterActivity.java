package com.whosup.android.whosup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.DateDisplayPicker;
import com.whosup.android.whosup.utils.Utility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class RegisterActivity extends Activity {

    EditText username,email,password,confirmPassword, firstName, lastName;
    String gender;
    CheckBox terms;
    Boolean checkedGender, checkedTerms;
    Toast toast;
    ConnectionDetector cd;
    DateDisplayPicker birthdate;
    private final String MALE_STRING = "Male";
    private final String FEMALE_STRING = "Female";





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
        birthdate=(DateDisplayPicker)findViewById(R.id.clientEditCreate_BirthDateDayPicker);
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
                   makeToast(R.string.noInternetConnection);
                    return;
                }

                if(!allFieldsOk()) {
                    return;

                }else{
                    // Save the Data in Database
                    //loginDataBaseAdapter.insertEntry(userName, password);
                    makeToast(R.string.account_successfully_created);
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
                    gender = MALE_STRING;
                    break;
            case R.id.radio_female:
                if (checkedGender)
                    gender = FEMALE_STRING;
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


        //Check birthdate to see if its 13 years old or older
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Date currentDate = null;
        try {
            currentDate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d("Birthdate: ", birthdate.getDate().toString());
        //Log.d("Current Date: ", currentDate.toString());
        if(Utility.getDiffYears(birthdate.getDate(),currentDate)<13){
            makeToast(R.string.must_be_13_years_or_older);
            return false;
        }


        //check if date was inserted
        if(birthdate.getDateWasInserted()){
            makeToast(R.string.insert_date);
        }

        // check if any of the fields are vaccant
        if(usernameStr.equals("")||passwordStr.equals("")||confirmPasswordStr.equals("")||
                emailStr.equals("") || firstNameStr.equals("") || lastNameStr.equals("") || !checkedGender){
            makeToast(R.string.please_insert_all_fields);
            return false;
        }


        //See if username is valid
        String pattern= "^[a-zA-Z0-9]*$";
        if(usernameStr.matches(pattern)){
            makeToast(R.string.invalid_username);
            return false;
        }
        //check if email is valid
        if(!Utility.isEmailValid(emailStr)){
            makeToast(R.string.invalid_email);
            return false;
        }
        // check if both password matches
        if(!password.equals(confirmPassword)){
            makeToast(R.string.password_does_not_match);
            return false;
        }
        return true;
    }

    public void makeToast(int s){
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
            toast.show();
        }
    }


}
