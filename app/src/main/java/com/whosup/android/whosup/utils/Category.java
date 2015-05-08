package com.whosup.android.whosup.utils;

public class Category {

    private String name;


    public Category(){}

    public Category(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}