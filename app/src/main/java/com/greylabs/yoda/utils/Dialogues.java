package com.greylabs.yoda.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.scheduler.AlarmScheduler;

public class Dialogues {

    static Dialog dialog;
    static PendingStep pendingStep;
    static Goal goal = null;
    static Context context;
    static AlarmScheduler alarmScheduler;
    static LinearLayout llButtons;
    static LinearLayout llExcuseLog;
    static EditText edtExcuse;

    public static void showNowNotificationDialogue(Context context, String CALLER, AlarmScheduler alarmScheduler) {
        //get the pendingStep object
        context = context;
        alarmScheduler = alarmScheduler;
        pendingStep = new PendingStep(context);
        goal = null;// = new Goal(context);
        if(alarmScheduler!=null) {
            pendingStep = new PendingStep(context).get(alarmScheduler.getStepId());
        }

        if(pendingStep!=null){
            goal = new Goal(context).get(pendingStep.getGoalId());
        }

        // custom dialog
        dialog = new Dialog(context);
        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dailogue_now_notification);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.dailogue_now_notification, null);
        dialog.setContentView(viewGroup);

        dialog.setTitle(context.getString(R.string.titleNowNotification));

        // set the custom dialog components - text, image and button
        TextView tvGoalName = (TextView) viewGroup.findViewById(R.id.tvGoalNameNowNotification);
        TextView tvStepName = (TextView) viewGroup.findViewById(R.id.tvStepNameNowNotification);
//        TextView tvTime = (TextView) viewGroup.findViewById(R.id.tvTime);
        edtExcuse = (EditText) viewGroup.findViewById(R.id.edtExcuseNowNotification);
        LinearLayout llDidIt = (LinearLayout) viewGroup.findViewById(R.id.llDidNowNotification);
        LinearLayout llDoingIt = (LinearLayout) viewGroup.findViewById(R.id.llDoingItNowNotification);
        LinearLayout llMissedIt = (LinearLayout) viewGroup.findViewById(R.id.llMissedNowNotification);
        llButtons = (LinearLayout) viewGroup.findViewById(R.id.llButtonsNowNotification);
        llExcuseLog = (LinearLayout) viewGroup.findViewById(R.id.llLogExcuseNowNotification);
        Button btnLogExcuse = (Button) viewGroup.findViewById(R.id.btnLogExcuseNowNotification);
//        CardView cvEmptyView, cvNotEmptyView;
//    Toolbar toolbar;, btnCloseEmptyView ;//,btnCloseNotEmptyView;

        tvGoalName.setText(goal.getNickName());
        tvStepName.setText(pendingStep.getNickName());

        MyListener myListener = new MyListener();
        btnLogExcuse.setOnClickListener(myListener);
        llDidIt.setOnClickListener(myListener);
        llDoingIt.setOnClickListener(myListener);
        llMissedIt.setOnClickListener(myListener);

        dialog.getWindow().setType(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    static class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.llDidNowNotification :
//                    Logger.showMsg(context, "Did it");
                    if(pendingStep!=null){
                        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                        pendingStep.save();
                    }
                    dialog.dismiss();
                    break;

                case R.id.llDoingItNowNotification :
//                    Logger.showMsg(context, "Doing it");
                    if(pendingStep!=null){
                        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.DOING);
                        pendingStep.save();
                        Logger.log("TAG","Alarm Scheduler : "+alarmScheduler);
                        if(alarmScheduler==null)
                            alarmScheduler=new AlarmScheduler(context);
                        else
                            alarmScheduler.initContext(context);
                        alarmScheduler.setStepId(pendingStep.getId());
                        alarmScheduler.postponeAlarm(5);
                    }
                    dialog.dismiss();
                    break;

                case R.id.llMissedNowNotification :
                    if(pendingStep!=null){
                        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
                        pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
                        pendingStep.save();

                        llButtons.setVisibility(View.GONE);
                        llExcuseLog.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.btnLogExcuseNowNotification :
                    edtExcuse.getText();
                    // put this text into the pendingStep
//                if(pendingStep!=null){
//                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
//                    pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
//                    pendingStep.save();
//                }
                    dialog.dismiss();
                    break;
            }
        }
    }
}
