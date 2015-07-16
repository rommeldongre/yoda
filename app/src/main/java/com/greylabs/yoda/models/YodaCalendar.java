package com.greylabs.yoda.models;

import android.content.Context;

/**
 * Created by Jaybhay Vijay on 7/15/2015.
 */
public class YodaCalendar {
    private Context context;


    /**
     *This method initialize calendar. Calendar need to be initialized when app is first time installed
     * This method must be called when app is installed first time
     *@return None
     */
    public void  init(){

    }

    /**
     *This method attach timebox to goal only if timebox fits into calender slots.
     * @param timeBox TimeBox object which represent Slots to allocate
     * @param  goalId Id of goal to which this timebox is being attached
     * @return return true if time box does not conflict with any slot of calendar, otherwise return
     * false
     */
    public boolean attachTimeBox(TimeBox timeBox , long goalId){

        return false;
    }

    /**
     * This method frees the slots allocated to timebox with id= timeBoxId.
     * @param timeBoxId id of time box whose slot you need to free
     * @return None
     */
    public void detachTimeBox(long timeBoxId){

    }


    /**
     * This method updates the slots in calender based on passed timebox object.This methods first checks
     * availability of slots of this timebox. If timebox fits then all steps are rescheduled and moved to
     * proper slots of calendar
     * @param timeBox updated TimeBox object
     * @return true is returned if TimeBox object does not conflict with any slot of calendar,otherwise
     * false is returned
     */
    public boolean updateTimeBox(TimeBox timeBox){


        return false;
    }
}
