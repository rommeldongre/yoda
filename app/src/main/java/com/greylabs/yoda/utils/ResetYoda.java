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
        database.getWritableDatabase().execSQL("DROP TABLE " + TableCompletedStep.completedStep);
        database.getWritableDatabase().execSQL("DROP TABLE " + TableGoal.goal);
        database.getWritableDatabase().execSQL("DROP TABLE " + TablePendingStep.pendingStep);
        database.getWritableDatabase().execSQL("DROP TABLE " + TableTimeBox.timeBox);
        database.getWritableDatabase().execSQL("DROP TABLE " + TableTimeBoxOn.timeBoxOn);
        database.getWritableDatabase().execSQL("DROP TABLE " + TableTimeBoxWhen.timeBoxWhen);
        database.getWritableDatabase().execSQL("DROP TABLE " + TableDay.day);
        database.getWritableDatabase().execSQL("DROP TABLE " + TableSlot.slot);
    }
}
