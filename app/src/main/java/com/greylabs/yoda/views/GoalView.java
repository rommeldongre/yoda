package com.greylabs.yoda.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.greylabs.yoda.R;

public class GoalView extends LinearLayout implements View.OnClickListener {

    private Context context;
    MyDonutProgress donutProgress;
    TextView tvGoalName;
    boolean isAddButton = false;


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

//    public WidgetTextFieldView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    this.context = context;
//        init();
//    }

    private void init() {

        inflate(getContext(), R.layout.view_goal, this);
        this.donutProgress = (MyDonutProgress) findViewById(R.id.donutProgressViewGoal);
        this.tvGoalName = (TextView)findViewById(R.id.tvViewGoal);
        tvGoalName.setText("Goal Name kldfldkjf asdasd");
        setDonutProgressValues();

        donutProgress.setOnClickListener(this);
    }

    private void setDonutProgressValues() {
        donutProgress.setProgress(75);
        donutProgress.setTextColor(getResources().getColor(R.color.white));
        donutProgress.setTextSize(25);
        donutProgress.setSuffixText("4");
        donutProgress.setFinishedStrokeWidth(7);
        donutProgress.setUnfinishedStrokeWidth(7);
        donutProgress.setFinishedStrokeColor(getResources().getColor(R.color.luminous_green));
        donutProgress.setUnfinishedStrokeColor(getResources().getColor(R.color.gray));
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

    public void setIsAddButton(boolean b) {
        if(b){
            isAddButton = b;
            donutProgress.setProgress(100);
            donutProgress.setTextColor(getResources().getColor(R.color.white));
            donutProgress.setTextSize(40);
            donutProgress.setSuffixText("+");
            donutProgress.setFinishedStrokeWidth(7);
            donutProgress.setUnfinishedStrokeWidth(7);
            donutProgress.setFinishedStrokeColor(getResources().getColor(R.color.luminous_green));
        }
    }

    @Override
    public void onClick(View v) {
        if(isAddButton){
            Toast.makeText(context, "add goal", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "hehe", Toast.LENGTH_SHORT).show();
        }
    }
}