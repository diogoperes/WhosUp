package com.whosup.drawer.placepicker;

public class PlaceInfo {
    public String placeId;
    public String name;
    public String description;
    public boolean isLink;

    PlaceInfo(String name) {
        this.isLink = true;
        this.name = name;
        this.placeId = "";
        this.description = "";
    }

    public PlaceInfo(String placeId, String name, String description) {
        this.isLink = false;
        this.placeId = placeId;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return this.name + ", " + this.description;
    }

    public String toString() {
        return placeId + ": <" + name + ">";
    }
}
