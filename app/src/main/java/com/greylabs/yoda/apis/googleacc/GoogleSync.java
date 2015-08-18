package com.greylabs.yoda.apis.googleacc;

import android.content.Context;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.greylabs.yoda.apis.Sync;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.utils.Prefs;

import java.io.IOException;
import java.util.List;
/**
 * Created by Jaybhay Vijay on 8/12/2015.
 */
public class GoogleSync implements Sync{

    private GoogleAccount googleAccount;
    private Context context;
    private com.google.api.services.tasks.Tasks service;

    public GoogleSync(Context context) {
        this.context = context;
        googleAccount=new GoogleAccount(context);
        this.service=(com.google.api.services.tasks.Tasks)googleAccount.getService();
    }

//    public void importToStretchGoal() throws IOException {
//        //import all tasks
//        YodaCalendar yodaCalendar=new YodaCalendar(context);
//        Prefs prefs=Prefs.getInstance(context);
//        TimeBox timeBox=new TimeBox(context);
//        yodaCalendar.setTimeBox(timeBox.get(prefs.getUnplannedTimeBoxId()));
//        TaskLists result=googleAccount.getService().tasklists().list().execute();
//        List<TaskList> taskLists=result.getItems();
//        if(taskLists!=null){
//            for (TaskList taskList:taskLists){
//                Tasks tasks=googleAccount.getService().tasks().list(taskList.getId()).execute();
//                if(tasks!=null) {
//                    for (Task task : tasks.getItems()) {
//                        PendingStep pendingStep = googleAccount.convertToPendingStep(task);
//                        pendingStep.setGoalId(prefs.getStretchGoalId());
//                        pendingStep.save();
//                    }
//                }
//            }
//        }
//        yodaCalendar.rescheduleSteps(prefs.getStretchGoalId());
//    }

    @Override
    public void sync() throws IOException {
        //import all tasks
        YodaCalendar yodaCalendar=new YodaCalendar(context);
        Prefs prefs=Prefs.getInstance(context);
        TimeBox timeBox=new TimeBox(context);

        TaskLists result=service.tasklists().list().execute();
        List<TaskList> taskLists=result.getItems();
        if(taskLists!=null){
            for (TaskList taskList:taskLists){
                Goal goal=googleAccount.convertToGoal(taskList);
                yodaCalendar.setTimeBox(timeBox.get(goal.getTimeBoxId()));
                Logger.log("TAG","Task List: "+ taskList.toString()+"  Goal: "+goal.toString());
                com.google.api.services.tasks.model.Tasks tasks=service.tasks().list(taskList.getId()).execute();
                if(tasks!=null) {
                    for (Task task : tasks.getItems()) {
                        PendingStep pendingStep = googleAccount.convertToPendingStep(task);
                        pendingStep.setGoalId(goal.getId());
                        pendingStep.setGoalStringId(goal.getStringId());
                        pendingStep.save();
                        Logger.log("TAG", "Task : " + taskList.toString() + "  Pending Step: " + goal.toString());
                    }
                }
                yodaCalendar.rescheduleSteps(goal.getId());
            }
        }
    }

}
