package com.greylabs.yoda.scheduler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/14/2015.
 */
public class BootCompleteService extends Service {
    private static final String TAG="BootCompleteService";

    private final IBinder iBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        BootCompleteService getService() {
            return BootCompleteService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.log(TAG, "BootCompleteService created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "BootCompleteService on start command...", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

}