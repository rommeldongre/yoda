package com.greylabs.yoda.utils;

import com.greylabs.yoda.activities.Yoda;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.models.Slot;
import com.greylabs.yoda.scheduler.AlarmScheduler;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 11/2/2015.
 */
public class GoalUtils {

    public static void rescheduleAllSteps(){
        List<PendingStep> pendingSteps=new PendingStep(Yoda.getContext()).getAll(PendingStep.PendingStepStatus.TODO);
        Calendar calendar=Calendar.getInstance();
        if(pendingSteps!=null) {
            AlarmScheduler alarmScheduler = new AlarmScheduler(Yoda.getContext());
            for (PendingStep pendingStep : pendingSteps) {
                Slot slot = new Slot(Yoda.getContext()).get(pendingStep.getSlotId());
                if(slot.getScheduleDate().compareTo(calendar.getTime())>0) {
                    alarmScheduler.setStepId(pendingStep.getId());
                    alarmScheduler.setSubStepId(pendingStep.getSubStepOf());
                    alarmScheduler.setPendingStepType(pendingStep.getPendingStepType());
                    alarmScheduler.setStartTime(slot.getWhen().getStartTime());
                    alarmScheduler.setDuration(pendingStep.getTime());
                    alarmScheduler.setAlarmDate(slot.getScheduleDate());
                    alarmScheduler.cancel();//cancel previous alarm if any
                    alarmScheduler.setAlarm();
                }
            }
        }
    }
}
