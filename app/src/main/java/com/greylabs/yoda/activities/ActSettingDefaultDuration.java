package com.greylabs.yoda.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.utils.ResetYoda;


public class ActSettingDefaultDuration extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    Toolbar toolbar;
    SeekBar sbStepDuration, sbSessionDuration, sbYodaSaysNotification;//, sbExportToCalendar;
    TextView tvStepDuration, tvSessionDuration, tvYodaSaysNotification;
    Paint thumbPaint, textPaint;
    RadioButton rbTopMost, rbBottomMost;
    RadioGroup rgPriorityNewStep;
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
        tvStepDuration = (TextView) findViewById(R.id.tvSbDefaultStepDurationActSettingsDefaultDuration);
        tvSessionDuration = (TextView) findViewById(R.id.tvSbDefaultSessionDurationActSettingsDefaultDuration);
        tvYodaSaysNotification = (TextView) findViewById(R.id.tvSbDefaultYodaSaysNotificationActSettingsDefaultDuration);
        rgPriorityNewStep = (RadioGroup) findViewById(R.id.rgPriorityNewStepSettingsDefaultDuration);
        rbTopMost = (RadioButton) findViewById(R.id.rbTopMostActSettingsDefaultDuration);
        rbBottomMost = (RadioButton) findViewById(R.id.rbBottomMostActSettingsDefaultDuration);
        btnResetYoda = (Button) findViewById(R.id.btnResetYodaActSettingsDefaultDuration);

        sbStepDuration.setOnSeekBarChangeListener(this);
        sbSessionDuration.setOnSeekBarChangeListener(this);
        sbYodaSaysNotification.setOnSeekBarChangeListener(this);
        btnResetYoda.setOnClickListener(this);
        rgPriorityNewStep.setOnCheckedChangeListener(this);

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
        if (prefs.isPriorityNewStepBottomMost()) {
            rbBottomMost.setChecked(true);
        } else {
            rbTopMost.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {
            case R.id.sbDefaultStepDurationActSettingsDefaultDuration:
                if (progress < 1) {
                    progress = 1;
                    sbStepDuration.setProgress(progress);
                }
                tvStepDuration.setText("" + progress);
                break;

            case R.id.sbDefaultSessionDurationActSettingsDefaultDuration:
                if (progress < 1) {
                    progress = 1;
                    sbSessionDuration.setProgress(progress);
                }
                tvSessionDuration.setText("" + progress);
                break;

            case R.id.sbDefaultYodaSaysNotificationActSettingsDefaultDuration:
                if (progress < 1) {
                    progress = 1;
                    sbYodaSaysNotification.setProgress(1);
                }
                tvYodaSaysNotification.setText("" + progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.sbDefaultStepDurationActSettingsDefaultDuration:
                prefs.setDefaultStepDuration(seekBar.getProgress());
                break;

            case R.id.sbDefaultSessionDurationActSettingsDefaultDuration:
                prefs.setDefaultSessionDuration(seekBar.getProgress());
                break;

            case R.id.sbDefaultYodaSaysNotificationActSettingsDefaultDuration:
                prefs.setYodaSaysNotificationDuration(seekBar.getProgress());
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.rbTopMostActSettingsDefaultDuration:
                if (rbTopMost.isChecked())
                    prefs.setPriorityNewStepBottomMost(false);
                break;

            case R.id.rbBottomMostActSettingsDefaultDuration:
                if (rbBottomMost.isChecked())
                    prefs.setPriorityNewStepBottomMost(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(this);
        alertLogout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ResetYoda.reset(ActSettingDefaultDuration.this);
                startActivity(new Intent(ActSettingDefaultDuration.this, ActSplashScreen.class));
                ActSettingDefaultDuration.this.finish();
            }
        });
        alertLogout.setNegativeButton("Cancel", null);
        alertLogout.setMessage(Constants.MSG_RESET_YODA);
        alertLogout.show();
    }
}