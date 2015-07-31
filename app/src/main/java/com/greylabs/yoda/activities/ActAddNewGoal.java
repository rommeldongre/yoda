package com.greylabs.yoda.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ScrollView;
import android.widget.Spinner;

import com.greylabs.yoda.R;
import com.greylabs.yoda.adapters.AdapterTimeBoxSpinner;
import com.greylabs.yoda.asynctask.AysncTaskWithProgressBar;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.views.MyFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActAddNewGoal extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText edtObjective, edtKeyResult, edtNickName, edtGoalReason, edtGoalReward, edtGoalBuddy;//edtTime,
    Button btnShowAdvanced, btnHideAdvanced;
    CardView cardViewAdvanced;
    ScrollView scrollView;
    Toolbar toolbar;
//    MyFloatingActionButton btnAddFirstStep;
    Spinner timeSpinner;
    AdapterTimeBoxSpinner adapterTimeBoxSpinner;
//    ArrayAdapter<String> spinnerArrayAdapter;
    List<TimeBox> timeBoxList = new ArrayList<>();
    ArrayList<String> timeBoxNames = new ArrayList<>();
    Goal goal;
    boolean isSaved = false;
    private YodaCalendar yodaCalendar;
    String caller;
    private long oldSelectedTimeBoxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_goal);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolBarActAddNewGoal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleActAddNewGoal));

//        btnAddFirstStep = (MyFloatingActionButton) findViewById(R.id.btnAddFirstStepActAddNewGoal);
        edtObjective = (EditText) findViewById(R.id.edtObjectiveActAddNewGoal);
        edtKeyResult = (EditText) findViewById(R.id.edtKeyResultActAddNewGoal);
        edtNickName = (EditText) findViewById(R.id.edtNickNameActAddNewGoal);
        cardViewAdvanced = (CardView) findViewById(R.id.cardViewAdvancedActAddNewGoal);
        edtGoalReason = (EditText) findViewById(R.id.edtGoalReasonActAddNewGoal);
        edtGoalReward = (EditText) findViewById(R.id.edtGoalRewardActAddNewGoal);
        edtGoalBuddy = (EditText) findViewById(R.id.edtGoalBuddyActAddNewGoal);
        btnShowAdvanced = (Button) findViewById(R.id.btnShowAdvancedActAddNewGoal);
        btnHideAdvanced = (Button) findViewById(R.id.btnHideAdvancedActAddNewGoal);
        timeSpinner = (Spinner) findViewById(R.id.spinnerTimeActAddNewGoal);
        scrollView = (ScrollView) findViewById(R.id.scrollViewAvtAddNewGoal);

        getTimeBoxListAndPopulate();

//        btnAddFirstStep.setOnClickListener(this);
        btnShowAdvanced.setOnClickListener(this);
        btnHideAdvanced.setOnClickListener(this);
        timeSpinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        caller = intent.getStringExtra(Constants.CALLER);
        switch (caller){
            case Constants.ACT_HOME :
                if(!intent.getExtras().getBoolean(Constants.GOAL_ATTACHED_IN_EXTRAS)){
                    goal = new Goal(this);
                }
                break;

            case Constants.ACT_ADD_NEW_STEP :
                if(!intent.getExtras().getBoolean(Constants.GOAL_ATTACHED_IN_EXTRAS)){
                    goal = new Goal(this);
                }
//                btnAddFirstStep.setVisibility(View.GONE);
                break;

            case Constants.ACT_GOAL_DETAILS :
                if(intent.getBooleanExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false)){
                    goal = (Goal)intent.getSerializableExtra(Constants.GOAL_OBJECT);

                    getSupportActionBar().setTitle(goal.getNickName());

                    edtNickName.setText(goal.getNickName().toString());
                    //select the previous timeBox
//                    for(int i=0; i<timeBoxList.size();i++){
//                        if(timeBoxList.get(i).getNickName().equals(intent.getExtras().getString(Constants.TIMEBOX_NICK_NAME)))
//                            timeSpinner.setSelection(i);
//                    }

                    oldSelectedTimeBoxId=goal.getTimeBoxId();
                    edtObjective.setText(goal.getObjective());
                    edtKeyResult.setText(goal.getKeyResult());
                    edtGoalReason.setText(goal.getReason());
                    edtGoalReward.setText(goal.getReward());
                    edtGoalBuddy.setText(goal.getBuddyEmail());

//                    btnAddFirstStep.setVisibility(View.GONE);
                    btnShowAdvanced.setVisibility(View.GONE);
                    cardViewAdvanced.setVisibility(View.VISIBLE);

                }else if(!intent.getExtras().getBoolean(Constants.GOAL_ATTACHED_IN_EXTRAS)){
                    goal = new Goal(this);
                }
                break;

//            case Constants.ACT_GOAL_LIST :
//                break;
        }
    }

    private void getTimeBoxListAndPopulate() {
        TimeBox tempTimeBox  = new TimeBox(this);
        timeBoxNames.add(getResources().getString(R.string.timeSpinnerHintActAddNewGoal));//add hint

        tempTimeBox.setNickName(getResources().getString(R.string.timeSpinnerHintActAddNewGoal));
        timeBoxList.add(tempTimeBox);
        List<TimeBox> timeBoxes=tempTimeBox.getAll(TimeBox.TimeBoxStatus.INACTIVE);
        if(timeBoxes!=null) {
            timeBoxList.addAll(timeBoxes);
            if (timeBoxList != null && !timeBoxList.isEmpty()) {
                for (int i = 0; i < timeBoxList.size(); i++) {
                    timeBoxNames.add(timeBoxList.get(i).getNickName());
                }
            }
        }
        timeBoxNames.add(getResources().getString(R.string.addNewTimeBoxSpinnerItemActAddNewGoal));//add new TB option
//        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeBoxNames);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        timeSpinner.setAdapter(spinnerArrayAdapter);
        TimeBox tempTimeBox1  = new TimeBox(this);
        tempTimeBox1.setNickName(getResources().getString(R.string.addNewTimeBoxSpinnerItemActAddNewGoal));
        timeBoxList.add(tempTimeBox1);
        adapterTimeBoxSpinner = new AdapterTimeBoxSpinner(this, timeBoxList);
        timeSpinner.setAdapter(adapterTimeBoxSpinner);
        timeSpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_add_new_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                Intent intent1 = new Intent();
                intent1.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, false);
                setResult(1, intent1);
                finish();
                break;
            case R.id.actionSaveActAddNewGoal :
                saveGoal();
                //according to caller send response through intent
                if(isSaved){
                    Intent intent2 = new Intent();
                    intent2.putExtra(Constants.GOAL_OBJECT, goal);
                    intent2.putExtra(Constants.GOAL_ATTACHED_IN_EXTRAS, true);
                    setResult(Constants.RESULTCODE_OF_ACT_ADD_GOAL, intent2);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGoal() {
        if(timeSpinner.getSelectedItemPosition()>0){

            if(yodaCalendar==null){
                yodaCalendar=new YodaCalendar(this,timeBoxList.get(timeSpinner.getSelectedItemPosition()));
            }
            String caller=getIntent().getStringExtra(Constants.CALLER);
            boolean isValidTimeBox=false;
            if(caller.equals(Constants.ACT_GOAL_DETAILS)) {
                isValidTimeBox = yodaCalendar.validateTimeBoxForUpdate(oldSelectedTimeBoxId);
            }else{
                isValidTimeBox=yodaCalendar.validateTimeBox();
            }

            if(isValidTimeBox==false){
                AlertDialog.Builder alert=new AlertDialog.Builder(this);
                alert.setPositiveButton("Ok", null);
                alert.setMessage(getString(R.string.msgActAddNewGoalTimeBoxNotApplicable));
                alert.show();
            }else if(edtNickName.getText() != null && edtNickName.getText().length() > 0 ){
                goal.initDatabase(this);
                goal.setNickName(edtNickName.getText().toString());
                goal.setTimeBoxId(timeBoxList.get(timeSpinner.getSelectedItemPosition()).getId());
                goal.setObjective(edtObjective.getText().toString());
                goal.setKeyResult(edtKeyResult.getText().toString());
                goal.setReason(edtGoalReason.getText().toString());
                goal.setReward(edtGoalReward.getText().toString());
                goal.setBuddyEmail(edtGoalBuddy.getText().toString());
                goal.save();
                //AsyncTaskAttachTimeBox asyncTaskAttachTimeBox=new AsyncTaskAttachTimeBox(this,new MyHandler(),yodaCalendar,"Please Wait,Attaching TimeBox",goal.getId());
                //asyncTaskAttachTimeBox.execute(yodaCalendar);
                yodaCalendar.detachTimeBox(oldSelectedTimeBoxId);
                yodaCalendar.attachTimeBox(goal.getId());
                yodaCalendar.rescheduleSteps(goal.getId());
                isSaved = true;
                Logger.showMsg(this, getResources().getString(R.string.msgGoalSavedActAddNewGoal));
                this.finish();
            }else {
                Logger.showMsg(this, getResources().getString(R.string.msgEnterGoalNickNameActAddNewGoal));
            }
        }else {
            Logger.showMsg(this, getResources().getString(R.string.msgSelectTimeBoxActAddNewGoal));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

//            case R.id.btnAddFirstStepActAddNewGoal :
//                saveGoal();
//                if(isSaved){
//                    Intent i = new Intent(this, ActAddNewStep.class);
//                    i.putExtra(Constants.CALLER, Constants.ACT_ADD_NEW_GOAL);
//                    i.putExtra(Constants.GOAL_OBJECT, goal);
//                    startActivity(i);
//                    this.finish();
//                }
//                break;

            case R.id.btnShowAdvancedActAddNewGoal:
                btnShowAdvanced.setVisibility(View.GONE);
                cardViewAdvanced.setVisibility(View.VISIBLE);
//                scrollView.post(new Runnable() {
//                    public void run() {
//                        scrollView.fullScroll(View.FOCUS_DOWN);
//                    }
//                });
                break;

            case R.id.btnHideAdvancedActAddNewGoal :
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
        if(position>0 && position+1 == timeBoxList.size()){            //timeBoxNames
            Intent intent = new Intent(this, ActAddTimeBox.class);
            intent.putExtra(Constants.CALLER, Constants.ACT_ADD_NEW_GOAL);
            startActivity(intent);
            timeSpinner.setSelection(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        //timeSpinner.setSelection(0);
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int res=(int)msg.obj;
            if(res== AysncTaskWithProgressBar.SUCCESS) {
                ActAddNewGoal.this.finish();
            }
        }
    }
}