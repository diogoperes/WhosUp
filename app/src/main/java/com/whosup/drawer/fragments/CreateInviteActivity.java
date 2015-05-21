package com.whosup.drawer.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.whosup.android.whosup.R;
import com.whosup.android.whosup.utils.Category;
import com.whosup.android.whosup.utils.ConnectionDetector;
import com.whosup.android.whosup.utils.Data;
import com.whosup.android.whosup.utils.DateDisplayPicker;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.ObservableScrollView;
import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.android.whosup.utils.ScrollViewListener;
import com.whosup.android.whosup.utils.SubCategory;
import com.whosup.android.whosup.utils.TimeDisplayPicker;
import com.whosup.android.whosup.utils.Utility;
import com.whosup.drawer.placepicker.PlaceDetail;
import com.whosup.drawer.placepicker.PlacePicker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateInviteActivity extends Fragment {

    private Spinner spinnerCategory;
    // array list for spinner adapter
    private ArrayList<Category> categoriesList;
    //An array of all of our categories
    private JSONArray mCategories = null;
    //manages all of our categories in a list.
    private Spinner spinnerSubCategory;
    private Category categorySelected = null;
    private int categorySelectedIndex = 0;
    private SubCategory subCategorySelected = null;
    private int subCategorySelectedIndex = 0;
    private ArrayList<Category> mCategoryList;
    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();
    private static final String LOG_TAG = "CreateInviteFragment";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private EditText editTextDescription;
    private Button mCreateInviteButton, mPickPlace;
    private TextView placeDescription;
    private String api_key = "AIzaSyC1JHD6BOCKX40CH5nIdh_6armrgWrW9-4";
    private Double latitude, longitude;
    private LatLng placeChosen;
    private GoogleMap map;
    private MapView mMapView;
    private DateDisplayPicker meetDay;
    private TimeDisplayPicker meetTime;
    private String usernameStr, firstNameStr, lastnameStr, genderStr, meetDayStr, meetTimeStr, description, currentDateStr, currentTimeStr, placeID;
    private String placeName = "";


    //Attempt Register Invite variables
    private ProgressDialog pDialog=null;
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final String CREATE_INVITE_URL = "http://whosup.host22.com/create_invite.php";
    ConnectionDetector cd;



    public CreateInviteActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_create_invite, container,
                false);

        final ObservableScrollView sv = (ObservableScrollView) rootview.findViewById(R.id.scrollView_create_invite);
        sv.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                sv.setVisibility(View.GONE);
                sv.setVisibility(View.VISIBLE);
            }
        });

        spinnerCategory = (Spinner) rootview.findViewById(R.id.spinnerCategory);
        spinnerSubCategory = (Spinner) rootview.findViewById(R.id.spinnerSubCategory);

        // spinner item select listener
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();
                categorySelected = mCategoryList.get(position);
                categorySelectedIndex = position;

                updateSubCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();
                subCategorySelected = categorySelected.getSubcategories().get(position);
                subCategorySelectedIndex = position;
                Log.v("SubCategory Selected P",subCategorySelectedIndex+"");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextDescription = (EditText) rootview.findViewById(R.id.description);
        editTextDescription.setMovementMethod(new ScrollingMovementMethod());
        editTextDescription.setOnTouchListener(touchListener);
        mPickPlace = (Button) rootview.findViewById(R.id.pick_place);
        mPickPlace.setOnClickListener(new PickPlaceOnClickListener());
        placeDescription = (TextView) rootview.findViewById(R.id.place_description);
        mCreateInviteButton = (Button) rootview.findViewById(R.id.create_invite);
        mCreateInviteButton.setOnClickListener(new CreateInviteOnClickListener());
        meetDay=(DateDisplayPicker)rootview.findViewById(R.id.clientEditCreate_MeetDayPicker);
        meetTime=(TimeDisplayPicker)rootview.findViewById(R.id.clientEditCreate_MeetTimePicker);
        // DEFINING MAPVIEW
        mMapView = (MapView) rootview.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.setClickable(false);
        return rootview;
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {

        super.onStart();
        updateCategoryList();
        updateSubCategory();
    }

    @Override
    public void onStop() {
        getActivity().setTitle(R.string.app_name);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences seti = getActivity().getSharedPreferences( "create_invite", 0);

        if(seti.contains("initialized")) {

            categorySelectedIndex = seti.getInt("categorySelectedIndex", 0);
            subCategorySelectedIndex = seti.getInt("subcategorySelectedIndex", 0);
            categorySelected = mCategoryList.get(categorySelectedIndex);
            subCategorySelected = categorySelected.getSubcategories().get(subCategorySelectedIndex);
            spinnerCategory.setSelection(categorySelectedIndex, false);
            spinnerSubCategory.setSelection(subCategorySelectedIndex, false);

            Log.v("Category Selected",categorySelected.getName() + "");
            Log.v("SubCategory Selected",subCategorySelected.getName() + "");
            Log.v("SubCategory SelectedI",subCategorySelectedIndex + "");
        }
        mMapView.onResume();
        getActivity().setTitle(R.string.create_invite);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences seti = getActivity().getSharedPreferences( "create_invite", 0);
        SharedPreferences.Editor edito = seti.edit();
        edito.putBoolean("initialized", true);
        edito.putInt("categorySelectedIndex", categorySelectedIndex);
        edito.putInt("subcategorySelectedIndex", subCategorySelectedIndex);
        Log.v("SubCategory Selected P",categorySelectedIndex+"");
        Log.v("SubCategory Selected I",subCategorySelectedIndex + "");

        edito.commit();

        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        getActivity().setTitle(R.string.app_name);
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    
    private void updateCategoryList() {

        mCategoryList= Data.getInstance().getCategories(getActivity().getApplicationContext());

        List<String> lables = new ArrayList<String>();
        if(categorySelected==null)
            categorySelected=mCategoryList.get(0);
        for(Category c : mCategoryList ){
            lables.add(c.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCategory.setAdapter(dataAdapter);
    }

    private void updateSubCategory(){
        List<String> lables = new ArrayList<String>();
        for(SubCategory sc : categorySelected.getSubcategories() ){
            lables.add(sc.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSubCategory.setAdapter(dataAdapter);
    }

    private class PickPlaceOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Create an intent with `PlacePicker.class`
            Intent intent = new Intent(getActivity(), PlacePicker.class);
            // Set your server api key (required)
            intent.putExtra(PlacePicker.PARAM_API_KEY, api_key);
            // Set extra query in a one line like below
            intent.putExtra(PlacePicker.PARAM_EXTRA_QUERY, "&rankby=distance");
            // Then start the intent for result
            startActivityForResult(intent, PlacePicker.REQUEST_CODE_PLACE);
        }
    }




    private class CreateInviteOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //checkConnection
            cd= new ConnectionDetector(getActivity().getApplicationContext());
            if(!cd.isConnectingToInternet()) {
                Toast.makeText(getActivity(), R.string.noInternetConnection, Toast.LENGTH_LONG).show();
                return;
            }

            if(!allFieldsOk()) {
                return;

            }else{
                // Save the Data in Database
                //loginDataBaseAdapter.insertEntry(userName, password);
                new AttemptCreateInvite().execute();


            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PlacePicker.REQUEST_CODE_PLACE && resultCode == PlacePicker.RESULT_OK) {
            PlaceDetail placeDetail = PlacePicker.fromIntent(data);
            placeID = data.getStringExtra(PlacePicker.PARAM_PLACE_ID);
            PlacePicker.fetchDetails(api_key, data.getStringExtra(PlacePicker.PARAM_PLACE_ID), new PlacePicker.OnDetailFetched() {
                @Override
                public void completed(PlaceDetail placeInfoDetail) {

                    latitude = placeInfoDetail.latitude;
                    longitude = placeInfoDetail.longitude;
                    placeDescription.setText(placeInfoDetail.description);
                    placeName = placeInfoDetail.description;

                    //DEBUG PURPOSES
                    System.out.println(placeInfoDetail.description);
                    System.out.println(latitude);
                    System.out.println(longitude);

                    //DISPLAY MAP
                    placeChosen = new LatLng(latitude, longitude);
                    displayMap(placeChosen, placeName);
                }
            });
            Log.v("=====PlacePicker=====", data.getStringExtra(PlacePicker.PARAM_PLACE_ID));
            Log.v("=====PlacePicker=====", data.getStringExtra(PlacePicker.PARAM_PLACE_DESCRIPTION));
        }

    }

    private void displayMap(LatLng position, String placeName) {
        // For showing a move to my loction button
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map = mMapView.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);

        // create marker
        MarkerOptions marker = new MarkerOptions().position(position).title(placeName);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        map.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(14).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMapView.invalidate();
    }

    private boolean allFieldsOk(){
        usernameStr= SPreferences.getInstance().getLoginUsername(getActivity().getApplicationContext());
        firstNameStr=SPreferences.getInstance().getLoginFirstName(getActivity().getApplicationContext());
        lastnameStr=SPreferences.getInstance().getLoginLastName(getActivity().getApplicationContext());
        genderStr=SPreferences.getInstance().getLoginGender(getActivity().getApplicationContext());
        description = editTextDescription.getText().toString();

        //put meetDay Date to String
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Date currentDate = null;
        meetDayStr = df.format(meetDay.getDate());
        Log.d("meetDay: ", meetDayStr);

        try {
            currentDate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //put meetTime Time to String
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        //Date date = new Date();
        meetTimeStr = fmt.format(meetTime.getDate());
        Log.d("meetTime: ", meetTimeStr);

        //current Date
        Calendar cal = Calendar.getInstance();
        currentDateStr = df.format(cal.getTime());

        //currentTime
        currentTimeStr = fmt.format(cal.getTime());

        String md = meetDayStr + " " + meetTimeStr;
        SimpleDateFormat sourceFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat desiredFormat   = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
        Date dateFormated = null;
        Date currentDateFormatted = null;
        String currentDateFormattedStr = desiredFormat.format(cal.getTime());
        try {
            Date date = sourceFormat.parse(md);
            String dateFormatedStr = desiredFormat.format(date);
            dateFormated = desiredFormat.parse(dateFormatedStr);
            currentDateFormatted = desiredFormat.parse(currentDateFormattedStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> differenceDate = Utility.getDifferenceTime(currentDateFormatted, dateFormated);
        if(differenceDate.get(0) ==0){
            if(differenceDate.get(1) ==0){
                if(differenceDate.get(2) == 0){
                    Toast.makeText(getActivity(), R.string.invalid_date, Toast.LENGTH_LONG).show();
                    return false;
                }else if(differenceDate.get(2) < 0){
                    Toast.makeText(getActivity(), R.string.invalid_date, Toast.LENGTH_LONG).show();
                    return false;
                }
            }else if(differenceDate.get(1) < 0){
                Toast.makeText(getActivity(), R.string.invalid_date, Toast.LENGTH_LONG).show();
                return false;
            }
        }else if(differenceDate.get(0) < 0){
            Toast.makeText(getActivity(), R.string.invalid_date, Toast.LENGTH_LONG).show();
            return false;
        }


        Log.v("DIFFERENCE TIME", "" + differenceDate.get(0) + differenceDate.get(1) +differenceDate.get(2));

        if(placeName.equals("")){
            Toast.makeText(getActivity(), R.string.enter_place, Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    public class AttemptCreateInvite extends AsyncTask<String, String, String> {



        /**
         * Before starting background thread Show Progress Dialog
         * */

        int success =0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog=null;
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Creating Invite...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... args) {

            // Check for success tag
            String birthdayStr=SPreferences.getInstance().getLoginBirthday(getActivity().getApplicationContext());
            Log.v("BIRTHDAY", birthdayStr);
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", usernameStr));
                Log.v("USERNAME SENT", usernameStr);
                params.add(new BasicNameValuePair("firstName", firstNameStr));
                Log.v("FIRSTNAME SENT", firstNameStr);
                params.add(new BasicNameValuePair("lastName", lastnameStr));
                Log.v("LASTNAME SENT", lastnameStr);
                params.add(new BasicNameValuePair("gender", genderStr));
                Log.v("GENDER SENT", genderStr);
                params.add(new BasicNameValuePair("birthday",birthdayStr));
                params.add(new BasicNameValuePair("meetDay", meetDayStr));
                params.add(new BasicNameValuePair("meetHour", meetTimeStr));
                params.add(new BasicNameValuePair("currentDate", currentDateStr));
                params.add(new BasicNameValuePair("currentTime", currentTimeStr));
                params.add(new BasicNameValuePair("categoryList", categorySelected.getName()));
                params.add(new BasicNameValuePair("subcategoriesList", subCategorySelected.getName()));
                params.add(new BasicNameValuePair("description", description));
                params.add(new BasicNameValuePair("latitude", latitude.toString()));
                params.add(new BasicNameValuePair("longitude", longitude.toString()));
                params.add(new BasicNameValuePair("placeID",placeID));
                params.add(new BasicNameValuePair("address",placeName));




                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        CREATE_INVITE_URL, "POST", params);

                // check your log for json response
                Log.d("Creating Invite attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Invite Created!", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Invite Failure!", json.getString(TAG_MESSAGE));

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
                Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), R.string.account_successfully_created, Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();

            }else{
                Toast.makeText(getActivity(), R.string.noConnection, Toast.LENGTH_LONG).show();

            }


        }

    }




}
