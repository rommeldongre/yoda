package com.greylabs.yoda.models;

import android.content.Context;

import com.greylabs.yoda.database.Database;

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
    private int day;
    private int weekDay;
    private Date date;
    private int weekOfMonth;
    private int quarterOfYear;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
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

    /**
     * This method retrieves all the slot of passed timebox.Internally it build where condition to
     * filter records of that fit in timebox
     * @param timeBox
     * @return None
     */
    public void getAll(TimeBox timeBox){


    }

    /**
     * This method save(updates) all days.
     * @param days
     * @return None
     */
    public void saveAll(List<Day> days){

    }
}
