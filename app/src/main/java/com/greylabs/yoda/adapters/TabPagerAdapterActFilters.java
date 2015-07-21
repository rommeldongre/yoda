package com.greylabs.yoda.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.greylabs.yoda.fragments.FragFilterNow;

import java.util.Locale;

public class TabPagerAdapterActFilters extends FragmentPagerAdapter
{
    //    private final FragmentManager mFragmentManager;
    private Context context;

    public TabPagerAdapterActFilters(FragmentManager fm) {
        super(fm);
//        this.mFragmentManager = fm ;
    }

    @Override
    public Fragment getItem(int index) {

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("currentApp", app );

        switch (index) {
            case 0:
                FragFilterNow fragFilterNow = new FragFilterNow();
//                bundle.putBoolean("hideBadgeCount", false);
//                fragFilterNow.setArguments(bundle);
                return fragFilterNow;

            case 1:
                FragFilterNow fragFilterNow1 = new FragFilterNow();
                return fragFilterNow1;

            case 2:
                FragFilterNow fragFilterNow2 = new FragFilterNow();
                return fragFilterNow2;

            case 3:
                FragFilterNow fragFilterNow3 = new FragFilterNow();
                return fragFilterNow3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

//    @Override
//    public int getPageIconResId(int position) {
//        return tab_icons[position];
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Now";
            case 1:
                return "Today";
            case 2:
                return "This Week";
            case 3:
                return "This Month";
        }
        return null;
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}