package com.troology.mygate.dashboard.ui;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ActivityActivityMessage;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ResidentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Loader loader;
    RelativeLayout parent;
    ArrayList<ApartmentDetails> residents;
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    ResidentAdapter adapter;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.residents);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        parent = findViewById(R.id.parent);
        recycler = findViewById(R.id.recycler);
        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(R.color.blue, R.color.green, R.color.orange, R.color.red);
        getData();

    }

    public void getData() {
        loader.show();
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uid", UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getUid());
        jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

        UtilsMethods.INSTANCE.getResidents(getApplicationContext(), jsonObject, parent, loader);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void onActivityActivityMessage(ActivityActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("ResidentsList")) {
            residents = new Gson().fromJson(activityFragmentMessage.getFrom(), new TypeToken<List<ApartmentDetails>>() {
            }.getType());
            swipe.setRefreshing(false);
            if (residents != null && residents.size() > 0) {
                layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recycler.setLayoutManager(layoutManager);
                adapter = new ResidentAdapter(this, residents);
                recycler.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ApartmentDetails> temp = new ArrayList();
                for (ApartmentDetails d : residents) {
                    //or use .equal(text) with you want equal match
                    //use .toLowerCase() for better matches
                    if (d.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                        temp.add(d);
                    }
                }
                adapter.updateList(temp);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
