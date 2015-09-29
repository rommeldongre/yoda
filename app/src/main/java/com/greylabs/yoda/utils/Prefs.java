package com.greylabs.yoda.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.greylabs.yoda.R;

public class Prefs  {
    Context context;
    SharedPreferences systemPrefs;
    SharedPreferences.Editor editor;

    static Prefs prefs;

    public static Prefs getInstance(Context context){
        if(prefs == null){
            prefs = new Prefs(context);
        }
        return prefs;
    }

    private Prefs(Context context) {
        this.context = context;
        this.systemPrefs = context.getSharedPreferences(Constants.USER_PREFS,Activity.MODE_PRIVATE);
        this.editor = systemPrefs.edit();
    }

//    public void commit() {
//        editor.commit();
//    }

    public int getDefaultStepDuration(){
        return systemPrefs.getInt(Constants.DEFAULT_STEP_DURATION, Constants.DEFAULT_STEP_DURATION_VALUE);
    }

    public void setDefaultStepDuration(int newDuration){
        editor.putInt(Constants.DEFAULT_STEP_DURATION, newDuration);
        editor.commit();
    }

    public int getDefaultSessionDuration(){
        return systemPrefs.getInt(Constants.DEFAULT_SESSION_DURATION, Constants.DEFAULT_SESSION_DURATION_VALUE);
    }

    public void setDefaultSessionDuration(int newDuration){
        editor.putInt(Constants.DEFAULT_SESSION_DURATION, newDuration);
        editor.commit();
    }

    public int getYodaSaysNotificationDuration(){
        return systemPrefs.getInt(Constants.DEFAULT_YODA_SAYS_NOTIFICATION_DURATION, Constants.DEFAULT_YODA_SAYS_NOTIFICATION_DURATION_VALUE);
    }

    public void setYodaSaysNotificationDuration(int newDuration){
        editor.putInt(Constants.DEFAULT_YODA_SAYS_NOTIFICATION_DURATION, newDuration);
        editor.commit();
    }

    public int getExportToCalendarDuration(){
        return systemPrefs.getInt(Constants.DEFAULT_EXPORT_TO_CALENDAR_DURATION, Constants.DEFAULT_EXPORT_TO_CALENDAR_DURATION_VALUE);
    }

    public void setExportToCalendarDuration(int newDuration){
        editor.putInt(Constants.DEFAULT_EXPORT_TO_CALENDAR_DURATION, newDuration);
        editor.commit();
    }

    public boolean isPriorityNewStepBottomMost(){
        return systemPrefs.getBoolean(Constants.PRIORITY_NEW_STEP_BOTTOM_MOST, Constants.PRIORITY_NEW_STEP_BOTTOM_MOST_VALUE);
    }

    public void setPriorityNewStepBottomMost(boolean isChecked){
        editor.putBoolean(Constants.PRIORITY_NEW_STEP_BOTTOM_MOST, isChecked);
        editor.commit();
    }

//    public boolean isBehaviourDoNotExpire(){
//        return systemPrefs.getBoolean(Constants.BEHAVIOUR_DO_NOT_EXPIRE, Constants.BEHAVIOUR_DO_NOT_EXPIRE_VALUE);
//    }
//
//    public void setBehaviourDoNotExpire(boolean isChecked){
//        editor.putBoolean(Constants.BEHAVIOUR_DO_NOT_EXPIRE, isChecked);
//        editor.commit();
//    }

    public void setWallpaperResourceId(int resId) {
        editor.putInt(Constants.WALLPAPER_RESOURCE_ID, resId);
        editor.commit();
    }

    public int getWallpaperResourceId() {
        return systemPrefs.getInt(Constants.WALLPAPER_RESOURCE_ID, R.drawable.wallpaper1);
    }

    public boolean isCalendarInitialized(){
        return systemPrefs.getBoolean(Constants.IS_CALENDAR_INITIALIZED, false);
    }

    public void setCalendarInitialized(boolean isChecked){
        editor.putBoolean(Constants.IS_CALENDAR_INITIALIZED, isChecked);
        editor.commit();
    }

    public boolean isOptionFromActQuickStartSelected() {
        return systemPrefs.getBoolean(Constants.OPTION_FROM_ACTQUICKSTART_SELECTED, false);
    }

    public void setOptionFromActQuickStartSelected(boolean isChecked){
        editor.putBoolean(Constants.OPTION_FROM_ACTQUICKSTART_SELECTED, isChecked);
        editor.commit();
    }

    public void setUnplannedTimeBoxId(long timeBoxId){
        editor.putLong(Constants.ID_UNPLANNED_TIMEBOX, timeBoxId);
        editor.commit();
    }

    public long getUnplannedTimeBoxId(){
        return systemPrefs.getLong(Constants.ID_UNPLANNED_TIMEBOX,0);
    }

    public void setStretchGoalId(long stretchGoalId){
        editor.putLong(Constants.ID_STRETCH_GOAL, stretchGoalId);
        editor.commit();
    }

    public long getStretchGoalId(){
        return systemPrefs.getLong(Constants.ID_STRETCH_GOAL,0);
    }

    /**
     * Account Prefs
     */
    public String getDefaultAccountEmailId(){
        return systemPrefs.getString(Constants.ACCOUNT_DEFAULT_EMAIL_ID, null);
    }

    public void setDefaultAccountEmailId(String emailId){
        editor.putString(Constants.ACCOUNT_DEFAULT_EMAIL_ID, emailId);
        editor.commit();
    }


    public String getDefaultAccountAuthToken(){
        return systemPrefs.getString(Constants.ACCOUNT_DEFAULT_AUTH_TOKEN, null);
    }

    public void setDefaultAccountAuthToken(String token){
        editor.putString(Constants.ACCOUNT_DEFAULT_AUTH_TOKEN, token);
        editor.commit();
    }


    public int getDefaultAccountType(){
        return systemPrefs.getInt(Constants.ACCOUNT_DEFAULT_ACC_TYPE, 0);
    }

    public void setDefaultAccountType(int type){
        editor.putInt(Constants.ACCOUNT_DEFAULT_ACC_TYPE, type);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public int getColorCodePosition() {
        return systemPrefs.getInt(Constants.COLORCODE_POSITION, 0);
    }

    public void setColorCodePosition(int position){
        editor.putInt(Constants.COLORCODE_POSITION, position);
        editor.commit();
    }
    public boolean getAutoSyncState() {
        return systemPrefs.getBoolean(Constants.ACCOUNT_AUTO_SYNC, false);
    }

    public void setAutoSyncState(boolean state){
        editor.putBoolean(Constants.ACCOUNT_AUTO_SYNC, state);
        editor.commit();
    }
}