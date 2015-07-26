package com.greylabs.yoda.scheduler;
import android.content.Context;

import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.models.Day;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
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

    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/


    /**********************************************************************************************/
    //Constructors
    /**********************************************************************************************/
    public YodaCalendar(Context context, TimeBox timeBox) {
        this.context = context;
        this.timeBox = timeBox;
        this.slot=new Slot(context);
        slots=slot.getAll(timeBox);
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
                slot.setGoalId(0);
                slot.setTimeBoxId(0);
                slot.setDayId(day.getId());
                slot.save();
                Logger.log(TAG, " " + dayOfYear + "Day and Slot:" + slot.toString() + "|||" + day.toString());
            }
            cal.add(Calendar.DATE,1);
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
        if(slots!=null && slots.size()>0) {
            for (Slot slot : slots) {
                slot.setTimeBoxId(timeBox.getId());
                slot.setGoalId(goalId);
                slot.setTime(3);
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
    public int detachTimeBox(){
        int slotCount=0;
        if(slots!=null) {
            for (Slot slot : slots) {
                slot.setTimeBoxId(0);
                slot.setGoalId(0);
                slot.setTime(0);
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
    public int updateCalendar(long goalId){
        int slotCount=0;
        if(slots!=null && validateTimeBoxForUpdate()){
            //above condition confirms that timebox does not conflicts with other
            //we need to detach first all slots that have been assigned to it to ensure that
            // slot is free.
            detachTimeBox();
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
        List<Slot> slots=slot.getAll(timeBox.getId());
        Collections.sort(slots, new SortByDate());
        //remove todays passed slots
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String  sqliteDate=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE)+" " +
                cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        Date date=CalendarUtils.parseDate(sqliteDate);
        Iterator<Slot> itSlots=slots.iterator();
        Set<TimeBoxWhen> whens=CalendarUtils.getPossibleWhenTypesOfDay();
        int i=0;
        while (itSlots.hasNext()){
            Slot slot=itSlots.next();
            for(TimeBoxWhen when:whens) {
                if (date.compareTo(slot.getScheduleDate()) == 0 && when==slot.getWhen()) {
                    itSlots.remove();
                }
            }
            if(i==5)
                break;
            i++;
        }

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
                        if (slot.getTimeBoxId()==timeBox.getId() && ps.getTime() <=slot.getTime()) {
                            slot.setTime(slot.getTime() - ps.getTime());
                            slot.setGoalId(ps.getGoalId());
                            ps.setSlotId(slot.getId());
                            slot.save();
                            ps.save();
                            AlarmScheduler alarmScheduler = new AlarmScheduler(context);
                            alarmScheduler.setStepId(ps.getId());
                            alarmScheduler.setSubStepId(ps.getId());
                            alarmScheduler.setPendingStepType(PendingStep.PendingStepType.SUB_STEP);
                            alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                            alarmScheduler.setDuration(ps.getTime());
                            alarmScheduler.setAlarmDate(slot.getScheduleDate());
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
                    if (slot.getTimeBoxId()==timeBox.getId() && pendingStep.getTime() <=slot.getTime()) {
                        slot.setTime(slot.getTime() - pendingStep.getTime());
                        slot.setGoalId(pendingStep.getGoalId());
                        pendingStep.setSlotId(slot.getId());
                        slot.save();
                        pendingStep.save();
                        AlarmScheduler alarmScheduler = new AlarmScheduler(context);
                        alarmScheduler.setStepId(pendingStep.getId());
                        alarmScheduler.setSubStepId(pendingStep.getId());
                        alarmScheduler.setPendingStepType(pendingStep.getPendingStepType());
                        alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                        alarmScheduler.setDuration(pendingStep.getTime());
                        alarmScheduler.setAlarmDate(slot.getScheduleDate());
                        alarmScheduler.setAlarm();
                        isScheduled =true;
                        break;
                    }
                }
                break;
        }
        return isScheduled;
    }

    /**********************************************************************************************/
    // Utils
    /**********************************************************************************************/
    public boolean validateTimeBox(){
        boolean isValid=true;
        //remove today's passed slots
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String  sqliteDate=cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE)+" " +
                cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        Date date=CalendarUtils.parseDate(sqliteDate);
        Iterator<Slot> itSlots=slots.iterator();
        Set<TimeBoxWhen> whens=CalendarUtils.getPossibleWhenTypesOfDay();
        int i=0;
        while (itSlots.hasNext()){
            Slot slot=itSlots.next();
            for(TimeBoxWhen when:whens) {
                if (date.compareTo(slot.getScheduleDate()) == 0 && (when!=slot.getWhen())) {
                    itSlots.remove();
                }
            }
            if(i==5)
                break;
            i++;
        }
        if(slots!=null) {
            for (Slot slot : slots) {
                if (slot.getTimeBoxId() != 0) {
                    isValid = false;
                    break;
                }
            }
        }else{
            isValid=false;
        }
        if(slots.size()==0)
            isValid=false;
        return isValid;
    }

    private boolean validateTimeBoxForUpdate(){
        boolean isValid=true;
        for(Slot slot:slots){
            if(slot.getTimeBoxId()!=0 & slot.getTimeBoxId()!=timeBox.getId()) {
                isValid = false;
                break;
            }
        }
        return isValid;

    }
}
