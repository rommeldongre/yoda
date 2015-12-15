package com.greylabs.yoda.utils;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jaybhay Vijay on 10/31/2015.
 */
public class PendingStepUtils {

    private static  final String TAG="PendingStepsUt";
    public static  void deletePendingStep(PendingStep pendingStep){
        pendingStep.cancelAlarm();
        pendingStep.freeSlot();
        if (pendingStep.getStringId()==null || pendingStep.getStringId().equals("")){
            Logger.d(TAG,"Pending step deleted:"+pendingStep.delete());
        }else {
            pendingStep.setDeleted(true);
            pendingStep.setSlotId(0);
            pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
            pendingStep.save();
            Logger.d(TAG,"Delete flag of pending step is set true");
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
        Logger.d(TAG,"Pending step "+pendingStep+" moved to goal :"+stretchGoal);
    }

    public static void markPendingStepDone(PendingStep pendingStep){
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.COMPLETED);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.freeSlot();
        pendingStep.setSlotId(0);
        pendingStep.save();
        pendingStep.cancelAlarm();
        Logger.d(TAG, "Pending step marked done :" + pendingStep);
    }


    public static void markPendingStepUnDone(PendingStep pendingStep) {
        pendingStep.setPendingStepStatus(PendingStep.PendingStepStatus.TODO);
        pendingStep.setUpdated(new DateTime(new Date(), TimeZone.getTimeZone("UTC")));
        pendingStep.save();
        Logger.d(TAG, "Pending step marked undone :" + pendingStep );
    }

}
