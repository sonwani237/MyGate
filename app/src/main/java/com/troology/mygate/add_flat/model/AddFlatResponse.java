package com.troology.mygate.add_flat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddFlatResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("cities")
    @Expose
    private ArrayList<CityList> cities;
    @SerializedName("locations")
    @Expose
    private ArrayList<LocationList> locations;
    @SerializedName("buildings")
    @Expose
    private ArrayList<BuildingList> buildings;
    @SerializedName("apartment_no")
    @Expose
    private ArrayList<BuildingList> apartment_no;

    public Boolean getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<CityList> getCities() {
        return cities;
    }

    public ArrayList<LocationList> getLocations() {
        return locations;
    }

    public ArrayList<BuildingList> getBuildings() {
        return buildings;
    }

    public ArrayList<BuildingList> getApartment_no() {
        return apartment_no;
    }
}
