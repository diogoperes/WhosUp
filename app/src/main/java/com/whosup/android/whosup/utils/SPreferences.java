package com.whosup.android.whosup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;


public class SPreferences {

    private static SPreferences sPreferences=null;
    public static final String PREFS_LOGIN_EMAIL_KEY = "EMAIL" ;
    public static final String PREFS_LOGIN_PASSWORD_KEY = "PASSWORD" ;
    public static final String PREFS_LOGIN_USERNAME_KEY = "USERNAME" ;
    public static final String PREFS_LOGIN_FIRSTNAME_KEY = "FIRSTNAME" ;
    public static final String PREFS_LOGIN_LASTNAME_KEY = "LASTNAME" ;
    public static final String PREFS_LOGIN_GENDER_KEY = "GENDER" ;


    public static SPreferences getInstance(){
        if(sPreferences==null){
            sPreferences = new SPreferences();
        }
        return sPreferences;
    }

    public void saveLogin(Context c, String email, String pass, String user, String firstName, String lastName, String gender){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_EMAIL_KEY, email.toString());
        editor.putString(PREFS_LOGIN_PASSWORD_KEY, pass);
        editor.putString(PREFS_LOGIN_USERNAME_KEY, user);
        editor.putString(PREFS_LOGIN_FIRSTNAME_KEY, firstName);
        editor.putString(PREFS_LOGIN_LASTNAME_KEY, lastName);
        editor.putString(PREFS_LOGIN_GENDER_KEY, gender);
        editor.commit();

    }

    public String getLoginEmail(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_EMAIL_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLoginPassword(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_PASSWORD_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLoginUsername(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_USERNAME_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLoginFirstName(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_FIRSTNAME_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLoginLastName(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_LASTNAME_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLoginGender(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_GENDER_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public void discardLoginCredentials(Context c){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);;
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
