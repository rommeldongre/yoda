package com.greylabs.yoda.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.activities.ActGoalDetails;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.utils.Constants;

public class StepView extends LinearLayout implements View.OnClickListener {

    private Context context;
    TextView tvStepName;
    Goal currentGoal;


    public StepView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public StepView(Context context, Goal goal) {
        super(context);
        this.context = context;
        this.currentGoal = goal;
        init();
    }

//    public WidgetTextFieldView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    this.context = context;
//        init();
//    }

    private void init() {

        inflate(getContext(), R.layout.view_step, this);
        this.tvStepName = (TextView)findViewById(R.id.tvStepNameViewStep);
        tvStepName.setText("Step of "+currentGoal.getNickName());
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(context, ActGoalDetails.class);
//        intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
//        context.startActivity(intent);
    }
}