package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;

    public Boolean getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public String getMobile() {
        return mobile;
    }

}

