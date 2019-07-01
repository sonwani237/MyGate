package com.troology.mygate.dashboard.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.splash.ui.SplashActivity;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;
import com.troology.mygate.utils.ViewPagerAdapter;

import java.util.Objects;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewpager;
    TabLayout tabs;
    String approval_status = "";
    RelativeLayout parent;
    Loader loader;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    private static final int REQUEST_PERMISSIONS = 1;
    RelativeLayout active, inactive;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getIntent()!=null){
            approval_status = getIntent().getStringExtra("approval_status");
        }

        Window window = this.getWindow();
        Drawable background = this.getResources().getDrawable(R.drawable.gradient_colour);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        viewpager = findViewById(R.id.viewpager);
        tabs = findViewById(R.id.tabs);
        parent = findViewById(R.id.parent);
        active = findViewById(R.id.active);
        inactive = findViewById(R.id.inactive);
        logout = findViewById(R.id.logout);
        setUpDashboard(tabs, viewpager);
        callPermission();

        if (approval_status.equalsIgnoreCase("1")){
            active.setVisibility(View.VISIBLE);
            inactive.setVisibility(View.GONE);
        }else {
            active.setVisibility(View.GONE);
            inactive.setVisibility(View.VISIBLE);
        }

        logout.setOnClickListener(this);
    }

    public void callPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

            ActivityCompat.requestPermissions(Dashboard.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            }
        }
    }

    private void setUpDashboard(TabLayout tabs, ViewPager viewpager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new FirstFragment(), getResources().getString(R.string.activity));
        viewPagerAdapter.addFragments(new SecondFragment(), getResources().getString(R.string.household));
        viewPagerAdapter.addFragments(new ThirdFragment(), getResources().getString(R.string.community));
        viewpager.setAdapter(viewPagerAdapter);
        tabs.setupWithViewPager(viewpager);
    }

    @Override
    public void onClick(View v) {
        if (v == logout){
            UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.userToken, "");
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
    }
}