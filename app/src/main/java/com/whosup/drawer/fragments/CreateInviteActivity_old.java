//package com.whosup.drawer.fragments;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.internal.widget.AdapterViewCompat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListAdapter;
//import android.widget.SimpleAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.whosup.android.whosup.R;
//import com.whosup.android.whosup.utils.Category;
//import com.whosup.android.whosup.utils.JSONParser;
//
//import org.apache.http.NameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class CreateInviteActivity_old extends Fragment {
//
//    ProgressDialog pDialog;
//    private Spinner spinnerCategory;
//    // array list for spinner adapter
//    private ArrayList<Category> categoriesList;
//    private String URL_CATEGORIES = "http://whosup.host22.com/category_list.php";
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_ID = "id";
//    private static final String TAG_CATEGORY = "category";
//    private static final String TAG_CATEGORIES = "categories";
//    private static final String TAG_MESSAGE = "message";
//
//    //An array of all of our categories
//    private JSONArray mCategories = null;
//    //manages all of our categories in a list.
//    private ArrayList<Category> mCategoryList;
//
//    // Creating JSON Parser object
//    JSONParser jsonParser = new JSONParser();
//
//    public CreateInviteActivity_old() {
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootview = inflater.inflate(R.layout.activity_create_invite, container,
//                false);
//        spinnerCategory = (Spinner) rootview.findViewById(R.id.spinnerCategory);
//        categoriesList = new ArrayList<Category>();
//
//        // spinner item select listener
//        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//
//                // On selecting a spinner item
//                String label = parent.getItemAtPosition(position).toString();
//
//                // Showing selected spinner item
//                Toast.makeText(parent.getContext(), "You selected: " + label,
//                        Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        return rootview;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        new GetCategories().execute();
//        getActivity().setTitle(R.string.title_home);
//    }
//
//
//
//    /**
//     * Async task to get all food categories
//     * */
//    private class GetCategories extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Fetching food categories..");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            mCategoryList = new ArrayList<Category>();
//            JSONParser jParser = new JSONParser();
//            try {
//                JSONArray data = new JSONArray(jParser.getJSONUrl(URL_CATEGORIES));
//                //Log.d("Categories List: ", jParser.getJSONUrl(URL_CATEGORIES));
//                for(int i = 0; i < data.length(); i++){
//                    JSONObject c = data.getJSONObject(i);
//                    Category cat = new Category(Integer.parseInt(c.getString(TAG_ID)),c.getString(TAG_CATEGORY));
//                    mCategoryList.add(cat);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            updateList();
//            //populateSpinner();
//        }
//
//    }
//
//
//
//    /**
//     * Inserts the parsed data into the listview.
//     */
//    private void updateList() {
//
//        List<String> lables = new ArrayList<String>();
//        for(Category c : mCategoryList ){
//            lables.add(c.getName());
//        }
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, lables);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinnerCategory.setAdapter(dataAdapter);
//
//
//    }
//
//
//
//
//
//}
