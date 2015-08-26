package com.greylabs.yoda.models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TablePendingStep;
import com.greylabs.yoda.database.MetaData.TableSlot;
import com.greylabs.yoda.scheduler.AlarmScheduler;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PendingStep implements Serializable {
    /**********************************************************************************************/
    // Instance variables
    /**
     * ******************************************************************************************
     */
    private long id;
    private String stringId="";//used for google task
    private String nickName;
    private int priority;
    private int time;
    private PendingStepType pendingStepType;
    private int stepCount;
    private int skipCount;
    private PendingStepStatus pendingStepStatus;
    private long goalId;
    private Date stepDate;
    private String goalStringId="";//used for google task;
    private long slotId;
    private long subStepOf;
    private DateTime updated=new DateTime(new Date());//last updated date
    private boolean deleted;//true if deleted
    transient private Database database;
    transient private Context context;
    transient private AlarmScheduler alarmScheduler;

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

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
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

    public String getGoalStringId() {
        return goalStringId;
    }

    public void setGoalStringId(String goalStringId) {
        this.goalStringId = goalStringId;
    }

    public Date getStepDate() {
        return stepDate;
    }

    public void setStepDate(Date stepDate) {
        this.stepDate = stepDate;
    }

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }


    public void initDatabase(Context context) {
        this.context=context;
        this.database = Database.getInstance(context);
    }


    public DateTime getUpdated() {
        return updated;
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
                this.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                this.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                this.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                this.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                this.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                this.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                this.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
            } while (c.moveToNext());
        }
        c.close();
        return this;
    }

    public List<PendingStep> getAll(PendingStepStatus status,PendingStepDeleted deleted) {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where "+TablePendingStep.status+"="+status.ordinal()+" " +
                " " + " and "+deleted.getCriteria() ;

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }


    public List<PendingStep> getAll(PendingStepStatus status,PendingStepDeleted deleted,long goalId) {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
//        String query = "select * " +
//                " " + " from " + TablePendingStep.pendingStep + " " +
//                " " + " where "+ TablePendingStep.status+"="+status.ordinal()+" " +
//                " " + " and ("+TablePendingStep.type+"="+PendingStepType.SINGLE_STEP.ordinal()+" or " +
//                " " + " "+TablePendingStep.type+"!="+PendingStepType.SPLIT_STEP.ordinal()+" ) " +
//                " " + " and "+TablePendingStep.status+" = "+status.ordinal()+" "+
//                " " + " and "+TablePendingStep.goalId+" = "+goalId+" " +
//                " " + " and "+deleted.getCriteria()+" " +
//                " " + " order by "+TablePendingStep.priority+" asc , "+TablePendingStep.nickName+" asc ";

        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where "+ TablePendingStep.status+"="+status.ordinal()+" " +
                " " + " and "+TablePendingStep.type+"!="+PendingStepType.SUB_STEP.ordinal()+"" +
                " " + " and "+TablePendingStep.status+" = "+status.ordinal()+" "+
                " " + " and "+TablePendingStep.goalId+" = "+goalId+" " +
                " " + " and "+deleted.getCriteria()+" " +
                " " + " order by "+TablePendingStep.priority+" asc , "+TablePendingStep.nickName+" asc ";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }


    public List<PendingStep> getAll(PendingStepStatus status) {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where "+ TablePendingStep.status+"="+status.ordinal()+" " +
                " " + " and ("+TablePendingStep.type+"!="+PendingStepType.SERIES_STEP.ordinal()+" or " +
                " " + " "+TablePendingStep.type+"!="+PendingStepType.SPLIT_STEP.ordinal()+" ) " ;

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }


    /**
     * This method return list of PendingStep of given goal and that are not substeps of some other
     * steps
     * @param goalId
     * @return List of Pending Steps
     */
    public List<PendingStep> getAll(long goalId) {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where " + TablePendingStep.goalId + " = " + goalId + " " +
                " " + " and " + TablePendingStep.type + "!=" + PendingStepType.SUB_STEP.ordinal()+" " +
                " " + " and "+TablePendingStep.deleted+"=0"+
                " " + " order by "+TablePendingStep.priority+" asc ";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
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
                " " + " and " + TablePendingStep.subStepOf + "=" + pendingStepId+" " +
                " " + " and "+TablePendingStep.type+"="+PendingStepType.SUB_STEP.ordinal()+" ";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }


    public List<PendingStep> getAllSubSteps(PendingStepStatus status ,PendingStepDeleted deleted,long pendingStepId, long goalId) {
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * " +
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where " + TablePendingStep.goalId + " = " + goalId + " " +
                " " + " and " + TablePendingStep.subStepOf + "=" + pendingStepId+" " +
                " " + " and " + TablePendingStep.status + "=" + status.ordinal()+" " +
                " " + " and "+TablePendingStep.type+"="+PendingStepType.SUB_STEP.ordinal()+" " +
                " " + " and "+deleted.getCriteria();

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }



    public long save() {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TablePendingStep.stringId,this.stringId);
        values.put(TablePendingStep.nickName, this.nickName);
        values.put(TablePendingStep.priority, this.priority);
        values.put(TablePendingStep.time, this.time);
        values.put(TablePendingStep.type, this.pendingStepType.ordinal());
        values.put(TablePendingStep.stepCount, this.stepCount);
        values.put(TablePendingStep.skipCount, this.skipCount);
        values.put(TablePendingStep.status, this.pendingStepStatus.ordinal());
        values.put(TablePendingStep.goalId, this.goalId);
        values.put(TablePendingStep.goalStringId,this.goalStringId);
        if(this.getUpdated()!=null)
            values.put(TablePendingStep.updated,CalendarUtils.getRFCTimestampToString(this.getUpdated()));
        values.put(TablePendingStep.deleted,(this.deleted)?1:0);
        if(this.getStepDate()!=null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.getStepDate());
            values.put(TablePendingStep.stepDate, CalendarUtils.getSqLiteDateFormat(cal));
        }
        values.put(TablePendingStep.slotId, this.slotId);
        values.put(TablePendingStep.subStepOf, this.subStepOf);
        long rowId;
        if (this.id != 0) {
            values.put(TablePendingStep.id, this.id);
        }
        rowId = db.insertWithOnConflict(TablePendingStep.pendingStep, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.id = rowId;
        return rowId;
    }

    public long saveSubStep(PendingStep pendingStep){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        pendingStep.setId(0);
        values.put(TablePendingStep.stringId, pendingStep.stringId);
        values.put(TablePendingStep.nickName, pendingStep.nickName);
        values.put(TablePendingStep.priority, pendingStep.priority);
        values.put(TablePendingStep.time, pendingStep.time);
        values.put(TablePendingStep.type, pendingStep.pendingStepType.ordinal());
        values.put(TablePendingStep.stepCount, pendingStep.stepCount);
        values.put(TablePendingStep.skipCount, pendingStep.skipCount);
        values.put(TablePendingStep.status, pendingStep.pendingStepStatus.ordinal());
        values.put(TablePendingStep.goalId, pendingStep.goalId);
        values.put(TablePendingStep.goalStringId,pendingStep.goalStringId);
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.SECOND,0);cal.set(Calendar.MINUTE, 0);cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.setTime(pendingStep.getStepDate());
        values.put(TablePendingStep.stepDate, CalendarUtils.getSqLiteDateFormat(cal));
        values.put(TablePendingStep.slotId, pendingStep.slotId);
        values.put(TablePendingStep.subStepOf, pendingStep.subStepOf);
        values.put(TablePendingStep.updated,CalendarUtils.getRFCTimestampToString(pendingStep.getUpdated()));
        values.put(TablePendingStep.deleted,(pendingStep.deleted)?1:0);

        long rowId;
        rowId=db.insert(TablePendingStep.pendingStep, null, values);
        return rowId;
    }
    public int delete() {
        SQLiteDatabase db = database.getWritableDatabase();
        int numOfRowAffected = db.delete(TablePendingStep.pendingStep, TablePendingStep.id + "=" + id, null);
        return numOfRowAffected;
    }
    public int deleteAllPendingSteps() {
        SQLiteDatabase db = database.getWritableDatabase();
        int numOfRowAffected = db.delete(TablePendingStep.pendingStep, TablePendingStep.id + "=" + goalId, null);
        return numOfRowAffected;
    }

    public int deleteSubSteps() {
        SQLiteDatabase db = database.getWritableDatabase();
        int numOfRowAffected = db.delete(TablePendingStep.pendingStep, TablePendingStep.subStepOf + "=" + id, null);
        return numOfRowAffected;
    }

    public void markSubSteps(boolean deleted) {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "update  " + TablePendingStep.pendingStep + " " +
                " " + "set " + TablePendingStep.deleted + "=" + (deleted ? 1 : 0) + " " +
                " " + " where " + TablePendingStep.goalId + " = " + this.goalId + " " +
                " " + " and " + TablePendingStep.subStepOf + "=" + this.id + " " +
                " " + " and " + TablePendingStep.type + "=" + PendingStepType.SUB_STEP.ordinal();

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }
    /**********************************************************************************************/
    //Utility Methods
    /********************************************************************************************/

    public long getIdIfExists(String stringId) {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select  "+TablePendingStep.id+","+TablePendingStep.stringId+" "+
                " " + " from " + TablePendingStep.pendingStep + " " +
                " " + " where " + TablePendingStep.stringId + " = '" + stringId + "' ";
        Cursor c = db.rawQuery(query, null);
        long id=0;
        if (c.moveToFirst()) {
            id=c.getLong(c.getColumnIndex(TablePendingStep.id));
         }
        return id;
    }

    public List<PendingStep> getAll(String filterCriteria){
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * "+
                " " + " from " + TablePendingStep.pendingStep +
                " " + " where ("  + TablePendingStep.type + "=" + PendingStepType.SUB_STEP.ordinal()+" " +
                " " + " or "+TablePendingStep.type+"="+ PendingStepType.SINGLE_STEP.ordinal()+" ) " +
                " " + "  and "+TablePendingStep.status+" = "+PendingStepStatus.TODO.ordinal()+" " +
                " " + "  and "+TablePendingStep.deleted+"=0 "+
                " "+filterCriteria+" " +
                " "+" order by "+TablePendingStep.priority+" asc";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
                pendingSteps.add(pendingStep);
            } while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }

    public List<PendingStep> getPendingSteps(long slotId){
        ArrayList<PendingStep> pendingSteps = null;
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "select * "+
                " " + " from " + TablePendingStep.pendingStep +" " +
                " " + " where "+TablePendingStep.slotId+" = "+slotId+" " +
                " " + " and ( "+TablePendingStep.status+"="+PendingStepStatus.DOING.ordinal()+" or " +
                " " + " "+TablePendingStep.status+"="+PendingStepStatus.TODO.ordinal()+" )";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            pendingSteps = new ArrayList<>();
            do {
                PendingStep pendingStep = new PendingStep(context);
                pendingStep.id = c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.stringId=c.getString(c.getColumnIndex(TablePendingStep.stringId));
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
                pendingStep.goalStringId=c.getString(c.getColumnIndex(TablePendingStep.goalStringId));
                pendingStep.stepDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TablePendingStep.stepDate)));
                pendingStep.slotId = c.getLong(c.getColumnIndex(TablePendingStep.slotId));
                pendingStep.subStepOf = c.getLong(c.getColumnIndex(TablePendingStep.subStepOf));
                pendingStep.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TablePendingStep.updated)));
                pendingStep.deleted=(c.getInt(c.getColumnIndex(TablePendingStep.deleted))==1)?true:false;
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
                " " + "where " +TablePendingStep.goalId + "=" + id+" " +
                " " + " and "+TablePendingStep.type+" != "+PendingStepType.SUB_STEP.ordinal();
        Cursor c = db.rawQuery(pendingStepCountQuery, null);
        if (c.moveToFirst()) {
            do {
                pendingStepCount = c.getInt(c.getColumnIndex("stepCount"));
            } while (c.moveToNext());
        }
        c.close();
        return pendingStepCount;
    }

    public long createSubSteps(int start ,int numberOfSteps,int time) {
        PendingStep pendingStepNew = new PendingStep(context);
        long rowId=0;
        for (int i = start; i <= numberOfSteps; i++) {
            pendingStepNew.setId(0);
            pendingStepNew.setGoalStringId("");
            pendingStepNew.setNickName(this.getNickName()+" Session "+i);
            pendingStepNew.setPriority(this.getPriority());
            pendingStepNew.setPendingStepType(PendingStepType.SUB_STEP);
            pendingStepNew.setStepCount(1);
            pendingStepNew.setSkipCount(0);
            pendingStepNew.setPendingStepStatus(PendingStepStatus.TODO);
            pendingStepNew.setGoalId(this.getGoalId());
            pendingStepNew.setGoalStringId(this.getGoalStringId());
            pendingStepNew.setTime(time);
            pendingStepNew.setStepDate(new Date());
            pendingStepNew.setSubStepOf(this.getId());
            pendingStepNew.setUpdated(new DateTime(new Date()));
            pendingStepNew.setDeleted(this.isDeleted());
            pendingStepNew.setGoalStringId(this.getGoalStringId());
            pendingStepNew.setStringId("");
            rowId+=pendingStepNew.saveSubStep(pendingStepNew);
        }
        return rowId;
    }

    public long updateSubSteps() {
        List<PendingStep> subSteps=this.getAllSubSteps(this.getId(),this.getGoalId());
        long rowId=0;
        if(subSteps!=null) {
            for (PendingStep subStep : subSteps) {
                subStep.setNickName(subStep.nickName);
                subStep.setPriority(this.getPriority());
                subStep.setPendingStepType(PendingStepType.SUB_STEP);
                subStep.setStepCount(1);
                subStep.setSkipCount(0);
                subStep.setPendingStepStatus(this.getPendingStepStatus());
                subStep.setGoalId(subStep.getGoalId());
                subStep.setTime(subStep.time);
                subStep.setSubStepOf(this.getId());
                subStep.setGoalStringId(this.getGoalStringId());
                subStep.setStringId(this.getStringId());
                rowId += subStep.save();
                if(this.getPendingStepStatus()==PendingStepStatus.COMPLETED){
                    subStep.cancelAlarm();
                }
            }
        }
        return rowId;
    }

    public boolean isSlotAssigned(long slotId){
        String query=" select count(*) as isPresent " +
                " "+"  from "+TablePendingStep.pendingStep+" "+
                " "+" where "+TablePendingStep.slotId+" = "+slotId;
        SQLiteDatabase db= database.getWritableDatabase();
        Cursor c=db.rawQuery(query, null);
        c.moveToFirst();
        int isPresent=c.getInt(c.getColumnIndex("isPresent"));
        c.close();
        return  isPresent>0;
    }

    public void updateGoalId(long oldGoalId,long newGoalId){
        String query="update "+TablePendingStep.pendingStep+" " +
                " "+" set "+TablePendingStep.goalId+" = "+newGoalId+" " +
                " "+" where "+TablePendingStep.goalId+" = "+oldGoalId;
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c=db.rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

    public void updateStringGoalId(long oldGoalId,String newStringGoalId){
        String query="update "+TablePendingStep.pendingStep+" " +
                " "+" set "+TablePendingStep.goalStringId+" = '"+newStringGoalId+"' " +
                " "+" where "+TablePendingStep.goalId+" = "+oldGoalId;
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c=db.rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

    public  String getColorCode(){
        Goal goal=new Goal(context).get(this.getGoalId());
        return goal.getColorCode();
    }

    public void cancelAlarm(){
        if(alarmScheduler==null)
            alarmScheduler=new AlarmScheduler(context);
        alarmScheduler.setStepId(this.getId());
        alarmScheduler.cancel();
    }

    public void cancelSubstepAlarms(){
        List<PendingStep> subSteps=this.getAllSubSteps(this.getId(),this.getGoalId());
        if(subSteps!=null) {
            if (alarmScheduler == null)
                alarmScheduler = new AlarmScheduler(context);
           for (PendingStep substep:subSteps) {
               alarmScheduler.setStepId(substep.getId());
               alarmScheduler.cancel();
           }
        }
    }



    public List<Integer> getStepIds(long goalId){
        List<Integer> stepIds=null;
        String query = " select "+TablePendingStep.id +
                " " + "from " + TablePendingStep.pendingStep + " " +
                " " + "where " +TablePendingStep.goalId + "=" + id+" " +
                " " + " and "+TablePendingStep.type+" != "+PendingStepType.SERIES_STEP.ordinal()+
                " " + " and "+TablePendingStep.type+" != "+PendingStepType.SPLIT_STEP.ordinal();
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c=db.rawQuery(query, null);
        if(c.moveToFirst()){
            stepIds=new ArrayList<>();
            do{
                int id=c.getInt(c.getColumnIndex(TablePendingStep.id));
                stepIds.add(id);
            }while (c.moveToNext());
        }
        return stepIds;
    }

    public boolean isNowStep(){
        Calendar  cal =Calendar.getInstance();
        cal.set(Calendar.SECOND,0);cal.set(Calendar.MILLISECOND,0);
        String currentDate= CalendarUtils.getSqLiteDateFormat(cal);

        Calendar calStart=Calendar.getInstance();
        calStart.setTime(this.getStepDate());
        String startDate=CalendarUtils.getSqLiteDateFormat(calStart);
        calStart.add(Calendar.HOUR_OF_DAY,this.getTime());
        calStart.add(Calendar.MINUTE,-1);
        String endDate=CalendarUtils.getSqLiteDateFormat(calStart);
        String query = " select "+TablePendingStep.id +
                " " + "from " + TablePendingStep.pendingStep + " " +
                " " + "where " +TablePendingStep.goalId + "=" + goalId+" " +
                " " + " and "+TablePendingStep.id+" = "+id+
                " " + " and ("+TablePendingStep.type+" != "+PendingStepType.SERIES_STEP.ordinal()+
                " " + " or "+TablePendingStep.type+" != "+PendingStepType.SPLIT_STEP.ordinal()+") " +
                " " + " and ( "+TablePendingStep.status+" = "+PendingStepStatus.TODO.ordinal()+" "+
                " " + " or "+TablePendingStep.status+" = "+PendingStepStatus.DOING.ordinal()+" ) "+
                " " + "  and ( '"+startDate+"' <= '"+currentDate+"' and " +
                "  '"+currentDate+"' <= '"+endDate+"')";

        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c=db.rawQuery(query, null);
        long id=0;
        if (c.moveToFirst())
            id =c.getLong(c.getColumnIndex(TablePendingStep.id));
        c.close();
        return id>0;
    }

    public void freeSlot(){
        String query=" update "+TableSlot.slot+" " +
                " set "+TableSlot.time+" = "+Constants.MAX_SLOT_DURATION+" " +
                " where "+TableSlot.id+" = "+this.slotId;
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        c.close();
    }

    public void freeSlots(){
        List<PendingStep> subSteps=getAllSubSteps(this.getId(),this.getGoalId());
        if(subSteps!=null) {
            for (PendingStep subStep : subSteps) {
                subStep.freeSlot();
                subStep.cancelAlarm();
                subStep.setSlotId(0);
                subStep.save();
            }
        }
    }
    public void freeAllSlots(long goalId){
        String query=" update "+TablePendingStep.pendingStep+" " +
                " set "+TablePendingStep.slotId+" = 0"+" " +
                " where "+TablePendingStep.goalId+" = "+goalId;
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        c.close();
    }

    public int getAllStepCount(long goalId){//substeps and single step count
        int timeSum=0;
        String query="select count(*) as stepCount "+
                " " +" from "+TablePendingStep.pendingStep+" " +
                " " +" where "+TablePendingStep.goalId+"="+goalId+" " +
                " " +" and "+TablePendingStep.deleted+"=0 " +
                " " +" and ("+TablePendingStep.type+"="+PendingStepType.SUB_STEP.ordinal()+" " +
                " " +" or "+TablePendingStep.type+"="+PendingStepType.SINGLE_STEP.ordinal()+" )" +
                " " +" and "+TablePendingStep.status+"!="+PendingStepStatus.COMPLETED.ordinal();
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        timeSum=c.getShort(c.getColumnIndex("stepCount"));
        c.close();
        return timeSum;
    }

    /**********************************************************************************************/
    // Enum Constants

    /**********************************************************************************************/
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

    public enum PendingStepDeleted{
        SHOW_DELETED("("+TablePendingStep.deleted+"= 1 )"),
        SHOW_NOT_DELETED("("+TablePendingStep.deleted+"= 0 )"),
        SHOW_BOTH("("+TablePendingStep.deleted+"= 1"+" or "+TablePendingStep.deleted+"= 0 )");
        String criteria;
        PendingStepDeleted(String criteria){
            this.criteria=criteria;
        }

        public String getCriteria(){
            return criteria;
        }
    }
}
