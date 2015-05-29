package com.whosup.android.whosup;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.Utility;
import com.whosup.listview.Invite;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewInviteActivity extends Activity {
    private Button attend_button;
    ConnectionDetector cd;
    JSONParser jsonParser = new JSONParser();


    private ProgressDialog pDialog=null;
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final String CREATE_INVITE_URL = "http://whosup.host22.com/view_invite.php";


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
        TextView hostPlace = (TextView) findViewById(R.id.place);
        attend_button = (Button) findViewById(R.id.attend_button);
        if(i.getStringExtra("can_attend").equals("1")){
            attend_button.setOnClickListener(new AttendInviteOnClickListener());
        }else{
            attend_button.setVisibility(View.GONE);
        }


        hostMeetDescription.setMovementMethod(new ScrollingMovementMethod());
        //hostMeetDescription.setOnTouchListener(touchListener);

        hostFirstName.setText(i.getStringExtra("invite_firstName"));
        hostLastName.setText(i.getStringExtra("invite_lastName"));
        hostAge.setText(Utility.getDiffYears(i.getStringExtra("invite_birthday"))+ " years");
        hostMeetCategory.setText(i.getStringExtra("invite_category"));
        hostMeetSubcategory.setText(i.getStringExtra("invite_subcategory"));
        hostMeetDescription.setText(i.getStringExtra("invite_description"));
        hostPlace.setText(i.getStringExtra("invite_address"));

        Drawable genderSymbol = null;
        if(i.getStringExtra("invite_gender").equals("Female")){
            genderSymbol = getResources().getDrawable(R.mipmap.ic_female_symbol);
        }else{
            genderSymbol = getResources().getDrawable(R.mipmap.ic_male_symbol);
        }
        hostGender.setImageDrawable(genderSymbol);
        hostMeetDay.setText(Utility.arrangeDate(i.getStringExtra("invite_meetDay")));
        hostMeetTime.setText(Utility.arrangeHour(i.getStringExtra("invite_meetHour")));


    }

    private class AttendInviteOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //checkConnection
            cd= new ConnectionDetector(getApplicationContext());
            if(!cd.isConnectingToInternet()) {
                Toast.makeText(ViewInviteActivity.this, R.string.noInternetConnection, Toast.LENGTH_LONG).show();
                return;
            }else{
                new AttendInvite().execute();
            }



        }
    }


    public class AttendInvite extends AsyncTask<String, String, String> {



        /**
         * Before starting background thread Show Progress Dialog
         * */

        int success =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog=null;
            pDialog = new ProgressDialog(ViewInviteActivity.this);
            pDialog.setMessage("Creating Invite...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            Intent i = getIntent();
            String idInviteStr = i.getStringExtra("invite_id");
            String hostUsernameStr = i.getStringExtra("invite_username");
            String invitedUsernameStr = SPreferences.getInstance().getLoginUsername(ViewInviteActivity.this);


            Log.v("Attend Invite", "Attend Invite");
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("idInvite",idInviteStr));
                params.add(new BasicNameValuePair("hostUsername",hostUsernameStr));
                params.add(new BasicNameValuePair("invitedUsername",invitedUsernameStr));



                Log.d("sending", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        CREATE_INVITE_URL, "POST", params);

                // check your log for json response
                Log.d("Sending request", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Request Sent", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Request Failed", json.getString(TAG_MESSAGE));

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
                Toast.makeText(ViewInviteActivity.this, file_url, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), R.string.account_successfully_created, Toast.LENGTH_LONG).show();
                //getSupportFragmentManager().popBackStack();

            }else{
                Toast.makeText(ViewInviteActivity.this, R.string.noConnection, Toast.LENGTH_LONG).show();

            }


        }

    }



}
