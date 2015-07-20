package com.greylabs.yoda.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import com.greylabs.yoda.R;
import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.Set;
import java.util.TreeSet;

public class ActAddTimeBox extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener {

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


    private ViewFlipper viewFlipper;
    private View llWeekly;
    private View llMonthly;
    private View llQuarterly;
    private View llYearly;
    private View llDaily;
    private TimeBox currentTimeBox;
    private Set<TimeBoxWhen> timeBoxWhenSet;
    private Set<SubValue> timeBoxOnSubValueSet;
    private TimeBoxTill timeBoxTill;
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
        cbWhenEarlyMorning=(CheckBox)findViewById(R.id.cbActCreateTimeBoxEarlyMorning);
        cbWhenMorning=(CheckBox)findViewById(R.id.cbActCreateTimeBoxMorning);
        cbWhenAfternoon=(CheckBox)findViewById(R.id.cbActCreateTimeBoxAfternoon);
        cbWhenEvening=(CheckBox)findViewById(R.id.cbActCreateTimeBoxAfternoon);
        cbWhenNight=(CheckBox)findViewById(R.id.cbActCreateTimeBoxNight);
        cbWhenLateNight=(CheckBox)findViewById(R.id.cbActCreateTimeBoxLateNight);

        //on
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
        llQuarterly =findViewById(R.id.four);
        llYearly=findViewById(R.id.five);

        cbOnWeeklySun=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklySun);
        cbOnWeeklyMon=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyMon);
        cbOnWeeklyTue=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyTue);
        cbOnWeeklyWed=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyWed);
        cbOnWeeklyThu=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyThu);
        cbOnWeeklyFri=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklyFri);
        cbOnWeeklySat=(CheckBox)llWeekly.findViewById(R.id.cbActCreateTimeBoxOnWeeklySat);

        cbOnMonthly1Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly1Week);
        cbOnMonthly2Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly2Week);
        cbOnMonthly3Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly3Week);
        cbOnMonthly4Week=(CheckBox)llMonthly.findViewById(R.id.cbActCreateTimeBoxOnMonthly4Week);

        cbOnQuaterly1Month=(CheckBox)llQuarterly.findViewById(R.id.cbActCreateTimeBoxOnQuarterly1Month);
        cbOnQuaterly2Month=(CheckBox)llQuarterly.findViewById(R.id.cbActCreateTimeBoxOnQuarterly2Month);
        cbOnQuaterly3Month=(CheckBox)llQuarterly.findViewById(R.id.cbActCreateTimeBoxOnQuarterly3Month);

        cbOnYearlyJan=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyJan);
        cbOnYearlyFeb=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyFeb);
        cbOnYearlyMar=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyMar);
        cbOnYearlyApr=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyApr);
        cbOnYearlyMay=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyMay);
        cbOnYearlyJun=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyJun);
        cbOnYearlyJul=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyJul);
        cbOnYearlyAug=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyAug);
        cbOnYearlySep=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlySep);
        cbOnYearlyOct=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyOct);
        cbOnYearlyNov=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyNov);
        cbOnYearlyDec=(CheckBox)llYearly.findViewById(R.id.cbActCreateTimeBoxOnYearlyDec);

        rbTillWeek=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillWeek);
        rbTillMonth=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillMonth);
        rbTillQuarter=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillQuarter);
        rbTillYear=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillYear);
        rbTillForever=(RadioButton)findViewById(R.id.rbActCreateTimeBoxTillForever);

        Intent intent = getIntent();
        switch (intent.getStringExtra(Constants.CALLER)){
            case Constants.ACT_TIMEBOX_LIST :
                currentTimeBox = new TimeBox(this);
                break;

            case Constants.ACT_ADD_NEW_GOAL :
                if(intent.getExtras().getBoolean(Constants.TIMEBOX_ATTACHED_IN_EXTRAS)){
                    currentTimeBox = (TimeBox) intent.getSerializableExtra(Constants.TIMEBOX_OBJECT);
                    getSupportActionBar().setTitle(getString(R.string.titleActAddNewTimeBoxEdit));

                    //initialize all the views here from the old values of the timebox
                }
                break;
        }
    }
    private void setHandlers(){
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
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.first)));
                break;
            case R.id.rbActCreateTimeBoxOnWeekly:
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.second)));
                break;
            case R.id.rbActCreateTimeBoxOnMonthly:
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.third)));
                break;
            case R.id.rbActCreateTimeBoxOnQuarterly:
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.four)));
                break;
            case R.id.rbActCreateTimeBoxOnYearly:
                invalidateOnValueSelections();
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.five)));
                break;
        }
    }

    private TimeBox createTimeBoxObjectFromUI(){
        if(timeBoxWhenSet==null){
            timeBoxWhenSet=new TreeSet<>();
        }else{
            timeBoxWhenSet.clear();
        }
        if(timeBoxOnSubValueSet==null){
            timeBoxOnSubValueSet=new TreeSet<>();
        }else{
            timeBoxOnSubValueSet.clear();
        }


        return null;
    }

    private void invalidateOnValueSelections(){
        cbOnWeeklyMon.setChecked(false);
        cbOnWeeklyTue.setChecked(false);
        cbOnWeeklyWed.setChecked(false);
        cbOnWeeklyThu.setChecked(false);
        cbOnWeeklyFri.setChecked(false);
        cbOnWeeklySat.setChecked(false);
        cbOnWeeklySun.setChecked(false);

        cbOnMonthly1Week.setChecked(false);
        cbOnMonthly2Week.setChecked(false);
        cbOnMonthly3Week.setChecked(false);
        cbOnMonthly4Week.setChecked(false);

        cbOnQuaterly1Month.setChecked(false);
        cbOnQuaterly2Month.setChecked(false);
        cbOnQuaterly3Month.setChecked(false);

        cbOnYearlyJan.setChecked(false);
        cbOnYearlyFeb.setChecked(false);
        cbOnYearlyMar.setChecked(false);
        cbOnYearlyApr.setChecked(false);
        cbOnYearlyMay.setChecked(false);
        cbOnYearlyJun.setChecked(false);
        cbOnYearlyJul.setChecked(false);
        cbOnYearlyAug.setChecked(false);
        cbOnYearlySep.setChecked(false);
        cbOnYearlyOct.setChecked(false);
        cbOnYearlyNov.setChecked(false);
        cbOnYearlyDec.setChecked(false);
    }
}