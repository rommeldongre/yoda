package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.BitmapUtility;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.views.CircleView;
import com.greylabs.yoda.views.MyDonutProgress;

import java.util.List;

public class ActGoalDetails extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String goalTitle;
    private YodaCalendar yodaCalendar;
    private Toolbar toolbar;
    private MyDonutProgress donutProgress;
    private Goal currentGoal;
    private TimeBox currentTimeBox;
    private CircleView btnBullet;
    private TextView tvNickName, tvTime, tvObjective, tvKeyResult, tvReason, tvReward, tvBuddy;
    private final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";

//    public static void navigate(Context context, View transitionImage, Goal currentGoal) {
//        String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";
//        Intent intent = new Intent(context, ActGoalDetailsOld.class);
//        intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
//        intent.putExtra(EXTRA_TITLE, currentGoal.getNickName());
//        context.startActivity(intent);
//
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, transitionImage,EXTRA_TITLE);
//        ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
//    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_goal_details);

        initialize();
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.ColorPrimaryDark);
        int primary = getResources().getColor(R.color.ColorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.btnFabActGoalDetails), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        fab.setRippleColor(lightVibrantColor);
//        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.ColorPrimary));
//        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

    private void initialize() {
        currentGoal  = (Goal)getIntent().getSerializableExtra(Constants.GOAL_OBJECT);
        currentGoal.initDatabase(this);
        goalTitle = currentGoal.getNickName();

        ViewCompat.setTransitionName(findViewById(R.id.appBar), goalTitle);//currentGoal.getNickName().toString()
        supportPostponeEnterTransition();

        toolbar = (Toolbar) findViewById(R.id.toolBarActGoalDetailsNew);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(goalTitle);                       //currentGoal.getNickName().toString()

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(goalTitle);                    //currentGoal.getNickName().toString()
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        Bitmap bitmap = BitmapUtility.decodeSampledBitmapFromResource(getResources(), Prefs.getInstance(this).getWallpaperResourceId(),100,100);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                applyPalette(palette);
            }
        });

        tvNickName = (TextView) findViewById(R.id.tvNickNameActGoalDetailsNew);
        btnBullet = (CircleView) findViewById(R.id.btnBulletActGoalDetailsNew);
        tvTime = (TextView) findViewById(R.id.tvTimeActGoalDetailsNew);
        tvObjective = (TextView) findViewById(R.id.tvObjectiveActGoalDetailsNew);
        tvKeyResult = (TextView) findViewById(R.id.tvKeyResultActGoalDetailsNew);
        tvReason = (TextView) findViewById(R.id.tvGoalReasonActGoalDetailsNew);
        tvReward = (TextView) findViewById(R.id.tvGoalRewardActGoalDetailsNew);
        tvBuddy = (TextView) findViewById(R.id.tvGoalBuddyActGoalDetailsNew);
        donutProgress = (MyDonutProgress) findViewById(R.id.donutProgressActGoalDetails);

        populateUI();
    }

    private void populateUI() {

        donutProgress.setProgress((int)currentGoal.getGoalProgress());
        donutProgress.setTextColor(getResources().getColor(R.color.white));
        donutProgress.setTextSize(25);
        donutProgress.setSuffixText(String.valueOf(currentGoal.getRemainingStepCount()));
        donutProgress.setFinishedStrokeWidth(7);
        donutProgress.setUnfinishedStrokeWidth(7);
        donutProgress.setFinishedStrokeColor(getResources().getColor(R.color.white));
        donutProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.gray));

        tvNickName.setText(currentGoal.getNickName());
        btnBullet.setFillColor(Integer.parseInt(currentGoal.getColorCode()));
        btnBullet.setShowTitle(false);
        currentTimeBox = new TimeBox(this).get(currentGoal.getTimeBoxId());
        tvTime.setText(currentTimeBox.getNickName().toString());
        tvObjective.setText(currentGoal.getObjective());
        tvKeyResult.setText(currentGoal.getKeyResult());
        tvReason.setText(currentGoal.getReason());
        tvReward.setText(currentGoal.getReward());
        tvBuddy.setText(currentGoal.getBuddyEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_goal_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                this.finish();
                break;
            case R.id.actionEditActGoalDetails :
                if(!currentGoal.getNickName().equals(Constants.NICKNAME_STRETCH_GOAL)){
                    Intent intent = new Intent(this, ActAddNewGoal.class);
                    intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
                    intent.putExtra(Constants.CALLER, Constants.ACT_GOAL_DETAILS);
                    intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                    intent.putExtra(Constants.TIMEBOX_NICK_NAME, currentTimeBox.getNickName());
                    this.startActivityForResult(intent, Constants.REQUEST_CODE_ACT_ACT_GOAL_DETAILS);
                }else {
                    AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                    alertLogout.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertLogout.setMessage(Constants.MSG_CANT_EDIT_DELETE_GOAL);
                    alertLogout.show();
                }
                break;

            case R.id.actionDeleteActGoalDetails :
                if(!currentGoal.getNickName().equals(Constants.NICKNAME_STRETCH_GOAL)){
                    AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                    alertLogout.setPositiveButton("Move", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            performActionDeleteGoalYes();
                        }
                    });
                    alertLogout.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            performActionDeleteGoalNo();
                        }
                    });
                    alertLogout.setNeutralButton("Cancel", null);
                    alertLogout.setMessage(Constants.MSG_DELETE_GOAL);
                    alertLogout.show();
                }else {
                    AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
                    alertLogout.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertLogout.setMessage(Constants.MSG_CANT_EDIT_DELETE_GOAL);
                    alertLogout.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performActionDeleteGoalYes(){
        Goal goal = currentGoal;
        Prefs prefs = Prefs.getInstance(this);
        PendingStep pendingStep = new PendingStep(this);
        pendingStep.updateGoalId(goal.getId(), prefs.getStretchGoalId());
        int numberOfRowsAffected = goal.delete();
        if (numberOfRowsAffected == 1) {
            yodaCalendar = new YodaCalendar(this);
            yodaCalendar.detachTimeBox(goal.getTimeBoxId());
            //move steps to Stretch Goal
            TimeBox timeBox = new TimeBox(this).get(prefs.getUnplannedTimeBoxId());
            yodaCalendar.setTimeBox(timeBox);
            yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
            Logger.showMsg(this, Constants.MSG_GOAL_DELETED);
            this.finish();
        }
    }

    private void performActionDeleteGoalNo(){
        //delete goal here and all the steps related to it
        Goal goal = currentGoal;
        PendingStep ps=new PendingStep(this);
        ps.setGoalId(goal.getId());
        List<PendingStep> pendingSteps=ps.getAll(goal.getId());
        for (PendingStep pendingStep:pendingSteps){
            switch (pendingStep.getPendingStepType()){
                case SUB_STEP:
                case SERIES_STEP:
                    List<PendingStep> subSteps=pendingStep.getAllSubSteps(pendingStep.getId(),goal.getId());
                    for(PendingStep subStep:subSteps){
                        subStep.cancelAlarm();
                        subStep.delete();
                    }
                    break;
                case SINGLE_STEP:
                    pendingStep.cancelAlarm();
                    pendingStep.delete();
                    break;
            }
        }
        //ps.deleteAllPendingSteps();
        int numberOfRowsAffected = goal.delete();
        if (numberOfRowsAffected == 1) {
            yodaCalendar = new YodaCalendar(this);
            yodaCalendar.detachTimeBox(goal.getTimeBoxId());
            Logger.showMsg(this, Constants.MSG_GOAL_DELETED);
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULTCODE_OF_ACT_ADD_GOAL && data.getExtras().getBoolean(Constants.GOAL_UPDATED)) {// result from ActAddNewGoal
            currentGoal = currentGoal.get(currentGoal.getId());
            populateUI();
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

}
