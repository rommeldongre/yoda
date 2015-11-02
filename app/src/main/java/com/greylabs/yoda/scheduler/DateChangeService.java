package com.greylabs.yoda.scheduler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.greylabs.yoda.utils.GoalUtils;
import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class DateChangeService extends Service {

    private static final String TAG="DateChangeService";
    private YodaCalendar yodaCalendar;
    private final IBinder iBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        DateChangeService getService() {
            return DateChangeService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.log(TAG, "Date Change Service created. Now updating database.");
        yodaCalendar=new YodaCalendar(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        yodaCalendar.updateCalendar();
        //GoalUtils.rescheduleAllSteps();
        return START_STICKY;
    }

}
