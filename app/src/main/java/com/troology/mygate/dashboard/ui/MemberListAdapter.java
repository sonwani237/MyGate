package com.troology.mygate.dashboard.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.MemberData;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder> {

    private ArrayList<MemberData> transactionsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView m_name, m_passcode;
        ImageView call,iv_share;
        RelativeLayout cv_memberList;

        public MyViewHolder(View view) {
            super(view);
            m_name = view.findViewById(R.id.m_name);
            m_passcode = view.findViewById(R.id.m_passcode);
            call = view.findViewById(R.id.call);
            cv_memberList = view.findViewById(R.id.cvmemberList);
            iv_share = view.findViewById(R.id.iv_share);
        }
    }

    public MemberListAdapter(ArrayList<MemberData> transactionsList, Context context) {
        this.transactionsList = transactionsList;
        this.mContext = context;
    }

    @Override
    public MemberListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_list, parent, false);

        return new MemberListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MemberListAdapter.MyViewHolder holder, int position) {
        final MemberData memberData = transactionsList.get(position);
        holder.m_name.setText("" + memberData.getmName());
        holder.m_passcode.setText("#" + memberData.getPasscode());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + memberData.getmMobile())));
            }
        });

        holder.cv_memberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,QrCodeActivity.class);
                intent.putExtra("name",memberData.getmName());
                intent.putExtra("passcode",memberData.getPasscode());
                mContext.startActivity(intent);
            }
        });

        holder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, memberData.getmName()+", Please use "+memberData.getPasscode()+" pass code for Entry/Exit");
                shareIntent.setType("text/plain");
                mContext.startActivity(shareIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

}