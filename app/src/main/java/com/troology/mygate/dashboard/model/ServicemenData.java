package com.troology.mygate.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicemenData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_id")
    @Expose
    private String service_id;
    @SerializedName("apartment_id")
    @Expose
    private String apartment_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("unique_id")
    @Expose
    private String unique_id;
    @SerializedName("service_type")
    @Expose
    private String service_type;

    public String getId() {
        return id;
    }

    public String getService_id() {
        return service_id;
    }

    public String getApartment_id() {
        return apartment_id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public String getService_type() {
        return service_type;
    }
}
