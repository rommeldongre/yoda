package com.greylabs.yoda.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG="AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.log(TAG,"Received Alarm. Starting Alarm Service to do background task.");
        Intent alarmService = new Intent(context, AlarmService.class);
        AlarmScheduler alarmScheduler=(AlarmScheduler)intent.getSerializableExtra("alarmScheduler");
        if(alarmScheduler==null){
            Logger.log(TAG,"AlarmScheduler received null");
        }else{
            Logger.log(TAG,"AlarmScheduler is not null:"+alarmScheduler.toString());
        }
        context.startService(alarmService);
    }
}
