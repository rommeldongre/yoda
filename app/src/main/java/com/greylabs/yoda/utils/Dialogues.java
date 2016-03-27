package com.greylabs.yoda.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.greylabs.yoda.activities.ActHome;
import com.greylabs.yoda.apis.Utils;
import com.greylabs.yoda.models.Day;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.scheduler.AlarmScheduler;
import com.greylabs.yoda.scheduler.YodaCalendar;
import com.greylabs.yoda.threads.CalendarUpdateAsyncThread;
import com.greylabs.yoda.threads.InitCalendarAsyncTask;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Dialogues {

    private static final String TAG="Dialog";
    public static boolean isUpdating=false;
    Dialog dialog;
    PendingStep pendingStep = null;
    PendingStep.PendingStepStartEnd startEnd;
    Goal goal = null;
    Context context;
    AlarmScheduler alarmScheduler;
    LinearLayout llButtons;
    LinearLayout llExcuseLog;
    AutoCompleteTextView edtExcuse;
    String caller;

    private Set<String> history = new HashSet<>();
    private ArrayAdapter<String> adapter;
    private Prefs prefs;

    public Dialogues(Context passedContext) {
        this.context = passedContext;
    }

    public void showNowNotificationDialogue(String CALLER, AlarmScheduler myAlarmScheduler1, PendingStep.PendingStepStartEnd startEnd, PendingStep myPendingStep1) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        caller = CALLER;
        this.startEnd = startEnd;
        if (caller.equals(Constants.ALARM_SERVICE)) {
            alarmScheduler = myAlarmScheduler1;
            if (alarmScheduler != null) {
                pendingStep = new PendingStep(context).get(alarmScheduler.getStepId());
            }
        } else if (caller.equals(Constants.ACT_HOME)) {
            pendingStep = myPendingStep1;
            pendingStep.initDatabase(context);
        }

        if (pendingStep != null) {
            goal = new Goal(context).get(pendingStep.getGoalId());
        }

        if (caller.equals(Constants.ALARM_SERVICE) && Dialogues.this.startEnd == PendingStep.PendingStepStartEnd.END) {
            if ((pendingStep.getPendingStepStatus() == PendingStep.PendingStepStatus.TODO ||
                    pendingStep.getPendingStepStatus() == PendingStep.PendingStepStatus.DOING)) {
                if(   pendingStep.isExpire() == PendingStep.PendingStepExpire.EXPIRE) {
                    pendingStep.freeSlot();
                    String notesString = pendingStep.getNotes();
                    if (notesString != null)
                        notesString = notesString + "\n Expired";
                    else
                        notesString = "Expired";
                    pendingStep.setNotes(notesString);
                    pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                    pendingStep.setSlotId(0);
                    pendingStep.cancelAlarm();
                    pendingStep.save();
                }
            }
            return;
        }
        // custom dialog
        dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.dailogue_now_notification, null);
        dialog.setContentView(viewGroup);

        TextView tvGoalName = (TextView) viewGroup.findViewById(R.id.tvGoalNameNowNotification);
        TextView tvStepName = (TextView) viewGroup.findViewById(R.id.tvStepNameNowNotification);

        prefs = Prefs.getInstance(context);
        if(prefs.getExcuseHistory() != null)
            history = prefs.getExcuseHistory();

        edtExcuse = (AutoCompleteTextView) viewGroup.findViewById(R.id.edtExcuseNowNotification);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));
        edtExcuse.setAdapter(adapter);

        edtExcuse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtExcuse.showDropDown();
                return false;
            }
        });

        LinearLayout llDidIt = (LinearLayout) viewGroup.findViewById(R.id.llDidNowNotification);
        LinearLayout llDoingIt = (LinearLayout) viewGroup.findViewById(R.id.llDoingItNowNotification);
        LinearLayout llMissedIt = (LinearLayout) viewGroup.findViewById(R.id.llMissedNowNotification);
        llButtons = (LinearLayout) viewGroup.findViewById(R.id.llButtonsNowNotification);
        llExcuseLog = (LinearLayout) viewGroup.findViewById(R.id.llLogExcuseNowNotification);
        Button btnLogExcuse = (Button) viewGroup.findViewById(R.id.btnLogExcuseNowNotification);
        Button btnCancelExcuse = (Button) viewGroup.findViewById(R.id.btnCancelExcuseNowNotification);

        tvGoalName.setText(goal.getNickName());
        tvStepName.setText(pendingStep.getNickName());

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (pendingStep.getPendingStepStatus() == PendingStep.PendingStepStatus.DOING
                        || pendingStep.getPendingStepStatus() == PendingStep.PendingStepStatus.TODO)
                    checkBackInFiveMins();
                if (caller.equals(Constants.ACT_HOME)) {
                    ((ActHome) context).onDialogueClosed();
//                    boolean c=pendingStep.getPendingStepStatus()== PendingStep.PendingStepStatus.TODO;
//                    if(new Date().compareTo(pendingStep.getStepDate())<0 )
//                        checkBackInFiveMins();
////                    if(Dialogues.this.startEnd== PendingStep.PendingStepStartEnd.END){
////                        checkExpiryOfStep();
////                    }
                }
                Day day = new Day(context);
                if (CalendarUtils.compareOnlyDates(day.getFirstDay(), new Date()) == true) {
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            Goal goal = new Goal(context).get(pendingStep.getGoalId());
                            TimeBox timeBox = new TimeBox(context).get(goal.getTimeBoxId());
                            YodaCalendar yodaCalendar = new YodaCalendar(context, timeBox);
                            yodaCalendar.rescheduleSteps(goal.getId());
                            Logger.d(TAG, "In Dialog:steps rescheduled");
                            return null;
                        }
                    };
                    task.execute();
                } else {
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            isUpdating = true;
                            Logger.d(TAG, "In Dialog: Found calendar not up to date, updating calendar");
                            YodaCalendar yodaCalendar = new YodaCalendar(context);
                            yodaCalendar.updateCalendar();
                            Logger.d(TAG, "In Dialog:steps rescheduled");
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            isUpdating = false;
                            super.onPostExecute(o);
                        }
                    };

                    if (isUpdating == false) {
                        task.execute();
                        isUpdating = true;
                    }
                }
            }
        });

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        btnLogExcuse.setOnClickListener(myOnClickListener);
        btnCancelExcuse.setOnClickListener(myOnClickListener);
        llDidIt.setOnClickListener(myOnClickListener);
        llDoingIt.setOnClickListener(myOnClickListener);
        llMissedIt.setOnClickListener(myOnClickListener);

        dialog.getWindow().setType(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void checkExpiryOfStep() {
        if (pendingStep.isExpire() == PendingStep.PendingStepExpire.EXPIRE) {
            //delete or mark step as deleted
            pendingStep.freeSlot();
            String notesString = pendingStep.getNotes();
            if (notesString != null)
                notesString = notesString + "\n Expired";
            else
                notesString = "Expired";
            pendingStep.setNotes(notesString);
            pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
            pendingStep.setSlotId(0);
            pendingStep.cancelAlarm();
            pendingStep.save();
        } else {
            //reschedule steps set current step as TODO.
            pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
            pendingStep.save();
            Goal goal = new Goal(context).get(pendingStep.getGoalId());
            TimeBox timeBox = new TimeBox(context).get(goal.getTimeBoxId());
            YodaCalendar yodaCalendar = new YodaCalendar(context, timeBox);
            yodaCalendar.rescheduleSteps(goal.getId());
        }
    }

    class MyOnClickListener implements View.OnClickListener {
        boolean checkExpiry = false;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llDidNowNotification:
                    if (pendingStep != null) {
                        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
                        pendingStep.save();
                    }
                    dialog.dismiss();
                    break;
                case R.id.llDoingItNowNotification:
//                    if (pendingStep != null) {
//                      //checkBackInFiveMins();
//                    }
                    dialog.dismiss();
                    break;
                case R.id.llMissedNowNotification:
                    if (pendingStep != null) {
                        //set dialogue's gravity as top
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.TOP;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                        window.setAttributes(wlp);

                        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
                        pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
                        pendingStep.save();
                        checkExpiry = true;
                        llButtons.setVisibility(View.GONE);
                        llExcuseLog.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btnLogExcuseNowNotification:

                    // Logging the missed steps notes along with the date

                    String notesString = pendingStep.getNotes();
                    if(notesString == null)
                        notesString = (edtExcuse.getText().toString()) + " - " + CalendarUtils.getFormattedDateWithSlot(new Date());
                    else
                        notesString = (edtExcuse.getText().toString()) + " - " + CalendarUtils.getFormattedDateWithSlot(new Date()) + "\r\n" + notesString;

                    addSearchInput((edtExcuse.getText().toString()));
                    prefs.setExcuseHistory(history);

                    Log.i("Excuse entered", notesString);
                    pendingStep.setNotes(notesString);
                    pendingStep.save();
                    Log.i("Excuse retrieved", new PendingStep(context).get(pendingStep.getId()).getNotes());
                    // put this text into the pendingStep
//                if(pendingStep!=null){
//                    pendingStep.sebtPendingStepStatus(PendingStep.PendingStepStatus.MISSED);
//                    pendingStep.setSkipCount(pendingStep.getSkipCount() + 1);
//                    pendingStep.save();
//                }
                    dialog.dismiss();
                    break;

                case R.id.btnCancelExcuseNowNotification:
                    llButtons.setVisibility(View.VISIBLE);
                    llExcuseLog.setVisibility(View.GONE);
                    break;
            }
//            if(startEnd== PendingStep.PendingStepStartEnd.END){
//                checkExpiryOfStep();
//            }
        }
    }

    private void addSearchInput(String input)
    {
        if (!history.contains(input))
        {
            history.add(input);
            adapter.notifyDataSetChanged();
        }
    }

    private void checkBackInFiveMins(){
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.DOING);
        pendingStep.save();
        Logger.d("TAG", "Alarm Scheduler : " + alarmScheduler);
        if (alarmScheduler == null)
            alarmScheduler = new AlarmScheduler(context);
        else
            alarmScheduler.initContext(context);
        alarmScheduler.setStepId(pendingStep.getId());
        alarmScheduler.setAlarmDate(new Date());
        alarmScheduler.postponeAlarm(5);
    }

}
