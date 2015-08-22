package com.greylabs.yoda.utils;

import android.content.Context;
import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.models.Goal;
import com.greylabs.yoda.models.PendingStep;
import com.greylabs.yoda.scheduler.AlarmScheduler;

import java.util.List;

import static com.greylabs.yoda.database.MetaData.*;

public class ResetYoda {
    public static void reset(Context context) {
        clearAlarms(context);
        clearPrefs(context);
        clearDatabase(context);
    }

    private static void clearPrefs(Context context) {
        Prefs prefs = Prefs.getInstance(context);
        prefs.clear();
    }

    private static void clearDatabase(Context context) {
        Database database = Database.getInstance(context);
        database.getWritableDatabase().execSQL("DROP TRIGGER IF  EXISTS "+TableDay.triggerDeleteSlotOnDayDelete);
        database.getWritableDatabase().execSQL("DROP TRIGGER IF  EXISTS "+TableSlot.triggerUpdatePendingStepOnSlotDelete);

//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TableDay.day);
//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TableSlot.slot);
//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TableTimeBox.timeBox);
//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TableTimeBoxOn.timeBoxOn);
//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TableTimeBoxWhen.timeBoxWhen);
//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TableGoal.goal);
//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TablePendingStep.pendingStep);
//         database.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TableCompletedStep.completedStep);

        
        database.getWritableDatabase().execSQL("DELETE FROM " + TableDay.day);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableSlot.slot);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableTimeBox.timeBox);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableTimeBoxOn.timeBoxOn);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableTimeBoxWhen.timeBoxWhen);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableGoal.goal);
        database.getWritableDatabase().execSQL("DELETE FROM " + TablePendingStep.pendingStep);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableCompletedStep.completedStep);
    }

    private  static void clearAlarms(Context context){
        List<Goal> goals=new Goal(context).getAll(Goal.GoalDeleted.SHOW_NOT_DELETED);
        PendingStep pendingStep=new PendingStep(context);
        AlarmScheduler alarmScheduler=new AlarmScheduler(context);
        for(Goal goal:goals){
            List<Integer> stepIds=pendingStep.getStepIds(goal.getId());
            if(stepIds!=null){
                for (Integer i:stepIds){
                    alarmScheduler.setStepId(i);
                    alarmScheduler.cancel();
                }
            }
        }
    }
}
