package com.troology.mygate.add_flat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuildingList {

    @SerializedName("building_id")
    @Expose
    private String building_id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getBuilding_id() {
        return building_id;
    }

    public String getName() {
        return name;
    }

}
