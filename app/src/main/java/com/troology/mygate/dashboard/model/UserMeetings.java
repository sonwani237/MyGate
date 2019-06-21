package com.troology.mygate.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMeetings {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("request_id")
    @Expose
    private String request_id;
    @SerializedName("flat_id")
    @Expose
    private String flat_id;
    @SerializedName("apartment_id")
    @Expose
    private String apartment_id;
    @SerializedName("request_by")
    @Expose
    private String request_by;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("passcode")
    @Expose
    private String passcode;
    @SerializedName("meeting_time")
    @Expose
    private String meeting_time;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isFamily")
    @Expose
    private String isFamily;

    public String getId() {
        return id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public String getApartment_id() {
        return apartment_id;
    }

    public String getRequest_by() {
        return request_by;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPasscode() {
        return passcode;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatus() {
        return status;
    }

    public String getIsFamily() {
        return isFamily;
    }
}
