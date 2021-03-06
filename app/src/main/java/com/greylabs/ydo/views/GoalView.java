package com.greylabs.ydo.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greylabs.ydo.R;
import com.greylabs.ydo.activities.ActGoalDetails;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.utils.CalendarUtils;
import com.greylabs.ydo.utils.Constants;

public class GoalView extends LinearLayout implements View.OnClickListener {

    private Context context;
    MyDonutProgress donutProgress;
    TextView tvGoalName, tvETGoal;
    Goal currentGoal;
    int COLOR_RED, COLOR_GREEN, COLOR_GRAY;

    public GoalView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public GoalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public GoalView(Context context, Goal goal) {
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
        inflate(getContext(), R.layout.view_goal, this);
        this.COLOR_RED = context.getResources().getColor(R.color.colorcode_red);
        this.COLOR_GREEN = context.getResources().getColor(R.color.luminous_green);
        this.COLOR_GRAY = context.getResources().getColor(R.color.gray);
        this.donutProgress = (MyDonutProgress) findViewById(R.id.donutProgressViewGoal);
        this.tvETGoal = (TextView)findViewById(R.id.tvETViewGoal);
        this.tvGoalName = (TextView)findViewById(R.id.tvViewGoal);
        tvETGoal.setText(CalendarUtils.getOnlyFormattedDate(this.currentGoal.getDueDate()));
        tvGoalName.setText(this.currentGoal.getNickName());
        setDonutProgressValues();

        donutProgress.setOnClickListener(this);
    }

    private void setDonutProgressValues() {
        donutProgress.setProgress((int) currentGoal.getGoalProgress());
        donutProgress.setTextColor(getResources().getColor(R.color.white));
        donutProgress.setTextSize(25);
        donutProgress.setSuffixText(String.valueOf(currentGoal.getRemainingStepCount()));
        donutProgress.setFinishedStrokeWidth(7);
        donutProgress.setUnfinishedStrokeWidth(7);
        donutProgress.setFinishedStrokeColor(COLOR_GREEN);
        if(currentGoal.allSlotsExhausted()){
            donutProgress.setUnfinishedStrokeColor(COLOR_RED);
        }else {
            donutProgress.setUnfinishedStrokeColor(COLOR_GRAY);
        }
        //        donutProgress.setInnerBottomText("3");
//        donutProgress.setInnerBottomTextColor(getResources().getColor(R.color.white));
//        donutProgress.setMax(int max);
//        donutProgress.setPrefixText("prefix");
//        donutProgress.setPrefixText(String prefixText);
//        donutProgress.setInnerBackgroundColor(int innerBackgroundColor);
//        donutProgress.setInnerBottomText(String innerBottomText);
//        donutProgress.setInnerBottomTextSize(float innerBottomTextSize);
//        donutProgress.setInnerBottomTextColor(int innerBottomTextColor);
    }

//    public void setIsAddButton(boolean b) {
//        if(b){
//            isAddButton = b;
//            donutProgress.setProgress(100);
//            donutProgress.setTextColor(getResources().getColor(R.color.white));
//            donutProgress.setTextSize(40);
//            donutProgress.setSuffixText("+");
//            donutProgress.setFinishedStrokeWidth(7);
//            donutProgress.setUnfinishedStrokeWidth(7);
//            donutProgress.setFinishedStrokeColor(getResources().getColor(R.color.luminous_green));
//        }
//    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(context, ActGoalDetailsOld.class);
//        intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
//        context.startActivity(intent);

//        ActGoalDetails.navigate((AppCompatActivity) context, v.findViewById(R.id.toolBarActGoalDetailsNew), currentGoal);
        Intent intent = new Intent(context, ActGoalDetails.class);
        intent.putExtra(Constants.GOAL_OBJECT, currentGoal);
        context.startActivity(intent);
    }
}