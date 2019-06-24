package com.troology.mygate.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceRequestDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("flat_id")
    @Expose
    private String flatId;
    @SerializedName("apartment_id")
    @Expose
    private String apartmentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("time_slot_in")
    @Expose
    private String timeSlotIn;
    @SerializedName("time_slot_out")
    @Expose
    private String timeSlotOut;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getFlatId() {
        return flatId;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTimeSlotIn() {
        return timeSlotIn;
    }

    public String getTimeSlotOut() {
        return timeSlotOut;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getStatus() {
        return status;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getType() {
        return type;
    }
}
