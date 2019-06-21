package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.UserMeetings;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<UserMeetings> userMeetings;

    public RequestAdapter(Context ctx, ArrayList<UserMeetings> meetings) {
        this.context = ctx;
        this.userMeetings = meetings;
    }

    @NonNull
    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_meeting, parent, false);
        return new RequestAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(userMeetings.get(position).getName());
        holder.mobile.setText(userMeetings.get(position).getMobile());
        holder.time.setText(userMeetings.get(position).getMeeting_time());
        holder.remark.setText(userMeetings.get(position).getRemarks());
        if (!userMeetings.get(position).getStatus().equalsIgnoreCase("1")){
            holder.passcode.setText("#"+userMeetings.get(position).getPasscode());
        }else {
            holder.passcode.setText("Not Approved");
        }
    }

    @Override
    public int getItemCount() {
        return userMeetings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile, time, remark, passcode;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            time = itemView.findViewById(R.id.time);
            remark = itemView.findViewById(R.id.remark);
            passcode = itemView.findViewById(R.id.passcode);
        }
    }

}
