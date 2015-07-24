package com.greylabs.yoda.models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TablePendingStep;
import com.greylabs.yoda.database.MetaData.TableSlot;
import com.greylabs.yoda.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PendingStep implements Serializable {
    /**********************************************************************************************/
    // Instance variables
    /**
     * ******************************************************************************************
     */
    private long id;
    private String nickName;
    private int priority;
    private int time;
    private PendingStepType pendingStepType;
    private int stepCount;
    private int skipCount;
    private PendingStepStatus pendingStepStatus;
    private long goalId;
    private long slotId;
    private long subStepOf;
    transient private Database database;
    transient private Context context;

    /**********************************************************************************************/
    //Getters and Setters

    /**
     * ******************************************************************************************
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public PendingStepType getPendingStepType() {
        return pendingStepType;
    }

    public void setPendingStepType(PendingStepType pendingStepType) {
        this.pendingStepType = pendingStepType;
    }

    public PendingStepStatus getPendingStepStatus() {
        return pendingStepStatus;
    }

    public void setPendingStepStatus(PendingStepStatus pendingStepStatus) {
        this.pendingStepStatus = pendingStepStatus;
    }

    public long getSubStepOf() {
        return subStepOf;
    }

    public void setSubStepOf(long subStepOf) {
        this.subStepOf = subStepOf;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }


    public void initDatabase(Context context) {
        this.database = Database.getInstance(context);
    }

    /**********************************************************************************************/
    // Constructors

    /**
     * ******************************************************************************************
     */
    public PendingStep(Context context) {
        this.context = context;
        database = Database.getInstance(context);
    }


    /**********************************************************************************************/
    //Core Methods

    /**
     * ******************************************************************************************
     */


    public PendingStep get(long id) {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + "where " + TablePendingStep.id + " = " + id;

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                this.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                this.nickName = c.getString(c.getColumnIndex(TablePendingStep.nickName));
                this.priority = c.getInt(c.getColumnIndex(TablePendingStep.priority));
                this.time = c.getInt(c.getColumnIndex(TablePendingStep.time));
                this.pendingStepType = PendingStepType.getIntegerToEnumType(
                        c.getInt(c.getColumnIndex(TablePendingStep.type)));
                this.stepCount = c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                this.skipCount = c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                this.pendingStepStatus = PendingStepStatus.getPendingStepStatus(
                        c.getInt(c.getColumnIndex(TablePendingStep.status)));
                this.goalId = c.getLong(c.getColumnIndex(TablePendingStep.goalId));
                this.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                this.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
            } while (c.moveToNext());
        }
        c.close();
        return this;
    }

    public List<PendingStep> getAll() {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " ";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.nickName = c.getString(c.getColumnIndex(TablePendingStep.nickName));
                pendingStep.priority = c.getInt(c.getColumnIndex(TablePendingStep.priority));
                pendingStep.time = c.getInt(c.getColumnIndex(TablePendingStep.time));
                pendingStep.pendingStepType = PendingStepType.getIntegerToEnumType(
                        c.getInt(c.getColumnIndex(TablePendingStep.type)));
                pendingStep.stepCount = c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                pendingStep.skipCount = c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                pendingStep.pendingStepStatus = PendingStepStatus.getPendingStepStatus(
                        c.getInt(c.getColumnIndex(TablePendingStep.status)));
                pendingStep.goalId = c.getInt(c.getColumnIndex(TablePendingStep.goalId));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }

    /**
     * This method return list of PendingStep of given goal and that are not substeps of some other
     * steps
     *
     * @param goalId
     * @return List of Pending Steps
     */
    public List<PendingStep> getAll(long goalId) {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where " + TablePendingStep.goalId + " = " + goalId + " " +
                " " + " and " + TablePendingStep.type + "!=" + PendingStepType.SUB_STEP.ordinal();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.nickName = c.getString(c.getColumnIndex(TablePendingStep.nickName));
                pendingStep.priority = c.getInt(c.getColumnIndex(TablePendingStep.priority));
                pendingStep.time = c.getInt(c.getColumnIndex(TablePendingStep.time));
                pendingStep.pendingStepType = PendingStepType.getIntegerToEnumType(
                        c.getInt(c.getColumnIndex(TablePendingStep.type)));
                pendingStep.stepCount = c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                pendingStep.skipCount = c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                pendingStep.pendingStepStatus = PendingStepStatus.getPendingStepStatus(
                        c.getInt(c.getColumnIndex(TablePendingStep.status)));
                pendingStep.goalId = c.getInt(c.getColumnIndex(TablePendingStep.goalId));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }

    public List<PendingStep> getAllSubSteps(long pendingStepId, long goalId) {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where " + TablePendingStep.goalId + " = " + goalId + " " +
                " " + " and " + TablePendingStep.id + "=" + pendingStepId;

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.nickName = c.getString(c.getColumnIndex(TablePendingStep.nickName));
                pendingStep.priority = c.getInt(c.getColumnIndex(TablePendingStep.priority));
                pendingStep.time = c.getInt(c.getColumnIndex(TablePendingStep.time));
                pendingStep.pendingStepType = PendingStepType.getIntegerToEnumType(
                        c.getInt(c.getColumnIndex(TablePendingStep.type)));
                pendingStep.stepCount = c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                pendingStep.skipCount = c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                pendingStep.pendingStepStatus = PendingStepStatus.getPendingStepStatus(
                        c.getInt(c.getColumnIndex(TablePendingStep.status)));
                pendingStep.goalId = c.getInt(c.getColumnIndex(TablePendingStep.goalId));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }


    public long save() {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TablePendingStep.nickName, this.nickName);
        values.put(TablePendingStep.priority, this.priority);
        values.put(TablePendingStep.time, this.time);
        values.put(TablePendingStep.type, this.pendingStepType.ordinal());
        values.put(TablePendingStep.stepCount, this.stepCount);
        values.put(TablePendingStep.skipCount, this.skipCount);
        values.put(TablePendingStep.status, this.pendingStepStatus.ordinal());
        values.put(TablePendingStep.goalId, this.goalId);
        values.put(TablePendingStep.slotId, this.slotId);
        values.put(TablePendingStep.subStepOf, this.subStepOf);
        long rowId;
        if (this.id != 0) {
            values.put(TablePendingStep.id, this.id);
        }
        rowId = db.insertWithOnConflict(TablePendingStep.pendingStep, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.id = rowId;
        //only applicable for SplitStep and SeriesStep
        switch (pendingStepType){
            case SPLIT_STEP:
                if(this.getTime()>Constants.MAX_SLOT_DURATION){
                    float numberOfSteps=this.getTime()/Constants.MAX_SLOT_DURATION;
                    Float f=new Float(numberOfSteps);
                    createSubSteps(f.intValue(),Constants.MAX_SLOT_DURATION);
                    if(numberOfSteps-f.intValue()>0.0f)
                        createSubSteps(1,this.getTime()%Constants.MAX_SLOT_DURATION);
                }
                break;
            case SERIES_STEP:
                createSubSteps(this.getStepCount(),this.getTime());
                break;
        }

        return rowId;
    }

    public int delete() {
        SQLiteDatabase db = database.getWritableDatabase();
        int numOfRowAffected = db.delete(TablePendingStep.pendingStep, TablePendingStep.id + "=" + id, null);
        return numOfRowAffected;
    }

    public int deleteSubSteps(long pendingStepId) {
        SQLiteDatabase db = database.getWritableDatabase();
        int numOfRowAffected = db.delete(TablePendingStep.pendingStep, TablePendingStep.subStepOf + "=" + pendingStepId, null);
        return numOfRowAffected;
    }
    /**********************************************************************************************/
    //Utility Methods

    /********************************************************************************************/
    public List<PendingStep> getAll(String filterCriteria){
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String cols=" s."+TablePendingStep.id+" as stepId , "+TablePendingStep.nickName+", " +
                TablePendingStep.priority+", "+TablePendingStep.time+", " +
                TablePendingStep.type+", "+TablePendingStep.stepCount+","+
                TablePendingStep.status+","+TablePendingStep.skipCount+", p."+
                TablePendingStep.goalId+" as stepGoalId ,"+TablePendingStep.slotId+","+
                TablePendingStep.subStepOf+","+TableSlot.scheduleDate;
        String query = "select "+ cols +
                " " + " from " + TablePendingStep.pendingStep + " as p join " + TableSlot.slot+" as s " +
                " " + " on ( p." +TablePendingStep.slotId+" = s."+TableSlot.id+" ) "+
                " " + " where "  + TablePendingStep.type + "!=" + PendingStepType.SUB_STEP.ordinal()+" " +
                " "+filterCriteria;
//        p." + TablePendingStep.goalId + " = " + goalId + " " +
//        " " + " and

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex("stepId"));
                pendingStep.nickName = c.getString(c.getColumnIndex(TablePendingStep.nickName));
                pendingStep.priority = c.getInt(c.getColumnIndex(TablePendingStep.priority));
                pendingStep.time = c.getInt(c.getColumnIndex(TablePendingStep.time));
                pendingStep.pendingStepType = PendingStepType.getIntegerToEnumType(
                        c.getInt(c.getColumnIndex(TablePendingStep.type)));
                pendingStep.stepCount = c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                pendingStep.skipCount = c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                pendingStep.pendingStepStatus = PendingStepStatus.getPendingStepStatus(
                        c.getInt(c.getColumnIndex(TablePendingStep.status)));
                pendingStep.goalId = c.getInt(c.getColumnIndex("stepGoalId"));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }

    public long getPendingStepCount(long goalId) {
        long pendingStepCount = 0;
        SQLiteDatabase db = database.getReadableDatabase();
        //this query returns sum of time of all steps that are present in the Complpeted Step table
        String pendingStepCountQuery = " select count(*) as stepCount " +
                " " + "from " + TablePendingStep.pendingStep + " " +
                " " + "where " +TablePendingStep.goalId + "=" + id;
        Cursor c = db.rawQuery(pendingStepCountQuery, null);
        if (c.moveToFirst()) {
            do {
                pendingStepCount = c.getInt(c.getColumnIndex("stepCount"));
            } while (c.moveToNext());
        }
        c.close();
        return pendingStepCount;
    }

    private long createSubSteps(int numberOfSteps,int time) {
        PendingStep pendingStepNew = new PendingStep(context);
        long rowId=0;
        for (int i = 1; i <= numberOfSteps; i++) {
            pendingStepNew.setId(0);
            pendingStepNew.setNickName("Part 1 of " + this.getNickName());
            pendingStepNew.setPriority(this.getPriority());
            pendingStepNew.setPendingStepType(PendingStepType.SUB_STEP);
            pendingStepNew.setStepCount(1);
            pendingStepNew.setSkipCount(0);
            pendingStepNew.setPendingStepStatus(PendingStepStatus.TODO);
            pendingStepNew.setGoalId(this.getGoalId());
            pendingStepNew.setTime(time);
            pendingStepNew.setSubStepOf(this.getId());
            rowId+=pendingStepNew.save();
        }
        return rowId;
    }
    /**********************************************************************************************/
    // Enum Constants

    /**
     * ******************************************************************************************
     */
    public enum PendingStepType {
        SPLIT_STEP, SERIES_STEP, SINGLE_STEP, SUB_STEP;

        public static PendingStepType getIntegerToEnumType(int type) {
            switch (type) {
                case 0:
                    return SPLIT_STEP;
                case 1:
                    return SERIES_STEP;
                case 2:
                    return SINGLE_STEP;
                case 3:
                    return SUB_STEP;
            }
            return SINGLE_STEP;
        }
    }

    public enum PendingStepStatus {
        TODO, DOING, MISSED, COMPLETED;

        public static PendingStepStatus getPendingStepStatus(int status) {
            switch (status) {
                case 0:
                    return TODO;
                case 1:
                    return DOING;
                case 2:
                    return MISSED;
                case 3:
                    return COMPLETED;
            }
            return TODO;
        }
    }
}
