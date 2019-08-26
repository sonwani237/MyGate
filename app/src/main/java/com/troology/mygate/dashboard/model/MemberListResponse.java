package com.troology.mygate.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MemberListResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("member_data")
    @Expose
    private ArrayList<MemberData> memberData;
    @SerializedName("ServiceRequestDetails")
    @Expose
    private ArrayList<ServiceRequestDetails> ServiceRequestDetails;

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }

    public ArrayList<MemberData> getMemberData() {
        return memberData;
    }

    public ArrayList<ServiceRequestDetails> getServiceRequestDetails() {
        return ServiceRequestDetails;
    }
}
