package com.greylabs.yoda.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.greylabs.yoda.models.TimeBox;
import com.greylabs.yoda.utils.Logger;

import java.io.Serializable;

import static com.greylabs.yoda.database.MetaData.*;

public class Database extends SQLiteOpenHelper implements Serializable{
    /**********************************************************************************************/
    //Database Details
    /**********************************************************************************************/
    private static final String TAG="Database";
    private static final String DATABASE_NAME="yoda";
    private static final int DATABASE_VERSION=1;

    /**********************************************************************************************/
    //Instance and class variables
    /**********************************************************************************************/
    private static Database database;


    /**********************************************************************************************/
    //Database Init
    /**********************************************************************************************/
    private Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static Database getInstance(Context context){
        if(database==null)
            database=new Database(context);
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TableDay.createDayTable);
        sqLiteDatabase.execSQL(TableSlot.createSlotTable);
        sqLiteDatabase.execSQL(TableTimeBox.createTimeBoxTable);
        sqLiteDatabase.execSQL(TableTimeBoxOn.createTimeBoxOnTable);
        sqLiteDatabase.execSQL(TableTimeBoxWhen.createTimeBoxWhenTable);
        sqLiteDatabase.execSQL(TableGoal.createGoalTable);
        //sqLiteDatabase.execSQL(TableGoal.createTrigger);
        sqLiteDatabase.execSQL(TablePendingStep.createPendingStepTable);
        sqLiteDatabase.execSQL(TableCompletedStep.createCompletedStepTable);
        Logger.log(TAG, "All tables created successfully");

        //populdate default data

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.delete(DATABASE_NAME, null, null);
        onCreate(sqLiteDatabase);
    }
}
