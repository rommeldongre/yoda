package com.greylabs.yoda.apis.googleacc;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.greylabs.yoda.apis.Sync;
import com.greylabs.yoda.asynctask.AysncTaskWithProgressBar;
import com.greylabs.yoda.enums.AccountType;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.threads.ImportTaskAsyncThread;
import com.greylabs.yoda.utils.ConnectionUtils;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.io.IOException;
import java.util.List;
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

        Prefs prefs=Prefs.getInstance(context);
        if (prefs.getDefaultAccountEmailId()!=null && ConnectionUtils.isNetworkAvailable(context)) {
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    googleAccount.authenticate();
                    return null;
                }
            };
            asyncTask.execute();
        }
//        } else {
//            ConnectionUtils.showNetworkNotAvailableDialog(context, "Internet Connection is not available . Last saved" +
//                    "Goals and Steps will be synchronised when you are online");
//        }
    }
}


