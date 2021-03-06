package com.greylabs.ydo.threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.greylabs.ydo.database.QuickStart;
import com.greylabs.ydo.models.Goal;
import com.greylabs.ydo.models.Slot;
import com.greylabs.ydo.models.TimeBox;
import com.greylabs.ydo.scheduler.YodaCalendar;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;

import java.util.List;

public class QuickStartAsyncTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    Context context;
    Handler myHandler;

    public QuickStartAsyncTask(Context activityContext, Handler handler) {
        this.context = activityContext;
        this.myHandler = handler;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(Constants.MSG_INITIALIZING_QUICK_START);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        QuickStart quickStart = new QuickStart(context);
        quickStart.quickStart();
        Slot slot=new Slot(context);
        slot.setDefaultGoalDetails();
        //schedule all steps
        YodaCalendar yodaCalendar=new YodaCalendar(context);
        List<Goal> goals=new Goal(context).getAll(Goal.GoalDeleted.SHOW_NOT_DELETED);
        TimeBox timeBox=new TimeBox(context);
        Goal stretchGoal=new Goal(context);
        stretchGoal.setId(Prefs.getInstance(context).getStretchGoalId());
        goals.remove(stretchGoal);
        for (Goal goal:goals ){
            timeBox=timeBox.get(goal.getTimeBoxId());
            yodaCalendar.setTimeBox(timeBox);
            try {
                yodaCalendar.attachTimeBox(goal.getId());
                yodaCalendar.rescheduleSteps(goal.getId());
            }catch (NullPointerException e){
                //this means time-box has not available slots, so we are deleting steps
                e.printStackTrace();
                goal.deletePendingSteps();
            }
        }
        timeBox=timeBox.get(Prefs.getInstance(context).getUnplannedTimeBoxId());
        yodaCalendar.setTimeBox(timeBox);
        yodaCalendar.rescheduleSteps(stretchGoal.getId());
        return null;
    }

//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        Logger.d("InitCalendarAsyncTask", "Calendar Initialized");
        Message message = new Message();
        message.obj = result;
        myHandler.sendMessage(message);
    }
}