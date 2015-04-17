package com.whosup.android.whosup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends Activity {

    EditText editTextUserName,editTextUserEmail,editTextPassword,editTextConfirmPassword;
    CheckBox terms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        Button button_register_new_account = (Button) findViewById(R.id.button_register_new_account);
        editTextUserName=(EditText)findViewById(R.id.editTextUserName);
        editTextUserEmail=(EditText)findViewById(R.id.editTextUserEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
        terms = (CheckBox) findViewById(R.id.agree_terms_and_conditions);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("lol");
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
                String userName=editTextUserName.getText().toString();
                String userEmail=editTextUserEmail.getText().toString();
                String password=editTextPassword.getText().toString();
                String confirmPassword=editTextConfirmPassword.getText().toString();
                // check if any of the fields are vaccant
                if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(), R.string.please_insert_all_fields, Toast.LENGTH_LONG).show();
                    return;
                }
                if(!userEmail.contains("@"))
                {
                    Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(getApplicationContext(), R.string.password_does_not_match, Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
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
}
