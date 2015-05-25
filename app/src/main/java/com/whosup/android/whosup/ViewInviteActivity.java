package com.whosup.android.whosup;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whosup.android.whosup.utils.Utility;
import com.whosup.listview.Invite;

public class ViewInviteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invite);
        Intent i = getIntent();

        TextView hostFirstName = (TextView) findViewById(R.id.hostFirstName);
        TextView hostLastName = (TextView) findViewById(R.id.hostLastName);
        TextView hostAge = (TextView) findViewById(R.id.hostAge);
        ImageView hostGender = (ImageView) findViewById(R.id.hostGender);
        TextView hostMeetCategory = (TextView) findViewById(R.id.hostMeetCategory);
        TextView hostMeetSubcategory = (TextView) findViewById(R.id.hostMeetSubcategory);
        TextView hostMeetDescription = (TextView) findViewById(R.id.hostMeetDescription);
        TextView hostMeetDay = (TextView) findViewById(R.id.hostMeetDay);
        TextView hostMeetTime = (TextView) findViewById(R.id.hostMeetTime);
        hostMeetDescription.setMovementMethod(new ScrollingMovementMethod());
        hostMeetDescription.setOnTouchListener(touchListener);

        hostFirstName.setText(i.getStringExtra("invite_firstName"));
        hostLastName.setText(i.getStringExtra("invite_lastName"));
        hostAge.setText(Utility.getDiffYears(i.getStringExtra("invite_birthday"))+ " years");
        hostMeetCategory.setText(i.getStringExtra("invite_category"));
        hostMeetSubcategory.setText(i.getStringExtra("invite_subcategory"));
        hostMeetDescription.setText(i.getStringExtra("invite_description"));

        Drawable genderSymbol = null;
        if(i.getStringExtra("invite_gender").equals("Female")){
            genderSymbol = getResources().getDrawable(R.mipmap.ic_female_symbol);
        }else{
            genderSymbol = getResources().getDrawable(R.mipmap.ic_male_symbol);
        }
        hostGender.setImageDrawable(genderSymbol);
        hostMeetDay.setText(arrangeDate(i.getStringExtra("invite_meetDay")));
        hostMeetTime.setText(arrangeHour(i.getStringExtra("invite_meetHour")));


    }

    View.OnTouchListener touchListener = new View.OnTouchListener(){
        public boolean onTouch(final View v, final MotionEvent motionEvent){
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        }
    };

    public String arrangeDate(String meetDay){
        String[] divider = meetDay.split("-");
        String year = divider[0];
        String month = divider[1];
        String day = divider[2];
        return day + "/" + month + "/" + year;
    }

    public String arrangeHour(String meetHour){
        String[] divider = meetHour.split(":");
        String hour = divider[0];
        String minute = divider[1];
        String second = divider[2];
        return hour + "h" + minute;
    }

}
