package com.greylabs.yoda.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.greylabs.yoda.fragments.FragFilter;
import com.greylabs.yoda.utils.Constants;

public class PagerAdapterActFilters extends FragmentPagerAdapter
{
    //    private final FragmentManager mFragmentManager;
    private final String[] titles = { "Today", "This Week", "This Month", "This Quarter", "This Year"};
    private Context context;

    public PagerAdapterActFilters(FragmentManager fm) {
        super(fm);
//        this.mFragmentManager = fm ;
    }

    @Override
    public Fragment getItem(int index) {

        Bundle bundle = new Bundle();

        switch (index) {
            case 0:
                FragFilter fragFilter = new FragFilter();
                bundle.putString(Constants.FILTER_SCOPE, Constants.SCOPE_TODAY);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 1:
                FragFilter fragFilter1 = new FragFilter();
                bundle.putString(Constants.FILTER_SCOPE, Constants.SCOPE_THIS_WEEK);
                fragFilter1.setArguments(bundle);
                return fragFilter1;

            case 2:
                FragFilter fragFilter2 = new FragFilter();
                bundle.putString(Constants.FILTER_SCOPE, Constants.SCOPE_THIS_MONTH);
                fragFilter2.setArguments(bundle);
                return fragFilter2;

            case 3:
                FragFilter fragFilter3 = new FragFilter();
                bundle.putString(Constants.FILTER_SCOPE, Constants.SCOPE_THIS_QUARTER);
                fragFilter3.setArguments(bundle);
                return fragFilter3;

            case 4:
                FragFilter fragFilter4 = new FragFilter();
                bundle.putString(Constants.FILTER_SCOPE, Constants.SCOPE_THIS_YEAR);
                fragFilter4.setArguments(bundle);
                return fragFilter4;

        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

//    @Override
//    public int getPageIconResId(int position) {
//        return tab_icons[position];
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}