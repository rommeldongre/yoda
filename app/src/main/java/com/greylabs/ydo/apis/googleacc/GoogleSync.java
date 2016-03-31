package com.greylabs.ydo.apis.googleacc;

import android.content.Context;
import android.os.AsyncTask;

import com.greylabs.ydo.utils.ConnectionUtils;
import com.greylabs.ydo.utils.Prefs;

import org.acra.ACRA;

import java.io.IOException;

/**
 * Created by Jaybhay Vijay on 8/12/2015.
 */
public class GoogleSync {

    private static GoogleAccount googleAccount;
    private static GoogleSync googleSync;
    private Context context;
    private com.google.api.services.tasks.Tasks service;


    private GoogleSync(Context context) {
        this.context = context;
        googleAccount = new GoogleAccount(context);
    }

    public static GoogleSync getInstance(Context context) {
        if (googleSync == null)
            googleSync = new GoogleSync(context);
        return googleSync;
    }

    public void sync() {

        final Prefs prefs=Prefs.getInstance(context);
        if (prefs.getDefaultAccountEmailId()!=null && ConnectionUtils.isNetworkAvailable(context)) {
            if(prefs.getAutoSyncState()) {
                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        googleAccount.authenticate();
                        try {
                          googleAccount.sync();
                        } catch (IOException e) {
                            e.printStackTrace();
                            ACRA.getErrorReporter().handleException(e);
                        }
                        return null;
                    }
                };
                asyncTask.execute();
            }
        }
//        } else {
//            ConnectionUtils.showNetworkNotAvailableDialog(context, "Internet Connection is not available . Last saved" +
//                    "Goals and Steps will be synchronised when you are online");
//        }
    }
}


