package com.greylabs.yoda.threads;

/**
 * Created by Jaybhay Vijay on 8/12/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.greylabs.yoda.apis.googleacc.GoogleSync;
import com.greylabs.yoda.database.NewStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.io.IOException;

public class ImportTaskAsyncThread extends AsyncTask<Void, Void, Void> {

    ProgressDialog progressDialog;
    Context context;
    Handler myHandler;

    public ImportTaskAsyncThread(Context activityContext, Handler handler) {
        this.context = activityContext;
        this.myHandler = handler;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        NewStep newStep=new NewStep(context);
        newStep.newStep();
        Slot slot=new Slot(context);
        slot.setDefaultGoalDetails();
        GoogleSync googleSync = new GoogleSync(context);
        try {
            googleSync.sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(Constants.MSG_IMPORTING_TASKS);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }



    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        Logger.log("ImportTaskAsyncThread", "Task Imported to stretch Goal");
        Message message = new Message();
        message.obj = result;
        myHandler.sendMessage(message);
    }
}