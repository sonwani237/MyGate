package com.troology.mygate.dashboard.ui;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.troology.mygate.R;
import com.troology.mygate.utils.ViewPagerAdapter;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {

    ViewPager viewpager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Window window = this.getWindow();
        Drawable background = this.getResources().getDrawable(R.drawable.gradient_colour);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

        viewpager = findViewById(R.id.viewpager);
        tabs = findViewById(R.id.tabs);
        setUpDashboard(tabs, viewpager);
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
