package com.troology.mygate.dashboard.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.troology.mygate.R;
import com.troology.mygate.dashboard.fragment.FragmentFrequent;
import com.troology.mygate.dashboard.fragment.FragmentOnce;

public class ViewPagerAdapterPopup extends FragmentPagerAdapter {

    private Context mContext;

    public ViewPagerAdapterPopup(Context mcontext, FragmentManager fm) {
        super(fm);
        this.mContext = mcontext;

    }


    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentOnce();
        } else {
            return new FragmentFrequent();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.once);
            case 1:
                return mContext.getString(R.string.category_places);
            default:
                return null;
        }
    }

}