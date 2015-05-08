package com.whosup.android.whosup.utils;


public class SubCategory {

    private Category category;
    private String subCategory;

    public SubCategory(Category category, String subCategory){
        //TODO adicionar id à categoria para poder ir buscar na sub
    }


    public Category getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }
}
