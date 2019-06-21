package com.troology.mygate.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MeetingData {

    @SerializedName("user_meetings")
    @Expose
    private ArrayList<UserMeetings> user_meetings;

    public ArrayList<UserMeetings> getUser_meetings() {
        return user_meetings;
    }
}
