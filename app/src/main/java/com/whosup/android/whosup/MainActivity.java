package com.whosup.android.whosup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.drawer.FragmentDrawer;
import com.whosup.drawer.fragments.Friends;
import com.whosup.drawer.fragments.CreateInviteActivity;
import com.whosup.drawer.fragments.Messages;
import com.whosup.listview.Invite;
import com.whosup.listview.InviteAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, AdapterView.OnItemClickListener {

    private ArrayList<Invite> inviteList;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private final String TAG_LATITUDE = "latitude";
    private final String TAG_LONGITUDE = "longitude";
    private final String TAG_RAY = "ray";
    private final String GET_INVITES_URL = "http://whosup.host22.com/get_invites.php";
    private String myLatitude = "38";
    private String myLongitude = "-9";
    private String myRay = "2";
    JSONArray invitesList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inviteList = new ArrayList<>();

        new LoadInvites().execute();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the ListView by Id and instantiate the adapter with
        // invite data and then set it the ListView
        ListView inviteListView = (ListView) findViewById(R.id.list_invites);
        InviteAdapter adapter = new InviteAdapter(this, inviteList);
        inviteListView.setAdapter(adapter);
        // Set the onItemClickListener on the ListView to listen for items clicks
        inviteListView.setOnItemClickListener(this);

        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this.getApplicationContext(), "jowf", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_exit:
                System.exit(0);
                return true;
            case R.id.action_logout:
                SPreferences.getInstance().discardLoginCredentials(getApplicationContext());
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new CreateInviteActivity();
                title = getString(R.string.create_invite);
                break;
            case 1:
                fragment = new Friends();
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new Messages(    );
                title = getString(R.string.title_messages);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            // set the toolbar title
            setTitle(title);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Invite selectedInvite = inviteList.get(position);
        //Toast.makeText(this, "You've selected :\n Invite from : " + selectedInvite.getFrom() + "\n Place : " + selectedInvite.getPlace(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Background Async Task to Load all Albums by making http request
     * */
    class LoadInvites extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Listing Invites ...");
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
            params.add(new BasicNameValuePair(TAG_LATITUDE, myLatitude));
            params.add(new BasicNameValuePair(TAG_LONGITUDE, myLongitude));
            params.add(new BasicNameValuePair(TAG_RAY, myRay));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(GET_INVITES_URL, "POST",
                    params);

            // Check your log cat for JSON reponse
            Log.d("Albums JSON: ", "> " + json.toString());

            try {
                invitesList = json.getJSONArray("Invites");
                Log.d("Albums JSONArray: ", "> " + invitesList);
                if (invitesList != null) {
                    // looping through All albums
                    for (int i = 0; i < invitesList.length(); i++) {
                        JSONObject c = invitesList.getJSONObject(i);


                        // Storing each json item values in variable
                        String username = c.getString("username");
                        String firstName = c.getString("firstName");
                        String lastName = c.getString("lastName");
                        String gender = c.getString("gender");
                        String birthday = c.getString("birthday");
                        String postDay = c.getString("postDay");
                        String postHour = c.getString("postHour");
                        String meetDay = c.getString("meetDay");
                        String meetHour = c.getString("meetHour");
                        String category = c.getString("category");
                        String subcategory = c.getString("subcategory");
                        String description = c.getString("description");
                        String latitude = c.getString("latitude");
                        String longitude = c.getString("longitude");
                        String placeID = c.getString("placeID");
                        String address = c.getString("address");
                        inviteList.add(new Invite(username, firstName, lastName, gender, birthday, postDay, postHour, meetDay, meetHour, category, subcategory, description, latitude,
                                longitude, placeID, address));
                    }
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
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    ListView inviteListView = (ListView) findViewById(R.id.list_invites);
                    InviteAdapter adapter = new InviteAdapter(MainActivity.this, inviteList);
                    inviteListView.setAdapter(adapter);



                }
            });

        }
    }

}
