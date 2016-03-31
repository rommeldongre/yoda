package com.greylabs.ydo.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.greylabs.ydo.models.Day;
import com.greylabs.ydo.scheduler.YodaCalendar;
import com.greylabs.ydo.utils.CalendarUtils;
import com.greylabs.ydo.utils.Logger;

import java.util.Date;

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
        progressDialog.dismiss();
        Message msg=new Message();
        msg.obj="";
        handler.handleMessage(msg);

    }
}
