package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentDetails {
    @SerializedName("city_name")
    @Expose
    private Boolean city_name;
    @SerializedName("location_name")
    @Expose
    private Boolean location_name;
    @SerializedName("building_name")
    @Expose
    private Boolean building_name;
    @SerializedName("apartment_no")
    @Expose
    private Boolean apartment_no;
    @SerializedName("rental_owner")
    @Expose
    private Boolean rental_owner;
    @SerializedName("token")
    @Expose
    private String token;

    public Boolean getCity_name() {
        return city_name;
    }

    public Boolean getLocation_name() {
        return location_name;
    }

    public Boolean getBuilding_name() {
        return building_name;
    }

    public Boolean getApartment_no() {
        return apartment_no;
    }

    public Boolean getRental_owner() {
        return rental_owner;
    }

    public String getToken() {
        return token;
    }
}
