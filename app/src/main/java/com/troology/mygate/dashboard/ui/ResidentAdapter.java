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
import com.troology.mygate.login_reg.model.ApartmentDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.MyViewHolder> {

    Context context;
    ArrayList<ApartmentDetails> apartmentDetails;

    public ResidentAdapter(Context ctx, ArrayList<ApartmentDetails> apartment) {
        this.context = ctx;
        this.apartmentDetails = apartment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.resident_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(apartmentDetails.get(position).getUsername());
        holder.flat.setText(apartmentDetails.get(position).getFlat_no()+", "+apartmentDetails.get(position).getApartment_name()+
                ", "+apartmentDetails.get(position).getCity_name()+", "+apartmentDetails.get(position).getState_name()+", "
                +apartmentDetails.get(position).getCountry_name());
    }

    @Override
    public int getItemCount() {
        return apartmentDetails.size();
    }

    public void updateList(ArrayList<ApartmentDetails> list){
        apartmentDetails = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, flat;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            flat = itemView.findViewById(R.id.flat);
        }
    }

}
