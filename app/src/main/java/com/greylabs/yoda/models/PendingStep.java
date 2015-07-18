package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TablePendingStep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PendingStep implements Serializable{
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private String nickName;
    private int priority;
    private int time;
    private boolean series;
    private int stepCount;
    private int skipCount;
    private long goalId;
    transient private Database database;
    transient private Context context;


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

    public boolean isSeries() {
        return series;
    }

    public void setSeries(boolean series) {
        this.series = series;
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
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public PendingStep(Context context){
        this.context=context;
        database=Database.getInstance(context);
    }


    /**********************************************************************************************/
    //Core Methods
    /**********************************************************************************************/

    @Override
    public String toString() {
        return "PendingStep{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", priority=" + priority +
                ", time=" + time +
                ", series=" + series +
                ", stepCount=" + stepCount +
                ", skipCount=" + skipCount +
                ", goalId=" + goalId +
                '}';
    }

    public PendingStep get(long id){
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TablePendingStep.pendingStep+" " +
                " "+"where "+TablePendingStep.id+" = "+id;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                this.id=c.getInt(c.getColumnIndex(TablePendingStep.id));
                this.nickName=c.getString(c.getColumnIndex(TablePendingStep.nickName));
                this.priority=c.getInt(c.getColumnIndex(TablePendingStep.priority));
                this.time=c.getInt(c.getColumnIndex(TablePendingStep.time));
                this.series=(c.getInt(c.getColumnIndex(TablePendingStep.series))==0)?false:true;
                this.stepCount=c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                this.skipCount=c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                this.goalId=c.getInt(c.getColumnIndex(TablePendingStep.goalId));
            }while (c.moveToNext());
        }
        c.close();
        return this;
    }

    public List<PendingStep> getAll(){
        ArrayList<PendingStep> pendingSteps=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TablePendingStep.pendingStep+" ";

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            pendingSteps=new ArrayList<>();
            do{
                PendingStep pendingStep=new PendingStep(context);
                pendingStep.id=c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.nickName=c.getString(c.getColumnIndex(TablePendingStep.nickName));
                pendingStep.priority=c.getInt(c.getColumnIndex(TablePendingStep.priority));
                pendingStep.time=c.getInt(c.getColumnIndex(TablePendingStep.time));
                pendingStep.series=(c.getInt(c.getColumnIndex(TablePendingStep.series))==0)?false:true;
                pendingStep.stepCount=c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                pendingStep.skipCount=c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                pendingStep.goalId=c.getInt(c.getColumnIndex(TablePendingStep.goalId));

                pendingSteps.add(pendingStep);
            }while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }

    public List<PendingStep> getAll(long goalId){
        ArrayList<PendingStep> pendingSteps=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TablePendingStep.pendingStep+" " +
                " "+" where "+TablePendingStep.goalId+" = "+goalId;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            pendingSteps=new ArrayList<>();
            do{
                PendingStep pendingStep=new PendingStep(context);
                pendingStep.id=c.getInt(c.getColumnIndex(TablePendingStep.id));
                pendingStep.nickName=c.getString(c.getColumnIndex(TablePendingStep.nickName));
                pendingStep.priority=c.getInt(c.getColumnIndex(TablePendingStep.priority));
                pendingStep.time=c.getInt(c.getColumnIndex(TablePendingStep.time));
                pendingStep.series=(c.getInt(c.getColumnIndex(TablePendingStep.series))==0)?false:true;
                pendingStep.stepCount=c.getInt(c.getColumnIndex(TablePendingStep.stepCount));
                pendingStep.skipCount=c.getInt(c.getColumnIndex(TablePendingStep.skipCount));
                pendingStep.goalId=c.getInt(c.getColumnIndex(TablePendingStep.goalId));

                pendingSteps.add(pendingStep);
            }while (c.moveToNext());
        }
        c.close();
        return pendingSteps;
    }



    public long save(){
        SQLiteDatabase db=database.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TablePendingStep.nickName,this.nickName);
        values.put(TablePendingStep.priority,this.priority);
        values.put(TablePendingStep.time,this.time);
        values.put(TablePendingStep.series,this.series?1:0);
        values.put(TablePendingStep.stepCount,this.stepCount);
        values.put(TablePendingStep.skipCount,this.skipCount);
        values.put(TablePendingStep.goalId, this.goalId);
        long rowId;
        if(this.id!=0){
            values.put(TablePendingStep.id,this.id);
        }
        rowId=db.insertWithOnConflict(TablePendingStep.pendingStep, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TablePendingStep.pendingStep, TablePendingStep.id + "=" + id, null);
        return numOfRowAffected;
    }
    /**********************************************************************************************/
    //Thin Result Methods
    /**********************************************************************************************/
    public long getPendingStepCount(long goalId){
        long pendingStepCount=0;
        SQLiteDatabase db=database.getReadableDatabase();
        //this query returns sum of time of all steps that are present in the Complpeted Step table
        String pendingStepCountQuery=" select count(*) as stepCount " +
                " "+"from "+ MetaData.TablePendingStep.pendingStep+" " +
                " "+"where "+ MetaData.TablePendingStep.goalId+"="+id;
        Cursor c=db.rawQuery(pendingStepCountQuery,null);
        if(c.moveToFirst()){
            do{
                pendingStepCount=c.getInt(c.getColumnIndex("stepCount"));
            }while(c.moveToNext());
        }
        c.close();
        return pendingStepCount;
    }
}
