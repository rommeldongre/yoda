package com.greylabs.ydo.threads;

/**
 * Created by Jaybhay Vijay on 8/12/2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.greylabs.ydo.apis.googleacc.GoogleAccount;
import com.greylabs.ydo.database.NewStep;
import com.greylabs.ydo.models.Slot;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Logger;

import java.io.IOException;

public class ImportTaskAsyncThread extends AsyncTask<Void, Void, Object> {

    ProgressDialog progressDialog;
    Context context;
    Handler myHandler;
    Integer res=0;
    public ImportTaskAsyncThread(Context activityContext, Handler handler) {
        this.context = activityContext;
        this.myHandler = handler;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        NewStep newStep = new NewStep(context);
        newStep.newStep();
        Slot slot = new Slot(context);
        slot.setDefaultGoalDetails();
        GoogleAccount googleAccount = new GoogleAccount(context);
        googleAccount.authenticate();
        Object obj=new Object();
        try {
            googleAccount.doImport();
            res= 1;
        } catch (UserRecoverableAuthIOException e) {
            e.printStackTrace();
            res= 2;
            obj=e.getIntent();
        } catch (IOException e) {
            e.printStackTrace();
            res= 0;
        }
        return obj;
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
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        Logger.d("ImportTaskAsyncThread", "Task Imported status:"+result);
        Message message = new Message();
        message.obj = result;
        message.arg1=res;
        myHandler.sendMessage(message);
    }
}