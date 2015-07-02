package com.greylabs.yoda.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.greylabs.yoda.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;
import net.i2p.android.ext.floatingactionbutton.TouchDelegateGroup;

public class MyFloatingActionsMenu extends ViewGroup {
    public static final int EXPAND_UP = 0;
    public static final int EXPAND_DOWN = 1;
    public static final int EXPAND_LEFT = 2;
    public static final int EXPAND_RIGHT = 3;
    public static final int LABELS_ON_LEFT_SIDE = 0;
    public static final int LABELS_ON_RIGHT_SIDE = 1;
    private static final int ANIMATION_DURATION = 300;
    private static final float COLLAPSED_PLUS_ROTATION = 0.0F;
    private static final float EXPANDED_PLUS_ROTATION = 135.0F;
    private int mAddButtonPlusColor;
    private int mAddButtonColorNormal;
    private int mAddButtonColorPressed;
    private int mAddButtonSize;
    private boolean mAddButtonStrokeVisible;
    private int mExpandDirection;
    private int mButtonSpacing;
    private int mLabelsMargin;
    private int mLabelsVerticalOffset;
    private boolean mExpanded;
    private AnimatorSet mExpandAnimation;
    private AnimatorSet mCollapseAnimation;
    private MyFloatingActionButton mAddButton;
    private MyFloatingActionsMenu.RotatingDrawable mRotatingDrawable;
    private int mMaxButtonWidth;
    private int mMaxButtonHeight;
    private int mLabelsStyle;
    private int mLabelsPosition;
    private int mButtonsCount;
    private TouchDelegateGroup mTouchDelegateGroup;
    private MyFloatingActionsMenu.OnFloatingActionsMenuUpdateListener mListener;
    private static Interpolator sExpandInterpolator = new OvershootInterpolator();
    private static Interpolator sCollapseInterpolator = new DecelerateInterpolator(3.0F);
    private static Interpolator sAlphaExpandInterpolator = new DecelerateInterpolator();

    public MyFloatingActionsMenu(Context context) {
        this(context, (AttributeSet) null);
    }

    public MyFloatingActionsMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mExpandAnimation = (new AnimatorSet()).setDuration(300L);
        this.mCollapseAnimation = (new AnimatorSet()).setDuration(300L);
        this.init(context, attrs);
    }

    public MyFloatingActionsMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mExpandAnimation = (new AnimatorSet()).setDuration(300L);
        this.mCollapseAnimation = (new AnimatorSet()).setDuration(300L);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mButtonSpacing = (int)(this.getResources().getDimension(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_actions_spacing) - this.getResources().getDimension(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_shadow_radius) - this.getResources().getDimension(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_shadow_offset));
        this.mLabelsMargin = this.getResources().getDimensionPixelSize(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_labels_margin);
        this.mLabelsVerticalOffset = this.getResources().getDimensionPixelSize(net.i2p.android.ext.floatingactionbutton.R.dimen.fab_shadow_offset);
        this.mTouchDelegateGroup = new TouchDelegateGroup(this);
        this.setTouchDelegate(this.mTouchDelegateGroup);
        TypedArray attr = context.obtainStyledAttributes(attributeSet, net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu, 0, 0);
        this.mAddButtonPlusColor = attr.getColor(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_addButtonPlusIconColor, this.getColor(17170443));
        this.mAddButtonColorNormal = attr.getColor(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_addButtonColorNormal, this.getColor(net.i2p.android.ext.floatingactionbutton.R.color.default_normal));
        this.mAddButtonColorPressed = attr.getColor(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_addButtonColorPressed, this.getColor(net.i2p.android.ext.floatingactionbutton.R.color.default_pressed));
        this.mAddButtonSize = attr.getInt(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_addButtonSize, 0);
        this.mAddButtonStrokeVisible = attr.getBoolean(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_addButtonStrokeVisible, true);
        this.mExpandDirection = attr.getInt(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_expandDirection, 0);
        this.mLabelsStyle = attr.getResourceId(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_labelStyle, 0);
        this.mLabelsPosition = attr.getInt(net.i2p.android.ext.floatingactionbutton.R.styleable.FloatingActionsMenu_fab_labelsPosition, 0);
        attr.recycle();
        if(this.mLabelsStyle != 0 && this.expandsHorizontally()) {
            throw new IllegalStateException("Action labels in horizontal expand orientation is not supported.");
        } else {
            this.createAddButton(context);
        }
    }

    public void setOnFloatingActionsMenuUpdateListener(MyFloatingActionsMenu.OnFloatingActionsMenuUpdateListener listener) {
        this.mListener = listener;
    }

    private boolean expandsHorizontally() {
        return this.mExpandDirection == 2 || this.mExpandDirection == 3;
    }

    private void createAddButton(final Context context) {
        MyFloatingActionButton myFloatingActionButton = new MyFloatingActionButton(context){

            void updateBackground() {
//                this.mPlusColor = FloatingActionsMenu.this.mAddButtonPlusColor;
                this.mColorNormal = MyFloatingActionsMenu.this.mAddButtonColorNormal;
                this.mColorPressed = MyFloatingActionsMenu.this.mAddButtonColorPressed;
                this.mStrokeVisible = MyFloatingActionsMenu.this.mAddButtonStrokeVisible;
                super.updateBackground();
            }

            public Drawable getIconDrawable() {
                MyFloatingActionsMenu.RotatingDrawable rotatingDrawable = new MyFloatingActionsMenu.RotatingDrawable(super.getIconDrawable());
                MyFloatingActionsMenu.this.mRotatingDrawable = rotatingDrawable;
                OvershootInterpolator interpolator = new OvershootInterpolator();
                ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", new float[]{135.0F, 0.0F});
                ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", new float[]{0.0F, 135.0F});
                collapseAnimator.setInterpolator(interpolator);
                expandAnimator.setInterpolator(interpolator);
                MyFloatingActionsMenu.this.mExpandAnimation.play(expandAnimator);
                MyFloatingActionsMenu.this.mCollapseAnimation.play(collapseAnimator);
//                Drawable drawable = getResources().getDrawable(R.drawable.ic_btn_plus_sign);
//                return drawable;
                return rotatingDrawable;
            }
        };
        myFloatingActionButton.setIcon(R.drawable.ic_btn_settings_act_home);
//        myFloatingActionButton.setColorNormal(R.color.transperent_more);
//        myFloatingActionButton.setColorPressed(R.color.transperent_less);
//        myFloatingActionButton.setStrokeVisible(true);
        myFloatingActionButton.setId(net.i2p.android.ext.floatingactionbutton.R.id.fab_expand_menu_button);
        myFloatingActionButton.setSize(this.mAddButtonSize);
        myFloatingActionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyFloatingActionsMenu.this.toggle();
            }
        });
        this.mAddButton = myFloatingActionButton;
        this.addView(this.mAddButton, super.generateDefaultLayoutParams());
        //-----------------------------------------------------------------------------------
    }

    public void addButton(MyFloatingActionButton button) {
        this.addView(button, this.mButtonsCount - 1);
        ++this.mButtonsCount;
        if(this.mLabelsStyle != 0) {
            this.createLabels();
        }
    }

    public void removeButton(MyFloatingActionButton button) {
        this.removeView(button.getLabelView());
        this.removeView(button);
        --this.mButtonsCount;
    }

    private int getColor(int id) {
        return this.getResources().getColor(id);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        this.mMaxButtonWidth = 0;
        this.mMaxButtonHeight = 0;
        int maxLabelWidth = 0;

        for(int i = 0; i < this.mButtonsCount; ++i) {
            View child = this.getChildAt(i);
            if(child.getVisibility() != GONE) {
                switch(this.mExpandDirection) {
                    case 0:
                    case 1:
                        this.mMaxButtonWidth = Math.max(this.mMaxButtonWidth, child.getMeasuredWidth());
                        height += child.getMeasuredHeight();
                        break;
                    case 2:
                    case 3:
                        width += child.getMeasuredWidth();
                        this.mMaxButtonHeight = Math.max(this.mMaxButtonHeight, child.getMeasuredHeight());
                }

                if(!this.expandsHorizontally()) {
                    TextView label = (TextView)child.getTag(net.i2p.android.ext.floatingactionbutton.R.id.fab_label);
                    if(label != null) {
                        maxLabelWidth = Math.max(maxLabelWidth, label.getMeasuredWidth());
                    }
                }
            }
        }

        if(!this.expandsHorizontally()) {
            width = this.mMaxButtonWidth + (maxLabelWidth > 0?maxLabelWidth + this.mLabelsMargin:0);
        } else {
            height = this.mMaxButtonHeight;
        }

        switch(this.mExpandDirection) {
            case 0:
            case 1:
                height += this.mButtonSpacing * (this.getChildCount() - 1);
                height = this.adjustForOvershoot(height);
                break;
            case 2:
            case 3:
                width += this.mButtonSpacing * (this.getChildCount() - 1);
                width = this.adjustForOvershoot(width);
        }

        this.setMeasuredDimension(width, height);
    }

    private int adjustForOvershoot(int dimension) {
        return dimension * 12 / 10;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int addButtonTop;
        int nextX;
        switch(this.mExpandDirection) {
            case 0:
            case 1:
                boolean expandUp = this.mExpandDirection == 0;
                if(changed) {
                    this.mTouchDelegateGroup.clearTouchDelegates();
                }

                int addButtonY = expandUp?b - t - this.mAddButton.getMeasuredHeight():0;
                int buttonsHorizontalCenter = this.mLabelsPosition == 0?r - l - this.mMaxButtonWidth / 2:this.mMaxButtonWidth / 2;
                int addButtonLeft = buttonsHorizontalCenter - this.mAddButton.getMeasuredWidth() / 2;
                this.mAddButton.layout(addButtonLeft, addButtonY, addButtonLeft + this.mAddButton.getMeasuredWidth(), addButtonY + this.mAddButton.getMeasuredHeight());
                int labelsOffset = this.mMaxButtonWidth / 2 + this.mLabelsMargin;
                int labelsXNearButton = this.mLabelsPosition == 0?buttonsHorizontalCenter - labelsOffset:buttonsHorizontalCenter + labelsOffset;
                int nextY = expandUp?addButtonY - this.mButtonSpacing:addButtonY + this.mAddButton.getMeasuredHeight() + this.mButtonSpacing;

                for(int var27 = this.mButtonsCount - 1; var27 >= 0; --var27) {
                    View var28 = this.getChildAt(var27);
                    if(var28 != this.mAddButton && var28.getVisibility() != GONE) {
                        addButtonTop = buttonsHorizontalCenter - var28.getMeasuredWidth() / 2;
                        nextX = expandUp?nextY - var28.getMeasuredHeight():nextY;
                        var28.layout(addButtonTop, nextX, addButtonTop + var28.getMeasuredWidth(), nextX + var28.getMeasuredHeight());
                        float var29 = (float)(addButtonY - nextX);
                        float var31 = 0.0F;
                        ViewHelper.setTranslationY(var28, this.mExpanded ? var31 : var29);
                        ViewHelper.setAlpha(var28, this.mExpanded?1.0F:0.0F);
                        MyFloatingActionsMenu.LayoutParams var30 = (MyFloatingActionsMenu.LayoutParams)var28.getLayoutParams();
                        var30.mCollapseDir.setFloatValues(new float[]{var31, var29});
                        var30.mExpandDir.setFloatValues(new float[]{var29, var31});
                        var30.setAnimationsTarget(var28);
                        View var32 = (View)var28.getTag(net.i2p.android.ext.floatingactionbutton.R.id.fab_label);
                        if(var32 != null) {
                            int var35 = this.mLabelsPosition == 0?labelsXNearButton - var32.getMeasuredWidth():labelsXNearButton + var32.getMeasuredWidth();
                            int var33 = this.mLabelsPosition == 0?var35:labelsXNearButton;
                            int var34 = this.mLabelsPosition == 0?labelsXNearButton:var35;
                            int labelTop = nextX - this.mLabelsVerticalOffset + (var28.getMeasuredHeight() - var32.getMeasuredHeight()) / 2;
                            var32.layout(var33, labelTop, var34, labelTop + var32.getMeasuredHeight());
                            Rect touchArea = new Rect(Math.min(addButtonTop, var33), nextX - this.mButtonSpacing / 2, Math.max(addButtonTop + var28.getMeasuredWidth(), var34), nextX + var28.getMeasuredHeight() + this.mButtonSpacing / 2);
                            this.mTouchDelegateGroup.addTouchDelegate(new TouchDelegate(touchArea, var28));
                            ViewHelper.setTranslationY(var32, this.mExpanded?var31:var29);
                            ViewHelper.setAlpha(var32, this.mExpanded?1.0F:0.0F);
                            MyFloatingActionsMenu.LayoutParams labelParams = (MyFloatingActionsMenu.LayoutParams)var32.getLayoutParams();
                            labelParams.mCollapseDir.setFloatValues(new float[]{var31, var29});
                            labelParams.mExpandDir.setFloatValues(new float[]{var29, var31});
                            labelParams.setAnimationsTarget(var32);
                        }

                        nextY = expandUp?nextX - this.mButtonSpacing:nextX + var28.getMeasuredHeight() + this.mButtonSpacing;
                    }
                }

                return;
            case 2:
            case 3:
                boolean expandLeft = this.mExpandDirection == 2;
                int addButtonX = expandLeft?r - l - this.mAddButton.getMeasuredWidth():0;
                addButtonTop = b - t - this.mMaxButtonHeight + (this.mMaxButtonHeight - this.mAddButton.getMeasuredHeight()) / 2;
                this.mAddButton.layout(addButtonX, addButtonTop, addButtonX + this.mAddButton.getMeasuredWidth(), addButtonTop + this.mAddButton.getMeasuredHeight());
                nextX = expandLeft?addButtonX - this.mButtonSpacing:addButtonX + this.mAddButton.getMeasuredWidth() + this.mButtonSpacing;

                for(int i = this.mButtonsCount - 1; i >= 0; --i) {
                    View child = this.getChildAt(i);
                    if(child != this.mAddButton && child.getVisibility() != GONE) {
                        int childX = expandLeft?nextX - child.getMeasuredWidth():nextX;
                        int childY = addButtonTop + (this.mAddButton.getMeasuredHeight() - child.getMeasuredHeight()) / 2;
                        child.layout(childX, childY, childX + child.getMeasuredWidth(), childY + child.getMeasuredHeight());
                        float collapsedTranslation = (float)(addButtonX - childX);
                        float expandedTranslation = 0.0F;
                        ViewHelper.setTranslationX(child, this.mExpanded?expandedTranslation:collapsedTranslation);
                        ViewHelper.setAlpha(child, this.mExpanded?1.0F:0.0F);
                        MyFloatingActionsMenu.LayoutParams params = (MyFloatingActionsMenu.LayoutParams)child.getLayoutParams();
                        params.mCollapseDir.setFloatValues(new float[]{expandedTranslation, collapsedTranslation});
                        params.mExpandDir.setFloatValues(new float[]{collapsedTranslation, expandedTranslation});
                        params.setAnimationsTarget(child);
                        nextX = expandLeft?childX - this.mButtonSpacing:childX + child.getMeasuredWidth() + this.mButtonSpacing;
                    }
                }
        }

    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MyFloatingActionsMenu.LayoutParams(super.generateDefaultLayoutParams());
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyFloatingActionsMenu.LayoutParams(super.generateLayoutParams(attrs));
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new MyFloatingActionsMenu.LayoutParams(super.generateLayoutParams(p));
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.bringChildToFront(this.mAddButton);
        this.mButtonsCount = this.getChildCount();
        if(this.mLabelsStyle != 0) {
            this.createLabels();
        }
    }

    private void createLabels() {
        ContextThemeWrapper context = new ContextThemeWrapper(this.getContext(), this.mLabelsStyle);

        for(int i = 0; i < this.mButtonsCount; ++i) {
            MyFloatingActionButton button = (MyFloatingActionButton)this.getChildAt(i);
            String title = button.getTitle();
            if(button != this.mAddButton && title != null && button.getTag(net.i2p.android.ext.floatingactionbutton.R.id.fab_label) == null) {
                TextView label = new TextView(context);
                label.setTextAppearance(this.getContext(), this.mLabelsStyle);
                label.setText(button.getTitle());
                this.addView(label);
                button.setTag(net.i2p.android.ext.floatingactionbutton.R.id.fab_label, label);
            }
        }

    }

    public void collapse() {
        if(this.mExpanded) {
            this.mExpanded = false;
            this.mTouchDelegateGroup.setEnabled(false);
            this.mCollapseAnimation.start();
            this.mExpandAnimation.cancel();
            if(this.mListener != null) {
                this.mListener.onMenuCollapsed();
            }
            this.mAddButton.setIcon(R.drawable.ic_btn_settings_act_home);
        }

    }

    public void toggle() {
        if(this.mExpanded) {
            this.collapse();
        } else {
            this.expand();
        }

    }

    public void expand() {
        if(!this.mExpanded) {
            this.mExpanded = true;
            this.mTouchDelegateGroup.setEnabled(true);
            this.mCollapseAnimation.cancel();
            this.mExpandAnimation.start();
            if(this.mListener != null) {
                this.mListener.onMenuExpanded();
            }
            this.mAddButton.setIcon(R.drawable.ic_btn_close_settings_act_home);
        }

    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        MyFloatingActionsMenu.SavedState savedState = new MyFloatingActionsMenu.SavedState(superState);
        savedState.mExpanded = this.mExpanded;
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if(state instanceof MyFloatingActionsMenu.SavedState) {
            MyFloatingActionsMenu.SavedState savedState = (MyFloatingActionsMenu.SavedState)state;
            this.mExpanded = savedState.mExpanded;
            this.mTouchDelegateGroup.setEnabled(this.mExpanded);
            if(this.mRotatingDrawable != null) {
                this.mRotatingDrawable.setRotation(this.mExpanded?135.0F:0.0F);
            }

            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public static class SavedState extends BaseSavedState {
        public boolean mExpanded;
        public static final Creator<MyFloatingActionsMenu.SavedState> CREATOR = new Creator() {
            public MyFloatingActionsMenu.SavedState createFromParcel(Parcel in) {
                return new MyFloatingActionsMenu.SavedState(in);
            }

            public MyFloatingActionsMenu.SavedState[] newArray(int size) {
                return new MyFloatingActionsMenu.SavedState[size];
            }
        };

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            this.mExpanded = in.readInt() == 1;
        }

        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.mExpanded?1:0);
        }
    }

    private class LayoutParams extends android.view.ViewGroup.LayoutParams {
        private ObjectAnimator mExpandDir = new ObjectAnimator();
        private ObjectAnimator mExpandAlpha = new ObjectAnimator();
        private ObjectAnimator mCollapseDir = new ObjectAnimator();
        private ObjectAnimator mCollapseAlpha = new ObjectAnimator();
        private boolean animationsSetToPlay;

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.mExpandDir.setInterpolator(MyFloatingActionsMenu.sExpandInterpolator);
            this.mExpandAlpha.setInterpolator(MyFloatingActionsMenu.sAlphaExpandInterpolator);
            this.mCollapseDir.setInterpolator(MyFloatingActionsMenu.sCollapseInterpolator);
            this.mCollapseAlpha.setInterpolator(MyFloatingActionsMenu.sCollapseInterpolator);
            this.mCollapseAlpha.setPropertyName("alpha");
            this.mCollapseAlpha.setFloatValues(new float[]{1.0F, 0.0F});
            this.mExpandAlpha.setPropertyName("alpha");
            this.mExpandAlpha.setFloatValues(new float[]{0.0F, 1.0F});
            switch(MyFloatingActionsMenu.this.mExpandDirection) {
                case 0:
                case 1:
                    this.mCollapseDir.setPropertyName("translationY");
                    this.mExpandDir.setPropertyName("translationY");
                    break;
                case 2:
                case 3:
                    this.mCollapseDir.setPropertyName("translationX");
                    this.mExpandDir.setPropertyName("translationX");
            }

        }

        public void setAnimationsTarget(View view) {
            this.mCollapseAlpha.setTarget(view);
            this.mCollapseDir.setTarget(view);
            this.mExpandAlpha.setTarget(view);
            this.mExpandDir.setTarget(view);
            if(!this.animationsSetToPlay) {
                MyFloatingActionsMenu.this.mCollapseAnimation.play(this.mCollapseAlpha);
                MyFloatingActionsMenu.this.mCollapseAnimation.play(this.mCollapseDir);
                MyFloatingActionsMenu.this.mExpandAnimation.play(this.mExpandAlpha);
                MyFloatingActionsMenu.this.mExpandAnimation.play(this.mExpandDir);
                this.animationsSetToPlay = true;
            }

        }
    }

    private static class RotatingDrawable extends LayerDrawable {
        private float mRotation;

        public RotatingDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        public float getRotation() {
            return this.mRotation;
        }

        public void setRotation(float rotation) {
            this.mRotation = rotation;
            this.invalidateSelf();
        }

        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(this.mRotation, (float)this.getBounds().centerX(), (float)this.getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }

    public interface OnFloatingActionsMenuUpdateListener {
        void onMenuExpanded();

        void onMenuCollapsed();
    }
}
