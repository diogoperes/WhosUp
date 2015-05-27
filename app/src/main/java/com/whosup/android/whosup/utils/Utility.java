package com.whosup.android.whosup.utils;

//string to md5


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.whosup.listview.Invite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {


    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String arrangeDate(String meetDay){
        String[] divider = meetDay.split("-");
        String year = divider[0];
        String month = divider[1];
        String day = divider[2];
        return day + "/" + month + "/" + year;
    }

    public static String arrangeHour(String meetHour){
        String[] divider = meetHour.split(":");
        String hour = divider[0];
        String minute = divider[1];
        String second = divider[2];
        return hour + "h" + minute;
    }

    //Compare dates
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static int getDiffYears(String date) {
        //today date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Date last = null;

        //date String formated
        Date first = null;

        try {
            last = df.parse(formattedDate);
            first = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return array where first position is days, second is hours, third is minutes and forth is seconds
     */
    public static ArrayList getDifferenceTime(Date startDate, Date endDate){
        ArrayList<Integer> differenceTime = new ArrayList<>();

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        int elapsedDaysInteger = (int)elapsedDays;
        differenceTime.add(elapsedDaysInteger);

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        int elapsedHoursInteger = (int)elapsedHours;
        differenceTime.add(elapsedHoursInteger);

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        int elapsedMinutesInteger = (int)elapsedMinutes;
        differenceTime.add(elapsedMinutesInteger);

        long elapsedSeconds = different / secondsInMilli;
        int elapsedSecondsInteger = (int)elapsedSeconds;
        differenceTime.add(elapsedSecondsInteger);

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);


        return differenceTime;
    }

    public static void displayPromptForEnablingGPS(
            final Activity activity)
    {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Enable either GPS or any other location"
                + " service to find current location.  Then refresh"
                + " the invite list.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }


    public static int getNumberCountStatus(Invite invite, String key){
        int number = 0;
        for (InviteAttend ia : invite.getInviteAttends()){
            if(ia.getState().equals(key)){
                number++;
            }
        }
        return number;
    }

}
