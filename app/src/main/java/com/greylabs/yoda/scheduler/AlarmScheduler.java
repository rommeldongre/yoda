package com.greylabs.yoda.scheduler;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Logger;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

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
    private int duration;
    private int startTime;
    private Context context;
    private AlarmManager alarmManager;

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
        calTarget.set(Calendar.HOUR_OF_DAY,startTime);
        Logger.log(TAG,"Target date:[Start Time]"+calTarget.getTime().toString());
        Intent broadcastReceiver = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)stepId,broadcastReceiver,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);

        //end time
        calTarget.add(Calendar.HOUR_OF_DAY, duration);
        Logger.log(TAG, "Target date:[End Time]" + calTarget.getTime().toString());
        broadcastReceiver = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, -(int)stepId,broadcastReceiver,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);
    }

    public void postponeAlarm(int mins){
        Calendar calTarget=Calendar.getInstance();
        calTarget.setTime(alarmDate);
        calTarget.set(Calendar.HOUR_OF_DAY, startTime);
        calTarget.add(Calendar.MINUTE,mins);
        Logger.log(TAG, "Target date:[Postpone to]" + calTarget.getTime().toString());
        Intent broadcastReceiver = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -(int)stepId,broadcastReceiver,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calTarget.getTimeInMillis(), pendingIntent);
    }

    /**
     * This method cancels the alarm matching to PendingIntent. Internally it takes stepId as
     * request code and cancels the corresponding alarm
     * @return None
     */
    public void cancel(){
        Intent broadcastReceiver = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context, (int)stepId,broadcastReceiver,0);
        PendingIntent pendingIntentEnd = PendingIntent.getBroadcast(context, -(int)stepId,broadcastReceiver,0);
        alarmManager.cancel(pendingIntentStart);
        alarmManager.cancel(pendingIntentEnd);
    }
}
