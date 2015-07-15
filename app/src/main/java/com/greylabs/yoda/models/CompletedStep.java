package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData;

import java.util.ArrayList;
import java.util.List;

import static  com.greylabs.yoda.database.MetaData.TableCompletedStep;

/**
 * Created by Jaybhay Vijay on 6/30/2015.
 */
public class CompletedStep {
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private byte series;
    private int stepCount;
    private Context context;
    private Database database;

    /**********************************************************************************************/
    //Getters and Setters
    /**********************************************************************************************/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getSeries() {
        return series;
    }

    public void setSeries(byte series) {
        this.series = series;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public CompletedStep(Context context){
        this.database=Database.getInstance(context);
        this.context=context;
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/
    @Override
    public String toString() {
        return "CompletedStep{" +
                "id=" + id +
                ", series=" + series +
                ", stepCount=" + stepCount +
                '}';
    }

    public CompletedStep get(long id){
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableCompletedStep.completedStep+" " +
                " "+"where "+TableCompletedStep.id+" = "+id;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                this.id=c.getInt(c.getColumnIndex(TableCompletedStep.id));
                this.series=(byte)c.getInt(c.getColumnIndex(TableCompletedStep.series));
                this.stepCount=c.getInt(c.getColumnIndex(TableCompletedStep.stepCount));
            }while (c.moveToNext());
        }
        c.close();
        return this;
    }

    public List<CompletedStep> getAll(long id){
        ArrayList<CompletedStep> completedSteps=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableCompletedStep.completedStep+" " ;

        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            completedSteps=new ArrayList<>();
            do{
                CompletedStep completedStep=new CompletedStep(context);
                completedStep.id=c.getInt(c.getColumnIndex(TableCompletedStep.id));
                completedStep.series=(byte)c.getInt(c.getColumnIndex(TableCompletedStep.series));
                completedStep.stepCount=c.getInt(c.getColumnIndex(TableCompletedStep.stepCount));
                completedSteps.add(completedStep);
            }while (c.moveToNext());
        }
        c.close();
        return completedSteps;
    }

    public long save(){
        SQLiteDatabase db=database.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TableCompletedStep.series,this.series);
        values.put(TableCompletedStep.stepCount,this.stepCount);
        long rowId;
        if(this.id!=0){
            values.put(TableCompletedStep.id,this.id);
        }
        rowId=db.insertWithOnConflict(TableCompletedStep.completedStep, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }

    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableCompletedStep.completedStep,TableCompletedStep.id+"="+id,null);
        return numOfRowAffected;
    }

    /**********************************************************************************************/
    //Thin Result Methods
    /**********************************************************************************************/
    public long getCompletedStepCount(long goalId){
        long completedStepCount=0;
        SQLiteDatabase db=database.getReadableDatabase();
        //this query returns sum of time of all steps that are present in the Complpeted Step table
        String completedStepCountQuery=" select count(*) as stepCount" +
                " "+ MetaData.TableCompletedStep.completedStep+" as c join "+" "+ MetaData.TablePendingStep.pendingStep+" as p " +
                " "+"on ( c."+ MetaData.TablePendingStep.id+" = "+ MetaData.TableCompletedStep.id+" )" +
                " "+"where p."+ MetaData.TablePendingStep.goalId+"="+id;
        Cursor c=db.rawQuery(completedStepCountQuery,null);
        if(c.moveToFirst()){
            do{
                completedStepCount=c.getInt(c.getColumnIndex("stepCount"));
            }while(c.moveToNext());
        }
        c.close();
        return completedStepCount;
    }

}
