package com.greylabs.yoda.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.Calendar;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG="AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.log(TAG,"Received Alarm. Starting Alarm Service to do background task.");
        AlarmScheduler alarmScheduler=(AlarmScheduler)intent.getSerializableExtra(Constants.ALARM_SCHEDULER);
         if(alarmScheduler==null){
            Logger.log(TAG,"AlarmScheduler received null");
        }else{
             Calendar calendar=Calendar.getInstance();
             calendar.set(Calendar.SECOND,0);
             calendar.set(Calendar.MILLISECOND,0);
             if(alarmScheduler.getAlarmDate().compareTo(calendar.getTime())<0) {
                 Intent alarmService = new Intent(context, AlarmService.class);
                 alarmService.putExtra(Constants.ALARM_SCHEDULER, alarmScheduler);
                 context.startService(alarmService);
                 Logger.log(TAG, "AlarmScheduler is not null:" + alarmScheduler.toString());
             }else
                 Logger.log(TAG,"Skipping alarm as alarm date is less than today's date");
        }
    }
}
