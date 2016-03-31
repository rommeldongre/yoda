package com.greylabs.ydo.scheduler;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Dialogues;
import com.greylabs.ydo.utils.Logger;

public class AlarmService extends Service {
    private static final String TAG="AlarmService";
//    private AlarmScheduler alarmScheduler;
    private final IBinder iBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "Alarm Service created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Alarm on start command...", Toast.LENGTH_SHORT).show();
       // YodaNotificationManager yodaNotificationManager=new YodaNotificationManager(this);
        //yodaNotificationManager.showNotification();
        AlarmScheduler alarmScheduler=null;
        PendingStep.PendingStepStartEnd startEnd=null;
        if(intent!=null) {
            alarmScheduler = (AlarmScheduler) intent.getSerializableExtra(Constants.ALARM_SCHEDULER);
            startEnd= (PendingStep.PendingStepStartEnd) intent.getSerializableExtra(Constants.STEP_START_END);
            if(startEnd== PendingStep.PendingStepStartEnd.START){
                playNotificationSound();
            }
        }
        if(alarmScheduler!=null) {
            Dialogues dialogues = new Dialogues(this);
            dialogues.showNowNotificationDialogue(Constants.ALARM_SERVICE, alarmScheduler,startEnd, null);

//            Intent actNowFilter = new Intent(this, ActNowFilter.class);
//            actNowFilter.putExtra(Constants.CALLER, Constants.ALARM_SERVICE);
//            actNowFilter.putExtra(Constants.ALARM_SCHEDULER, alarmScheduler);
//            actNowFilter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(actNowFilter);
            AlarmReceiver.completeWakefulIntent(intent);

        }
        return START_STICKY;
    }

    private void playNotificationSound(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
