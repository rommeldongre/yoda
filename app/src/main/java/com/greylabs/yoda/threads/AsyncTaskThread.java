package com.greylabs.yoda.threads;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.greylabs.yoda.apis.googleacc.GoogleAccount;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

import java.io.IOException;

public class AsyncTaskThread extends AsyncTask<Void, Void, Void> {

    Context context;
    Handler myHandler;
    int OPERATION;

    public AsyncTaskThread( Context activityContext, Handler handler, int OPERATION) {
        this.context = activityContext;
        this.myHandler = handler;
        this.OPERATION = OPERATION;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... params) {
        GoogleAccount googleAccount = new GoogleAccount(context);
        googleAccount.authenticate();
        try {
            switch (OPERATION){
                case Constants.OPERATION_SYNC_NOW :
                    googleAccount.sync();
                    break;

                case Constants.OPERATION_IMPORT:
                    googleAccount.doImport();
                    break;

                case Constants.OPERATION_EXPORT :
                    googleAccount.doExport();
                    break;
            }
        }catch (UserRecoverableAuthIOException e){
            context.startActivity(e.getIntent());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Logger.log("Import(1) / Export(2) done :", " " + OPERATION);
        Message message = new Message();
        message.obj = OPERATION;
        myHandler.sendMessage(message);
    }
}