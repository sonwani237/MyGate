package com.troology.mygate.dashboard.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.splash.ui.SplashActivity;
import com.troology.mygate.utils.ActivityActivityMessage;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.FragmentActivityMessage;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;
import com.troology.mygate.utils.ViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewpager;
    TabLayout tabs;
    String approval_status = "";
    RelativeLayout parent;
    Loader loader;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int REQUEST_PERMISSIONS = 1;
    RelativeLayout active, inactive;
    Button logout;
    Handler handler;
    TextView toolbar_flatno, toolbar_flataddress, flat_msg;
    private static long back_pressed;
    ApartmentDetails details;
    ImageView iv_cab, iv_delivery, iv_guest;
    public static String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getIntent() != null) {
            approval_status = getIntent().getStringExtra("approval_status");
        }

        details = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);

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
        iv_cab = findViewById(R.id.ivcar);
        flat_msg = findViewById(R.id.flat_msg);
        iv_delivery = findViewById(R.id.ivscooter);
        iv_guest = findViewById(R.id.ivguest);
        toolbar_flatno = toolbar.findViewById(R.id.toolbar_flatno);
        toolbar_flataddress = toolbar.findViewById(R.id.toolbarflataddress);
        setUpDashboard(tabs, viewpager);
        callPermission();

        if (approval_status.equalsIgnoreCase("1")) {
            active.setVisibility(View.VISIBLE);
            inactive.setVisibility(View.GONE);
        } else {
            active.setVisibility(View.GONE);
            inactive.setVisibility(View.VISIBLE);

            if (UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class).getRes_type().equalsIgnoreCase("1")){
                flat_msg.setText(getResources().getString(R.string.flat_approval));
            }else {
                flat_msg.setText(getResources().getString(R.string.office_approval));
            }

            handler = new Handler(Dashboard.this.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("uid", UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getUid());
                    jsonObject.addProperty("token", UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());

                    UtilsMethods.INSTANCE.ApartmentsDetail(Dashboard.this, jsonObject, parent, 1, null);

                    handler.postDelayed(this, 5000);
                }
            }, 1000);
        }
        logout.setOnClickListener(this);

        toolbar_flatno.setText(details.getFlat_no());
        toolbar_flataddress.setText(details.getApartment_name().toUpperCase());
        onClickimages();
    }

    public void callPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(Dashboard.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

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
        if (v == logout) {
            UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.userToken, "");
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        if (v == iv_cab) {
            type = "1";
            Intent intent = new Intent(Dashboard.this, PopupActivity.class);
            startActivity(intent);
        }
        if (v == iv_delivery) {
            type = "2";
            Intent intent = new Intent(Dashboard.this, PopupActivity.class);
            startActivity(intent);
        }
        if (v == iv_guest) {
            type = "3";
            Intent intent = new Intent(Dashboard.this, PopupActivity.class);
            startActivity(intent);
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
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void onActivityActivityMessage(final ActivityActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("approval_status")) {
            if (activityFragmentMessage.getFrom().equalsIgnoreCase("1")) {
                active.setVisibility(View.VISIBLE);
                inactive.setVisibility(View.GONE);
                handler.removeCallbacks(null);
                handler.removeCallbacksAndMessages(null);
            } else {
                active.setVisibility(View.GONE);
                inactive.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.exit_alert), parent);
        }
        back_pressed = System.currentTimeMillis();
    }

    public void onClickimages() {

        iv_cab.setOnClickListener(this);
//==================================================================================================
        iv_delivery.setOnClickListener(this);
//==================================================================================================
        iv_guest.setOnClickListener(this);
    }

}