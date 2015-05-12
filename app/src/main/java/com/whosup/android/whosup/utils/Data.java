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
            for (String s : myCatArray) {
                Category cat = new Category(s);
                categories.add(cat);
            }
        }
        return categories;
    }


//    public ArrayList<SubCategory> getSubCategories(Context c){
//        //getResourceId
//    }


}
