package com.greylabs.ydo.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class TouchCheckBox extends View {

    private Paint mCirclePaint;
    private Paint mCorrectPaint;
    private int radius;
    private int width, height;
    private int cx, cy;
    private float[] points = new float[6];
    private float correctProgress;
    private float downY;
    private boolean isChecked;
    private boolean toggle;
    private boolean isAnim;

    private OnCheckedChangeListener listener;
    private int unCheckColor = Color.GRAY;
    private int circleColor = Color.RED;

    public TouchCheckBox(Context context) {
        this(context, null);
    }

    public TouchCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TouchCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStrokeWidth(dip2px(context, 2));
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mCorrectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCorrectPaint.setColor(circleColor);
        mCorrectPaint.setStyle(Paint.Style.FILL);
        mCorrectPaint.setStrokeWidth(dip2px(context, 2));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    hideCorrect();
                } else {
                    showCheck();
                }
            }
        });
    }

    public void setChecked(boolean checked){
        if (isChecked && !checked) {
            hideCorrect();
        } else if(!isChecked && checked) {
            showCheck();
        }
    }

    public boolean isChecked(){
        return isChecked;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        oldw=w;
        oldh=h;

//        height = width = Math.min(oldw - getPaddingLeft() - getPaddingRight(),oldh - getPaddingBottom() - getPaddingTop());
        height = width = Math.min(oldw,oldh);
        cx = oldw / 2;
        cy = oldh / 2;

        float r = height / 2f;
        points[0] = r / 2f + getPaddingLeft();
        points[1] = r + getPaddingTop();
        points[2] = r * 5f / 6f + getPaddingLeft();
        points[3] = r + r / 3f + getPaddingTop();
        points[4] = r * 1.5f +getPaddingLeft();
        points[5] = r - r / 3f + getPaddingTop();
        radius = (int)((height/2) - mCirclePaint.getStrokeWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCirclePaint.setColor(circleColor);
        mCorrectPaint.setColor(circleColor);
        canvas.drawCircle(cx, cy, radius, mCirclePaint);

        if(correctProgress>0) {
            float x = points[2] + (points[4] - points[2]) * correctProgress;
            float y = points[3] + (points[5] - points[3]) * correctProgress;

            canvas.drawLine(points[0], points[1], points[2], points[3], mCorrectPaint);
            canvas.drawLine(points[2], points[3], x - mCirclePaint.getStrokeWidth(), y + mCirclePaint.getStrokeWidth(), mCorrectPaint);
        }
    }

    public void setCircleColor(int color){
        circleColor = color;
    }

    public void setCorrectColor(int color){
        mCorrectPaint.setColor(color);
    }

    public void setUnCheckColor(int color){
        unCheckColor = color;
    }

    private void showUnChecked() {
        if (isAnim) {
            return;
        }
        isAnim = true;
        isChecked = false;
        isAnim = false;
        if(listener!=null){
            listener.onCheckedChanged(TouchCheckBox.this,false);
        }
        invalidate();
    }

    private void showCheck() {
        if (isAnim) {
            return;
        }
        isAnim = true;
        isChecked = true;
        isAnim = false;
        if(listener!=null){
            listener.onCheckedChanged(TouchCheckBox.this,true);
        }
        showCorrect();
        invalidate();
    }

    private void showCorrect() {
        if (isAnim) {
            return;
        }
        isAnim = true;
        correctProgress = 1;
        invalidate();
        isAnim = false;
    }
    private void hideCorrect() {
        if (isAnim) {
            return;
        }
        isAnim = true;
        correctProgress=0;
        invalidate();
        isAnim = false;
        showUnChecked();
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
        this.listener = listener;
    }
    public interface OnCheckedChangeListener{
        void onCheckedChanged(View buttonView, boolean isChecked);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}