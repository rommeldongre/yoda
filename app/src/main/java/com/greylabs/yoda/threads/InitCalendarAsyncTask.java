package com.greylabs.yoda.threads;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.scheduler.AlarmReceiver;
import com.greylabs.yoda.scheduler.AlarmScheduler;
import com.greylabs.yoda.scheduler.DateChangeReceiver;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.util.Calendar;

public class InitCalendarAsyncTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    Context context;
    Handler myHandler;

    public InitCalendarAsyncTask(Context activityContext, Handler handler) {
        this.context = activityContext;
        this.myHandler = handler;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(Constants.MSG_INITIALIZING_CALENDAR);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        Database.getInstance(context);
        YodaCalendar.init(context);
        AlarmScheduler alarmScheduler=new AlarmScheduler(context);
        alarmScheduler.setCalendarUpdateInterval();
//        return jsonObject;
        return null;
    }

//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        Logger.log("InitCalendarAsyncTask", "Calendar Initialized");
        Message message = new Message();
        message.obj = result;
        myHandler.sendMessage(message);
    }
}