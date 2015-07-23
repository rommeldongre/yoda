package com.greylabs.yoda.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.utils.Logger;

public class ActNowFilter extends ActionBarActivity implements View.OnClickListener {

    TextView tvGoalName, tvStepName, tvTime;
    LinearLayout llDidIt, llDoingIt, llMissedIt;
    Toolbar toolbar;
    Button btnCloseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_filter);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolBarActNowFilter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActAddNewStep));

        btnCloseActivity = (Button) findViewById(R.id.btnCloseActNowFilter);
        tvGoalName = (TextView) findViewById(R.id.tvGoalNameActNowFilter);
        tvStepName = (TextView) findViewById(R.id.tvStepNameActNowFilter);
        tvTime = (TextView) findViewById(R.id.tvTimeActNowFilter);
        llDidIt = (LinearLayout) findViewById(R.id.llDidActNowFilter);
        llDoingIt = (LinearLayout) findViewById(R.id.llDoingItActNowFilter);
        llMissedIt = (LinearLayout) findViewById(R.id.llMissedActNowFilter);

        btnCloseActivity.setOnClickListener(this);
        llDidIt.setOnClickListener(this);
        llDoingIt.setOnClickListener(this);
        llMissedIt.setOnClickListener(this);

        getCurrentStepScheduledFromLocal();
    }

    private void getCurrentStepScheduledFromLocal() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llDidActNowFilter :
                Logger.showMsg(this, "Did it");
                finish();
                break;

            case R.id.llDoingItActNowFilter :
                Logger.showMsg(this, "Doing it");
                finish();
                break;

            case R.id.llMissedActNowFilter :
                Logger.showMsg(this, "Missed it");
                finish();
                break;

            case R.id.btnCloseActNowFilter :
                finish();
                break;
        }
    }
}
