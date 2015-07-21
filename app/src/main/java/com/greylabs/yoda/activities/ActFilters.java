package com.greylabs.yoda.activities;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.TabPagerAdapterActFilters;

public class ActFilters  extends ActionBarActivity
{

    private Toolbar toolbar;
//    Menu menu;

    private PagerSlidingTabStrip tabBarView;
    private ViewPager viewPager;
    private TabPagerAdapterActFilters tabPagerAdapter;
//    private int[] tab_icons_for_create_right = {R.drawable.ic_home_drawer_acthome, R.drawable.ic_notifications_drawer_acthome,
//            R.drawable.ic_app_info_app_home, R.drawable.ic_create_afo_act_app_home };
//    private int[] tab_icons_for_no_create_right = {R.drawable.ic_home_drawer_acthome, R.drawable.ic_notifications_drawer_acthome,
//            R.drawable.ic_app_info_app_home };


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

        viewPager.setOffscreenPageLimit(0);
        viewPager.setSaveEnabled(false);
        tabPagerAdapter = new TabPagerAdapterActFilters(getSupportFragmentManager());
        viewPager.setAdapter(tabPagerAdapter);

        tabBarView = (PagerSlidingTabStrip) findViewById(R.id.tabBarActFilters);
        tabBarView.setViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        this.menu = menu;
//        getMenuInflater().inflate(R.menu.menu_act_app_home_new, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home :
//                finish();
//                break;
////            case R.id.actionSearchActAppHomeNew :
////                onSearchRequested();
////                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onTabSelected(MaterialTab tab) {
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//    @Override
//    public void onTabReselected(MaterialTab materialTab) {
//
//    }
//    @Override
//    public void onTabUnselected(MaterialTab materialTab) {}
}