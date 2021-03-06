package com.greylabs.ydo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.github.lzyzsd.circleprogress.Utils;
import com.greylabs.ydo.R;

public class MyArcProgress extends View {
    private float myLeft, myTop, myRight, myBottom;
    float padding;
    private Paint paint;
    protected Paint textPaint;
    private RectF rectF;
    private RectF arcRectF;
    private float strokeWidth;
    private float suffixTextSize;
    private float bottomTextSize;
    private String bottomText;
    private float textSize;
    private int textColor;
    private int progress;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private int backgroundCircleColor;
    private int dividerColor;
    private float arcAngle;
    private String suffixText;
    private String stepName;
    private String goalName;
    private float suffixTextPadding;
    private float arcBottomHeight;
    private final int default_finished_color;
    private final int default_unfinished_color;
    private final int default_text_color;
    private final float default_suffix_text_size;
    private final float default_suffix_padding;
    private final float default_bottom_text_size;
    private final float default_stroke_width;
    private final String default_suffix_text;
    private final int default_max;
    private final float default_arc_angle;
    private float default_text_size;
    private final int min_size;
//    private static final String INSTANCE_STATE = "saved_instance";
//    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
//    private static final String INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size";
//    private static final String INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding";
//    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
//    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
//    private static final String INSTANCE_TEXT_SIZE = "text_size";
//    private static final String INSTANCE_TEXT_COLOR = "text_color";
//    private static final String INSTANCE_PROGRESS = "progress";
//    private static final String INSTANCE_MAX = "max";
//    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
//    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
//    private static final String INSTANCE_ARC_ANGLE = "arc_angle";
//    private static final String INSTANCE_SUFFIX = "suffix";

    public MyArcProgress(Context context) {
        this(context, (AttributeSet) null);
    }

    public MyArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.rectF = new RectF();
        this.arcRectF = new RectF();
        this.progress = 0;
        this.suffixText = "%";
        this.default_finished_color = -1;
        this.default_unfinished_color = Color.rgb(72, 106, 176);
        this.backgroundCircleColor = getResources().getColor(R.color.dark_transperent);
        this.default_text_color = Color.rgb(66, 145, 241);
        this.default_max = 100;
        this.default_arc_angle = 288.0F;
        this.default_text_size = Utils.sp2px(this.getResources(), 18.0F);
        this.min_size = (int) Utils.dp2px(this.getResources(), 100.0F);
        this.default_text_size = Utils.sp2px(this.getResources(), 40.0F);
        this.default_suffix_text_size = Utils.sp2px(this.getResources(), 15.0F);
        this.default_suffix_padding = Utils.dp2px(this.getResources(), 4.0F);
        this.default_suffix_text = "%";
        this.default_bottom_text_size = Utils.sp2px(this.getResources(), 10.0F);
        this.default_stroke_width = Utils.dp2px(this.getResources(), 4.0F);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, com.github.lzyzsd.circleprogress.R.styleable.ArcProgress, defStyleAttr, 0);
        this.initByAttributes(attributes);
        attributes.recycle();
        this.initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        this.finishedStrokeColor = attributes.getColor(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_finished_color, -1);
        this.unfinishedStrokeColor = attributes.getColor(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_unfinished_color, this.default_unfinished_color);
        this.textColor = attributes.getColor(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_text_color, this.default_text_color);
        this.textSize = attributes.getDimension(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_text_size, this.default_text_size);
        this.arcAngle = attributes.getDimension(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_angle, 288.0F);
        this.setMax(attributes.getInt(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_max, 100));
        this.setProgress(attributes.getInt(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_progress, 0));
        this.strokeWidth = attributes.getDimension(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_stroke_width, this.default_stroke_width);
        this.suffixTextSize = attributes.getDimension(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_suffix_text_size, this.default_suffix_text_size);
        this.suffixText = TextUtils.isEmpty(attributes.getString(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_suffix_text)) ? this.default_suffix_text : attributes.getString(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_suffix_text);
        this.suffixTextPadding = attributes.getDimension(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_suffix_text_padding, this.default_suffix_padding);
        this.bottomTextSize = attributes.getDimension(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_bottom_text_size, this.default_bottom_text_size);
        this.bottomText = attributes.getString(com.github.lzyzsd.circleprogress.R.styleable.ArcProgress_arc_bottom_text);
    }

    protected void initPainters() {
        this.textPaint = new TextPaint();
        this.textPaint.setColor(this.textColor);
        this.textPaint.setTextSize(this.textSize);
        this.textPaint.setAntiAlias(true);
        this.paint = new Paint();
        this.paint.setColor(this.default_unfinished_color);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(this.strokeWidth);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void invalidate() {
        this.initPainters();
        super.invalidate();
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getSuffixTextSize() {
        return this.suffixTextSize;
    }

    public void setSuffixTextSize(float suffixTextSize) {
        this.suffixTextSize = suffixTextSize;
        this.invalidate();
    }

    public String getBottomText() {
        return this.bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > this.getMax()) {
            this.progress %= this.getMax();
        }

        this.invalidate();
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            this.invalidate();
        }

    }

    public float getBottomTextSize() {
        return this.bottomTextSize;
    }

    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getFinishedStrokeColor() {
        return this.finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    public int getUnfinishedStrokeColor() {
        return this.unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    public float getArcAngle() {
        return this.arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    public String getSuffixText() {
        return this.suffixText;
    }

    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    public float getSuffixTextPadding() {
        return this.suffixTextPadding;
    }

    public void setSuffixTextPadding(float suffixTextPadding) {
        this.suffixTextPadding = suffixTextPadding;
        this.invalidate();
    }

    protected int getSuggestedMinimumHeight() {
        return this.min_size;
    }

    protected int getSuggestedMinimumWidth() {
        return this.min_size;
    }

    public int getBackgroundCircleColor() {
        return backgroundCircleColor;
    }

    public void setBackgroundCircleColor(int backgroundCircleColor) {
        this.backgroundCircleColor = backgroundCircleColor;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        this.rectF.set(this.strokeWidth / 2.0F, this.strokeWidth / 2.0F, (float)MeasureSpec.getSize(widthMeasureSpec) - this.strokeWidth / 2.0F, (float)MeasureSpec.getSize(heightMeasureSpec) - this.strokeWidth / 2.0F);
//        padding = strokeWidth/2;
//        this.rectF.set((this.strokeWidth / 2.0F)+padding ,( this.strokeWidth / 2.0F)+padding, ((float)MeasureSpec.getSize(widthMeasureSpec) - this.strokeWidth / 2.0F)-padding, ((float)MeasureSpec.getSize(heightMeasureSpec) - this.strokeWidth / 2.0F)-padding);
//        this.arcRectF.set(this.rectF.left+padding, rectF.top, this.rectF.right - padding, rectF.bottom);
//        float radius = (float)this.getWidth() / 2.0F;
//        float angle = (360.0F - this.arcAngle) / 2.0F;
//        this.arcBottomHeight = radius * (float)(1.0D - Math.cos((double)(angle / 180.0F) * 3.141592653589793D));
//        this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//    }
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        float startAngle = 270.0F - this.arcAngle / 2.0F;
//        float finishedSweepAngle = (float) this.progress / (float) this.getMax() * this.arcAngle;
//
//        // background circle
//        Paint paintBackgroundCircle = new Paint();
//        paintBackgroundCircle.setColor(this.backgroundCircleColor);
//        canvas.drawCircle((float) this.getWidth() / 2.0F, (float) this.getHeight() / 2.0F, (float) this.getHeight() / 2.0F, paintBackgroundCircle);
//
//        // divider line
//        Paint paintLine = new Paint();
//        paintLine.setColor(this.dividerColor);
//        paintLine.setStrokeWidth(2);
//        canvas.drawLine(this.arcRectF.left + this.getStrokeWidth(), this.arcRectF.centerY(), this.arcRectF.right - this.getStrokeWidth(), this.arcRectF.centerY(), paintLine);
//
//        // two arcs
//        this.paint.setColor(this.unfinishedStrokeColor);
//        this.paint.setStrokeCap(Paint.Cap.BUTT);
//        canvas.drawArc(this.arcRectF, startAngle, this.arcAngle, false, this.paint);
//        this.paint.setColor(this.finishedStrokeColor);
//        canvas.drawArc(this.arcRectF, startAngle, finishedSweepAngle, false, this.paint);
//
//        float bottomTextBaseline;
//        if (!TextUtils.isEmpty(stepName)) {
//            this.textPaint.setColor(this.textColor);
//            this.textPaint.setTextSize(this.textSize);
//            bottomTextBaseline = this.textPaint.descent() + this.textPaint.ascent();
//            float textBaseline = ((float) this.getHeight() - bottomTextBaseline) / 3.0F;
//            canvas.drawText(stepName, ((float) this.getWidth() - this.textPaint.measureText(stepName)) / 2.0F, textBaseline, this.textPaint);
//            textBaseline = ((float) this.getHeight() - bottomTextBaseline) / 1.5F;
//            if (goalName == null)
//                goalName = "";
//            canvas.drawText(goalName, ((float) this.getWidth() - this.textPaint.measureText(goalName)) / 2.0F, textBaseline, this.textPaint);
//        }
//
//        if (!TextUtils.isEmpty(this.getBottomText())) {
//            this.textPaint.setTextSize(this.bottomTextSize);
//            bottomTextBaseline = (float) this.getHeight() - this.arcBottomHeight - (this.textPaint.descent() + this.textPaint.ascent()) / 2.0F;
//            canvas.drawText(this.getBottomText(), ((float) this.getWidth() - this.textPaint.measureText(this.getBottomText())) / 2.0F, bottomTextBaseline, this.textPaint);
//        }
//
//    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        padding = strokeWidth;
        myLeft = this.strokeWidth / 2;
        myRight = ((float) MeasureSpec.getSize(widthMeasureSpec) - this.strokeWidth / 2);
        float height = (float) MeasureSpec.getSize(heightMeasureSpec);
        myTop = (height / 2) - ((myRight - myLeft) / 2);
        myBottom = (height / 2) + ((myRight - myLeft) / 2);
        this.rectF.set(myLeft, myTop, myRight, myBottom);
        this.arcRectF.set(myLeft + padding, myTop + padding, myRight - padding, myBottom - padding);
        this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF myArcRect = new RectF();
        myArcRect.left = rectF.left + padding;
        myArcRect.top = rectF.top + padding;
        myArcRect.right = rectF.right - padding;
        myArcRect.bottom = rectF.bottom - padding;

        // background circle
        Paint paintBackgroundCircle = new Paint();
        paintBackgroundCircle.setColor(this.backgroundCircleColor);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), (myBottom - myTop) / 2.0F, paintBackgroundCircle);

        // two arcs
        this.paint.setColor(this.unfinishedStrokeColor);
        this.paint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawArc(myArcRect, 120, 300, false, this.paint);
        this.paint.setColor(this.finishedStrokeColor);

        float progressAngle = (float) this.progress * 3;

        canvas.drawArc(myArcRect, 120, progressAngle, false, this.paint);

        float bottomTextBaseline;
        if (!TextUtils.isEmpty(stepName)) {
            this.textPaint.setColor(this.textColor);
            this.textPaint.setTextSize(this.textSize);
            bottomTextBaseline = this.textPaint.descent() + this.textPaint.ascent();
            float textBaseline = (rectF.bottom * 1 / 2) - bottomTextBaseline;
            canvas.drawText(stepName, rectF.centerX() - (this.textPaint.measureText(stepName) / 2.0F), textBaseline, this.textPaint);
            textBaseline = (rectF.bottom * 3 / 5) - bottomTextBaseline;
            if (goalName == null)
                goalName = "";
            // for goal change the font to italics
            this.textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
            canvas.drawText(goalName, rectF.centerX() - (this.textPaint.measureText(goalName) / 2.0F), textBaseline, this.textPaint);
        }

        if (!TextUtils.isEmpty(this.getBottomText())) {
            // change back the typeface to normal
            this.textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            this.textPaint.setTextSize(this.textSize - 10);
            bottomTextBaseline = this.textPaint.descent() + this.textPaint.ascent();
            float textBaseline = (rectF.bottom * 6 / 7) - bottomTextBaseline;
            canvas.drawText("Steps Remaining", rectF.centerX() - (this.textPaint.measureText("Steps Remaining") / 2.0F), textBaseline, this.textPaint);

            this.textPaint.setTextSize(this.bottomTextSize);
            bottomTextBaseline = (rectF.bottom - 2 * strokeWidth) - ((this.textPaint.descent() + this.textPaint.ascent()) / 2.0F);
            canvas.drawText(this.getBottomText(), rectF.centerX() - (this.textPaint.measureText(this.getBottomText()) / 2.0F), bottomTextBaseline, this.textPaint);
        }
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("saved_instance", super.onSaveInstanceState());
        bundle.putFloat("stroke_width", this.getStrokeWidth());
        bundle.putFloat("suffix_text_size", this.getSuffixTextSize());
        bundle.putFloat("suffix_text_padding", this.getSuffixTextPadding());
        bundle.putFloat("bottom_text_size", this.getBottomTextSize());
        bundle.putString("bottom_text", this.getBottomText());
        bundle.putFloat("text_size", this.getTextSize());
        bundle.putInt("text_color", this.getTextColor());
        bundle.putInt("progress", this.getProgress());
        bundle.putInt("max", this.getMax());
        bundle.putInt("finished_stroke_color", this.getFinishedStrokeColor());
        bundle.putInt("unfinished_stroke_color", this.getUnfinishedStrokeColor());
        bundle.putFloat("arc_angle", this.getArcAngle());
        bundle.putString("suffix", this.getSuffixText());
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.strokeWidth = bundle.getFloat("stroke_width");
            this.suffixTextSize = bundle.getFloat("suffix_text_size");
            this.suffixTextPadding = bundle.getFloat("suffix_text_padding");
            this.bottomTextSize = bundle.getFloat("bottom_text_size");
            this.bottomText = bundle.getString("bottom_text");
            this.textSize = bundle.getFloat("text_size");
            this.textColor = bundle.getInt("text_color");
            this.setMax(bundle.getInt("max"));
            this.setProgress(bundle.getInt("progress"));
            this.finishedStrokeColor = bundle.getInt("finished_stroke_color");
            this.unfinishedStrokeColor = bundle.getInt("unfinished_stroke_color");
            this.suffixText = bundle.getString("suffix");
            this.initPainters();
            super.onRestoreInstanceState(bundle.getParcelable("saved_instance"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}