package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("otp")
    @Expose
    private OTP otp;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("User_data")
    @Expose
    private ArrayList<UserDetails> userDetails;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public OTP getOtp() {
        return otp;
    }

    public ArrayList<UserDetails> getUserDetails() {
        return userDetails;
    }

    public String getMobile() {
        return mobile;
    }

}

