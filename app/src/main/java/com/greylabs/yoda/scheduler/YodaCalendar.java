package com.greylabs.yoda.scheduler;
import android.content.Context;

import com.greylabs.yoda.enums.TimeBoxTill;
import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.models.Day;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.utils.sorters.SortByDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Jaybhay Vijay on 7/15/2015.
 */
public class YodaCalendar {
    /**********************************************************************************************/
    //Instance variable
    /**********************************************************************************************/
    private static  final String TAG="YodaCalendar";
    private Context context;
    private Slot slot;
    private List<Slot> slots;
    private TimeBox timeBox;
    private Prefs prefs;
    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/


    /**********************************************************************************************/
    //Constructors
    /**********************************************************************************************/
    public YodaCalendar(Context context){
        this.context = context;
        this.slot=new Slot(context);
        this.prefs=Prefs.getInstance(context);
    }
    public YodaCalendar(Context context, TimeBox timeBox) {
        this(context);
        this.timeBox = timeBox;
        slots=slot.getAll(timeBox);
    }

    public void setTimeBox(TimeBox timeBox){
        this.timeBox=timeBox;
    }
    /**********************************************************************************************/
    //Methods
    /**********************************************************************************************/
    /**
     *This method initialize calendar. Calendar need to be initialized when app is first time installed
     * This method must be called when app is installed first time or device rebooted.
     *@return None
     */
    public static void  init(Context context){
        Prefs prefs=Prefs.getInstance(context);
        //Create calender and set todays date
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Logger.log(TAG, "Initializing calender from date:"+cal.getTime().toString());
        int maxDays=365;
        //Check for leap year condition
        Calendar yr=Calendar.getInstance();
        if(cal.get(Calendar.MONTH)>Calendar.FEBRUARY){
            //Feb of current year passed, check for next year Feb month
            Logger.log(TAG, "Feb of current month is passed.");
            yr.add(Calendar.YEAR, 1);
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }else{
            //Feb of next month is appearing in current year
            Logger.log(TAG, "Feb of current month is coming.");
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }
        if(yr.getActualMaximum(Calendar.DAY_OF_MONTH)==29) {
            maxDays++;
            Logger.log(TAG,"Detected Leap year. One more day added to maxDays");
        }
        Logger.log(TAG,"Maximum Days in Calender:"+maxDays);
        //Fill database
        Logger.log(TAG,"Filling entries in database.");
        int dayOfYear=1;
        int dayOfWeek=-1;
        int weekOfMonth=-1;
        int monthOfYear=-1;
        int quarterOfYear=-1;
        int year=-1;
        Day day=new Day(context);
        cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //SimpleDateFormat sqliteDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(dayOfYear=1;dayOfYear<=maxDays;dayOfYear++){
            dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
            weekOfMonth= CalendarUtils.getWeek(cal.get(Calendar.DATE));
            monthOfYear= cal.get(Calendar.MONTH);
            quarterOfYear=CalendarUtils.getQuarter(monthOfYear);
            year=cal.get(Calendar.YEAR);
            day.setId(0);

            day.setDate(cal.getTime());
            day.setDayOfYear(dayOfYear);
            day.setDayOfWeek(dayOfWeek);
            day.setWeekOfMonth(weekOfMonth);
            day.setMonthOfYear(monthOfYear);
            day.setQuarterOfYear(quarterOfYear);
            day.setYear(year);
            day.save();
            Slot slot=new Slot(context);
            for(int slotOfDay=0;slotOfDay<6;slotOfDay++){
                slot.setId(0);
                slot.setWhen(TimeBoxWhen.getIntegerToEnumType(slotOfDay));
                slot.setTime(Constants.MAX_SLOT_DURATION);
                slot.setScheduleDate(day.getDate());
                slot.setGoalId(prefs.getStretchGoalId());
                slot.setTimeBoxId(prefs.getUnplannedTimeBoxId());
                slot.setDayId(day.getId());
                slot.save();
                Logger.log(TAG, " " + dayOfYear + "Day and Slot:" + slot.toString() + "|||" + day.toString());
            }
            cal.add(Calendar.DATE,1);
        }

    }


    private Calendar getCalendarInstance(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
    private int getMaxDays(Calendar cal){
        int maxDays=365;
        //Check for leap year condition
        Calendar yr=Calendar.getInstance();
        if(cal.get(Calendar.MONTH)>Calendar.FEBRUARY){
            //Feb of current year passed, check for next year Feb month
            Logger.log(TAG, "Feb of current month is passed.");
            yr.add(Calendar.YEAR, 1);
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }else{
            //Feb of next month is appearing in current year
            Logger.log(TAG, "Feb of current month is coming.");
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }
        if(yr.getActualMaximum(Calendar.DAY_OF_MONTH)==29) {
            maxDays++;
            Logger.log(TAG,"Detected Leap year. One more day added to maxDays");
        }
        Logger.log(TAG,"Maximum Days in Calender:"+maxDays);
        return maxDays;
    }
    public void updateCalendar(){
        //Create calender and set todays date
        Calendar cal=getCalendarInstance();
        Logger.log(TAG, "Updating calender from date:"+cal.getTime().toString());
        //update database
        Logger.log(TAG,"Updating entries in database.");
        int dayOfYear=1;
        int dayOfWeek=-1;
        int weekOfMonth=-1;
        int monthOfYear=-1;
        int quarterOfYear=-1;
        int year=-1;

        Day day=new Day(context);
        cal=Calendar.getInstance();Calendar cal1=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);cal1.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);cal1.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);cal1.set(Calendar.MILLISECOND, 0);

        //get all days of calendar
        List<Day> days=day.getAll();
        day=days.get(0);
        int daysDeleted=0;
        Calendar temp=Calendar.getInstance();
        if(day.getDate().compareTo(cal.getTime())>0){
            //first entry in the Calendar DB  is greater than  current
            String startDate=CalendarUtils.getSqLiteDateFormat(cal);
            cal1.setTime(day.getDate());
            String endDate=CalendarUtils.getSqLiteDateFormat(cal1);
            //daysDeleted=day.deleteNextDays(startDate, endDate);
            int numberOfDays=cal1.get(Calendar.DAY_OF_YEAR)-cal.get(Calendar.DAY_OF_YEAR);
            temp.setTime(cal.getTime());
            for(int i=1;i<=numberOfDays;i++){
                dayOfYear=i;
                dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
                weekOfMonth= CalendarUtils.getWeek(cal.get(Calendar.DATE));
                monthOfYear= cal.get(Calendar.MONTH);
                quarterOfYear=CalendarUtils.getQuarter(monthOfYear);
                year=cal.get(Calendar.YEAR);

                day.setId(0);
                day.setDate(cal.getTime());
                day.setDayOfYear(dayOfYear);
                day.setDayOfWeek(dayOfWeek);
                day.setWeekOfMonth(weekOfMonth);
                day.setMonthOfYear(monthOfYear);
                day.setQuarterOfYear(quarterOfYear);
                day.setYear(year);
                day.save();
                slot=new Slot(context);
                for(int j=0;j<6;j++){
                    slot.setId(0);
                    slot.setWhen(TimeBoxWhen.getIntegerToEnumType(j));
                    slot.setTime(Constants.MAX_SLOT_DURATION);
                    slot.setScheduleDate(day.getDate());
                    slot.setGoalId(prefs.getStretchGoalId());
                    slot.setTimeBoxId(prefs.getUnplannedTimeBoxId());
                    slot.setDayId(day.getId());
                    slot.save();
                }
                int count=days.get(days.size()-i).delete();
                cal.add(Calendar.DATE, 1);
            }
        }
        else if(day.getDate().compareTo(cal.getTime())<0){
            //first entry in the Calendar DB  is less than  current
            cal1.setTime(day.getDate());
            String startDate=CalendarUtils.getSqLiteDateFormat(cal1);
            String endDate=CalendarUtils.getSqLiteDateFormat(cal);
            daysDeleted=day.deletePrevDays(startDate, endDate);
            int numberOfDays=cal.get(Calendar.DAY_OF_YEAR)-cal1.get(Calendar.DAY_OF_YEAR);
            cal.setTime(days.get(days.size() - 1).getDate());
            cal.add(Calendar.DATE, 1);
            temp.setTime(cal1.getTime());
            for(int i=1;i<=numberOfDays;i++){
                dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
                weekOfMonth= CalendarUtils.getWeek(cal.get(Calendar.DATE));
                monthOfYear= cal.get(Calendar.MONTH);
                quarterOfYear=CalendarUtils.getQuarter(monthOfYear);
                year=cal.get(Calendar.YEAR);

                day.setId(0);
                day.setDate(cal.getTime());
                day.setDayOfYear(dayOfYear);
                day.setDayOfWeek(dayOfWeek);
                day.setWeekOfMonth(weekOfMonth);
                day.setMonthOfYear(monthOfYear);
                day.setQuarterOfYear(quarterOfYear);
                day.setYear(year);
                day.save();
                slot=new Slot(context);
                for(int j=0;j<6;j++){
                    slot.setId(0);
                    slot.setWhen(TimeBoxWhen.getIntegerToEnumType(j));
                    slot.setTime(Constants.MAX_SLOT_DURATION);
                    slot.setScheduleDate(day.getDate());
                    slot.setGoalId(prefs.getStretchGoalId());
                    slot.setTimeBoxId(prefs.getUnplannedTimeBoxId());
                    slot.setDayId(day.getId());
                    slot.save();
                }
                cal.add(Calendar.DATE,1);
            }
        }

        //set dayOfYear
        int maxDaysOfYear=getMaxDays(cal);
        days=day.getAll();
        for(int i=1;i<=maxDaysOfYear;i++) {
            days.get(i-1).setDayOfYear(i);
            days.get(i-1).save();
        }
        //update for all timeboxes having forever as Till time-need optimization
        List<TimeBox> timeBoxes=new TimeBox(context).getAll(TimeBox.TimeBoxStatus.ACTIVE);
        Goal goal=new Goal(context);
        for(TimeBox timeBox:timeBoxes){
            if(timeBox.getTillType()== TimeBoxTill.FOREVER){
                this.timeBox=timeBox;
                attachTimeBox(goal.getGoalId(timeBox.getId()));
            }
        }
    }
    /**
     *This method attach timebox to goal only if timebox fits into calender slots.This maps timebox
     * actual Calendar.
     * @param goalId Id of goal to which this timebox is being attached
     * @return returns value greater than 0  if timebox does not conflict with the slots otherwise 0
     */
    public int attachTimeBox(long goalId){
        int slotCount=0;
        PendingStep pendingStep=new PendingStep(context);
        if(slots!=null && slots.size()>0) {
            for (Slot slot : slots) {
                //following checks that any Unplanned Slot is assigned to some step or not
                // If it is assigned then keep this slot as Unplanned TimeBox
                if( pendingStep.isSlotAssigned(slot.getId()))
                    continue;
                slot.setTimeBoxId(timeBox.getId());
                slot.setGoalId(goalId);
                slot.setTime(Constants.MAX_SLOT_DURATION);
                slot.save();
                slotCount++;
            }
        }
        return slotCount;
    }

    /**
     * This method frees the slots allocated to timebox with id= timeBoxId.It removes all actual mappings
     * corresponding to this timebox
     * @return returns value greater than 0  if timebox detached from goal successfully
     * otherwise 0 indicating that timebox was not attached previously .
     */
    public int detachTimeBox(long timeBoxId){
        List<Slot> slots=null;
        if(timeBoxId!=prefs.getUnplannedTimeBoxId())
            slots=slot.getAll(timeBoxId);
        int slotCount=0;
        if(slots!=null) {
            for (Slot slot : slots) {
                slot.setGoalId(prefs.getStretchGoalId());
                slot.setTimeBoxId(prefs.getUnplannedTimeBoxId());
                slot.setTime(Constants.MAX_SLOT_DURATION);
                slot.save();
                slotCount++;
            }
        }
        return slotCount;

    }

    /**
     * This method updates already attached timebox.Internally it also assign slot to the steps
     * based on their priority
     * @param goalId goalId to which this timebox is associated
     * @return returns value greater than 0  if timebox does not conflict and updated successfully
     * otherwise 0
     */
    public int updateTimeBox(long oldTimeBoxId ,long goalId){
        int slotCount=0;
        if(slots!=null){
            //above condition confirms that timebox does not conflicts with other
            //we need to detach first all slots that have been assigned to it to ensure that
            // slot is free.
            detachTimeBox(oldTimeBoxId);
            for (Slot slot:slots){
                slot.setTimeBoxId(timeBox.getId());
                slot.setGoalId(goalId);
                slot.save();
                slotCount++;
            }
            //change step slots below
        }
        return slotCount;
    }

    /**********************************************************************************************/
    // Step Scheduler
    /**********************************************************************************************/
    public boolean scheduleStep(PendingStep pendingStep) {
        boolean isScheduled=false;
        slots=slot.getAll(timeBox.getId());
        //Collections.sort(slots, new SortByDate());
        removeTodaysPassedSlots();
        switch (pendingStep.getPendingStepType()){
            case SPLIT_STEP:
            case SERIES_STEP:
                Iterator<PendingStep> pendingSteps = pendingStep.
                        getAllSubSteps(pendingStep.getId(), pendingStep.getGoalId()).iterator();
                while (pendingSteps.hasNext()) {
                    PendingStep ps=pendingSteps.next();
                    Iterator<Slot> it = slots.iterator();
                    while (it.hasNext()) {
                        Slot slot = it.next();
                        if (ps.getTime() <=slot.getTime() && slot.getTimeBoxId()==timeBox.getId()  ) {
                            slot.setTime(slot.getTime() - ps.getTime());
                            slot.setGoalId(ps.getGoalId());
                            ps.setSlotId(slot.getId());
                            slot.save();
                            ps.setStepDate(slot.getScheduleDate());
                            ps.save();
                            AlarmScheduler alarmScheduler = new AlarmScheduler(context);
                            alarmScheduler.setStepId(ps.getId());
                            alarmScheduler.setSubStepId(ps.getSubStepOf());
                            alarmScheduler.setPendingStepType(PendingStep.PendingStepType.SUB_STEP);
                            alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                            alarmScheduler.setDuration(ps.getTime());
                            alarmScheduler.setAlarmDate(slot.getScheduleDate());
                            alarmScheduler.cancel();
                            alarmScheduler.setAlarm();
                            isScheduled=true;
                            break;
                        }
                    }
                }
                break;
            case SINGLE_STEP:
                Iterator<Slot> it = slots.iterator();
                while (it.hasNext()) {
                    Slot slot = it.next();
                    if (pendingStep.getTime() <=slot.getTime() && slot.getTimeBoxId()==timeBox.getId()  ) {
                        slot.setTime(slot.getTime() - pendingStep.getTime());
                        slot.setGoalId(pendingStep.getGoalId());
                        pendingStep.setSlotId(slot.getId());
                        slot.save();
                        pendingStep.setStepDate(slot.getScheduleDate());
                        pendingStep.save();
                        AlarmScheduler alarmScheduler = new AlarmScheduler(context);
                        alarmScheduler.setStepId(pendingStep.getId());
                        alarmScheduler.setSubStepId(pendingStep.getId());
                        alarmScheduler.setPendingStepType(pendingStep.getPendingStepType());
                        alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                        alarmScheduler.setDuration(pendingStep.getTime());
                        alarmScheduler.setAlarmDate(slot.getScheduleDate());
                        alarmScheduler.cancel();
                        alarmScheduler.setAlarm();
                        isScheduled =true;
                        break;
                    }
                }
                break;
        }
        return isScheduled;
    }

    /**
     * This method schedules all the steps associated with the goal . This method is called when
     * TimeBox associated with the goal chenged or updated.
     * @param goalId
     * @return
     */
    public int rescheduleSteps(long goalId){
        int count=0;
        List<PendingStep> pendingSteps= new PendingStep(context).getAll(goalId);
        if(pendingSteps!=null) {
             slots=slot.getAll(timeBox.getId());
            Iterator<Slot> it;
            for (PendingStep pendingStep : pendingSteps) {
                //Collections.sort(slots, new SortByDate());
                removeTodaysPassedSlots();
                switch (pendingStep.getPendingStepType()){
                    case SPLIT_STEP:
                    case SERIES_STEP:
                        Iterator<PendingStep> substeps = pendingStep.
                                getAllSubSteps(pendingStep.getId(), pendingStep.getGoalId()).iterator();
                        it = slots.iterator();
                        while (substeps.hasNext()) {
                            PendingStep substep=substeps.next();
                            while (it.hasNext()) {
                                Slot slot = it.next();
                                slot.setTime(Constants.MAX_SLOT_DURATION);
                                if (substep.getTime() <=slot.getTime() && slot.getTimeBoxId()==timeBox.getId()  ) {
                                    slot.setTime(slot.getTime() - substep.getTime());
                                    slot.setGoalId(substep.getGoalId());
                                    substep.setSlotId(slot.getId());
                                    slot.save();
                                    substep.setStepDate(slot.getScheduleDate());
                                    substep.save();
                                    AlarmScheduler alarmScheduler = new AlarmScheduler(context);
                                    alarmScheduler.setStepId(substep.getId());
                                    alarmScheduler.setSubStepId(pendingStep.getSubStepOf());
                                    alarmScheduler.setPendingStepType(PendingStep.PendingStepType.SUB_STEP);
                                    alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                                    alarmScheduler.setDuration(substep.getTime());
                                    alarmScheduler.setAlarmDate(slot.getScheduleDate());
                                    alarmScheduler.cancel();
                                    alarmScheduler.setAlarm();
                                    count++;
                                    it.remove();
                                    break;
                                }
                            }
                        }
                        break;
                    case SINGLE_STEP:
                        it = slots.iterator();
                        while (it.hasNext()) {
                            Slot slot = it.next();
                            slot.setTime(Constants.MAX_SLOT_DURATION);
                            if (pendingStep.getTime() <=slot.getTime() && slot.getTimeBoxId()==timeBox.getId()  ) {
                                slot.setTime(slot.getTime() - pendingStep.getTime());
                                slot.setGoalId(pendingStep.getGoalId());
                                pendingStep.setSlotId(slot.getId());
                                slot.save();
                                pendingStep.setStepDate(slot.getScheduleDate());
                                pendingStep.save();
                                AlarmScheduler alarmScheduler = new AlarmScheduler(context);
                                alarmScheduler.setStepId(pendingStep.getId());
                                alarmScheduler.setSubStepId(pendingStep.getId());
                                alarmScheduler.setPendingStepType(pendingStep.getPendingStepType());
                                alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                                alarmScheduler.setDuration(pendingStep.getTime());
                                alarmScheduler.setAlarmDate(slot.getScheduleDate());
                                alarmScheduler.cancel();
                                alarmScheduler.setAlarm();
                                count++;
                                it.remove();
                                break;
                            }
                        }
                        break;
                }
            }
        }
        return count;
    }

    /**********************************************************************************************/
    // Utils
    /**********************************************************************************************/
    public boolean validateTimeBox(){
        boolean isValid=true;
        removeTodaysPassedSlots();
        if(slots!=null) {
            for (Slot slot : slots) {
                if (slot.getTimeBoxId() != prefs.getUnplannedTimeBoxId()) {
                    isValid = false;
                    break;
                }
            }
        }else{
            isValid=false;
        }
        if( slots==null || slots.size()==0 )
            isValid=false;
        return isValid;
    }

    public boolean validateTimeBoxForUpdate(long oldTimeBoxId){
        boolean isValid=true;
        removeTodaysPassedSlots();
        if(slots!=null) {
            for (Slot slot : slots) {
                if (slot.getTimeBoxId() != prefs.getUnplannedTimeBoxId()) {
                    if(slot.getTimeBoxId() != oldTimeBoxId) {
                        isValid = false;
                        break;
                    }
                }
            }
        }
        if(slots.size()==0 || slots==null)
            isValid=false;

        return isValid;

    }

    public void removeTodaysPassedSlots(){
        //remove today's passed slots
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String  sqliteDate=CalendarUtils.getSqLiteDateFormat(cal);
        Date date=CalendarUtils.parseDate(sqliteDate);

       if (slots!=null) {
           Slot slot;
           Iterator<Slot> itSlots = slots.iterator();
           while (itSlots.hasNext()) {
               slot = itSlots.next();
               if (date.compareTo(slot.getScheduleDate()) == 0) {
                   itSlots.remove();
               } else {
                   break;
               }
           }
       }

//        Set<TimeBoxWhen> whens=CalendarUtils.getPossibleWhenTypesOfDay();
//        if(slots!=null) {
//            for (TimeBoxWhen when : whens) {
//                Slot slot = null;
//                Iterator<Slot> itSlots = slots.iterator();
//
//                while (itSlots.hasNext()) {
//                    slot = itSlots.next();
//                    if (date.compareTo(slot.getScheduleDate()) == 0 && (when != slot.getWhen())) {
//                        itSlots.remove();
//                        break;
//                    }
//                }
//                if (date.compareTo(slot.getScheduleDate()) != 0)
//                    break;
//
//            }
//        }
    }
}
