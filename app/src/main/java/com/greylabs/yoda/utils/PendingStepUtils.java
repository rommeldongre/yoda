package com.greylabs.yoda.utils;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jaybhay Vijay on 10/31/2015.
 */
public class PendingStepUtils {

    public static  void deletePendingStep(PendingStep pendingStep){
        pendingStep.cancelAlarm();
        pendingStep.freeSlot();
        if (pendingStep.getStringId()==null || pendingStep.getStringId().equals("")){
            pendingStep.delete();
        }else {
            pendingStep.setDeleted(true);
            pendingStep.setSlotId(0);
            pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
            pendingStep.save();
        }
    }
    public static void movePendingStepToStretchGoal(PendingStep pendingStep,Goal stretchGoal){
        pendingStep.cancelAlarm();
        pendingStep.freeSlot();
        pendingStep.setSlotId(0);
        pendingStep.setStringId("");
        pendingStep.setGoalStringId(stretchGoal.getStringId());
        pendingStep.setGoalId(stretchGoal.getId());
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.save();
    }

    public static void markPendingStepDone(PendingStep pendingStep){
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.freeSlot();
        pendingStep.setSlotId(0);
        pendingStep.save();
        pendingStep.cancelAlarm();
    }


    public static void markPendingStepUnDone(PendingStep pendingStep) {
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.save();
    }

}
