package com.whosup.listview;

public class Invite {

    private int drawableId;
    private String from;
    private String place;
    private String distance;

    public Invite(int drawableId, String from, String place, String distance) {
        super();
        this.drawableId = drawableId;
        this.from = from;
        this.place = place;
        this.distance = distance;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
