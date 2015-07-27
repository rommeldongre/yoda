package com.greylabs.yoda.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.greylabs.yoda.database.QuickStart;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

public class QuickStartAsyncTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    Context context;
    Handler myHandler;

    public QuickStartAsyncTask(Context activityContext, Handler handler) {
        this.context = activityContext;
        this.myHandler = handler;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(Constants.MSG_INITIALIZING_QUICK_START);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        QuickStart quickStart = new QuickStart(context);
        quickStart.quickStart();
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