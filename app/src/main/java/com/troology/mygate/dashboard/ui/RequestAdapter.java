package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

import org.json.JSONObject;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<NotificationModel> userMeetings;
    Loader loader;
    View parent;
    public static boolean edit;

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
    public void onBindViewHolder(@NonNull final RequestAdapter.MyViewHolder holder, final int position) {

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

        holder.passcode.setText("#"+userMeetings.get(position).getPasscode());//Hit sendRequest like method from this adapter when status  = 0

        holder.rellayparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,QrCodeActivity.class);
                intent.putExtra("name", holder.name.getText().toString());
                intent.putExtra("passcode", holder.passcode.getText().toString());
                context.startActivity(intent);
            }
        });

        holder.rellayparent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Action");
                alert.setMessage("Edit or Delete you activity");
                alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        JsonObject object = new JsonObject();
                        object.addProperty("token",UtilsMethods.INSTANCE.get(context, ApplicationConstant.
                                INSTANCE.loginPerf, UserDetails.class).getToken());
                        object.addProperty("record_id",userMeetings.get(position).getRecordId());
                        object.addProperty("apartment_id",userMeetings.get(position).getApartmentId());
                        object.addProperty("flat_id",userMeetings.get(position).getFlatId());
                        object.addProperty("member_type",userMeetings.get(position).getMemberType());
                        object.addProperty("name",userMeetings.get(position).getName());
                        object.addProperty("email","");
                        object.addProperty("mobile",userMeetings.get(position).getMobile());
                        object.addProperty("time_from",userMeetings.get(position).getTimeFrom());
                        object.addProperty("time_to",userMeetings.get(position).getTimeTo());
                        object.addProperty("activity_type",userMeetings.get(position).getActivityType());
                        object.addProperty("stay_days",userMeetings.get(position).getStayDays());
                        object.addProperty("remarks",userMeetings.get(position).getRemarks());
                        object.addProperty("request_by",userMeetings.get(position).getRequestBy());
                        object.addProperty("contact_per_uid","");

                        edit = true;

                        Intent intent = new Intent(context,PopupActivity.class);
                        intent.putExtra("apartmentId",userMeetings.get(position).getApartmentId());
                        intent.putExtra("memberType",userMeetings.get(position).getMemberType());
                        intent.putExtra("record_id",userMeetings.get(position).getRecordId());
                        context.startActivity(intent);

                    }
                });
                alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("token",UtilsMethods.INSTANCE.get(context, ApplicationConstant.
                                INSTANCE.loginPerf, UserDetails.class).getToken());
                        jsonObject.addProperty("record_id",""+userMeetings.get(position).getRecordId());

                       UtilsMethods.INSTANCE.DeleteActivity(context,jsonObject,holder.rellayparent,holder.loader);
                       notifyItemRemoved(position);

                    }
                });

                alert.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return userMeetings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile, time, date, remark, passcode, s_time, e_time;
        RelativeLayout rellayparent;
        ImageView img, s_img;
        CardView cardView;
        Loader loader;

        public MyViewHolder(View itemView) {
            super(itemView);
            loader = new Loader(context, android.R.style.Theme_Translucent_NoTitleBar);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            remark = itemView.findViewById(R.id.remark);
            passcode = itemView.findViewById(R.id.passcode);
            rellayparent = itemView.findViewById(R.id.rellayparent);
            cardView = itemView.findViewById(R.id.cv_request);

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
