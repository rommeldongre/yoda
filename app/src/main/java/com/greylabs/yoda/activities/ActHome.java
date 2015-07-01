package com.greylabs.yoda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.greylabs.yoda.R;
import com.greylabs.yoda.views.GoalView;
import com.greylabs.yoda.views.MyArcProgress;

import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;

public class ActHome extends Activity implements View.OnClickListener {

    HorizontalScrollView scrollView;
    LinearLayout linearLayout;
    GoalView btnAddGoal;
    MyArcProgress arcTotalProgress;
    FloatingActionButton btnAddStep;
    FloatingActionsMenu btnSettings;
    FloatingActionButton btnFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // full screen Activity
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);
        initialize();
    }

    private void initialize() {
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollViewActHome);
        arcTotalProgress = (MyArcProgress) findViewById(R.id.arcTotalProgressActHome);
        btnAddStep = (FloatingActionButton) findViewById(R.id.btnAddStepActHome);
        btnFilters = (FloatingActionButton) findViewById(R.id.btnFilterActHome);
        btnSettings = (FloatingActionsMenu) findViewById(R.id.btnSettingsActHome);


        setStyleToArcTotalProgress();
        getGoalsFromLocalAndPopulate();
        arcTotalProgress.setOnClickListener(this);
        btnAddStep.setOnClickListener(this);
        btnFilters.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
    }

    private void setStyleToArcTotalProgress() {
        arcTotalProgress.setProgress(65);
        arcTotalProgress.setStrokeWidth(30);
        arcTotalProgress.setFinishedStrokeColor(getResources().getColor(R.color.luminous_green));
        arcTotalProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.gray_unfinished_progress));
        arcTotalProgress.setBackgroundCircleColor(getResources().getColor(R.color.temp_transperent));
        arcTotalProgress.setDividerColor(getResources().getColor(R.color.gray));
//        arcTotalProgress.setArcAngle(330);

        // check for string lengths first and strip accordingly
        arcTotalProgress.setTextSize(20);
        arcTotalProgress.setTextColor(getResources().getColor(R.color.white));
        arcTotalProgress.setStepName("Step Name");
        arcTotalProgress.setGoalName("Goal Name");

        arcTotalProgress.setBottomTextSize(30);
        arcTotalProgress.setBottomText("38");
//        arcTotalProgress.setSuffixTextSize(float suffixTextSize);
//        arcTotalProgress.setMax(int max);
//        arcTotalProgress.setSuffixText(String suffixText);
//        arcTotalProgress.setSuffixTextPadding(float suffixTextPadding);
    }

    private void getGoalsFromLocalAndPopulate() {
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //add all the goals from db here loop
        for(int i=0; i<7; i++){
            GoalView goalView = new GoalView(this);
            linearLayout.addView(goalView);
        }
        // init btnAddGoal
        btnAddGoal = new GoalView(this);
        btnAddGoal.setIsAddButton(true);
        linearLayout.addView(btnAddGoal);

        scrollView.addView(linearLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arcTotalProgressActHome :
                break;

            case R.id.btnAddStepActHome :
                break;

            case R.id.btnFilterActHome :
                break;

            case R.id.btnSettingsActHome :
                break;
        }
    }
}