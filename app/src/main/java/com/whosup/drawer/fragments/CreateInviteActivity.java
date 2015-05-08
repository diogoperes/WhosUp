package com.whosup.drawer.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.whosup.android.whosup.R;
import com.whosup.android.whosup.utils.Category;
import com.whosup.android.whosup.utils.Data;
import com.whosup.android.whosup.utils.JSONParser;
import com.whosup.android.whosup.utils.SubCategory;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class CreateInviteActivity extends Fragment {

    private Spinner spinnerCategory;
    private Spinner spinnerSubCategory;
    private Category categorySelected;
    private SubCategory subCategorySelected;




    private ArrayList<Category> mCategoryList;

    public CreateInviteActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_create_invite, container,
                false);
        spinnerCategory = (Spinner) rootview.findViewById(R.id.spinnerCategory);
        spinnerSubCategory = (Spinner) rootview.findViewById(R.id.spinnerSubCategory);

        // spinner item select listener
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();
                categorySelected=mCategoryList.get(position);


                updateSubCategory();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();
                subCategorySelected = categorySelected.getSubcategories().get(position);

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
        updateCategoryList();
        updateSubCategory();
        getActivity().setTitle(R.string.title_home);
    }







    /**
     * Inserts the parsed data into the listview.
     */
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
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSubCategory.setAdapter(dataAdapter);


    }


}
