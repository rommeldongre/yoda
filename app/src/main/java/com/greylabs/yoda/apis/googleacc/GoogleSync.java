package com.greylabs.yoda.apis.googleacc;

import android.content.Context;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.model.Tasks;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 8/12/2015.
 */
public class GoogleSync {

    private GoogleAccount googleAccount;
    private Context context;

    public GoogleSync(Context context) {
        this.context = context;
        googleAccount=new GoogleAccount(context);
    }

    public void sync() throws IOException {
        //import all tasks
        TaskLists result=googleAccount.getService().tasklists().list().execute();
        List<TaskList> taskLists=result.getItems();
        if(taskLists!=null){
            for (TaskList taskList:taskLists){
                Goal goal=googleAccount.convertToGoal(taskList);
                Tasks tasks=googleAccount.getService().tasks().list(taskList.getId()).execute();
                for (Task task:tasks.getItems()){
                    PendingStep pendingStep=googleAccount.convertToPendingStep(task);
                    pendingStep.setGoalId(goal.getId());
                    pendingStep.save();
                }
            }
        }
    }

}
