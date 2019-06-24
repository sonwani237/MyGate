package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.ServiceRequestDetails;

import java.util.ArrayList;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

    private ArrayList<ServiceRequestDetails> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile, in_time, out_time, service;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            mobile = view.findViewById(R.id.mobile);
            in_time = view.findViewById(R.id.in_time);
            out_time = view.findViewById(R.id.out_time);
            service = view.findViewById(R.id.service);
        }
    }

    public ServiceListAdapter(ArrayList<ServiceRequestDetails> transactionsList, Context context) {
        this.transactionsList = transactionsList;
        this.mContext = context;
    }

    @Override
    public ServiceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_service_list, parent, false);

        return new ServiceListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ServiceListAdapter.MyViewHolder holder, int position) {
        final ServiceRequestDetails memberData = transactionsList.get(position);
        holder.name.setText(memberData.getName());
        holder.mobile.setText(memberData.getMobile());
        holder.in_time.setText(memberData.getTimeSlotIn());
        holder.out_time.setText(memberData.getTimeSlotOut());
        holder.service.setText(memberData.getType());

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}