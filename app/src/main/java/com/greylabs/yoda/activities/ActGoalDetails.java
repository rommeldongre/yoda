package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.R;
import com.greylabs.yoda.apis.googleacc.GoogleSync;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.BitmapUtility;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.views.CircleView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ActGoalDetails extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private YodaCalendar yodaCalendar;
    private Toolbar toolbar;
    private FloatingActionButton fabStepList;
//    private MyDonutProgress donutProgress;
    private ProgressBar progressBar;
    private Goal currentGoal;
    private TimeBox currentTimeBox;
    private CircleView btnBullet;
    private TextView tvEta, tvNickName, tvTime, tvObjective, tvKeyResult, tvReason, tvReward, tvBuddy;
    private final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";
    int primaryColor;
    int primaryDarkColor;

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

    private void initialize() {
        currentGoal  = (Goal)getIntent().getSerializableExtra(Constants.GOAL_OBJECT);
        currentGoal.initDatabase(this);

        ViewCompat.setTransitionName(findViewById(R.id.appBar), currentGoal.getNickName());
        supportPostponeEnterTransition();

//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(getResources().getColor(R.color.example_color));

        toolbar = (Toolbar) findViewById(R.id.toolBarActGoalDetailsNew);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentGoal.getNickName());

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle(goalTitle);                    //currentGoal.getNickName().toString()
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        tvEta = (TextView) findViewById(R.id.tvEtaActGoalDetailsNew);
        fabStepList = (FloatingActionButton) findViewById(R.id.btnFabActGoalDetails);
        tvNickName = (TextView) findViewById(R.id.tvNickNameActGoalDetailsNew);
        btnBullet = (CircleView) findViewById(R.id.btnBulletActGoalDetailsNew);
        tvTime = (TextView) findViewById(R.id.tvTimeActGoalDetailsNew);
        tvObjective = (TextView) findViewById(R.id.tvObjectiveActGoalDetailsNew);
        tvKeyResult = (TextView) findViewById(R.id.tvKeyResultActGoalDetailsNew);
        tvReason = (TextView) findViewById(R.id.tvGoalReasonActGoalDetailsNew);
        tvReward = (TextView) findViewById(R.id.tvGoalRewardActGoalDetailsNew);
        tvBuddy = (TextView) findViewById(R.id.tvGoalBuddyActGoalDetailsNew);
        progressBar = (ProgressBar) findViewById(R.id.pbActGoalDetailsNew);
//        donutProgress = (MyDonutProgress) findViewById(R.id.donutProgressActGoalDetails);

        fabStepList.setOnClickListener(this);
        populateUI();
    }

    private void populateUI() {

//        donutProgress.setProgress((int)currentGoal.getGoalProgress());
//        donutProgress.setTextColor(getResources().getColor(R.color.white));
//        donutProgress.setTextSize(25);
//        donutProgress.setSuffixText(String.valueOf(currentGoal.getRemainingStepCount()));
//        donutProgress.setFinishedStrokeWidth(7);
//        donutProgress.setUnfinishedStrokeWidth(7);
//        donutProgress.setFinishedStrokeColor(getResources().getColor(R.color.white));
//        donutProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.gray));

        Bitmap bitmap = BitmapUtility.decodeSampledBitmapFromResource(getResources(), Prefs.getInstance(this).getWallpaperResourceId(), 100, 100);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                applyPalette(palette);
            }
        });

        collapsingToolbarLayout.setTitle(currentGoal.getNickName().toString());
        tvEta.setText(CalendarUtils.getOnlyFormattedDate(this.currentGoal.getDueDate()));
        progressBar.setProgress((int)currentGoal.getGoalProgress());
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

    private void applyPalette(Palette palette) {
        primaryDarkColor = getResources().getColor(R.color.ColorPrimaryDark);
        primaryColor = getResources().getColor(R.color.ColorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primaryColor));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDarkColor));
        updateBackground((FloatingActionButton) findViewById(R.id.btnFabActGoalDetails), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        fab.setRippleColor(lightVibrantColor);
//        int vibrantColor = palette.getVibrantColor(primaryColor);
//        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
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
        Prefs prefs = Prefs.getInstance(this);
        Goal goal = currentGoal;
        Goal stretchGoal=new Goal(this).get(prefs.getStretchGoalId());

        PendingStep pendingStep = new PendingStep(this);
        List<PendingStep> temp=pendingStep.getAll(PendingStep.PendingStepStatus.TODO, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,goal.getId());
        List<PendingStep> pendingSteps=new ArrayList<>();
        if(temp!=null){
            pendingSteps.addAll(temp);
            temp=pendingStep.getAll(PendingStep.PendingStepStatus.COMPLETED, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,goal.getId());
            if(temp!=null){
                pendingSteps.addAll(temp);
            }
        }
        for(PendingStep ps:pendingSteps){
            switch (ps.getPendingStepType()){
                case SPLIT_STEP:
                case SERIES_STEP:
                    temp=ps.getAllSubSteps(PendingStep.PendingStepStatus.TODO, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,ps.getId(),goal.getId());
                    if(temp!=null){
                        List<PendingStep> subSteps=new ArrayList<>();
                        subSteps.addAll(temp);
                        temp=ps.getAllSubSteps(PendingStep.PendingStepStatus.COMPLETED, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,ps.getId(),goal.getId());
                        if(temp!=null)
                            subSteps.addAll(temp);
                        for(PendingStep substep:subSteps){
                            substep.cancelAlarm();
                            substep.setStringId("");
                            substep.setGoalStringId(stretchGoal.getStringId());
                            substep.setGoalId(stretchGoal.getId());
                            substep.freeSlot();
                            substep.save();
                        }
                    }
                    ps.cancelAlarm();
                    ps.freeSlot();
                    ps.setStringId("");
                    ps.setGoalStringId(stretchGoal.getStringId());
                    ps.setGoalId(stretchGoal.getId());
                    ps.save();
                    break;
                case SINGLE_STEP:
                    ps.cancelAlarm();
                    ps.setStringId("");
                    ps.setGoalStringId(stretchGoal.getStringId());
                    ps.setGoalId(stretchGoal.getId());
                    ps.freeSlot();
                    ps.save();
            }
        }
        long oldTimeBoxId=goal.getTimeBoxId();
        goal.setDeleted(true);
        goal.setTimeBoxId(0);//No TimeBox
        goal.setUpdated(new DateTime(new Date()));
        goal.save();

        YodaCalendar yodaCalendar = new YodaCalendar(this);
        yodaCalendar.detachTimeBox(oldTimeBoxId);
        //move steps to Stretch Goal
        TimeBox timeBox = new TimeBox(this).get(prefs.getUnplannedTimeBoxId());
        yodaCalendar.setTimeBox(timeBox);
        yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
        Logger.showMsg(this, Constants.MSG_GOAL_DELETED);
        //sync code
        GoogleSync.getInstance(this).sync();
        this.finish();
    }



    private void performActionDeleteGoalNo() {
        //delete goal here and all the steps related to it
        Goal goal = currentGoal;
        PendingStep ps = new PendingStep(this);
        ps.setGoalId(goal.getId());
        List<PendingStep> pendingSteps = ps.getAll(goal.getId());
        if (pendingSteps != null){
            for (PendingStep pendingStep : pendingSteps) {
                switch (pendingStep.getPendingStepType()) {
                    case SUB_STEP:
                    case SERIES_STEP:
                        List<PendingStep> subSteps = pendingStep.getAllSubSteps(pendingStep.getId(), goal.getId());
                        for (PendingStep subStep : subSteps) {
                            subStep.cancelAlarm();
                            subStep.setDeleted(true);
                            subStep.save();
                        }
                        pendingStep.setDeleted(true);
                        pendingStep.save();
                        break;
                    case SINGLE_STEP:
                        pendingStep.cancelAlarm();
                        pendingStep.setDeleted(true);
                        pendingStep.save();
                        break;
                }
            }
        }
        //ps.deleteAllPendingSteps();
        goal.setDeleted(true);
        goal.setUpdated(new DateTime(new Date()));
        goal.setTimeBoxId(0);
        goal.save();
        yodaCalendar = new YodaCalendar(this);
        yodaCalendar.detachTimeBox(goal.getTimeBoxId());
        Logger.showMsg(this, Constants.MSG_GOAL_DELETED);
        //sync code
        GoogleSync.getInstance(this).sync();
        this.finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFabActGoalDetails :
                Intent i = new Intent(this, ActStepList.class);
                i.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                i.putExtra(Constants.GOAL_OBJECT, currentGoal);
                i.putExtra(Constants.CALLER, Constants.ACT_GOAL_LIST);
                startActivity(i);
                break;
        }
    }
}
