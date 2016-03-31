package com.greylabs.ydo.database;

import android.content.Context;

import com.google.api.client.util.DateTime;
import com.greylabs.ydo.R;
import com.greylabs.ydo.enums.AccountType;
import com.greylabs.ydo.enums.Daily;
import com.greylabs.ydo.enums.SubValue;
import com.greylabs.ydo.enums.TimeBoxOn;
import com.greylabs.ydo.enums.TimeBoxTill;
import com.greylabs.ydo.enums.TimeBoxWhen;
import com.greylabs.ydo.enums.WeekDay;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.models.Slot;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.scheduler.YodaCalendar;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;

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

        //1 Health
        timeBox = new TimeBox(context);
        timeBox.setNickName("Early Morning-Daily till this year");
        ////set when
        com.greylabs.ydo.models.TimeBoxWhen timeBoxWhen = new com.greylabs.ydo.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.EARLY_MORNING);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        ////set on
        com.greylabs.ydo.models.TimeBoxOn timeBoxOn = new com.greylabs.ydo.models.TimeBoxOn(context, TimeBoxOn.DAILY);
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

        //2 Wise
        timeBox = new TimeBox(context);
        timeBox.setNickName("Week nights till this month");
        //on
        timeBoxOn = new com.greylabs.ydo.models.TimeBoxOn(context, TimeBoxOn.WEEKLY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(WeekDay.MONDAY);
        timeBoxOns.add(WeekDay.TUESDAY);
        timeBoxOns.add(WeekDay.WEDNESDAY);
        timeBoxOns.add(WeekDay.THURSDAY);
        timeBoxOns.add(WeekDay.FRIDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.ydo.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.NIGHT);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.MONTH);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_4));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 2 Added");

        //3 Organise
        timeBox = new TimeBox(context);
        timeBox.setNickName("Weekend, Afternoon");
        //on
        timeBoxOn = new com.greylabs.ydo.models.TimeBoxOn(context, TimeBoxOn.WEEKLY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(WeekDay.SUNDAY);
        timeBoxOns.add(WeekDay.SATURDAY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        //when
        timeBoxWhen = new com.greylabs.ydo.models.TimeBoxWhen(context);
        timeBoxWhens = new TreeSet<>();
        timeBoxWhens.add(TimeBoxWhen.AFTERNOON);
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        //till
        timeBox.setTillType(TimeBoxTill.FOREVER);
        timeBox.setColorCode(String.valueOf(Constants.COLORCODE_TIMEBOX_6));
        timeBox.save();
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "Timebox 3 Added");

        //Stretch Goal
        timeBox = new TimeBox(context);
        timeBox.setNickName(Constants.NICKNAME_UNPLANNED_TIMEBOX);
        //on
        timeBoxOn = new com.greylabs.ydo.models.TimeBoxOn(context, TimeBoxOn.DAILY);
        timeBoxOns = new TreeSet<>();
        timeBoxOns.add(Daily.DAILY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        timeBoxWhen = new com.greylabs.ydo.models.TimeBoxWhen(context);
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

        //2 Wise
        goal = new Goal(context);
        goal.setNickName("Wise");
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(1));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.save();
        goalIds.add(goal.getId());
        Logger.d(TAG, "Goal 2 added");

        //3 Organise
        goal = new Goal(context);
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
        Logger.d(TAG, "Goal 3 added");

        //Stretch Goal
        goal = new Goal(context);
        goal.setNickName(Constants.NICKNAME_STRETCH_GOAL);
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(3));
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
                "Read a book",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                10,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 2 added");

        //Step for Goal 3:
        createStep(2,
                "Pay bills",
                PendingStep.PendingStepType.SERIES_STEP,
                PendingStep.PendingStepExpire.NOT_EXPIRE,
                3,
                10,
                Constants.PENDING_STEP_PRIORITY_TOP_MOST);
        Logger.d(TAG, "Step 3 added");
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