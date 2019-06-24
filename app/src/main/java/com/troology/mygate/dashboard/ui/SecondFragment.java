package com.troology.mygate.dashboard.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.MemberData;
import com.troology.mygate.dashboard.model.ServiceRequestDetails;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.FragmentActivityMessage;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SecondFragment extends Fragment implements View.OnClickListener {

    TextView name, passcode, addMember;
    ApartmentDetails details;
    ArrayList<MemberData> memberData;
    ArrayList<ServiceRequestDetails> serviceRequestDetails;
    RecyclerView recycler, service_recycler;
    LinearLayoutManager layoutManager;
    MemberListAdapter adapter;
    ServiceListAdapter serviceListAdapter;
    ScrollView parent;
    CardView cv_userdetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        InItView(view);
        return view;
    }

    private void InItView(View view) {
        parent = view.findViewById(R.id.parent);
        name = view.findViewById(R.id.name);
        passcode = view.findViewById(R.id.passcode);
        addMember = view.findViewById(R.id.addMember);
        recycler = view.findViewById(R.id.recycler);
        service_recycler = view.findViewById(R.id.service_recycler);
        cv_userdetails = view.findViewById(R.id.cv_profile);

        details = UtilsMethods.INSTANCE.get(Objects.requireNonNull(getActivity()), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);
        if (details != null) {
            name.setText(details.getUsername());
            passcode.setText("#"+UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.userPassPerf, String.class));
        }

        addMember.setOnClickListener(this);
        cv_userdetails.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getMemberData();
    }

    private void getMemberData() {

        if (UtilsMethods.INSTANCE.isNetworkAvailable(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("flat_id", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getFlat_id());
            jsonObject.addProperty("apartment_id", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getApartment_id());
            jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

            UtilsMethods.INSTANCE.viewMember(getActivity(), jsonObject, parent, null);
            UtilsMethods.INSTANCE.viewServiceMember(getActivity(), jsonObject, parent, null);
        } else {
            UtilsMethods.INSTANCE.snackBar("", parent);
        }
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage fragmentActivityMessage) {
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("MemberList")) {
            memberData = new Gson().fromJson(fragmentActivityMessage.getFrom(), new TypeToken<List<MemberData>>() {
            }.getType());
            if (memberData.size() > 0) {
                layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recycler.setLayoutManager(layoutManager);
                adapter = new MemberListAdapter(memberData, getActivity());
                recycler.setAdapter(adapter);
            }
        }
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("ServiceList")) {
            serviceRequestDetails = new Gson().fromJson(fragmentActivityMessage.getFrom(), new TypeToken<List<ServiceRequestDetails>>() {
            }.getType());
            if (serviceRequestDetails.size() > 0) {
                layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                service_recycler.setLayoutManager(layoutManager);
                serviceListAdapter = new ServiceListAdapter(serviceRequestDetails, getActivity());
                service_recycler.setAdapter(serviceListAdapter);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == addMember) {
            startActivity(new Intent(getActivity(), AddMember.class));
        }
        if (v == cv_userdetails) {
            startActivity(new Intent(getActivity(), UserProfile.class));
        }
    }
}
