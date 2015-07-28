package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.greylabs.yoda.R;
import com.greylabs.yoda.threads.NewStepAsyncTask;
import com.greylabs.yoda.threads.QuickStartAsyncTask;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

public class ActQuickStart extends Activity implements View.OnClickListener {

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
        Prefs prefs = Prefs.getInstance(this);
        prefs.setOptionFromActQuickStartSelected(true);
        switch(v.getId()){
            case R.id.rlQuickStartActQuickStart :
                new QuickStartAsyncTask(this, new MyHandler()).execute();
                break;

            case R.id.rlNewStepActQuickStart :
                new NewStepAsyncTask(this, new MyHandler()).execute();
                break;

            case R.id.rlImportTaskActQuickStart :
                Logger.showMsg(this, "import");
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