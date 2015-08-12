package com.greylabs.yoda.apis.googleacc;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Prefs;

import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;

/**
 * Created by Jaybhay Vijay on 8/11/2015.
 */
public class GoogleAccount  {

    private static final Level LOGGING_LEVEL = Level.OFF;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final String TAG = "GoogleAccount";
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_AUTHORIZATION = 1;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    final HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    GoogleAccountCredential credential;
    com.google.api.services.tasks.Tasks service;
    private Context context;

    public GoogleAccount(Context context){
        this.context=context;
        SharedPreferences settings =context.getSharedPreferences(Constants.SHARED_PREFS_ACCOUNT,Context.MODE_PRIVATE);
        settings.edit().putString(PREF_ACCOUNT_NAME,getEmail(context));
        settings.edit().commit();
        credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(TasksScopes.TASKS));
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, getEmail(context)));
        // Tasks clients
        service = new com.google.api.services.tasks.Tasks.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Google-TasksAndroidSample/1.0").build();
    }

    public Tasks getService() {
        return service;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        Dialog dialog =GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, (Activity) context,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /** Check that Google Play services APK is installed and up to date. */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }





    public TaskList buildGoal(Goal goal) {
        TaskList taskList=new TaskList();
        taskList.setId(String.valueOf(goal.getId()));
        taskList.setKind("tasks#taskList");
        taskList.setTitle(goal.getNickName());
        return taskList;
    }


    public Task buildPending(PendingStep pendingStep) {
        Task task=new Task();
        task.setId(String.valueOf(pendingStep.getId()));
        task.setKind("tasks#task");
        task.setTitle(pendingStep.getNickName());
        return task;
    }

    public Goal convertToGoal(TaskList taskList) {
        Goal goal=new Goal(context);
       boolean isStretchGoal=false;
        try {
            goal.setId(Integer.parseInt(taskList.getId()));
        }catch (Exception e){
            if(taskList.getTitle().equals("@default")) {
                goal.setId(Prefs.getInstance(context).getStretchGoalId());
                isStretchGoal=true;
            }
            else
                goal.setId(0);
        }

        goal=goal.get(goal.getId());
        if(!isStretchGoal)
            goal.setNickName(taskList.getTitle());
        return goal;
    }

    public PendingStep convertToPendingStep(Task task) {
        PendingStep pendingStep=new PendingStep(context);
        try {
            pendingStep.setId(Integer.parseInt(task.getId()));
        }catch (Exception e){
            pendingStep.setId(0);
        }

        pendingStep=pendingStep.get(pendingStep.getId());
        pendingStep.setNickName(task.getTitle());

        return pendingStep;
    }

    private String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }
    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        } return account;
    }

}
