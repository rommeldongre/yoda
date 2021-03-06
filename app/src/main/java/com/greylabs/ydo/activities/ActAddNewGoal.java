package com.greylabs.ydo.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.greylabs.ydo.R;
import com.greylabs.ydo.adapters.AdapterTimeBoxSpinner;
import com.greylabs.ydo.apis.googleacc.GoogleSync;
import com.greylabs.ydo.enums.AccountType;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.models.Slot;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.scheduler.YodaCalendar;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ActAddNewGoal extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText edtObjective, edtKeyResult, edtNickName, edtGoalReason, edtGoalReward;//, edtGoalBuddy;//edtTime,
    TextView edtGoalBuddy;
    SearchView svGoalBuddy;
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
    private static final String TAG = "activitiesActAddNewGoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_goal);
        initialize();
    }

    private void initialize() {
        Intent intent = getIntent();
        caller = intent.getStringExtra(Constants.CALLER);
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
//        edtGoalBuddy = (EditText) findViewById(R.id.edtGoalBuddyActAddNewGoal);
        edtGoalBuddy = (TextView) findViewById(R.id.edtGoalBuddyActAddNewGoal);
        svGoalBuddy = (SearchView) findViewById(R.id.svGoalBuddyActAddNewGoal);
        btnShowAdvanced = (Button) findViewById(R.id.btnShowAdvancedActAddNewGoal);
        btnHideAdvanced = (Button) findViewById(R.id.btnHideAdvancedActAddNewGoal);
        timeSpinner = (Spinner) findViewById(R.id.spinnerTimeActAddNewGoal);
        scrollView = (ScrollView) findViewById(R.id.scrollViewAvtAddNewGoal);

        setupSearchView();

//        btnAddFirstStep.setOnClickListener(this);
        btnShowAdvanced.setOnClickListener(this);
        btnHideAdvanced.setOnClickListener(this);
        timeSpinner.setOnItemSelectedListener(this);

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
//                        if(timeBoxList.get(i).getId() == goal.getTimeBoxId())
//                            oldSelectedTimeBoxId=goal.getTimeBoxId();
//                            timeSpinner.setSelection(i);
//                    }

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
        getTimeBoxListAndPopulate();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        svGoalBuddy.setSearchableInfo(searchableInfo);
        svGoalBuddy.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtGoalBuddy.setVisibility(View.GONE);
            }
        });
        svGoalBuddy.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                edtGoalBuddy.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void getTimeBoxListAndPopulate() {
        if(timeBoxList.size()>0)
            timeBoxList.clear();
        TimeBox tempTimeBox  = new TimeBox(this);
        timeBoxNames.add(getResources().getString(R.string.timeSpinnerHintActAddNewGoal));//add hint

        tempTimeBox.setNickName(getResources().getString(R.string.timeSpinnerHintActAddNewGoal));
        tempTimeBox.setColorCode(String.valueOf(getResources().getColor(R.color.ColorPrimaryDark)));
        timeBoxList.add(tempTimeBox);

        // add old TB if goal is being edited
        if(caller.equals(Constants.ACT_GOAL_DETAILS))
            timeBoxList.add(new TimeBox(this).get(goal.getTimeBoxId()));

        if(tempTimeBox.getAll(TimeBox.TimeBoxStatus.INACTIVE)!=null){
            timeBoxList.addAll(tempTimeBox.getAll(TimeBox.TimeBoxStatus.INACTIVE));
        }else {
            Logger.showSnack(this, toolbar, Constants.MSG_ADD_NEW_TIMEBOX, Snackbar.LENGTH_LONG);
        }
        timeBoxNames.add(getResources().getString(R.string.addNewTimeBoxSpinnerItemActAddNewGoal));//add new TB option

        TimeBox tempTimeBox1 = new TimeBox(this);
        tempTimeBox1.setNickName(getResources().getString(R.string.addNewTimeBoxSpinnerItemActAddNewGoal));
        tempTimeBox1.setColorCode(String.valueOf(getResources().getColor(R.color.ColorPrimary)));
        timeBoxList.add(tempTimeBox1);
        adapterTimeBoxSpinner = new AdapterTimeBoxSpinner(this, timeBoxList);
        timeSpinner.setAdapter(adapterTimeBoxSpinner);

        if(caller.equals(Constants.ACT_GOAL_DETAILS))
            timeSpinner.setSelection(1);
        else
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
            if(!edtNickName.getText().equals(Constants.NICKNAME_UNPLANNED_TIMEBOX)){
                if(yodaCalendar==null){
                    yodaCalendar=new YodaCalendar(this,timeBoxList.get(timeSpinner.getSelectedItemPosition()));
                }
//            String caller=getIntent().getStringExtra(Constants.CALLER);
                boolean isValidTimeBox=false;
                if(caller.equals(Constants.ACT_GOAL_DETAILS)) {
                    isValidTimeBox = yodaCalendar.validateTimeBoxForUpdate(goal.getTimeBoxId());
                }else{
                    isValidTimeBox=yodaCalendar.validateTimeBox();
                }

                if(isValidTimeBox==false){
                    isSaved=false;
                    AlertDialog.Builder alert=new AlertDialog.Builder(this);
                    alert.setPositiveButton("Ok", null);
                    alert.setMessage(getString(R.string.msgActAddNewGoalTimeBoxNotApplicable));
                    alert.show();
                }else if(edtNickName.getText() != null && edtNickName.getText().length() > 0 ){
                    Log.i(TAG, "267");
                    if (!new Goal(this).ifGoalNameExists(edtNickName.getText().toString())){
                        Log.i(TAG, "269");
                        TimeBox timeBox=timeBoxList.get(timeSpinner.getSelectedItemPosition());
                        if(timeBox.getId()!=goal.getTimeBoxId()){               // if new timebox selected
                            PendingStep pendingStep=new PendingStep(this);
                            Slot slot=new Slot(this);
                            if(pendingStep.getAllStepCount(goal.getId())<=slot.getPossibleSlotCount(timeBox)) {
                                yodaCalendar.detachTimeBox(goal.getTimeBoxId());
                                pendingStep.freeAllSlots(goal.getId());
                                yodaCalendar.setTimeBox(timeBox);
                                yodaCalendar.attachTimeBox(goal.getId());
                                yodaCalendar.rescheduleSteps(goal.getId());
                                Prefs prefs=Prefs.getInstance(this);
                                yodaCalendar.setTimeBox(new TimeBox(this).get(prefs.getUnplannedTimeBoxId()));
                                yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
                                isSaved=true;
                            }else {
                                AlertDialog.Builder alert=new AlertDialog.Builder(this);
                                alert.setPositiveButton("Ok", null);
                                alert.setMessage(getString(R.string.msgCannotSaveGoalWithSelectedTimeBox));
                                alert.show();
                                isSaved=false;
                            }
                        }else {
                            isSaved=true;
                        }
                        if(isSaved) {
                            goal.initDatabase(this);
                            goal.setNickName(edtNickName.getText().toString());
                            long seletedTBId = timeBoxList.get(timeSpinner.getSelectedItemPosition()).getId();
                            goal.setTimeBoxId(seletedTBId);
                            goal.setObjective(edtObjective.getText().toString());
                            goal.setKeyResult(edtKeyResult.getText().toString());
                            goal.setReason(edtGoalReason.getText().toString());
                            goal.setReward(edtGoalReward.getText().toString());
                            goal.setBuddyEmail(edtGoalBuddy.getText().toString());
                            Prefs prefs = Prefs.getInstance(this);
                            goal.setAccount(prefs.getDefaultAccountEmailId());
                            goal.setAccountType(AccountType.getIntegerToEnum(prefs.getDefaultAccountType()));
                            goal.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
                            goal.save();
                            Slot slot = new Slot(this);
                            int totalHoursAllotted = slot.getTotalSlotCount(seletedTBId)*Constants.MAX_SLOT_DURATION;
                            Logger.showMsg(this, getString(R.string.msgGoalSavedActAddNewGoal)+" "+totalHoursAllotted+" hour(s) in it");
                            //sync code
                            GoogleSync.getInstance(this).sync();
                            //sync code
                            switch (caller) {
                                case Constants.ACT_ADD_NEW_STEP:
                                    Intent secIntent = new Intent();
                                    secIntent.putExtra(Constants.GOAL_CREATED, true);
                                    setResult(Constants.RESULTCODE_OF_ACT_ADD_GOAL, secIntent);
                                    break;

                                case Constants.ACT_GOAL_DETAILS:
                                    Intent thirdIntent = new Intent();
                                    thirdIntent.putExtra(Constants.GOAL_UPDATED, true);
                                    setResult(Constants.RESULTCODE_OF_ACT_ADD_GOAL, thirdIntent);
                            }
                            this.finish();
                        }
                    }else {
                        Logger.showSnack(this, toolbar, getString(R.string.msgGoalNickNameExistsActAddNewGoal));
                    }
                }else {
//                    Logger.showMsg(this, getString(R.string.msgEnterGoalNickNameActAddNewGoal));
                    Logger.showSnack(this, toolbar, getString(R.string.msgEnterGoalNickNameActAddNewGoal));
                }
            }else {
//                Logger.showMsg(this, getResources().getString(R.string.msgSelectTimeBoxActAddNewGoal));
                Logger.showSnack(this, toolbar, getString(R.string.msgSelectTimeBoxActAddNewGoal));
            }
        }else {
//            Logger.showMsg(this, getString(R.string.msgSelectTimeBoxActAddNewGoal));
            Logger.showSnack(this, toolbar, getString(R.string.msgSelectTimeBoxActAddNewGoal));
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
            startActivityForResult(intent, Constants.REQUEST_CODE_ACT_ACT_ADD_NEW_GOAL);
            timeSpinner.setSelection(0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            //handles suggestion clicked query
            String emailId = getEmailOfClickedContact(intent);
            if(emailId!=null){
                collapseSearchView();
                edtGoalBuddy.setText(emailId);
            }else {
                collapseSearchView();
//                Logger.showMsg(this, getString(R.string.msgNoEmailIdAttached));
                Logger.showSnack(this, toolbar, getString(R.string.msgNoEmailIdAttached), Snackbar.LENGTH_LONG);
            }
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            edtGoalBuddy.setText(query);
            collapseSearchView();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void collapseSearchView() {
        svGoalBuddy.setIconified(true);
        svGoalBuddy.setIconified(true);
    }

    private String getEmailOfClickedContact(Intent intent) {
        String emailIdOfContact = null;
//        int emailType = ContactsContract.CommonDataKinds.Email.TYPE_WORK;
//        String contactName = null;
        ContentResolver cr = getContentResolver();
        Cursor phoneCursor = cr.query(intent.getData(), null, null, null, null);
        phoneCursor.moveToFirst();
//        String name = phoneCursor
//                .getString(phoneCursor
//                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        String id = phoneCursor.getString(phoneCursor
                .getColumnIndex(BaseColumns._ID));
        phoneCursor.close();

        Cursor emailsCursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
        if(emailsCursor.getCount()>0){
            emailsCursor.moveToFirst();
            emailIdOfContact = emailsCursor.getString(emailsCursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//        emailType = emailsCursor.getInt(emailsCursor
//                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        }
        emailsCursor.close();
        return emailIdOfContact;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //timeSpinner.setSelection(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULTCODE_OF_ACT_ADD_TIMEBOX && data.getExtras().getBoolean(Constants.TIMEBOX_CREATED)) {
            getTimeBoxListAndPopulate();
            adapterTimeBoxSpinner = new AdapterTimeBoxSpinner(this, timeBoxList);
            timeSpinner.setAdapter(adapterTimeBoxSpinner);
            timeSpinner.setSelection(timeBoxList.size() - 2);
            oldSelectedTimeBoxId = timeBoxList.size() - 2;
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            //do nothing
        }
    }

//    private class MyHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            int res=(int)msg.obj;
//            if(res== AysncTaskWithProgressBar.SUCCESS) {
//                ActAddNewGoal.this.finish();
//            }
//        }
//    }
}