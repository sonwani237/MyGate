package com.troology.mygate.dashboard.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.utils.UtilsMethods;

public class ThirdFragment extends Fragment implements View.OnClickListener {

    CardView residents, local;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        residents = view.findViewById(R.id.residents);
        local = view.findViewById(R.id.local);

        residents.setOnClickListener(this);
        local.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.residents:
                startActivity(new Intent(getActivity(), ResidentActivity.class));
                break;
            case R.id.local:
                startActivity(new Intent(getActivity(), LocalServices.class));
                break;
        }
    }
}
