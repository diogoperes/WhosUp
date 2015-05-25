package com.whosup.android.whosup;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.whosup.listview.Invite;

import java.util.ArrayList;

public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //ADDING MARKERS OF INVITE'S PLACE LOCATIONS
        /* TODO MARKERS CLUSTERING */
        ArrayList<Invite> invitesList = MainActivity.getInviteList();
        for(int i = 0; i < invitesList.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(invitesList.get(i).getLatitude()),
                                         Double.parseDouble(invitesList.get(i).getLongitude())))
                    .title(invitesList.get(i).getAddress())
                    .snippet(invitesList.get(i).getDescription()));
        }

        //SOME UI SETTINGS
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setMyLocationEnabled(true);

        //CENTER CAMERA ON YOUR CURRENT LOCATION. ZOOM: 10
        //VARIABLE IS NEEDED TO ANIMATE CAMERA ONLY ONCE
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            int animateCameraOnce = 1;
            @Override
            public void onMyLocationChange(Location location) {
                System.out.println("ViewMap @ onMyLocationChange: " + location);
                if(animateCameraOnce == 1) {
                    LatLng myLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 10);
                    mMap.animateCamera(cameraUpdate);
                }
                animateCameraOnce--;
            }
        });
    }
}
