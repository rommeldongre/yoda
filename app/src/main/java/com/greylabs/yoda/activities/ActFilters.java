package com.greylabs.yoda.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.PagerAdapterActFiltersNew;
import com.greylabs.yoda.enums.StepFilterType;
import com.greylabs.yoda.fragments.FragFilterFinal;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewFragFilterFinal;

public class ActFilters extends AppCompatActivity implements OnClickOfRecyclerViewFragFilterFinal//OnCheckExpandableList
{
    //    private PagerAdapterActFilters pagerAdapter;
    private PagerAdapterActFiltersNew pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        initialize();
    }

    private void initialize() {

        // PagerSlidingTabsStrip in the logical calendar is replaced with tabLayout from design support libraries
        // which gives us the feature of setting tab gravity as center

        String[] titles = {"Done", "Today", "This Week", "This Month", "This Quarter", "This Year", "Never"};

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBarActFilters);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.titleActFilters));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerActFilters);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabBarActFilters);

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager.setSaveEnabled(false);
        viewPager.setOffscreenPageLimit(titles.length);
//        pagerAdapter = new PagerAdapterActFilters(getSupportFragmentManager());
        pagerAdapter = new PagerAdapterActFiltersNew(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//            case R.id.actionSearchActAppHomeNew :
//                onSearchRequested();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickRecyclerView(int position, String operation, StepFilterType scope) {
        //scope is not used yet
        FragFilterFinal fragFilterFinal = pagerAdapter.getFragFilterNew();
        fragFilterFinal.onClickRecyclerView(position, operation);
    }
}