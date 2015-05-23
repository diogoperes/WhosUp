package com.whosup.android.whosup.utils;

import java.util.ArrayList;

public class Category {

    private ArrayList<SubCategory> subcategories = new ArrayList<>();
    private String name;
    private String value;
    private int id;
    private String drawableID;

    public Category(){}

    public Category(Integer id, String name, String value, String drawableID){
        this.name = name;
        this.value = value;
        this.id = id;
        this.drawableID=drawableID;
    }

    public String getName(){
        return this.name;
    }

    public String getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public ArrayList<SubCategory> getSubcategories() {
        return subcategories;
    }

    public String getDrawableID() {
        return drawableID;
    }
}