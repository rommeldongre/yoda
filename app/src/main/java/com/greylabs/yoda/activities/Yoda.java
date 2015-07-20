package com.greylabs.yoda.activities;

import android.app.Application;
import android.content.res.Configuration;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Prefs;

/**
 * Created by Jaybhay Vijay on 7/19/2015.
 */
public class Yoda extends Application {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Prefs prefs=Prefs.getInstance(getApplicationContext());
        if(prefs.getApplicationFirstTimeInstalled()==true){
            //if application is first time installed then create database,
            //and initialize database
            Database.getInstance(getApplicationContext());
            YodaCalendar.init(getApplicationContext());
            prefs.setApplicationFirstTimeInstalled(false);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
