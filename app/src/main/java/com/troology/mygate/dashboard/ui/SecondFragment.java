package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    public static ScrollView parent;
    RelativeLayout cv_userdetails;
    ImageView loc_services, family, user_img;
    String imagepath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        InItView(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void InItView(View view) {
        parent = view.findViewById(R.id.parent);
        name = view.findViewById(R.id.name);
        passcode = view.findViewById(R.id.passcode);
        family = view.findViewById(R.id.family);
        loc_services = view.findViewById(R.id.loc_services);
        addMember = view.findViewById(R.id.addMember);
        recycler = view.findViewById(R.id.recycler);
        service_recycler = view.findViewById(R.id.service_recycler);
        cv_userdetails = view.findViewById(R.id.cv_profile);
        user_img = view.findViewById(R.id.user_img);

        details = UtilsMethods.INSTANCE.get(Objects.requireNonNull(getActivity()), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);
        if (details != null) {
            name.setText(details.getUsername() + " (Me)");
            passcode.setText(details.getFlat_no() + ", " + details.getApartment_name() + ", " + details.getCity_name() + ", " + details.getState_name() + ", " + details.getCountry_name());
        }

        imagepath = UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.profilePic, String.class);

        Glide.with(getActivity()).load(ApplicationConstant.INSTANCE.baseUrl + "/" + imagepath)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .error(getResources().getDrawable(R.drawable.man))
                .into(user_img);

        addMember.setOnClickListener(this);
        cv_userdetails.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getMemberData();

        if (user_img!=null){
            imagepath = UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.profilePic, String.class);

            Glide.with(getActivity()).load(ApplicationConstant.INSTANCE.baseUrl + "/" + imagepath)
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .error(getResources().getDrawable(R.drawable.man))
                    .into(user_img);
        }
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
            UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
        }
    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage fragmentActivityMessage) {
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("MemberList")) {
            memberData = new Gson().fromJson(fragmentActivityMessage.getFrom(), new TypeToken<List<MemberData>>() {
            }.getType());
            if (memberData != null && memberData.size() > 0) {
                family.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
                layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recycler.setLayoutManager(layoutManager);
                adapter = new MemberListAdapter(memberData, getActivity());
                recycler.setAdapter(adapter);
            } else {
                family.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            }
        }
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("ServiceList")) {
            serviceRequestDetails = new Gson().fromJson(fragmentActivityMessage.getFrom(), new TypeToken<List<ServiceRequestDetails>>() {
            }.getType());
            if (serviceRequestDetails != null && serviceRequestDetails.size() > 0) {
                loc_services.setVisibility(View.GONE);
                service_recycler.setVisibility(View.VISIBLE);
                layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                service_recycler.setLayoutManager(layoutManager);
                serviceListAdapter = new ServiceListAdapter(serviceRequestDetails, getActivity());
                service_recycler.setAdapter(serviceListAdapter);
            } else {
                loc_services.setVisibility(View.VISIBLE);
                service_recycler.setVisibility(View.GONE);
            }
        }
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("ServiceListUpdate")) {
            getMemberData();
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
