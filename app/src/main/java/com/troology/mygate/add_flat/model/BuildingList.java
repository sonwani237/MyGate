package com.troology.mygate.add_flat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuildingList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("apartment_id")
    @Expose
    private String apartment_id;
    @SerializedName("apartment_no")
    @Expose
    private String apartment_no;

    public String getId() {
        return id;
    }

    public String getApartment_id() {
        return apartment_id;
    }

    public String getApartment_no() {
        return apartment_no;
    }
}
