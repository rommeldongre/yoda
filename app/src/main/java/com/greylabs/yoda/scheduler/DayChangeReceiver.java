package com.greylabs.yoda.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 8/27/2015.
 */
public class DayChangeReceiver extends BroadcastReceiver {
    private static final String TAG="DayChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.log(TAG, "Inside DateChangeReceiver");
        Intent dateChangeService = new Intent(context, DateChangeService.class);
        context.startService(dateChangeService);
        Logger.log(TAG,"Started Day change service.");

    }
}
