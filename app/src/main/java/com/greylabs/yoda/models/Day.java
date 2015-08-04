package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData.*;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.WhereConditionBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaybhay Vijay on 7/15/2015.
 */
public class Day {
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private int dayOfYear;
    private int dayOfWeek;
    private Date date;
    private int weekOfMonth;
    private int monthOfYear;
    private int quarterOfYear;
    private int year;
    private List<Slot> slots;
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
    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(int weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }
    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }
    public int getQuarterOfYear() {
        return quarterOfYear;
    }
    public void setQuarterOfYear(int quarterOfYear) {
        this.quarterOfYear = quarterOfYear;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }
    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public Day(Context context){
        this.context=context;
        this.database=Database.getInstance(context);
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", dayOfYear=" + dayOfYear +
                ", dayOfWeek=" + dayOfWeek +
                ", date=" + date +
                ", weekOfMonth=" + weekOfMonth +
                ", monthOfYear=" + monthOfYear +
                ", quarterOfYear=" + quarterOfYear +
                ", year=" + year +
                '}';
    }

    /**
     * This method save(updates) or insert day.
     * @return None
     */
    public long save(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.setTime(this.date);
        String sqliteDate=CalendarUtils.getSqLiteDateFormat(cal);
        SQLiteDatabase db=database.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TableDay.date,sqliteDate);
        cv.put(TableDay.dayOfYear,this.dayOfYear);
        cv.put(TableDay.dayOfWeek,this.dayOfWeek);
        cv.put(TableDay.weekOfMonth,this.weekOfMonth);
        cv.put(TableDay.monthOfYear,this.monthOfYear);
        cv.put(TableDay.quarterOfYear,this.quarterOfYear);
        cv.put(TableDay.year,this.year);
        if(this.id!=0)
            cv.put(TableDay.id,this.id);
        long rowId=db.insertWithOnConflict(TableDay.day,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }

    public Day get(long id){
        String query="select * " +
                    " "+" from "+TableDay.day+" " +
                " "+" where "+TableDay.id+" = "+id;
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                this.setId(c.getInt(c.getColumnIndex(TableDay.id)));
                this.setDayOfYear(c.getInt(c.getColumnIndex(TableDay.dayOfYear)));
                this.setDate(CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableDay.date))));
                this.setDayOfWeek(c.getInt(c.getColumnIndex(TableDay.dayOfWeek)));
                this.setWeekOfMonth(c.getInt(c.getColumnIndex(TableDay.weekOfMonth)));
                this.setMonthOfYear(c.getInt(c.getColumnIndex(TableDay.monthOfYear)));
                this.setQuarterOfYear(c.getInt(c.getColumnIndex(TableDay.quarterOfYear)));
                this.setYear(c.getInt(c.getColumnIndex(TableDay.year)));
            }while (c.moveToNext());
        }
        return this;
    }


    public List<Day> getAll(){
        List<Day> days=null;
        String query="select * " +
                " "+" from "+TableDay.day+" " +
                " "+" order by strftime('%Y-%m-%d %H:%M:%S',"+TableDay.date+") asc ";
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            days=new ArrayList<>();
            do{
                Day day=new Day(context);
                day.setId(c.getInt(c.getColumnIndex(TableDay.id)));
                day.setDayOfYear(c.getInt(c.getColumnIndex(TableDay.dayOfYear)));
                day.setDate(CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableDay.date))));
                day.setDayOfWeek(c.getInt(c.getColumnIndex(TableDay.dayOfWeek)));
                day.setWeekOfMonth(c.getInt(c.getColumnIndex(TableDay.weekOfMonth)));
                day.setMonthOfYear(c.getInt(c.getColumnIndex(TableDay.monthOfYear)));
                day.setQuarterOfYear(c.getInt(c.getColumnIndex(TableDay.quarterOfYear)));
                day.setYear(c.getInt(c.getColumnIndex(TableDay.year)));
                days.add(day);
            }while (c.moveToNext());
        }
        return days;
    }

    public int delete(){
            SQLiteDatabase db=database.getWritableDatabase();
            int numOfRowAffected=db.delete(TableDay.day, TableDay.id + "=" + id, null);
            return numOfRowAffected;
    }

    public int deletePrevDays(String startDate,String endDate){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableDay.day,TableDay.date+">='"+startDate+"'"+" and "+TableDay.date+"<'"+endDate+"'",null);
        return numOfRowAffected;
    }

    public int deleteNextDays(String startDate,String endDate){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableDay.day,TableDay.date+"<'"+startDate+"'"+" and "+TableDay.date+"=>'"+endDate+"'",null);
        return numOfRowAffected;
    }

}
