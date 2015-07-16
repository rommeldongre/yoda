package com.greylabs.yoda.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.greylabs.yoda.R;
import com.greylabs.yoda.utils.Logger;

public class ActCreateTimeBox extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener {

    private Toolbar toolbar;
    private CheckBox cbWhenEarlyMorning;
    private CheckBox cbWhenMorning;
    private CheckBox cbWhenAfternoon;
    private CheckBox cbWhenEvening;
    private CheckBox cbWhenNight;
    private CheckBox cbWhenLateNight;

    private RadioGroup rgOn;
    private RadioButton rbOnDaily;
    private RadioButton rbOnWeekly;private CheckBox cbOnWeeklySun,cbOnWeeklyMon,cbOnWeeklyTue,cbOnWeeklyWed,cbOnWeeklyThu,cbOnWeeklyFri,cbOnWeeklySat;
    private RadioButton rbOnMonthly;private CheckBox cbOnMonthly1Week,cbOnMonthly2Week,cbOnMonthly3Week,cbOnMonthly4Week;
    private RadioButton rbOnQuaterly;private CheckBox cbOnQuaterly1Month,cbOnQuaterly2Month,cbOnQuaterly3Month;
    private RadioButton rbOnYearly;private CheckBox cbOnYearlyJan,cbOnYearlyFeb,cbOnYearlyMar,cbOnYearlyApr,cbOnYearlyMay,cbOnYearlyJun,
            cbOnYearlyJul,cbOnYearlyAug,cbOnYearlySep,cbOnYearlyOct,cbOnYearlyNov,cbOnYearlyDec;

    private RadioButton rbTillWeek,rbTillMonth,rbTillQuarter,rbTillYear,rbTillForever;


    ViewFlipper viewFlipper;
    RadioButton radioButton3;
    RadioButton radioButton4;
    View llWeekly;
    View llMonthly;
    View llQuaterly;
    View llYearly;
    View llDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_create_time_box);
        initUI();
        setHandlers();
    }

    private void initUI(){

        toolbar = (Toolbar) findViewById(R.id.toolBarActCreateTimeBox);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActCeateTimeBox));

        //when

        viewFlipper=(ViewFlipper)findViewById(R.id.vfActCreateTimeBoxOn);
        rgOn=(RadioGroup)findViewById(R.id.rgActCreateTimeBoxOn);
        rbOnDaily=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnDaily);
        rbOnMonthly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnMonthly);
        rbOnQuaterly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnQuarterly);
        rbOnWeekly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnWeekly);
        rbOnYearly=(RadioButton)findViewById(R.id.rbActCreateTimeBoxOnYearly);

        llDaily=findViewById(R.id.first);
        llWeekly=findViewById(R.id.second);
        llMonthly=findViewById(R.id.third);
        llQuaterly=findViewById(R.id.four);
        llYearly=findViewById(R.id.five);
    }
    private void setHandlers(){
//        rbOnMonthly.setOnCheckedChangeListener(this);
//        rbOnWeekly.setOnCheckedChangeListener(this);
//        rbOnQuaterly.setOnCheckedChangeListener(this);
//        rbOnYearly.setOnCheckedChangeListener(this);
        rgOn.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_create_time_box, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                this.finish();
                break;

            case R.id.actionSaveActCreateTimeBox :
                Logger.showMsg(this, "TimeBox saved");
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
        switch (radioButtonID){
            case R.id.rbActCreateTimeBoxOnDaily:
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.first)));
                break;
            case R.id.rbActCreateTimeBoxOnWeekly:
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.second)));
                break;
            case R.id.rbActCreateTimeBoxOnMonthly:
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.third)));
                break;
            case R.id.rbActCreateTimeBoxOnQuarterly:
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.four)));
                break;
            case R.id.rbActCreateTimeBoxOnYearly:
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.five)));
                break;
        }
    }
}