package com.whosup.android.whosup.utils;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeDisplayPicker extends TextView implements
        TimePickerDialog.OnTimeSetListener {
    private Context _context;

    public TimeDisplayPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _context = context;
    }

    public TimeDisplayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        setAttributes();
    }

    public TimeDisplayPicker(Context context) {
        super(context);
        _context = context;
        setAttributes();
    }

    private void setAttributes() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
    }

    private void showTimeDialog() {
        final Calendar c = Calendar.getInstance();
        // Use the current time as the default values for the picker
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog tp = new TimePickerDialog(_context, this, hour,
                minute, DateFormat.is24HourFormat(_context));
        tp.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    public String getTimeForPersistence(String str) {
        String[] time = str.split(":");
        String time2 = time[0] + ":" + time[1] + ":00";
        return time2;
    }
    public static int getHour(String str) {
        if (str != null) {
            String[] time = str.split(":");
            Integer hora = Integer.parseInt(time[0]);
            return hora;
        } else {
            return 0;
        }
    }

    public static int getMinute(String str) {
        if (str != null) {
            String[] time = str.split(":");
            Integer hora = Integer.parseInt(time[1]);
            return hora;
        } else {
            return 0;
        }
    }

}