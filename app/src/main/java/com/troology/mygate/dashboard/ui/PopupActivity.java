package com.troology.mygate.dashboard.ui;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.fragment.FragmentFrequent;
import com.troology.mygate.dashboard.fragment.FragmentOnce;
import com.troology.mygate.utils.FragmentActivityMessage;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.ViewPagerAdapter;

public class PopupActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView iv_close, ser_type;

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

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new FragmentOnce(), getResources().getString(R.string.once));
        viewPagerAdapter.addFragments(new FragmentFrequent(), getResources().getString(R.string.category_places));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(this.getResources().getColor(R.color.colorPrimary));
        if (Dashboard.type.equalsIgnoreCase("1")){
            Glide.with(this).load(getResources().getDrawable(R.drawable.car))
                    .into(ser_type);
        }else if (Dashboard.type.equalsIgnoreCase("2")){
            Glide.with(this).load(getResources().getDrawable(R.drawable.motorbike))
                    .into(ser_type);
        }else {
            Glide.with(this).load(getResources().getDrawable(R.drawable.guestt))
//                    .centerCrop()
//                    .apply(RequestOptions.circleCropTransform())
                    .into(ser_type);
        }

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
