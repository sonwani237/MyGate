package com.troology.mygate.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("mobile")
    @Expose
    private String mMobile;
    @SerializedName("passcode")
    @Expose
    private String passcode;
    @SerializedName("image")
    @Expose
    private String image;

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getImage() { return image; }

    public String getmName() {
        return mName;
    }

    public String getmMobile() {
        return mMobile;
    }

    public String getPasscode() {
        return passcode;
    }


}
