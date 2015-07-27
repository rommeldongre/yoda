package com.greylabs.yoda.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.greylabs.yoda.R;
import com.greylabs.yoda.threads.InitCalendarAsyncTask;
import com.greylabs.yoda.utils.Prefs;

public class ActSplashScreen extends Activity{

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
        prefs = Prefs.getInstance(this);
        StartAnimations();
        iv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!prefs.isCalendarInitialized() && !prefs.isOptionFromActQuickStartSelected()){
                    startActivity(new Intent(ActSplashScreen.this, ActQuickStart.class));
                    ActSplashScreen.this.finish();
                }else if (!prefs.isCalendarInitialized()) {
                    new InitCalendarAsyncTask(ActSplashScreen.this, new MyHandler()).execute();
                }else {
                    startActivity(new Intent(ActSplashScreen.this, ActHome.class));
                    ActSplashScreen.this.finish();
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
            startActivity(new Intent(ActSplashScreen.this, ActQuickStart.class));
            ActSplashScreen.this.finish();
        }
    }
}