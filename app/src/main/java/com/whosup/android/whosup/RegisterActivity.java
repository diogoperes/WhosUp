package com.whosup.android.whosup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
    Boolean checkedGender=false, checkedTerms=false;
    Toast toast;
    ConnectionDetector cd;
    DateDisplayPicker birthdate;
    private final String MALE_STRING = "Male";
    private final String FEMALE_STRING = "Female";

    String usernameStr,emailStr,passwordStr,confirmPasswordStr,firstNameStr,lastNameStr,birthdateStr;


    private static final String REGISTER_URL = "http://whosup.host22.com/register.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private ProgressDialog pDialog=null;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();


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
                checkedTerms = ((CheckBox) v).isChecked();
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
                    new AttemptRegister().execute();


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

        usernameStr=username.getText().toString();
        emailStr=email.getText().toString();
        passwordStr=password.getText().toString();
        confirmPasswordStr=confirmPassword.getText().toString();
        firstNameStr=firstName.getText().toString();
        lastNameStr=lastName.getText().toString();

        //check if date was inserted
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        int yearInserted = Integer.parseInt(yearFormat.format(birthdate.getDate()));
        //Log.d("Birthdate: ", new String(yearInserted+""));
        if(yearInserted<1900){
            makeToast(R.string.insert_date);
            return false;
        }

        // check if any of the fields are vaccant
        if(usernameStr.equals("")||passwordStr.equals("")||confirmPasswordStr.equals("")||
                emailStr.equals("") || firstNameStr.equals("") || lastNameStr.equals("") || !checkedGender){
            makeToast(R.string.please_insert_all_fields);
            return false;
        }



        //See if username is valid
        String pattern= "^[a-zA-Z0-9]*$";
        if(!usernameStr.matches(pattern)){
            makeToast(R.string.invalid_username);
            return false;
        }
        //check if email is valid
        if(!Utility.isEmailValid(emailStr)){
            makeToast(R.string.invalid_email);
            return false;
        }
        // check if both password matches
        if(!passwordStr.equals(confirmPasswordStr)){
            makeToast(R.string.password_does_not_match);
            return false;
        }

        //Check birthdate to see if its 13 years old or older
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Date currentDate = null;
        birthdateStr = df.format(birthdate.getDate());
        Log.d("Birthdate: ", birthdateStr);

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

        if(!checkedTerms){
            makeToast(R.string.you_must_accept_the_terms_and_conditions);
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


    public class AttemptRegister extends AsyncTask<String, String, String> {



        /**
         * Before starting background thread Show Progress Dialog
         * */

        int success =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog=null;
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Attempting register...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag

            String passwordMD5 = Utility.MD5(passwordStr);
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", usernameStr));
                params.add(new BasicNameValuePair("email", emailStr));
                params.add(new BasicNameValuePair("password", passwordMD5));
                params.add(new BasicNameValuePair("confirm_password", passwordMD5));
                params.add(new BasicNameValuePair("firstName", firstNameStr));
                params.add(new BasicNameValuePair("lastName", lastNameStr));
                params.add(new BasicNameValuePair("gender", gender));
                params.add(new BasicNameValuePair("bday", birthdateStr));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // check your log for json response
                Log.d("Register Attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Register Successful!", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));

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
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), R.string.account_successfully_created, Toast.LENGTH_LONG).show();

                if(success==1){
                    finish();
                }
            }else{
                Toast.makeText(RegisterActivity.this, R.string.noConnection, Toast.LENGTH_LONG).show();

            }


        }

    }

}
