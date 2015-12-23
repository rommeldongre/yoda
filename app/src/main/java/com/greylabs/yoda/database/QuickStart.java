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
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public final class QuickStart {

    private final static String TAG = "QuickStart";
    //static int rowsAdded=0;
    ArrayList<Long> timeBoxIds;
    ArrayList<Long> goalIds;
    private Context context;

    public QuickStart(Context context) {
        this.context = context;
        timeBoxIds = new ArrayList<>();
        goalIds = new ArrayList<>();
    }

    public void quickStart() {
        Prefs prefs = Prefs.getInstance(context);
        prefs.setDefaultAccountEmailId("");
        prefs.setDefaultAccountType(AccountType.LOCAL.ordinal());
        addDefaultTimeBoxes();
        addDefaultsGoals();
        addDefaultSteps();
    }

    private void addDefaultTimeBoxes() {
        TimeBox timeBox;
        Set<TimeBoxWhen> timeBoxWhens;
        Set<SubValue> timeBoxOns;

        //1
        timeBox = new TimeBox(context);
        timeBox.setNickName("Early Morning-Daily till this year");
        ////set when
        com.greylabs.yoda.models.TimeBoxWhen timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.EARLY_MORNING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        ////set on
        com.greylabs.yoda.models.TimeBoxOn timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.DAILY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(Daily.DAILY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //set till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_1));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 1 Added");

        //2
        timeBox = new TimeBox(context);
        timeBox.setNickName("Daily evening, till this year");
        //on
        timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.DAILY);
        timeBoxOns = new TreeSet<>();
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.EVENING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_2));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 2 Added");


        //3
        timeBox = new TimeBox(context);
        timeBox.setNickName("Week Nights, till this year");
        //on
        timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.WEEKLY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(WeekDay.MONDAY);
        timeBoxOns.add(WeekDay.TUESDAY);
        timeBoxOns.add(WeekDay.WEDNESDAY);
        timeBoxOns.add(WeekDay.THURSDAY);
        timeBoxOns.add(WeekDay.FRIDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.MORNING);
        timeBoxWhens.add(TimeBoxWhen.AFTERNOON);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_3));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 3 Added");

        //4 Wise
        timeBox = new TimeBox(context);
        timeBox.setNickName("Week nights till this year");
        //on
        timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.WEEKLY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(WeekDay.MONDAY);
        timeBoxOns.add(WeekDay.TUESDAY);
        timeBoxOns.add(WeekDay.WEDNESDAY);
        timeBoxOns.add(WeekDay.THURSDAY);
        timeBoxOns.add(WeekDay.FRIDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.NIGHT);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_4));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 4 Added");

        //5 Social
        timeBox = new TimeBox(context);
        timeBox.setNickName("Weekend Nights till this year");
        //on
        timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.WEEKLY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(WeekDay.SATURDAY);
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.LATE_NIGHT);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_5));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 5 Added");

        //6 Housekeeping
        timeBox = new TimeBox(context);
        timeBox.setNickName("Weekend, Afternoon");
        //on
        timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.WEEKLY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOns.add(WeekDay.SATURDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.AFTERNOON);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.FOREVER);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_6));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 6 Added");

        //7 Hobby
        timeBox = new TimeBox(context);
        timeBox.setNickName("Weekend Mornings");
        //on
        timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.WEEKLY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOns.add(WeekDay.SATURDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.MORNING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.YEAR);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_7));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 7 Added");

        //Stretch Goal
        timeBox = new TimeBox(context);
        timeBox.setNickName(Constants.NICKNAME_UNPLANNED_TIMEBOX);
        //on
        timeBoxOn = new com.greylabs.yoda.models.TimeBoxOn(context, TimeBoxOn.DAILY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(Daily.DAILY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        timeBoxWhen = new com.greylabs.yoda.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        timeBox.setTillType(TimeBoxTill.FOREVER);
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.colorcode_black)));
        timeBox.save();
        Prefs pref = Prefs.getInstance(context);
        pref.setUnplannedTimeBoxId(timeBox.getId());
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox for Stretch Added");
    }

    private void addDefaultsGoals() {
        Prefs prefs = Prefs.getInstance(context);
        Goal goal;

        //1 Health
        goal = new Goal(context);
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
        Logger.d(TAG, "Goal 1 added");

        //2 Family
        goal = new Goal(context);
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
        Logger.d(TAG, "Goal 2 added");


        //3 Career
        goal = new Goal(context);
        goal.setNickName("Career");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(2));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());
        Logger.d(TAG, "Goal 3 added");

        //4 Wise
        goal = new Goal(context);
        goal.setNickName("Wise");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(3));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());
        Logger.d(TAG, "Goal 4 added");

        //5 Social
        goal = new Goal(context);
        goal.setNickName("Social");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(4));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());
        Logger.d(TAG, "Goal 5 added");

        //6 Housekeeping
        goal = new Goal(context);
        goal.setNickName("Organize");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(5));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());
        Logger.d(TAG, "Goal 6 added");

        //7 Hobby
        goal = new Goal(context);
        goal.setNickName("Hobby");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(6));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());
        Logger.d(TAG, "Goal 7 added");

        //Stretch Goal
        goal = new Goal(context);
        goal.setNickName(Constants.NICKNAME_STRETCH_GOAL);
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(7));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.setStringId("@default");
        goal.save();
        Prefs pref = Prefs.getInstance(context);
        pref.setStretchGoalId(goal.getId());
        goalIds.add(goal.getId());
        Logger.d(TAG, "Stretch Goal added");
    }

    private void addDefaultSteps() {

        //Step for Goal 1:
        createStep(0,
                "Run",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                30,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 1 added");

        //Step for Goal 2:
        createStep(1,
                "Play with kids",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                20,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 2 added");

        //Step for Goal 3:
        //no step

        //Step for Goal 4:
        createStep(3,
                "Read a book",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                20,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 4 added");

        //Step for Goal 5:
        createStep(4,
                "Dinner with friends",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                10,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 5 added");

        //Step for Goal 6:
        createStep(5,
                "Pay bills",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                10,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 6 added");

        //Step for Goal 7:
        createStep(6,
                "Golf",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                10,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 7 added");
    }



    private void createStep(int goalIdIndex,
                                  String nickname,
                                  PendingStep.PendingStepType pendingStepType,
                                  PendingStep.PendingStepExpire expire,
                                  int stepTime,
                                  int pendingStepCount,
                                  String priority){
        Goal currentGoal = new Goal(context).get(goalIds.get(goalIdIndex));

        PendingStep pendingStep = new PendingStep(context);
        pendingStep.setNickName(nickname);
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.setExpire(expire);

        if (pendingStepType.equals(Constants.PENDING_STEP_TYPE_SINGLE_STEP)) {
            if (stepTime > 3) {
                pendingStep.setPendingStepType(PendingStep.PendingStepType.SPLIT_STEP);
                pendingStep.setStepCount(stepTime / 3);
                if (stepTime % 3 >= 1)
                    pendingStep.setStepCount(pendingStep.getStepCount() + 1);
            } else {
                pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
                pendingStep.setStepCount(1);
            }
            pendingStep.setTime(stepTime);
        } else {
            pendingStep.setPendingStepType(PendingStep.PendingStepType.SERIES_STEP);
            pendingStep.setTime(stepTime);
            pendingStep.setStepCount(pendingStepCount);
        }

        pendingStep.setGoalId(currentGoal.getId());
        if (pendingStep.getStringId() == null || pendingStep.getStringId().equals(""))
            pendingStep.setGoalStringId(currentGoal.getStringId());

        TimeBox timeBox = new TimeBox(context);
        timeBox = timeBox.get(currentGoal.getTimeBoxId());
        YodaCalendar yodaCalendar = new YodaCalendar(context, timeBox);

        Slot slot = new Slot(context);
        int stepCount = 0;
        if (pendingStep.getId() == 0) {
            //stepTime=currentStep.getStepCount()*currentStep.getTime();
            stepCount = pendingStep.getStepCount();
        }

        int substeps = 0;

        List<PendingStep> subStepsList = new ArrayList<>();
        pendingStep.initDatabase(context);
        PendingStep ps = pendingStep;
        ps.save();
        switch (ps.getPendingStepType()) {
            case SPLIT_STEP:
                if (ps.getTime() > Constants.MAX_SLOT_DURATION) {
                    float numberOfSteps = (float) ps.getTime() / Constants.MAX_SLOT_DURATION;
                    Float f = new Float(numberOfSteps);
                    ps.createSubSteps(1, f.intValue(), Constants.MAX_SLOT_DURATION);
                    if (numberOfSteps - f.intValue() > 0.0f)
                        ps.createSubSteps(f.intValue() + 1, f.intValue() + 1, pendingStep.getTime() % Constants.MAX_SLOT_DURATION);
                }
                subStepsList = pendingStep.getAllSubSteps(pendingStep.getId(), currentGoal.getId());
                break;

            case SERIES_STEP:
                ps.createSubSteps(1, pendingStep.getStepCount(), pendingStep.getTime());
                subStepsList = pendingStep.getAllSubSteps(pendingStep.getId(), currentGoal.getId());
                break;

            case SUB_STEP:
            case SINGLE_STEP:
                subStepsList.add(pendingStep);
        }

        ArrayList<PendingStep> stepArrayList = new ArrayList<>();


        if (subStepsList != null) {
            if ( priority != null && priority.equals(Constants.PENDING_STEP_PRIORITY_TOP_MOST)) {
                stepArrayList.addAll(0, subStepsList);
            } else if ( priority != null && priority.equals(Constants.PENDING_STEP_PRIORITY_BOTTOM_MOST)) {
                stepArrayList.addAll(subStepsList);
            }

            //}
            //save all the steps in the array with priorities
            for (int i = 0; i < stepArrayList.size(); i++) {
                stepArrayList.get(i).initDatabase(context);
                stepArrayList.get(i).setPriority(i + 1);
                stepArrayList.get(i).setUpdated(new DateTime(new Date()));
                stepArrayList.get(i).save();
                //stepArrayList.get(i).updateSubSteps();
            }
            pendingStep.save();
        }





    }
}