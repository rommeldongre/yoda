package com.greylabs.yoda.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.greylabs.yoda.R;

public class Prefs {
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
        this.systemPrefs = context.getSharedPreferences("user_prefs", Activity.MODE_PRIVATE);
        this.editor = systemPrefs.edit();
    }

//    public void commit() {
//        editor.commit();
//    }

    public void setApplicationFirstTimeInstalled(boolean isAppFirstTimeInstalled){
        editor.putBoolean(Constants.IS_APPLICATION_FIRST_TIME_INSTALLED, isAppFirstTimeInstalled);
        editor.commit();
    }
    public boolean getApplicationFirstTimeInstalled(){
        return systemPrefs.getBoolean(Constants.IS_APPLICATION_FIRST_TIME_INSTALLED, true);
    }
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

//    public boolean getBottomMostPriorityOfNewStep(){
//        return systemPrefs.getBoolean(Constants.BOTTOM_MOST_PRIORITY_OF_NEW_STEP, Constants.BOTTOM_MOST_PRIORITY_OF_NEW_STEP_VALUE);
//    }
//
//    public void setBottomMostPriorityOfNewStep(boolean isChecked){
//        editor.putBoolean(Constants.BOTTOM_MOST_PRIORITY_OF_NEW_STEP, isChecked);
//        editor.commit();
//    }
//
//    public boolean getTopMostPriorityOfNewStep(){
//        return systemPrefs.getBoolean(Constants.TOP_MOST_PRIORITY_OF_NEW_STEP, Constants.TOP_MOST_PRIORITY_OF_NEW_STEP_VALUE);
//    }
//
//    public void setTopMostPriorityOfNewStep(boolean isChecked){
//        editor.putBoolean(Constants.TOP_MOST_PRIORITY_OF_NEW_STEP, isChecked);
//        editor.commit();
//    }
//
//    public boolean getDontExpireBehaviour(){
//        return systemPrefs.getBoolean(Constants.DONT_EXPIRE_BEHAVIOUR, Constants.DONT_EXPIRE_BEHAVIOUR_VALUE);
//    }
//
//    public void setDontExpireBehaviour(boolean isChecked){
//        editor.putBoolean(Constants.DONT_EXPIRE_BEHAVIOUR, isChecked);
//        editor.commit();
//    }
//
//    public boolean getExpireBehaviour(){
//        return systemPrefs.getBoolean(Constants.EXPIRE_BEHAVIOUR, Constants.EXPIRE_BEHAVIOUR_VALUE);
//    }
//
//    public void setExpireBehaviour(boolean isChecked){
//        editor.putBoolean(Constants.EXPIRE_BEHAVIOUR, isChecked);
//        editor.commit();
//    }

    public boolean isPriorityNewStepBottomMost(){
        return systemPrefs.getBoolean(Constants.PRIORITY_NEW_STEP_BOTTOM_MOST, Constants.PRIORITY_NEW_STEP_BOTTOM_MOST_VALUE);
    }

    public void setPriorityNewStepBottomMost(boolean isChecked){
        editor.putBoolean(Constants.PRIORITY_NEW_STEP_BOTTOM_MOST, isChecked);
        editor.commit();
    }

    public boolean isBehaviourDoNotExpire(){
        return systemPrefs.getBoolean(Constants.BEHAVIOUR_DO_NOT_EXPIRE, Constants.BEHAVIOUR_DO_NOT_EXPIRE_VALUE);
    }

    public void setBehaviourDoNotExpire(boolean isChecked){
        editor.putBoolean(Constants.BEHAVIOUR_DO_NOT_EXPIRE, isChecked);
        editor.commit();
    }

    public void setWallpaperResourceId(int resId) {
        editor.putInt(Constants.WALLPAPER_RESOURCE_ID, resId);
        editor.commit();
    }

    public int getWallpaperResourceId() {
        return systemPrefs.getInt(Constants.WALLPAPER_RESOURCE_ID, R.drawable.wallpaper1);
    }
}