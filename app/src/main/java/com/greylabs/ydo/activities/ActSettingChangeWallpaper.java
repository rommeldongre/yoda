package com.greylabs.ydo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.greylabs.ydo.R;
import com.greylabs.ydo.utils.BitmapUtility;
import com.greylabs.ydo.utils.Constants;
import com.greylabs.ydo.utils.Logger;
import com.greylabs.ydo.utils.Prefs;

public class ActSettingChangeWallpaper extends AppCompatActivity implements View.OnClickListener {
    private boolean touch = false;
    Button btnSetWallpaper;
    private RelativeLayout rlActSettingChangeWallpaper;
    private ImageView ivBackground;
    private LinearLayout llActSettingChangeWallpaper;
    private HorizontalScrollView horizontalScrollView;
    private int[] drawables = new int[]{
            R.drawable.wallpaper1,
            R.drawable.wallpaper2,
            R.drawable.wallpaper3,
            R.drawable.wallpaper4,
            R.drawable.wallpaper5
    };
    private ImageObject[] imageObjects;
    private ImageObject currentImageObject;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen Activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_change_wallpaper);
        rlActSettingChangeWallpaper = (RelativeLayout)findViewById(R.id.rlActSettingsChangeWallpaper);
        ivBackground = (ImageView)findViewById(R.id.ivActSettingsChangeWallpaper);
        llActSettingChangeWallpaper = (LinearLayout)findViewById(R.id.llActSettingsChangeWallpaper);
        btnSetWallpaper = (Button) findViewById(R.id.btnSetWallpaperActSettingsChangeWallpaper);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsvActSettingsChangeWallpaper);
        btnSetWallpaper.setOnClickListener(this);

        imageObjects = new ImageObject[drawables.length];
        for(int i=0;i<imageObjects.length;i++)
            imageObjects[i] = new ImageObject();
        for (int d=0;d<drawables.length;d++){
            ImageView imageView = new ImageView(this);
            Bitmap bm = BitmapUtility.decodeSampledBitmapFromResource(getResources(), drawables[d], 300, 500);
            imageObjects[d].bitmap = bm;
            imageObjects[d].resId = drawables[d];
            imageView.setTag(imageObjects[d]);
            imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bm, 150, 150));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageObject imageObject = (ImageObject)v.getTag();
                    ivBackground.setImageBitmap(imageObject.bitmap);
                    currentImageObject = imageObject;
                }
            });
            llActSettingChangeWallpaper.addView(imageView);
            rlActSettingChangeWallpaper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(touch) {
//                        llActSettingChangeWallpaper.setVisibility(View.VISIBLE);
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        touch = false;
                    }
                    else {
//                        llActSettingChangeWallpaper.setVisibility(View.GONE);
                        horizontalScrollView.setVisibility(View.GONE);
                        touch = true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        // set wallpaper resource value into prefs here
        Prefs prefs = Prefs.getInstance(this);
        if(currentImageObject == null)
            currentImageObject=imageObjects[0];
        prefs.setWallpaperResourceId(currentImageObject.resId);
        Intent intent=new Intent();
        setResult(Constants.RESULTCODE_ACT_SETTINGS_CHANGE_WALLPAPER, intent);
        Logger.showMsg(this, Constants.MSG_WALLPAPER_SET_SUCCESFULLY);
        finish();
    }

    private class ImageObject{
        public Bitmap bitmap;
        public int resId;
    }
}