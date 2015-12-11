package com.greylabs.yoda.activities;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.greylabs.yoda.R;
import com.greylabs.yoda.scheduler.AlarmScheduler;
import com.greylabs.yoda.scheduler.YodaCalendar;

/*import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(
        formUri = "https://jaybhayvijay.cloudant.com/acra-yoda/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = "asterairconglandonsweree",
        formUriBasicAuthPassword = "4ekMGGwl4TTAXJXjn4tutU6R",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crashToast
)*/
public class Yoda extends Application {
    private static  Context context;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //ACRA.init(this);
        context=getApplicationContext();
//        AlarmScheduler alarmScheduler=new AlarmScheduler(context);
//        alarmScheduler.setCalendarUpdateInterval();
    }

    public static Context getContext(){
        return context;
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
