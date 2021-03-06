package com.greylabs.ydo.utils;

import android.content.Context;

import com.greylabs.ydo.database.MetaData;
import com.greylabs.ydo.enums.StepFilterType;
import com.greylabs.ydo.enums.TimeBoxWhen;
import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.models.Slot;
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

    private String buildCriteria(StepFilterType stepFilterType){
        String criteria="";
        String startDate="";
        String endDate="";

        Calendar calStart;
        Calendar calEnd;
//        calStart.set(Calendar.HOUR_OF_DAY, 0);calStart.set(Calendar.MINUTE, 0);calStart.set(Calendar.SECOND, 0);
//        calStart.set(Calendar.MILLISECOND,0);
//
//        calEnd.set(Calendar.HOUR_OF_DAY, 0);calEnd.set(Calendar.MINUTE, 0);calEnd.set(Calendar.SECOND, 0);
//        calEnd.set(Calendar.MILLISECOND,0);
//        calStart.set(Calendar.HOUR_OF_DAY,TimeBoxWhen.EARLY_MORNING.getStartTime());
//        calEnd.set(Calendar.HOUR_OF_DAY, TimeBoxWhen.LATE_NIGHT.getStartTime());
//        calEnd.add(Calendar.MINUTE, 179);

        switch (stepFilterType){
            case TODAY:
                calStart=getStartCal();
                calEnd=getEndCal();
                startDate=CalendarUtils.getSqLiteDateFormat(calStart);
                endDate=CalendarUtils.getSqLiteDateFormat(calEnd);
                break;
            case THIS_WEEK:
                calStart=getStartCal();
                calEnd=getEndCal();
                calStart.add(Calendar.DAY_OF_WEEK,1);
                startDate=CalendarUtils.getSqLiteDateFormat(calStart);

                calEnd.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                endDate=CalendarUtils.getSqLiteDateFormat(calEnd);
                break;
            case THIS_MONTH:
                calStart=getStartCal();
                calEnd=getEndCal();
                calStart.add(Calendar.WEEK_OF_YEAR, 1);
                calStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                startDate=CalendarUtils.getSqLiteDateFormat(calStart);

                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate=CalendarUtils.getSqLiteDateFormat(calEnd);

                break;
            case THIS_QUARTER:
                calStart=getStartCal();
                calEnd=getEndCal();
                calStart.set(Calendar.DAY_OF_MONTH, calStart.getActualMaximum(Calendar.DAY_OF_MONTH));
                calStart.add(Calendar.DATE, 1);
                startDate=CalendarUtils.getSqLiteDateFormat(calStart);

                calEnd.set(Calendar.MONTH, CalendarUtils.getLastMonthOfQuarter(calEnd.get(Calendar.MONTH)));
                calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate=CalendarUtils.getSqLiteDateFormat(calEnd);
                break;
            case THIS_YEAR:
                calStart=getStartCal();
                calEnd=getEndCal();
                calStart.set(Calendar.MONTH, CalendarUtils.getLastMonthOfQuarter(calStart.get(Calendar.MONTH))+1);
                calStart.set(Calendar.DATE,1);
                startDate=CalendarUtils.getSqLiteDateFormat(calStart);

                calEnd.set(Calendar.MONTH, Calendar.DECEMBER);
                calEnd.set(Calendar.DAY_OF_MONTH,31);
                endDate=CalendarUtils.getSqLiteDateFormat(calEnd);
                break;
            case NEVER:

        }

        criteria=" and  ( "+ MetaData.TablePendingStep.stepDate+ ">= '"+startDate+"'"+
                " and "+MetaData.TablePendingStep.stepDate+" <= '"+endDate+"' )";
        return criteria;
    }
    private Calendar getStartCal(){
        Calendar calStart=Calendar.getInstance();
        calStart.setFirstDayOfWeek(Calendar.SUNDAY);
        calStart.set(Calendar.HOUR_OF_DAY, 0);calStart.set(Calendar.MINUTE, 0);calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND,0);
        calStart.set(Calendar.HOUR_OF_DAY,TimeBoxWhen.EARLY_MORNING.getStartTime());
        return calStart;
    }

    private Calendar getEndCal(){
        Calendar calEnd=Calendar.getInstance();
        calEnd.setFirstDayOfWeek(Calendar.SUNDAY);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);calEnd.set(Calendar.MINUTE, 0);calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);
        calEnd.set(Calendar.HOUR_OF_DAY, TimeBoxWhen.LATE_NIGHT.getStartTime());
        calEnd.add(Calendar.MINUTE, 179);
        return calEnd;
    }
    public Map<Long, List<PendingStep>> getPendingSteps(StepFilterType stepFilterType){
        Map<Long, List<PendingStep>> filteredSteps=new HashMap<>();
        PendingStep pendingStep=new PendingStep(context);
        List<PendingStep> pendingSteps=pendingStep.getAll(buildCriteria(stepFilterType));
        List<Long> goalIds=new ArrayList<>();
        if(pendingSteps!=null){
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
            Slot slot=new Slot(context);
            for(long goalId:goalIds){
                List<PendingStep> goalSteps=new ArrayList<>();
                for(PendingStep ps: pendingSteps){
                    if(ps.getGoalId()==goalId)
                        goalSteps.add(ps);
                }
                filteredSteps.put(goalId,goalSteps);
            }
        }
        return  filteredSteps;
    }

    public ArrayList<PendingStep> getPendingStepsArrayList(StepFilterType stepFilterType){

        if (stepFilterType == StepFilterType.DONE){
            ArrayList<PendingStep> pendingSteps= new PendingStep(context).getAllPendingStepsByStatus(PendingStep.PendingStepStatus.COMPLETED, MetaData.TablePendingStep.stepDate+" desc ");
            return  pendingSteps;
        }

        if (stepFilterType == StepFilterType.NEVER){
            ArrayList<PendingStep> pendingSteps= new PendingStep(context).getAllPendingStepsByStatus(PendingStep.PendingStepStatus.UNSCHEDULED, MetaData.TablePendingStep.priority + " asc ");
            return  pendingSteps;
        }

        ArrayList<PendingStep> pendingSteps= new PendingStep(context).getAllStepsArrayList(buildCriteria(stepFilterType));
        return  pendingSteps;
    }
}
