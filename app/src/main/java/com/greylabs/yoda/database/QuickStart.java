package com.greylabs.yoda.database;

import android.content.Context;

import com.greylabs.yoda.enums.Daily;
import com.greylabs.yoda.enums.SubValue;
import com.greylabs.yoda.enums.TimeBoxOn;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.enums.WeekDay;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public final class QuickStart {

    private final static String TAG="QuickStart";
    //static int rowsAdded=0;
    ArrayList<Long> timeBoxIds;
    ArrayList<Long> goalIds;
    private Context context;
    public QuickStart(Context context){
        this.context=context;
        timeBoxIds=new ArrayList<>();
        goalIds=new ArrayList<>();
    }
    public void quickStart(){
        addDefaultTimeBoxes();
        addDefaultsGoals();
        addDefaultSteps();
    }
    private void addDefaultTimeBoxes(){
        TimeBox timeBox;
        Set<TimeBoxWhen> timeBoxWhens;
        Set<SubValue> timeBoxOns;
        //1
        timeBox=new TimeBox(context);
        timeBox.setNickName("Daily Early Morning Forever");
        ////set when
        com.greylabs.yoda.models.TimeBoxWhen timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.EARLY_MORNING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        ////set on
        com.greylabs.yoda.models.TimeBoxOn timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.DAILY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(Daily.DAILY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //set till
        timeBox.setTillType(TimeBoxTill.FOREVER);
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");

        //2
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekday Afternoons till this year");
       //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(WeekDay.MONDAY);
        timeBoxOns.add(WeekDay.TUESDAY);
        timeBoxOns.add(WeekDay.WEDNESDAY);
        timeBoxOns.add(WeekDay.THURSDAY);
        timeBoxOns.add(WeekDay.FRIDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.AFTERNOON);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");

        //3
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekday Evenings Forever");
        //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(WeekDay.SATURDAY);
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.EVENING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");

        //4
        timeBox=new TimeBox(context);
        timeBox.setNickName("Sunday Morning Forever");
        //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.MORNING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.FOREVER);
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");

        //5
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekend Afternoon Forever");
       //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.DAILY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(Daily.DAILY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.EVENING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");

        //6
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekend Evenings ");
        //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.EVENING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.QUARTER);
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");

        //7
        timeBox=new TimeBox(context);
        timeBox.setNickName("Week Nights");
        //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(WeekDay.MONDAY);
        timeBoxOns.add(WeekDay.TUESDAY);
        timeBoxOns.add(WeekDay.WEDNESDAY);
        timeBoxOns.add(WeekDay.THURSDAY);
        timeBoxOns.add(WeekDay.FRIDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.LATE_NIGHT);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");

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
        timeBox.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setUnplannedTimeBoxId(timeBox.getId());
        timeBoxIds.add(timeBox.getId());
        Logger.log(TAG, "1 Added");
    }

    private void addDefaultsGoals(){
        Goal goal;

        //1
        goal=new Goal(context);
        goal.setNickName("Health");
        goal.setObjective("Stay Healthy");
        goal.setKeyResult("Weight under 70Kg");
        goal.setTimeBoxId(timeBoxIds.get(0));
        goal.save();
        goalIds.add(goal.getId());


        //2
        goal=new Goal(context);
        goal.setNickName("Wealth");
        goal.setObjective("Make more money");
        goal.setKeyResult("Increase in income stream");
        goal.setTimeBoxId(timeBoxIds.get(1));
        goal.save();
        goalIds.add(goal.getId());


        //3
        goal=new Goal(context);
        goal.setNickName("Family");
        goal.setObjective("Stay with Family");
        goal.setKeyResult("Happy Life");
        goal.setTimeBoxId(timeBoxIds.get(2));
        goal.save();
        goalIds.add(goal.getId());


        //4
        goal=new Goal(context);
        goal.setNickName("Distress");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(3));
        goal.save();
        goalIds.add(goal.getId());

        //5
        goal=new Goal(context);
        goal.setNickName("Wise1");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(4));
        goal.save();
        goalIds.add(goal.getId());

        //6
        goal=new Goal(context);
        goal.setNickName("Socialize");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(5));
        goal.save();
        goalIds.add(goal.getId());

        //7
        goal=new Goal(context);
        goal.setNickName("Wise2");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(6));
        goal.save();
        goalIds.add(goal.getId());

        //8
        goal=new Goal(context);
        goal.setNickName(Constants.NICKNAME_STRETCH_GOAL);
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(7));
        goal.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setStretchGoalId(goal.getId());
        goalIds.add(goal.getId());
    }

    private void addDefaultSteps(){
        PendingStep pendingStep;

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Swim 30 mins");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(0));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Oatmeal breakfast");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(0));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Make 5 calls");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Write blog article");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Follow up on claim");
        pendingStep.setPriority(3);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Finish");
        pendingStep.setPriority(4);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();


        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Play with kids");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Cook with spouse");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Skype with parents");
        pendingStep.setPriority(3);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Pay weekly bills");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(3));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Clean out inbox");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(3));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Play tennis");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(4));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Dinner with friends");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(5));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Read book");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(6));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.save();
    }
}
