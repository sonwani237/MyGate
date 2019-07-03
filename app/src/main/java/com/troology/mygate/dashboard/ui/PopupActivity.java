package com.troology.mygate.dashboard.ui;

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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.troology.mygate.R;
import com.troology.mygate.dashboard.adapter.ViewPagerAdapterPopup;
import com.troology.mygate.dashboard.fragment.FragmentFrequent;
import com.troology.mygate.dashboard.fragment.FragmentOnce;
import com.troology.mygate.utils.FragmentActivityMessage;
import com.troology.mygate.utils.GlobalBus;

public class PopupActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView iv_close;
    String type;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup);

        iv_close = findViewById(R.id.ivclose);
        type =  getIntent().getStringExtra("type");
        viewPager = (ViewPager) findViewById(R.id.viewpager_popup);
        ViewPagerAdapterPopup adapter = new ViewPagerAdapterPopup(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_popup);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(this.getResources().getColor(R.color.colorPrimary));

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabfont, null);
            tv.setTypeface(this.getResources().getFont(R.font.opensans_semibold));
            tabLayout.getTabAt(i).setCustomView(tv);
        }

        FragmentActivityMessage fragmentActivityMessage =
                new FragmentActivityMessage("type", type);
        GlobalBus.getBus().post(fragmentActivityMessage);

     /*   Bundle bundle = new Bundle();
        bundle.putString("type", ""+type);
// set Fragmentclass Arguments
        FragmentOnce fragmentOnce = new FragmentOnce();
        fragmentOnce.setArguments(bundle);

        FragmentFrequent fragmentFrequent = new FragmentFrequent();
        fragmentFrequent.setArguments(bundle);*/

    }


}
