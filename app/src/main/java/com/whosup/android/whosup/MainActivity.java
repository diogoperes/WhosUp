package com.whosup.android.whosup;

import android.app.ProgressDialog;
import android.content.Context;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.drawer.FragmentDrawer;
import com.whosup.drawer.fragments.CreateInviteActivity;
import com.whosup.drawer.fragments.Friends;
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

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private ArrayList<Invite> inviteList;
    private Menu menu;
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
    private ListView inviteListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inviteList = new ArrayList<>();

        //GET USERNAME AND EMAIL
        SPreferences userSession = new SPreferences();
        TextView usernameTextView = (TextView) findViewById(R.id.username);
        TextView emailTextView = (TextView) findViewById(R.id.email);
        usernameTextView.setText(userSession.getLoginFirstName(this.getApplicationContext()) + " " +
                userSession.getLoginLastName(this.getApplicationContext()));
        emailTextView.setText(userSession.getLoginUsername(this.getApplicationContext()));
        new LoadInvites().execute();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        requestLocationUpdate();


        /*
        // Get the ListView by Id and instantiate the adapter with
        // invite data and then set it the ListView
        ListView inviteListView = (ListView) findViewById(R.id.list_invites);
        InviteAdapter adapter = new InviteAdapter(this, inviteList, my);
        inviteListView.setAdapter(adapter);
        // Set the onItemClickListener on the ListView to listen for items clicks
        */



        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getApplicationContext().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(query);
                //COLLAPSE KEYBOARD AND SEARCHVIEW -.-
                searchView.setIconified(true);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


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
            case R.id.action_refresh:
                new refreshInvites(this).execute();
                return true;
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

    public class refreshInvites extends AsyncTask<Void, Void, Void> {
        private Context mCon;

        public refreshInvites(Context con) {
            mCon = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=null;
            pDialog = new ProgressDialog(mCon);
            pDialog.setMessage(getApplicationContext().getString(R.string.refreshing_invites));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // TODO Refreshing invites
                Thread.sleep(4000);
                return null;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void params) {
            if(pDialog!=null)
                pDialog.dismiss();
        }
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

                    inviteListView = (ListView) findViewById(R.id.list_invites);
                    InviteAdapter adapter = new InviteAdapter(MainActivity.this, inviteList, mLastLocation);
                    inviteListView.setAdapter(adapter);

                    inviteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });


                }
            });

        }
    }







    private void requestLocationUpdate() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_COARSE);
        crta.setAltitudeRequired(true);
        crta.setBearingRequired(true);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_LOW);
//        String provider = locationManager.getBestProvider(crta, true);
        String provider = LocationManager.NETWORK_PROVIDER;
        Log.d("","provider : "+provider);
//        // String provider = LocationManager.GPS_PROVIDER;
//        locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                System.out.println("MY LOCATION HAS CHANGED: "+location);
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        });
        mLastLocation = locationManager.getLastKnownLocation(provider);
        System.out.println("MY FIRST LAST LOCATION: "+mLastLocation);
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
//        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

}
