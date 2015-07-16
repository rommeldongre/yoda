package com.greylabs.yoda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

public class ActGoalDetails extends ActionBarActivity {

    Toolbar toolbar;
    Goal currentGoal;
    TimeBox currentTimeBox;

    TextView tvNickName, tvTime, tvObjective, tvKeyResult, tvReason, tvReward, tvBuddy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_details);
        initialize();
    }

    private void initialize() {

        currentGoal  = (Goal)getIntent().getSerializableExtra(Constants.GOAL_OBJECT);

        toolbar = (Toolbar) findViewById(R.id.toolBarActGoalDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentGoal.getNickName().toString());

        tvNickName = (TextView) findViewById(R.id.tvNickNameActGoalDetails);
        tvTime = (TextView) findViewById(R.id.tvTimeActGoalDetails);
        tvObjective = (TextView) findViewById(R.id.tvObjectiveActGoalDetails);
        tvKeyResult = (TextView) findViewById(R.id.tvKeyResultActGoalDetails);
        tvReason = (TextView) findViewById(R.id.tvGoalReasonActGoalDetails);
        tvReward = (TextView) findViewById(R.id.tvGoalRewardActGoalDetails);
        tvBuddy = (TextView) findViewById(R.id.tvGoalBuddyActGoalDetails);

        tvNickName.setText(currentGoal.getNickName());
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
                Intent intent = new Intent(this, ActAddNewGoal.class);
                intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
                intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                intent.putExtra(Constants.TIMEBOX_NICK_NAME, currentTimeBox.getNickName());
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}