package com.greylabs.yoda.activities;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.TabPagerAdapterActFilters;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class ActFilters  extends ActionBarActivity implements MaterialTabListener
{

    private Toolbar toolbar;
//    Menu menu;

    private MaterialTabHost tabBarView;
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
    @Override
    protected void onResume() {
        super.onResume();
        if(tabPagerAdapter!=null)
            tabPagerAdapter.notifyDataSetChanged();
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

        tabBarView = (MaterialTabHost) findViewById(R.id.tabBarActFilters);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//              tabBarView.setSelectedTab(position);
                tabBarView.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });

        tabBarView.addTab(
                tabBarView.newTab()
//                        .setIcon(getResources().getDrawable(tab_icons_for_no_create_right[n]))
                        .setTabListener(this));
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

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }
    @Override
    public void onTabUnselected(MaterialTab materialTab) {}
}