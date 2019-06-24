package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApartmentsResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_details")
    @Expose
    private UserDetails user_details;
    @SerializedName("flat_detail")
    @Expose
    private ArrayList<ApartmentDetails> apartment_details;

    public String getStatus() {
        return status;
    }

    public UserDetails getUser_details() {
        return user_details;
    }

    public ArrayList<ApartmentDetails> getApartment_details() {
        return apartment_details;
    }
}
