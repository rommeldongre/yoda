package com.greylabs.yoda.utils;

import android.content.Context;

import com.greylabs.yoda.database.MetaData.TableSlot;
import com.greylabs.yoda.enums.StepFilterType;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jaybhay Vijay on 7/23/2015.
 */
public class FilterUtility {

    private Context context;
    public FilterUtility(Context context) {
        this.context = context;
    }

    public Map<Goal, List<PendingStep>> getPendingSteps(StepFilterType stepFilterType){
        String criteria="";
        String startDate="";
        String endDate="";
        Map<Goal, List<PendingStep>> filteredSteps=new HashMap<>();
        Calendar cal=Calendar.getInstance();
        switch (stepFilterType){
            case TODAY:
                startDate=CalendarUtils.getSqLiteDateFormat(cal);
                criteria=" and "+ TableSlot.scheduleDate+" = "+startDate;
                break;
            case THIS_WEEK:
                startDate=CalendarUtils.getSqLiteDateFormat(cal);
                cal.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY-cal.get(Calendar.DAY_OF_WEEK));
                endDate=CalendarUtils.getSqLiteDateFormat(cal);
                criteria=" and ( "+TableSlot.scheduleDate+ ">= "+startDate+
                        " and "+TableSlot.scheduleDate+" <= "+endDate+" )";
                break;
            case THIS_MONTH:
                startDate=CalendarUtils.getSqLiteDateFormat(cal);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate=CalendarUtils.getSqLiteDateFormat(cal);
                criteria=" and ( "+TableSlot.scheduleDate+ ">= "+startDate+
                        " and "+TableSlot.scheduleDate+" <= "+endDate+" )";

                break;
            case THIS_QUARTER:
                startDate=CalendarUtils.getSqLiteDateFormat(cal);
                cal.set(Calendar.MONTH, CalendarUtils.getLastMonthOfQuarter(cal.get(Calendar.MONTH)));
                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate=CalendarUtils.getSqLiteDateFormat(cal);
                criteria=" and ( "+TableSlot.scheduleDate+ ">= "+startDate+
                        " and "+TableSlot.scheduleDate+" <= "+endDate+" )";
                break;
            case THIS_YEAR:
                startDate=CalendarUtils.getSqLiteDateFormat(cal);
                cal.set(Calendar.MONTH, Calendar.DECEMBER);
                cal.set(Calendar.DAY_OF_MONTH,31);
                endDate=CalendarUtils.getSqLiteDateFormat(cal);
                criteria=" and ( "+TableSlot.scheduleDate+ ">= "+startDate+
                        " and "+TableSlot.scheduleDate+" <= "+endDate+" )";
                break;
        }

        PendingStep pendingStep=new PendingStep(context);
        List<PendingStep> pendingSteps=pendingStep.getAll(criteria);
        List<Long> goalIds=new ArrayList<>();
        for (PendingStep ps:pendingSteps){
             long goalId=ps.getGoalId();
             boolean isPresent=false;
             for(long gid:goalIds){
                 if(gid==goalId) {
                     isPresent=true;
                     break;
                 }
             }
            if (isPresent==false) {
                goalIds.add(goalId);
            }
        }
        //for each unique goal id create entry in hash map
        for(long goalId:goalIds){
            Goal goal=new Goal(context).get(goalId);
            List<PendingStep> goalSteps=new ArrayList<>();
            for(PendingStep ps: pendingSteps){
                if(ps.getGoalId()==goalId)
                    goalSteps.add(ps);
            }
            filteredSteps.put(goal,goalSteps);
        }

        return  filteredSteps;
    }

}
