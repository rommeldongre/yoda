package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TableTimeBox;
import com.greylabs.yoda.database.MetaData.TableTimeBoxOn;
import com.greylabs.yoda.database.MetaData.TableTimeBoxWhen;
import com.greylabs.yoda.enums.TimeBoxTill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TimeBox implements Serializable{
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private String nickName;
    transient private Context context;
    transient private Database database;
    private TimeBoxTill tillType;
    private TimeBoxOn timeBoxOn;
    private TimeBoxWhen timeBoxWhen;
    private String colorCode;

    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/
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

    public TimeBoxOn getTimeBoxOn() {
        return timeBoxOn;
    }
    public void setTimeBoxOn(TimeBoxOn timeBoxOn) {
        this.timeBoxOn = timeBoxOn;
    }

    public TimeBoxWhen getTimeBoxWhen() {
        return timeBoxWhen;
    }

    public void setTimeBoxWhen(TimeBoxWhen timeBoxWhen) {
        this.timeBoxWhen = timeBoxWhen;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }
    public TimeBoxTill getTillType() {
        return tillType;
    }

    public void setTillType(TimeBoxTill tillType) {
        this.tillType = tillType;
    }
    public String getColorCode() {
        return colorCode;
    }
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public TimeBox(Context context){
        this.context=context;
        this.database=Database.getInstance(context);
    }
    public TimeBox(Context context,TimeBoxWhen timeBoxWhen,TimeBoxOn timeBoxOn,TimeBoxTill tillType){
        this(context);
        this.timeBoxWhen=timeBoxWhen;
        this.timeBoxOn=timeBoxOn;
        this.tillType=tillType;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/



    public TimeBox get(long id){
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableTimeBox.timeBox+" " +
                " "+"where "+TableTimeBox.id+" = "+id;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                this.id=c.getInt(c.getColumnIndex(TableTimeBox.id));
                this.nickName=c.getString(c.getColumnIndex(TableTimeBox.nickName));
                this.colorCode=c.getString(c.getColumnIndex(TableTimeBox.colorCode));
                this.timeBoxOn=new TimeBoxOn(context,
                        com.greylabs.yoda.enums.TimeBoxOn.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableTimeBox.on))));
                this.timeBoxOn.setTimeBoxId(this.id);
                this.timeBoxOn.setSubValues(this.timeBoxOn.get());
                this.timeBoxWhen=new TimeBoxWhen(context);
                this.timeBoxWhen.setTimeBoxId(this.id);
                this.timeBoxWhen.setWhenValues(this.timeBoxWhen.get());
                this.tillType=TimeBoxTill.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableTimeBox.till)));
            }while (c.moveToNext());
        }
        c.close();
        //db.close();
        return this;
    }

    public List<TimeBox> getAll(TimeBoxStatus status){
        ArrayList<TimeBox> timeBoxes=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="";
        String cols=" t."+TableTimeBox.id+" as tbId , t."+TableTimeBox.nickName+" as timeBoxNickname,"+TableTimeBox.on+"," +
                " "+TableTimeBox.till;
        String query1= " select  " +cols+
                " "+" from "+ TableTimeBox.timeBox+" as t join  "+ MetaData.TableGoal.goal+" as g" +
                " "+" on ( t."+TableTimeBox.id+" = g."+ MetaData.TableGoal.timeBoxId+" ) ";
        String query2=" select  " +cols+
                " "+" from "+ TableTimeBox.timeBox+" as t ";
        if(status==TimeBoxStatus.ACTIVE){
            query= query1;

        }else if(status==TimeBoxStatus.INACTIVE){
            query= " "+query2+" except "+query1+" ";
        }else{
            query=query2;
        }

        Cursor c=db.rawQuery(query, null);
        if(c.moveToFirst()){
            timeBoxes=new ArrayList<>();
            do{
                TimeBox timeBox=new TimeBox(context);
                timeBox.id=c.getLong(c.getColumnIndex("tbId"));
                timeBox.nickName=c.getString(c.getColumnIndex("timeBoxNickname"));
                timeBox.colorCode=c.getString(c.getColumnIndex(TableTimeBox.colorCode));
                timeBox.timeBoxOn=new TimeBoxOn(context,
                        com.greylabs.yoda.enums.TimeBoxOn.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableTimeBox.on))));
                timeBox.timeBoxOn.setTimeBoxId(timeBox.id);
                timeBox.timeBoxOn.setSubValues(timeBox.timeBoxOn.get());
                timeBox.timeBoxWhen=new TimeBoxWhen(context);
                timeBox.timeBoxWhen.setTimeBoxId(timeBox.id);
                timeBox.timeBoxWhen.setWhenValues(timeBox.timeBoxWhen.get());
                timeBox.tillType=TimeBoxTill.getIntegerToEnumType(c.getInt(c.getColumnIndex(TableTimeBox.till)));
                timeBoxes.add(timeBox);

            }while (c.moveToNext());
        }
        c.close();
        //db.close();
        return timeBoxes;
    }

    public long save(){
        SQLiteDatabase db=database.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TableTimeBox.nickName,this.nickName);
        values.put(TableTimeBox.on, com.greylabs.yoda.enums.TimeBoxOn.getEnumToIntegerType(this.timeBoxOn.getOnType()));
        values.put(TableTimeBox.till, TimeBoxTill.getEnumToIntegerType(this.tillType));
        values.put(TableTimeBox.colorCode,this.colorCode);
        long rowId;
        if(this.id!=0){
            values.put(TableTimeBox.id,this.id);
        }
        rowId=db.insertWithOnConflict(TableTimeBox.timeBox, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.timeBoxOn.setTimeBoxId(rowId);
        this.timeBoxOn.save();
        this.timeBoxWhen.setTimeBoxId(rowId);
        this.timeBoxWhen.save();
        this.id=rowId;
        return rowId;
    }

    public int delete(){
        this.timeBoxOn.delete();
        this.timeBoxWhen.delete();
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableTimeBox.timeBox, TableTimeBox.id + "=" + id, null);
        //db.close();
        return numOfRowAffected;
    }

    public long saveTimeBoxOn(long timeBoxId,List<Integer> timeBoxOns){
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId=0;
        for(int val:timeBoxOns) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxOn.id, timeBoxId);
            values.put(TableTimeBoxOn.on, val);
            rowId += db.insertWithOnConflict(TableTimeBoxOn.timeBoxOn, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return rowId;
    }

    public long saveTimeBoxWhen(long timeBoxId,List<Integer> timeBoxWhens){
        SQLiteDatabase db=database.getWritableDatabase();
        long rowId=0;
        for(int val:timeBoxWhens) {
            ContentValues values = new ContentValues();
            values.put(TableTimeBoxWhen.id, timeBoxId);
            values.put(TableTimeBoxWhen.when, val);
            rowId += db.insertWithOnConflict(TableTimeBoxWhen.timeBoxWhen, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return rowId;
    }
    @Override
    public String toString() {
        return "TimeBox{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", tillType=" + tillType +
                ", timeBoxOn=" + timeBoxOn +
                ", timeBoxWhen=" + timeBoxWhen +
                '}';
    }

    /**********************************************************************************************/
    // Utils
    /**********************************************************************************************/
    public enum TimeBoxStatus {
        ACTIVE,INACTIVE,NONE;
        public static TimeBoxStatus getIntegerToEnumType(int type){
            TimeBoxStatus timeBoxStatus=null;
            switch (type){
                case 0: timeBoxStatus=ACTIVE;break;
                case 1: timeBoxStatus=INACTIVE;break;
                default:timeBoxStatus=NONE;
            }
            return timeBoxStatus;
        }
    }


}
