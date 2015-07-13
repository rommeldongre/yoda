package com.greylabs.yoda.schedular;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 * This broadcast receiver will receive only if device is ON and date has been changed i.e. next day
 */

public class DateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.log("DateChangeReceiver","Inside DateChangeReceiver");
        Intent dateChangeService = new Intent(context, DateChangeService.class);
        context.startService(dateChangeService);
    }
}
