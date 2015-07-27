package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.greylabs.yoda.R;
import com.greylabs.yoda.database.QuickStart;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

public class ActQuickStart extends Activity implements View.OnClickListener {

    Button btnQuickStart, btnImport, btnNewStep;

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
        btnQuickStart = (Button) findViewById(R.id.btnQuickStartActQuickStart);
        btnNewStep = (Button) findViewById(R.id.btnNewStepActQuickStart);
        btnImport = (Button) findViewById(R.id.btnImportTaskActQuickStart);

        btnQuickStart.setOnClickListener(this);
        btnNewStep.setOnClickListener(this);
        btnImport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Prefs prefs = Prefs.getInstance(this);
        prefs.setOptionFromActQuickStartSelected(true);
        Logger.showMsg(this, "adf");
        switch(v.getId()){
            case R.id.btnQuickStartActQuickStart :
                QuickStart quickStart = new QuickStart(this);
                quickStart.quickStart();
                startActivity(new Intent(this, ActHome.class));
                this.finish();
                break;

            case R.id.btnNewStepActQuickStart :
                break;

            case R.id.btnImportTaskActQuickStart :
                break;
        }
    }
}