package com.greylabs.ydo.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.greylabs.ydo.R;
import com.greylabs.ydo.adapters.AdapterGoalSpinner;
import com.greylabs.ydo.apis.googleacc.GoogleSync;
import com.greylabs.ydo.enums.TimeBoxTill;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.models.Slot;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.scheduler.YodaCalendar;
import com.greylabs.ydo.utils.CalendarUtils;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ActAddNewStep extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    Toolbar toolbar;
    EditText edtStepName, edtStepNotes;
    CardView cardViewAdvanced;
    Button btnShowAdvanced, btnHideAdvanced;

    RadioGroup stepType;
    RadioButton singleStep, series;
    RadioGroup priority;
    RadioButton topMost, bottomMost;

    RelativeLayout rlSeekBarPriority;
    SeekBar seekBarPriority;
    TextView tvSeekBarPriority;

    Spinner goalSpinner;
    SeekBar sbNoOfSteps, sbTimeSeriesStep, sbTimeSingleStep;
    TextView tvTimeSingleStep, tvNoOfSeriesSteps, tvTimeSeriesSteps;
    LinearLayout singleStepPanel, seriesPanel;
    ScrollView scrollView;
    RadioButton rbDontExpire, rbExpire;
    RadioGroup rgBehaviourOfExpiredSteps;

    AdapterGoalSpinner adapterGoalSpinner;
    List<Goal> goalList = new ArrayList<>();
    ArrayList<String> goalNamesList = new ArrayList<>();
    Goal currentGoal;
    static int goalChosen;
    PendingStep currentStep;
    Paint thumbPaint, textPaint;
    ArrayList<PendingStep> stepArrayList = new ArrayList<>();
    Prefs prefs;
    private boolean isScheduled;
    int newPosition = 0;
    String caller, operation;
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
        textPaint.setTextSize(getResources().getDimension(R.dimen.textSizeSeekbarBullet));

        thumbPaint = new Paint();
        thumbPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(getResources().getColor(R.color.ColorPrimary));

        intent = getIntent();
        caller = intent.getStringExtra(Constants.CALLER);
        operation = intent.getStringExtra(Constants.OPERATION);
        prefs = Prefs.getInstance(this);

        toolbar = (Toolbar) findViewById(R.id.toolBarActAddNewStep);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.titleActAddNewStep));
        }

        scrollView = (ScrollView) findViewById(R.id.scrollViewAvtAddNewStep);
        edtStepName = (EditText) findViewById(R.id.edtStepNameActAddNewStep);
        edtStepNotes = (EditText) findViewById(R.id.edtStepNotesActAddNewStep);
        goalSpinner = (Spinner) findViewById(R.id.spinnerGoalNameActAddNewStep);
        btnShowAdvanced = (Button) findViewById(R.id.btnShowAdvancedActAddNewStep);

        cardViewAdvanced = (CardView) findViewById(R.id.cardViewAdvancedActAddNewStep);
        btnHideAdvanced = (Button) findViewById(R.id.btnHideAdvancedActAddNewStep);

        stepType = (RadioGroup) findViewById(R.id.radioGroupStepType);
        singleStep = (RadioButton) findViewById(R.id.radioButtonSingleStep);
        series = (RadioButton) findViewById(R.id.radioButtonSeries);
        priority = (RadioGroup) findViewById(R.id.radioGroupPriority);
        topMost = (RadioButton) findViewById(R.id.radioButtonTopMost);
        bottomMost = (RadioButton) findViewById(R.id.radioButtonBottomMost);

        rlSeekBarPriority = (RelativeLayout) findViewById(R.id.rlSeekBarPriority);
        seekBarPriority = (SeekBar) findViewById(R.id.seekBarPriority);
        tvSeekBarPriority = (TextView) findViewById(R.id.tvSeekBarPriority);

        sbTimeSingleStep = (SeekBar) findViewById(R.id.seekbarSingleStepTimeActAddNewStep);
        sbNoOfSteps = (SeekBar) findViewById(R.id.seekbarStepsInSeriesActAddNewStep);
        sbTimeSeriesStep = (SeekBar) findViewById(R.id.seekbarTimeForEachStepActAddNewStep);
        tvTimeSingleStep = (TextView) findViewById(R.id.tvSeekbarSingleStepTimeActAddNewStep);
        tvNoOfSeriesSteps = (TextView) findViewById(R.id.tvSeekbarStepsInSeriesActAddNewStep);
        tvTimeSeriesSteps = (TextView) findViewById(R.id.tvSeekbarTimeForEachStepActAddNewStep);

        singleStepPanel = (LinearLayout) findViewById(R.id.SingleStepPanelActAddNewStep);
        seriesPanel = (LinearLayout) findViewById(R.id.SeriesPanelActAddNewStep);
        rbDontExpire = (RadioButton) findViewById(R.id.rbDontExpireActAddNewStep);
        rbExpire = (RadioButton) findViewById(R.id.rbExpireActAddNewStep);
        rgBehaviourOfExpiredSteps = (RadioGroup) findViewById(R.id.rgBehaviourOfExpiredStepsActAddNewStep);

        //get defaults from settings and set Spinners accordingly
        sbTimeSingleStep.setOnSeekBarChangeListener(this);
        sbNoOfSteps.setOnSeekBarChangeListener(this);
        sbTimeSeriesStep.setOnSeekBarChangeListener(this);
        btnShowAdvanced.setOnClickListener(this);
        btnHideAdvanced.setOnClickListener(this);
        goalSpinner.setOnItemSelectedListener(this);

        setAdvancedStepRadioButtons();

        stepType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.radioButtonSingleStep:
                        setAdvancedStepRadioButtons();
                        break;
                    case R.id.radioButtonSeries:
                        setAdvancedStepRadioButtons();
                        if (btnShowAdvanced.getVisibility() == View.VISIBLE) {
                            btnShowAdvanced.setVisibility(View.GONE);
                            cardViewAdvanced.setVisibility(View.VISIBLE);
                            scrollView.post(new Runnable() {
                                public void run() {
                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                        break;
                }
            }
        });

        setDefaultValues();
        getGoalListAndPopulate();
        getStepArrayFromLocal();
    }

    private void setAdvancedStepRadioButtons() {
        if (((RadioButton) findViewById(stepType.getCheckedRadioButtonId())).getText().equals(Constants.PENDING_STEP_TYPE_SINGLE_STEP)) {
            singleStepPanel.setVisibility(View.VISIBLE);
            seriesPanel.setVisibility(View.GONE);
        } else {
            singleStepPanel.setVisibility(View.GONE);
            seriesPanel.setVisibility(View.VISIBLE);
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }

    private void setDefaultValues() {
        sbTimeSingleStep.setProgress(prefs.getDefaultStepDuration());
        sbNoOfSteps.setProgress(2);
        sbTimeSeriesStep.setProgress(prefs.getDefaultStepDuration());
        if (prefs.isPriorityNewStepBottomMost()) {
            //choose bottom most
            bottomMost.setChecked(true);
        } else {
            topMost.setChecked(true);
        }
    }


    private void getStepArrayFromLocal() {
        stepArrayList.clear();
        PendingStep pendingStep = new PendingStep(this);
        currentGoal = goalList.get(goalSpinner.getSelectedItemPosition());
//        if (currentGoal != null && pendingStep.getAll(currentGoal.getId()) != null)
//            if(pendingStep.getAll(PendingStep.PendingStepStatus.TODO, PendingStep.PendingStepDeleted.SHOW_NOT_DELETED,currentGoal.getId())!=null)
//                stepArrayList.addAll(pendingStep.getAll(PendingStep.PendingStepStatus.TODO,currentGoal.getId()));

        List<PendingStep> temp = pendingStep.getAll(PendingStep.PendingStepStatus.TODO,
                PendingStep.PendingStepDeleted.SHOW_NOT_DELETED, currentGoal.getId());
        if (currentGoal != null && temp != null) {
            stepArrayList.addAll(temp);
        }
    }

    private void getGoalListAndPopulate() {
        // check context and populate spinner else show only one currentGoal
        switch (caller) {

            case Constants.ACT_HOME:
                operation = "";
                getGoalListFromLocal();
                currentStep = new PendingStep(this);
                break;

//            case Constants.ACT_ADD_NEW_GOAL:
//                currentGoal = (Goal) intent.getSerializableExtra(Constants.GOAL_OBJECT);
//                goalList.add(currentGoal);
//                goalNamesList.add(currentGoal.getNickName());
//                currentStep = new PendingStep(this);
//                break;

            case Constants.ACT_STEP_LIST:
                if (operation.equals(Constants.OPERATION_ADD)) {
                    getGoalListFromLocal();
                    currentGoal = (Goal) intent.getSerializableExtra(Constants.GOAL_OBJECT);
                    int oldGoalPosition = 0;
                    for (int i = 0; i < goalList.size(); i++) {
                        if (currentGoal.getId() == goalList.get(i).getId())
                            oldGoalPosition = i;
                    }
                    goalSpinner.setSelection(oldGoalPosition);
                    goalChosen = oldGoalPosition;
//                    getGoalListFromLocal();
                    currentStep = new PendingStep(this);

                } else {
                    btnShowAdvanced.setVisibility(View.GONE);
                    cardViewAdvanced.setVisibility(View.VISIBLE);
                    currentStep = (PendingStep) intent.getSerializableExtra(Constants.STEP_OBJECT);
                    currentStep.initDatabase(this);
                    getGoalListFromLocal();
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(currentStep.getNickName());
                    edtStepName.setText(currentStep.getNickName());
                    edtStepNotes.setText(currentStep.getNotes());
                    if (currentStep.isExpire().equals(PendingStep.PendingStepExpire.EXPIRE)) {
                        if (!rbExpire.isChecked())
                            rbExpire.setChecked(true);
                    } else {
                        if (!rbDontExpire.isChecked())
                            rbDontExpire.setChecked(true);
                    }


                    int oldGoalPosition = 0;
                    for (int i = 0; i < goalList.size(); i++) {
                        if (currentStep.getGoalId() == goalList.get(i).getId())
                            oldGoalPosition = i;
                    }
                    goalSpinner.setSelection(oldGoalPosition);
                    goalChosen = oldGoalPosition;

                    // visibility of radio group is set to gone and the priority is shown in the seek bar

                    priority.setVisibility(View.GONE);
                    rlSeekBarPriority.setVisibility(View.VISIBLE);

                    currentGoal.setId(currentStep.getGoalId());
                    int count = currentGoal.getRemainingStepCount();
                    seekBarPriority.setMax(count);

                    seekBarPriority.setProgress(currentStep.getPriority());
                    seekBarPriority.setEnabled(false);
                    tvSeekBarPriority.setText(String.valueOf(currentStep.getPriority()) + "/" + String.valueOf(count));

                    if (currentStep.getPendingStepType() == PendingStep.PendingStepType.SINGLE_STEP
                            || currentStep.getPendingStepType() == PendingStep.PendingStepType.SUB_STEP
                            ) {
                        singleStep.setChecked(true);
                        sbTimeSingleStep.setProgress(currentStep.getTime());
                    } else {
                        series.setChecked(true);
                        sbTimeSeriesStep.setProgress(currentStep.getTime());
                        sbNoOfSteps.setProgress(currentStep.getStepCount());
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
        if (goalList.size() > 0) {
            goalList.clear();
        }
        currentGoal = new Goal(this);
        List<Goal> temp = currentGoal.getAll(Goal.GoalDeleted.SHOW_NOT_DELETED);
        if (temp != null)
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
                goalChosen = 0;
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
            if (currentStep.getId() != 0) {
                //change updated date only if nickname is different than previous
                if (currentStep.getNickName().equals(edtStepName.getText().toString()))
                    currentStep.setUpdated(new DateTime(new Date()));
            } else {
                currentStep.setUpdated(new DateTime(new Date()));
            }
            currentStep.setNickName(edtStepName.getText().toString());
            currentStep.setNotes(edtStepNotes.getText().toString());
            currentStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
            currentStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
            if (rbExpire.isChecked()) {
                currentStep.setExpire(PendingStep.PendingStepExpire.EXPIRE);
            } else {
                currentStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
            }

            if (((RadioButton) findViewById(stepType.getCheckedRadioButtonId())).getText().equals(Constants.PENDING_STEP_TYPE_SINGLE_STEP)) {
                if (sbTimeSingleStep.getProgress() > 3) {
                    currentStep.setPendingStepType(PendingStep.PendingStepType.SPLIT_STEP);
                    currentStep.setStepCount(sbTimeSingleStep.getProgress() / 3);
                    if (sbTimeSingleStep.getProgress() % 3 >= 1)
                        currentStep.setStepCount(currentStep.getStepCount() + 1);
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

            currentStep.setGoalId(currentGoal.getId());
            if (currentStep.getStringId() == null || currentStep.getStringId().equals(""))
                currentStep.setGoalStringId(currentGoal.getStringId());

            TimeBox timeBox = new TimeBox(this);
            timeBox = timeBox.get(currentGoal.getTimeBoxId());
            YodaCalendar yodaCalendar = new YodaCalendar(this, timeBox);
            //assume default priority is bottom most irrespective of settings
            //boolean isScheduled = yodaCalendar.scheduleStep(currentStep);
            Slot slot = new Slot(this);
            int stepCount = 0;
            if (currentStep.getId() == 0) {
                //stepTime=currentStep.getStepCount()*currentStep.getTime();
                stepCount = currentStep.getStepCount();
            }

            //slot.getTotalSlotCount(timeBox.getId())*Constants.MAX_SLOT_DURATION<=
            //(currentStep.getAllStepTimeSum(currentGoal.getId())+stepTime)
            int substeps = 0;


//            if (slot.getTotalSlotCount(timeBox.getId())<(currentStep.getAllStepCount(currentGoal.getId())+stepCount)) {
//                isScheduled=false;
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(getString(R.string.msgYodaSays));
//                builder.setMessage(getString(R.string.msgCannotSaveStepActAddNewStep));
//                builder.setPositiveButton(getString(R.string.btnOk), null);
//                builder.show();
//            } else {
            List<PendingStep> subStepsList = new ArrayList<>();
            currentStep.initDatabase(this);
            PendingStep ps = currentStep;
            ps.save();
            switch (ps.getPendingStepType()) {
                case SPLIT_STEP:
//                        if(ps.getStringId()!=null || ps.getStringId().equals("")){
//                            ps.freeSlots();//and cancel alarms
//                            ps.deleteSubSteps();
//                        }else{
//                            ps.save();
//                            ps.markSubSteps(true);
//                            ps.freeSlots();
//                        }
                    if (ps.getTime() > Constants.MAX_SLOT_DURATION) {
                        float numberOfSteps = (float) ps.getTime() / Constants.MAX_SLOT_DURATION;
                        Float f = numberOfSteps;
                        ps.createSubSteps(1, f.intValue(), Constants.MAX_SLOT_DURATION);
                        if (numberOfSteps - f.intValue() > 0.0f)
                            ps.createSubSteps(f.intValue() + 1, f.intValue() + 1, currentStep.getTime() % Constants.MAX_SLOT_DURATION);
                    }
                    subStepsList = currentStep.getAllSubSteps(currentStep.getId(), currentGoal.getId());
                    break;
                case SERIES_STEP:
//                        if(ps.getStringId()!=null || ps.getStringId().equals("")){
//                            ps.freeSlots();//and cancel alarms
//                            ps.deleteSubSteps();
//                        }else {
//                            ps.save();
//                            ps.markSubSteps(true);
//                            ps.freeSlots();
//                        }
                    ps.createSubSteps(1, currentStep.getStepCount(), currentStep.getTime());
                    subStepsList = currentStep.getAllSubSteps(currentStep.getId(), currentGoal.getId());
                    break;
                case SUB_STEP:
                case SINGLE_STEP:
                    subStepsList.add(currentStep);
            }
            if (subStepsList != null) {

                String priorityText = ((RadioButton) findViewById(priority.getCheckedRadioButtonId())).getText().toString();

                if (priorityText.equals(Constants.PENDING_STEP_PRIORITY_TOP_MOST)) {
                    stepArrayList.addAll(0, subStepsList);
                } else if (priorityText.equals(Constants.PENDING_STEP_PRIORITY_BOTTOM_MOST)) {
                    stepArrayList.addAll(subStepsList);
                }

                //}
                //save all the steps in the array with priorities
                for (int i = 0; i < stepArrayList.size(); i++) {
                    stepArrayList.get(i).initDatabase(this);
                    stepArrayList.get(i).setPriority(i + 1);
                    stepArrayList.get(i).setUpdated(new DateTime(new Date()));
                    stepArrayList.get(i).save();
                    //stepArrayList.get(i).updateSubSteps();
                }
                currentStep.save();
                //if user sets priority to Manual or TopMost ,then need to rearrange steps
                switch (priorityText) {
                    case Constants.TEXT_PRIORITY_SPINNER_TOP_MOST:
                        yodaCalendar.rescheduleSteps(goalList.get(goalSpinner.getSelectedItemPosition()).getId());
                        //yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
                        break;
                    case Constants.TEXT_PRIORITY_SPINNER_BOTTOM_MOST:
                        // yodaCalendar.scheduleStep(currentStep);
                        yodaCalendar.rescheduleSteps(goalList.get(goalSpinner.getSelectedItemPosition()).getId());
                        break;
                    default:
                        yodaCalendar.rescheduleSteps(goalList.get(goalSpinner.getSelectedItemPosition()).getId());
                        break;
                }
                currentStep = currentStep.get(currentStep.getId());
                //sync code
                GoogleSync.getInstance(this).sync();
                //sync code
                AlertDialog.Builder alertStepAdded = new AlertDialog.Builder(this);
                alertStepAdded.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goalChosen = 0;
                        ActAddNewStep.this.finish();
                    }
                });
                isScheduled = true;
                Date stepDate;
                if (subStepsList.size() > 0 && (currentStep.getPendingStepType() == PendingStep.PendingStepType.SPLIT_STEP || currentStep.getPendingStepType() == PendingStep.PendingStepType.SERIES_STEP)) {
                    stepDate = subStepsList.get(0).getTopmostSubstepScheduleDate(currentStep.getId());
                } else {
                    stepDate = currentStep.getStepDate();
                }
                if (stepDate == null) {
                    alertStepAdded.setMessage("Time allotted for this goal is too short for all " +
                            "steps and hence some steps have remained unscheduled.");
                } else {
                    alertStepAdded.setMessage("The Step \'" + currentStep.getNickName() +
                            "\' has been scheduled with a start date of \'" +
                            CalendarUtils.getOnlyFormattedDate(stepDate) + "\'");
                }
                alertStepAdded.setCancelable(false);
                alertStepAdded.show();
            }
        } else {
//            Logger.showMsg(this, getString(R.string.msgEnterStepNameActAddNewStep));
            Logger.showSnack(this, toolbar, getString(R.string.msgEnterStepNameActAddNewStep));
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
                if (!operation.equals(Constants.OPERATION_EDIT))
                    setWhetherToExpireValue(goalList.get(position));
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
        }
    }

    private void setWhetherToExpireValue(Goal goal) {
        if (new TimeBox(this).get(goal.getTimeBoxId()).getTillType() == TimeBoxTill.FOREVER) {
            rbExpire.setChecked(true);
        } else {
            rbDontExpire.setChecked(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {
            case R.id.seekbarSingleStepTimeActAddNewStep:
                if (progress < 1) {
                    progress = 1;
                    sbTimeSingleStep.setProgress(progress);
                }
                tvTimeSingleStep.setText("" + progress);
                break;

            case R.id.seekbarStepsInSeriesActAddNewStep:
                if (((RadioButton) findViewById(stepType.getCheckedRadioButtonId())).getText().equals(Constants.PENDING_STEP_TYPE_SINGLE_STEP) && progress < 2) {
                    progress = 2;
                    sbNoOfSteps.setProgress(progress);
                }
                tvNoOfSeriesSteps.setText("" + progress);
                break;

            case R.id.seekbarTimeForEachStepActAddNewStep:
                if (progress < 1) {
                    progress = 1;
                    sbTimeSeriesStep.setProgress(progress);
                }
                tvTimeSeriesSteps.setText("" + progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
                bottomMost.setChecked(true);
//                stepPrioritySpinner.setSelection(1);
            } else {
                topMost.setChecked(true);
//                stepPrioritySpinner.setSelection(0);
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