package com.greylabs.yoda.apis.googleacc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.greylabs.yoda.apis.Sync;
import com.greylabs.yoda.apis.TaskAccount;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;

/**
 * Created by Jaybhay Vijay on 8/11/2015.
 */
public class GoogleAccount extends TaskAccount implements Sync, DialogInterface.OnClickListener {

    public static final String ACCOUNT_TYPE = "com.google";
    private static final Level LOGGING_LEVEL = Level.OFF;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final String TAG = "GoogleAccount";
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    final HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    GoogleAccountCredential credential;
    com.google.api.services.tasks.Tasks service;
    private Prefs prefs;

    public GoogleAccount(Context context) {
        super(context);
        prefs = Prefs.getInstance(context);
    }

    public List<String> getUsers() {
        return users;
    }

    @Override
    public void authenticate() {
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
    public Object getService() {
        return service;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, (Activity) context,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     */
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
        Logger.log(TAG, users.get(position));
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        authenticate();
        dismissChooseAccountDialog();
    }

    @Override
    public Object buildGoal(Goal goal) {
        TaskList taskList = new TaskList();
        if (!(goal.getStringId() == null || goal.getStringId().equals("")))
            taskList.setId(goal.getStringId());
        //taskList.setUpdated(DateTime.parseRfc3339(CalendarUtils.getRFCTimestampToString(goal.getUpdated())));
        taskList.setKind("tasks#taskList");
        taskList.setTitle(goal.getNickName());
        return taskList;
    }

    @Override
    public Object buildPendingStep(PendingStep pendingStep) {
        Task task = new Task();
        if (!(pendingStep.getStringId() == null || pendingStep.getStringId().equals("")))
            task.setId(pendingStep.getStringId());

        task.setKind("tasks#task");
        if (!(pendingStep.getStringId() == null || pendingStep.getStringId().equals("")))
            task.setId(pendingStep.getStringId());
        //task.setUpdated(DateTime.parseRfc3339(CalendarUtils.getRFCTimestampToString(pendingStep.getUpdated())));
        task.setDeleted(pendingStep.isDeleted());
        switch (pendingStep.getPendingStepStatus()) {
            case DOING:
            case MISSED:
            case TODO:
                task.setStatus("needsAction");
                break;
            case COMPLETED:
                task.setStatus("completed");
        }
        if (pendingStep.getPendingStepType() == PendingStep.PendingStepType.SUB_STEP) {
            PendingStep ps = new PendingStep(context).get(pendingStep.getSubStepOf());
            task.setParent(ps.getStringId());
        }
        task.setTitle(pendingStep.getNickName());
        return task;
    }

    @Override
    public Goal convertToGoal(Object taskListObject) {
        if (!(taskListObject instanceof TaskList))
            throw new IllegalArgumentException("Invalid object passed to this method");
        TaskList taskList = (TaskList) taskListObject;
        Goal goal = new Goal(context);
        Prefs prefs = Prefs.getInstance(context);
        long id = goal.getIdIfExists(taskList.getId());
        if (id == prefs.getStretchGoalId()) {
            //new tasklist, need to add goal
            goal = goal.get(prefs.getStretchGoalId());
            goal.setId(prefs.getStretchGoalId());
            goal.setTimeBoxId(prefs.getUnplannedTimeBoxId());
            goal.setUpdated(taskList.getUpdated());
            goal.setStringId("@default");
            if (!taskList.getId().equals("@default"))
                goal.setStringId(taskList.getId());
        } else {
            //update tasklist,need to update goal
            goal = goal.get(id);
            goal.setNickName(taskList.getTitle());
        }

        return goal;
    }

    @Override
    public PendingStep convertToPendingStep(Object taskObject) {
        if (!(taskObject instanceof Task))
            throw new IllegalArgumentException("Invalid object passed to this method");
        Task task = (Task) taskObject;
        PendingStep pendingStep = new PendingStep(context);
        long id = pendingStep.getIdIfExists(task.getId());
        if (id == 0) {
            //new step ,need to insert
            if (task.getStatus().equals("needsAction") || (task.getDeleted() == Boolean.FALSE)) {
                pendingStep.setId(0);
                pendingStep.setTime(Constants.MAX_SLOT_DURATION);
                pendingStep.setStringId(task.getId());
                pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
                pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
                pendingStep.setUpdated(task.getUpdated());
            }
            if (!task.getStatus().equals("needsAction"))
                pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
        } else {
            pendingStep = pendingStep.get(id);
        }
        //check for parent
        id = pendingStep.getIdIfExists(task.getParent());
        pendingStep.setSubStepOf(id);
        if (id != 0) {
            pendingStep.setPendingStepType(PendingStep.PendingStepType.SUB_STEP);
            //and its parents type set to Series Step
            PendingStep ps = new PendingStep(context).get(id);
            if (ps.getPendingStepType() == PendingStep.PendingStepType.SINGLE_STEP)
                ps.setPendingStepType(PendingStep.PendingStepType.SERIES_STEP);
            pendingStep.setGoalId(ps.getGoalId());
            ps.save();
        }
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
        YodaCalendar yodaCalendar = new YodaCalendar(context);
        TimeBox timeBox = new TimeBox(context);
        TaskLists result = service.tasklists().list().execute();
        List<TaskList> taskLists = result.getItems();
        Goal goal=new Goal(context);
        List<String> serverStringGoalIds=new ArrayList<>();
        if (taskLists != null) {
            for (TaskList taskList : taskLists) {
                serverStringGoalIds.add(taskList.getId());
                goal = convertToGoal(taskList);
                if (compareDateTime(taskList.getUpdated(), goal.getUpdated())) {
                    //means server has latest data
                    goal.setUpdated(taskList.getUpdated());
                    if (prefs.getStretchGoalId() != goal.getId())
                        goal.save();
                }else{
                    //means app has latest data
                    if(goal.isDeleted()){
                        service.tasklists().delete(taskList.getId()).execute();
                        goal.deletePendingSteps();
                        goal.delete();
                        continue;
                    }else if (prefs.getStretchGoalId() != goal.getId()) {
                        taskList.setTitle(goal.getNickName());
                        service.tasklists().update(taskList.getId(), taskList).execute();
                    }
                }
                yodaCalendar.setTimeBox(timeBox.get(goal.getTimeBoxId()));
                Logger.log(TAG, "Task List: " + taskList.toString() + "  Goal: " + goal.toString());
                com.google.api.services.tasks.model.Tasks tasks=null;
                if(!goal.isDeleted())
                    tasks = service.tasks().list(taskList.getId()).setShowCompleted(false).setShowCompleted(false).execute();
                if (tasks != null) {
                    List<Task> myTasks = tasks.getItems();
                    if (myTasks != null) {
                        for (Task task : myTasks) {
                            PendingStep pendingStep = convertToPendingStep(task);
                            if(pendingStep.getId()==0){
                                //means that this new step , insert it to our database
                                pendingStep.setStringId(task.getId());
                                pendingStep.setGoalStringId(taskList.getId());
                                pendingStep.setGoalId(goal.getId());
                                pendingStep.save();
                            }else
                            {
                                if (compareDateTime(task.getUpdated(), pendingStep.getUpdated())) {
                                    //means server has latest data
                                    if (task.getStatus().equals("needsAction")) {
                                        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
                                        //pendingStep.setTime(Constants.MAX_SLOT_DURATION);
                                    }
                                    else {
                                        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                                        pendingStep.cancelAlarm();
                                        pendingStep.freeSlot();
                                        pendingStep.setSlotId(0);
                                    }
                                    if (task.getDeleted() != null) {
                                        pendingStep.setDeleted(task.getDeleted());
                                    }
                                    if (pendingStep.isDeleted()) {
                                        pendingStep.cancelAlarm();
                                        pendingStep.freeSlot();
                                        pendingStep.delete();
                                    } else {
                                        pendingStep.setGoalId(goal.getId());
                                        pendingStep.setGoalStringId(goal.getStringId());
                                        pendingStep.save();//insert or update
                                    }
                                } else {//means app has latest data
                                    if (pendingStep.isDeleted()) {
                                        service.tasks().delete(goal.getStringId(), pendingStep.getStringId()).execute();
                                        pendingStep.cancelAlarm();;
                                        pendingStep.freeSlot();
                                        pendingStep.delete();

                                    } else {
                                        if (pendingStep.getPendingStepStatus() == PendingStep.PendingStepStatus.COMPLETED) {
                                            task.setStatus("completed");
                                            pendingStep.cancelAlarm();
                                            pendingStep.setSlotId(0);
                                        }
                                        task.setTitle(pendingStep.getNickName());
                                        service.tasks().update(goal.getStringId(), pendingStep.getStringId(), task).execute();
                                        pendingStep.save();
                                    }
                                }
                            }
                            Logger.log(TAG, "Task : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                        }
                    }
                }
                if(!goal.isDeleted())
                    yodaCalendar.rescheduleSteps(goal.getId());
            }
            List<String> appStringGoalIds=goal.getDistinctGoalStringIds();
            if(appStringGoalIds!=null && serverStringGoalIds!=null) {
                appStringGoalIds.removeAll(serverStringGoalIds);
                appStringGoalIds.remove("");
                appStringGoalIds.remove("@default");
                for (String stringGoalId : appStringGoalIds) {
                    goal.deleteAllSteps(stringGoalId);
                    goal.deleteGoal(stringGoalId);
                }
            }
        }
    }
    public void doExport() throws IOException {
        Logger.log(TAG, "Exporting Goals and Pending steps.wait...");
        exportGoals();
    }

    private void exportGoals() throws IOException {
        Goal goal = new Goal(context);
        List<Goal> goals = goal.getAll(Goal.GoalDeleted.SHOW_BOTH);
        if (goals != null) {
            for (Goal g : goals) {
                //export goals data
                TaskList taskList = (TaskList) buildGoal(g);
                Logger.log(TAG, "TaskList : " + taskList + "  Goal: " + g);
                //for new goals
                if (g.getStringId() == null || g.getStringId().equals("")) {
                    //for new goals simply insert
                    TaskList result = service.tasklists().insert(taskList).execute();
                    g.setStringId(result.getId());
                    g.setUpdated(result.getUpdated());
                    g.save();
                    //if there are already some steps set there goal string ids
                    g.setPendingStepGoalStringId(g.getStringId());
                } else {
                    //for old goals that are present at server side,
                    if (!g.getStringId().equals("@default")) {
                        TaskList result = service.tasklists().get(g.getStringId()).execute();
                        if (!compareDateTime(g.getUpdated(), result.getUpdated())) {
                            //means server has latest data
                            if (result.getId() == null) {
                                //means that goal is deleted
                                g.deleteAllSteps(g.getStringId());
                                g.deleteGoal(g.getStringId());
                            } else {
                                g.setNickName(result.getTitle());
                                g.setUpdated(result.getUpdated());
                                g.save();
                            }
                        } else {
                            //means app has latest data
                            if (!g.getStringId().equals("@default")) {
                                if (g.isDeleted()) {
                                    service.tasklists().delete(g.getStringId()).execute();
                                    g.deletePendingSteps();
                                    g.delete();
                                }
                                else {
                                    result = service.tasklists().update(g.getStringId(), taskList).execute();
                                    g.setUpdated(result.getUpdated());
                                    g.save();
                                }
                            }
                        }

                    }
                }
                exportPendingSteps(g);
            }
        }
    }

    private void exportPendingSteps(Goal goal) throws IOException {
        PendingStep pendingStep = new PendingStep(context);
        List<PendingStep> pendingSteps = pendingStep.getAll(PendingStep.PendingStepStatus.TODO,
                PendingStep.PendingStepDeleted.SHOW_BOTH, goal.getId());
        if (pendingSteps != null) {
            List<PendingStep> completed = pendingStep.getAll(PendingStep.PendingStepStatus.COMPLETED,
                    PendingStep.PendingStepDeleted.SHOW_BOTH, goal.getId());
            if (completed != null)
                pendingSteps.addAll(completed);

            for (PendingStep ps : pendingSteps) {
                Task task = (Task) buildPendingStep(ps);
                if(ps.getGoalStringId()==null || ps.getGoalStringId().equals(""))
                    ps.setGoalStringId(goal.getStringId());
                task.setTitle(ps.getNickName());
                task.setNotes("This step is added by Yoda");
                if (ps.getStepDate() != null)
                    task.setDue(new DateTime(ps.getStepDate()));
                Task result = service.tasks().get(ps.getGoalStringId(), ps.getStringId()).execute();
                if (ps.getStringId() == null || ps.getStringId().equals("")) {
                    //new steps simply insert
                    result = service.tasks().insert(goal.getStringId(), task).execute();
                    ps.setStringId(result.getId());
                    ps.setUpdated(result.getUpdated());
                    ps.setGoalStringId(goal.getStringId());
                    ps.save();
                    if (ps.getPendingStepType() == PendingStep.PendingStepType.SUB_STEP) {
                        service.tasks().move(goal.getStringId(), ps.getStringId()).setParent(task.getParent()).execute();
                        Logger.log(TAG, "Move Task to Parent : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                    }
                    Logger.log(TAG, "Insert Task : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                } else {
                    //old steps,update needed
                    if (compareDateTime(ps.getUpdated(), result.getUpdated())) {
                        //means app that app has latest data
                        //temporary code
                        if(result.getDeleted()==Boolean.TRUE)
                            ps.setDeleted(true);
                        if(result.getStatus().equals("completed"))
                            ps.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                       //end
                        if (ps.isDeleted()) {
                            service.tasks().delete(ps.getGoalStringId(), ps.getStringId()).execute();
                            ps.delete();
                            ps.cancelAlarm();
                            ps.freeSlot();
                        }else {
                            if (ps.getPendingStepStatus() == PendingStep.PendingStepStatus.COMPLETED) {
                                task.setStatus("completed");
                                ps.cancelAlarm();
                                ps.freeSlot();
                            }
                            task.setTitle(ps.getNickName());
                            result = service.tasks().update(ps.getGoalStringId(), ps.getStringId(), task).execute();
                            ps.setUpdated(result.getUpdated());
                            Logger.log(TAG, "Updated Task : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                            ps.save();
                        }
                    } else {
                        //means that server has latest data
                        task = result;
                        if (task.getDeleted() == Boolean.TRUE) {
                            ps.cancelAlarm();
                            ps.delete();
                        } else {
                            if (task.getStatus().equals("completed")) {
                                ps.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                                ps.setSlotId(0);
                                ps.cancelAlarm();
                            }
                            ps.setNickName(task.getTitle());
                            ps.save();
                        }
                    }
                }
            }
        }
    }
    private boolean compareDateTime(DateTime dateTime1,DateTime dateTime2){
        TimeZone timeZone= TimeZone.getDefault();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d1=null;
        Date d2=null;
        boolean res=false;
        try {
            calendar.setTime(sdf.parse(dateTime1.toString()));
            calendar.add(Calendar.MILLISECOND, timeZone.getRawOffset());
            calendar.getTime();
            d1=calendar.getTime();
            d2=sdf.parse(CalendarUtils.getStringToRFCTimestamp(dateTime2.toString()).toString());
            if(d1.compareTo(d2)>=0)
                res=true;
            else
                res=false;
        } catch (ParseException e) {
            res=true;
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            return res;
        }

    }

}
