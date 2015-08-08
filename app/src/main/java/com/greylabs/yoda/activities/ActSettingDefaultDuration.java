package com.greylabs.yoda.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.utils.ResetYoda;


public class ActSettingDefaultDuration extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    Toolbar toolbar;
    SeekBar sbStepDuration, sbSessionDuration, sbYodaSaysNotification, sbExportToCalendar;
    Paint thumbPaint, textPaint;
    RadioButton rbTopMost, rbBottomMost, rbDontExpire, rbExpire;
    RadioGroup rgPriorityNewStep, rgBehaviourOfExpiredSteps;
    Button btnResetYoda;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_default_duration);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolBarActSettingsDefaultDuration);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActSettingDefaultDuration));

        sbStepDuration = (SeekBar) findViewById(R.id.sbDefaultStepDurationActSettingsDefaultDuration);
        sbSessionDuration = (SeekBar) findViewById(R.id.sbDefaultSessionDurationActSettingsDefaultDuration);
        sbYodaSaysNotification = (SeekBar) findViewById(R.id.sbDefaultYodaSaysNotificationActSettingsDefaultDuration);
        sbExportToCalendar = (SeekBar) findViewById(R.id.sbDefaultExportToCalendarActSettingsDefaultDuration);
        rgPriorityNewStep = (RadioGroup) findViewById(R.id.rgPriorityNewStepSettingsDefaultDuration);
        rgBehaviourOfExpiredSteps = (RadioGroup) findViewById(R.id.rgBehaviourOfExpiredStepsSettingsDefaultDuration);
        rbTopMost = (RadioButton) findViewById(R.id.rbTopMostActSettingsDefaultDuration);
        rbBottomMost = (RadioButton) findViewById(R.id.rbBottomMostActSettingsDefaultDuration);
        rbDontExpire = (RadioButton) findViewById(R.id.rbDontExpireActSettingsDefaultDuration);
        rbExpire = (RadioButton) findViewById(R.id.rbExpireActSettingsDefaultDuration);
        btnResetYoda = (Button) findViewById(R.id.btnResetYodaActSettingsDefaultDuration);

        sbStepDuration.setOnSeekBarChangeListener(this);
        sbSessionDuration.setOnSeekBarChangeListener(this);
        sbYodaSaysNotification.setOnSeekBarChangeListener(this);
        sbExportToCalendar.setOnSeekBarChangeListener(this);
        btnResetYoda.setOnClickListener(this);

//        rbTopMost.setOnCheckedChangeListener(this);
//        rbBottomMost.setOnCheckedChangeListener(this);
//        rbDontExpire.setOnCheckedChangeListener(this);
//        rbExpire.setOnCheckedChangeListener(this);

        rgPriorityNewStep.setOnCheckedChangeListener(this);
        rgBehaviourOfExpiredSteps.setOnCheckedChangeListener(this);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(25);

        thumbPaint = new Paint();
        thumbPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(getResources().getColor(R.color.ColorPrimary));

        prefs = Prefs.getInstance(this);

        sbStepDuration.setProgress(prefs.getDefaultStepDuration());
        sbSessionDuration.setProgress(prefs.getDefaultSessionDuration());
        sbYodaSaysNotification.setProgress(prefs.getYodaSaysNotificationDuration());
        sbExportToCalendar.setProgress(prefs.getExportToCalendarDuration());
        if(prefs.isPriorityNewStepBottomMost()){
            rbBottomMost.setChecked(true);
        }else {
            rbTopMost.setChecked(true);
        }
        if(prefs.isBehaviourDoNotExpire()){
            rbDontExpire.setChecked(true);
        }else {
            rbExpire.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_act_add_new_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String valueString = String.valueOf(seekBar.getProgress());
        seekBar.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, valueString));

//        switch (seekBar.getId()){
//            case R.id.sbDefaultStepDurationActSettingsDefaultDuration :
//                break;
//
//            case R.id.sbDefaultSessionDurationActSettingsDefaultDuration :
//                break;
//
//            case R.id.sbDefaultYodaSaysNotificationActSettingsDefaultDuration :
//                break;
//
//            case R.id.sbDefaultExportToCalendarActSettingsDefaultDuration :
//                break;
//        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()){
            case R.id.sbDefaultStepDurationActSettingsDefaultDuration :
                prefs.setDefaultStepDuration(seekBar.getProgress());
                break;

            case R.id.sbDefaultSessionDurationActSettingsDefaultDuration :
                prefs.setDefaultSessionDuration(seekBar.getProgress());
                break;

            case R.id.sbDefaultYodaSaysNotificationActSettingsDefaultDuration :
                prefs.setYodaSaysNotificationDuration(seekBar.getProgress());
                break;

            case R.id.sbDefaultExportToCalendarActSettingsDefaultDuration :
                prefs.setExportToCalendarDuration(seekBar.getProgress());
                break;
        }
    }

    public BitmapDrawable writeOnDrawable(int drawableId, String text){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bm);
        canvas.drawCircle(bm.getWidth() / 2, bm.getHeight() / 2, 25, thumbPaint);
        canvas.drawText(text, bm.getWidth()/2 - textPaint.measureText(text)/2, bm.getHeight()/2+7, textPaint);
        return new BitmapDrawable(bm);
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()){
//
//            case R.id.rbTopMostActSettingsDefaultDuration :
//                Logger.showMsg(this, "Top Most");
//                break;
//
//            case R.id.rbBottomMostActSettingsDefaultDuration :
//                Logger.showMsg(this, "Bottom Most");
//                break;
//
//            case R.id.rbDontExpireActSettingsDefaultDuration :
//                Logger.showMsg(this, "Dont expire");
//                break;
//
//            case R.id.rbExpireActSettingsDefaultDuration :
//                Logger.showMsg(this, "Expire");
//                break;
//        }
//    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){

            case R.id.rbTopMostActSettingsDefaultDuration :
                if(rbTopMost.isChecked())
                    prefs.setPriorityNewStepBottomMost(false);
                break;

            case R.id.rbBottomMostActSettingsDefaultDuration :
                if(rbBottomMost.isChecked())
                    prefs.setPriorityNewStepBottomMost(true);
                break;

            case R.id.rbDontExpireActSettingsDefaultDuration :
                if(rbDontExpire.isChecked())
                    prefs.setBehaviourDoNotExpire(true);
                break;

            case R.id.rbExpireActSettingsDefaultDuration :
                if(rbExpire.isChecked())
                    prefs.setBehaviourDoNotExpire(false);
                break;
        }
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btnResetYodaActSettingsDefaultDuration :
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
        alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                ProgressDialog progressDialog = new ProgressDialog(ActSettingDefaultDuration.this);
//                progressDialog.setMessage(Constants.MSG_RESETTING_YODA);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progressDialog.setCancelable(false);
//                progressDialog.show();
                ResetYoda.reset(ActSettingDefaultDuration.this);
//                progressDialog.dismiss();
                startActivity(new Intent(ActSettingDefaultDuration.this, ActSplashScreen.class));
                ActSettingDefaultDuration.this.finish();
            }
        });
        alertLogout.setNegativeButton("Cancel", null);
        alertLogout.setMessage(Constants.MSG_RESET_YODA);
        alertLogout.show();
//                break;
//        }
    }
}