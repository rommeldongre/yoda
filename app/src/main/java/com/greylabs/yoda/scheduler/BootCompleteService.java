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
        Logger.d(TAG, "BootCompleteService created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "BootCompleteService on start command...", Toast.LENGTH_SHORT).show();
        //update calendar
        YodaCalendar yodaCalendar=new YodaCalendar(this);
        yodaCalendar.updateCalendar();

        //set calendar update interval
        AlarmScheduler alarmScheduler=new AlarmScheduler(this);
//        alarmScheduler.setCalendarUpdateInterval();

        //reschedule all step alarms
        alarmScheduler.rescheduleAllSteps(this);



        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

}
