package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableSlot;
import com.greylabs.yoda.database.MetaData.TablePendingStep;
import com.greylabs.yoda.database.MetaData.TableDay;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.WhereConditionBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 7/15/2015.
 */
public class Slot {
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private com.greylabs.yoda.enums.TimeBoxWhen when;
    private int time;
    private long goalId;
    private long timeBoxId;
    private Date scheduleDate;
    private long dayId;
    private Database database;
    private Context context;
    /**********************************************************************************************/
    // Getters and Setters
    /**********************************************************************************************/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }

    public long getTimeBoxId() {
        return timeBoxId;
    }

    public void setTimeBoxId(long timeBoxId) {
        this.timeBoxId = timeBoxId;
    }

    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }

    public com.greylabs.yoda.enums.TimeBoxWhen getWhen() {
        return when;
    }

    public void setWhen(com.greylabs.yoda.enums.TimeBoxWhen when) {
        this.when = when;
    }
    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public Slot(Context context){
        this.context=context;
        this.database=Database.getInstance(context);
    }
    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", when=" + when +
                ", time=" + time +
                ", goalId=" + goalId +
                ", timeBoxId=" + timeBoxId +
                ", scheduleDate=" + scheduleDate +
                ", dayId=" + dayId +
                '}';
    }
    /**
     * This method retrieves all the slot of passed timebox.Internally it build where condition to
     * filter records that fit in timebox
     * @param timeBox
     * @return None
     */
    public List<Slot> getAll(TimeBox timeBox){
        List<Slot> slots=null;
        String cols=" s."+TableSlot.id+" as slotId ,"+TableSlot.time+","+TableSlot.goalId+"," +
                " "+TableSlot.timeBoxId+","+TableSlot.dayId+" ";
        String query="select "+cols+" from " +
                " "+TableDay.day+" as d  join "+TableSlot.slot+" as s " +
                " "+" on ( d."+TableDay.id+" = "+" s."+TableSlot.dayId+" " +
                " "+ WhereConditionBuilder.buildWhereCondition(timeBox);
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            slots=new ArrayList<>();
            do{
                Slot slot=new Slot(context);
                slot.setId(c.getLong(c.getColumnIndex("slotId")));
                slot.setTime(c.getInt(c.getColumnIndex(TableSlot.time)));
                slot.setGoalId(c.getLong(c.getColumnIndex(TableSlot.goalId)));
                slot.setTimeBoxId(c.getLong(c.getColumnIndex(TableSlot.timeBoxId)));
                slot.setDayId(c.getLong(c.getColumnIndex(TableSlot.dayId)));
                slots.add(slot);
            }while (c.moveToNext());
        }
        return slots;
    }


    /**
     * This method returns the all slots of corresponds to dayId.
     * @return list of slots having day ID =dayId
     */
    public List<Slot> getAll(){
        List<Slot> slots = null;
        String query=" select * from "+TableSlot.slot+" " +
                " "+"where "+TableSlot.dayId+" = "+dayId;

        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            slots=new ArrayList<>();
            do{
                Slot slot=new Slot(context);
                slot.setId(c.getInt(c.getColumnIndex(TableSlot.id)));
                slot.setWhen(com.greylabs.yoda.enums.TimeBoxWhen.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableSlot.when))));
                slot.setScheduleDate(CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableSlot.scheduleDate))));
                slot.setTime(c.getInt(c.getColumnIndex(TableSlot.time)));
                slot.setGoalId(c.getInt(c.getColumnIndex(TableSlot.goalId)));
                slot.setTimeBoxId(c.getInt(c.getColumnIndex(TableSlot.timeBoxId)));
                slot.setDayId(c.getInt(c.getColumnIndex(TableSlot.dayId)));
                slots.add(slot);
            }while(c.moveToNext());
        }
        return slots;
    }

    /**
     * This methods save(update) the slot with specified id and dayId
     * @return returns id of the the record.
     */
    public long save(){
        SQLiteDatabase db=database.getWritableDatabase();
        ContentValues cv=new ContentValues();
        if(id!=0)
            cv.put(TableSlot.id,id);
        cv.put(TableSlot.when,when.getValue());
        cv.put(TableSlot.time,time);
        cv.put(TableSlot.goalId,goalId);
        cv.put(TableSlot.timeBoxId,timeBoxId);
        cv.put(TableSlot.dayId,dayId);
        long rowId=db.insertWithOnConflict(TableSlot.slot, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }

    public List<Slot> getNextSlots(){
        List<Slot> slots=null;
        String cols=" s."+TableSlot.id+","+TableSlot.when+","+TableSlot.time+","+TableSlot.goalId+"." +
                " "+TableSlot.timeBoxId+","+TableSlot.dayId;
        String query="select "+cols+" from " +
                " "+TablePendingStep.pendingStep+" as p join "+TableSlot.slot+" as s join " +TableDay.day+" as d "+
                " "+" on ( p."+TablePendingStep.slotId+" = "+" s."+TableSlot.id+" and " + " s."+TableSlot.dayId+" = "+TableDay.id+" ) "+
                " "+" where "+TableSlot.timeBoxId+" = "+timeBoxId;
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            slots=new ArrayList<>();
            do{
                Slot slot=new Slot(context);
                slot.setId(c.getInt(c.getColumnIndex(TableSlot.id)));
                slot.setWhen(com.greylabs.yoda.enums.TimeBoxWhen.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableSlot.when))));
                slot.setScheduleDate(CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableDay.date))));
                slot.setTime(c.getInt(c.getColumnIndex(TableSlot.time)));
                slot.setGoalId(c.getInt(c.getColumnIndex(TableSlot.goalId)));
                slot.setTimeBoxId(c.getInt(c.getColumnIndex(TableSlot.timeBoxId)));
                slot.setDayId(c.getInt(c.getColumnIndex(TableSlot.dayId)));
                slots.add(slot);
            }while(c.moveToNext());
        }
        return slots;
    }

}
