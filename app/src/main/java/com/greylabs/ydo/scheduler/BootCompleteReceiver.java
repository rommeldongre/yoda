package com.greylabs.ydo.scheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.greylabs.ydo.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/14/2015.
 */
public class BootCompleteReceiver extends WakefulBroadcastReceiver {
    private static  final String TAG="BootCompleteReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "Received alarm. Starting BootComplete service");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent bootCompleteService = new Intent(context, BootCompleteService.class);
            startWakefulService(context,bootCompleteService);
        }else{
            Logger.d(TAG, "Sender is null,Can not start BootCompleteService");
        }
    }
}
