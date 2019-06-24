package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.ServicemenData;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    Context context;
    ArrayList<ServicemenData> servicemenData;

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
    }

    @Override
    public int getItemCount() {
        return servicemenData.size();
    }

    public void updateList(ArrayList<ServicemenData> list){
        servicemenData = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, service;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            service = itemView.findViewById(R.id.service);
        }
    }

}
