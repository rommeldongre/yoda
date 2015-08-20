package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.apis.TasksSample;
import com.greylabs.yoda.apis.googleacc.GoogleAccount;
import com.greylabs.yoda.apis.googleacc.GoogleSync;
import com.greylabs.yoda.enums.AccountType;
import com.greylabs.yoda.models.Day;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.scheduler.BootCompleteService;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.threads.ImportTaskAsyncThread;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.views.GoalView;
import com.greylabs.yoda.views.MyArcProgress;
import com.greylabs.yoda.views.MyFloatingActionButton;
import com.greylabs.yoda.views.MyFloatingActionsMenu;

import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
    private Slot slot;
    private PendingStep nowPendingStep;
    private Goal nowGoal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
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
        populateNowInfo();
        setStyleToArcTotalProgress();

        Logger.log("----------- scrollview height : ", ""+scrollView.getHeight());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateNowInfo();
    }

    private void populateNowInfo() {
        if(slot==null) slot=new Slot(this);
        if(nowPendingStep==null) nowPendingStep=new PendingStep(this);
        if(nowGoal==null) nowGoal=new Goal(this);
        List<PendingStep> pendingSteps=nowPendingStep.getPendingSteps(slot.getActiveSlotId());
        if(pendingSteps!=null && pendingSteps.size()>0) {
            for (PendingStep ps : pendingSteps) {
                if (ps.isNowStep()) {
                    nowPendingStep = ps;
                    break;
                }
            }
            if(nowPendingStep!=null){
                arcTotalProgress.setStepName(nowPendingStep.getNickName());
                nowGoal=nowGoal.get(nowPendingStep.getGoalId());
                arcTotalProgress.setGoalName(nowGoal.getNickName());
            }
        }else{
            Prefs prefs=Prefs.getInstance(this);
            nowGoal=nowGoal.get(prefs.getStretchGoalId());
            arcTotalProgress.setGoalName(Constants.NICKNAME_STRETCH_GOAL);
            arcTotalProgress.setStepName(" No Step");
        }
        setStyleToArcTotalProgress();
    }

    private void setStyleToArcTotalProgress() {
        arcTotalProgress.setProgress((int)nowGoal.getGoalProgress());
        arcTotalProgress.setStrokeWidth(30);
        arcTotalProgress.setFinishedStrokeColor(getResources().getColor(R.color.luminous_green));
        arcTotalProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.gray_unfinished_progress));
        arcTotalProgress.setBackgroundCircleColor(getResources().getColor(R.color.transperent_total_arc_background));
        arcTotalProgress.setDividerColor(getResources().getColor(R.color.white));
//        arcTotalProgress.setArcAngle(330);

        // check for string lengths first and strip accordingly
        arcTotalProgress.setTextSize(20);
        arcTotalProgress.setTextColor(getResources().getColor(R.color.white));
        if(nowPendingStep==null || nowPendingStep.getNickName()==null) {
            arcTotalProgress.setStepName("No Step");
        }else{
            arcTotalProgress.setStepName(nowPendingStep.getNickName());
        }
        arcTotalProgress.setGoalName(nowGoal.getNickName());

        arcTotalProgress.setBottomTextSize(30);
        arcTotalProgress.setBottomText(String.valueOf(nowGoal.getRemainingStepCount()));
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
        TextView tvAddNewGoal = new TextView(this);
        tvAddNewGoal.setText(getString(R.string.tvAddNewGoalActHome));
        tvAddNewGoal.setTextColor(getResources().getColor(R.color.white));
        tvAddNewGoal.setGravity(Gravity.CENTER);
        tvAddNewGoal.setSingleLine(true);

        // init btnAddGoal
        btnAddGoal = new MyFloatingActionButton(this);
        btnAddGoal.setId(R.id.addNewGoalActHome);
        btnAddGoal.setIcon(R.drawable.ic_btn_plus_sign);
        btnAddGoal.setColorNormal(getResources().getColor(R.color.transperent_total_arc_background));
        btnAddGoal.setColorPressed(getResources().getColor(R.color.transperent_more));
        btnAddGoal.setOnClickListener(this);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
        relativeLayout.addView(btnAddGoal);
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.addView(tvAddNewGoal);
        linearLayout1.addView(relativeLayout);
        linearLayout.addView(linearLayout1);
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
                intent1.putExtra(Constants.KEY_PENDING_STEP_OBJECT,nowPendingStep);
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
                //startActivity(new Intent(this, TasksSample.class));
                final GoogleAccount googleAccount=new GoogleAccount(this);
                googleAccount.chooseAccountDialog(GoogleAccount.ACCOUNT_TYPE, "Choose Account", new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        prefs.setDefaultAccountEmailId(googleAccount.getUsers().get(position));
                        prefs.setDefaultAccountType(AccountType.GOOGLE.ordinal());
                        googleAccount.dismissChooseAccountDialog();
                    }
                }, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        AsyncTask asyncTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                GoogleAccount googleAccount = new GoogleAccount(ActHome.this);
                                googleAccount.authenticate();

                                return null;
                            }
                        };
                        asyncTask.execute();
                    }
                });

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