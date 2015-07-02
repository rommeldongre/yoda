package com.greylabs.yoda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.greylabs.yoda.R;
import com.greylabs.yoda.views.GoalView;
import com.greylabs.yoda.views.MyArcProgress;
import com.greylabs.yoda.views.MyFloatingActionsMenu;

import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;
import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;


public class ActHome extends Activity implements View.OnClickListener, FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, MyFloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    HorizontalScrollView scrollView;
    LinearLayout linearLayout;
    RelativeLayout backgroundLayout;
    GoalView btnAddGoal;
    MyArcProgress arcTotalProgress;
    FloatingActionButton btnAddStep;
//    FloatingActionsMenu btnSettings;

    MyFloatingActionsMenu btnSettings;
//    FloatingActionButton btnFilters;


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
        backgroundLayout = (RelativeLayout) findViewById(R.id.LayoutToBeHidden);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollViewActHome);
        arcTotalProgress = (MyArcProgress) findViewById(R.id.arcTotalProgressActHome);
        btnAddStep = (FloatingActionButton) findViewById(R.id.btnAddStepActHome);
//        btnFilters = (FloatingActionButton) findViewById(R.id.btnFilterActHome);
        btnSettings = (MyFloatingActionsMenu) findViewById(R.id.btnSettingsActHome);


        setStyleToArcTotalProgress();
        getGoalsFromLocalAndPopulate();
        arcTotalProgress.setOnClickListener(this);
        btnAddStep.setOnClickListener(this);
//        btnFilters.setOnClickListener(this);
//        btnSettings.setOnClickListener(this);
        btnSettings.setOnFloatingActionsMenuUpdateListener(this);
    }

    private void setStyleToArcTotalProgress() {
        arcTotalProgress.setProgress(65);
        arcTotalProgress.setStrokeWidth(30);
        arcTotalProgress.setFinishedStrokeColor(getResources().getColor(R.color.luminous_green));
        arcTotalProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.gray_unfinished_progress));
        arcTotalProgress.setBackgroundCircleColor(getResources().getColor(R.color.transperent_total_arc_background));
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

//            case R.id.btnFilterActHome :
//                break;

//            case R.id.btnSettingsActHome :
//                if(btnSettings.isExpanded()){
//                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.transperent_dark_white));
//                    Toast.makeText(this, "hshs", Toast.LENGTH_SHORT).show();
//                }else {
//                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.transperent));
//                }
//                break;
        }
    }

    @Override
    public void onMenuExpanded() {
        backgroundLayout.setVisibility(View.GONE);
    }

    @Override
    public void onMenuCollapsed() {
        backgroundLayout.setVisibility(View.VISIBLE);
    }
}