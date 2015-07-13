package com.greylabs.yoda.schedular;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class AlarmService extends Service {

    private final IBinder iBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Alarm...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
