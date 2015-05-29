package com.whosup.android.whosup;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.User;
import com.whosup.android.whosup.utils.Utility;
import com.whosup.drawer.fragments.ViewProfileFragment;
import com.whosup.listview.Invite;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import com.whosup.android.whosup.utils.InviteAttend;
import com.whosup.listview.MyInviteAdapter;

public class MyInviteInformationActivity extends AppCompatActivity{

    private Button change_status_button;
    MyInviteInformationActivity thisActivity;
    private boolean changing_status = false;
    ConnectionDetector cd;
    Invite invite;
    ListView inviteAttendeesListView;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog=null;
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final String CHANGE_STATUS_URL = "http://whosup.host22.com/change_invite_status.php";
    private static final String CHANGE_ATTENDEE_STATUS_URL = "http://whosup.host22.com/attend_change_status.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = MyInviteInformationActivity.this;
        setContentView(R.layout.activity_my_invite_information);
        change_status_button = (Button) findViewById(R.id.change_status);
        Intent i = getIntent();
        invite = (Invite) i.getSerializableExtra("invite");
        inviteAttendeesListView = (ListView) findViewById(R.id.attends);
        inviteAttendeesListView.setClickable(true);
        inviteAttendeesListView.setOnItemClickListener(new inviteAttendeesListViewOnItemClickListener());
        if(invite.getInviteAttends()!=null){
            try{

                //System.out.println("INVITE LIST FIRST: " + inviteList.get(0).getDistanceFromMe());
                AttendeesAdapter adapter = new AttendeesAdapter(MyInviteInformationActivity.this, invite.getInviteAttends());
                inviteAttendeesListView.setAdapter(adapter);
            }catch (Exception ex){
                Toast.makeText(MyInviteInformationActivity.this, "Can't load invites, try refreshing", Toast.LENGTH_SHORT).show();
            }
        }



        if(invite.getIsOpen().equals("1")){
            change_status_button.setText(R.string.close_invite);
        }else{
            change_status_button.setText(R.string.open_invite);
        }

        change_status_button.setOnClickListener(new ChangeStatusOnClickListener());



    }




    private class ChangeStatusOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //checkConnection
            cd= new ConnectionDetector(getApplicationContext());
            if(!cd.isConnectingToInternet()) {
                Toast.makeText(MyInviteInformationActivity.this, R.string.noInternetConnection, Toast.LENGTH_LONG).show();
                return;
            }else{
                if(!changing_status){
                    new ChangeStatus().execute();
                }
            }

        }
    }


    public class ChangeAttendeeStatus extends AsyncTask<String, String, String> {
        private String statusTAG;
        private int position;
        private Button confirm, reject;

        public ChangeAttendeeStatus(int position, String statusTAG, Button confirm, Button reject){
            this.statusTAG = statusTAG;
            this.position = position;
            this.confirm = confirm;
            this.reject = reject;
        }


        /**
         * Before starting background thread Show Progress Dialog
         * */

        int success =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=null;
            pDialog = new ProgressDialog(MyInviteInformationActivity.this);
            if (statusTAG.equals("confirm")){
                System.out.println("CONFIRMING ATTENDEE");
                pDialog.setMessage("Confirming Attendee");
            }else if(statusTAG.equals("reject")){
                System.out.println("REJECTING ATTENDEE");
                pDialog.setMessage("Rejecting Attendee");
            }else{
                return;
            }

            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();




        }

        @Override
        protected String doInBackground(String... args) {




            //Log.v("Attend Invite", "Attend Invite");
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag_status",statusTAG));
                params.add(new BasicNameValuePair("id",invite.getInviteAttends().get(position).getId()));



                Log.d("sending", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        CHANGE_ATTENDEE_STATUS_URL, "POST", params);

                // check your log for json response
                Log.d("Sending request", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Status changed", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Status changed failed", json.getString(TAG_MESSAGE));
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
            changing_status = false;
            if (file_url != null){
                Toast.makeText(MyInviteInformationActivity.this, file_url, Toast.LENGTH_LONG).show();
                if (statusTAG.equals("confirm")){
                    invite.getInviteAttends().get(position).setState("confirmed");
                    reject.setText("REJECT");
                    reject.setBackgroundResource(R.drawable.border_layout);
                    GradientDrawable backgroundGradientReject = (GradientDrawable) reject.getBackground();
                    backgroundGradientReject.setStroke(3, Color.parseColor("#ffc51818"));
                    confirm.setBackgroundColor(Color.parseColor("#ff3ac726"));
                    confirm.setText("CONFIRMED");
                }else{
                    invite.getInviteAttends().get(position).setState("rejected");
                    confirm.setText("CONFIRM");
                    confirm.setBackgroundResource(R.drawable.border_layout);
                    GradientDrawable backgroundGradientConfirm = (GradientDrawable) confirm.getBackground();
                    backgroundGradientConfirm.setStroke(3, Color.parseColor("#ff3ac726"));
                    reject.setBackgroundColor(Color.parseColor("#ffc51818"));
                    reject.setText("REJECTED");
                }
            }else{
                Toast.makeText(MyInviteInformationActivity.this, R.string.noConnection, Toast.LENGTH_LONG).show();

            }


        }

    }


    public class ChangeStatus extends AsyncTask<String, String, String> {



        /**
         * Before starting background thread Show Progress Dialog
         * */

        int success =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* pDialog=null;
            pDialog = new ProgressDialog(MyInviteInformationActivity.this);
            pDialog.setMessage("Changing status...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
            changing_status=true;
            System.out.println("CHANGING STATUS");

        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            Intent i = getIntent();
            String idInviteStr = invite.getId();

            String isOpenStr = null;
            if(invite.getIsOpen().equals("1")){
                isOpenStr="0";

            }else{
                isOpenStr="1";
            }


            Log.v("Attend Invite", "Attend Invite");
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("inviteID",idInviteStr));
                params.add(new BasicNameValuePair("isOpen",isOpenStr));



                Log.d("sending", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        CHANGE_STATUS_URL, "POST", params);

                // check your log for json response
                Log.d("Sending request", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Status changed", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Status changed failed", json.getString(TAG_MESSAGE));

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
            //pDialog.dismiss();
            changing_status = false;
            if (file_url != null){
                Toast.makeText(MyInviteInformationActivity.this, file_url, Toast.LENGTH_LONG).show();
                if(invite.getIsOpen().equals("1")){
                    invite.setIsOpen("0");
                }else{
                    invite.setIsOpen("1");
                }
            }else{
                Toast.makeText(MyInviteInformationActivity.this, R.string.noConnection, Toast.LENGTH_LONG).show();

            }


        }

    }

    public class AttendeesAdapter extends BaseAdapter {
        protected List<InviteAttend> attendeesList;
        private LayoutInflater mInflater;
        private Context context;

        public AttendeesAdapter(Context context, List<InviteAttend> attendeesList) {
            mInflater = LayoutInflater.from(context);
            this.attendeesList = attendeesList;
            this.context=context;


        }


        @Override
        public int getCount() {
            return attendeesList.size();
        }

        @Override
        public Object getItem(int position) {
            return attendeesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            //return inviteList.get(position).getUsername();
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {

                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_row, parent, false);
                holder.firstName = (TextView) convertView.findViewById(R.id.attendeeFirstName);
                holder.lastName = (TextView) convertView.findViewById(R.id.attendeeLastName);
                holder.confirm = (Button) convertView.findViewById(R.id.confirmButton);
                holder.reject = (Button) convertView.findViewById(R.id.rejectButton);


                GradientDrawable backgroundGradientConfirm = (GradientDrawable) holder.confirm.getBackground();
                backgroundGradientConfirm.setStroke(3, Color.parseColor("#ff3ac726"));
                GradientDrawable backgroundGradientReject = (GradientDrawable) holder.reject.getBackground();
                backgroundGradientReject.setStroke(3, Color.parseColor("#ffc51818"));
                if(invite.getInviteAttends().get(position).getState().equals("confirmed")) {
                    holder.confirm.setBackgroundColor(Color.parseColor("#ff3ac726"));
                    holder.confirm.setText("CONFIRMED");


                }else if(invite.getInviteAttends().get(position).getState().equals("rejected")){
                    holder.reject.setBackgroundColor(Color.parseColor("#ffc51818"));
                    holder.reject.setText("REJECTED");
                }




                holder.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(!invite.getInviteAttends().get(position).getState().equals("confirmed")) {
                        new ChangeAttendeeStatus(position, "confirm", holder.confirm, holder.reject).execute();
                    }
                    }
                });
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(!invite.getInviteAttends().get(position).getState().equals("rejected")) {
                        new ChangeAttendeeStatus(position, "reject", holder.confirm, holder.reject).execute();
                    }
                    }
                });



                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            InviteAttend attendee = attendeesList.get(position);
            holder.firstName.setText(attendee.getUserProfile().getFirstName());
            holder.lastName.setText(attendee.getUserProfile().getLastName());



            return convertView;
        }

        private class ViewHolder {
            TextView firstName;
            TextView lastName;
            Button confirm;
            Button reject;

        }

    }

    private class inviteAttendeesListViewOnItemClickListener implements AdapterView.OnItemClickListener {
        Fragment fragment = null;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("Criar User Profile");
            User myUser = new User(false,
                    invite.getInviteAttends().get(position).getUserProfile().getUsername(),
                    invite.getInviteAttends().get(position).getUserProfile().getFirstName(),
                    invite.getInviteAttends().get(position).getUserProfile().getLastName(),
                    invite.getInviteAttends().get(position).getUserProfile().getGender(),
                    invite.getInviteAttends().get(position).getUserProfile().getBirthday(),
                    invite.getInviteAttends().get(position).getUserProfile().getCity(),
                    invite.getInviteAttends().get(position).getUserProfile().getCountry(),
                    invite.getInviteAttends().get(position).getUserProfile().getPhotoLink(),
                    invite.getInviteAttends().get(position).getUserProfile().getAboutMe(),
                    invite.getInviteAttends().get(position).getUserProfile().getCustomPhrase());
            //i.putExtra("user", myUser);

            Bundle args = new Bundle();
            args.putSerializable("user", myUser);

            fragment = new ViewProfileFragment();
            fragment.setArguments(args);
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.my_profile_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                System.out.println("ADD TO BACKSTACK");
                fragmentTransaction.commit();

            }
        }
    }
}
