package com.rey.material.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.rey.material.R;
import com.rey.material.app.ThemeManager;
import com.rey.material.drawable.RippleDrawable;
import com.rey.material.util.ColorUtil;
import com.rey.material.util.ThemeUtil;
import com.rey.material.util.TypefaceUtil;
import com.rey.material.util.ViewUtil;

/**
 * Created by Ret on 3/18/2015.
 */
public class Slider extends View implements ThemeManager.OnThemeChangedListener{

    private RippleManager mRippleManager;
    protected int mStyleId;
    protected int mCurrentStyle = ThemeManager.THEME_UNDEFINED;

    private Paint mPaint;
    private RectF mDrawRect;
    private RectF mTempRect;
    private Path mLeftTrackPath;
    private Path mRightTrackPath;
    private Path mMarkPath;

    private int mMinValue = 0;
    private int mMaxValue = 100;
    private int mStepValue = 1;

    private boolean mDiscreteMode = false;

    private int mPrimaryColor;
    private int mSecondaryColor;
    private int mTrackSize = -1;
    private Paint.Cap mTrackCap = Paint.Cap.BUTT;
    private int mThumbBorderSize = -1;
    private int mThumbRadius = -1;
    private int mThumbFocusRadius = -1;
    private float mThumbPosition = -1;
    private Typeface mTypeface = Typeface.DEFAULT;
    private int mTextSize = -1;
    private int mTextColor = 0xFFFFFFFF;
    private int mGravity = Gravity.CENTER;
    private int mTravelAnimationDuration = -1;
    private int mTransformAnimationDuration = -1;
    private Interpolator mInterpolator;
    private int mBaselineOffset;

    private int mTouchSlop;
    private PointF mMemoPoint;
    private boolean mIsDragging;
    private float mThumbCurrentRadius;
    private float mThumbFillPercent;
    private boolean mAlwaysFillThumb = false;
    private int mTextHeight;
    private int mMemoValue;
    private String mValueText;

    private ThumbRadiusAnimator mThumbRadiusAnimator;
    private ThumbStrokeAnimator mThumbStrokeAnimator;
    private ThumbMoveAnimator mThumbMoveAnimator;

    private boolean mIsRtl = false;

    /**
     * Interface definition for a callback to be invoked when thumb's position changed.
     */
    public interface OnPositionChangeListener{
        /**
         * Called when thumb's position changed.
         *
         * @param view The view fire this event.
         * @param fromUser Indicate the change is from user touch event or not.
         * @param oldPos The old position of thumb.
         * @param newPos The new position of thumb.
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue);
    }

    private OnPositionChangeListener mOnPositionChangeListener;

    public interface ValueDescriptionProvider{

        String getDescription(int value);

    }

    private ValueDescriptionProvider mValueDescriptionProvider;

    public Slider(Context context) {
        super(context);

        init(context, null, 0, 0);
    }

    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Slider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //default color
        mPrimaryColor = ThemeUtil.colorControlActivated(context, 0xFF000000);
        mSecondaryColor = ThemeUtil.colorControlNormal(context, 0xFF000000);

        mDrawRect = new RectF();
        mTempRect = new RectF();
        mLeftTrackPath = new Path();
        mRightTrackPath = new Path();

        mThumbRadiusAnimator = new ThumbRadiusAnimator();
        mThumbStrokeAnimator = new ThumbStrokeAnimator();
        mThumbMoveAnimator = new ThumbMoveAnimator();

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMemoPoint = new PointF();

        applyStyle(context, attrs, defStyleAttr, defStyleRes);

        if(!isInEditMode())
            mStyleId = ThemeManager.getStyleId(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyStyle(int resId){
        ViewUtil.applyStyle(this, resId);
        applyStyle(getContext(), null, 0, resId);
    }

    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        getRippleManager().onCreate(this, context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Slider, defStyleAttr, defStyleRes);
        int minValue = getMinValue();
        int maxValue = getMaxValue();
        boolean valueRangeDefined = false;
        int value = -1;
        boolean valueDefined = false;
        String familyName = null;
        int style = Typeface.NORMAL;
        boolean textStyleDefined = false;
        for(int i = 0, count = a.getIndexCount(); i < count; i++){
            int attr = a.getIndex(i);
            if(attr == R.styleable.Slider_sl_discreteMode)
                mDiscreteMode = a.getBoolean(attr, false);
            else if(attr == R.styleable.Slider_sl_primaryColor)
                mPrimaryColor = a.getColor(attr, 0);
            else if(attr == R.styleable.Slider_sl_secondaryColor)
                mSecondaryColor = a.getColor(attr, 0);
            else if(attr == R.styleable.Slider_sl_trackSize)
                mTrackSize = a.getDimensionPixelSize(attr, 0);
            else if(attr == R.styleable.Slider_sl_trackCap) {
                int cap = a.getInteger(attr, 0);
                if(cap == 0)
                    mTrackCap = Paint.Cap.BUTT;
                else if(cap == 1)
                    mTrackCap = Paint.Cap.ROUND;
                else
                    mTrackCap = Paint.Cap.SQUARE;
            }
            else if(attr == R.styleable.Slider_sl_thumbBorderSize)
                mThumbBorderSize = a.getDimensionPixelSize(attr, 0);
            else if(attr == R.styleable.Slider_sl_thumbRadius)
                mThumbRadius = a.getDimensionPixelSize(attr, 0);
            else if(attr == R.styleable.Slider_sl_thumbFocusRadius)
                mThumbFocusRadius = a.getDimensionPixelSize(attr, 0);
            else if(attr == R.styleable.Slider_sl_travelAnimDuration) {
                mTravelAnimationDuration = a.getInteger(attr, 0);
                mTransformAnimationDuration = mTravelAnimationDuration;
            }
            else if(attr == R.styleable.Slider_sl_alwaysFillThumb) {
                mAlwaysFillThumb = a.getBoolean(R.styleable.Slider_sl_alwaysFillThumb, false);
            }
            else if(attr == R.styleable.Slider_sl_interpolator){
                int resId = a.getResourceId(R.styleable.Slider_sl_interpolator, 0);
                mInterpolator = AnimationUtils.loadInterpolator(context, resId);
            }
            else if(attr == R.styleable.Slider_android_gravity)
                mGravity = a.getInteger(attr, 0);
            else if(attr == R.styleable.Slider_sl_minValue) {
                minValue = a.getInteger(attr, 0);
                valueRangeDefined = true;
            }
            else if(attr == R.styleable.Slider_sl_maxValue) {
                maxValue = a.getInteger(attr, 0);
                valueRangeDefined = true;
            }
            else if(attr == R.styleable.Slider_sl_stepValue)
                mStepValue = a.getInteger(attr, 0);
            else if(attr == R.styleable.Slider_sl_value) {
                value = a.getInteger(attr, 0);
                valueDefined = true;
            }
            else if(attr == R.styleable.Slider_sl_fontFamily) {
                familyName = a.getString(attr);
                textStyleDefined = true;
            }
            else if(attr == R.styleable.Slider_sl_textStyle) {
                style = a.getInteger(attr, 0);
                textStyleDefined = true;
            }
            else if(attr == R.styleable.Slider_sl_textColor)
                mTextColor = a.getColor(attr, 0);
            else if(attr == R.styleable.Slider_sl_textSize)
                mTextSize = a.getDimensionPixelSize(attr, 0);
            else if(attr == R.styleable.Slider_android_enabled)
                setEnabled(a.getBoolean(attr, true));
            else if(attr == R.styleable.Slider_sl_baselineOffset)
                mBaselineOffset = a.getDimensionPixelOffset(attr, 0);
        }

        a.recycle();

        if(mTrackSize < 0)
            mTrackSize = ThemeUtil.dpToPx(context, 2);

        if(mThumbBorderSize < 0)
            mThumbBorderSize = ThemeUtil.dpToPx(context, 2);

        if(mThumbRadius < 0)
            mThumbRadius = ThemeUtil.dpToPx(context, 10);

        if(mThumbFocusRadius < 0)
            mThumbFocusRadius = ThemeUtil.dpToPx(context, 14);

        if(mTravelAnimationDuration < 0){
            mTravelAnimationDuration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);
            mTransformAnimationDuration = mTravelAnimationDuration;
        }

        if(mInterpolator == null)
            mInterpolator = new DecelerateInterpolator();

        if(valueRangeDefined)
            setValueRange(minValue, maxValue, false);

        if(valueDefined)
            setValue(value, false);
        else if(mThumbPosition < 0)
            setValue(mMinValue, false);

        if(textStyleDefined)
            mTypeface = TypefaceUtil.load(context, familyName, style);

        if(mTextSize < 0)
            mTextSize = context.getResources().getDimensionPixelOffset(R.dimen.abc_text_size_small_material);

        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(mTypeface);

        measureText();
        invalidate();
    }

    @Override
    public void onThemeChanged(ThemeManager.OnThemeChangedEvent event) {
        int style = ThemeManager.getInstance().getCurrentStyle(mStyleId);
        if(mCurrentStyle != style){
            mCurrentStyle = style;
            applyStyle(mCurrentStyle);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mStyleId != 0) {
            ThemeManager.getInstance().registerOnThemeChangedListener(this);
            onThemeChanged(null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RippleManager.cancelRipple(this);
        if(mStyleId != 0)
            ThemeManager.getInstance().unregisterOnThemeChangedListener(this);
    }

    private void measureText(){
        if(mValueText == null)
            return;

        Rect temp = new Rect();
        mPaint.setTextSize(mTextSize);
        float width = mPaint.measureText(mValueText);
        float maxWidth = (float)(mThumbRadius * Math.sqrt(2) * 2 - ThemeUtil.dpToPx(getContext(), 8));
        if(width > maxWidth){
            float textSize = mTextSize * maxWidth / width;
            mPaint.setTextSize(textSize);
        }

        mPaint.getTextBounds(mValueText, 0, mValueText.length(), temp);
        mTextHeight = temp.height();
    }

    private String getValueText(){
        int value = getValue();
        if(mValueText == null || mMemoValue != value){
            mMemoValue = value;
            mValueText = mValueDescriptionProvider == null ? String.valueOf(mMemoValue) : mValueDescriptionProvider.getDescription(mMemoValue);
            measureText();
        }

        return mValueText;
    }

    /**
     * @return The minimum selectable value.
     */
    public int getMinValue(){
        return mMinValue;
    }

    /**
     * @return The maximum selectable value.
     */
    public int getMaxValue(){
        return mMaxValue;
    }

    /**
     * @return The step value.
     */
    public int getStepValue(){
        return mStepValue;
    }

    /**
     * Set the randge of selectable value.
     * @param min The minimum selectable value.
     * @param max The maximum selectable value.
     * @param animation Indicate that should show animation when thumb's current position changed.
     */
    public void setValueRange(int min, int max, boolean animation){
        if(max < min || (min == mMinValue && max == mMaxValue))
            return;

        float oldValue = getExactValue();
        float oldPosition = getPosition();
        mMinValue = min;
        mMaxValue = max;

        setValue(oldValue, animation);
        if(mOnPositionChangeListener != null && oldPosition == getPosition() && oldValue != getExactValue())
            mOnPositionChangeListener.onPositionChanged(this, false, oldPosition, oldPosition, Math.round(oldValue), getValue());
    }

    /**
     * @return The selected value.
     */
    public int getValue(){
        return Math.round(getExactValue());
    }

    /**
     * @return The exact selected value.
     */
    public float getExactValue(){
        return (mMaxValue - mMinValue) * getPosition() + mMinValue;
    }

    /**
     * @return The current position of thumb in [0..1] range.
     */
    public float getPosition(){
        return mThumbMoveAnimator.isRunning() ? mThumbMoveAnimator.getPosition() : mThumbPosition;
    }

    /**
     * Set current position of thumb.
     * @param pos The position in [0..1] range.
     * @param animation Indicate that should show animation when change thumb's position.
     */
    public void setPosition(float pos, boolean animation){
        setPosition(pos, animation, animation, false);
    }

    private void setPosition(float pos, boolean moveAnimation, boolean transformAnimation, boolean fromUser){
        boolean change = getPosition() != pos;
        int oldValue = getValue();
        float oldPos = getPosition();

        if(!moveAnimation || !mThumbMoveAnimator.startAnimation(pos)){
            mThumbPosition = pos;

            if(transformAnimation) {
                if(!mIsDragging)
                    mThumbRadiusAnimator.startAnimation(mThumbRadius);
                mThumbStrokeAnimator.startAnimation(pos == 0 ? 0 : 1);
            }
            else{
                mThumbCurrentRadius = mThumbRadius;
                mThumbFillPercent = (mAlwaysFillThumb || mThumbPosition != 0) ? 1 : 0;
                invalidate();
            }
        }

        int newValue = getValue();
        float newPos = getPosition();

        if(change && mOnPositionChangeListener != null)
            mOnPositionChangeListener.onPositionChanged(this, fromUser, oldPos, newPos, oldValue, newValue);
    }

    /**
     * Changes the primary color and invalidates the view to force a redraw.
     * @param color New color to assign to mPrimaryColor.
     */
    public void setPrimaryColor(int color) {
        mPrimaryColor = color;
        invalidate();
    }

    /**
     * Changes the secondary color and invalidates the view to force a redraw.
     * @param color New color to assign to mSecondaryColor.
     */
    public void setSecondaryColor(int color) {
        mSecondaryColor = color;
        invalidate();
    }

    /**
     * Set if we want the thumb to always be filled.
     * @param alwaysFillThumb Do we want it to always be filled.
     */
    public void setAlwaysFillThumb(boolean alwaysFillThumb) {
        mAlwaysFillThumb = alwaysFillThumb;
    }

    /**
     * Set the selected value of this Slider.
     * @param value The selected value.
     * @param animation Indicate that should show animation when change thumb's position.
     */
    public void setValue(float value, boolean animation){
        value = Math.min(mMaxValue, Math.max(value, mMinValue));
        setPosition((value - mMinValue) / (mMaxValue - mMinValue), animation);
    }

    /**
     * Set a listener will be called when thumb's position changed.
     * @param listener The {@link OnPositionChangeListener} will be called.
     */
    public void setOnPositionChangeListener(OnPositionChangeListener listener){
        mOnPositionChangeListener = listener;
    }

    public void setValueDescriptionProvider(ValueDescriptionProvider provider){
        mValueDescriptionProvider = provider;
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        Drawable background = getBackground();
        if(background instanceof RippleDrawable && !(drawable instanceof RippleDrawable))
            ((RippleDrawable) background).setBackgroundDrawable(drawable);
        else
            super.setBackgroundDrawable(drawable);
    }

    protected RippleManager getRippleManager(){
        if(mRippleManager == null){
            synchronized (RippleManager.class){
                if(mRippleManager == null)
                    mRippleManager = new RippleManager();
            }
        }

        return mRippleManager;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        RippleManager rippleManager = getRippleManager();
        if (l == rippleManager)
            super.setOnClickListener(l);
        else {
            rippleManager.setOnClickListener(l);
            setOnClickListener(rippleManager);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
                widthSize = getSuggestedMinimumWidth();
                break;
            case MeasureSpec.AT_MOST:
                widthSize = Math.min(widthSize, getSuggestedMinimumWidth());
                break;
        }

        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                heightSize = getSuggestedMinimumHeight();
                break;
            case MeasureSpec.AT_MOST:
                heightSize = Math.min(heightSize, getSuggestedMinimumHeight());
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public int getSuggestedMinimumWidth() {
        return (mDiscreteMode ? (int)(mThumbRadius * Math.sqrt(2)) : mThumbFocusRadius) * 4 + getPaddingLeft() + getPaddingRight();
    }

    @Override
    public int getSuggestedMinimumHeight() {
        return (mDiscreteMode ? (int)(mThumbRadius * (4 + Math.sqrt(2))) : mThumbFocusRadius * 2) + getPaddingTop() + getPaddingBottom();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        boolean rtl = layoutDirection == LAYOUT_DIRECTION_RTL;
        if(mIsRtl != rtl) {
            mIsRtl = rtl;
            invalidate();
        }
    }

    @Override
    public int getBaseline() {
        int align = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        int baseline;

        if(mDiscreteMode){
            int fullHeight = (int)(mThumbRadius * (4 + Math.sqrt(2)));
            int height = mThumbRadius * 2;
            switch (align) {
                case Gravity.TOP:
                    baseline = Math.max(getPaddingTop(), fullHeight - height) + mThumbRadius;
                    break;
                case Gravity.BOTTOM:
                    baseline = getMeasuredHeight() - getPaddingBottom();
                    break;
                default:
                    baseline = Math.round(Math.max((getMeasuredHeight() - height) / 2f, fullHeight - height) + mThumbRadius);
                    break;
            }
        }
        else{
            int height = mThumbFocusRadius * 2;
            switch (align) {
                case Gravity.TOP:
                    baseline = getPaddingTop() + mThumbFocusRadius;
                    break;
                case Gravity.BOTTOM:
                    baseline = getMeasuredHeight() - getPaddingBottom();
                    break;
                default:
                    baseline = Math.round((getMeasuredHeight() - height) / 2f + mThumbFocusRadius);
                    break;
            }
        }

        return baseline + mBaselineOffset;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mDrawRect.left = getPaddingLeft() + mThumbRadius;
        mDrawRect.right = w - getPaddingRight() - mThumbRadius;

        int align = mGravity & Gravity.VERTICAL_GRAVITY_MASK;

        if(mDiscreteMode){
            int fullHeight = (int)(mThumbRadius * (4 + Math.sqrt(2)));
            int height = mThumbRadius * 2;
            switch (align) {
                case Gravity.TOP:
                    mDrawRect.top = Math.max(getPaddingTop(), fullHeight - height);
                    mDrawRect.bottom = mDrawRect.top + height;
                    break;
                case Gravity.BOTTOM:
                    mDrawRect.bottom = h - getPaddingBottom();
                    mDrawRect.top = mDrawRect.bottom - height;
                    break;
                default:
                    mDrawRect.top = Math.max((h - height) / 2f, fullHeight - height);
                    mDrawRect.bottom = mDrawRect.top + height;
                    break;
            }
        }
        else{
            int height = mThumbFocusRadius * 2;
            switch (align) {
                case Gravity.TOP:
                    mDrawRect.top = getPaddingTop();
                    mDrawRect.bottom = mDrawRect.top + height;
                    break;
                case Gravity.BOTTOM:
                    mDrawRect.bottom = h - getPaddingBottom();
                    mDrawRect.top = mDrawRect.bottom - height;
                    break;
                default:
                    mDrawRect.top = (h - height) / 2f;
                    mDrawRect.bottom = mDrawRect.top + height;
                    break;
            }
        }
    }

    private boolean isThumbHit(float x, float y, float radius){
        float cx = mDrawRect.width() * mThumbPosition + mDrawRect.left;
        float cy = mDrawRect.centerY();

        return x >= cx - radius && x <= cx + radius && y >= cy - radius && y < cy + radius;
    }

    private double distance(float x1, float y1, float x2, float y2){
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private float correctPosition(float position){
        if(!mDiscreteMode)
            return position;

        int totalOffset = mMaxValue - mMinValue;
        int valueOffset = Math.round(totalOffset * position);
        int stepOffset = valueOffset / mStepValue;
        int lowerValue = stepOffset * mStepValue;
        int higherValue = Math.min(totalOffset, (stepOffset + 1) * mStepValue);

        if(valueOffset - lowerValue < higherValue - valueOffset)
            position = lowerValue / (float)totalOffset;
        else
            position = higherValue / (float)totalOffset;

        return position;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        super.onTouchEvent(event);
        getRippleManager().onTouchEvent(this, event);

        if(!isEnabled())
            return false;

        float x = event.getX();
        float y = event.getY();
        if(mIsRtl)
            x = 2 * mDrawRect.centerX() - x;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsDragging = isThumbHit(x, y, mThumbRadius) && !mThumbMoveAnimator.isRunning();
                mMemoPoint.set(x, y);
                if(mIsDragging) {
                    mThumbRadiusAnimator.startAnimation(mDiscreteMode ? 0 : mThumbFocusRadius);

                    if(getParent() != null)
                        getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsDragging) {
                    if(mDiscreteMode) {
                        float position = correctPosition(Math.min(1f, Math.max(0f, (x - mDrawRect.left) / mDrawRect.width())));
                        setPosition(position, true, true, true);
                    }
                    else{
                        float offset = (x - mMemoPoint.x) / mDrawRect.width();
                        float position = Math.min(1f, Math.max(0f, mThumbPosition + offset));
                        setPosition(position, false, true, true);
                        mMemoPoint.x = x;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mIsDragging) {
                    mIsDragging = false;
                    setPosition(getPosition(), true, true, true);

                    if(getParent() != null)
                        getParent().requestDisallowInterceptTouchEvent(false);
                }
                else if(distance(mMemoPoint.x, mMemoPoint.y, x, y) <= mTouchSlop){
                    float position = correctPosition(Math.min(1f, Math.max(0f, (x - mDrawRect.left) / mDrawRect.width())));
                    setPosition(position, true, true, true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(mIsDragging) {
                    mIsDragging = false;
                    setPosition(getPosition(), true, true, true);

                    if(getParent() != null)
                        getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        return true;
    }

    private void getTrackPath(float x, float y, float radius){
        float halfStroke = mTrackSize / 2f;

        mLeftTrackPath.reset();
        mRightTrackPath.reset();

        if(radius - 1f < halfStroke){
            if(mTrackCap != Paint.Cap.ROUND){
                if(x > mDrawRect.left){
                    mLeftTrackPath.moveTo(mDrawRect.left, y - halfStroke);
                    mLeftTrackPath.lineTo(x, y - halfStroke);
                    mLeftTrackPath.lineTo(x, y + halfStroke);
                    mLeftTrackPath.lineTo(mDrawRect.left, y + halfStroke);
                    mLeftTrackPath.close();
                }

                if(x < mDrawRect.right){
                    mRightTrackPath.moveTo(mDrawRect.right, y + halfStroke);
                    mRightTrackPath.lineTo(x, y + halfStroke);
                    mRightTrackPath.lineTo(x, y - halfStroke);
                    mRightTrackPath.lineTo(mDrawRect.right, y - halfStroke);
                    mRightTrackPath.close();
                }
            }
            else{
                if(x > mDrawRect.left){
                    mTempRect.set(mDrawRect.left, y - halfStroke, mDrawRect.left + mTrackSize, y + halfStroke);
                    mLeftTrackPath.arcTo(mTempRect, 90, 180);
                    mLeftTrackPath.lineTo(x, y - halfStroke);
                    mLeftTrackPath.lineTo(x, y + halfStroke);
                    mLeftTrackPath.close();
                }

                if(x < mDrawRect.right){
                    mTempRect.set(mDrawRect.right - mTrackSize, y - halfStroke, mDrawRect.right, y + halfStroke);
                    mRightTrackPath.arcTo(mTempRect, 270, 180);
                    mRightTrackPath.lineTo(x, y + halfStroke);
                    mRightTrackPath.lineTo(x, y - halfStroke);
                    mRightTrackPath.close();
                }
            }
        }
        else{
            if(mTrackCap != Paint.Cap.ROUND){
                mTempRect.set(x - radius + 1f, y - radius + 1f, x + radius - 1f, y + radius - 1f);
                float angle = (float)(Math.asin(halfStroke / (radius - 1f)) / Math.PI * 180);

                if(x - radius > mDrawRect.left){
                    mLeftTrackPath.moveTo(mDrawRect.left, y - halfStroke);
                    mLeftTrackPath.arcTo(mTempRect, 180 + angle, -angle * 2);
                    mLeftTrackPath.lineTo(mDrawRect.left, y + halfStroke);
                    mLeftTrackPath.close();
                }

                if(x + radius < mDrawRect.right){
                    mRightTrackPath.moveTo(mDrawRect.right, y - halfStroke);
                    mRightTrackPath.arcTo(mTempRect, -angle, angle * 2);
                    mRightTrackPath.lineTo(mDrawRect.right, y + halfStroke);
                    mRightTrackPath.close();
                }
            }
            else{
                float angle = (float)(Math.asin(halfStroke / (radius - 1f)) / Math.PI * 180);

                if(x - radius > mDrawRect.left){
                    float angle2 = (float)(Math.acos(Math.max(0f, (mDrawRect.left + halfStroke - x + radius) / halfStroke)) / Math.PI * 180);

                    mTempRect.set(mDrawRect.left, y - halfStroke, mDrawRect.left + mTrackSize, y + halfStroke);
                    mLeftTrackPath.arcTo(mTempRect, 180 - angle2, angle2 * 2);

                    mTempRect.set(x - radius + 1f, y - radius + 1f, x + radius - 1f, y + radius - 1f);
                    mLeftTrackPath.arcTo(mTempRect, 180 + angle, -angle * 2);
                    mLeftTrackPath.close();
                }

                if(x + radius < mDrawRect.right){
                    float angle2 = (float)Math.acos(Math.max(0f, (x + radius - mDrawRect.right + halfStroke) / halfStroke));
                    mRightTrackPath.moveTo((float) (mDrawRect.right - halfStroke + Math.cos(angle2) * halfStroke), (float) (y + Math.sin(angle2) * halfStroke));

                    angle2 = (float)(angle2 / Math.PI * 180);
                    mTempRect.set(mDrawRect.right - mTrackSize, y - halfStroke, mDrawRect.right, y + halfStroke);
                    mRightTrackPath.arcTo(mTempRect, angle2, -angle2 * 2);

                    mTempRect.set(x - radius + 1f, y - radius + 1f, x + radius - 1f, y + radius - 1f);
                    mRightTrackPath.arcTo(mTempRect, -angle, angle * 2);
                    mRightTrackPath.close();
                }
            }
        }
    }

    private Path getMarkPath(Path path, float cx, float cy, float radius, float factor){
        if(path == null)
            path = new Path();
        else
            path.reset();

        float x1 = cx - radius;
        float y1 = cy;
        float x2 = cx + radius;
        float y2 = cy;
        float x3 = cx;
        float y3 = cy + radius;

        float nCx = cx;
        float nCy = cy - radius * factor;

        // calculate first arc
        float angle = (float)(Math.atan2(y2 - nCy, x2 - nCx) * 180 / Math.PI);
        float nRadius = (float)distance(nCx, nCy, x1, y1);
        mTempRect.set(nCx - nRadius, nCy - nRadius, nCx + nRadius, nCy + nRadius);
        path.moveTo(x1, y1);
        path.arcTo(mTempRect, 180 - angle, 180 + angle * 2);

        if(factor > 0.9f)
            path.lineTo(x3, y3);
        else{
            // find center point for second arc
            float x4 = (x2 + x3) / 2;
            float y4 = (y2 + y3) / 2;

            double d1 = distance(x2, y2, x4, y4);
            double d2 = d1 / Math.tan(Math.PI * (1f - factor) / 4);

            nCx = (float)(x4 - Math.cos(Math.PI / 4) * d2);
            nCy = (float)(y4 - Math.sin(Math.PI / 4) * d2);

            // calculate second arc
            angle = (float)(Math.atan2(y2 - nCy, x2 - nCx) * 180 / Math.PI);
            float angle2 = (float)(Math.atan2(y3 - nCy, x3 - nCx) * 180 / Math.PI);
            nRadius = (float)distance(nCx, nCy, x2, y2);
            mTempRect.set(nCx - nRadius, nCy - nRadius, nCx + nRadius, nCy + nRadius);
            path.arcTo(mTempRect, angle, angle2 - angle);

            // calculate third arc
            nCx = cx * 2 - nCx;
            angle = (float)(Math.atan2(y3 - nCy, x3 - nCx) * 180 / Math.PI);
            angle2 = (float)(Math.atan2(y1 - nCy, x1 - nCx) * 180 / Math.PI);
            mTempRect.set(nCx - nRadius, nCy - nRadius, nCx + nRadius, nCy + nRadius);
            path.arcTo(mTempRect, angle + (float)Math.PI / 4, angle2 - angle);
        }

        path.close();

        return path;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        float x = mDrawRect.width() * mThumbPosition + mDrawRect.left;
        if(mIsRtl)
            x = 2 * mDrawRect.centerX() - x;
        float y = mDrawRect.centerY();
        int filledPrimaryColor = ColorUtil.getMiddleColor(mSecondaryColor, isEnabled() ? mPrimaryColor : mSecondaryColor, mThumbFillPercent);

        getTrackPath(x, y, mThumbCurrentRadius);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mIsRtl ? filledPrimaryColor : mSecondaryColor);
        canvas.drawPath(mRightTrackPath, mPaint);
        mPaint.setColor(mIsRtl ? mSecondaryColor : filledPrimaryColor);
        canvas.drawPath(mLeftTrackPath, mPaint);

        mPaint.setColor(filledPrimaryColor);
        if(mDiscreteMode){
            float factor = 1f - mThumbCurrentRadius / mThumbRadius;

            if(factor > 0){
                mMarkPath = getMarkPath(mMarkPath, x, y, mThumbRadius, factor);
                mPaint.setStyle(Paint.Style.FILL);
                int saveCount = canvas.save();
                canvas.translate(0, -mThumbRadius * 2 * factor);
                canvas.drawPath(mMarkPath, mPaint);
                mPaint.setColor(ColorUtil.getColor(mTextColor, factor));
                canvas.drawText(getValueText(), x, y + mTextHeight / 2f - mThumbRadius * factor, mPaint);
                canvas.restoreToCount(saveCount);
            }

            float radius = isEnabled() ? mThumbCurrentRadius : mThumbCurrentRadius - mThumbBorderSize;
            if(radius > 0) {
                mPaint.setColor(filledPrimaryColor);
                canvas.drawCircle(x, y, radius, mPaint);
            }
        }
        else{
            float radius = isEnabled() ? mThumbCurrentRadius : mThumbCurrentRadius - mThumbBorderSize;
            if(mThumbFillPercent == 1)
                mPaint.setStyle(Paint.Style.FILL);
            else{
                float strokeWidth = (radius - mThumbBorderSize) * mThumbFillPercent + mThumbBorderSize;
                radius = radius - strokeWidth / 2f;
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(strokeWidth);
            }
            canvas.drawCircle(x, y, radius, mPaint);
        }
    }

    class ThumbRadiusAnimator implements Runnable{

        boolean mRunning = false;
        long mStartTime;
        float mStartRadius;
        int mRadius;

        public void resetAnimation(){
            mStartTime = SystemClock.uptimeMillis();
            mStartRadius = mThumbCurrentRadius;
        }

        public boolean startAnimation(int radius) {
            if(mThumbCurrentRadius == radius)
                return false;

            mRadius = radius;

            if(getHandler() != null){
                resetAnimation();
                mRunning = true;
                getHandler().postAtTime(this, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);
                invalidate();
                return true;
            }
            else {
                mThumbCurrentRadius = mRadius;
                invalidate();
                return false;
            }
        }

        public void stopAnimation() {
            mRunning = false;
            mThumbCurrentRadius = mRadius;
            if(getHandler() != null)
                getHandler().removeCallbacks(this);
            invalidate();
        }

        @Override
        public void run() {
            long curTime = SystemClock.uptimeMillis();
            float progress = Math.min(1f, (float)(curTime - mStartTime) / mTransformAnimationDuration);
            float value = mInterpolator.getInterpolation(progress);

            mThumbCurrentRadius = (mRadius - mStartRadius) * value + mStartRadius;

            if(progress == 1f)
                stopAnimation();

            if(mRunning) {
                if(getHandler() != null)
                    getHandler().postAtTime(this, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);
                else
                    stopAnimation();
            }

            invalidate();
        }

    }

    class ThumbStrokeAnimator implements Runnable{

        boolean mRunning = false;
        long mStartTime;
        float mStartFillPercent;
        int mFillPercent;

        public void resetAnimation(){
            mStartTime = SystemClock.uptimeMillis();
            mStartFillPercent = mThumbFillPercent;
        }

        public boolean startAnimation(int fillPercent) {
            if(mThumbFillPercent == fillPercent)
                return false;

            mFillPercent = fillPercent;

            if(getHandler() != null){
                resetAnimation();
                mRunning = true;
                getHandler().postAtTime(this, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);
                invalidate();
                return true;
            }
            else {
                mThumbFillPercent = mAlwaysFillThumb ? 1 : mFillPercent;
                invalidate();
                return false;
            }
        }

        public void stopAnimation() {
            mRunning = false;
            mThumbFillPercent = mAlwaysFillThumb ? 1 : mFillPercent;
            if(getHandler() != null)
                getHandler().removeCallbacks(this);
            invalidate();
        }

        @Override
        public void run() {
            long curTime = SystemClock.uptimeMillis();
            float progress = Math.min(1f, (float)(curTime - mStartTime) / mTransformAnimationDuration);
            float value = mInterpolator.getInterpolation(progress);

            mThumbFillPercent = mAlwaysFillThumb ? 1 : ((mFillPercent - mStartFillPercent) * value + mStartFillPercent);

            if(progress == 1f)
                stopAnimation();

            if(mRunning) {
                if(getHandler() != null)
                    getHandler().postAtTime(this, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);
                else
                    stopAnimation();
            }

            invalidate();
        }

    }

    class ThumbMoveAnimator implements Runnable{

        boolean mRunning = false;
        long mStartTime;
        float mStartFillPercent;
        float mStartRadius;
        float mStartPosition;
        float mPosition;
        float mFillPercent;
        int mDuration;

        public boolean isRunning(){
            return mRunning;
        }

        public float getPosition(){
            return mPosition;
        }

        public void resetAnimation(){
            mStartTime = SystemClock.uptimeMillis();
            mStartPosition = mThumbPosition;
            mStartFillPercent = mThumbFillPercent;
            mStartRadius = mThumbCurrentRadius;
            mFillPercent = mPosition == 0 ? 0 : 1;
            mDuration = mDiscreteMode && !mIsDragging ? mTransformAnimationDuration * 2 + mTravelAnimationDuration : mTravelAnimationDuration;
        }

        public boolean startAnimation(float position) {
            if(mThumbPosition == position)
                return false;

            mPosition = position;

            if(getHandler() != null){
                resetAnimation();
                mRunning = true;
                getHandler().postAtTime(this, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);
                invalidate();
                return true;
            }
            else {
                mThumbPosition = position;
                invalidate();
                return false;
            }
        }

        public void stopAnimation() {
            mRunning = false;
            mThumbCurrentRadius = mDiscreteMode && mIsDragging ? 0 : mThumbRadius;
            mThumbFillPercent = mAlwaysFillThumb ? 1 : mFillPercent;
            mThumbPosition = mPosition;
            if(getHandler() != null)
                getHandler().removeCallbacks(this);
            invalidate();
        }

        @Override
        public void run() {
            long curTime = SystemClock.uptimeMillis();
            float progress = Math.min(1f, (float)(curTime - mStartTime) / mDuration);
            float value = mInterpolator.getInterpolation(progress);

            if(mDiscreteMode){
                if(mIsDragging) {
                    mThumbPosition = (mPosition - mStartPosition) * value + mStartPosition;
                    mThumbFillPercent = mAlwaysFillThumb ? 1 : ((mFillPercent - mStartFillPercent) * value + mStartFillPercent);
                }
                else{
                    float p1 = (float)mTravelAnimationDuration / mDuration;
                    float p2 = (float)(mTravelAnimationDuration + mTransformAnimationDuration)/ mDuration;
                    if(progress < p1) {
                        value = mInterpolator.getInterpolation(progress / p1);
                        mThumbCurrentRadius = mStartRadius * (1f - value);
                        mThumbPosition = (mPosition - mStartPosition) * value + mStartPosition;
                        mThumbFillPercent = mAlwaysFillThumb ? 1 : ((mFillPercent - mStartFillPercent) * value + mStartFillPercent);
                    }
                    else if(progress > p2){
                        mThumbCurrentRadius = mThumbRadius * (progress - p2) / (1 - p2);
                    }
                }
            }
            else{
                mThumbPosition = (mPosition - mStartPosition) * value + mStartPosition;
                mThumbFillPercent = mAlwaysFillThumb ? 1 : ((mFillPercent - mStartFillPercent) * value + mStartFillPercent);

                if(progress < 0.2)
                    mThumbCurrentRadius = Math.max(mThumbRadius + mThumbBorderSize * progress * 5, mThumbCurrentRadius);
                else if(progress >= 0.8)
                    mThumbCurrentRadius = mThumbRadius + mThumbBorderSize * (5f - progress * 5);
            }


            if(progress == 1f)
                stopAnimation();

            if(mRunning) {
                if(getHandler() != null)
                    getHandler().postAtTime(this, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);
                else
                    stopAnimation();
            }

            invalidate();
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.position = getPosition();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setPosition(ss.position, false);
        requestLayout();
    }

    static class SavedState extends BaseSavedState {
        float position;

        /**
         * Constructor called from {@link Slider#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            position = in.readFloat();
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(position);
        }

        @Override
        public String toString() {
            return "Slider.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " pos=" + position + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
