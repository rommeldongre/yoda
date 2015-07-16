package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.TableSlot;
import java.util.ArrayList;
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

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
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
        cv.put(TableSlot.slot,when.getValue());
        cv.put(TableSlot.time,time);
        cv.put(TableSlot.goalId,goalId);
        cv.put(TableSlot.timeBoxId,timeBoxId);
        cv.put(TableSlot.dayId,dayId);
        long rowId=db.insertWithOnConflict(TableSlot.slot,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }
}
