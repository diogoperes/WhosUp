package com.whosup.android.whosup.utils;



import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.whosup.android.whosup.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Data {

    private static ArrayList<Category> categories = new ArrayList<>();

    private static Data sData=null;

    public static Data getInstance(){
        if(sData==null){
            sData = new Data();
        }
        return sData;
    }





    public ArrayList<Category> getCategories(Context c){
        if(categories.size()==0){
            String[] myCatArray = c.getResources().getStringArray(R.array.categories);
            String[] mySubCatArray;
            int subcategoryResourceId;
            String value;
            int id = 0;
            for (String s : myCatArray) {
                value=c.getResources().getStringArray(R.array.categories_identifier)[id];
                subcategoryResourceId=c.getResources().getIdentifier(value, "array", c.getPackageName());
                Log.d("IDENTIFICADOR ", "R.array."+value);
                mySubCatArray = c.getResources().getStringArray(subcategoryResourceId);
                Category cat = new Category(id, s, value);
                categories.add(cat);
                for (String sc : mySubCatArray) {
                    Log.d("SubCategory ", sc);
                    SubCategory subcat = new SubCategory(sc);
                    cat.getSubcategories().add(subcat);
                }
                id++;
            }
        }
        return categories;
    }




}
