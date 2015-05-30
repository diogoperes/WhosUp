package com.whosup.android.whosup;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.GPSTracker;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.User;
import com.whosup.android.whosup.utils.Utility;
import com.whosup.drawer.FragmentDrawer;
import com.whosup.drawer.fragments.CreateInviteActivity;
import com.whosup.drawer.fragments.MyAttendeesFragment;
import com.whosup.drawer.fragments.MyInvitesFragment;
import com.whosup.drawer.fragments.ViewProfileFragment;
import com.whosup.listview.Invite;
import com.whosup.listview.InviteAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private static ArrayList<Invite> inviteList;
    private Menu menu;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private final String TAG_LATITUDE = "latitude";
    private final String TAG_LONGITUDE = "longitude";
    private final String TAG_RAY = "ray";
    private final String TAG_USERNAME = "username";
    private final String GET_INVITES_URL = "http://whosup.host22.com/get_invites.php";
    private String myLatitude = "0";
    private String myLongitude = "0";
    public String myRay = "2";
    JSONArray invitesList = null;
    private ListView inviteListView;
    private ArrayList<String> categoriesSelected = new ArrayList<>();
    private GPSTracker gps;
    LocationManager locationManager;
    ConnectionDetector cd;
    private final int DO_IN_BACKGROUND_TIMEOUT = 5 * 1000;
    private boolean myInvitesFragmentIsOpen = false, myProfileIsOpen = false;
    private EditText radiusEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inviteList = new ArrayList<>();
        inviteListView = (ListView) findViewById(R.id.list_invites);
        //GET USERNAME AND EMAIL
        SPreferences userSession = new SPreferences();
        TextView usernameTextView = (TextView) findViewById(R.id.username);
        TextView emailTextView = (TextView) findViewById(R.id.email);
        usernameTextView.setText(userSession.getLoginFirstName(this.getApplicationContext()) + " " +
                userSession.getLoginLastName(this.getApplicationContext()));
        emailTextView.setText(userSession.getLoginUsername(this.getApplicationContext()));
        /*radiusEditText = (EditText) findViewById(R.id.radiusEditText);
        Button filter_button = (Button) findViewById(R.id.filter_button);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/

        /*radiusEditText.clearFocus();
        usernameTextView.requestFocus();*/
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gps = new GPSTracker(this);

        TryLoad();
        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }


        inviteListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                Intent i = new Intent(getApplicationContext(), ViewInviteActivity.class);
                i.putExtra("can_attend", "1");
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

    public void refreshWithFilters(String ray, ArrayList<String> categories){
        categoriesSelected = categories;
        double km = Integer.parseInt(ray) / 100.0;
        myRay=km+"";
        System.out.println("RAY ENTERED: " + myRay);
        TryLoad();
    }


    public void TryLoad(){
        /*System.out.println("RADIUS EDIT TEXT INPUT: " + radiusEditText.getText().toString());
        radiusEditText.clearFocus();
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(radiusEditText.getText().toString().length()>0){
            System.out.println("RADIUS INT PARSE: " + Integer.parseInt(radiusEditText.getText().toString()));

            double km = Integer.parseInt(radiusEditText.getText().toString()) / 90.0;
            String.format("%.2f", km);
            System.out.println("KM DISTANCE VIEW: " + km);
            myRay = km + "";
        }else{
            myRay="0";
        }*/
        System.out.println("MY RAY: " + myRay);

        mLastLocation=gps.getLocation();
        if(gps.canGetLocation()){

        }/*else{
            gps.showSettingsAlert();
        }*/
        //requestLocationUpdate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isNetworkEnabled, isGPSEnabled;

        cd= new ConnectionDetector(getApplicationContext());


        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        System.out.println("GPS STATE: " + isGPSEnabled);
        if(!cd.isConnectingToInternet()) {
            Toast.makeText(this, "Can't connect to the network.", Toast.LENGTH_SHORT).show();
        }else if(!isGPSEnabled){
            gps.showSettingsAlert();
        }else if(mLastLocation==null){
            gps.turnOnHighPrecisionMode();
        }else{
            new LoadInvites().execute();
            //Utility.displayPromptForEnablingGPS(this);
        }
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

                //COLLAPSE KEYBOARD AND SEARCHVIEW -.-
                searchView.setIconified(true);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                System.out.println(query);
                ArrayList<Invite> inviteListFiltered = new ArrayList<>();

                for(Invite i : inviteList){
                    String name = i.getFirstName() + " "+ i.getLastName();
                    String date = Utility.arrangeDate(i.getMeetDay());
                    String time = Utility.arrangeHour(i.getMeetHour());
                    //System.out.println("INVITE DAY: " + date + " TIME: " + time);
                    if( name.toLowerCase().contains(query) || i.getSubcategory().toLowerCase().contains(query) || i.getAddress().toLowerCase().contains(query)
                            || date.contains(query) || time.contains(query)){
                        inviteListFiltered.add(i);
                    }
                }

                InviteAdapter adapter = new InviteAdapter(MainActivity.this, inviteListFiltered, mLastLocation);
                inviteListView.setAdapter(adapter);
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
//                new refreshInvites(this).execute();
                TryLoad();
                return true;
            case R.id.action_filter:
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragment = new FilterFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.filter_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                System.out.println("ADD TO BACKSTACK");
                fragmentTransaction.commit();
                return true;
            case R.id.action_viewmap:
                Intent intent = new Intent(MainActivity.this, ViewMapActivity.class);
                startActivity(intent);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                System.out.println("BACK STACK CLEARED" + "286");
                myInvitesFragmentIsOpen=false;
                myProfileIsOpen=false;
                fragment = new CreateInviteActivity();
                title = getString(R.string.create_invite);
                break;
            case 1:
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                System.out.println("BACK STACK CLEARED" + "294");
                myInvitesFragmentIsOpen=false;
                myProfileIsOpen=true;
                //Intent i = new Intent(MainActivity.this, ViewProfileFragment.class );
                User myUser = new User(true,
                        SPreferences.getInstance().getLoginUsername(MainActivity.this),
                        SPreferences.getInstance().getLoginFirstName(MainActivity.this),
                        SPreferences.getInstance().getLoginLastName(MainActivity.this),
                        SPreferences.getInstance().getLoginGender(MainActivity.this),
                        SPreferences.getInstance().getLoginBirthday(MainActivity.this),
                        SPreferences.getInstance().getLoginCity(MainActivity.this),
                        SPreferences.getInstance().getLoginCountry(MainActivity.this),
                        SPreferences.getInstance().getLoginPhotoLink(MainActivity.this),
                        SPreferences.getInstance().getLoginAboutMe(MainActivity.this),
                        SPreferences.getInstance().getLoginCustomPhrase(MainActivity.this));
                //i.putExtra("user", myUser);

                Bundle args = new Bundle();
                args.putSerializable("user", myUser);

                fragment = new ViewProfileFragment();
                fragment.setArguments(args);




                title = getString(R.string.view_profile);
                break;
            case 2:
                if (!myInvitesFragmentIsOpen){
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    System.out.println("BACK STACK CLEARED" + "325");
                    myInvitesFragmentIsOpen = true;
                    myProfileIsOpen=false;
                    fragment = new MyInvitesFragment();
                    title = getString(R.string.my_invites);
                }
                break;
            case 3:
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment = new MyAttendeesFragment();

                title = getString(R.string.my_attendees);
                break;
            default:
                break;
        }


        if (fragment != null) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            System.out.println("ADD TO BACKSTACK");
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

    class TaskKiller extends TimerTask {
        private AsyncTask<?, ?, ?> mTask;
        public TaskKiller(AsyncTask<?, ?, ?> task) {
            this.mTask = task;
        }

        public void run() {
            System.out.println("TIMER CANCELED");
            pDialog.dismiss();
            mTask.cancel(true);
        }
    }

    /**
     * Background Async Task to Load all Albums by making http request
     * */
     @SuppressWarnings("deprecation")
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


        public void filterCategories(){
            ArrayList<Invite> invitesListFiltered = new ArrayList<Invite>();
            if (categoriesSelected.size()>0) {
                for (Invite i : inviteList) {
                    for (String cs : categoriesSelected) {
                        if (i.getCategory().equals(cs)) {
                            invitesListFiltered.add(i);
                        }
                    }
                }
                inviteList = invitesListFiltered;
            }
        }

        /**
         * getting Albums JSON
         */
        protected String doInBackground(String... args) {
            Timer timer = new Timer();
            System.out.println("TIMER STARTED");
            timer.schedule(new TaskKiller(this), DO_IN_BACKGROUND_TIMEOUT);
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_LATITUDE, mLastLocation.getLatitude()+""));
            params.add(new BasicNameValuePair(TAG_LONGITUDE, mLastLocation.getLongitude()+""));
            params.add(new BasicNameValuePair(TAG_RAY, myRay));
            params.add(new BasicNameValuePair(TAG_USERNAME, SPreferences.getInstance().getLoginUsername(getApplicationContext())));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(GET_INVITES_URL, "POST",
                    params);



            // Check your log cat for JSON reponse
            //Log.d("Invites JSON: ", "> " + json.toString());
            inviteList.clear();
            try {
                invitesList = json.getJSONArray("Invites");
                //Log.d("Invites JSONArray: ", "> " + invitesList);
                if (invitesList != null) {
                    // looping through All albums
                    for (int i = 0; i < invitesList.length(); i++) {
                        JSONObject c = invitesList.getJSONObject(i);


                        // Storing each json item values in variable
                        String id = c.getString("id");
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
                        String isOpen = c.getString("isOpen");
                        inviteList.add(new Invite(id, username, firstName, lastName, gender, birthday, postDay, postHour, meetDay, meetHour, category, subcategory, description, latitude,
                                longitude, placeID, address, isOpen));
                    }
                } else {
                    Log.d("Invites: ", "null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("TIMER ENDED");
            timer.cancel();
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
            /*runOnUiThread(new Runnable() {
                public void run() {*/
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    try{
                        updateDistances(inviteList);
                        sortListByDistance(inviteList);
                        filterCategories();
                        System.out.println("INVITE LIST FIRST: " + inviteList.get(0).getDistanceFromMe());
                        InviteAdapter adapter = new InviteAdapter(MainActivity.this, inviteList, mLastLocation);

                        inviteListView.setAdapter(adapter);
                    }catch (Exception ex){
                        Toast.makeText(getApplicationContext(), "Can't load invites, try refreshing", Toast.LENGTH_SHORT).show();
                    }





               // }
           // });

        }

        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "Can't retrieve invites, try refreshing", Toast.LENGTH_SHORT).show();
        }
    }










/*
    private void requestLocationUpdate() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_COARSE);
        crta.setAltitudeRequired(true);
        crta.setBearingRequired(true);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(crta, true);
        //String provider = LocationManager.NETWORK_PROVIDER;
        if(provider==null){
            provider=LocationManager.NETWORK_PROVIDER;
        }
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
*/

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
        System.out.println("My Profile Fragment open: " + myProfileIsOpen);
        System.out.println("My Invites Fragment open: " + myInvitesFragmentIsOpen);
        if(myProfileIsOpen){
            myProfileIsOpen=false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            System.out.println("BACK STACK CLEARED" + 588);
        }
        System.out.println("MAIN RESUMED");
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

    public void sortListByDistance(ArrayList<Invite> list){
        Collections.sort(list, new Comparator<Invite>() {
            @Override
            public int compare(Invite i1, Invite i2) {
                //System.out.println("A ORGANIZAR: " + i1.getDistanceFromMe());
                if (i1.getDistanceFromMe() == i2.getDistanceFromMe())
                    return 0;
                return i1.getDistanceFromMe() < i2.getDistanceFromMe() ? -1 : 1;
            }
        });


    }

    public void updateDistances(ArrayList<Invite> il){

        for(Invite invite : il) {
            Location inviteLocation = new Location("");
            inviteLocation.setLatitude(Double.parseDouble(invite.getLatitude()));
            inviteLocation.setLongitude(Double.parseDouble(invite.getLongitude()));
            //Log.v("CURRENT LOCATION", "" + myLocation);


            float distanceInMeters = inviteLocation.distanceTo(mLastLocation);

            //Log.v("DISTANCE TO", "" + distanceInMeters);
            double distanceInKm = distanceInMeters / 1000;
            distanceInKm = Math.round(distanceInKm * 100.0) / 100.0;
            invite.setDistanceFromMe(distanceInKm);
            //System.out.println("SET DISTANCE FROM ME: " + distanceInKm);
        }

    }

    public static ArrayList<Invite> getInviteList() {
        return inviteList;
    }

    @Override
    public void onBackPressed() {
        System.out.println("BACKSTACK COUNT: " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Program")
                    .setMessage("Are you sure you want to close 'who's up'?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            myInvitesFragmentIsOpen=false;
            myProfileIsOpen=false;
            getSupportFragmentManager().popBackStack();
        }

    }


    public static class FilterFragment extends Fragment {
        EditText radius;
        ArrayList<String> categoriesSelected = new ArrayList<>();
        Button filterButton;
        String ray = "100";
        boolean radio_restaurant = false, radio_bar_cafe = false, radio_outdoor_sport=false,  radio_culture_art=false;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootview = inflater.inflate(R.layout.fragment_filters, null);
            radius = (EditText) rootview.findViewById(R.id.radiusEditText);
            filterButton = (Button) rootview.findViewById(R.id.filter_button);
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radio_restaurant)categoriesSelected.add("Restaurant");
                    if(radio_bar_cafe)categoriesSelected.add("Bar / Cafe");
                    if(radio_outdoor_sport)categoriesSelected.add("Outdoor / Sport");
                    if(radio_culture_art)categoriesSelected.add("Culture / Art");


                    ray = radius.getText().toString();

                    ((MainActivity) getActivity()).refreshWithFilters(ray, categoriesSelected);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });

            CheckBox radio_restaurant = (CheckBox) rootview.findViewById(R.id.radio_restaurant);
            radio_restaurant.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickButton(v);//THIS IS THE METHOD YOU WROTE ON THE ATTACHED CODE!!
                }
            });
            CheckBox radio_bar_cafe = (CheckBox) rootview.findViewById(R.id.radio_bar_cafe);
            radio_bar_cafe.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickButton(v);//THIS IS THE METHOD YOU WROTE ON THE ATTACHED CODE!!
                }
            });
            CheckBox radio_outdoor_sport = (CheckBox) rootview.findViewById(R.id.radio_outdoor_sport);
            radio_outdoor_sport.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickButton(v);//THIS IS THE METHOD YOU WROTE ON THE ATTACHED CODE!!
                }
            });
            CheckBox radio_culture_art = (CheckBox) rootview.findViewById(R.id.radio_culture_art);
            radio_culture_art.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickButton(v);//THIS IS THE METHOD YOU WROTE ON THE ATTACHED CODE!!
                }
            });



            return rootview;
        }



        public void onClickButton(View view) {
            // Is the view now checked?
            boolean checked = ((CheckBox) view).isChecked();

            // Check which checkbox was clicked
            switch(view.getId()) {
                case R.id.radio_restaurant:
                    if (checked) {
                        radio_restaurant=true;
                    }else {
                        radio_restaurant=false;
                    }
                    break;
                case R.id.radio_bar_cafe:
                    if (checked) {
                        radio_bar_cafe=true;
                    }else {
                        radio_bar_cafe=false;
                    }
                    break;
                case R.id.radio_outdoor_sport:
                    if (checked) {
                        radio_outdoor_sport=true;
                    }else {
                        radio_outdoor_sport=false;
                    }
                    break;
                case R.id.radio_culture_art:
                    if (checked) {
                        radio_culture_art=true;
                    }else {
                        radio_culture_art=false;
                    }
                    break;

            }
        }

    }



}
