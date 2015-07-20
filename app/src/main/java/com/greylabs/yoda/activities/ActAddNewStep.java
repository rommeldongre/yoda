package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.util.ArrayList;
import java.util.List;

public class ActAddNewStep extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    Toolbar toolbar;
    EditText edtStepName;
    CardView cardViewAdvanced;
    Button btnShowAdvanced, btnHideAdvanced;
    Spinner goalSpinner, stepTypeSpinner, stepPrioritySpinner;
    SeekBar  sbNoOfSteps, sbTimeSeriesStep, sbTimeSingleStep;
    LinearLayout singleStepPanel, seriesPanel;
    ScrollView scrollView;
    ArrayAdapter<String> spinnerArrayAdapter;
    List<Goal> goalList = new ArrayList<>();
    ArrayList<String> goalNamesList = new ArrayList<>();
    Goal currentGoal;
    static int goalChosen;
    PendingStep currentStep;
    Paint thumbPaint, textPaint;
    ArrayList<PendingStep> stepArrayList = new ArrayList<>();
    Prefs prefs;
    int newPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_step);
        initialize();
    }

    private void initialize() {
        prefs = Prefs.getInstance(this);
        toolbar = (Toolbar) findViewById(R.id.toolBarActAddNewStep);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActAddNewStep));

        scrollView = (ScrollView) findViewById(R.id.scrollViewAvtAddNewStep);
        edtStepName = (EditText) findViewById(R.id.edtStepNameActAddNewStep);
        goalSpinner = (Spinner) findViewById(R.id.spinnerGoalNameActAddNewStep);
        btnShowAdvanced = (Button) findViewById(R.id.btnShowAdvancedActAddNewStep);

        cardViewAdvanced = (CardView) findViewById(R.id.cardViewAdvancedActAddNewStep);
        btnHideAdvanced = (Button) findViewById(R.id.btnHideAdvancedActAddNewStep);
        stepPrioritySpinner = (Spinner) findViewById(R.id.spinnerPriorityActAddNewStep);
        stepTypeSpinner = (Spinner) findViewById(R.id.spinnerStepTypeActAddNewStep);
//        sbTimeSingleStep = (SeekBar) findViewById(R.id.seekbarSingleStepTimeActAddNewStep);
        sbTimeSingleStep = (SeekBar) findViewById(R.id.seekbarSingleStepTimeActAddNewStep);
        sbNoOfSteps = (SeekBar) findViewById(R.id.seekbarStepsInSeriesActAddNewStep);
        sbTimeSeriesStep = (SeekBar) findViewById(R.id.seekbarTimeForEachStepActAddNewStep);
        singleStepPanel = (LinearLayout) findViewById(R.id.SingleStepPanelActAddNewStep);
        seriesPanel = (LinearLayout) findViewById(R.id.SeriesPanelActAddNewStep);

        //get defaults from settings and set Spinners accordingly


        getGoalListAndPopulate();

        sbTimeSingleStep.setOnSeekBarChangeListener(this);
        sbNoOfSteps.setOnSeekBarChangeListener(this);
        sbTimeSeriesStep.setOnSeekBarChangeListener(this);
        btnShowAdvanced.setOnClickListener(this);
        btnHideAdvanced.setOnClickListener(this);
        goalSpinner.setOnItemSelectedListener(this);
        stepTypeSpinner.setOnItemSelectedListener(this);
        stepPrioritySpinner.setOnItemSelectedListener(this);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(25);

        thumbPaint = new Paint();
        thumbPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(getResources().getColor(R.color.ColorPrimary));

        setDefaultValues();
        getStepArrayFromLocal();

        currentStep = new PendingStep(this);
    }

    private void setDefaultValues() {
        sbTimeSingleStep.setProgress(prefs.getDefaultStepDuration());
        sbTimeSeriesStep.setProgress(prefs.getDefaultStepDuration());
        if(prefs.isPriorityNewStepBottomMost()){
            //choose bottom most
            stepPrioritySpinner.setSelection(1);
        }else {
            stepPrioritySpinner.setSelection(0);
        }
    }


    private void getStepArrayFromLocal() {
        stepArrayList.clear();
        PendingStep pendingStep = new PendingStep(this);
        currentGoal = goalList.get(goalSpinner.getSelectedItemPosition());
        if(currentGoal != null && pendingStep.getAll(currentGoal.getId()) != null)
            stepArrayList.addAll(pendingStep.getAll(currentGoal.getId()));
    }

    private void getGoalListAndPopulate() {
        // check context and populate spinner else show only one currentGoal
        Intent intent = getIntent();
        if(intent.getExtras().getBoolean(Constants.GOAL_ATTACHED_IN_EXTRAS)){
            currentGoal  = (Goal)intent.getSerializableExtra(Constants.GOAL_OBJECT);
            goalList.add(currentGoal);
            goalNamesList.add(currentGoal.getNickName());
        }else if(!intent.getExtras().getBoolean(Constants.GOAL_ATTACHED_IN_EXTRAS)){
            getGoalListFromLocal();
        }
        goalNamesList.add(getResources().getString(R.string.addNewGoalSpinnerItemActAddNewStep));//add new Goal option
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goalNamesList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(spinnerArrayAdapter);
        goalSpinner.setSelection(0);
    }

    private void getGoalListFromLocal() {
        currentGoal = new Goal(this);
        goalList = currentGoal.getAll();
//        if(goalList.size()>0){
//            goalList.clear();
//            goalNamesList.clear();
//        }
        if(goalList != null){
            for(int i=0; i< goalList.size();i++){
                goalNamesList.add(goalList.get(i).getNickName());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_add_new_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
            case R.id.actionSaveActAddNewStep :
                saveStep();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStep() {
        currentStep = new PendingStep(this);
        if(edtStepName.getText() != null && edtStepName.getText().length() > 0){
//            currentStep.setNickName(edtStepName.getText().toString());
//            currentStep.setPriority();
//            currentStep.setTime();
//            currentStep.setSeries();
//            currentStep.setStepCount();
////            currentStep.setSkipCount();
//            currentStep.setGoalId();
//            currentStep.save();
            Logger.showMsg(this, getResources().getString(R.string.msgStepSavedActAddNewStep));
            this.finish();
        }else {
            Logger.showMsg(this, getResources().getString(R.string.msgEnterStepNameActAddNewStep));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnShowAdvancedActAddNewStep:
                btnShowAdvanced.setVisibility(View.GONE);
                cardViewAdvanced.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                break;

            case R.id.btnHideAdvancedActAddNewStep :
                cardViewAdvanced.setVisibility(View.GONE);
                btnShowAdvanced.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_UP);
                    }
                });
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinnerGoalNameActAddNewStep :
                if(position+1 == goalNamesList.size()){
                Intent intent = new Intent(this, ActAddNewGoal.class);
                intent.putExtra(Constants.CALLER, Constants.ACT_ADD_NEW_STEP);
                intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                this.startActivity(intent);
                }else{
                    goalChosen = position;
                    getStepArrayFromLocal();
                }
                break;

            case R.id.spinnerPriorityActAddNewStep :
                if(stepPrioritySpinner.getSelectedItem().toString().equals("Change Manually")){
                    if(edtStepName.getText()!=null && edtStepName.getText().length()>0){
                        if(stepArrayList.size()>0)
                            stepArrayList.clear();
                        getStepArrayFromLocal();
                        stepArrayList.add(generateCurrentStepObject());

                        Intent i = new Intent(this, ActStepList.class);
//                    //--------------------- send the current step object also
                        i.putExtra(Constants.STEP_ARRAY_LIST, stepArrayList);
                        i.putExtra(Constants.GOAL_OBJECT, goalList.get(goalSpinner.getSelectedItemPosition()));
                        i.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                        i.putExtra(Constants.CALLER, Constants.ACT_ADD_NEW_STEP);
                        startActivityForResult(i, 1);
                    }else {
                        Logger.showMsg(this, Constants.MSG_ENTER_STEP_NAME);
                        if(prefs.isPriorityNewStepBottomMost()){
                            stepPrioritySpinner.setSelection(1);
                        }else {
                            stepPrioritySpinner.setSelection(0);
                        }
                    }
                }
                break;

            case R.id.spinnerStepTypeActAddNewStep :
                if(position == 0){
                    singleStepPanel.setVisibility(View.VISIBLE);
                    seriesPanel.setVisibility(View.GONE);
                }else if(position == 1){
                    singleStepPanel.setVisibility(View.GONE);
                    seriesPanel.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        String valueString = String.valueOf(seekBar.getProgress());
        seekBar.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, valueString));

        switch (seekBar.getId()){
            case R.id.seekbarSingleStepTimeActAddNewStep :
                break;

            case R.id.seekbarStepsInSeriesActAddNewStep :
                break;

            case R.id.seekbarTimeForEachStepActAddNewStep :
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public BitmapDrawable writeOnDrawable(int drawableId, String text){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bm);


        canvas.drawCircle(bm.getWidth() / 2, bm.getHeight() / 2, 25, thumbPaint);
        canvas.drawText(text, bm.getWidth()/2 - textPaint.measureText(text)/2, bm.getHeight()/2+7, textPaint);
        return new BitmapDrawable(bm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        goalSpinner.setSelection(goalChosen);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.RESULTCODE_OF_ACT_ADD_GOAL && data.getExtras().getBoolean(Constants.GOAL_ATTACHED_IN_EXTRAS)){// result from ActAddNewGoal
            getGoalListFromLocal();
            goalList.add((Goal) data.getExtras().getSerializable(Constants.GOAL_OBJECT));
            goalNamesList.add(((Goal) data.getExtras().getSerializable(Constants.GOAL_OBJECT)).getNickName());
            goalSpinner.setSelection(goalList.size()-1);
            goalChosen = goalList.size()-1;
        }else if (resultCode == Constants.RESULTCODE_OF_ACT_STEP_LIST){             // result from ActStepList
            if(!stepArrayList.isEmpty())
                stepArrayList.clear();
            stepArrayList = (ArrayList<PendingStep>) data.getSerializableExtra(Constants.STEPS_ARRAY_LIST_WITH_NEW_PRIORITIES);

            for (int i = 0; i<stepArrayList.size(); i++){
                if(currentStep.getNickName().equals(stepArrayList.get(i).getNickName()))
                    newPosition = i+1;
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stepPrioritySpinner.setAdapter(spinnerAdapter);
            spinnerAdapter.add(""+newPosition);
            spinnerAdapter.add("Top-most");
            spinnerAdapter.add("Bottom-most");
            spinnerAdapter.add("Change Manually");
            stepPrioritySpinner.setSelection(0);
        }else if(resultCode == Activity.RESULT_CANCELED) {
            getStepArrayFromLocal();
            if(prefs.isPriorityNewStepBottomMost()){
                //choose bottom most
                stepPrioritySpinner.setSelection(1);
            }else {
                stepPrioritySpinner.setSelection(0);
            }
        }
    }

    private PendingStep generateCurrentStepObject() {

        if(edtStepName.getText()!=null && edtStepName.getText().length()>0){
            currentStep.setNickName(edtStepName.getText().toString());
        }else {
            Logger.showMsg(this, Constants.MSG_ENTER_STEP_NAME);
        }
        currentStep.setGoalId(goalList.get(goalSpinner.getSelectedItemPosition()).getId());
        if(stepTypeSpinner.getSelectedItemPosition()==0){
            currentStep.setStepCount(1);
            currentStep.setTime(sbTimeSingleStep.getProgress());
            if(sbTimeSingleStep.getProgress() > Constants.MAX_SLOT_DURATION){
                currentStep.setPendingStepType(PendingStep.PendingStepType.SPLIT_STEP);
            }else {
                currentStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
            }
        }else if (stepTypeSpinner.getSelectedItemPosition()==1){
            currentStep.setStepCount(sbNoOfSteps.getProgress());
            currentStep.setTime(sbTimeSeriesStep.getProgress());
            currentStep.setPendingStepType(PendingStep.PendingStepType.SERIES_STEP);
        }
        return currentStep;
    }
}