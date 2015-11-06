package com.greylabs.yoda.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.scheduler.AlarmScheduler;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

public class ActNowFilter extends AppCompatActivity implements View.OnClickListener {

    TextView tvGoalName, tvStepName, tvTime;
    EditText edtExcuse;
    LinearLayout llDidIt, llDoingIt, llMissedIt, llButtons, llExcuseLog;
    CardView cvEmptyView, cvNotEmptyView;
    Button btnLogExcuse, btnCloseEmptyView ;
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
        btnCloseEmptyView = (Button) findViewById(R.id.btnCloseEmptyViewActNowFilter);
        btnLogExcuse = (Button) findViewById(R.id.btnLogExcuseActNowFilter);
        tvGoalName = (TextView) findViewById(R.id.tvGoalNameActNowFilter);
        tvStepName = (TextView) findViewById(R.id.tvStepNameActNowFilter);
        tvTime = (TextView) findViewById(R.id.tvTimeActNowFilter);
        edtExcuse = (EditText) findViewById(R.id.edtExcuseActNowFilter);
        llButtons = (LinearLayout) findViewById(R.id.llButtonsActNowFilter);
        llDidIt = (LinearLayout) findViewById(R.id.llDidActNowFilter);
        llDoingIt = (LinearLayout) findViewById(R.id.llDoingItActNowFilter);
        llMissedIt = (LinearLayout) findViewById(R.id.llMissedActNowFilter);
        llExcuseLog = (LinearLayout) findViewById(R.id.llLogExcuseActNowFilter);
        cvNotEmptyView = (CardView) findViewById(R.id.cvNotEmptyViewActNowFilter);
        cvEmptyView = (CardView) findViewById(R.id.cvEmptyViewActNowFilter);

        btnCloseEmptyView.setOnClickListener(this);
        btnLogExcuse.setOnClickListener(this);
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
            }
        }else if(caller.equals(Constants.ACT_HOME)){
            pendingStep=(PendingStep)getIntent().getSerializableExtra(Constants.KEY_PENDING_STEP_OBJECT);
            pendingStep.initDatabase(this);
            checkForEmptyViewVisibility();
        }

        if(pendingStep!=null){
            goal = new Goal(this).get(pendingStep.getGoalId());
            tvGoalName.setText(goal.getNickName());
            tvStepName.setText(pendingStep.getNickName());
        }else{
            Prefs prefs=Prefs.getInstance(this);
            goal = new Goal(this).get(prefs.getStretchGoalId());
            tvGoalName.setText(goal.getNickName());
            tvStepName.setText("No Step");
        }
    }

    private void checkForEmptyViewVisibility() {
        if (pendingStep == null || pendingStep.getNickName()==null) {
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
                    Logger.d("TAG", "Alarm Scheduler : " + alarmScheduler);
                    if(alarmScheduler==null)
                        alarmScheduler=new AlarmScheduler(this);
                    else
                        alarmScheduler.initContext(this);
                    alarmScheduler.setStepId(pendingStep.getId());
                    alarmScheduler.postponeAlarm(5);
                }
                finish();
                break;

            case R.id.llMissedActNowFilter :
                if(pendingStep!=null){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
                    pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
                    pendingStep.save();
                }
                showLogExcuseLayout();
                break;

            case R.id.btnLogExcuseActNowFilter :
                edtExcuse.getText();
                // put this text into the pendingStep
//                if(pendingStep!=null){
//                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
//                    pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
//                    pendingStep.save();
//                }
                finish();
                break;

            case R.id.btnCloseEmptyViewActNowFilter:
                finish();
                break;
        }
    }

    private void showLogExcuseLayout() {
        llButtons.setVisibility(View.GONE);
        llExcuseLog.setVisibility(View.VISIBLE);
    }
}
