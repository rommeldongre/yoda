package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TableSlot;
import com.greylabs.yoda.database.MetaData.TablePendingStep;
import com.greylabs.yoda.database.MetaData.TableDay;
import com.greylabs.yoda.enums.*;
import com.greylabs.yoda.enums.TimeBoxWhen;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Prefs;
import com.greylabs.yoda.utils.WhereConditionBuilder;

import java.util.ArrayList;
import java.util.Calendar;
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
        String cols=" s."+TableSlot.id+" as slotId ,"+TableSlot.when+","+TableSlot.time+","+TableSlot.scheduleDate+","+TableSlot.goalId+"," +
                " "+TableSlot.timeBoxId+","+TableSlot.dayId+", "+TableDay.dayOfYear+","+TableDay.dayOfWeek+"," +
                ""+TableDay.weekOfMonth+","+TableDay.monthOfYear+","+TableDay.quarterOfYear+","+TableDay.year+" ";
        String query="select "+cols+" from " +
                " "+TableDay.day+" as d  join "+TableSlot.slot+" as s " +
                " "+" on ( d."+TableDay.id+" = "+" s."+TableSlot.dayId+" )" +
                " "+ WhereConditionBuilder.buildWhereCondition(timeBox);
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            slots=new ArrayList<>();
            do{
                Slot slot=new Slot(context);
                slot.setId(c.getLong(c.getColumnIndex("slotId")));
                slot.setTime(c.getInt(c.getColumnIndex(TableSlot.time)));
                slot.setWhen(com.greylabs.yoda.enums.TimeBoxWhen.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableSlot.when))));
                slot.setScheduleDate(CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableSlot.scheduleDate))));
                slot.setGoalId(c.getLong(c.getColumnIndex(TableSlot.goalId)));
                slot.setTimeBoxId(c.getLong(c.getColumnIndex(TableSlot.timeBoxId)));
                slot.setDayId(c.getLong(c.getColumnIndex(TableSlot.dayId)));
                slots.add(slot);
            }while (c.moveToNext());
        }
        return slots;
    }


    /**
     * This method returns the all slots that corresponds to timeBoxId
     * @return list of slots having day ID =dayId
     */
    public List<Slot> getAll(long timeBoxId){
        List<Slot> slots = null;
        String query=" select * from "+TableSlot.slot+" " +
                " "+"where "+TableSlot.timeBoxId+" = "+timeBoxId+" " +
                " "+" order by datetime( "+TableSlot.scheduleDate+" ) asc ";

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
                //slot.setTimeBoxId(Constants.MAX_SLOT_DURATION);
                slots.add(slot);
            }while(c.moveToNext());
        }
        return slots;
    }


    public Slot get(long slotId){
        Slot slot=null;
        String query=" select * from "+TableSlot.slot+" " +
                " "+"where "+TableSlot.id+" = "+slotId+" " ;

        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
             slot=new Slot(context);
            do{
                slot.setId(c.getInt(c.getColumnIndex(TableSlot.id)));
                slot.setWhen(com.greylabs.yoda.enums.TimeBoxWhen.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableSlot.when))));
                slot.setScheduleDate(CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableSlot.scheduleDate))));
                slot.setTime(c.getInt(c.getColumnIndex(TableSlot.time)));
                slot.setGoalId(c.getInt(c.getColumnIndex(TableSlot.goalId)));
                slot.setTimeBoxId(c.getInt(c.getColumnIndex(TableSlot.timeBoxId)));
                slot.setDayId(c.getInt(c.getColumnIndex(TableSlot.dayId)));
            }while(c.moveToNext());
        }
        return slot;
    }


    /**
     * This method returns the all slots of corresponds to dayId.
     * @return list of slots having day ID =dayId
     */
    public List<Slot> getAll(){
        List<Slot> slots = null;
        String query=" select * from "+TableSlot.slot+" " +
                " "+"where "+TableSlot.dayId+" = "+dayId+
                " "+" order by strftime('%Y-%m-%d %H:%M:%S',"+TableSlot.scheduleDate+") asc , " +
                " "+" "+TableSlot.when+" asc " ;

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
        cv.put(TableSlot.when, when.getValue());
        cv.put(TableSlot.time,time);
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTime(scheduleDate);
        String  sqliteDate= CalendarUtils.getSqLiteDateFormat(cal);
        cv.put(TableSlot.scheduleDate,sqliteDate);
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

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableSlot.slot, TableSlot.id + "=" + id, null);
        return numOfRowAffected;
    }

    public void setDefaultGoalDetails(){
        SQLiteDatabase db=database.getWritableDatabase();
        Prefs prefs=Prefs.getInstance(context);
        String query="update "+TableSlot.slot+" " +
              " "+" set  "+TableSlot.goalId+" = "+prefs.getStretchGoalId()+" , " +
              " "+" "+TableSlot.  timeBoxId+" = "+prefs.getUnplannedTimeBoxId()+" ";

        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        c.close();
    }

    public long getActiveSlotId(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.SECOND,0);cal.set(Calendar.MILLISECOND,0);
        TimeBoxWhen timeBoxWhen=TimeBoxWhen.getWhen(cal);
        String nowDate=CalendarUtils.getSqLiteDateFormat(cal);


        String query="select  "+TableSlot.id+" " +
                " " +" from "+TableSlot.slot+" " +
                " " +" where strftime('%Y-%m-%d',"+TableSlot.scheduleDate+") = strftime('%Y-%m-%d','"+nowDate+"')" +
                " " +" and "+TableSlot.when+" = "+TimeBoxWhen.getEnumToIntegerType(timeBoxWhen);

        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        long id=c.getLong(c.getColumnIndex(TableSlot.id));
        c.close();
        return id;
    }

    public int getSlotCount(long timeBoxId){
        int slotCount=0;
        String query="select count(*) as slotCount " +
                " " +" from "+TableSlot.slot+" " +
                " " +" where "+TableSlot.timeBoxId+"="+timeBoxId;
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        slotCount=c.getShort(c.getColumnIndex("slotCount"));
        c.close();
        return slotCount;
    }

    public int getPossibleSlotCount(TimeBox timeBox){
        int slotCount=0;
        String query="select count(*) as slotCount " +
                " "+"from "+TableDay.day+" as d  join "+TableSlot.slot+" as s " +
                " "+" on ( d."+TableDay.id+" = "+" s."+TableSlot.dayId+" )" +
                " "+ WhereConditionBuilder.buildWhereCondition(timeBox);
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        slotCount=c.getShort(c.getColumnIndex("slotCount"));
        c.close();
        return slotCount;
    }

}
