package com.greylabs.yoda.utils;

public class Constants {

    public static final String IS_APPLICATION_FIRST_TIME_INSTALLED ="IS_APPLICATION_FIRST_TIME_INSTALLED" ;
    public static final String GOAL_ATTACHED_IN_EXTRAS = "GOAL_ATTACHED_IN_EXTRAS";
    public static final String GOAL_OBJECT = "GOAL_OBJECT";
    public static final String DEFAULT_STEP_DURATION = "DEFAULT_STEP_DURATION";
    public static final int DEFAULT_STEP_DURATION_VALUE = 3;
    public static final String DEFAULT_SESSION_DURATION = "DEFAULT_SESSION_DURATION";
    public static final int DEFAULT_SESSION_DURATION_VALUE = 3;
    public static final String DEFAULT_YODA_SAYS_NOTIFICATION_DURATION = "DEFAULT_YODA_SAYS_NOTIFICATION_DURATION";
    public static final int DEFAULT_YODA_SAYS_NOTIFICATION_DURATION_VALUE = 3;
    public static final String DEFAULT_EXPORT_TO_CALENDAR_DURATION = "DEFAULT_EXPORT_TO_CALENDAR_DURATION";
    public static final int DEFAULT_EXPORT_TO_CALENDAR_DURATION_VALUE = 7;

    public static final String BOTTOM_MOST_PRIORITY_OF_NEW_STEP = "BOTTOM_MOST_PRIORITY_OF_NEW_STEP";
    public static final boolean BOTTOM_MOST_PRIORITY_OF_NEW_STEP_VALUE = false;
    public static final String TOP_MOST_PRIORITY_OF_NEW_STEP = "TOP_MOST_PRIORITY_OF_NEW_STEP";
    public static final boolean TOP_MOST_PRIORITY_OF_NEW_STEP_VALUE = false;
    public static final String DONT_EXPIRE_BEHAVIOUR = "DONT_EXPIRE_BEHAVIOUR";
    public static final boolean DONT_EXPIRE_BEHAVIOUR_VALUE = false;
    public static final String EXPIRE_BEHAVIOUR = "EXPIRE_BEHAVIOUR";
    public static final boolean EXPIRE_BEHAVIOUR_VALUE = false;
    public static final String PRIORITY_NEW_STEP_BOTTOM_MOST = "PRIORITY_NEW_STEP_BOTTOM_MOST";
    public static final boolean PRIORITY_NEW_STEP_BOTTOM_MOST_VALUE = false;
    public static final String BEHAVIOUR_DO_NOT_EXPIRE = "BEHAVIOUR_DO_NOT_EXPIRE";
    public static final boolean BEHAVIOUR_DO_NOT_EXPIRE_VALUE = false;
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_EDIT = "EDIT";
    public static final String OPERATION_MARK_STEP_DONE = "OPERATION_MARK_STEP_DONE";
    public static final String OPERATION_SHOW_STEPS = "OPERATION_SHOW_STEPS";
    public static final String MSG_DELETE_STEP = "Are you sure you want to delete the step?";
    public static final String MSG_STEP_DELETED = "Step Deleted";
    public static final String MSG_DELETE_GOAL = "Are you sure you want to delete the goal?";
    public static final String MSG_GOAL_DELETED = "Goal Deleted";
    public static final String MSG_DELETE_TIMEBOX = "Are you sure you want to delete the TimeBox?";
    public static final String MSG_TIMEBOX_DELETED = "TimeBox Deleted";
    public static final String STEPS_ARRAY_LIST_WITH_NEW_PRIORITIES = "STEP_ARRAY_LIST";
    public static final String TIMEBOX_NICK_NAME = "TIMEBOX_NICK_NAME";
    public static final int MAX_SLOT_DURATION=3;
    public static final String ACT_ADD_NEW_STEP = "ACT_ADD_NEW_STEP";
    public static final String CALLER = "CALLER";
    public static final String ACT_HOME = "ACT_HOME";
    public static final String ACT_GOAL_DETAILS = "ACT_GOAL_DETAILS";
    public static final String ACT_GOAL_LIST = "ACT_GOAL_LIST";
    public static final String ACT_STEP_LIST = "ACT_STEP_LIST";
    public static final String STEP_ATTACHED_IN_EXTRAS = "STEP_ATTACHED_IN_EXTRAS";
    public static final String ACT_ADD_NEW_GOAL = "ACT_ADD_NEW_GOAL";
    public static final String MSG_ENTER_STEP_NAME = "Please Enter Step Name";
    public static final String STEP_ARRAY_LIST = "STEP_ARRAY_LIST";
    public static final String TIMEBOX_OBJECT = "TIMEBOX_OBJECT";
    public static final String TIMEBOX_ATTACHED_IN_EXTRAS = "TIMEBOX_ATTACHED_IN_EXTRAS";
    public static final String ACT_TIMEBOX_LIST = "ACT_TIMEBOX_LIST";
    public static final String MSG_WALLPAPER_SET_SUCCESFULLY = "Wallpaper Set";
    public static final String WALLPAPER_RESOURCE_ID = "WALLPAPER_RESOURCE_ID";
    public static final String OPERATION = "OPERATION";
    public static final String OPERATION_ADD = "OPERATION_ADD";
    public static final String NICKNAME_UNPLANNED_TIMEBOX = "Unplanned";
    public static final String MSG_CANT_EDIT_DELETE_TIMEBOX = "Unplanned TimeBox can not be edited or deleted.";
    public static final String NICKNAME_STRETCH_GOAL = "Stretch Goal";
    public static final String MSG_CANT_EDIT_DELETE_GOAL = "Stretch Goal can not be edited or deleted.";
    public static final String FILTER_SCOPE = "scope";
    public static final String SCOPE_TODAY = "TODAY";
    public static final String SCOPE_THIS_WEEK = "THIS_WEEK";
    public static final String SCOPE_THIS_MONTH = "THIS_MONTH";
    public static final String SCOPE_THIS_QUARTER = "THIS_QUARTER";
    public static final String SCOPE_THIS_YEAR = "THIS_YEAR";
    public static final String STEP_OBJECT = "STEP_OBJECT";
    public static final String PENDING_STEP_TYPE_SINGLE_STEP = "Single Step";
    public static final String PENDING_STEP_TYPE_SERIES_STEP = "Series";
    public static final String PENDING_STEP_PRIORITY_TOP_MOST = "Top-most";
    public static final String PENDING_STEP_PRIORITY_BOTTOM_MOST = "Bottom-most";
    public static final String PENDING_STEP_PRIORITY_CHANGE_MANUALY = "Change Manually";
    public static final String ALARM_SERVICE = "ALARM_SERVICE";
    public static final String ALARM_SCHEDULER ="ALARM_SCHEDULER" ;
    public static final String IS_CALENDAR_INITIALIZED = "IS_CALENDAR_INITIALIZED";
    public static final String MSG_INITIALIZING_CALENDAR = "Initializing Calendar, Please Wait";
    public static final String OPTION_FROM_ACTQUICKSTART_SELECTED = "OPTION_FROM_ACTQUICKSTART_SELECTED";
    public static final String MSG_INITIALIZING_QUICK_START = "Please Wait...";
    public static final String TEXT_PRIORITY_SPINNER_TOP_MOST ="Top-most" ;
    public static final String TEXT_PRIORITY_SPINNER_BOTTOM_MOST ="Bottom-most" ;
    public static final String TEXT_PRIORITY_SPINNER_CHANGE_MANUALLY ="Change Manually" ;
    public static final String ADD_NEW_GOAL_SPINNER_ITEM = "Add new goal";
    public static final String GOAL_CREATED = "GOAL_CREATED";
    public static final String TIMEBOX_CREATED = "TIMEBOX_CREATED";
    public static final int RESULTCODE_OF_ACT_STEP_LIST = 1;
    public static final int RESULTCODE_OF_ACT_ADD_GOAL = 2;
    public static final int RESULTCODE_OF_ACT_ADD_TIMEBOX = 3;
    public static final int RESULTCODE_ACT_SETTINGS_CHANGE_WALLPAPER = 21;
    public static final int REQUEST_CODE_ACT_ACT_ADD_NEW_STEP = 1;
    public static final int REQUEST_CODE_ACT_ACT_ADD_NEW_GOAL = 2;
    public static final String ID_UNPLANNED_TIMEBOX ="Unplanned TimeBoxId" ;
    public static final String ID_STRETCH_GOAL ="Stretch GoalId" ;
    public static final String MSG_RESET_YODA = "Resetting App will erase all the data! Are you sure, you want to delete all alarms and start over again?";
    public static final String MSG_RESETTING_YODA = "Resetting App, Please wait...";
    public static final int COLORCODE_UNPLANNED_TIMEBOX = -3931905;
}
