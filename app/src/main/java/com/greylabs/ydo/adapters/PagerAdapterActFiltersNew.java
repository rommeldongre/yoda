package com.greylabs.ydo.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.greylabs.ydo.enums.StepFilterType;
import com.greylabs.ydo.fragments.FragFilterFinal;
import com.greylabs.ydo.utils.Constants;

public class PagerAdapterActFiltersNew extends FragmentPagerAdapter
{

    // The title of the tabs are coming from the class which is calling this adapter
    // because there the list of titles is needed

    private String[] titles;

    FragFilterFinal fragFilter;

    public PagerAdapterActFiltersNew(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    public FragFilterFinal getFragFilterNew() { return fragFilter; }

    @Override
    public Fragment getItem(int index) {

        Bundle bundle = new Bundle();
        fragFilter = new FragFilterFinal();
        switch (index) {
            case 0:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.DONE);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 1:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.TODAY);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 2:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_WEEK);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 3:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_MONTH);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 4:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_QUARTER);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 5:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.THIS_YEAR);
                fragFilter.setArguments(bundle);
                return fragFilter;

            case 6:
                bundle.putSerializable(Constants.FILTER_SCOPE, StepFilterType.NEVER);
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