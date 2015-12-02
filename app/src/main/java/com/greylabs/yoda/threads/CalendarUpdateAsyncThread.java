package com.greylabs.yoda.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.greylabs.yoda.models.Day;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 11/17/2015.
 */
public class CalendarUpdateAsyncThread extends AsyncTask<Void,Void,Void>{
    private final static String TAG="CalUpdate";
    private ProgressDialog progressDialog;
    private Context context;
    private Handler handler;

    public CalendarUpdateAsyncThread(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Wait for a moment...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Logger.d(TAG,"Checking Calendar State");
        Day day=new Day(context);
        if(CalendarUtils.compareOnlyDates(day.getFirstDay(),new Date())==false) {
            PendingStep pendingStep=new PendingStep(context);
            List<PendingStep> pendingSteps=pendingStep.getAllExpireSteps();
            for (PendingStep ps:pendingSteps){
                ps.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                ps.cancelAlarm();
                ps.save();
            }
            Logger.d(TAG,"Step clean up done");
            YodaCalendar yodaCalendar = new YodaCalendar(context);
            yodaCalendar.updateCalendar();
            Logger.d(TAG, "Calendar updated success");
        }else{
            Logger.d(TAG,"Calendar up to date");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Message msg=new Message();
        msg.obj="";
        handler.handleMessage(msg);
        progressDialog.dismiss();
    }
}
