package com.greylabs.yoda.activities;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.greylabs.yoda.R;
import com.greylabs.yoda.utils.BitmapUtility;
import com.greylabs.yoda.utils.Constants;
import com.greylabs.yoda.utils.Logger;

public class ActSettingChangeWallpaper extends AppCompatActivity implements View.OnClickListener {
    private boolean touch = false;
//    private Toolbar toolbar;
    Button btnSetWallpaper;
    private RelativeLayout rlActSettingChangeWallpaper;
    private ImageView ivBackground;
    private LinearLayout llActSettingChangeWallpaper;
    private int[] drawables = new int[]{
            R.drawable.wallpaper1,
            R.drawable.wallpaper2,
            R.drawable.wallpaper3,
            R.drawable.wallpaper4,
            R.drawable.wallpaper5,
            R.drawable.wallpaper6,
            R.drawable.wallpaper7,
            R.drawable.wallpaper8,
            R.drawable.wallpaper9
    };
    private ImageObject[] imageObjects;
    private ImageObject currentImageObject;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_setting_change_wallpaper);
//        toolbar = (Toolbar)findViewById(R.id.toolbarActSettingChangeWallpaper);
        rlActSettingChangeWallpaper = (RelativeLayout)findViewById(R.id.rlActSettingsChangeWallpaper);
        ivBackground = (ImageView)findViewById(R.id.ivActSettingsChangeWallpaper);
        llActSettingChangeWallpaper = (LinearLayout)findViewById(R.id.llActSettingsChangeWallpaper);
        btnSetWallpaper = (Button) findViewById(R.id.btnSetWallpaperActSettingsChangeWallpaper);
        btnSetWallpaper.setOnClickListener(this);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        drawables = getIntent().getIntArrayExtra("drawables");

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
                        llActSettingChangeWallpaper.setVisibility(View.VISIBLE);
                        touch = false;
                    }
                    else {
                        llActSettingChangeWallpaper.setVisibility(View.GONE);
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
        Logger.showMsg(this, Constants.MSG_WALLPAPER_SET_SUCCESFULLY);
        finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_android_gallery_view, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_done) {
//            if(currentImageObject == null)
//                currentImageObject = imageObjects[0];
//            Intent intent = new Intent();
//            intent.putExtra("drawableResourceId",currentImageObject.resId);
//            setResult(Activity.RESULT_OK, intent);
//            finish();//finishing activity
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private class ImageObject{
        public Bitmap bitmap;
        public int resId;
    }
}