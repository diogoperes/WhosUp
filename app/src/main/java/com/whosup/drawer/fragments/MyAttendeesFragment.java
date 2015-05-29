package com.whosup.drawer.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.whosup.android.whosup.MyInviteInformationActivity;
import com.whosup.android.whosup.R;
import com.whosup.android.whosup.ViewInviteActivity;
import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.InviteAttend;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.User;
import com.whosup.android.whosup.utils.Utility;
import com.whosup.listview.Invite;
import com.whosup.listview.MyInviteAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DiogoPeres on 29-05-2015.
 */
public class MyAttendeesFragment extends Fragment {
    private ListView inviteListView;
    private ProgressDialog pDialog;
    JSONArray invitesList = null;
    private boolean refreshing = false;
    //Invite and state of the attend
    private static ArrayList<Invite> inviteList;
    private static ArrayList<String> inviteListStatus;
    ConnectionDetector cd;
    private final String TAG_USERNAME = "username";
    private final String GET_MY_ATTENDEES_URL = "http://whosup.host22.com/get_my_attendees.php";
    JSONParser jsonParser = new JSONParser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_my_invites, null);
        /*FrameLayout fl = (FrameLayout) rootview.findViewById(R.id.my_invites_fragment);
        fl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        */
        inviteList = new ArrayList<>();
        inviteListStatus = new ArrayList<>();
        inviteListView = (ListView) rootview.findViewById(R.id.list_invites);
        inviteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(rootview.getContext(), ViewInviteActivity.class);
                i.putExtra("can_attend", "0");
                i.putExtra("invite_id", inviteList.get(position).getId());
                i.putExtra("invite_username", inviteList.get(position).getUsername());
                i.putExtra("invite_firstName", inviteList.get(position).getFirstName());
                i.putExtra("invite_lastName", inviteList.get(position).getLastName());
                i.putExtra("invite_gender", inviteList.get(position).getGender());
                i.putExtra("invite_birthday", inviteList.get(position).getBirthday());
                //System.out.println( inviteList.get(position).getBirthday() + "");
                i.putExtra("invite_postDay", inviteList.get(position).getPostDay());
                i.putExtra("invite_postHour", inviteList.get(position).getPostHour());
                i.putExtra("invite_meetDay", inviteList.get(position).getMeetDay());
                i.putExtra("invite_meetHour", inviteList.get(position).getMeetHour());
                i.putExtra("invite_category", inviteList.get(position).getCategory());
                i.putExtra("invite_subcategory", inviteList.get(position).getSubcategory());
                i.putExtra("invite_description", inviteList.get(position).getDescription());
                i.putExtra("invite_latitude", inviteList.get(position).getLatitude());
                i.putExtra("invite_longitude", inviteList.get(position).getLongitude());
                i.putExtra("invite_placeID", inviteList.get(position).getPlaceID());
                i.putExtra("invite_address", inviteList.get(position).getAddress());
                startActivity(i);
            }
        });

        if(refreshing==false) {
            cd = new ConnectionDetector(getActivity());
            if (!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), "Can't connect to the network.", Toast.LENGTH_SHORT).show();
            } else {
                new LoadMyAttendees().execute();
            }
        }

        return rootview;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(R.string.my_attendees);
    }

    @Override
    public void onPause() {
        getActivity().setTitle(R.string.app_name);
        super.onPause();
    }

    /**
     * Background Async Task to Load all Albums by making http request
     * */

    class LoadMyAttendees extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshing=true;
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Listing My Attendees ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(TAG_USERNAME, SPreferences.getInstance().getLoginUsername(getActivity())));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(GET_MY_ATTENDEES_URL, "POST",
                    params);


            // Check your log cat for JSON reponse
            //Log.d("Invites JSON: ", "> " + json.toString());
            inviteList.clear();
            try {
                invitesList = json.getJSONArray("InvitesAttendees");
                //Log.d("Invites JSONArray: ", "> " + invitesList);
                if (invitesList != null) {
                    // looping through All Invites
                    for (int i = 0; i < invitesList.length(); i++) {
                        JSONObject c = invitesList.getJSONObject(i);
                        // Storing each json item values in variable
                        String idInviteAttendee = c.getString("idInviteAttendee");
                        String idInvite = c.getString("idInvite");
                        String hostUsername = c.getString("hostUsername");
                        String invitedUsername = c.getString("invitedUsername");
                        String state = c.getString("state");

                        JSONArray inviteJSONArray = c.getJSONArray("Invite");
                        JSONObject inviteJSONObject = inviteJSONArray.getJSONObject(0);

                        String id = inviteJSONObject.getString("id");
                        String username = inviteJSONObject.getString("username");
                        String firstName = inviteJSONObject.getString("firstName");
                        String lastName = inviteJSONObject.getString("lastName");
                        String gender = inviteJSONObject.getString("gender");
                        String birthday = inviteJSONObject.getString("birthday");
                        String postDay = inviteJSONObject.getString("postDay");
                        String postHour = inviteJSONObject.getString("postHour");
                        String meetDay = inviteJSONObject.getString("meetDay");
                        String meetHour = inviteJSONObject.getString("meetHour");
                        String category = inviteJSONObject.getString("category");
                        String subcategory = inviteJSONObject.getString("subcategory");
                        String description = inviteJSONObject.getString("description");
                        String latitude = inviteJSONObject.getString("latitude");
                        String longitude = inviteJSONObject.getString("longitude");
                        String placeID = inviteJSONObject.getString("placeID");
                        String address = inviteJSONObject.getString("address");
                        String isOpen = inviteJSONObject.getString("isOpen");
                        Invite newInvite = new Invite(id, username, firstName, lastName, gender, birthday, postDay, postHour, meetDay, meetHour, category, subcategory, description, latitude,
                                longitude, placeID, address, isOpen);

                        inviteList.add(newInvite);
                        inviteListStatus.add(state);
                        System.out.println("INVITE STATUS: " + inviteListStatus.get(i));
                        System.out.println("ADDED ATTEND");

                    }
                    System.out.println("INVITE LIST: " + inviteList);
                } else {
                    Log.d("Invites: ", "null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all albums
            refreshing=false;
            pDialog.dismiss();
            try{
                //System.out.println("INVITE LIST FIRST: " + inviteList.get(0).getDistanceFromMe());
                MyAttendeesAdapter adapter = new MyAttendeesAdapter(getActivity(), inviteList, inviteListStatus);
                inviteListView.setAdapter(adapter);
            }catch (Exception ex){
                Toast.makeText(getActivity(), "Can't load invites, try refreshing", Toast.LENGTH_SHORT).show();
            }


        }

        protected void onCancelled() {
            Toast.makeText(getActivity(), "Can't retrieve invites, try refreshing", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyAttendeesAdapter extends BaseAdapter {
        protected ArrayList<Invite> attendeesList;
        protected ArrayList<String> attendeesListStatus;
        private LayoutInflater mInflater;
        private Context context;

        public MyAttendeesAdapter(Context context, ArrayList<Invite> attendeesList, ArrayList<String> attendeesListStatus) {
            mInflater = LayoutInflater.from(context);
            this.attendeesList = attendeesList;
            this.attendeesListStatus = attendeesListStatus;
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
            ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {

                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_row_my_attendees, parent, false);

                holder.name = (TextView) convertView.findViewById(R.id.inviteFrom);
                holder.subcategory = (TextView) convertView.findViewById(R.id.inviteSubcategory);
                holder.status = (TextView) convertView.findViewById(R.id.inviteStatus);
                holder.address = (TextView) convertView.findViewById(R.id.inviteLocation);
                holder.hostAge = (TextView) convertView.findViewById(R.id.hostAge);
                holder.meetDay = (TextView) convertView.findViewById(R.id.meetDay);
                holder.meetHour = (TextView) convertView.findViewById(R.id.meetHour);
                holder.imgCategory = (ImageView) convertView.findViewById(R.id.imgCategory);
                holder.imgHostGender = (ImageView) convertView.findViewById(R.id.hostGender);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Invite invite = inviteList.get(position);
            holder.name.setText(invite.getFirstName()+ " "+ invite.getLastName());
            holder.subcategory.setText(invite.getSubcategory());

            holder.status.setText(inviteListStatus.get(position) + "");
            holder.address.setText(invite.getAddress());
            holder.imgCategory.setImageDrawable(Utility.getImage(context, invite));
            holder.meetDay.setText(Utility.arrangeDate(invite.getMeetDay()));
            holder.meetHour.setText(Utility.arrangeHour(invite.getMeetHour()));
            holder.hostAge.setText(Utility.getDiffYears(invite.getBirthday()) + "Y");




            return convertView;
        }

        private class ViewHolder {
            TextView name;
            TextView subcategory;
            TextView status;
            TextView address;
            TextView hostAge;
            TextView meetDay;
            TextView meetHour;
            ImageView imgCategory;
            ImageView imgHostGender;

        }

    }



}
