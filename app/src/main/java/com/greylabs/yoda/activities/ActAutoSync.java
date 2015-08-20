package com.greylabs.yoda.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.greylabs.yoda.R;
import com.greylabs.yoda.utils.Logger;
import com.greylabs.yoda.views.TouchCheckBox;

public class ActAutoSync extends Activity implements TouchCheckBox.OnCheckedChangeListener {

    TouchCheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sync);
        initialize();
    }

    private void initialize() {
        checkBox = (TouchCheckBox) findViewById(R.id.cbActAutoSync);

        checkBox.setOnCheckedChangeListener(this);
        checkBox.setCircleColor(Color.CYAN);
        checkBox.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onCheckedChanged(View buttonView, boolean isChecked) {
        if(isChecked){
            Logger.showMsg(this, "checked");
        }else {
            Logger.showMsg(this, "unchecked");
        }
    }
}
