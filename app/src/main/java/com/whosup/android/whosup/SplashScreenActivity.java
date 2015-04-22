package com.whosup.android.whosup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.whosup.android.whosup.utils.SPreferences;

public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activityzz
               //Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                //startActivity(i);

                // close this activity
               String pass="";
                String email = SPreferences.getInstance().getLoginEmail(getApplicationContext());
                //finish();
                if(email!=null){
                    if(!email.equals("")) {
                        pass = SPreferences.getInstance().getLoginPassword(getApplicationContext());
                        autoLogin();
                    }else{
                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void autoLogin() {
    }

}
