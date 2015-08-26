package com.greylabs.yoda.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
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

public class ActNowFilter extends Activity implements View.OnClickListener {

    TextView tvGoalName, tvStepName, tvTime;
    EditText edtExcuse;
    LinearLayout llDidIt, llDoingIt, llMissedIt, llButtons, llExcuseLog;
    CardView cvEmptyView, cvNotEmptyView;
//    Toolbar toolbar;
    Button btnLogExcuse, btnCloseEmptyView ;//,btnCloseNotEmptyView;
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

//        btnCloseNotEmptyView = (Button) findViewById(R.id.btnCloseNotEmptyViewActNowFilter);
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

//        btnCloseNotEmptyView.setOnClickListener(this);
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
            // get the current step from local
           // pendingStep = new PendingStep(this);
            pendingStep=(PendingStep)getIntent().getSerializableExtra(Constants.KEY_PENDING_STEP_OBJECT);
            pendingStep.initDatabase(this);
            checkForEmptyViewVisibility();
        }

        if(pendingStep!=null){
            goal = new Goal(this).get(pendingStep.getGoalId());
            tvGoalName.setText(goal.getNickName());
            tvStepName.setText(pendingStep.getNickName());
            //tvTime.setText(String.valueOf(alarmScheduler.getStartTime()));
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

            case R.id.llMissedActNowFilter :
//                Logger.showMsg(this, "Missed it");
                if(pendingStep!=null){
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
                    pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
                    pendingStep.save();
                }
//                finish();
//                showLogExcuseDialogue();
                showLogExcuseLayout();
                break;

//            case R.id.btnCloseNotEmptyViewActNowFilter:
//                this.finish();
//                break;

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

    private void showLogExcuseDialogue() {
        // custom dialog
//        final Dialog dialog = new Dialog(this);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dailogue_log_excuse);
//        dialog.setTitle(getString(R.string.titleDialogueExcuse));
//
//        // set the custom dialog components - text, image and button
//        EditText edtExcuse = (EditText) dialog.findViewById(R.id.edtExcuseDialogueExcuse);
//        Button dialogButton = (Button) dialog.findViewById(R.id.btnLogDialogueExcuse);
//        // if button is clicked, close the custom dialog
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // set excuse to the pending step here
//                dialog.dismiss();
//                finish();
//            }
//        });
//        dialog.show();
    }
}
