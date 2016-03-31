package com.greylabs.ydo.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greylabs.ydo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MyFloatingActionButton extends ImageButton {
    public static final int SIZE_NORMAL = 0;
    public static final int SIZE_MINI = 1;
    int mColorNormal;
    int mColorPressed;
    int mColorDisabled;
    String mTitle;
    private int mIcon;
    private Drawable mIconDrawable;
    private int mSize;
    private float mCircleSize;
    private float mShadowRadius;
    private float mShadowOffset;
    private int mDrawableSize;
    boolean mStrokeVisible;

    public MyFloatingActionButton(Context context) {
        this(context, (AttributeSet)null);
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs);
    }

    void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton, 0, 0);
        this.mColorNormal = attr.getColor(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton_fab_colorNormal, this.getColor(net.i2p.android.ext.floatingactionbutton.R.color.default_normal));
        this.mColorPressed = attr.getColor(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton_fab_colorPressed, this.getColor(net.i2p.android.ext.floatingactionbutton.R.color.default_pressed));
        this.mColorDisabled = attr.getColor(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton_fab_colorDisabled, this.getColor(net.i2p.android.ext.floatingactionbutton.R.color.default_disabled));
        this.mSize = attr.getInt(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton_fab_size, 0);
        this.mIcon = attr.getResourceId(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton_fab_icon, 0);
        this.mTitle = attr.getString(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton_fab_title);
        this.mStrokeVisible = attr.getBoolean(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionButton_fab_stroke_visible, true);
        attr.recycle();
        this.updateCircleSize();
        this.mShadowRadius = this.getDimension(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_shadow_radius);
        this.mShadowOffset = this.getDimension(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_shadow_offset);
        this.updateDrawableSize();
        this.updateBackground();
    }

    private void updateDrawableSize() {
        this.mDrawableSize = (int)(this.mCircleSize + 2.0F * this.mShadowRadius);
    }

    private void updateCircleSize() {
        this.mCircleSize = this.getDimension(this.mSize == 0? net.i2p.android.ext.floatingactionbutton.R.dimen.fab_size_normal: net.i2p.android.ext.floatingactionbutton.R.dimen.fab_size_mini);
    }

    public void setSize(int size) {
        if(size != 1 && size != 0) {
            throw new IllegalArgumentException("Use @FAB_SIZE constants only!");
        } else {
            if(this.mSize != size) {
                this.mSize = size;
                this.updateCircleSize();
                this.updateDrawableSize();
                this.updateBackground();
            }

        }
    }

    public int getSize() {
        return this.mSize;
    }

    public void setIcon(int icon) {
        if(this.mIcon != icon) {
            this.mIcon = icon;
            this.mIconDrawable = null;
            this.updateBackground();
        }

    }

    public void setIconDrawable(@NonNull Drawable iconDrawable) {
        if(this.mIconDrawable != iconDrawable) {
            this.mIcon = 0;
            this.mIconDrawable = iconDrawable;
            this.updateBackground();
        }

    }

    public int getColorNormal() {
        return this.mColorNormal;
    }

    public void setColorNormalResId(int colorNormal) {
        this.setColorNormal(this.getColor(colorNormal));
    }

    public void setColorNormal(int color) {
        if(this.mColorNormal != color) {
            this.mColorNormal = color;
            this.updateBackground();
        }

    }

    public int getColorPressed() {
        return this.mColorPressed;
    }

    public void setColorPressedResId(int colorPressed) {
        this.setColorPressed(this.getColor(colorPressed));
    }

    public void setColorPressed(int color) {
        if(this.mColorPressed != color) {
            this.mColorPressed = color;
            this.updateBackground();
        }

    }

    public int getColorDisabled() {
        return this.mColorDisabled;
    }

    public void setColorDisabledResId(int colorDisabled) {
        this.setColorDisabled(this.getColor(colorDisabled));
    }

    public void setColorDisabled(int color) {
        if(this.mColorDisabled != color) {
            this.mColorDisabled = color;
            this.updateBackground();
        }

    }

    public void setStrokeVisible(boolean visible) {
        if(this.mStrokeVisible != visible) {
            this.mStrokeVisible = visible;
            this.updateBackground();
        }

    }

    public boolean isStrokeVisible() {
        return this.mStrokeVisible;
    }

    int getColor(int id) {
        return this.getResources().getColor(id);
    }

    float getDimension(int id) {
        return this.getResources().getDimension(id);
    }

    public void setTitle(String title) {
        this.mTitle = title;
        TextView label = this.getLabelView();
        if(label != null) {
            label.setTextColor(getResources().getColor(R.color.gray));
            label.setText(title);
        }

    }

    TextView getLabelView() {
        return (TextView)this.getTag(net.i2p.android.ext.floatingactionbutton.R.id.fab_label);
    }

    public String getTitle() {
        return this.mTitle;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(this.mDrawableSize, this.mDrawableSize);
    }

    void updateBackground() {
        float strokeWidth = this.getDimension(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_stroke_width);
        float halfStrokeWidth = strokeWidth / 2.0F;
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{this.getResources().getDrawable(this.mSize == 0? net.i2p.android.ext.floatingactionbutton.R.drawable.fab_bg_normal: net.i2p.android.ext.floatingactionbutton.R.drawable.fab_bg_mini), this.createFillDrawable(strokeWidth), this.createOuterStrokeDrawable(strokeWidth), this.getIconDrawable()});
        int iconOffset = (int)(this.mCircleSize - this.getDimension(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_icon_size)) / 2;
        int circleInsetHorizontal = (int)this.mShadowRadius;
        int circleInsetTop = (int)(this.mShadowRadius - this.mShadowOffset);
        int circleInsetBottom = (int)(this.mShadowRadius + this.mShadowOffset);
        layerDrawable.setLayerInset(1, circleInsetHorizontal, circleInsetTop, circleInsetHorizontal, circleInsetBottom);
        layerDrawable.setLayerInset(2, (int)((float)circleInsetHorizontal - halfStrokeWidth), (int)((float)circleInsetTop - halfStrokeWidth), (int)((float)circleInsetHorizontal - halfStrokeWidth), (int)((float)circleInsetBottom - halfStrokeWidth));
        layerDrawable.setLayerInset(3, circleInsetHorizontal + iconOffset, circleInsetTop + iconOffset, circleInsetHorizontal + iconOffset, circleInsetBottom + iconOffset);
        this.setBackgroundCompat(layerDrawable);
    }

    Drawable getIconDrawable() {
        return (Drawable)(this.mIconDrawable != null?this.mIconDrawable:(this.mIcon != 0?this.getResources().getDrawable(this.mIcon):new ColorDrawable(0)));
    }

    private StateListDrawable createFillDrawable(float strokeWidth) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-16842910}, this.createCircleDrawable(this.mColorDisabled, strokeWidth));
        drawable.addState(new int[]{16842919}, this.createCircleDrawable(this.mColorPressed, strokeWidth));
        drawable.addState(new int[0], this.createCircleDrawable(this.mColorNormal, strokeWidth));
        return drawable;
    }

    private Drawable createCircleDrawable(int color, float strokeWidth) {
        int alpha = Color.alpha(color);
        int opaqueColor = this.opaque(color);
        ShapeDrawable fillDrawable = new ShapeDrawable(new OvalShape());
        Paint paint = fillDrawable.getPaint();
        paint.setAntiAlias(true);
        paint.setColor(opaqueColor);
        Drawable[] layers = new Drawable[]{fillDrawable, this.createInnerStrokesDrawable(opaqueColor, strokeWidth)};
        Object drawable = alpha != 255 && this.mStrokeVisible?new MyFloatingActionButton.TranslucentLayerDrawable(alpha, layers):new LayerDrawable(layers);
        int halfStrokeWidth = (int)(strokeWidth / 2.0F);
        ((LayerDrawable)drawable).setLayerInset(1, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth, halfStrokeWidth);
        return (Drawable)drawable;
    }

    private Drawable createOuterStrokeDrawable(float strokeWidth) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        Paint paint = shapeDrawable.getPaint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(-16777216);
        paint.setAlpha(this.opacityToAlpha(0.02F));
        return shapeDrawable;
    }

    private int opacityToAlpha(float opacity) {
        return (int)(255.0F * opacity);
    }

    private int darkenColor(int argb) {
        return this.adjustColorBrightness(argb, 0.9F);
    }

    private int lightenColor(int argb) {
        return this.adjustColorBrightness(argb, 1.1F);
    }

    private int adjustColorBrightness(int argb, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(argb, hsv);
        hsv[2] = Math.min(hsv[2] * factor, 1.0F);
        return Color.HSVToColor(Color.alpha(argb), hsv);
    }

    private int halfTransparent(int argb) {
        return Color.argb(Color.alpha(argb) / 2, Color.red(argb), Color.green(argb), Color.blue(argb));
    }

    private int opaque(int argb) {
        return Color.rgb(Color.red(argb), Color.green(argb), Color.blue(argb));
    }

    private Drawable createInnerStrokesDrawable(final int color, float strokeWidth) {
        if(!this.mStrokeVisible) {
            return new ColorDrawable(0);
        } else {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            final int bottomStrokeColor = this.darkenColor(color);
            final int bottomStrokeColorHalfTransparent = this.halfTransparent(bottomStrokeColor);
            final int topStrokeColor = this.lightenColor(color);
            final int topStrokeColorHalfTransparent = this.halfTransparent(topStrokeColor);
            Paint paint = shapeDrawable.getPaint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.STROKE);
            shapeDrawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {
                public Shader resize(int width, int height) {
                    return new LinearGradient((float)(width / 2), 0.0F, (float)(width / 2), (float)height, new int[]{topStrokeColor, topStrokeColorHalfTransparent, color, bottomStrokeColorHalfTransparent, bottomStrokeColor}, new float[]{0.0F, 0.2F, 0.5F, 0.8F, 1.0F}, Shader.TileMode.CLAMP);
                }
            });
            return shapeDrawable;
        }
    }

    @SuppressLint({"NewApi"})
    private void setBackgroundCompat(Drawable drawable) {
        if(Build.VERSION.SDK_INT >= 16) {
            this.setBackground(drawable);
        } else {
            this.setBackgroundDrawable(drawable);
        }

    }

    public void setVisibility(int visibility) {
        TextView label = this.getLabelView();
        if(label != null) {
            label.setVisibility(visibility);
        }

        super.setVisibility(visibility);
    }

    private static class TranslucentLayerDrawable extends LayerDrawable {
        private final int mAlpha;

        public TranslucentLayerDrawable(int alpha, Drawable... layers) {
            super(layers);
            this.mAlpha = alpha;
        }

        public void draw(Canvas canvas) {
            Rect bounds = this.getBounds();
            canvas.saveLayerAlpha((float)bounds.left, (float)bounds.top, (float)bounds.right, (float)bounds.bottom, this.mAlpha, 31);
            super.draw(canvas);
            canvas.restore();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({0L, 1L})
    public @interface FAB_SIZE {
    }
}
