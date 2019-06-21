package com.troology.mygate.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.troology.mygate.login_reg.model.ApartmentDetails;

import java.util.ArrayList;

public class ResidentsResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("ResientsData")
    @Expose
    private ArrayList<ApartmentDetails> ResidentsData;
    @SerializedName("MeetingData")
    @Expose
    private MeetingData MeetingData;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<ApartmentDetails> getResidentsData() {
        return ResidentsData;
    }

    public MeetingData getMeetingData() {
        return MeetingData;
    }
}
