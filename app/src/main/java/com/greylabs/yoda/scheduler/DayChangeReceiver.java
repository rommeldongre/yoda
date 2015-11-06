package com.greylabs.yoda.scheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 8/27/2015.
 */
public class DayChangeReceiver extends WakefulBroadcastReceiver {
    private static final String TAG="DayChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "Inside DateChangeReceiver");
        Intent dateChangeService = new Intent(context, DateChangeService.class);
        context.startService(dateChangeService);
        Logger.d(TAG, "Started Day change service.");

    }
}
