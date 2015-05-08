package com.whosup.drawer.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.whosup.android.whosup.R;
import com.whosup.android.whosup.utils.Category;
import com.whosup.android.whosup.utils.Data;
import com.whosup.android.whosup.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateInviteActivity extends Fragment {

    ProgressDialog pDialog;
    private Spinner spinnerCategory;
    // array list for spinner adapter
    private ArrayList<Category> categoriesList;




    //An array of all of our categories
    private JSONArray mCategories = null;
    //manages all of our categories in a list.
    private ArrayList<Category> mCategoryList;

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    public CreateInviteActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.activity_create_invite, container,
                false);
        spinnerCategory = (Spinner) rootview.findViewById(R.id.spinnerCategory);
        categoriesList = new ArrayList<Category>();

        // spinner item select listener
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                /*Toast.makeText(parent.getContext(), "You selected: " + label,
                        Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        updateList();

        getActivity().setTitle(R.string.title_home);
    }







    /**
     * Inserts the parsed data into the listview.
     */
    private void updateList() {

        mCategoryList= Data.getInstance().getCategories(getActivity().getApplicationContext());

        List<String> lables = new ArrayList<String>();
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





}
