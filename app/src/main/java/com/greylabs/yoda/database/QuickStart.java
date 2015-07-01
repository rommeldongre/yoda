package com.greylabs.yoda.database;

import android.content.Context;
import com.greylabs.yoda.enums.TimeBoxOn;
import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.enums.WeekDay;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;

import java.util.ArrayList;

/**
 * Created by Jaybhay Vijay on 7/1/2015.
 */
public final class QuickStart {

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
    }

    private void addDefaultTimeBoxes(){
        TimeBox timeBox;
        ArrayList<Integer> timeBoxWhens;
        ArrayList<Integer> timeBoxOns;

        //1
        timeBox=new TimeBox(context);
        timeBox.setNickName("Daily Early Morning Forever");
        timeBox.setTimeBoxOnType(TimeBoxOn.getEnumToIntegerType(TimeBoxOn.DAILY));
        timeBox.setTimeBoxTillType(TimeBoxTill.getEnumToIntegerType(TimeBoxTill.FOREVER));
        timeBox.save();
        timeBoxWhens=new ArrayList<>();
        timeBoxWhens.add(TimeBoxWhen.getEnumToIntegerType(TimeBoxWhen.EARLY_MORNING));
        timeBox.saveTimeBoxWhen(timeBox.getId(), timeBoxWhens);
        timeBoxIds.add(timeBox.getId());

        //2
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekday Afternoons till this year");
        timeBox.setTimeBoxOnType(TimeBoxOn.getEnumToIntegerType(TimeBoxOn.WEEKLY));
        timeBox.setTimeBoxTillType(TimeBoxTill.getEnumToIntegerType(TimeBoxTill.YEAR));
        timeBox.save();

        timeBoxOns=new ArrayList<>();
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.MONDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.TUESDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.WEDNESDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.THURSDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.FRIDAY));
        timeBox.saveTimeBoxOn(timeBox.getId(), timeBoxOns);

        timeBoxWhens=new ArrayList<>();
        timeBoxWhens.add(TimeBoxWhen.getEnumToIntegerType(TimeBoxWhen.AFTERNOON));
        timeBox.saveTimeBoxWhen(timeBox.getId(), timeBoxWhens);
        timeBoxIds.add(timeBox.getId());

        //3
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekday Evenings Forever");
        timeBox.setTimeBoxOnType(TimeBoxOn.getEnumToIntegerType(TimeBoxOn.WEEKLY));
        timeBox.setTimeBoxTillType(TimeBoxTill.getEnumToIntegerType(TimeBoxTill.YEAR));
        timeBox.save();

        timeBoxOns=new ArrayList<>();
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.SATURDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.SUNDAY));
        timeBox.saveTimeBoxOn(timeBox.getId(), timeBoxOns);

        timeBoxWhens=new ArrayList<>();
        timeBoxWhens.add(TimeBoxWhen.getEnumToIntegerType(TimeBoxWhen.EVENING));
        timeBox.saveTimeBoxWhen(timeBox.getId(), timeBoxWhens);
        timeBoxIds.add(timeBox.getId());

        //4
        timeBox=new TimeBox(context);
        timeBox.setNickName("Sunday Morning Forever");
        timeBox.setTimeBoxOnType(TimeBoxOn.getEnumToIntegerType(TimeBoxOn.WEEKLY));
        timeBox.setTimeBoxTillType(TimeBoxTill.getEnumToIntegerType(TimeBoxTill.FOREVER));
        timeBox.save();

        timeBoxOns=new ArrayList<>();
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.SUNDAY));
        timeBox.saveTimeBoxOn(timeBox.getId(), timeBoxOns);

        timeBoxWhens=new ArrayList<>();
        timeBoxWhens.add(TimeBoxWhen.getEnumToIntegerType(TimeBoxWhen.MORNING));
        timeBox.saveTimeBoxWhen(timeBox.getId(), timeBoxWhens);
        timeBoxIds.add(timeBox.getId());


        //5
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekend Afternoon Forever");
        timeBox.setTimeBoxOnType(TimeBoxOn.getEnumToIntegerType(TimeBoxOn.DAILY));
        timeBox.setTimeBoxTillType(TimeBoxTill.getEnumToIntegerType(TimeBoxTill.YEAR));
        timeBox.save();

        timeBoxWhens=new ArrayList<>();
        timeBoxWhens.add(TimeBoxWhen.getEnumToIntegerType(TimeBoxWhen.EVENING));
        timeBox.saveTimeBoxWhen(timeBox.getId(), timeBoxWhens);
        timeBoxIds.add(timeBox.getId());

        //6
        timeBox=new TimeBox(context);
        timeBox.setNickName("Weekend Evenings ");
        timeBox.setTimeBoxOnType(TimeBoxOn.getEnumToIntegerType(TimeBoxOn.MONTHLY));
        timeBox.setTimeBoxTillType(TimeBoxTill.getEnumToIntegerType(TimeBoxTill.QUARTER));
        timeBox.save();

        timeBoxOns=new ArrayList<>();
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.SUNDAY));
        timeBox.saveTimeBoxOn(timeBox.getId(), timeBoxOns);

        timeBoxWhens=new ArrayList<>();
        timeBoxWhens.add(TimeBoxWhen.getEnumToIntegerType(TimeBoxWhen.EVENING));
        timeBox.saveTimeBoxWhen(timeBox.getId(), timeBoxWhens);
        timeBoxIds.add(timeBox.getId());

        //7
        timeBox=new TimeBox(context);
        timeBox.setNickName("Week Nights");
        timeBox.setTimeBoxOnType(TimeBoxOn.getEnumToIntegerType(TimeBoxOn.WEEKLY));
        timeBox.setTimeBoxTillType(TimeBoxTill.getEnumToIntegerType(TimeBoxTill.YEAR));
        timeBox.save();

        timeBoxOns=new ArrayList<>();
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.MONDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.TUESDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.WEDNESDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.THURSDAY));
        timeBoxOns.add(WeekDay.getEnumToIntegerType(WeekDay.FRIDAY));
        timeBox.saveTimeBoxOn(timeBox.getId(), timeBoxOns);

        timeBoxWhens=new ArrayList<>();
        timeBoxWhens.add(TimeBoxWhen.getEnumToIntegerType(TimeBoxWhen.LATE_NIGHT));
        timeBox.saveTimeBoxWhen(timeBox.getId(), timeBoxWhens);
        timeBoxIds.add(timeBox.getId());

        //8
        timeBox=new TimeBox(context);
        timeBox.setNickName("Unplanned");
        timeBox.save();
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



        goal=new Goal(context);
        goal.setNickName("Family");
        goal.setObjective("Stay with Family");
        goal.setKeyResult("Happy Life");
        goal.setTimeBoxId(timeBoxIds.get(2));
        goal.save();
        goalIds.add(goal.getId());


        goal=new Goal(context);
        goal.setNickName("Distress");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(3));
        goal.save();
        goalIds.add(goal.getId());


        goal=new Goal(context);
        goal.setNickName("Wise");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(4));
        goal.save();
        goalIds.add(goal.getId());


        goal=new Goal(context);
        goal.setNickName("Socialize");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(5));
        goal.save();
        goalIds.add(goal.getId());


        goal=new Goal(context);
        goal.setNickName("Wise");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(6));
        goal.save();
        goalIds.add(goal.getId());



    }

    private void addDefaultSteps(){
        PendingStep pendingStep;

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Swim 30 mins");
        pendingStep.setPriority(1);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(0));
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Oatmeal breakfast");
        pendingStep.setPriority(2);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(0));
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Make 5 calls");
        pendingStep.setPriority(1);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Write blog article");
        pendingStep.setPriority(2);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Follow up on claim");
        pendingStep.setPriority(3);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Finish");
        pendingStep.setPriority(4);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(1));
        pendingStep.save();


        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Play with kids");
        pendingStep.setPriority(1);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Cook with spouse");
        pendingStep.setPriority(2);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Skype with parents");
        pendingStep.setPriority(3);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(2));
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Pay weekly bills");
        pendingStep.setPriority(1);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(3));
        pendingStep.save();
        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Clean out inbox");
        pendingStep.setPriority(2);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(3));
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Play tennis");
        pendingStep.setPriority(1);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(4));
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Dinner with friends");
        pendingStep.setPriority(2);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(5));
        pendingStep.save();

        pendingStep=new PendingStep(context);
        pendingStep.setNickName("Read book");
        pendingStep.setPriority(1);
        pendingStep.setSeries(false);
        pendingStep.setStepCount(1);
        pendingStep.setTime(3);
        pendingStep.setGoalId(goalIds.get(6));
        pendingStep.save();



    }


}
