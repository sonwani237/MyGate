package com.troology.mygate.dashboard.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;
import com.troology.mygate.utils.ViewPagerAdapter;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {

    ViewPager viewpager;
    TabLayout tabs;
    String approval_status = "";
    RelativeLayout parent;
    Loader loader;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    private static final int REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getIntent()!=null){
            approval_status = getIntent().getStringExtra("approval_status");
            Log.e("approval_status"," >>> "+approval_status);
        }

        Window window = this.getWindow();
        Drawable background = this.getResources().getDrawable(R.drawable.gradient_colour);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        viewpager = findViewById(R.id.viewpager);
        tabs = findViewById(R.id.tabs);
        parent = findViewById(R.id.parent);
        setUpDashboard(tabs, viewpager);
        callPermission();
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



}
