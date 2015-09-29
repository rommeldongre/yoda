package com.greylabs.yoda.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.PagerAdapterActFiltersNew;
import com.greylabs.yoda.interfaces.OnClickOfRecyclerViewFragFiltrNew;

public class ActFilters extends AppCompatActivity implements OnClickOfRecyclerViewFragFiltrNew//OnCheckExpandableList
{
    private Toolbar toolbar;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;
    //    private PagerAdapterActFilters pagerAdapter;
    private PagerAdapterActFiltersNew pagerAdapter;

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

        viewPager.setSaveEnabled(false);
//        pagerAdapter = new PagerAdapterActFilters(getSupportFragmentManager());
        pagerAdapter = new PagerAdapterActFiltersNew(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabStrip.setViewPager(viewPager);
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

//    @Override
//    public void onCheckExpandableList(int groupPosition, int childPosition, Goal goal) {
//        FragFilter fragFilter = pagerAdapter.getFragFilter();
//        fragFilter.onCheckExpandableList(groupPosition, childPosition, goal);
//    }

    @Override
    public void onClickRecyclerView(int position, String operation) {
//        FragFilterNew fragFilterToday = pagerAdapter.getFragFilterNew();
//        fragFilterToday.onClickRecyclerView(position, operation);
    }
}