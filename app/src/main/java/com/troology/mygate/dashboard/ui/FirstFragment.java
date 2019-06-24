package com.troology.mygate.dashboard.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.MemberData;
import com.troology.mygate.dashboard.model.UserMeetings;
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

public class FirstFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    FloatingActionButton fab;
    Loader loader;
    RelativeLayout parent;
    ArrayList<UserMeetings> userMeetings;
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    RequestAdapter adapter;
    SwipeRefreshLayout swipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        inItView(view);

        return view;
    }

    private void inItView(View view) {
        fab = view.findViewById(R.id.fab);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        parent = view.findViewById(R.id.parent);
        recycler = view.findViewById(R.id.recycler);
        swipe = view.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(R.color.blue,
                R.color.green,
                R.color.orange,
                R.color.red);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == fab){
            startActivity(new Intent(getActivity(), CreateRequest.class));
        }
    }

    public void getData(){
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("flat_id", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getFlat_id());
        jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(getActivity(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

        UtilsMethods.INSTANCE.getRequest(getActivity(), jsonObject, parent, loader);

    }

    @Subscribe
    public void onFragmentActivityMessage(FragmentActivityMessage fragmentActivityMessage) {
        if (fragmentActivityMessage.getMessage().equalsIgnoreCase("RequestList")){
            swipe.setRefreshing(false);
            userMeetings = new Gson().fromJson(fragmentActivityMessage.getFrom(), new TypeToken<List<UserMeetings>>(){}.getType());
            if (userMeetings.size()>0){
                layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recycler.setLayoutManager(layoutManager);
                adapter = new RequestAdapter(getActivity(), userMeetings);
                recycler.setAdapter(adapter);
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
    public void onRefresh() {
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
