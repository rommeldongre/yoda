package com.greylabs.yoda.threads;

/**
 * Created by Jaybhay Vijay on 8/12/2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.greylabs.yoda.apis.googleacc.GoogleAccount;
import com.greylabs.yoda.database.NewStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.io.IOException;

public class ImportTaskAsyncThread extends AsyncTask<Void, Void, Integer> {

    ProgressDialog progressDialog;
    Context context;
    Handler myHandler;

    public ImportTaskAsyncThread(Context activityContext, Handler handler) {
        this.context = activityContext;
        this.myHandler = handler;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        NewStep newStep = new NewStep(context);
        newStep.newStep();
        Slot slot = new Slot(context);
        slot.setDefaultGoalDetails();
        GoogleAccount googleAccount = new GoogleAccount(context);
        googleAccount.authenticate();
        Integer res=0;
        try {
            googleAccount.doImport();
            res= 1;
        } catch (UserRecoverableAuthIOException e) {
            ((Activity) context).startActivityForResult(e.getIntent(), 1);
            res= 2;
        } catch (IOException e) {
            e.printStackTrace();
            res= 0;
        }
        return res;
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
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        Logger.d("ImportTaskAsyncThread", "Task Imported to stretch Goal");
        Message message = new Message();
        message.obj = result;
        myHandler.sendMessage(message);
    }
}