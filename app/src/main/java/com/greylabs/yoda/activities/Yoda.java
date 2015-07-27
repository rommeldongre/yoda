package com.greylabs.yoda.activities;

import android.app.Application;
import android.content.res.Configuration;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.QuickStart;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Prefs;

public class Yoda extends Application {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
