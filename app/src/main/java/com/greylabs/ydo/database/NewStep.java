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
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;

import java.util.ArrayList;
import java.util.Date;
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
        Prefs prefs=Prefs.getInstance(context);
        if(prefs.getDefaultAccountEmailId()==null)
            prefs.setDefaultAccountEmailId("");
        if(prefs.getDefaultAccountType()==0)
            prefs.setDefaultAccountType(AccountType.GOOGLE.ordinal());
        prefs.setDefaultAccountType(AccountType.LOCAL.ordinal());
        if(prefs.getStretchGoalId()==0) {
            addDefaultTimeBoxes();
            addDefaultsGoals();
        }
    }
    private void addDefaultTimeBoxes(){
        //unplanned timebox
        TimeBox timeBox;
        Set<TimeBoxWhen> timeBoxWhens;
        Set<SubValue> timeBoxOns;
        com.greylabs.ydo.models.TimeBoxWhen timeBoxWhen;
        com.greylabs.ydo.models.TimeBoxOn timeBoxOn;

        //8
        timeBox=new TimeBox(context);
        timeBox.setNickName(Constants.NICKNAME_UNPLANNED_TIMEBOX);
        //on
        timeBoxOn=new com.greylabs.ydo.models.TimeBoxOn(context,TimeBoxOn.DAILY);
        timeBoxOns=new TreeSet<>();
        timeBoxOns.add(Daily.DAILY);
        timeBoxOn.setSubValues(timeBoxOns);
        timeBox.setTimeBoxOn(timeBoxOn);
        timeBoxWhen=new com.greylabs.ydo.models.TimeBoxWhen(context);
        timeBoxWhens=new TreeSet<>();
        timeBoxWhen.setWhenValues(timeBoxWhens);
        timeBox.setTimeBoxWhen(timeBoxWhen);
        timeBox.setTillType(TimeBoxTill.FOREVER);
        timeBox.setColorCode(String.valueOf(context.getResources().getColor(R.color.black)));
        timeBox.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setUnplannedTimeBoxId(timeBox.getId());
        timeBoxIds.add(timeBox.getId());
        Logger.d(TAG, "1 Added");
    }

    private void addDefaultsGoals(){
        //unplanned goal
        Prefs prefs=Prefs.getInstance(context);
        Goal goal;
        goal=new Goal(context);
        goal.setNickName(Constants.NICKNAME_STRETCH_GOAL);
        goal.setObjective("");
        goal.setKeyResult("");
        goal.setTimeBoxId(timeBoxIds.get(0));
        goal.setDeleted(false);
        goal.setUpdated(new DateTime(new Date()));
        goal.setAccount(prefs.getDefaultAccountEmailId());
        goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
        goal.setStringId("@default");
        goal.save();
        Prefs pref=Prefs.getInstance(context);
        pref.setStretchGoalId(goal.getId());
        goalIds.add(goal.getId());
    }
}
