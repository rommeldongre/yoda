package com.greylabs.yoda.activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.utils.BitmapUtility;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Dialogues;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.views.GoalView;
import com.greylabs.yoda.views.MyArcProgress;
import com.greylabs.yoda.views.MyFloatingActionButton;
import com.greylabs.yoda.views.MyFloatingActionsMenu;

import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ActHome extends AppCompatActivity implements View.OnClickListener, FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, MyFloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    HorizontalScrollView scrollView;
    LinearLayout linearLayout;
    RelativeLayout layoutToBeHidden, layoutWallpaper,
            layoutSettingsBackground, layoutOverlapping, llEmptyView;
    ImageView ivWallpaper;
    MyArcProgress arcTotalProgress;
    MyFloatingActionsMenu btnSettings;
    MyFloatingActionButton btnAddGoal, btnAddNewGoal, btnMyGoals, btnMyTimeBoxes, btnDefaultDuration, btnGoogleSettings,
            btnChangeWallpaper, btnFilters, btnAddStep; //btnExportToGoogleCalender, btnImportGoogleTasks
    Prefs prefs;

    ArrayList<Goal> goalList = new ArrayList<>();
    private Slot slot;
    private PendingStep nowPendingStep;
    private Goal nowGoal;
    private boolean noStep = true;


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
        llEmptyView = (RelativeLayout) findViewById(R.id.emptyViewActHome);

        ivWallpaper = (ImageView) findViewById(R.id.ivWallpaperActHome);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollViewActHome);
        arcTotalProgress = (MyArcProgress) findViewById(R.id.arcTotalProgressActHome);
        btnAddStep = (MyFloatingActionButton) findViewById(R.id.btnAddStepActHome);
        btnFilters = (MyFloatingActionButton) findViewById(R.id.btnFilterActHome);
        btnSettings = (MyFloatingActionsMenu) findViewById(R.id.btnSettingsActHome);

        btnMyGoals = (MyFloatingActionButton) findViewById(R.id.btnMyGoalsActHome);
        btnAddNewGoal = (MyFloatingActionButton) findViewById(R.id.addNewGoal);
        btnMyTimeBoxes = (MyFloatingActionButton) findViewById(R.id.btnMyTimeBoxesActHome);
        btnDefaultDuration = (MyFloatingActionButton) findViewById(R.id.btnDefaultDurationActHome);
        btnGoogleSettings = (MyFloatingActionButton) findViewById(R.id.btnGoogleSettingsActHome);
        btnChangeWallpaper = (MyFloatingActionButton) findViewById(R.id.btnChangeWallpaperActHome);
//        btnExportToGoogleCalender = (MyFloatingActionButton) findViewById(R.id.btnExportToGoogleCalActHome);
//        btnImportGoogleTasks = (MyFloatingActionButton) findViewById(R.id.btnImportGoogleTasksActHome);

        //set wallpaper here
        prefs = Prefs.getInstance(this);
//        layoutWallpaper.setBackgroundResource(prefs.getWallpaperResourceId());
//        layoutWallpaper.setBackground(new BitmapDrawable(BitmapUtility.decodeSampledBitmapFromResource(getResources(),prefs.getWallpaperResourceId(),ivWallpaper.getWidth(),ivWallpaper.getHeight())));
//        ivWallpaper.setImageBitmap(BitmapUtility.decodeSampledBitmapFromResource(getResources(),prefs.getWallpaperResourceId(),100,200));
        Drawable dr = new BitmapDrawable(BitmapUtility.decodeSampledBitmapFromResource(getResources(), prefs.getWallpaperResourceId(), 100, 200));
        layoutWallpaper.setBackgroundDrawable(dr);

        getGoalsFromLocalAndPopulate();
        arcTotalProgress.setOnClickListener(this);
        btnAddStep.setOnClickListener(this);
        btnFilters.setOnClickListener(this);
        btnSettings.setOnFloatingActionsMenuUpdateListener(this);

        btnMyGoals.setOnClickListener(this);
        btnAddNewGoal.setOnClickListener(this);
        btnMyTimeBoxes.setOnClickListener(this);
        btnDefaultDuration.setOnClickListener(this);
        btnGoogleSettings.setOnClickListener(this);
        btnChangeWallpaper.setOnClickListener(this);
//        btnExportToGoogleCalender.setOnClickListener(this);
//        btnImportGoogleTasks.setOnClickListener(this);
        populateNowInfo();
        setStyleToArcTotalProgress();
    }

    public void populateNowInfo() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (slot == null) slot = new Slot(ActHome.this);
                if (nowPendingStep == null) nowPendingStep = new PendingStep(ActHome.this);
                if (nowGoal == null) nowGoal = new Goal(ActHome.this);
                List<PendingStep> pendingSteps = nowPendingStep.getPendingSteps(slot.getActiveSlotId());
                if (pendingSteps != null && pendingSteps.size() > 0) {
                    for (PendingStep ps : pendingSteps) {
                        if (ps.isNowStep()) {
                            nowPendingStep = ps;
                            noStep = false;
                            break;
                        }
                    }
                    if (nowPendingStep != null) {
                        if (nowPendingStep.getNickName() != null){
                            arcTotalProgress.setStepName(nowPendingStep.getNickName());
                        }
                        else arcTotalProgress.setStepName("Untitled Step");

                        nowGoal = nowGoal.get(nowPendingStep.getGoalId());
                        arcTotalProgress.setGoalName(nowGoal.getNickName());

                        showEmptyView(false);
                    } else {
                        showEmptyView(true);
                    }

                } else {
//            Prefs prefs=Prefs.getInstance(ActHome.this);
//            nowGoal=nowGoal.get(prefs.getStretchGoalId());
//            arcTotalProgress.setGoalName(Constants.NICKNAME_STRETCH_GOAL);
//            arcTotalProgress.setStepName(" No Step");
                    showEmptyView(true);
                }
                setStyleToArcTotalProgress();
            }
        });
    }

    private void showEmptyView(final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(b){
                    Calendar cal = Calendar.getInstance();
                    int currentHour = cal.get(Calendar.HOUR_OF_DAY);

                    if (currentHour >= 0 && currentHour <= 5){
                        arcTotalProgress.setVisibility(View.GONE);
                        llEmptyView.setVisibility(View.VISIBLE);
                        Log.i("ActHome", "Detects night");
                        Log.i("Hour_of_day", String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));

                    }else{

                        Slot nowSlot = new Slot(getApplicationContext()).get(slot.getActiveSlotId());
                        if (nowSlot.getGoalId() == prefs.getStretchGoalId()){
                            arcTotalProgress.setStepName("No Step");
                            arcTotalProgress.setGoalName(Constants.NICKNAME_STRETCH_GOAL);
                            noStep = true;

                        }else{
                            arcTotalProgress.setStepName("No Step");
                            arcTotalProgress.setGoalName(new Goal(getApplicationContext()).get(nowSlot.getGoalId()).getNickName());
                            noStep = true;
                        }

                    }
                }else {
                    arcTotalProgress.setVisibility(View.VISIBLE);
                    llEmptyView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setStyleToArcTotalProgress() {
        arcTotalProgress.invalidate();
        arcTotalProgress.setProgress((int) nowGoal.getGoalProgress());
        arcTotalProgress.setStrokeWidth(getResources().getDimension(R.dimen.arcTotalProgressStrokeWidth));
        arcTotalProgress.setFinishedStrokeColor(getResources().getColor(R.color.luminous_green));
        arcTotalProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.gray_unfinished_progress));
        arcTotalProgress.setBackgroundCircleColor(getResources().getColor(R.color.transparent_total_arc_background));
        arcTotalProgress.setDividerColor(getResources().getColor(R.color.white));
//        arcTotalProgress.setArcAngle(330);

        // check for string lengths first and strip accordingly
        arcTotalProgress.setTextSize(getResources().getDimension(R.dimen.arcTotalProgressTextSize));
        arcTotalProgress.setTextColor(getResources().getColor(R.color.white));


        arcTotalProgress.setBottomTextSize(getResources().getDimension(R.dimen.arcTotalProgressLargeTextSize));
        arcTotalProgress.setBottomText(String.valueOf(nowGoal.getRemainingStepCount()));
//        arcTotalProgress.setSuffixTextSize(float suffixTextSize);
//        arcTotalProgress.setMax(int max);
//        arcTotalProgress.setSuffixText(String suffixText);
//        arcTotalProgress.setSuffixTextPadding(float suffixTextPadding);
    }

    private void getGoalsFromLocalAndPopulate() {
        List<Goal> temp = new Goal(this).getAll(Goal.GoalDeleted.SHOW_NOT_DELETED);
        if (temp != null) {
            if (goalList != null)
                goalList.clear();
            goalList.addAll(temp);
        }

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //add all the goals from db here loop
        for (int i = 0; i < goalList.size(); i++) {
            GoalView goalView = new GoalView(this, goalList.get(i));
            linearLayout.addView(goalView);
        }
        /*TextView tvAddNewGoal = new TextView(this);
        tvAddNewGoal.setText(getString(R.string.tvAddNewGoalActHome));
        tvAddNewGoal.setTextColor(getResources().getColor(R.color.white));
        tvAddNewGoal.setGravity(Gravity.CENTER);
        tvAddNewGoal.setSingleLine(true);*/

        // init btnAddGoal
        /*btnAddGoal = new MyFloatingActionButton(this);
        btnAddGoal.setId(R.id.addNewGoalActHome);
        btnAddGoal.setIcon(R.drawable.ic_btn_plus_sign);
        btnAddGoal.setColorNormal(getResources().getColor(R.color.transparent_total_arc_background));
        btnAddGoal.setColorPressed(getResources().getColor(R.color.transparent_more));
        btnAddGoal.setOnClickListener(this);*/

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
        relativeLayout.setLayoutParams(params);

        //relativeLayout.addView(btnAddGoal);
        final LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        //linearLayout1.addView(tvAddNewGoal);
        linearLayout1.addView(relativeLayout);
        linearLayout.addView(linearLayout1);
        scrollView.addView(linearLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewGoalActHome:
                Intent intent = new Intent(this, ActAddNewGoal.class);
                intent.putExtra(Constants.CALLER, Constants.ACT_HOME);
                intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                this.startActivity(intent);
                break;

            case R.id.arcTotalProgressActHome:

                if (!noStep){
                    Dialogues dialogues = new Dialogues(this);
                    dialogues.showNowNotificationDialogue(Constants.ACT_HOME, null, PendingStep.PendingStepStartEnd.START, nowPendingStep);
                }
//
//                Intent intent1 = new Intent(this, ActNowFilter.class);
//                intent1.putExtra(Constants.CALLER, Constants.ACT_HOME);
//                intent1.putExtra(Constants.KEY_PENDING_STEP_OBJECT,nowPendingStep);
//                this.startActivity(intent1);
                break;

            case R.id.btnAddStepActHome:
                Intent intent2 = new Intent(this, ActAddNewStep.class);
                intent2.putExtra(Constants.CALLER, Constants.ACT_HOME);
//                intent2.putExtra(Constants.STEP_ATTACHED_IN_EXTRAS, false);
                startActivity(intent2);
                break;

            case R.id.btnFilterActHome:
                startActivity(new Intent(this, ActFilters.class));
                break;

            case R.id.btnMyGoalsActHome:
                startActivity(new Intent(this, ActGoalList.class));
                btnSettings.collapse();
                break;

            case R.id.addNewGoal:
                Intent newIntent = new Intent(this, ActAddNewGoal.class);
                newIntent.putExtra(Constants.CALLER, Constants.ACT_HOME);
                newIntent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                startActivity(newIntent);
                btnSettings.collapse();
                break;

            case R.id.btnMyTimeBoxesActHome:
                startActivity(new Intent(this, ActTimeBoxList.class));
                btnSettings.collapse();
                break;

            case R.id.btnDefaultDurationActHome:
                startActivityForResult(new Intent(this, ActSettingDefaultDuration.class), Constants.REQUEST_CODE_ACT_HOME);
                btnSettings.collapse();
                break;

            case R.id.btnGoogleSettingsActHome:
                final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
                    GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, this, 0).show();
                } else {
                    // startActivity(new Intent(this,TasksSample.class));
                    startActivity(new Intent(this, ActSettingsGoogle.class));
                }
                btnSettings.collapse();
                break;

//            case R.id.btnExportToGoogleCalActHome :
//                startActivity(new Intent(this, ActSettingsGoogle.class));
//                btnSettings.collapse();
//                break;
//
//            case R.id.btnImportGoogleTasksActHome :
//                btnSettings.collapse();
//                break;

            case R.id.btnChangeWallpaperActHome:
                startActivityForResult(new Intent(this, ActSettingChangeWallpaper.class), Constants.REQUEST_CODE_ACT_HOME);
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        scrollView.removeAllViews();
//        getGoalsFromLocalAndPopulate();
//        populateNowInfo();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.RESULTCODE_ACT_SETTINGS_CHANGE_WALLPAPER:
                layoutWallpaper.setBackgroundResource(prefs.getWallpaperResourceId());
                break;

//            case Constants.RESULT_CODE_ACT_SETTINGS_DEFAULT_DURATION:
//                this.finish();
//                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        scrollView.removeAllViews();
        getGoalsFromLocalAndPopulate();
        populateNowInfo();
    }

    public void onDialogueClosed() {
        scrollView.removeAllViews();
        getGoalsFromLocalAndPopulate();
        populateNowInfo();
    }
}