package com.greylabs.yoda.activities;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.PagerAdapterActFilters;
import com.greylabs.yoda.fragments.FragFilter;
import com.greylabs.yoda.interfaces.OnCheckExpandableList;
import com.greylabs.yoda.models.Goal;

public class ActFilters  extends AppCompatActivity implements OnCheckExpandableList{

    private Toolbar toolbar;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;
    private PagerAdapterActFilters pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        initialize();
    }

    private void initialize() {

        toolbar = (Toolbar) findViewById(R.id.toolBarActFilters);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.titleActFilters));

        viewPager = (ViewPager) findViewById(R.id.viewPagerActFilters);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabBarActFilters);

        viewPager.setOffscreenPageLimit(0);
        viewPager.setSaveEnabled(false);
        pagerAdapter = new PagerAdapterActFilters(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabStrip.setViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
//            case R.id.actionSearchActAppHomeNew :
//                onSearchRequested();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckExpandableList(int groupPosition, int childPosition, Goal goal) {
        FragFilter fragFilter = pagerAdapter.getFragFilter();
//        FragFilter fragFilter = pagerAdapter.getItem(pagerAdapter.);
        fragFilter.onCheckExpandableList(groupPosition, childPosition, goal);
    }
}