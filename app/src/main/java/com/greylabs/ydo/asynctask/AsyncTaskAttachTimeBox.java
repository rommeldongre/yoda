//package com.greylabs.yoda.asynctask;
//
//import android.content.Context;
//import android.os.Message;
//
//import com.greylabs.yoda.activities.ActAddNewGoal;
//import com.greylabs.yoda.scheduler.YodaCalendar;
//import com.greylabs.yoda.utils.Constants;
//
///**
// * Created by Jaybhay Vijay on 7/21/2015.
// */
//public class AsyncTaskAttachTimeBox extends AysncTaskWithProgressBar{
//
//
//    private long goalId;
//    protected ActAddNewGoal.MyHandler handler;
//    public AsyncTaskAttachTimeBox(Context context, ActAddNewGoal.MyHandler myHandler, Object object, String message, long goalId) {
//        super(context, object, message);
//        this.goalId=goalId;
//        this.handler=handler;
//    }
//
//    @Override
//    protected Void doInBackground(Object... objects) {
//        YodaCalendar yodaCalendar=(YodaCalendar)objects[0];
//        yodaCalendar.attachTimeBox(goalId);
//        return super.doInBackground(objects);
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        Message message=new Message();
//        message.obj=SUCCESS;
//        handler.handleMessage(message);
//    }
//}
