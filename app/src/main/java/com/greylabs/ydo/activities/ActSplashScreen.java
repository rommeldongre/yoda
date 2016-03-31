package com.greylabs.ydo.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.greylabs.ydo.R;
import com.greylabs.ydo.threads.CalendarUpdateAsyncThread;
import com.greylabs.ydo.threads.InitCalendarAsyncTask;
import com.greylabs.ydo.utils.Prefs;

public class ActSplashScreen extends AppCompatActivity {
    private static final String TAG="ActSplashScreen";
    RelativeLayout rl;
    ImageView iv;
    Prefs prefs;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initialize();
    }

    private void initialize() {
        rl = (RelativeLayout) findViewById(R.id.rlActSplashScreen);
        iv = (ImageView) findViewById(R.id.ivActSplashScreen);

//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
//        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//        int logoWidth = Math.round(dpWidth);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(logoWidth, logoWidth);
//        iv.setLayoutParams(layoutParams);

        prefs = Prefs.getInstance(this);
//        StartAnimations();
        iv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefs.isCalendarInitialized()) {
                    new CalendarUpdateAsyncThread(ActSplashScreen.this,new CalendarUpdateHandler()).execute();
                } else if (!prefs.isCalendarInitialized()) {
                    new InitCalendarAsyncTask(ActSplashScreen.this, new MyHandler()).execute();
                }

                if(prefs.isCalendarInitialized()){
//                    new CalendarUpdateAsyncThread(ActSplashScreen.this,new CalendarUpdateHandler()).execute();
//                    Logger.d(TAG, "Checking Calendar State");
//                    Day day=new Day(ActSplashScreen.this);
//                    if(CalendarUtils.compareOnlyDates(day.getFirstDay(), new Date())==false) {
//                        YodaCalendar yodaCalendar = new YodaCalendar(ActSplashScreen.this);
//                        yodaCalendar.updateCalendar();
//                        Logger.d(TAG, "Calendar updated success");
//                    }else{
//                        Logger.d(TAG,"Calendar up to date");
//                    }
                }
            }
        }, 3000);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        rl.clearAnimation();
        rl.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        iv.clearAnimation();
        iv.startAnimation(anim);
    }



    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            prefs.setCalendarInitialized(true);
            checkIfOptionFromActQuickStartSelected();
        }
    }

    class CalendarUpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            checkIfOptionFromActQuickStartSelected();
            ActSplashScreen.this.finish();
        }
    }
    private void checkIfOptionFromActQuickStartSelected() {
        if(prefs.isOptionFromActQuickStartSelected()) {
            startActivity(new Intent(ActSplashScreen.this, ActHome.class));
            ActSplashScreen.this.finish();
        }else {
            startActivity(new Intent(ActSplashScreen.this, ActQuickStart.class));
            ActSplashScreen.this.finish();
        }
    }
}