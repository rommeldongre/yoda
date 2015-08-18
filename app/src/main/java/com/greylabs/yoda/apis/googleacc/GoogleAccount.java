package com.greylabs.yoda.apis.googleacc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.GestureOverlayView;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.greylabs.yoda.apis.Sync;
import com.greylabs.yoda.apis.TaskAccount;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Jaybhay Vijay on 8/11/2015.
 */
public class GoogleAccount extends TaskAccount implements Sync, DialogInterface.OnClickListener {

    public static final String ACCOUNT_TYPE="com.google";
    private static final Level LOGGING_LEVEL = Level.OFF;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final String TAG = "GoogleAccount";
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    final HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    GoogleAccountCredential credential;
    com.google.api.services.tasks.Tasks service;
    private Prefs prefs;

    public GoogleAccount(Context context){
        super(context);
        prefs=Prefs.getInstance(context);
    }

    public List<String> getUsers(){
        return users;
    }
    @Override
    public  void authenticate() {
        credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(TasksScopes.TASKS));
        credential.setSelectedAccountName(prefs.getDefaultAccountEmailId());
        service = new com.google.api.services.tasks.Tasks.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Yoda").build();
        try {
            sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Object getService(){
        return service;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        Dialog dialog =GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, (Activity) context,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /** Check that Google Play services APK is installed and up to date. */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Prefs.getInstance(context).setDefaultAccountEmailId(users.get(position));
        Logger.log("You selected", users.get(position));
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        authenticate();
        dismissChooseAccountDialog();
    }

    @Override
    public Object buildGoal(Goal goal) {
        TaskList taskList=new TaskList();
        if(!(goal.getStringId()==null || goal.getStringId().equals("")))
            taskList.setId(goal.getStringId());
        //taskList.setUpdated(DateTime.parseRfc3339(CalendarUtils.getRFCTimestampToString(goal.getUpdated())));
        taskList.setKind("tasks#taskList");
        taskList.setTitle(goal.getNickName());
        return taskList;
    }

    @Override
    public Object buildPendingStep(PendingStep pendingStep) {
        Task task=new Task();
        if(!(pendingStep.getStringId()==null || pendingStep.getStringId().equals("")))
            task.setId(pendingStep.getStringId());

        task.setKind("tasks#task");
        if(!(pendingStep.getStringId()==null || pendingStep.getStringId().equals("")))
            task.setId(pendingStep.getStringId());
        //task.setUpdated(DateTime.parseRfc3339(CalendarUtils.getRFCTimestampToString(pendingStep.getUpdated())));
        task.setDeleted(pendingStep.isDeleted());
        switch (pendingStep.getPendingStepStatus()){
            case DOING:
            case MISSED:
            case TODO:
                task.setStatus("needsAction");
                break;
            case COMPLETED:task.setStatus("completed");
        }
        task.setTitle(pendingStep.getNickName());
        return task;
    }

    @Override
    public Goal convertToGoal(Object taskListObject) {
        if(! (taskListObject instanceof TaskList))
            throw  new IllegalArgumentException("Invalid object passed to this method");
        TaskList taskList=(TaskList)taskListObject;
        Goal goal=new Goal(context);
        Prefs prefs=Prefs.getInstance(context);
        long id=goal.getIdIfExists(taskList.getId());
        if(id==prefs.getStretchGoalId()){
            //new tasklist, need to add goal
            goal=goal.get(prefs.getStretchGoalId());
            goal.setId(prefs.getStretchGoalId());
            goal.setTimeBoxId(prefs.getUnplannedTimeBoxId());
            goal.setUpdated(taskList.getUpdated());
            goal.setStringId("@default");
            if(!taskList.getId().equals("@default"))
                goal.setStringId(taskList.getId());
        }else{
            //update tasklist,need to update goal
            goal = goal.get(id);
            goal.setNickName(taskList.getTitle());
        }

        return goal;
    }

    @Override
    public PendingStep convertToPendingStep(Object taskObject) {
        if(! (taskObject instanceof Task))
            throw  new IllegalArgumentException("Invalid object passed to this method");
        Task task=(Task)taskObject;
        PendingStep pendingStep=new PendingStep(context);
        long id=pendingStep.getIdIfExists(task.getId());
        if(id==0 ){
            //new step ,need to insert
            if( task.getStatus().equals("needsAction")|| (task.getDeleted()==Boolean.FALSE)) {
                pendingStep.setId(0);
                pendingStep.setTime(Constants.MAX_SLOT_DURATION);
                pendingStep.setStringId(task.getId());
                pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
                pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
                pendingStep.setUpdated(task.getUpdated());
            }
        }else{
            pendingStep = pendingStep.get(id);
            if (task.getStatus().equals("needsAction"))
                pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
            else {
                pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
            }


            if (task.getDeleted() != null) {
                pendingStep.setDeleted(task.getDeleted());
            }
            id=pendingStep.getIdIfExists(task.getParent());
            if(id!=0) {
                pendingStep.setSubStepOf(id);
                pendingStep.setPendingStepType(PendingStep.PendingStepType.SUB_STEP);
                //and its parents type set to Series Step
                PendingStep ps=pendingStep.get(id);
                ps.setPendingStepType(PendingStep.PendingStepType.SERIES_STEP);
            }
            //pendingStep.save();
        }
        //pendingStep.setUpdated(CalendarUtils.getStringToRFCTimestamp(task.getUpdated().toStringRfc3339()));
        pendingStep.setNickName(task.getTitle());
        return pendingStep;
    }

    @Override
    public void sync() throws IOException {
        doImport();
        doExport();
    }

    public void doImport() throws IOException {
        //import all tasks
        YodaCalendar yodaCalendar=new YodaCalendar(context);
        TimeBox timeBox=new TimeBox(context);

        TaskLists result=service.tasklists().list().execute();
        List<TaskList> taskLists=result.getItems();
        if(taskLists!=null){
            for (TaskList taskList:taskLists){
                Goal goal=convertToGoal(taskList);
                if(taskList.getUpdated().getValue()>=goal.getUpdated().getValue()){
                    goal.setUpdated(taskList.getUpdated());
                    if(prefs.getStretchGoalId()!=goal.getId())
                        goal.save();
                }
                yodaCalendar.setTimeBox(timeBox.get(goal.getTimeBoxId()));
                Logger.log("TAG", "Task List: " + taskList.toString() + "  Goal: " + goal.toString());
                com.google.api.services.tasks.model.Tasks tasks=service.tasks().list(taskList.getId()).setShowDeleted(true).setShowCompleted(true).execute();
                //com.google.api.services.tasks.model.Tasks tasks=service.tasks().list(taskList.getId()).execute();
                if(tasks!=null) {
                    List<Task> myTasks=tasks.getItems();
                    if(myTasks!=null) {
                        for (Task task : myTasks) {
                            PendingStep pendingStep = convertToPendingStep(task);
                            if (pendingStep.getId()!=0 && pendingStep.isDeleted()) {
                                service.tasks().delete(goal.getStringId(), pendingStep.getStringId()).execute();
                                pendingStep.delete();
                            } else if (pendingStep.getId() != 0 && pendingStep.getPendingStepStatus() == PendingStep.PendingStepStatus.COMPLETED) {
                                pendingStep.save();
                                pendingStep.setTime(Constants.MAX_SLOT_DURATION);
                                pendingStep.cancelAlarm();
                                service.tasks().update(goal.getStringId(), pendingStep.getStringId(), task).execute();
                            } else {
                                //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                if (task.getUpdated().getValue() >= pendingStep.getUpdated().getValue()) {
                                    if(task.getDeleted()==Boolean.FALSE || !task.getStatus().equals("completed")) {
                                        pendingStep.setGoalId(goal.getId());
                                        pendingStep.setGoalStringId(goal.getStringId());
                                        pendingStep.save();//insert or update
                                    }
                                }
                            }
                            Logger.log("TAG", "Task : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                        }
                    }
                }
                yodaCalendar.rescheduleSteps(goal.getId());
            }
        }
    }

    public void doExport() throws IOException {
        //goals
        Goal goal=new Goal(context);
        List<Goal> goals=goal.getAll();
        if(goals!=null) {
            for (Goal g : goals) {
                //export goals data
                TaskList taskList =(TaskList)buildGoal(g);
                if (g.getStringId() == null || g.getStringId().equals("")) {
                    //insert to google task list
                    TaskList result = service.tasklists().insert(taskList).execute();
                    g.setStringId(result.getId());
                    g.setUpdated(result.getUpdated());
                    g.save();
                } else {
                    if(g.isDeleted()){
                        service.tasklists().delete(goal.getStringId()).execute();
                    }else {
                        if(!g.getStringId().equals("@default")){
                            TaskList  result = service.tasklists().get(g.getStringId()).execute();
                            if(result.getUpdated().getValue()<g.getUpdated().getValue()) {
                                result = service.tasklists().update(g.getStringId(), taskList).execute();
                                g.setUpdated(result.getUpdated());
                                g.save();
                            }
                        }
                    }
                }

                //do following for each step in this goal
                PendingStep pendingStep = new PendingStep(context);
                List<PendingStep> pendingSteps = pendingStep.getAll(PendingStep.PendingStepStatus.TODO, g.getId());
                if(pendingSteps!=null) {
                    List<PendingStep> completed=pendingStep.getAll(PendingStep.PendingStepStatus.COMPLETED,g.getId());
                    if(completed!=null)
                        pendingSteps.addAll(completed);

                    for (PendingStep ps : pendingSteps) {
                        Task task =(Task) buildPendingStep(ps);
                        switch (ps.getPendingStepType()) {
                            case SPLIT_STEP:
                            case SERIES_STEP:
                                task.setTitle(ps.getNickName());
                                task.setNotes("This step is added by Yoda");
                                if (ps.getStringId() == null || ps.getStringId().equals("")) {
                                    Task result = service.tasks().insert(g.getStringId(), task).execute();
                                    ps.setStringId(result.getId());
                                    ps.setUpdated(result.getUpdated());
                                    ps.setGoalStringId(g.getStringId());
                                    ps.save();
                                } else {
                                    if(ps.isDeleted()){
                                        service.tasks().delete(ps.getGoalStringId(), ps.getStringId()).execute();
                                        ps.delete();
                                    }else {
                                        Task result= service.tasks().get(ps.getGoalStringId(), ps.getStringId()).execute();
                                        if(result.getUpdated().getValue()<ps.getUpdated().getValue()) {
                                            result= service.tasks().update(ps.getGoalStringId(), ps.getStringId(), task).execute();
                                            ps.setUpdated(result.getUpdated());
                                            ps.save();
                                        }
                                    }
                                }

                                List<PendingStep> subSteps = pendingStep.getAllSubSteps(pendingStep.getId(), pendingStep.getGoalId());
                                if(subSteps!=null){
                                    for (PendingStep substep : subSteps) {
                                        task=(Task)buildPendingStep(substep);
                                        task.setTitle(substep.getNickName());
                                        task.setNotes("This step is added by Yoda");
                                        task.setDue(new DateTime(substep.getStepDate()));
                                        task.setParent(ps.getStringId());
                                        if (substep.getStringId() == null || substep.getStringId().equals("")) {
                                            Task result = service.tasks().insert(g.getStringId(), task).execute();
                                            substep.setStringId(result.getId());
                                            substep.setUpdated(result.getUpdated());
                                            substep.setGoalStringId(g.getStringId());
                                            substep.save();
                                        } else {
                                            if(substep.isDeleted()){
                                                service.tasks().delete(substep.getGoalStringId(), substep.getStringId()).execute();
                                                substep.delete();
                                            }else {
                                                Task result= service.tasks().get(substep.getGoalStringId(), substep.getStringId()).execute();
                                                if(result.getUpdated().getValue()<substep.getUpdated().getValue()) {
                                                    result= service.tasks().update(substep.getGoalStringId(), substep.getStringId(), task).execute();
                                                    substep.setUpdated(result.getUpdated());
                                                    substep.save();
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case SINGLE_STEP:
                                task=(Task)buildPendingStep(ps);
                                task.setTitle(ps.getNickName());
                                task.setNotes("This step is added by Yoda");
                                task.setDue(new DateTime(ps.getStepDate()));
                                if (ps.getStringId() == null || ps.getStringId().equals("")) {
                                    Task result = service.tasks().insert(g.getStringId(), task).execute();
                                    ps.setStringId(result.getId());
                                    ps.setUpdated(result.getUpdated());
                                    ps.setGoalStringId(g.getStringId());
                                    ps.save();
                                } else {
                                    if(ps.isDeleted()){
                                        service.tasks().delete(ps.getGoalStringId(), ps.getStringId()).execute();
                                        ps.delete();
                                    }else {
                                        Task result= service.tasks().get(ps.getGoalStringId(), ps.getStringId()).execute();
                                        if(result.getUpdated().getValue()<ps.getUpdated().getValue()) {
                                            result= service.tasks().update(ps.getGoalStringId(), ps.getStringId(), task).execute();
                                            ps.setUpdated(result.getUpdated());
                                            ps.save();
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }

    }


}
