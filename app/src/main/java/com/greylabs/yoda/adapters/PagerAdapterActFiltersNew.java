package com.greylabs.yoda.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.greylabs.yoda.enums.StepFilterType;
import com.greylabs.yoda.fragments.FragFilterNew;
import com.greylabs.yoda.utils.Constants;

public class PagerAdapterActFiltersNew extends FragmentPagerAdapter
{
    private final String[] titles = { "Today", "This Week", "This Month", "This Quarter", "This Year"};

    FragFilterNew fragFilter;

    public PagerAdapterActFiltersNew(FragmentManager fm) {
        super(fm);
    }

    public FragFilterNew getFragFilterNew() { return fragFilter; }

    @Override
    public Fragment getItem(int index) {

        Bundle bundle = new Bundle();
        fragFilter = new FragFilterNew();

        switch (index) {
            case 0:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.TODAY);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 1:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_WEEK);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 2:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_MONTH);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 3:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_QUARTER);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 4:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_YEAR);
                fragFilter.setArguments(bundle);
                return fragFilter;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}