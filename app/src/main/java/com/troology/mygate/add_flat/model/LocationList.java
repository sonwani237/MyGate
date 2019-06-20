package com.troology.mygate.add_flat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationList {

    @SerializedName("state_id")
    @Expose
    private String location_id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getLocation_id() {
        return location_id;
    }

    public String getName() {
        return name;
    }

}
