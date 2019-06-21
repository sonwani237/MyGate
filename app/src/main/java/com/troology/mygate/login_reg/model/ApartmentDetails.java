package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentDetails {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("flat_id")
    @Expose
    private String flat_id;
    @SerializedName("passcode")
    @Expose
    private String passcode;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("flat_no")
    @Expose
    private String flat_no;
    @SerializedName("apartment_name")
    @Expose
    private String apartment_name;
    @SerializedName("apartment_id")
    @Expose
    private String apartment_id;
    @SerializedName(value = "city_id",alternate ="city_name")
    @Expose
    private String city_name;
    @SerializedName(value = "state_id",alternate ="state_name")
    @Expose
    private String state_name;
    @SerializedName(value = "country_id",alternate ="country_name")
    @Expose
    private String country_name;
    @SerializedName("approval_status")
    @Expose
    private String approval_status;

    public String getId() {
        return id;
    }

    public String getPasscode() {
        return passcode;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public String getApartment_name() {
        return apartment_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getState_name() {
        return state_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public String getApartment_id() {
        return apartment_id;
    }

}
