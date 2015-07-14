package com.greylabs.yoda.schedular;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
public class AlarmInfo implements Serializable{
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private static final String TAG="Alarm";
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
    /**
     *This method sets the slarm for given target date. You need to specify the target date in the
     * * AlarmInfo object attributes.The stepId used as request code for PendingIntent to enable
     * us to cancel this alarm for step having id stepId.
     *@return None
     */
    public void setAlarm(){

//        Calendar calTarget=Calendar.getInstance();
//        calTarget.setTime(alarmDate);
//        Logger.log(TAG,"Target date:"+calTarget.getTime().toString());
//        Intent broadcastReceiver = new Intent(context, AlarmBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)stepId,broadcastReceiver,0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,calTarget.getTimeInMillis(),pendingIntent);

    }

    /**
     * This method cancels the alarm matching to PendingIntent. Internally it takes stepId as
     * request code and cancels the corresponding alarm
     * @return None
     */
    public void cancel(){
//        Intent broadcastReceiver = new Intent(context, AlarmBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)stepId,broadcastReceiver,0);
//        alarmManager.cancel(pendingIntent);
    }

}
