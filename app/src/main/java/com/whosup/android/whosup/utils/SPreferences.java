package com.whosup.android.whosup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPreferences {

    private static SPreferences sPreferences=null;
    public static final String PREFS_LOGIN_EMAIL_KEY = "EMAIL" ;
    public static final String PREFS_LOGIN_PASSWORD_KEY = "PASSWORD" ;
    public static final String PREFS_LOGIN_USERNAME_KEY = "USERNAME" ;
    public static final String PREFS_LOGIN_FIRSTNAME_KEY = "FIRSTNAME" ;
    public static final String PREFS_LOGIN_LASTNAME_KEY = "LASTNAME" ;
    public static final String PREFS_LOGIN_GENDER_KEY = "GENDER" ;
    public static final String PREFS_LOGIN_BIRTHDAY_KEY = "BIRTHDAY" ;
    public static final String PREFS_LOGIN_CITY_KEY = "CITY";
    public static final String PREFS_LOGIN_COUNTRY_KEY = "COUNTRY";
    public static final String PREFS_LOGIN_CUSTOMPHRASE_KEY = "CUSTOMPHRASE";
    public static final String PREFS_LOGIN_ABOUTME_KEY = "ABOUTME";
    public static final String PREFS_LOGIN_PHOTOLINK_KEY = "PHOTOLINK";


    public static SPreferences getInstance(){
        if(sPreferences==null){
            sPreferences = new SPreferences();
        }
        return sPreferences;
    }

    public void saveLogin(Context c, String email, String pass, String user, String firstName, String lastName, String gender, String birthday,
                            String city, String country, String customPhrase, String aboutMe, String photoLink){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_EMAIL_KEY, email);
        editor.putString(PREFS_LOGIN_PASSWORD_KEY, pass);
        editor.putString(PREFS_LOGIN_USERNAME_KEY, user);
        editor.putString(PREFS_LOGIN_FIRSTNAME_KEY, firstName);
        editor.putString(PREFS_LOGIN_LASTNAME_KEY, lastName);
        editor.putString(PREFS_LOGIN_GENDER_KEY, gender);
        editor.putString(PREFS_LOGIN_BIRTHDAY_KEY, birthday);
        editor.putString(PREFS_LOGIN_CITY_KEY, city);
        editor.putString(PREFS_LOGIN_COUNTRY_KEY, country);
        editor.putString(PREFS_LOGIN_CUSTOMPHRASE_KEY, customPhrase);
        editor.putString(PREFS_LOGIN_ABOUTME_KEY, aboutMe);
        editor.putString(PREFS_LOGIN_PHOTOLINK_KEY, photoLink);
        editor.apply();

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

    public void setLoginPassword(Context c, String password) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_PASSWORD_KEY, password);
        editor.apply();
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

    public String getLoginCity(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_CITY_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public void setLoginCity(Context c, String city) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_CITY_KEY, city);
        editor.apply();
    }

    public String getLoginCountry(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_COUNTRY_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public void setLoginCountry(Context c, String country) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_COUNTRY_KEY, country);
        editor.apply();
    }

    public String getLoginCustomPhrase(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_CUSTOMPHRASE_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public void setLoginCustomPhrase(Context c, String customPhrase) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_CUSTOMPHRASE_KEY, customPhrase);
        editor.apply();
    }

    public String getLoginAboutMe(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_ABOUTME_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public void setLoginAboutMe(Context c, String aboutMe) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_ABOUTME_KEY, aboutMe);
        editor.apply();
    }

    public String getLoginPhotoLink(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_PHOTOLINK_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public void setLoginPhotoLink(Context c, String photoLink) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_LOGIN_PHOTOLINK_KEY, photoLink);
        editor.apply();
    }

    public String getLoginBirthday(Context c){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sharedPrefs.getString(PREFS_LOGIN_BIRTHDAY_KEY, null);
        } catch (Exception e) {
            return "";
        }
    }

    public void discardLoginCredentials(Context c){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }




}
