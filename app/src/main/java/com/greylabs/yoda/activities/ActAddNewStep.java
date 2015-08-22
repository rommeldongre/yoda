package com.greylabs.yoda.activities;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterGoalSpinner;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActAddNewStep extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    Toolbar toolbar;
    EditText edtStepName;
    CardView cardViewAdvanced;
    Button btnShowAdvanced, btnHideAdvanced;
    Spinner goalSpinner, stepTypeSpinner, stepPrioritySpinner;
    SeekBar sbNoOfSteps, sbTimeSeriesStep, sbTimeSingleStep;
    LinearLayout singleStepPanel, seriesPanel;
    ScrollView scrollView;
    AdapterGoalSpinner adapterGoalSpinner;
//    ArrayAdapter<String> spinnerArrayAdapter;
    List<Goal> goalList = new ArrayList<>();
    ArrayList<String> goalNamesList = new ArrayList<>();
    Goal currentGoal;
    static int goalChosen;
    PendingStep currentStep;
    Paint thumbPaint, textPaint;
    ArrayList<PendingStep> stepArrayList = new ArrayList<>();
    Prefs prefs;
    int newPosition = 0;
    String caller;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_step);
        initialize();
    }

    private void initialize() {
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(25);

        thumbPaint = new Paint();
        thumbPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(getResources().getColor(R.color.ColorPrimary));

        intent = getIntent();
        caller = intent.getStringExtra(Constants.CALLER);
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

        setDefaultValues();
        getStepArrayFromLocal();
    }

    private void setDefaultValues() {
        sbTimeSingleStep.setProgress(prefs.getDefaultStepDuration());
        sbTimeSingleStep.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, String.valueOf(prefs.getDefaultStepDuration())));
        sbTimeSeriesStep.setProgress(prefs.getDefaultStepDuration());
        sbTimeSeriesStep.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, String.valueOf(prefs.getDefaultStepDuration())));
        if (prefs.isPriorityNewStepBottomMost()) {
            //choose bottom most
            stepPrioritySpinner.setSelection(1);
        } else {
            stepPrioritySpinner.setSelection(0);
        }
    }


    private void getStepArrayFromLocal() {
        stepArrayList.clear();
        PendingStep pendingStep = new PendingStep(this);
        currentGoal = goalList.get(goalSpinner.getSelectedItemPosition());
//        if (currentGoal != null && pendingStep.getAll(currentGoal.getId()) != null)
//            if(pendingStep.getAll(PendingStep.PendingStepStatus.TODO, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,currentGoal.getId())!=null)
//                stepArrayList.addAll(pendingStep.getAll(PendingStep.PendingStepStatus.TODO,currentGoal.getId()));

        List<PendingStep> temp=pendingStep.getAll(PendingStep.PendingStepStatus.TODO,
                PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,currentGoal.getId());
        if(currentGoal!=null && temp!=null){
            stepArrayList.addAll(temp);
        }
 }

    private void getGoalListAndPopulate() {
        // check context and populate spinner else show only one currentGoal
        switch (caller){

            case Constants.ACT_HOME :
                getGoalListFromLocal();
                currentStep = new PendingStep(this);
                break;

//            case Constants.ACT_ADD_NEW_GOAL:
//                currentGoal = (Goal) intent.getSerializableExtra(Constants.GOAL_OBJECT);
//                goalList.add(currentGoal);
//                goalNamesList.add(currentGoal.getNickName());
//                currentStep = new PendingStep(this);
//                break;

            case Constants.ACT_STEP_LIST :
                if(intent.getStringExtra(Constants.OPERATION).equals(Constants.OPERATION_ADD)){
                    getGoalListFromLocal();
                    currentGoal = (Goal) intent.getSerializableExtra(Constants.GOAL_OBJECT);
                    int oldGoalPosition = 0;
                    for(int i=0; i<goalList.size();i++){
                        if(currentGoal.getId() == goalList.get(i).getId())
                            oldGoalPosition = i;
                    }
                    goalSpinner.setSelection(oldGoalPosition);
                    goalChosen = oldGoalPosition;
//                    getGoalListFromLocal();
                    currentStep = new PendingStep(this);
                }else {
                    currentStep = (PendingStep) intent.getSerializableExtra(Constants.STEP_OBJECT);
                    currentStep.initDatabase(this);
                    getGoalListFromLocal();
                    getSupportActionBar().setTitle(currentStep.getNickName());
                    edtStepName.setText(currentStep.getNickName());

                    int oldGoalPosition = 0;
                    for(int i=0; i<goalList.size();i++){
                        if(currentStep.getGoalId() == goalList.get(i).getId())
                            oldGoalPosition = i;
                    }
                    goalSpinner.setSelection(oldGoalPosition);
                    goalChosen = oldGoalPosition;

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stepPrioritySpinner.setAdapter(spinnerAdapter);
                    spinnerAdapter.add(String.valueOf(currentStep.getPriority()));
//                    spinnerAdapter.add("Top-most");
//                    spinnerAdapter.add("Bottom-most");
//                    spinnerAdapter.add("Change Manually");
//                    stepPrioritySpinner.setSelection(0);
                    stepPrioritySpinner.setEnabled(false);

                    if(currentStep.getPendingStepType() == PendingStep.PendingStepType.SINGLE_STEP){
                        stepTypeSpinner.setSelection(0);
                        sbTimeSingleStep.setProgress(currentStep.getTime());
                        sbTimeSingleStep.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, String.valueOf(currentStep.getTime())));
                    }else {
                        stepTypeSpinner.setSelection(1);
                        sbTimeSeriesStep.setProgress(currentStep.getTime());
                        sbTimeSeriesStep.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, String.valueOf(currentStep.getTime())));
                        sbNoOfSteps.setProgress(currentStep.getStepCount());
                        sbNoOfSteps.setThumb(writeOnDrawable(R.drawable.ic_btn_plus_sign, String.valueOf(currentStep.getStepCount())));
                    }
                }
                break;
        }
//        if(!caller.equals(Constants.ACT_ADD_NEW_GOAL)){
//            goalNamesList.add(getResources().getString(R.string.addNewGoalSpinnerItemActAddNewStep));//add new Goal option
//            Goal tempGoal = new Goal(this);
//            tempGoal.setNickName(getResources().getString(R.string.addNewGoalSpinnerItemActAddNewStep));
//            goalList.add(tempGoal);
//        }

//        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goalNamesList);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterGoalSpinner = new AdapterGoalSpinner(this, goalList);
        goalSpinner.setAdapter(adapterGoalSpinner);
        goalSpinner.setSelection(0);
    }

    private void getGoalListFromLocal() {
        if(goalList.size()>0){
            goalList.clear();
        }
        currentGoal = new Goal(this);
        List<Goal> temp=currentGoal.getAll(Goal.GoalDeleted.SHOW_NOT_DELETED);
        if(temp!=null)
            goalList.addAll(temp);
//        if (goalList != null) {
//            for (int i = 0; i < goalList.size(); i++) {
//                goalNamesList.add(goalList.get(i).getNickName());
//            }
//        }
        Goal tempGoal = new Goal(this);
        tempGoal.setNickName(Constants.ADD_NEW_GOAL_SPINNER_ITEM);
        goalList.add(tempGoal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_add_new_step, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.actionSaveActAddNewStep:
                saveStep();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStep() {
        if (edtStepName.getText() != null && edtStepName.getText().length() > 0) {
            if(currentStep.getId()!=0){
                //change updated date only if nickname is different than previous
                if(currentStep.getNickName().equals(edtStepName.getText().toString()))
                    currentStep.setUpdated(new DateTime(new Date()));
            }else{
                currentStep.setUpdated(new DateTime(new Date()));
            }
            currentStep.setNickName(edtStepName.getText().toString());
            currentStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);

            if (stepTypeSpinner.getSelectedItem().equals(Constants.PENDING_STEP_TYPE_SINGLE_STEP)) {
                if (sbTimeSingleStep.getProgress() > 3) {
                    currentStep.setPendingStepType(PendingStep.PendingStepType.SPLIT_STEP);
                    currentStep.setStepCount(sbTimeSingleStep.getProgress() / 3);
                } else {
                    currentStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
                    currentStep.setStepCount(1);
                }
                currentStep.setTime(sbTimeSingleStep.getProgress());
            } else {
                currentStep.setPendingStepType(PendingStep.PendingStepType.SERIES_STEP);
                currentStep.setTime(sbTimeSeriesStep.getProgress());
                currentStep.setStepCount(sbNoOfSteps.getProgress());
            }
//            currentStep.setSkipCount();
            currentStep.setGoalId(currentGoal.getId());
            if(currentStep.getStringId()==null || currentStep.getStringId().equals(""))
                currentStep.setGoalStringId(currentGoal.getStringId());


            if (stepPrioritySpinner.getSelectedItem().equals(Constants.PENDING_STEP_PRIORITY_TOP_MOST)) {
                stepArrayList.add(0, currentStep);
            } else if (stepPrioritySpinner.getSelectedItem().equals(Constants.PENDING_STEP_PRIORITY_BOTTOM_MOST)) {
                stepArrayList.add(currentStep);
            }
//            else {
//                stepArrayList.add(Integer.parseInt(stepPrioritySpinner.getSelectedItem().toString()) - 1, currentStep);
//            }
            TimeBox timeBox = new TimeBox(this);
            YodaCalendar yodaCalendar = new YodaCalendar(this, timeBox.get(currentGoal.getTimeBoxId()));
            //save all the steps in the array with priorities
            for (int i = 0; i < stepArrayList.size(); i++) {
                stepArrayList.get(i).initDatabase(this);
                stepArrayList.get(i).setPriority(i + 1);
                stepArrayList.get(i).setUpdated(new DateTime(new Date()));
                stepArrayList.get(i).save();

                stepArrayList.get(i).updateSubSteps();
            }
            PendingStep ps =currentStep;
            switch (ps.getPendingStepType()){
                case SPLIT_STEP:
                    ps.save();
                    ps.markSubSteps(true);
                    ps.freeSlots();
                    if(ps.getTime()>Constants.MAX_SLOT_DURATION){
                        float numberOfSteps=(float)ps.getTime()/Constants.MAX_SLOT_DURATION;
                        Float f=new Float(numberOfSteps);
                        ps.createSubSteps(1, f.intValue(), Constants.MAX_SLOT_DURATION);
                        if(numberOfSteps-f.intValue()>0.0f)
                            ps.createSubSteps(f.intValue()+1,f.intValue()+1,currentStep.getTime()%Constants.MAX_SLOT_DURATION);
                    }
                    break;
                case SERIES_STEP:
                    ps.save();
                    ps.markSubSteps(true);
                    ps.freeSlots();
                    ps.createSubSteps(1, currentStep.getStepCount(), currentStep.getTime());
                    break;
            }

            //assume default priority is bottom most irrespective of settings
            boolean isScheduled = yodaCalendar.scheduleStep(currentStep);
            if (!isScheduled) {
                switch (currentStep.getPendingStepType()){
                    case SINGLE_STEP:
                        currentStep.setDeleted(true);
                        currentStep.freeSlot();
                        currentStep.delete();
                        break;
                    case SPLIT_STEP:
                    case SERIES_STEP:
                        currentStep.markSubSteps(true);
                        currentStep.freeSlots();
                        currentStep.deleteAllPendingSteps();
                        currentStep.delete();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.msgYodaSays));
                builder.setMessage(getString(R.string.msgCannotSaveStepActAddNewStep));
                builder.setPositiveButton(getString(R.string.btnOk), null);
                builder.show();
            } else {
                //if user sets priority to Manual or TopMost ,then need to rearrange steps
                if (!stepPrioritySpinner.getSelectedItem().toString().equals(Constants.TEXT_PRIORITY_SPINNER_BOTTOM_MOST)) {
                    yodaCalendar.rescheduleSteps(goalList.get(goalSpinner.getSelectedItemPosition()).getId());
                }
                currentStep = currentStep.get(currentStep.getId());
                AlertDialog.Builder alertStepAdded = new AlertDialog.Builder(this);
                alertStepAdded.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActAddNewStep.this.finish();
                    }
                });
                alertStepAdded.setMessage("Step " + currentStep.getNickName() + " added towards Goal " +
                        currentGoal.getNickName() + " with Done date of " +
                        CalendarUtils.getOnlyFormattedDate(currentStep.getStepDate()));
                alertStepAdded.setCancelable(false);
                alertStepAdded.show();
            }
        } else {
            Logger.showMsg(this, getResources().getString(R.string.msgEnterStepNameActAddNewStep));
        }
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnShowAdvancedActAddNewStep:
                btnShowAdvanced.setVisibility(View.GONE);
                cardViewAdvanced.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                break;

            case R.id.btnHideAdvancedActAddNewStep:
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
        switch (parent.getId()) {
            case R.id.spinnerGoalNameActAddNewStep:
//                if (position + 1 == goalNamesList.size()) {
                if (position + 1 == goalList.size()) {
                    Intent intent = new Intent(this, ActAddNewGoal.class);
                    intent.putExtra(Constants.CALLER, Constants.ACT_ADD_NEW_STEP);
                    intent.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                    this.startActivityForResult(intent, Constants.REQUEST_CODE_ACT_ADD_NEW_STEP);
                } else {
                    goalChosen = position;
                    getStepArrayFromLocal();
                }
                break;

//            case R.id.spinnerPriorityActAddNewStep:
//                if (stepPrioritySpinner.getSelectedItem().toString().equals("Change Manually")) {
//                    if (edtStepName.getText() != null && edtStepName.getText().length() > 0) {
//                        if (stepArrayList.size() > 0)
//                            stepArrayList.clear();
//                        getStepArrayFromLocal();
//                        stepArrayList.add(generateCurrentStepObject());
//
//                        Intent i = new Intent(this, ActStepList.class);
////                    //--------------------- send the current step object also
//                        i.putExtra(Constants.STEP_ARRAY_LIST, stepArrayList);
//                        i.putExtra(Constants.GOAL_OBJECT, goalList.get(goalSpinner.getSelectedItemPosition()));
//                        i.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
//                        i.putExtra(Constants.CALLER, Constants.ACT_ADD_NEW_STEP);
//                        startActivityForResult(i, 1);
//                    } else {
//                        Logger.showMsg(this, Constants.MSG_ENTER_STEP_NAME);
//                        if (prefs.isPriorityNewStepBottomMost()) {
//                            stepPrioritySpinner.setSelection(1);
//                        } else {
//                            stepPrioritySpinner.setSelection(0);
//                        }
//                    }
//                }
//                break;

            case R.id.spinnerStepTypeActAddNewStep:
                if (position == 0) {
                    singleStepPanel.setVisibility(View.VISIBLE);
                    seriesPanel.setVisibility(View.GONE);
                } else if (position == 1) {
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

        switch (seekBar.getId()) {
            case R.id.seekbarSingleStepTimeActAddNewStep:
                break;

            case R.id.seekbarStepsInSeriesActAddNewStep:
                break;

            case R.id.seekbarTimeForEachStepActAddNewStep:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public BitmapDrawable writeOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bm);


        canvas.drawCircle(bm.getWidth() / 2, bm.getHeight() / 2, 25, thumbPaint);
        canvas.drawText(text, bm.getWidth() / 2 - textPaint.measureText(text) / 2, bm.getHeight() / 2 + 7, textPaint);
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
        if (resultCode == Constants.RESULTCODE_OF_ACT_ADD_GOAL && data.getExtras().getBoolean(Constants.GOAL_CREATED)) {// result from ActAddNewGoal
            getGoalListFromLocal();
            adapterGoalSpinner = new AdapterGoalSpinner(this, goalList);
            goalSpinner.setAdapter(adapterGoalSpinner);
            goalSpinner.setSelection(goalList.size() - 2);
            goalChosen = goalList.size() - 2;
        }
//        else if (resultCode == Constants.RESULTCODE_OF_ACT_STEP_LIST) {             // result from ActStepList
//            if (!stepArrayList.isEmpty())
//                stepArrayList.clear();
//            stepArrayList = (ArrayList<PendingStep>) data.getSerializableExtra(Constants.STEPS_ARRAY_LIST_WITH_NEW_PRIORITIES);
//
//            for (int i = 0; i<stepArrayList.size(); i++){
//                if(currentStep.getNickName().equals(stepArrayList.get(i).getNickName())){
//                    newPosition = i+1;
//                    stepArrayList.remove(i);
//                }
//            }
//
//            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
//            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            stepPrioritySpinner.setAdapter(spinnerAdapter);
//            spinnerAdapter.add("" + newPosition);
//            spinnerAdapter.add(Constants.TEXT_PRIORITY_SPINNER_TOP_MOST);//"Top-most"
//            spinnerAdapter.add(Constants.TEXT_PRIORITY_SPINNER_BOTTOM_MOST);//"Bottom-most"
//            spinnerAdapter.add(Constants.TEXT_PRIORITY_SPINNER_CHANGE_MANUALLY);//"Change Manually"
//            stepPrioritySpinner.setSelection(0);
//        }
        else if (resultCode == Activity.RESULT_CANCELED) {
//            getStepArrayFromLocal();
//            adapterGoalSpinner.notifyDataSetChanged();
            if (prefs.isPriorityNewStepBottomMost()) {
                //choose bottom most
                stepPrioritySpinner.setSelection(1);
            } else {
                stepPrioritySpinner.setSelection(0);
            }
        }
    }

//    private PendingStep generateCurrentStepObject() {
//
//        if (edtStepName.getText() != null && edtStepName.getText().length() > 0) {
//            currentStep.setNickName(edtStepName.getText().toString());
//        } else {
//            Logger.showMsg(this, Constants.MSG_ENTER_STEP_NAME);
//        }
//        currentStep.setGoalId(goalList.get(goalSpinner.getSelectedItemPosition()).getId());
//        if (stepTypeSpinner.getSelectedItemPosition() == 0) {
//            currentStep.setStepCount(1);
//            currentStep.setTime(sbTimeSingleStep.getProgress());
//            if (sbTimeSingleStep.getProgress() > Constants.MAX_SLOT_DURATION) {
//                currentStep.setPendingStepType(PendingStep.PendingStepType.SPLIT_STEP);
//            } else {
//                currentStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
//            }
//        } else if (stepTypeSpinner.getSelectedItemPosition() == 1) {
//            currentStep.setStepCount(sbNoOfSteps.getProgress());
//            currentStep.setTime(sbTimeSeriesStep.getProgress());
//            currentStep.setPendingStepType(PendingStep.PendingStepType.SERIES_STEP);
//        }
//        return currentStep;
//    }
}