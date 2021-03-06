package com.greylabs.ydo.apis.googleacc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.greylabs.ydo.apis.Sync;
import com.greylabs.ydo.apis.TaskAccount;
import com.greylabs.ydo.database.MetaData;
import com.greylabs.ydo.enums.TimeBoxWhen;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.PendingStep;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.scheduler.YodaCalendar;
import com.greylabs.ydo.utils.CalendarUtils;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.GoalUtils;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
    DateTime lastSyncDate=null;

    int NUMBER_OF_DAYS_TO_EXPORT;

    public GoogleAccount(Context context) {
        super(context);
        prefs = Prefs.getInstance(context);
    }

    public GoogleAccount(Context context, int NUMBER_OF_DAYS_TO_EXPORT){
        super(context);
        prefs = Prefs.getInstance(context);
        this.NUMBER_OF_DAYS_TO_EXPORT = NUMBER_OF_DAYS_TO_EXPORT;
    }

    public List<String> getUsers() {
        return users;
    }

    @Override
    public void authenticate() {
        credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(TasksScopes.TASKS));
        credential.setSelectedAccountName(prefs.getDefaultAccountEmailId());
        service = new com.google.api.services.tasks.Tasks.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("ydo").build();
        //((Activity)context).startActivity(credential.newChooseAccountIntent());
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
        Logger.d(TAG, users.get(position));
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
        if (pendingStep.getNotes() != null) {
            task.setNotes(pendingStep.getNotes());
        }
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
            //old if condition
            //task.getStatus().equals("needsAction") ||(task.getDeleted()==null || task.getDeleted()==Boolean.FALSE)
//            if (task.getStatus().equals("needsAction") ||
//                    (task.getDeleted()==null || task.getDeleted()==Boolean.FALSE)) {
            pendingStep.setId(0);
            pendingStep.setTime(Constants.MAX_SLOT_DURATION);
            pendingStep.setStringId(task.getId());
            pendingStep.setPendingStepType(PendingStep.PendingStepType.SINGLE_STEP);
            pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
            pendingStep.setExpire(PendingStep.PendingStepExpire.NOT_EXPIRE);
            pendingStep.setUpdated(task.getUpdated());
            if (task.getNotes() != null) {
                pendingStep.setNotes(task.getNotes());
            }
//            }
            if ((task.getDeleted() != null || task.getDeleted() == Boolean.TRUE)) {
                pendingStep.setDeleted(true);
            } else {
                pendingStep.setDeleted(false);
            }
            if (task.getStatus().equals("needsAction")) {
                pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
            } else {
                pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
            }
        } else {
            pendingStep = pendingStep.get(id);
        }
        //check for parent
        id = pendingStep.getIdIfExists(task.getParent());
        if (id != 0) {
            pendingStep.setSubStepOf(id);
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
        Goal goal = new Goal(context);
        List<String> serverStringGoalIds = new ArrayList<>();
        if (taskLists != null) {
            for (TaskList taskList : taskLists) {
                serverStringGoalIds.add(taskList.getId());
                goal = convertToGoal(taskList);
                if (compareDateTime(taskList.getUpdated(), goal.getUpdated())) {
                    //means server has latest data
                    Logger.d(TAG, "Goal:::Server has Latest Data: Server Update:" + taskList.getUpdated() + " App Update:" + goal.getUpdated());
                    goal.setUpdated(taskList.getUpdated());
                    if (prefs.getStretchGoalId() != goal.getId())
                        goal.save();
                }
                yodaCalendar.setTimeBox(timeBox.get(goal.getTimeBoxId()));
                Logger.d(TAG, "Task List: " + taskList.toString() + "  Goal: " + goal.toString());
                com.google.api.services.tasks.model.Tasks tasks = null;
                if (!goal.isDeleted()) {
                    com.google.api.services.tasks.Tasks.TasksOperations.List list
                            = service.tasks().list(taskList.getId()).setShowCompleted(true).setShowDeleted(true);

                    if(prefs.getLastSyncDate()!=null){
                        list.setUpdatedMin(prefs.getLastSyncDate().toStringRfc3339());
                    }
                    tasks=list.execute();
                }
                if (tasks != null) {
                    List<Task> myTasks = tasks.getItems();
                    if (myTasks != null) {
                        for (Task task : myTasks) {

                            // Among all the imported tasks if a tasks title is empty it is not added as a step

                            if(!task.getTitle().isEmpty()) {
                                PendingStep pendingStep = convertToPendingStep(task);
                                if (pendingStep.getId() == 0 && (pendingStep.isDeleted()
                                        || pendingStep.getPendingStepStatus() == PendingStep.PendingStepStatus.COMPLETED)) {
                                    continue;
                                }
                                if (pendingStep.getId() == 0) {
                                    //means that this new step , insert it to our database
                                    pendingStep.setStringId(task.getId());
                                    pendingStep.setGoalStringId(taskList.getId());
                                    pendingStep.setGoalId(goal.getId());
                                    pendingStep.setPriority(goal.getHighestPriority(goal.getId()) + 1);
                                    pendingStep.save();
                                } else {
                                    if (compareDateTime(task.getUpdated(), pendingStep.getUpdated())) {
                                        //means server has latest data
                                        Logger.d(TAG, "PendingStep:::Server has latest data. Server update:" + task.getUpdated() + " App updated:" + pendingStep.getUpdated());
                                        if (task.getStatus().equals("needsAction")) {
                                            pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
                                            //pendingStep.setTime(Constants.MAX_SLOT_DURATION);
                                        } else {
                                            pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                                            pendingStep.cancelAlarm();
                                            pendingStep.freeSlot();
                                            pendingStep.setSlotId(0);
                                            Logger.d(TAG, "Import: step marked completed in local database");
                                        }
                                        //update
                                        pendingStep.setNickName(task.getTitle());
                                        pendingStep.setUpdated(task.getUpdated());
                                        pendingStep.save();

                                        //check for deleted
                                        if (task.getDeleted() != null) {
                                            pendingStep.setDeleted(task.getDeleted());
                                        }
                                        if (pendingStep.isDeleted()) {
                                            pendingStep.cancelAlarm();
                                            pendingStep.freeSlot();
                                            pendingStep.delete();
                                            Logger.d(TAG, "Step Deleted" + pendingStep);
                                        }
                                    }
                                }
                                Logger.d(TAG, "Task : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                                lastSyncDate = task.getUpdated();
                            }
                        }
                    }
                }
                if (!goal.isDeleted())
                    yodaCalendar.rescheduleSteps(goal.getId());
            }
            List<String> appStringGoalIds = goal.getDistinctGoalStringIds();
            Logger.d(TAG,"Goals App String Ids:"+appStringGoalIds.toString());
            if (appStringGoalIds != null && serverStringGoalIds != null) {
                appStringGoalIds.removeAll(serverStringGoalIds);
                appStringGoalIds.remove("");
                appStringGoalIds.remove("@default");
                for (String stringGoalId : appStringGoalIds) {
                    Logger.d(TAG, "Goal string ID being deleted:" + appStringGoalIds.toString());
                    Goal g=goal.get(goal.getIdIfExists(stringGoalId));
                    GoalUtils.clearAlarms(g, context);
                    yodaCalendar.detachTimeBox(g.getTimeBoxId());
                    goal.deleteAllSteps(stringGoalId);
                    goal.deleteGoal(stringGoalId);
                }
            }
        }
    }

    public void doExport() throws IOException {
        Logger.d(TAG, "Exporting Goals and Pending steps.wait...");
        exportGoals();
    }

    public void exportToCalendar() throws IOException {

        credential = GoogleAccountCredential.usingOAuth2(context, Collections.singletonList(CalendarScopes.CALENDAR));
        credential.setSelectedAccountName(prefs.getDefaultAccountEmailId());

        com.google.api.services.calendar.Calendar calendar = new com.google.api.services.calendar.Calendar.Builder(httpTransport,
                jsonFactory, credential)
                .setApplicationName("ydo").build();

        ArrayList<PendingStep> pendingSteps= new PendingStep(context).getAllPendingStepsByStatus(PendingStep.PendingStepStatus.TODO, MetaData.TablePendingStep.stepDate + " desc ");

        long MILLIS_IN_DAY = 60 * 60 * 24 * 1000;
        long SLOT_LENGTH = TimeBoxWhen.getSlotLength();
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / MILLIS_IN_DAY) * MILLIS_IN_DAY;

        String CALENDAR_ID = "primary";

        for (PendingStep ps : pendingSteps){

            Log.e(TAG, ps.getPendingStepStatus().toString() + " : " + ps.getNickName());

            try {
                if (compareDateTime(new DateTime(false, (new Date(dateOnly)).getTime() + NUMBER_OF_DAYS_TO_EXPORT * MILLIS_IN_DAY, 0), new DateTime(false, ps.getStepDate().getTime(), 0))) {

                    Event event = new Event()
                            .setSummary(ps.getNickName());

                    EventDateTime start = new EventDateTime().setDateTime(new DateTime(false, ps.getStepDate().getTime(), 0));
                    event.setStart(start);

                    EventDateTime end = new EventDateTime().setDateTime(new DateTime(false, ps.getStepDate().getTime() + SLOT_LENGTH, 0));
                    event.setEnd(end);

                    event = calendar.events().insert(CALENDAR_ID, event).execute();
                    Log.e(TAG, event.getHtmlLink());
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    private void exportGoals() throws IOException {
        YodaCalendar yodaCalendar = new YodaCalendar(context);
        Goal goal = new Goal(context);
        List<Goal> goals = goal.getAll(Goal.GoalDeleted.SHOW_BOTH);
        if (goals != null) {
            for (Goal g : goals) {
                //export goals data
                TaskList taskList = (TaskList) buildGoal(g);
                Logger.d(TAG, "TaskList : " + taskList + "  Goal: " + g);
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
                        TaskList result = service.tasklists().get(g.getStringId()).execute();
                        if (compareDateTime(g.getUpdated(), result.getUpdated())) {
                            Logger.d(TAG, "Goal:::App has Latest Data: Server Update:" + result.getUpdated() + " App Update:" + g.getUpdated());
                                if (g.isDeleted()) {
                                    GoalUtils.clearAlarms(g,context);
                                    yodaCalendar.detachTimeBox(goal.getTimeBoxId());
                                    g.deletePendingSteps();
                                    g.delete();
                                    service.tasklists().delete(g.getStringId()).execute();
                                } else {
                                    result = service.tasklists().update(g.getStringId(), taskList).execute();
                                    g.setUpdated(result.getUpdated());
                                    g.save();
                                }
                        }
                }
                exportPendingSteps(g);
            }
        }
        if (lastSyncDate!=null)
            prefs.setLastSyncDate(lastSyncDate);
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

            Iterator<PendingStep> it=pendingSteps.iterator();
            while (it.hasNext()){
                PendingStep ps=it.next();
                if(prefs.getLastSyncDate()!=null && !compareDateTime(ps.getUpdated(), prefs.getLastSyncDate())){
                    it.remove();
                }
            }

            for (PendingStep ps : pendingSteps) {
                Task task = (Task) buildPendingStep(ps);
                if (ps.getGoalStringId() == null || ps.getGoalStringId().equals(""))
                    ps.setGoalStringId(goal.getStringId());
                task.setTitle(ps.getNickName());
                if (ps.getStepDate() != null)
                    task.setDue(new DateTime(ps.getStepDate()));
                Task result = null;
                if (ps.getStringId() != null || !ps.getStringId().equals("")) {
                    result = service.tasks().get(ps.getGoalStringId(), ps.getStringId()).execute();
                }
                if (ps.getStringId() == null || ps.getStringId().equals("")) {
                    //new steps simply insert
                    result = service.tasks().insert(goal.getStringId(), task).execute();
                    ps.setStringId(result.getId());
                    ps.setUpdated(result.getUpdated());
                    ps.setGoalStringId(goal.getStringId());
                    ps.save();
                    if (ps.getPendingStepType() == PendingStep.PendingStepType.SUB_STEP) {
                        service.tasks().move(goal.getStringId(), ps.getStringId()).setParent(task.getParent()).execute();
                        Logger.d(TAG, "Move Task to Parent : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                    }
                    Logger.d(TAG, "Insert Task : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                } else {
                    if (compareDateTime(ps.getUpdated(), result.getUpdated())) {
                        //means app that app has latest data
                        Logger.d(TAG, "Pending Step:::App has Latest Data: Server Update:" + result.getUpdated() + " App Update:" + ps.getUpdated());
                        if (ps.isDeleted()) {
                            ps.delete();
                            ps.cancelAlarm();
                            ps.freeSlot();
                            service.tasks().delete(ps.getGoalStringId(), ps.getStringId()).execute();
                        } else {
                            task.setTitle(ps.getNickName());
                            result = service.tasks().update(ps.getGoalStringId(), ps.getStringId(), task).execute();
                            ps.setUpdated(result.getUpdated());
                            Logger.d(TAG, "Updated Task : " + task.toString() + "  Pending Step: " + pendingStep.toString());
                            ps.save();
                        }
                    }
                }
            }
        }
    }

    private boolean compareDateTime(DateTime dateTime1, DateTime dateTime2) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(timeZone);
        Date d1 = null;
        Date d2 = null;
        boolean res = false;
        try {
            calendar.setTime(sdf.parse(dateTime1.toString()));
            calendar.add(Calendar.MILLISECOND, timeZone.getRawOffset());
            calendar.getTime();
            d1 = calendar.getTime();
            d2 = sdf.parse(CalendarUtils.getStringToRFCTimestamp(dateTime2.toString()).toString());
            Logger.d(TAG, "Comparison: D1: " + d1 + "  D2: " + d2);
            if (d1.compareTo(d2) > 0)
                res = true;
            else
                res = false;
        } catch (ParseException e) {
            res = true;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }

    }

}
