package com.troology.mygate.add_flat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlatList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("flat_id")
    @Expose
    private String flat_id;
    @SerializedName("flat_no")
    @Expose
    private String flat_no;

    public String getId() {
        return id;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public String getFlat_no() {
        return flat_no;
    }
}
