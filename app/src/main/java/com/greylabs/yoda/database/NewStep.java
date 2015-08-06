package com.greylabs.yoda.database;

import android.content.Context;

import com.greylabs.yoda.enums.Daily;
import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.TimeBoxOn;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public final class NewStep {

    private final static String TAG="NewStep";
    //static int rowsAdded=0;
    ArrayList<Long> timeBoxIds;
    ArrayList<Long> goalIds;
    private Context context;
    public NewStep(Context context){
        this.context=context;
        timeBoxIds=new ArrayList<>();
        goalIds=new ArrayList<>();
    }
    public void newStep(){
        addDefaultTimeBoxes();
        addDefaultsGoals();
    }
    private void addDefaultTimeBoxes(){
        //unplanned timebox
        TimeBox timeBox;
        Set<TimeBoxWhen> timeBoxWhens;
        Set<SubValue> timeBoxOns;
        com.greylabs.yoda.models.TimeBoxWhen timeBoxWhen;
        com.greylabs.yoda.models.TimeBoxOn timeBoxOn;

        //8
        timeBox=new TimeBox(context);
        timeBox.setNickName(Constants.NICKNAME_UNPLANNED_TIMEBOX);
        //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.DAILY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(Daily.DAILY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        timeBox.setTillType(TimeBoxTill.FOREVER);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_UNPLANNED_TIMEBOX));
        timeBox.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setUnplannedTimeBoxId(timeBox.getId());
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");
    }

    private void addDefaultsGoals(){
        //unplanned goal
        Goal goal;
        goal=new Goal(context);
        goal.setNickName(Constants.NICKNAME_STRETCH_GOAL);
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(0));
        goal.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setStretchGoalId(goal.getId());
        goalIds.add(goal.getId());
    }
}
