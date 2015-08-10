package com.greylabs.yoda.scheduler;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
//Usage
//AlarmInfo alarmInfo=new AlarmInfo(this);
//        Calendar cal=Calendar.getInstance();
//        cal.add(Calendar.SECOND,30);
//        alarmInfo.setAlarmDate(cal.getTime());
//        alarmInfo.setStepId(2);
//        alarmInfo.setAlarm();
//
//        alarmInfo.setStepId(2);
//        alarmInfo.cancel();
public class AlarmScheduler implements Serializable{
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private static final String TAG="Alarm";
    private long stepId;
    private long subStepId;
    private PendingStep.PendingStepType pendingStepType;
    private Date alarmDate;

    @Override
    public String toString() {
        return "AlarmScheduler{" +
                "stepId=" + stepId +
                ", subStepId=" + subStepId +
                ", pendingStepType=" + pendingStepType +
                ", alarmDate=" + alarmDate +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    private int duration;
    private int startTime;
    transient private Context context;
    transient private AlarmManager alarmManager;

    /**********************************************************************************************/
    // Getters and Setters
    /**********************************************************************************************/
    public long getStepId() {
        return stepId;
    }
    public void setStepId(long stepId) {
        this.stepId = stepId;
    }
    public long getSubStepId() {
        return subStepId;
    }

    public void setSubStepId(long subStepId) {
        this.subStepId = subStepId;
    }

    public PendingStep.PendingStepType getPendingStepType() {
        return pendingStepType;
    }

    public void setPendingStepType(PendingStep.PendingStepType pendingStepType) {
        this.pendingStepType = pendingStepType;
    }
    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
    }
    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**********************************************************************************************/
    // Constructor
    /**********************************************************************************************/
    public AlarmScheduler(Context context){
        this.context=context;
        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public void initContext(Context context){
        this.context=context;
        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
    /**
     *This method sets the slarm for given target date. You need to specify the target date in the
     * * AlarmInfo object attributes.The stepId used as request code for PendingIntent to enable
     * us to cancel this alarm for step having id stepId.
     *@return None
     */
    public void setAlarm(){

        Calendar calTarget=Calendar.getInstance();
        calTarget.setTime(alarmDate);

        //start Time
        calTarget.set(Calendar.HOUR_OF_DAY, startTime);
        Logger.log(TAG, "Target date:[Start Time]" + calTarget.getTime().toString());
        Intent broadcastReceiver = new Intent(context, AlarmReceiver.class);
        broadcastReceiver.putExtra(Constants.ALARM_SCHEDULER,this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)stepId,broadcastReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);

        //end time
        calTarget.add(Calendar.HOUR_OF_DAY, duration);
        calTarget.add(Calendar.MINUTE,-1);
        Logger.log(TAG, "Target date:[End Time]" + calTarget.getTime().toString());
        broadcastReceiver = new Intent(context, AlarmReceiver.class);
        broadcastReceiver.putExtra(Constants.ALARM_SCHEDULER, this);
        pendingIntent = PendingIntent.getBroadcast(context, -(int)stepId,broadcastReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);
    }

    public void setRepeatingAlarm(Calendar calendar,long interval,PendingIntent pendingIntent){
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
    }

    public void postponeAlarm(int mins){
        Calendar calTarget=Calendar.getInstance();
        calTarget.add(Calendar.MINUTE, mins);
        Logger.log(TAG, "Target date:[Postpone to]" + calTarget.getTime().toString());
        Intent broadcastReceiver = new Intent(context, AlarmReceiver.class);
        broadcastReceiver.putExtra(Constants.ALARM_SCHEDULER, this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)stepId,broadcastReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);
    }

    /**
     * This method cancels the alarm matching to PendingIntent. Internally it takes stepId as
     * request code and cancels the corresponding alarm
     * @return None
     */
    public void cancel(){
        Intent broadcastReceiver = new Intent(context, AlarmReceiver.class);
        broadcastReceiver.putExtra(Constants.ALARM_SCHEDULER,this);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context, (int)stepId,broadcastReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(context, -(int)stepId,broadcastReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntentStart);
        alarmManager.cancel(pendingIntentEnd);
    }

    public  void setCalendarUpdateInterval(){
        Intent broadcastReceiver = new Intent(context, DateChangeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,-2,broadcastReceiver,0);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.setRepeatingAlarm(calendar, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void rescheduleAllSteps(){
        List<PendingStep> pendingSteps=new PendingStep(context).getAll(PendingStep.PendingStepStatus.TODO);
        Calendar calendar=Calendar.getInstance();
        if(pendingSteps!=null) {
            for (PendingStep pendingStep : pendingSteps) {
                Slot slot = new Slot(context).get(pendingStep.getSlotId());
                if(slot.getScheduleDate().compareTo(calendar.getTime())>0) {
                    AlarmScheduler alarmScheduler = new AlarmScheduler(context);
                    alarmScheduler.setStepId(pendingStep.getId());
                    alarmScheduler.setSubStepId(pendingStep.getSubStepOf());
                    alarmScheduler.setPendingStepType(pendingStep.getPendingStepType());
                    alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                    alarmScheduler.setDuration(pendingStep.getTime());
                    alarmScheduler.setAlarmDate(slot.getScheduleDate());
                    alarmScheduler.cancel();//cancel previous alarm if any
                    alarmScheduler.setAlarm();
                }
            }
        }
    }
}
