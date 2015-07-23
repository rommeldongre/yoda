package com.greylabs.yoda.scheduler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class AlarmService extends Service {
    private static final String TAG="AlarmService";

    private final IBinder iBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.log(TAG,"Alarm Service created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Alarm on start command...", Toast.LENGTH_SHORT).show();
        YodaNotificationManager yodaNotificationManager=new YodaNotificationManager(this);
        yodaNotificationManager.showNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {


        // Tell the user we stopped.

    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
}
