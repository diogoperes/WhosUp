package com.whosup.drawer.fragments;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;

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
import com.whosup.android.whosup.utils.Data;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SubCategory;
import com.whosup.drawer.placepicker.PlaceDetail;
import com.whosup.drawer.placepicker.PlacePicker;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class CreateInviteActivity extends Fragment {

    private Spinner spinnerCategory;
    // array list for spinner adapter
    private ArrayList<Category> categoriesList;
    //An array of all of our categories
    private JSONArray mCategories = null;
    //manages all of our categories in a list.
    private Spinner spinnerSubCategory;
    private Category categorySelected;
    private SubCategory subCategorySelected;
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
        spinnerCategory = (Spinner) rootview.findViewById(R.id.spinnerCategory);
        spinnerSubCategory = (Spinner) rootview.findViewById(R.id.spinnerSubCategory);

        // spinner item select listener
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();
                categorySelected = mCategoryList.get(position);

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

        // DEFINING MAPVIEW
        mMapView = (MapView) rootview.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

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
    }

    @Override
    public void onStop() {
        getActivity().setTitle(R.string.app_name);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCategoryList();
        updateSubCategory();
        mMapView.onResume();
        getActivity().setTitle(R.string.create_invite);
    }

    @Override
    public void onPause() {
        super.onPause();
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

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PlacePicker.REQUEST_CODE_PLACE && resultCode == PlacePicker.RESULT_OK) {
            PlaceDetail placeDetail = PlacePicker.fromIntent(data);
            PlacePicker.fetchDetails(api_key, data.getStringExtra(PlacePicker.PARAM_PLACE_ID), new PlacePicker.OnDetailFetched() {
                @Override
                public void completed(PlaceDetail placeInfoDetail) {
                    latitude = placeInfoDetail.latitude;
                    longitude = placeInfoDetail.longitude;
                    placeDescription.setText(placeInfoDetail.description);
                    String placeName = placeInfoDetail.description;

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
}
