package com.greylabs.yoda.activities;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.greylabs.yoda.R;
import com.greylabs.yoda.apis.googleacc.GoogleAccount;
import com.greylabs.yoda.enums.AccountType;
import com.greylabs.yoda.threads.ImportTaskAsyncThread;
import com.greylabs.yoda.threads.NewStepAsyncTask;
import com.greylabs.yoda.threads.QuickStartAsyncTask;
import com.greylabs.yoda.utils.ConnectionUtils;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

public class ActQuickStart extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ActQuickSt";
    RelativeLayout rlQuickStart, rlImport, rlNewStep;
    Prefs prefs = Prefs.getInstance(this);
    Context contextActQuickStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_start);

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.isOptionFromActQuickStartSelected() == true) {
            startActivity(new Intent(ActQuickStart.this, ActHome.class));
            Logger.d(TAG, "FINiSHED 1");
            this.finish();
        }
    }

    private void initialize() {
        contextActQuickStart = this;
        rlQuickStart = (RelativeLayout) findViewById(R.id.rlQuickStartActQuickStart);
        rlNewStep = (RelativeLayout) findViewById(R.id.rlNewStepActQuickStart);
        rlImport = (RelativeLayout) findViewById(R.id.rlImportTaskActQuickStart);

        rlQuickStart.setOnClickListener(this);
        rlNewStep.setOnClickListener(this);
        rlImport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlQuickStartActQuickStart:
                new QuickStartAsyncTask(this, new MyHandlerQuickStart(this)).execute();
                break;

            case R.id.rlNewStepActQuickStart:
                new NewStepAsyncTask(this, new MyHandlerNewStep(this)).execute();
                break;

            case R.id.rlImportTaskActQuickStart:
                if (ConnectionUtils.isNetworkAvailable(this)) {
                    final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                    if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
                        GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, this, 0).show();
                    } else {
                        //otherwise doImport
                        final GoogleAccount googleAccount = new GoogleAccount(this);
                        Account[] accounts = googleAccount.getAccounts(this, GoogleAccount.ACCOUNT_TYPE);
                        if (accounts.length > 0) {
                            googleAccount.chooseAccountDialog(GoogleAccount.ACCOUNT_TYPE, "Choose Account", new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    prefs.setDefaultAccountEmailId(googleAccount.getUsers().get(position));
                                    prefs.setDefaultAccountType(AccountType.GOOGLE.ordinal());
                                    googleAccount.dismissChooseAccountDialog();
                                    new ImportTaskAsyncThread(ActQuickStart.this, new MyHandlerImportTask(ActQuickStart.this)).execute();
                                }
                            }, null);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            builder.setTitle("Alert");
                            builder.setMessage(getString(R.string.msgNoAccountActQuickStart));
                            builder.setPositiveButton("Ok", null);
                            builder.create().show();
                        }
                    }
                } else {
                    ConnectionUtils.showNetworkNotAvailableDialog(this, getString(R.string.msgNoNetworkConnectionActQuiCkStart));
                }
                break;
        }
    }


    private class MyHandlerQuickStart extends Handler {
        Context context;

        public MyHandlerQuickStart(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            prefs.setOptionFromActQuickStartSelected(true);
            startActivity(new Intent(ActQuickStart.this, ActHome.class));
            Logger.d(TAG, "FINiSHED 2");
            ((Activity) context).finish();

        }
    }

    private class MyHandlerNewStep extends Handler {
        Context context;

        public MyHandlerNewStep(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            prefs.setOptionFromActQuickStartSelected(true);
            startActivity(new Intent(ActQuickStart.this, ActHome.class));

            Logger.d(TAG, "FINiSHED 3");
            ((Activity) context).finish();
        }
    }


    private class MyHandlerImportTask extends Handler {
        Context context;

        public MyHandlerImportTask(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            Integer success = msg.arg1;
            Logger.d(TAG, "Handled message from ImportTask Async,result is :" + success);
            if (success == 1) {
                Logger.d(TAG, "Handled message from ImportTask Async,inside :" + success);
                prefs.setOptionFromActQuickStartSelected(true);
                startActivity(new Intent(ActQuickStart.this, ActHome.class));
                Logger.d(TAG, "FINiSHED 4");
                ((Activity) context).finish();
            } else if (success == 2) {
                Logger.d(TAG, "Handled message from ImportTask Async,inside :" + success);
                startActivityForResult((Intent) msg.obj, 1);
                Logger.showMsg(context, "Authenticating...");
            } else {
                Logger.d(TAG, "Handled message from ImportTask Async,inside :" + success);
                Logger.showMsg(context, "Failed to sync. Try again.");
            }
            Logger.d(TAG, "Handled message from ImportTask Async, end of handleMsg");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Logger.d(TAG, "Authorization Successful and importing tasks from server");
            new ImportTaskAsyncThread(ActQuickStart.this, new MyHandlerImportTask(ActQuickStart.this)).execute();
        } else {
            Logger.showMsg(this, "Unable to import tasks. ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "On Destroy");
    }
}