package com.greylabs.yoda.scheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 * This broadcast receiver will receive only if device is ON and date has been changed i.e. next day
 */

public class DateChangeReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            Logger.d("DateChangeReceiver", "Inside DateChangeReceiver");
            Intent dateChangeService = new Intent(context, DateChangeService.class);
            startWakefulService(context,dateChangeService);
        }
    }
}
