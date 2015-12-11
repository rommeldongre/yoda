package com.greylabs.yoda.scheduler;
import android.content.Context;

import com.google.api.client.util.DateTime;
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
import com.greylabs.yoda.utils.sorters.SortPendingStepByPriority;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

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
        Logger.d(TAG, "Initializing calender from date:" + cal.getTime().toString());
        int maxDays=365;
        //Check for leap year condition
        Calendar yr=Calendar.getInstance();
        if(cal.get(Calendar.MONTH)>Calendar.FEBRUARY){
            //Feb of current year passed, check for next year Feb month
            Logger.d(TAG, "Feb of current month is passed.");
            yr.add(Calendar.YEAR, 1);
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }else{
            //Feb of next month is appearing in current year
            Logger.d(TAG, "Feb of current month is coming.");
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }
        if(yr.getActualMaximum(Calendar.DAY_OF_MONTH)==29) {
            maxDays++;
            Logger.d(TAG, "Detected Leap year. One more day added to maxDays");
        }
        Logger.d(TAG, "Maximum Days in Calender:" + maxDays);
        //Fill database
        Logger.d(TAG, "Filling entries in database.");
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
                Logger.d(TAG, " " + dayOfYear + "Day and Slot:" + slot.toString() + "|||" + day.toString());
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
            Logger.d(TAG, "Feb of current month is passed.");
            yr.add(Calendar.YEAR, 1);
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }else{
            //Feb of next month is appearing in current year
            Logger.d(TAG, "Feb of current month is coming.");
            yr.set(Calendar.MONTH, Calendar.FEBRUARY);
        }
        if(yr.getActualMaximum(Calendar.DAY_OF_MONTH)==29) {
            maxDays++;
            Logger.d(TAG, "Detected Leap year. One more day added to maxDays");
        }
        Logger.d(TAG, "Maximum Days in Calender:" + maxDays);
        return maxDays;
    }
    public void updateCalendar(){
        //Create calender and set todays date
        Calendar cal=getCalendarInstance();
        Logger.d(TAG, "Updating calender from date:" + cal.getTime().toString());
        //update database
        Logger.d(TAG, "Updating entries in database.");
        int dayOfYear=1;
        int dayOfWeek=-1;
        int weekOfMonth=-1;
        int monthOfYear=-1;
        int quarterOfYear=-1;
        int year=-1;

        boolean isChanged=false;
        Day day=new Day(context);
        cal=Calendar.getInstance();Calendar cal1=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);cal1.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);cal1.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);cal1.set(Calendar.MILLISECOND, 0);
        Logger.d(TAG, "Initialized calendat variables");

        //get all days of calendar
        List<Day> days=day.getAll();
        day=days.get(0);
        Logger.d(TAG, "First day in Yoda Calendar :" + day.toString());
        int daysDeleted=0;
        Calendar temp=Calendar.getInstance();
        if(day.getDate().compareTo(cal.getTime())>0){
            Logger.d(TAG, "First date in yoda calendar is greater than current date");
            //first entry in the Calendar DB  is greater than  current
            //String startDate=CalendarUtils.getSqLiteDateFormat(cal);
            cal1.setTime(day.getDate());
            //String endDate=CalendarUtils.getSqLiteDateFormat(cal1);
            //daysDeleted=day.deleteNextDays(startDate, endDate);
            int numberOfDays=cal1.get(Calendar.DAY_OF_YEAR)-cal.get(Calendar.DAY_OF_YEAR);
            Logger.d(TAG, "First date:" + day.getDate().toString() + " and Current date:" + cal.getTime().toString() + " " +
                    " and their Difference:" + numberOfDays);
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
                Logger.d(TAG, "Day added:" + day.toString());
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
                    Logger.d(TAG, "Slot added:" + slot.toString());
                }
                Logger.d(TAG, "Day Deleted :" + days.get(days.size() - i).toString());
                int count=days.get(days.size()-i).delete();
                cal.add(Calendar.DATE, 1);
            }
            isChanged=true;
        }
        else if(day.getDate().compareTo(cal.getTime())<0){
            Logger.d(TAG, "First date in yoda calendar is less than current date");
            //first entry in the Calendar DB  is less than  current
            cal1.setTime(day.getDate());
            String startDate=CalendarUtils.getSqLiteDateFormat(cal1);
            String endDate=CalendarUtils.getSqLiteDateFormat(cal);
            daysDeleted=day.deletePrevDays(startDate, endDate);
            Logger.d(TAG, "Number of Days deleted at the end of Yoda Calendar :" + daysDeleted);
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
            isChanged=true;
        }

        //set dayOfYear
        if(isChanged) {
            int maxDaysOfYear = getMaxDays(cal);
            days = day.getAll();
            for (int i = 1; i <= maxDaysOfYear; i++) {
                days.get(i - 1).setDayOfYear(i);
                days.get(i - 1).save();
            }
            //mark for steps expired
            PendingStep pendingStep=new PendingStep(context);
            List<PendingStep> pendingSteps=pendingStep.getAllExpireSteps();
            Date today=Calendar.getInstance().getTime();
            if(pendingSteps!=null) {
                for (PendingStep ps : pendingSteps) {
                    if(ps.getStepDate().compareTo(today)<0) {
                        ps.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                        ps.cancelAlarm();
                        ps.save();
                    }
                }
                Logger.d(TAG, "Step clean up done");
            }else {
                Logger.d(TAG, "No need to clean up steps");
            }


            //update for all time boxes having forever as Till time-need optimization
            List<TimeBox> timeBoxes = new TimeBox(context).getAll(TimeBox.TimeBoxStatus.ACTIVE);
            Goal goal = new Goal(context);
            timeBoxes.remove(new TimeBox(context).get(prefs.getUnplannedTimeBoxId()));
            for (TimeBox timeBox : timeBoxes) {
                this.timeBox = timeBox;
                if (timeBox.getTillType() == TimeBoxTill.FOREVER){
                    attachTimeBox(goal.getGoalId(timeBox.getId()));
                    rescheduleSteps(goal.getGoalId(timeBox.getId()));
                }else{
                     rescheduleSteps(goal.getGoalId(timeBox.getId()));
                }
            }
            //reschedule steps in stretch Goal
            this.timeBox=new TimeBox(context).get(prefs.getUnplannedTimeBoxId());
            rescheduleSteps(prefs.getStretchGoalId());

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
        slots=slot.getAll(timeBox);
        removeTodaysPassedSlots();
//        PendingStep pendingStep=new PendingStep(context);
        if(slots!=null && slots.size()>0) {
            for (Slot slot : slots) {
                //following checks that any Unplanned Slot is assigned to some step or not
                // If it is assigned then keep this slot as Unplanned TimeBox
//                if( pendingStep.isSlotAssigned(slot.getId()))
//                    continue;
                slot.setTimeBoxId(timeBox.getId());
                slot.setGoalId(goalId);
                slot.setTime(Constants.MAX_SLOT_DURATION);
                slot.save();
                slotCount++;
            }
        }
//        rescheduleSteps(prefs.getStretchGoalId());
        return slotCount;
    }

    // added by rohit to free the slots of stretch goal
//    private void freeStretchTBSlots() {
//        ArrayList<PendingStep> stepArrayList = new ArrayList<>();
//        Goal stretchGoal = new Goal(context).get(prefs.getStretchGoalId());
//        stepArrayList.addAll(new PendingStep(context).getAll(stretchGoal.getId()));
//        int length = stepArrayList.size();
//        for(int i=0;i<length;i++){
//            stepArrayList.get(i).setUpdated(new DateTime(new Date()));
//            stepArrayList.get(i).freeSlot();
//            stepArrayList.get(i).setSlotId(0);
//            stepArrayList.get(i).save();
//            stepArrayList.get(i).cancelAlarm();
//            if(stepArrayList.get(i).getPendingStepType()== PendingStep.PendingStepType.SPLIT_STEP||
//                    stepArrayList.get(i).getPendingStepType()== PendingStep.PendingStepType.SERIES_STEP) {
//                stepArrayList.get(i).updateSubSteps();
//                stepArrayList.get(i).freeSlots();
//            }
//        }
//    }

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
        Calendar calendar=Calendar.getInstance();
        boolean isScheduled=false;
        slots=slot.getAll(timeBox.getId());
        //Collections.sort(slots, new SortByDate());
        if(slots==null | slots.size()==0) {
            pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.UNSCHEDULED);
            pendingStep.save();
            return false;
        }
        Goal goal=new Goal(context).get(pendingStep.getGoalId());
        removeTodaysPassedSlots();
        List<PendingStep> pendingStepsList=new ArrayList<>();
        switch (pendingStep.getPendingStepType()){
            case SPLIT_STEP:
            case SERIES_STEP:
                List<PendingStep> temp=pendingStep.getAllSubSteps(pendingStep.getId(), pendingStep.getGoalId());
                if (temp!=null)
                    pendingStepsList.addAll(temp);
                break;
            case SINGLE_STEP:
                pendingStepsList.add(pendingStep);
                break;
        }
        Iterator<PendingStep> pendingSteps =pendingStepsList .iterator();
        PendingStep ps = null;
        int sessionCount=0;
        while (pendingSteps.hasNext()) {
            ps=pendingSteps.next();
            Iterator<Slot> it = slots.iterator();
            while (it.hasNext()) {
                Slot slot = it.next();
                if (!ps.isSlotAssigned(slot.getId()) && slot.getTimeBoxId()==timeBox.getId() ) {
                    isScheduled=scheduleSingleStep(ps,goal, slot, calendar);
                    break;
                }
            }
            sessionCount++;
        }
        if(pendingStepsList!=null && pendingStepsList.size()>=1 && ps!=null) {
            pendingStep.setUpdated(ps.getUpdated());
            pendingStep.setStepDate(pendingStepsList.get(0).getStepDate());
            pendingStep.setStepCount(sessionCount);
            pendingStep.save();
            goal.setDueDate(pendingStepsList.get(pendingStepsList.size()-1).getStepDate());
            goal.save();
        }
        return isScheduled;
    }

    private boolean scheduleSingleStep(PendingStep ps,Goal goal,Slot slot,Calendar calendar){
        slot.setTime(slot.getTime() - ps.getTime());
        slot.setGoalId(ps.getGoalId());
        ps.setSlotId(slot.getId());
        calendar.setTime(slot.getScheduleDate());
        calendar.add(Calendar.HOUR_OF_DAY, slot.getWhen().getStartTime());
        updateStep(ps, slot);
        ps.setStepDate(calendar.getTime());
        ps.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        slot.save();
        ps.save();
        goal.setDueDate(ps.getStepDate());
        goal.save();
        AlarmScheduler alarmScheduler = new AlarmScheduler(context);
        alarmScheduler.setStepId(ps.getId());
        alarmScheduler.setSubStepId(ps.getSubStepOf());
        alarmScheduler.setPendingStepType(PendingStep.PendingStepType.SUB_STEP);
        alarmScheduler.setStartTime(slot.getWhen().getStartTime());
        alarmScheduler.setDuration(ps.getTime());
        alarmScheduler.setAlarmDate(slot.getScheduleDate());
        alarmScheduler.cancel();
        alarmScheduler.setAlarm();
        return  true;
    }
    private void updateStep(PendingStep pendingStep, Slot slot) {
        if(pendingStep.getStepDate()!=null && pendingStep.getId()!=0){
            //change updated date,only if step date changed
            if(pendingStep.getStepDate().compareTo(slot.getScheduleDate())!=0)
                pendingStep.setUpdated(new DateTime(new Date()));
        }else{
            pendingStep.setUpdated(new DateTime(new Date()));
        }

    }

    /**
     * This method schedules all the steps associated with the goal . This method is called when
     * TimeBox associated with the goal chenged or updated.
     * @param goalId
     * @return
     */
    public int rescheduleSteps(long goalId){
        int count=0;
        Calendar calendar=Calendar.getInstance();
        Goal goal=new Goal(context).get(goalId);
        slots=slot.getAll(timeBox.getId());
        removeTodaysPassedSlots();
        PendingStep pendingStep=new PendingStep(context);
        List<PendingStep> pendingStepsList=new ArrayList<>();
        List<PendingStep> temp= pendingStep.getAll(PendingStep.PendingStepStatus.TODO,
                PendingStep.PendingStepDeleted.SHOW_NOT_DELETED, goalId);
        if(temp!=null){
            pendingStepsList.addAll(temp);
        }
        temp=pendingStep.getAll(PendingStep.PendingStepStatus.UNSCHEDULED,
                PendingStep.PendingStepDeleted.SHOW_NOT_DELETED, goalId);
        if(temp!=null){
            pendingStepsList.addAll(temp);
        }
        Collections.sort(pendingStepsList,new SortPendingStepByPriority());

        if (pendingStepsList==null) return count;
        Iterator<PendingStep> pendingSteps =pendingStepsList .iterator();
        PendingStep ps=null;
        while (pendingSteps.hasNext()) {
            ps=pendingSteps.next();
            Iterator<Slot> it = slots.iterator();
            if(slots.size()==0) {
                ps.setPendingStepStatus(PendingStep.PendingStepStatus.UNSCHEDULED);
                ps.setSlotId(0);
                ps.save();
            }
            while (it.hasNext()) {
                Slot slot = it.next();
                slot.setTime(Constants.MAX_SLOT_DURATION);
                if (ps.getTime() <= slot.getTime() && slot.getTimeBoxId() == timeBox.getId()) {
                    scheduleSingleStep(ps,goal,slot,calendar);
                    it.remove();
                    break;
                }
            }
        }
        if(ps!=null){
            goal.setDueDate(ps.getStepDate());
            goal.save();
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

    public  int removeTodaysPassedSlots(){
        //remove today's passed slots
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String  sqliteDate=CalendarUtils.getSqLiteDateFormat(cal);
        Date date=CalendarUtils.parseDate(sqliteDate);

        //check for slots
        int count=0;
        Set<TimeBoxWhen> whens=CalendarUtils.getTodaysPassedSlots();
        if(slots!=null && whens!=null) {
            for (TimeBoxWhen when : whens) {
                Slot slot = null;
                Iterator<Slot> itSlots = slots.iterator();
                while (itSlots.hasNext()) {
                    slot = itSlots.next();
                    if (date.compareTo(slot.getScheduleDate()) == 0 && (when == slot.getWhen())) {
                        itSlots.remove();
                        count++;
                        break;
                    }
                }
            }
        }
        return  count;
    }
}
