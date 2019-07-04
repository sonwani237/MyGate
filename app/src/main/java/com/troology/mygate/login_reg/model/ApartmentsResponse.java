package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.troology.mygate.dashboard.model.ServicemenData;

import java.util.ArrayList;

public class ApartmentsResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user_details")
    @Expose
    private UserDetails user_details;
    @SerializedName("flat_detail")
    @Expose
    private ArrayList<ApartmentDetails> apartment_details;
    @SerializedName("ServicemenData")
    @Expose
    private ArrayList<ServicemenData> ServicemenData;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public UserDetails getUser_details() {
        return user_details;
    }

    public ArrayList<ApartmentDetails> getApartment_details() {
        return apartment_details;
    }

    public ArrayList<ServicemenData> getServicemenData() {
        return ServicemenData;
    }
}
