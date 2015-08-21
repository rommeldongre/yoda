package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.BitmapUtility;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.views.CircleView;

public class ActGoalDetails extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String title = "rohit";

    private Toolbar toolbar;
    private Goal currentGoal;
    private TimeBox currentTimeBox;
    private CircleView btnBullet;
    private TextView tvNickName, tvTime, tvObjective, tvKeyResult, tvReason, tvReward, tvBuddy;
    private final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";

    public static void navigate(Context context, View transitionImage, Goal currentGoal) {

        String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";

        Intent intent = new Intent(context, ActGoalDetailsOld.class);
        intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
        intent.putExtra(EXTRA_TITLE, currentGoal.getNickName());
        context.startActivity(intent);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, transitionImage,EXTRA_TITLE);
        ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
    }

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

        ViewCompat.setTransitionName(findViewById(R.id.appBar), title);//currentGoal.getNickName().toString()
        supportPostponeEnterTransition();

        toolbar = (Toolbar) findViewById(R.id.toolBarActGoalDetailsNew);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);                       //currentGoal.getNickName().toString()

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);                    //currentGoal.getNickName().toString()
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

        populateUI();
    }

    private void populateUI() {
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
        }
        return super.onOptionsItemSelected(item);
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
