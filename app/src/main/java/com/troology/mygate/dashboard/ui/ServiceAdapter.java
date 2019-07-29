package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.ServicemenData;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<ServicemenData> servicemenData;
    private TextView d_name, d_number, in_time, out_time;
    private Button submit;
    private String unique_id;
    private String start_time = "", end_time = "", formattedDate = "";

    public ServiceAdapter(Context ctx, ArrayList<ServicemenData> apartment) {
        this.context = ctx;
        this.servicemenData = apartment;
    }

    @NonNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list, parent, false);
        return new ServiceAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(servicemenData.get(position).getName());
        holder.number.setText(servicemenData.get(position).getMobile());
        holder.service.setText(servicemenData.get(position).getService_type());
        holder.service_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalService(servicemenData.get(position).getName(), servicemenData.get(position).getMobile(), servicemenData.get(position).getUnique_id());
            }
        });
    }

    View dialogView;
    private void LocalService(String name, String mobile, String id) {
        dialogView = View.inflate(context, R.layout.create_service, null);

        Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(calendar.getTime());

        unique_id = id;

        d_name = dialog.findViewById(R.id.name);
        d_number = dialog.findViewById(R.id.number);
        in_time = dialog.findViewById(R.id.in_time);
        out_time = dialog.findViewById(R.id.out_time);
        submit = dialog.findViewById(R.id.submit);

        d_name.setText(name);
        d_number.setText(mobile);
        submit.setOnClickListener(this);
        in_time.setOnClickListener(this);
        out_time.setOnClickListener(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return servicemenData.size();
    }

    public void updateList(ArrayList<ServicemenData> list) {
        servicemenData = list;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.in_time:
                ShowTime(1);
                break;
            case R.id.out_time:
                ShowTime(2);
                break;
            case R.id.submit:

                ApartmentDetails details = UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);
                if (UtilsMethods.INSTANCE.isNetworkAvailable(context)) {

                    Loader loader = new Loader(context, android.R.style.Theme_Translucent_NoTitleBar);
                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    JsonObject object = new JsonObject();
                    object.addProperty("flat_id", details.getFlat_id());
                    object.addProperty("apartment_id", details.getApartment_id());
                    object.addProperty("unique_id", unique_id);
                    object.addProperty("token", UtilsMethods.INSTANCE.get(context, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
                    object.addProperty("name", d_name.getText().toString());
                    object.addProperty("mobile", d_number.getText().toString());
                    object.addProperty("time_slot_in", in_time.getText().toString());
                    object.addProperty("time_slot_out", out_time.getText().toString());
                    object.addProperty("status", "1");

                    UtilsMethods.INSTANCE.AddServices(context, object, dialogView, loader);
                } else {
                    UtilsMethods.INSTANCE.snackBar(context.getResources().getString(R.string.network_error), dialogView);
                }

                break;
        }
    }

    private void ShowTime(final int i) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (i==1){
                            start_time = hourOfDay + ":" + minute + ":00";
                            in_time.setText(UtilsMethods.INSTANCE.ShortTime(formattedDate + " " + start_time).toUpperCase());
                        }else {
                            end_time = hourOfDay + ":" + minute + ":00";
                            if(checkTimings(start_time, end_time)){
                                out_time.setText(UtilsMethods.INSTANCE.ShortTime(formattedDate + " " + end_time).toUpperCase());
                            }else {
                                out_time.setText("Select");
                                UtilsMethods.INSTANCE.snackBar(context.getResources().getString(R.string.time_error), dialogView);
                            }
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private boolean checkTimings(String time, String endtime) {

        String pattern = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            return date1.before(date2);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, service;
        LinearLayout service_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            service = itemView.findViewById(R.id.service);
            service_lay = itemView.findViewById(R.id.service_lay);
        }
    }

}
