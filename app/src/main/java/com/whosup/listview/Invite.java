package com.whosup.listview;

import com.whosup.android.whosup.utils.InviteAttend;

import java.io.Serializable;
import java.util.ArrayList;

public class Invite implements Serializable {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;
    private String postDay;
    private String postHour;
    private String meetDay;
    private String meetHour;
    private String category;
    private String subcategory;
    private String description;
    private String latitude;
    private String longitude;
    private String placeID;
    private String address;
    private String isOpen;
    private double distanceFromMe;
    private ArrayList<InviteAttend> inviteAttends = new ArrayList<>();



    public Invite(String id, String username, String firstName, String lastName, String gender, String birthday, String postDay, String postHour, String meetDay, String meetHour, String category
            , String subcategory, String description, String latitude, String longitude, String placeID, String address, String isOpen) {
        super();
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.postDay = postDay;
        this.postHour = postHour;
        this.meetDay = meetDay;
        this.meetHour = meetHour;
        this.category = category;
        this.subcategory = subcategory;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeID = placeID;
        this.address = address;
        this.isOpen=isOpen;



    }


    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPostDay() {
        return postDay;
    }

    public String getPostHour() {
        return postHour;
    }

    public String getMeetDay() {
        return meetDay;
    }

    public String getMeetHour() {
        return meetHour;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPlaceID() {
        return placeID;
    }

    public String getAddress() {
        return address;
    }

    public double getDistanceFromMe() {
        return distanceFromMe;
    }

    public String getId() {
        return id;
    }

    public void setDistanceFromMe(double distanceFromMe) {
        this.distanceFromMe = distanceFromMe;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public ArrayList<InviteAttend> getInviteAttends() {
        return inviteAttends;
    }

    public void setInviteAttends(ArrayList<InviteAttend> inviteAttends) {
        this.inviteAttends = inviteAttends;
    }
}
