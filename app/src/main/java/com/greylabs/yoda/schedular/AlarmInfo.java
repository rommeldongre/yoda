package com.greylabs.yoda.schedular;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.greylabs.yoda.activities.ActHome;
import com.greylabs.yoda.models.PendingStep;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class AlarmInfo implements Serializable{
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long stepId;
    private Date alarmDate;
    private int duration;
    private Context context;
    private AlarmManager alarmManager;

    public long getStepId() {
        return stepId;
    }
    /**********************************************************************************************/
    // Getters and Setters
    /**********************************************************************************************/

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
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
    public AlarmInfo(Context context){
        this.context=context;
        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    public void setAlarm(){
        Calendar calNow=Calendar.getInstance();
        Calendar calTarget=Calendar.getInstance();
        calTarget.setTime(alarmDate);

        Intent broadcastReceiver = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,broadcastReceiver,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calTarget.getTimeInMillis()-calNow.getTimeInMillis(),pendingIntent);

    }
}
