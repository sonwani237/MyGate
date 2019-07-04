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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.splash.model.NotificationModel;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<NotificationModel> userMeetings;
    Loader loader;
    View parent;

    public RequestAdapter(Context ctx, ArrayList<NotificationModel> meetings, View view) {
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
    public void onBindViewHolder(@NonNull RequestAdapter.MyViewHolder holder,  int position) {

        if (!userMeetings.get(position).getMemberType().equalsIgnoreCase("Guest") ){
            holder.name.setText(""+userMeetings.get(position).getMemberType());
            holder.s_time.setText(UtilsMethods.INSTANCE.ShortTime(userMeetings.get(position).getTimeFrom())+" - "+UtilsMethods.INSTANCE.ShortTime(userMeetings.get(position).getTimeTo()));
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.ic_access_time))
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.s_img);
            if (!userMeetings.get(position).getRemarks().equalsIgnoreCase("")){
                holder.remark.setText(""+userMeetings.get(position).getRemarks());
            }else {
                holder.remark.setText("N/A");
            }

        }else {
            holder.name.setText(""+userMeetings.get(position).getName());
            holder.remark.setText(""+userMeetings.get(position).getMobile());
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.ic_person_outline))
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.s_img);

            if (!userMeetings.get(position).getRequestBy().equalsIgnoreCase("2")){
                holder.s_time.setText("Invited by you.");
            }else {
                holder.s_time.setText("Requested by gatekeeper.");
            }
        }

        holder.e_time.setText("Valid till "+UtilsMethods.INSTANCE.ShortDate(userMeetings.get(position).getTimeTo()));

        if (userMeetings.get(position).getMemberType().equalsIgnoreCase("Cab")){
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.car))
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.img);
        }else if (userMeetings.get(position).getMemberType().equalsIgnoreCase("Delivery")){
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.motorbike))
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.img);
        }else {
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.guestt))
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.img);
        }

        holder.time.setText(userMeetings.get(position).getTime());

        if (userMeetings.get(position).getStatus()==1){
            holder.passcode.setText("#"+userMeetings.get(position).getPasscode());//Hit sendRequest like method from this adapter when status  = 0
        }else {
            holder.passcode.setText("Click to Approve");
           /* holder.rellayparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest(position);
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return userMeetings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile, time, date, remark, passcode, s_time, e_time;
        RelativeLayout rellayparent;
        ImageView img, s_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            remark = itemView.findViewById(R.id.remark);
            passcode = itemView.findViewById(R.id.passcode);
            rellayparent = itemView.findViewById(R.id.rellayparent);

            s_time = itemView.findViewById(R.id.s_time);
            e_time = itemView.findViewById(R.id.e_time);

            img = itemView.findViewById(R.id.img);
            s_img = itemView.findViewById(R.id.s_img);

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
//            object.addProperty("apartment_id", userMeetings.get(pos).getApartment_id());
//            object.addProperty("flat_id", userMeetings.get(pos).getFlat_id());
            object.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
            object.addProperty("action", "1");

            UtilsMethods.INSTANCE.RequestAction(context, object, parent, loader);
        } else {
            UtilsMethods.INSTANCE.snackBar(context.getResources().getString(R.string.network_error), parent);
        }
    }

}
