package com.greylabs.yoda.activities;

import android.accounts.Account;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.greylabs.yoda.R;
import com.greylabs.yoda.apis.googleacc.GoogleAccount;
import com.greylabs.yoda.enums.AccountType;
import com.greylabs.yoda.utils.ConnectionUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.views.MyFloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ActSettingsGoogle extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Spinner accountSpinner;
    SwitchCompat autoSyncSwitch;
    MyFloatingActionButton btnExportNow, btnSyncNow, btnImportNow;
    SeekBar sbExportToCalendar;
    Paint thumbPaint, textPaint;
    ArrayList<Account> accountArrayList = new ArrayList<>();
    ArrayAdapter<String> accountSpinnerAdapter;
    Account currentAccount;
    Prefs prefs;
    boolean isAccountPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seetings_google);

        initialize();
    }

    private void initialize() {
        prefs = Prefs.getInstance(this);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(25);

        thumbPaint = new Paint();
        thumbPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(getResources().getColor(R.color.ColorPrimary));

        toolbar = (Toolbar) findViewById(R.id.toolBarActSettingsGoogle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActSettingsGoogle));

        accountSpinner = (Spinner) findViewById(R.id.spinnerAccountActSettingsGoogle);
        autoSyncSwitch = (SwitchCompat) findViewById(R.id.toggleSwitchAutoSyncActSettingsGoogle);
        btnSyncNow = (MyFloatingActionButton) findViewById(R.id.btnSyncNowWithGoogleActSettingsGoogle);
        btnImportNow = (MyFloatingActionButton) findViewById(R.id.btnImportNowFromGTasksActSettingsGoogle);
        btnExportNow = (MyFloatingActionButton) findViewById(R.id.btnExportNowToGoogleCalActSettingsGoogle);
        sbExportToCalendar = (SeekBar) findViewById(R.id.sbDefaultExportCalDurationActSettingsGoogle);

        sbExportToCalendar.setProgress(prefs.getExportToCalendarDuration());
        sbExportToCalendar.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, String.valueOf(prefs.getExportToCalendarDuration())));
        autoSyncSwitch.setChecked(prefs.getAutoSyncState());

        sbExportToCalendar.setOnSeekBarChangeListener(this);
        btnSyncNow.setOnClickListener(this);
        btnImportNow.setOnClickListener(this);
        btnExportNow.setOnClickListener(this);
        autoSyncSwitch.setOnCheckedChangeListener(this);
        accountSpinner.setOnItemSelectedListener(this);

        getUserAccounts();
    }

    private void getUserAccounts() {
        GoogleAccount googleAccount = new GoogleAccount(this);
        Account[] accounts = googleAccount.getAccounts(this, GoogleAccount.ACCOUNT_TYPE);
        ArrayList<String> emailIdsArrayList = new ArrayList<>();
        if(accounts != null){
            accountArrayList = new ArrayList<Account>(Arrays.asList(accounts));
            emailIdsArrayList = new ArrayList();
            for(Account account : accountArrayList){
                emailIdsArrayList.add(account.name);
            }
            isAccountPresent = true;
        }else {
            emailIdsArrayList.add("Please add an Account first");
            disableAllViews();
        }
        accountSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,emailIdsArrayList);
        accountSpinner.setAdapter(accountSpinnerAdapter);
        if(isAccountPresent && prefs.getDefaultAccountEmailId()!=null){
            // set selected spinner item from prefs
            int oldSelectedAccountIndex;
            for(int i=0; i<accountArrayList.size();i++) {
                if (accountArrayList.get(i).name.equals(prefs.getDefaultAccountEmailId())) {
                    oldSelectedAccountIndex = i;
                    accountSpinner.setSelection(oldSelectedAccountIndex);
                }
            }
            currentAccount = accountArrayList.get(accountSpinner.getSelectedItemPosition());
        }
    }

    private void disableAllViews() {
        btnSyncNow.setEnabled(false);
        btnImportNow.setEnabled(false);
        btnExportNow.setEnabled(false);
        autoSyncSwitch.setEnabled(false);
        sbExportToCalendar.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View v) {
        if(ConnectionUtils.isNetworkAvailable(this)) {
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    GoogleAccount googleAccount = new GoogleAccount(ActSettingsGoogle.this);
                    googleAccount.authenticate();
                    try {
                        switch (v.getId()){
                            case R.id.btnSyncNowWithGoogleActSettingsGoogle :
                                googleAccount.sync();
                                break;

                            case R.id.btnImportNowFromGTasksActSettingsGoogle:
                                googleAccount.doImport();
                                break;

                            case R.id.btnExportNowToGoogleCalActSettingsGoogle :
                                googleAccount.doExport();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            asyncTask.execute();
        }else{
            ConnectionUtils.showNetworkNotAvailableDialog(this, Constants.MSG_NO_CONNECTION);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String valueString = String.valueOf(seekBar.getProgress());
        sbExportToCalendar.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, valueString));
        if(progress<1){
            sbExportToCalendar.setProgress(1);
        }
    }
    public BitmapDrawable writeOnDrawable(int drawableId, String text){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bm);
        canvas.drawCircle(bm.getWidth() / 2, bm.getHeight() / 2, 25, thumbPaint);
        canvas.drawText(text, bm.getWidth()/2 - textPaint.measureText(text)/2, bm.getHeight()/2+7, textPaint);
        return new BitmapDrawable(bm);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        prefs.setExportToCalendarDuration(seekBar.getProgress());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        prefs.setAutoSyncState(isChecked);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(isAccountPresent){
            prefs.setDefaultAccountEmailId(accountArrayList.get(accountSpinner.getSelectedItemPosition()).name);
            prefs.setDefaultAccountType(AccountType.GOOGLE.ordinal());
            currentAccount = accountArrayList.get(accountSpinner.getSelectedItemPosition());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}