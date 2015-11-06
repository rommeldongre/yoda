package com.greylabs.yoda.database;

import android.content.Context;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.R;
import com.greylabs.yoda.enums.AccountType;
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
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
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
        Prefs prefs=Prefs.getInstance(context);
        prefs.setDefaultAccountEmailId("");
        prefs.setDefaultAccountType(AccountType.LOCAL.ordinal());
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
        timeBox.setNickName("Early Morning-Daily till Forever");
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
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.colorcode_red)));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "1 Added");

        //2
        timeBox=new TimeBox(context);
        timeBox.setNickName("Evening-Weekly-Monday,Tuesday,Wednesday,Thursday,Friday till this year");
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
        timeBoxWhens.add(TimeBoxWhen.EVENING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.colorcode_blue)));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "1 Added");

        //3
        timeBox=new TimeBox(context);
        timeBox.setNickName("Morning-Weekly-Sunday till this Quarter");
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
        timeBox.setTillType(TimeBoxTill.QUARTER);
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.colorcode_green)));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "1 Added");

        //4
        timeBox=new TimeBox(context);
        timeBox.setNickName("Evening-Weekly-Sunday,Saturday till this Quarter");
        //on
        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOns.add(WeekDay.SATURDAY);
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
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.colorcode_yellow)));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "1 Added");

        //5
        timeBox=new TimeBox(context);
        timeBox.setNickName("Late Night-Weekly-Monday,Tuesday,Wednesday,Thursday,Friday till this Month");
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
        timeBox.setTillType(TimeBoxTill.MONTH);
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.colorcode_orange)));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "1 Added");

//        //6
//        timeBox=new TimeBox(context);
//        timeBox.setNickName("Evening-Weekly-Sunday,Saturday till this Quarter");
//        //on
//        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
//        timeBoxOns=new TreeSet<>();
//        timeBoxOns.add(WeekDay.SUNDAY);
//        timeBoxOns.add(WeekDay.SATURDAY);
//        timeBoxOn.setSubValues(timeBoxOns);
//        timeBox.setTimeBoxOn(timeBoxOn);
//        //when
//        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
//        timeBoxWhens=new TreeSet<>();
//        timeBoxWhens.add(TimeBoxWhen.EVENING);
//        timeBoxWhen.setWhenValues(timeBoxWhens);
//        timeBox.setTimeBoxWhen(timeBoxWhen);
//        //till
//        timeBox.setTillType(TimeBoxTill.QUARTER);
//        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_6));
//        timeBox.save();
//        timeBoxIds.add(timeBox.getId());
//        Logger.log(TAG, "1 Added");
//
//        //7
//        timeBox=new TimeBox(context);
//        timeBox.setNickName("Night-Weekly-Monday,Tuesday,Wednesday,Thursday,Friday till Forever");
//        //on
//        timeBoxOn=new com.greylabs.yoda.models.TimeBoxOn(context,TimeBoxOn.WEEKLY);
//        timeBoxOns=new TreeSet<>();
//        timeBoxOns.add(WeekDay.MONDAY);
//        timeBoxOns.add(WeekDay.TUESDAY);
//        timeBoxOns.add(WeekDay.WEDNESDAY);
//        timeBoxOns.add(WeekDay.THURSDAY);
//        timeBoxOns.add(WeekDay.FRIDAY);
//        timeBoxOn.setSubValues(timeBoxOns);
//        timeBox.setTimeBoxOn(timeBoxOn);
//        //when
//        timeBoxWhen=new com.greylabs.yoda.models.TimeBoxWhen(context);
//        timeBoxWhens=new TreeSet<>();
//        timeBoxWhens.add(TimeBoxWhen.NIGHT);
//        timeBoxWhen.setWhenValues(timeBoxWhens);
//        timeBox.setTimeBoxWhen(timeBoxWhen);
//        //till
//        timeBox.setTillType(TimeBoxTill.FOREVER);
//        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_7));
//        timeBox.save();
//        timeBoxIds.add(timeBox.getId());
//        Logger.log(TAG, "1 Added");

        //6
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
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.colorcode_black)));
        timeBox.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setUnplannedTimeBoxId(timeBox.getId());
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "1 Added");
    }

    private void addDefaultsGoals(){
        Prefs prefs=Prefs.getInstance(context);
        Goal goal;
        //1
        goal=new Goal(context);
        goal.setNickName("Health");
        goal.setObjective("Stay Healthy");
        goal.setKeyResult("Weight under 70Kg");
        goal.setTimeBoxId(timeBoxIds.get(0));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());

        //2
        goal=new Goal(context);
        goal.setNickName("Family");
        goal.setObjective("Stay with Family");
        goal.setKeyResult("Happy Life");
        goal.setTimeBoxId(timeBoxIds.get(1));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());


        //3
        goal=new Goal(context);
        goal.setNickName("Organize");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(2));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());


        //4
        goal=new Goal(context);
        goal.setNickName("Socialize");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(3));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());

        //5
        goal=new Goal(context);
        goal.setNickName("Wise");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(4));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());

        //6
        goal=new Goal(context);
        goal.setNickName(Constants.NICKNAME_STRETCH_GOAL);
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(5));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.setStringId("@default");
        goal.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setStretchGoalId(goal.getId());
        goalIds.add(goal.getId());
    }

    private void addDefaultSteps(){
        PendingStep pendingStep;

        //steps for goal 1
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Swim 30 mins");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(0));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date()));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Oatmeal breakfast");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(0));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();

//        pendingStep=new PendingStep(context);
//        pendingStep.setNickName("Make 5 calls");
//        pendingStep.setPriority(1);
//        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
//        pendingStep.setStepCount(1);
//        pendingStep.setTime(3);
//        pendingStep.setGoalId(goalIds.get(1));
//        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
//        pendingStep.save();
//        pendingStep=new PendingStep(context);
//        pendingStep.setNickName("Write blog article");
//        pendingStep.setPriority(2);
//        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
//        pendingStep.setStepCount(1);
//        pendingStep.setTime(3);
//        pendingStep.setGoalId(goalIds.get(1));
//        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
//        pendingStep.save();
//        pendingStep=new PendingStep(context);
//        pendingStep.setNickName("Follow up on claim");
//        pendingStep.setPriority(3);
//        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
//        pendingStep.setStepCount(1);
//        pendingStep.setTime(3);
//        pendingStep.setGoalId(goalIds.get(1));
//        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
//        pendingStep.save();
//        pendingStep=new PendingStep(context);
//        pendingStep.setNickName("Finish");
//        pendingStep.setPriority(4);
//        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
//        pendingStep.setStepCount(1);
//        pendingStep.setTime(3);
//        pendingStep.setGoalId(goalIds.get(1));
//        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
//        pendingStep.save();

        //steps for goal 2
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Play with kids");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date()));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Cook with spouse");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Skype with parents");
        pendingStep.setPriority(3);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();

        //steps for goal 3
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Pay weekly bills");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Clean out inbox");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();

//        pendingStep=new PendingStep(context);
//        pendingStep.setNickName("Play tennis");
//        pendingStep.setPriority(1);
//        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
//        pendingStep.setStepCount(1);
//        pendingStep.setTime(3);
//        pendingStep.setGoalId(goalIds.get(4));
//        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
//        pendingStep.save();

        //steps for goal 4
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Dinner with friends");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(3));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();

        //steps for goal 5
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Read book");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(4));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();

        //steps for goal 6
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Get Cable");
        pendingStep.setPriority(1);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(5));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Fix Piano");
        pendingStep.setPriority(2);
        pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(5));
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setDeleted(false);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
        pendingStep.save();
    }
}