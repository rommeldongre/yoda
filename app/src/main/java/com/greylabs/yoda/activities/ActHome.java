package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.greylabs.yoda.R;
import com.greylabs.yoda.views.GoalView;
import com.greylabs.yoda.views.MyArcProgress;
import com.greylabs.yoda.views.MyFloatingActionButton;
import com.greylabs.yoda.views.MyFloatingActionsMenu;

import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;


public class ActHome extends Activity implements View.OnClickListener, FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, MyFloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    HorizontalScrollView scrollView;
    LinearLayout linearLayout ;
    RelativeLayout layoutToBeHidden, layoutWallpaper, layoutSettingsBackground, layoutOverlapping;
    MyArcProgress arcTotalProgress;
    MyFloatingActionsMenu btnSettings;
    MyFloatingActionButton btnAddGoal, btnDefaultDuration, btnAutosyncWithGoogle,
            btnExportToGoogleCalender, btnImportGoogleTasks, btnChangeWallpaper, btnFilters, btnAddStep;

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
        layoutToBeHidden = (RelativeLayout) findViewById(R.id.LayoutToBeHiddenActHome);
        layoutSettingsBackground = (RelativeLayout) findViewById(R.id.layoutSettingsBackgroundActHome);
        layoutWallpaper = (RelativeLayout) findViewById(R.id.layoutWallpaperActHome);
        layoutOverlapping = (RelativeLayout) findViewById(R.id.layoutOverlappingActHome);

        scrollView = (HorizontalScrollView) findViewById(R.id.scrollViewActHome);
        arcTotalProgress = (MyArcProgress) findViewById(R.id.arcTotalProgressActHome);
        btnAddStep = (MyFloatingActionButton) findViewById(R.id.btnAddStepActHome);
        btnFilters = (MyFloatingActionButton) findViewById(R.id.btnFilterActHome);
        btnSettings = (MyFloatingActionsMenu) findViewById(R.id.btnSettingsActHome);

        btnDefaultDuration = (MyFloatingActionButton) findViewById(R.id.btnDefaultDurationActHome);
        btnAutosyncWithGoogle = (MyFloatingActionButton) findViewById(R.id.btnAutosyncWithGoogleActHome);
        btnExportToGoogleCalender = (MyFloatingActionButton) findViewById(R.id.btnExportToGoogleCalActHome);
        btnImportGoogleTasks = (MyFloatingActionButton) findViewById(R.id.btnImportGoogleTasksActHome);
        btnChangeWallpaper = (MyFloatingActionButton) findViewById(R.id.btnChangeWallpaperActHome);

        setStyleToArcTotalProgress();
        getGoalsFromLocalAndPopulate();
        arcTotalProgress.setOnClickListener(this);
        btnAddStep.setOnClickListener(this);
        btnFilters.setOnClickListener(this);
        btnSettings.setOnFloatingActionsMenuUpdateListener(this);

        btnDefaultDuration.setOnClickListener(this);
        btnAutosyncWithGoogle.setOnClickListener(this);
        btnExportToGoogleCalender.setOnClickListener(this);
        btnImportGoogleTasks.setOnClickListener(this);
        btnChangeWallpaper.setOnClickListener(this);
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
        btnAddGoal = new MyFloatingActionButton(this);
        btnAddGoal.setId(R.id.addNewGoalActHome);
        btnAddGoal.setIcon(R.drawable.ic_btn_plus_sign);
        btnAddGoal.setColorNormal(getResources().getColor(R.color.transperent_total_arc_background));
        btnAddGoal.setColorPressed(getResources().getColor(R.color.transperent_more));
        btnAddGoal.setOnClickListener(this);
        linearLayout.addView(btnAddGoal);

        scrollView.addView(linearLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addNewGoalActHome:
                startActivity(new Intent(this, ActAddNewGoal.class));
                break;

            case R.id.arcTotalProgressActHome :
                startActivity(new Intent(this, ActNowFilter.class));
                break;

            case R.id.btnAddStepActHome :
                startActivity(new Intent(this, ActAddStep.class));
                break;

            case R.id.btnFilterActHome :
                startActivity(new Intent(this, ActTodayFilter.class));
                break;

            case R.id.btnDefaultDurationActHome :
                startActivity(new Intent(this, ActSettingDefaultDuration.class));
                btnSettings.collapse();
                break;

            case R.id.btnAutosyncWithGoogleActHome :
                startActivity(new Intent(this, ActSettingAutosyncWithGoogle.class));
                btnSettings.collapse();
                break;

            case R.id.btnExportToGoogleCalActHome :
                startActivity(new Intent(this, ActSettingExportToGoogleCal.class));
                btnSettings.collapse();
                break;

            case R.id.btnImportGoogleTasksActHome :
                startActivity(new Intent(this, ActSettingImportGoogleTasks.class));
                btnSettings.collapse();
                break;

            case R.id.btnChangeWallpaperActHome :
                startActivity(new Intent(this, ActSettingChangeWallpaper.class));
                btnSettings.collapse();
                break;

//            case R.id.btnSettingsActHome :
//                if(btnSettings.isExpanded()){
//                    layoutToBeHidden.setBackgroundColor(getResources().getColor(R.color.transperent_dark_white));
//                    Toast.makeText(this, "hshs", Toast.LENGTH_SHORT).show();
//                }else {
//                    layoutToBeHidden.setBackgroundColor(getResources().getColor(R.color.transperent));
//                }
//                break;
        }
    }

    @Override
    public void onMenuExpanded() {
        layoutOverlapping.setVisibility(View.VISIBLE);
//        layoutToBeHidden.setVisibility(View.GONE);
//        layoutSettingsBackground.setBackgroundColor(getResources().getColor(R.color.settings_background_act_home));
    }

    @Override
    public void onMenuCollapsed() {
        layoutOverlapping.setVisibility(View.GONE);
//        layoutToBeHidden.setVisibility(View.VISIBLE);
//        layoutSettingsBackground.setBackgroundColor(getResources().getColor(R.color.transperent));
    }
}