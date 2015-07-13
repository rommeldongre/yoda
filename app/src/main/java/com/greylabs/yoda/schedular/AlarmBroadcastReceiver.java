package com.greylabs.yoda.schedular;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, AlarmService.class);
        context.startService(service1);
    }
}
