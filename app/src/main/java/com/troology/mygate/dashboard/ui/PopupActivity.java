package com.troology.mygate.dashboard.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.fragment.FragmentFrequent;
import com.troology.mygate.dashboard.fragment.FragmentOnce;
import com.troology.mygate.splash.model.NotificationModel;
import com.troology.mygate.utils.ViewPagerAdapter;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity implements View.OnClickListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView iv_close, ser_type;
    public static Integer record_id;
    public static String membertype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));

        iv_close = findViewById(R.id.ivclose);
        ser_type = findViewById(R.id.ser_type);

        viewPager = findViewById(R.id.viewpager_popup);
        tabLayout = findViewById(R.id.tablayout_popup);

        if (RequestAdapter.edit) {
            record_id = (Integer) getIntent().getExtras().get("record_id");
            membertype = (String) getIntent().getExtras().get("memberType");
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new FragmentOnce(), getResources().getString(R.string.once));
        viewPagerAdapter.addFragments(new FragmentFrequent(), getResources().getString(R.string.category_places));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(this.getResources().getColor(R.color.colorPrimary));

            if (Dashboard.type.equalsIgnoreCase("1") || membertype.equalsIgnoreCase("cab")) {
                Glide.with(this).load(getResources().getDrawable(R.drawable.car)).into(ser_type);
                Dashboard.type = "1";
            } else if (Dashboard.type.equalsIgnoreCase("2") || membertype.equalsIgnoreCase("delivery")) {
                Glide.with(this).load(getResources().getDrawable(R.drawable.motorbike)).into(ser_type);
                Dashboard.type = "2";
            } else {
                Glide.with(this).load(getResources().getDrawable(R.drawable.guestt)).into(ser_type);
                Dashboard.type = "3";
            }
        iv_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_close) {
            finish();
        }
    }
}
