package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetails {

    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
