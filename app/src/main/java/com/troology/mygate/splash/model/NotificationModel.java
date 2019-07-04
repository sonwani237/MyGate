package com.troology.mygate.splash.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("member_type")
    @Expose
    private String memberType;
    @SerializedName("time_to")
    @Expose
    private String timeTo;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("apartment_id")
    @Expose
    private String apartmentId;
    @SerializedName("record_id")
    @Expose
    private Integer recordId;
    @SerializedName("request_by")
    @Expose
    private String requestBy;
    @SerializedName("flat_id")
    @Expose
    private String flatId;
    @SerializedName("time_from")
    @Expose
    private String timeFrom;
    @SerializedName("activity_type")
    @Expose
    private String activityType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("stay_days")
    @Expose
    private String stayDays;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("passcode")
    @Expose
    private Integer passcode;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMemberType() {
        return memberType;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getMobile() {
        return mobile;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public String getFlatId() {
        return flatId;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getName() {
        return name;
    }

    public String getStayDays() {
        return stayDays;
    }

    public String getTime() {
        return time;
    }

    public Integer getPasscode() {
        return passcode;
    }

    public String getRemarks() {
        return remarks;
    }

    public Integer getStatus() {
        return status;
    }
}
