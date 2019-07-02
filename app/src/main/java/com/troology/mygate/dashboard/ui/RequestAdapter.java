package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.UserMeetings;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<UserMeetings> userMeetings;
    Loader loader;
    View parent;

    public RequestAdapter(Context ctx, ArrayList<UserMeetings> meetings, View view) {
        this.context = ctx;
        this.userMeetings = meetings;
        this.parent = view;
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
        holder.name.setText(""+userMeetings.get(position).getName());
        holder.mobile.setText(""+userMeetings.get(position).getMobile());
        holder.time.setText(UtilsMethods.INSTANCE.ShortTime(userMeetings.get(position).getMeeting_time()));
        holder.date.setText(UtilsMethods.INSTANCE.ShortDate(userMeetings.get(position).getMeeting_time()));
        holder.remark.setText(""+userMeetings.get(position).getRemarks());
        if (userMeetings.get(position).getStatus().equalsIgnoreCase("1")){
            holder.passcode.setText("#"+userMeetings.get(position).getPasscode());//Hit sendRequest like method from this adapter when status  = 0
        }else {
            holder.passcode.setText("Click to Approve");
            holder.rellayparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userMeetings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile, time, date, remark, passcode;
        RelativeLayout rellayparent;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            remark = itemView.findViewById(R.id.remark);
            passcode = itemView.findViewById(R.id.passcode);
            rellayparent = itemView.findViewById(R.id.rellayparent);

            loader = new Loader(context, android.R.style.Theme_Translucent_NoTitleBar);

        }
    }

    public void sendRequest(int pos) {

        if (UtilsMethods.INSTANCE.isNetworkAvailable(context)) {
            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            JsonObject object = new JsonObject();
            object.addProperty("status", userMeetings.get(pos).getStatus());
            object.addProperty("request_id", userMeetings.get(pos).getName());
            object.addProperty("apartment_id", userMeetings.get(pos).getApartment_id());
            object.addProperty("flat_id", userMeetings.get(pos).getFlat_id());
            object.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
            object.addProperty("action", "1");

            UtilsMethods.INSTANCE.RequestAction(context, object, parent, loader);
        } else {
            UtilsMethods.INSTANCE.snackBar(context.getResources().getString(R.string.network_error), parent);
        }
    }

}
