package com.greylabs.yoda.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

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
             Intent alarmService = new Intent(context, AlarmService.class);
             alarmService.putExtra(Constants.ALARM_SCHEDULER, alarmScheduler);
             context.startService(alarmService);
             Logger.log(TAG,"AlarmScheduler is not null:"+alarmScheduler.toString());
        }
    }
}
