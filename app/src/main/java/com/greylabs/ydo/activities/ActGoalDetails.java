package com.greylabs.ydo.activities;

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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.apis.googleacc.GoogleSync;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.utils.BitmapUtility;
import com.greylabs.ydo.utils.CalendarUtils;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.GoalUtils;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;
import com.greylabs.ydo.views.CircleView;

public class ActGoalDetails extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fabStepList;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        currentGoal = (Goal) getIntent().getSerializableExtra(Constants.GOAL_OBJECT);
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

        fabStepList.setOnClickListener(this);
        populateUI();
    }

    private void populateUI() {
        Bitmap bitmap = BitmapUtility.decodeSampledBitmapFromResource(getResources(), Prefs.getInstance(this).getWallpaperResourceId(), 100, 100);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                applyPalette(palette);
            }
        });

        collapsingToolbarLayout.setTitle(currentGoal.getNickName().toString());
        tvEta.setText(CalendarUtils.getOnlyFormattedDate(this.currentGoal.getDueDate()));
        progressBar.setProgress((int) currentGoal.getGoalProgress());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_goal_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.actionEditActGoalDetails:
                if (!currentGoal.getNickName().equals(Constants.NICKNAME_STRETCH_GOAL)) {
                    Intent intent = new Intent(this, ActAddNewGoal.class);
                    intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
                    intent.putExtra(Constants.CALLER, Constants.ACT_GOAL_DETAILS);
                    intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                    intent.putExtra(Constants.TIMEBOX_NICK_NAME, currentTimeBox.getNickName());
                    this.startActivityForResult(intent, Constants.REQUEST_CODE_ACT_ACT_GOAL_DETAILS);
                } else {
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

            case R.id.actionDeleteActGoalDetails:
                if (!currentGoal.getNickName().equals(Constants.NICKNAME_STRETCH_GOAL)) {
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
                } else {
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


    private void performActionDeleteGoalYes() {
        GoalUtils.performActionDeleteGoalYes(currentGoal);
        Logger.showMsg(this, Constants.MSG_GOAL_DELETED);
        //sync code
        GoogleSync.getInstance(this).sync();
        //sync codr
        this.finish();
    }


    private void performActionDeleteGoalNo() {
        //delete goal here and all the steps related to it
        GoalUtils.performActionDeleteGoalNo(currentGoal);
        Logger.showMsg(this, Constants.MSG_GOAL_DELETED);
        //sync code
        GoogleSync.getInstance(this).sync();
        //sync code
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULTCODE_OF_ACT_ADD_GOAL && data.getExtras().getBoolean(Constants.GOAL_UPDATED)) {// result from ActAddNewGoal
            currentGoal = currentGoal.get(currentGoal.getId());
            populateUI();
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFabActGoalDetails:
                Intent i = new Intent(this, ActStepList.class);
                i.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                i.putExtra(Constants.GOAL_OBJECT, currentGoal);
                i.putExtra(Constants.CALLER, Constants.ACT_GOAL_LIST);
                startActivity(i);
                break;
        }
    }
}
