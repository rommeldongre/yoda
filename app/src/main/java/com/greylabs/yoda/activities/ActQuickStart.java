package com.greylabs.yoda.activities;

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

//    Button rlQuickStart, rlImport, rlNewStep;
    RelativeLayout rlQuickStart, rlImport, rlNewStep;

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

    private void initialize() {
        rlQuickStart = (RelativeLayout) findViewById(R.id.rlQuickStartActQuickStart);
        rlNewStep = (RelativeLayout) findViewById(R.id.rlNewStepActQuickStart);
        rlImport = (RelativeLayout) findViewById(R.id.rlImportTaskActQuickStart);

        rlQuickStart.setOnClickListener(this);
        rlNewStep.setOnClickListener(this);
        rlImport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Prefs prefs = Prefs.getInstance(this);
        prefs.setOptionFromActQuickStartSelected(true);
        switch(v.getId()){
            case R.id.rlQuickStartActQuickStart :
                new QuickStartAsyncTask(this, new MyHandler()).execute();
                break;

            case R.id.rlNewStepActQuickStart :
                new NewStepAsyncTask(this, new MyHandler()).execute();
                break;

            case R.id.rlImportTaskActQuickStart :
                //new ImportTaskAsyncThread(this,new MyHandler()).execute();
                if(ConnectionUtils.isNetworkAvailable(this)) {
                    final GoogleAccount googleAccount = new GoogleAccount(this);
                    googleAccount.chooseAccountDialog(GoogleAccount.ACCOUNT_TYPE, "Choose Account", new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            prefs.setDefaultAccountEmailId(googleAccount.getUsers().get(position));
                            prefs.setDefaultAccountType(AccountType.GOOGLE.ordinal());
                            googleAccount.dismissChooseAccountDialog();
                        }
                    }, new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            new ImportTaskAsyncThread(ActQuickStart.this, new MyHandler()).execute();
                        }
                    });
                }else{
                    ConnectionUtils.showNetworkNotAvailableDialog(this,"Check your Internet Connection and try again. You can" +
                            "You can choose Quick Start or New Step");
                }
                break;
        }
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            startActivity(new Intent(ActQuickStart.this, ActHome.class));
            ActQuickStart.this.finish();
        }
    }
}