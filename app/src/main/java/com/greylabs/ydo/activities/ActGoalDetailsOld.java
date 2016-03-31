package com.greylabs.ydo.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.views.CircleView;

public class ActGoalDetailsOld extends AppCompatActivity {

    Toolbar toolbar;
    Goal currentGoal;
    TimeBox currentTimeBox;
    CircleView btnBullet;
    TextView tvNickName, tvTime, tvObjective, tvKeyResult, tvReason, tvReward, tvBuddy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details_old);
        initialize();
    }

    private void initialize() {
        currentGoal  = (Goal)getIntent().getSerializableExtra(Constants.GOAL_OBJECT);
        currentGoal.initDatabase(this);

        toolbar = (Toolbar) findViewById(R.id.toolBarActGoalDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentGoal.getNickName().toString());

        tvNickName = (TextView) findViewById(R.id.tvNickNameActGoalDetails);
        btnBullet = (CircleView) findViewById(R.id.btnBulletActGoalDetails);
        tvTime = (TextView) findViewById(R.id.tvTimeActGoalDetails);
        tvObjective = (TextView) findViewById(R.id.tvObjectiveActGoalDetails);
        tvKeyResult = (TextView) findViewById(R.id.tvKeyResultActGoalDetails);
        tvReason = (TextView) findViewById(R.id.tvGoalReasonActGoalDetails);
        tvReward = (TextView) findViewById(R.id.tvGoalRewardActGoalDetails);
        tvBuddy = (TextView) findViewById(R.id.tvGoalBuddyActGoalDetails);

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