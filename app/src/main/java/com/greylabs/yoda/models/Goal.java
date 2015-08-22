package com.greylabs.yoda.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.api.client.util.DateTime;
import com.greylabs.yoda.database.Database;
import com.greylabs.yoda.database.MetaData;
import com.greylabs.yoda.database.MetaData.TableGoal;
import com.greylabs.yoda.database.MetaData.TablePendingStep;
import com.greylabs.yoda.enums.AccountType;
import com.greylabs.yoda.utils.CalendarUtils;
import com.greylabs.yoda.utils.Prefs;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Goal implements Serializable{
    /**********************************************************************************************/
    // Instance variables
    /**********************************************************************************************/
    private long id;
    private String stringId="";//used for google tasks.
    private String nickName;
    private String objective;
    private String keyResult;
    private String reason;
    private String reward;
    private String buddyEmail;
    private byte status;
    private int order;
    private int progress;//progress of goal in percent
    private Date dueDate;
    private long timeBoxId;
    private DateTime updated=new DateTime(new Date());//last updated date
    private boolean deleted;//true if deleted
    private String account;
    private AccountType accountType;
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

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(String keyResult) {
        this.keyResult = keyResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getBuddyEmail() {
        return buddyEmail;
    }

    public void setBuddyEmail(String buddyEmail) {
        this.buddyEmail = buddyEmail;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public long getTimeBoxId() {
        return timeBoxId;
    }
    public void initDatabase(Context context){
        this.database=Database.getInstance(context);
    }
    public void setTimeBoxId(long timeBoxId) {
        this.timeBoxId = timeBoxId;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    /**********************************************************************************************/
    // Constructors
    /**********************************************************************************************/
    public Goal(Context context){
        this.database=Database.getInstance(context);
        this.context=context;
        dueDate=new Timestamp(new Date().getTime());
    }

    /**********************************************************************************************/
    // Methods
    /**********************************************************************************************/

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", objective='" + objective + '\'' +
                ", keyResult='" + keyResult + '\'' +
                ", reason='" + reason + '\'' +
                ", reward='" + reward + '\'' +
                ", buddyEmail='" + buddyEmail + '\'' +
                ", status=" + status +
                ", order=" + order +
                ", dueDate=" + dueDate +
                ", timeBoxId=" + timeBoxId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        Goal goal=(Goal)o;
        return goal.id==this.id;
    }

    public Goal get(long id){
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableGoal.goal+" " +
                " "+"where "+ TableGoal.id+" = "+id;
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                this.id=c.getInt(c.getColumnIndex(TableGoal.id));
                this.stringId=c.getString(c.getColumnIndex(TableGoal.stringId));
                this.nickName=c.getString(c.getColumnIndex(TableGoal.nickName));
                this.objective=c.getString(c.getColumnIndex(TableGoal.objective));
                this.keyResult=c.getString(c.getColumnIndex(TableGoal.keyResult));
                this.reason=c.getString(c.getColumnIndex(TableGoal.reason));
                this.reward=c.getString(c.getColumnIndex(TableGoal.buddyEmail));
                this.status=(byte)c.getInt(c.getColumnIndex(TableGoal.status));
                this.dueDate= CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableGoal.dueDate)));
                this.timeBoxId=c.getInt(c.getColumnIndex(TableGoal.timeBoxId));
                this.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TableGoal.updated)));
                this.deleted=(c.getInt(c.getColumnIndex(TableGoal.deleted))==1)?true:false;
                this.account=c.getString(c.getColumnIndex(TableGoal.account));
                this.accountType=AccountType.getIntegerToEnum(c.getInt(c.getColumnIndex(TableGoal.accountType)));
            }while (c.moveToNext());
        }
        c.close();
        return this;
    }

    public List<Goal> getAll(){
        ArrayList<Goal> goals=null;
        SQLiteDatabase db=database.getReadableDatabase();
        String query="select * " +
                " "+" from "+ TableGoal.goal+"" +
                " "+" where "+TableGoal.deleted+"=0";
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            goals=new ArrayList<>();
            do{
                Goal goal=new Goal(context);
                goal.id=c.getInt(c.getColumnIndex(TableGoal.id));
                goal.stringId=c.getString(c.getColumnIndex(TableGoal.stringId));
                goal.nickName=c.getString(c.getColumnIndex(TableGoal.nickName));
                goal.objective=c.getString(c.getColumnIndex(TableGoal.objective));
                goal.keyResult=c.getString(c.getColumnIndex(TableGoal.keyResult));
                goal.reason=c.getString(c.getColumnIndex(TableGoal.reason));
                goal.reward=c.getString(c.getColumnIndex(TableGoal.buddyEmail));
                goal.status=(byte)c.getInt(c.getColumnIndex(TableGoal.status));
                goal.dueDate=CalendarUtils.parseDate(c.getString(c.getColumnIndex(TableGoal.dueDate)));
                goal.timeBoxId=c.getInt(c.getColumnIndex(TableGoal.timeBoxId));
                goal.updated=CalendarUtils.getStringToRFCTimestamp(c.getString(c.getColumnIndex(TableGoal.updated)));
                goal.deleted=(c.getInt(c.getColumnIndex(TableGoal.deleted))==1)?true:false;
                goal.account=c.getString(c.getColumnIndex(TableGoal.account));
                goal.accountType=AccountType.getIntegerToEnum(c.getInt(c.getColumnIndex(TableGoal.accountType)));
                goals.add(goal);
            }while (c.moveToNext());
        }
        c.close();
        return goals;
    }

    public long save() {
        SQLiteDatabase db=database.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(TableGoal.stringId,this.stringId);
        values.put(TableGoal.nickName,this.nickName);
        values.put(TableGoal.objective,this.objective);
        values.put(TableGoal.keyResult,this.keyResult);
        values.put(TableGoal.reason,this.reason);
        values.put(TableGoal.reward,this.reward);
        values.put(TableGoal.buddyEmail,this.buddyEmail);
        values.put(TableGoal.status,this.status);
        values.put(TableGoal.order, this.order);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);cal.set(Calendar.MINUTE, 0);cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.setTime(this.getDueDate());
        values.put(TableGoal.dueDate, CalendarUtils.getSqLiteDateFormat(cal));
        values.put(TableGoal.timeBoxId, this.timeBoxId);
        values.put(TableGoal.updated,CalendarUtils.getRFCTimestampToString(this.getUpdated()));
        values.put(TableGoal.deleted,(this.deleted)?1:0);
        values.put(TableGoal.account,this.account);
        values.put(TableGoal.accountType,this.accountType.ordinal());
        long rowId;
        if (this.id!=0){
            values.put(TableGoal.id,this.id);
        }
        rowId=db.insertWithOnConflict(TableGoal.goal, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        this.id=rowId;
        return rowId;
    }

    public void setPendingStepGoalStringId(String goalId){
        String query="update "+TablePendingStep.pendingStep+" " +
                " set "+TablePendingStep.goalStringId+" = '"+goalId+"' " +
                " where "+TablePendingStep.goalId+"="+this.getId();
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        c.close();
    }
    public int delete(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableGoal.goal,TableGoal.id+"="+id,null);
        return numOfRowAffected;
    }

    public int deletePendingSteps(){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TablePendingStep.pendingStep,TablePendingStep.goalId+"="+this.id,null);
        return numOfRowAffected;
    }

    public int  deleteAllSteps(String stringGoalId){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TablePendingStep.pendingStep,TablePendingStep.goalStringId+"='"+stringGoalId+"'",null);
        return numOfRowAffected;
    }
    public int deleteGoal(String stringGoalId){
        SQLiteDatabase db=database.getWritableDatabase();
        int numOfRowAffected=db.delete(TableGoal.goal,TableGoal.stringId+"='"+stringGoalId+"'",null);
        return numOfRowAffected;
    }

    /**
     * This method computes the completion of goal in terms of the percent
     * @return percentage of goal completion
     */
    public float getGoalProgress(){
        int totalStepsCompletedTime=0;
        int totalStepsUnCompletedTime=0;
        SQLiteDatabase db=database.getReadableDatabase();
        //this query returns sum of time of all steps that are present in the Complpeted Step table
        String sumOfTimeComplete=" select sum(" +TablePendingStep.time+") as  totalStepsCompletedTime "+
                " "+" from "+TablePendingStep.pendingStep+" " +
                " "+" where ("+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SINGLE_STEP.ordinal()+" or " +
                " "+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SUB_STEP.ordinal()+" ) and (" +
                " " +TablePendingStep.status+" = "+PendingStep.PendingStepStatus.COMPLETED.ordinal()+" )" +
                " "+" and "+ TablePendingStep.goalId+" = "+this.id;
        Cursor c=db.rawQuery(sumOfTimeComplete,null);
        if(c.moveToFirst()){
            do{
                totalStepsCompletedTime=c.getInt(c.getColumnIndex("totalStepsCompletedTime"));
            }while(c.moveToNext());
        }
        c.close();
        String sumOfTimePending=" select sum("+TablePendingStep.time+") as totalStepsUnCompletedTime  " +
                " "+" from "+TablePendingStep.pendingStep+" " +
                " "+" where ("+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SINGLE_STEP.ordinal()+" or " +
                " "+TablePendingStep.type+"= "+ PendingStep.PendingStepType.SUB_STEP.ordinal()+" ) and (" +
                " " +TablePendingStep.status+" = "+PendingStep.PendingStepStatus.TODO.ordinal()+ " )"+
                " "+" and "+ TablePendingStep.goalId+" = "+this.id;

         c=db.rawQuery(sumOfTimePending,null);
        if(c.moveToFirst()){
            do{
                totalStepsUnCompletedTime=c.getInt(c.getColumnIndex("totalStepsUnCompletedTime"));
            }while(c.moveToNext());
        }
        c.close();
        float percentage=(float)((totalStepsCompletedTime/(float)(totalStepsCompletedTime+totalStepsUnCompletedTime))*100.0);
        return percentage;
    }

    public int getStepCount(){
        int stepCount=0;
        String query="select count(*) as stepCount" +
                " "+" from "+TablePendingStep.pendingStep+" " +
                " "+" where "+TablePendingStep.goalId+" = "+this.id+" " +
                " "+" and "+TablePendingStep.type+"!="+ PendingStep.PendingStepType.SUB_STEP.ordinal();
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        stepCount=c.getInt(c.getColumnIndex("stepCount"));
        return stepCount;
    }
    public int getRemainingStepCount(){
        int stepCount=0;
        String query="select count(*) as stepCount" +
                " "+" from "+TablePendingStep.pendingStep+" " +
                " "+" where "+TablePendingStep.goalId+" = "+this.id+" " +
                " "+" and ("+TablePendingStep.type+"="+ PendingStep.PendingStepType.SUB_STEP.ordinal()+" " +
                " "+" or "+TablePendingStep.type+"="+ PendingStep.PendingStepType.SINGLE_STEP.ordinal()+") " +
                " "+" and "+TablePendingStep.status+" = "+PendingStep.PendingStepStatus.TODO.ordinal()+" " +
                " "+" and "+TablePendingStep.deleted+"=0";
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        stepCount=c.getInt(c.getColumnIndex("stepCount"));
        return stepCount;
    }

    public long getGoalId(long timeBoxId){
        long goalId=0;
        String query="select id " +
                " "+" from "+TableGoal.goal+" " +
                " "+" where "+TableGoal.timeBoxId+" = "+timeBoxId;
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            goalId=c.getLong(c.getColumnIndex(TableGoal.id));
        }
        return goalId;
    }
    public String getColorCode(){
        String colorCode="";
        String query="select  " +MetaData.TableTimeBox.colorCode+" "+
                " "+" from "+ MetaData.TableTimeBox.timeBox+" " +
                " "+" where "+ MetaData.TableTimeBox.id+" = "+this.timeBoxId;
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            colorCode=c.getString(c.getColumnIndex(MetaData.TableTimeBox.colorCode));
        }
        return colorCode;
    }

    public long getIdIfExists(String stringGoalId){
        long id= Prefs.getInstance(context).getStretchGoalId();
        String query="select  "+TableGoal.id +" ,"+TableGoal.stringId+" "+
                " "+" from "+ TableGoal.goal+" " +
                " "+" where "+ TableGoal.stringId+" = '"+stringGoalId+"'";
        SQLiteDatabase db=database.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            id=c.getLong(c.getColumnIndex(TableGoal.id));
        }
        return id;
    }

    public List<String> getDistinctGoalStringIds(){
        String query =" select  distinct("+TablePendingStep.goalStringId+") as stringIds " +
                     "  from "+TablePendingStep.pendingStep;
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        List<String> stringGoals=null;
        if(c.moveToFirst()){
            stringGoals=new ArrayList<>();
            do{
                stringGoals.add(c.getString(c.getColumnIndex("stringIds")));
            }while (c.moveToNext());
        }
        return stringGoals;
    }
}
