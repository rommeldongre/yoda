package com.greylabs.yoda.utils;

import android.content.Context;
import com.greylabs.yoda.database.Database;
import static com.greylabs.yoda.database.MetaData.*;

public class ResetYoda {
    public static void reset(Context context) {
        clearPrefs(context);
        clearDatabase(context);
    }

    private static void clearPrefs(Context context) {
        Prefs prefs = Prefs.getInstance(context);
        prefs.clear();
    }

    private static void clearDatabase(Context context) {
        Database database = Database.getInstance(context);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableCompletedStep.completedStep);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableGoal.goal);
        database.getWritableDatabase().execSQL("DELETE FROM " + TablePendingStep.pendingStep);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableTimeBox.timeBox);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableTimeBoxOn.timeBoxOn);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableTimeBoxWhen.timeBoxWhen);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableDay.day);
        database.getWritableDatabase().execSQL("DELETE FROM " + TableSlot.slot);
    }
}
