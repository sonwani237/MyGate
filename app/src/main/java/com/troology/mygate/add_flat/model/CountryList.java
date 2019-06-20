package com.troology.mygate.add_flat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryList {
    @SerializedName("country_id")
    @Expose
    private String country_id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getCountry_id() {
        return country_id;
    }

    public String getName() {
        return name;
    }
}
