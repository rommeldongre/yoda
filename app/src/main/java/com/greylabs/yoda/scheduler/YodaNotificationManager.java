package com.greylabs.yoda.scheduler;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Jaybhay Vijay on 7/22/2015.
 */
public class YodaNotificationManager {

    private Context context;
    public YodaNotificationManager(Context context){
        this.context=context;
    }
    public void showNotification(){
     NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context)
        .setContentTitle("My notification")
        .setContentText("Hello World!");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,builder.build());
    }
}
