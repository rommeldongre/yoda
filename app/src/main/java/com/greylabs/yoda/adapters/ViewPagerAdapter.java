package com.greylabs.yoda.adapters;
import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ViewPagerAdapter extends PagerAdapter {

    Activity activity;
    int [] imagesArraList;

    public ViewPagerAdapter(Activity act, int [] newImagesArrayList) {
        imagesArraList = newImagesArrayList;
        activity = act;
    }

    public int getCount() {
        return imagesArraList.length;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public Object instantiateItem(View collection, int position) {
        ImageView view = new ImageView(activity);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        view.setScaleType(ScaleType.FIT_XY);
//        view.setBackgroundResource(imagesArraList.get(position));
//        view.setImageBitmap(BitmapUtility.decodeSampledBitmapFromResource(imagesArraList[position], 600, 400));
        view.setImageResource(imagesArraList[position]);
        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}