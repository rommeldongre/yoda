package com.greylabs.yoda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.scheduler.AlarmScheduler;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

public class ActNowFilter extends Activity implements View.OnClickListener {

    TextView tvGoalName, tvStepName, tvTime;
    LinearLayout llDidIt, llDoingIt, llMissedIt;
    CardView cvEmptyView, cvNotEmptyView;
//    Toolbar toolbar;
    Button btnCloseNotEmptyView, btnCloseEmptyView;
    private PendingStep pendingStep;
    private Goal goal;
    String caller;
    AlarmScheduler alarmScheduler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_filter);
        initialize();
    }

    private void initialize() {
//        toolbar = (Toolbar) findViewById(R.id.toolBarActNowFilter);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(getResources().getString(R.string.titleActAddNewStep));

        btnCloseNotEmptyView = (Button) findViewById(R.id.btnCloseNotEmptyViewActNowFilter);
        btnCloseEmptyView = (Button) findViewById(R.id.btnCloseEmptyViewActNowFilter);
        tvGoalName = (TextView) findViewById(R.id.tvGoalNameActNowFilter);
        tvStepName = (TextView) findViewById(R.id.tvStepNameActNowFilter);
        tvTime = (TextView) findViewById(R.id.tvTimeActNowFilter);
        llDidIt = (LinearLayout) findViewById(R.id.llDidActNowFilter);
        llDoingIt = (LinearLayout) findViewById(R.id.llDoingItActNowFilter);
        llMissedIt = (LinearLayout) findViewById(R.id.llMissedActNowFilter);
        cvNotEmptyView = (CardView) findViewById(R.id.cvNotEmptyViewActNowFilter);
        cvEmptyView = (CardView) findViewById(R.id.cvEmptyViewActNowFilter);

        btnCloseNotEmptyView.setOnClickListener(this);
        btnCloseEmptyView.setOnClickListener(this);
        llDidIt.setOnClickListener(this);
        llDoingIt.setOnClickListener(this);
        llMissedIt.setOnClickListener(this);

        getCurrentStepScheduledFromLocal();
    }

    private void getCurrentStepScheduledFromLocal() {
        caller = getIntent().getStringExtra(Constants.CALLER);
        if(caller.equals(Constants.ALARM_SERVICE)){
            alarmScheduler=(AlarmScheduler)getIntent().getSerializableExtra(Constants.ALARM_SCHEDULER);
            if(alarmScheduler!=null) {
                pendingStep = new PendingStep(this).get(alarmScheduler.getStepId());
                goal = new Goal(this).get(pendingStep.getGoalId());
                tvGoalName.setText(goal.getNickName());
                tvStepName.setText(pendingStep.getNickName());
                tvTime.setText(String.valueOf(alarmScheduler.getStartTime()));
            }
        }else if(caller.equals(Constants.ACT_HOME)){
            // get the current step from local
           // pendingStep = new PendingStep(this);
            checkForEmptyViewVisibility();
        }
    }

    private void checkForEmptyViewVisibility() {
        if (pendingStep == null) {
            cvEmptyView.setVisibility(View.VISIBLE);
            cvNotEmptyView.setVisibility(View.GONE);
        }
        else {
            cvEmptyView.setVisibility(View.GONE);
            cvNotEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llDidActNowFilter :
                Logger.showMsg(this, "Did it");
                if(pendingStep!=null){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                    pendingStep.save();
                }
                finish();
                break;

            case R.id.llDoingItActNowFilter :
                Logger.showMsg(this, "Doing it");
                if(pendingStep!=null){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.DOING);
                    pendingStep.save();
                    Logger.log("TAG","Alarm Scheduler : "+alarmScheduler);
                    if(alarmScheduler==null)
                        alarmScheduler=new AlarmScheduler(this);
                    else
                        alarmScheduler.initContext(this);
                    alarmScheduler.setStepId(pendingStep.getId());
                    alarmScheduler.postponeAlarm(5);
                }
                finish();
                break;

            case R.id.btnCloseNotEmptyViewActNowFilter:
            case R.id.llMissedActNowFilter :
                Logger.showMsg(this, "Missed it");
                if(pendingStep!=null){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
                    pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
                    pendingStep.save();
                }
                finish();
                break;

            case R.id.btnCloseEmptyViewActNowFilter:
                finish();
                break;
        }
    }
}
