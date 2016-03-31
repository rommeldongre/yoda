package com.greylabs.ydo.threads;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.greylabs.ydo.apis.googleacc.GoogleAccount;
import com.greylabs.ydo.utils.Constants;

import java.io.IOException;

public class AsyncTaskThread extends AsyncTask<Void, Void, Integer> {

    Context context;
    Handler myHandler;
    int OPERATION;
    int NUMBER_OF_DAYS_TO_EXPORT;

    public AsyncTaskThread(Context activityContext, Handler handler, int OPERATION, Bundle bundle) {
        this.context = activityContext;
        this.myHandler = handler;
        this.OPERATION = OPERATION;
        if(bundle != null)
            NUMBER_OF_DAYS_TO_EXPORT = bundle.getInt(Constants.NUMBER_OF_DAYS_TO_EXPORT);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(Void... params) {
        GoogleAccount googleAccount = new GoogleAccount(context);
        googleAccount.authenticate();
        try {
            switch (OPERATION) {
                case Constants.OPERATION_SYNC_NOW:
                    googleAccount.sync();
                    break;

                case Constants.OPERATION_IMPORT:
                    googleAccount.doImport();
                    break;

                case Constants.OPERATION_EXPORT:
                    googleAccount = new GoogleAccount(context, NUMBER_OF_DAYS_TO_EXPORT);
                    googleAccount.exportToCalendar();
                    break;
            }
        } catch (UserRecoverableAuthIOException e) {
            //context.startActivity(e.getIntent());
            //Logger.showMsg(context, "Wait authenticating...");
            ((Activity) context).startActivityForResult(e.getIntent(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            OPERATION=4;
        }
        return OPERATION;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        Message message = new Message();
        message.obj = result;
        myHandler.sendMessage(message);
    }
}