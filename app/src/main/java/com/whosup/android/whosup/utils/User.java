package com.whosup.android.whosup.utils;


public class User {

    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;
    private String city;
    private String country;
    private String photoLink;
    private String aboutMe;
    private String customPhrase;

    public User(String username, String firstName, String lastName, String gender, String birthday, String city, String country,
                  String photoLink, String aboutMe, String customPhrase) {
        super();
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.city = city;
        this.country = country;
        this.photoLink = photoLink;
        this.aboutMe = aboutMe;
        this.customPhrase = customPhrase;
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

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getCustomPhrase() {
        return customPhrase;
    }
}
