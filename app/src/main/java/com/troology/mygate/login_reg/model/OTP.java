package com.troology.mygate.login_reg.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTP {
    @SerializedName("phone_otp")
    @Expose
    private String phone_otp;

    public String getPhone_otp() {
        return phone_otp;
    }
}
