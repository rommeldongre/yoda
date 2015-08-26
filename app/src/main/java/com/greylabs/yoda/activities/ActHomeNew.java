package com.greylabs.yoda.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.greylabs.yoda.R;


public class ActHomeNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // full screen Activity
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home_new);
    }
}