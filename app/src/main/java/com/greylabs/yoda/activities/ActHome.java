package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.greylabs.yoda.R;
import com.greylabs.yoda.apis.TasksSample;
import com.greylabs.yoda.models.Day;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.views.GoalView;
import com.greylabs.yoda.views.MyArcProgress;
import com.greylabs.yoda.views.MyFloatingActionButton;
import com.greylabs.yoda.views.MyFloatingActionsMenu;

import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;


public class ActHome extends Activity implements View.OnClickListener, FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, MyFloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    HorizontalScrollView scrollView;
    LinearLayout linearLayout ;
    RelativeLayout layoutToBeHidden, layoutWallpaper, layoutSettingsBackground, layoutOverlapping;
    MyArcProgress arcTotalProgress;
    MyFloatingActionsMenu btnSettings;
    MyFloatingActionButton btnAddGoal, btnMyGoals, btnMyTimeBoxes, btnDefaultDuration, btnAutosyncWithGoogle,
            btnExportToGoogleCalender, btnImportGoogleTasks, btnChangeWallpaper, btnFilters, btnAddStep;
    Prefs prefs;

    ArrayList<Goal> goalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        QuickStart quickStart = new QuickStart(this);
//        quickStart.quickStart();

        YodaCalendar yodaCalendar=new YodaCalendar(this);
        Day day=new Day(this);
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

        btnMyGoals = (MyFloatingActionButton) findViewById(R.id.btnMyGoalsActHome);
        btnMyTimeBoxes = (MyFloatingActionButton) findViewById(R.id.btnMyTimeBoxesActHome);
        btnDefaultDuration = (MyFloatingActionButton) findViewById(R.id.btnDefaultDurationActHome);
        btnAutosyncWithGoogle = (MyFloatingActionButton) findViewById(R.id.btnAutosyncWithGoogleActHome);
        btnExportToGoogleCalender = (MyFloatingActionButton) findViewById(R.id.btnExportToGoogleCalActHome);
        btnImportGoogleTasks = (MyFloatingActionButton) findViewById(R.id.btnImportGoogleTasksActHome);
        btnChangeWallpaper = (MyFloatingActionButton) findViewById(R.id.btnChangeWallpaperActHome);

        //set wallpaper here
        prefs = Prefs.getInstance(this);
        layoutWallpaper.setBackgroundResource(prefs.getWallpaperResourceId());

        setStyleToArcTotalProgress();
        getGoalsFromLocalAndPopulate();
        arcTotalProgress.setOnClickListener(this);
        btnAddStep.setOnClickListener(this);
        btnFilters.setOnClickListener(this);
        btnSettings.setOnFloatingActionsMenuUpdateListener(this);

        btnMyGoals.setOnClickListener(this);
        btnMyTimeBoxes.setOnClickListener(this);
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
        arcTotalProgress.setDividerColor(getResources().getColor(R.color.white));
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
        if(new Goal(this).getAll() != null){
            if(goalList != null)
                goalList.clear();
            goalList.addAll(new Goal(this).getAll());
        }

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //add all the goals from db here loop
        for(int i=0; i < goalList.size(); i++){
            GoalView goalView = new GoalView(this, goalList.get(i));
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
                Intent intent = new Intent(this, ActAddNewGoal.class);
                intent.putExtra(Constants.CALLER, Constants.ACT_HOME);
                intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                this.startActivity(intent);
                break;

            case R.id.arcTotalProgressActHome :
                Intent intent1 = new Intent(this, ActNowFilter.class);
                intent1.putExtra(Constants.CALLER, Constants.ACT_HOME);
                this.startActivity(intent1);
                break;

            case R.id.btnAddStepActHome :
                Intent intent2 = new Intent(this, ActAddNewStep.class);
                intent2.putExtra(Constants.CALLER, Constants.ACT_HOME);
//                intent2.putExtra(Constants.STEP_ATTACHED_IN_EXTRAS, false);
                startActivity(intent2);
                break;

            case R.id.btnFilterActHome :
                startActivity(new Intent(this, ActFilters.class));
                break;

            case R.id.btnMyGoalsActHome :
                startActivity(new Intent(this, ActGoalList.class));
                btnSettings.collapse();
                break;

            case R.id.btnMyTimeBoxesActHome :
                startActivity(new Intent(this, ActTimeBoxList.class));
                btnSettings.collapse();
                break;

            case R.id.btnDefaultDurationActHome :
                startActivity(new Intent(this, ActSettingDefaultDuration.class));
                btnSettings.collapse();
                break;

            case R.id.btnAutosyncWithGoogleActHome :
                startActivity(new Intent(this, TasksSample.class));
                btnSettings.collapse();
                break;

            case R.id.btnExportToGoogleCalActHome :
                btnSettings.collapse();
                break;

            case R.id.btnImportGoogleTasksActHome :
                btnSettings.collapse();
                break;

            case R.id.btnChangeWallpaperActHome :
                startActivityForResult(new Intent(this, ActSettingChangeWallpaper.class), 1);
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
//        layoutOverlapping.setVisibility(View.VISIBLE);
        layoutToBeHidden.setVisibility(View.GONE);
        layoutSettingsBackground.setBackgroundColor(getResources().getColor(R.color.settings_background_act_home));
    }

    @Override
    public void onMenuCollapsed() {
//        layoutOverlapping.setVisibility(View.GONE);
        layoutToBeHidden.setVisibility(View.VISIBLE);
        layoutSettingsBackground.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollView.removeAllViews();
        getGoalsFromLocalAndPopulate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.RESULTCODE_ACT_SETTINGS_CHANGE_WALLPAPER){
//            prefs = Prefs.getInstance(this);
            layoutWallpaper.setBackgroundResource(prefs.getWallpaperResourceId());
        }
    }
}