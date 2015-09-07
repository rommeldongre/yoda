package com.greylabs.yoda.activities;

import android.app.Activity;
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

import com.greylabs.yoda.R;
import com.greylabs.yoda.apis.googleacc.GoogleAccount;
import com.greylabs.yoda.enums.AccountType;
import com.greylabs.yoda.threads.ImportTaskAsyncThread;
import com.greylabs.yoda.threads.NewStepAsyncTask;
import com.greylabs.yoda.threads.QuickStartAsyncTask;
import com.greylabs.yoda.utils.ConnectionUtils;
import com.greylabs.yoda.utils.Prefs;

public class ActQuickStart extends AppCompatActivity implements View.OnClickListener {

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
        if(prefs.isOptionFromActQuickStartSelected()==true){
            startActivity(new Intent(ActQuickStart.this, ActHome.class));
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
        switch(v.getId()){
            case R.id.rlQuickStartActQuickStart :
                new QuickStartAsyncTask(this, new MyHandler(this)).execute();
                break;

            case R.id.rlNewStepActQuickStart :
                new NewStepAsyncTask(this, new MyHandler(this)).execute();
                break;

            case R.id.rlImportTaskActQuickStart :
                if(ConnectionUtils.isNetworkAvailable(this)) {
                    final GoogleAccount googleAccount = new GoogleAccount(this);
                    googleAccount.chooseAccountDialog(GoogleAccount.ACCOUNT_TYPE, "Choose Account", new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            prefs.setDefaultAccountEmailId(googleAccount.getUsers().get(position));
                            prefs.setDefaultAccountType(AccountType.GOOGLE.ordinal());
                            googleAccount.dismissChooseAccountDialog();
                            new ImportTaskAsyncThread(ActQuickStart.this, new MyHandler(contextActQuickStart)).execute();
                        }
                    }, new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
//                            new ImportTaskAsyncThread(ActQuickStart.this, new MyHandler()).execute();
                        }
                    });
                }else{
                    ConnectionUtils.showNetworkNotAvailableDialog(this,getString(R.string.msgNoNetworkConnectionActQuiCkStart));
                }
                break;
        }
    }


    private class MyHandler extends Handler {
        Context context;

        public MyHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            prefs.setOptionFromActQuickStartSelected(true);
            ((Activity)context).finish();
            startActivity(new Intent(ActQuickStart.this, ActHome.class));
        }
    }
}