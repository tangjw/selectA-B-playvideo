package com.zonsim.demo.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zonsim.demo.L;

/**
 * ^-^
 * Created by tang-jw on 7/7.
 */

public class RangeBar extends View {
    
    private static final java.lang.String TAG = "RangeBar";
    private int mLeftIndex = -1;
    private int mRightIndex = -1;
    
    private PointAView mLeftThumb;
    
    private PointBView mRightThumb;
    
    
    private int mDefaultWidth = 500;
    
    private int mDefaultHeight = 150;
    private Paint mBackLinePaint;
    private Paint mConnectLinePaint;
    
    
    private int mDiffX;
    private int mDiffY;
    private float mLastX;
    private float mLastY;
    private int mTickDistance;
    private int mWith;
    private ThumbView mThumbView;
    /**
     * 默认左右边界的padding值 16dp
     */
    private int mPadding;
    private float mFloatTickDistance;
    private int mCoY;
    
    public RangeBar(Context context) {
        this(context, null);
    }
    
    public RangeBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public RangeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    
    private void init() {
        
        final Context context = getContext();
        
        mPadding = dip2px(context, 16f);
        
        mBackLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackLinePaint.setColor(Color.WHITE);
        mBackLinePaint.setStrokeWidth(dip2px(context, 1f));
        mBackLinePaint.setStyle(Paint.Style.STROKE);
        mBackLinePaint.setStrokeCap(Paint.Cap.ROUND);
        
        mConnectLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mConnectLinePaint.setColor(Color.RED);
        mConnectLinePaint.setStrokeWidth(dip2px(context, 1f));
        mConnectLinePaint.setStyle(Paint.Style.STROKE);
        
        
        mLeftThumb = new PointAView(context);
        
        mRightThumb = new PointBView(context);
        
        mThumbView = new ThumbView(context);
        
        mLeftIndex = 0;
        
    }
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        
        // Get measureSpec mode and size values.
        final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        // The RangeBar width should be as large as possible.
        if (measureWidthMode == MeasureSpec.AT_MOST) {
            width = measureWidth;
        } else if (measureWidthMode == MeasureSpec.EXACTLY) {
            width = measureWidth;
        } else {
            width = mDefaultWidth;
        }
        
        // The RangeBar height should be as small as possible.
        if (measureHeightMode == MeasureSpec.AT_MOST) {
            height = Math.min(mDefaultHeight, measureHeight);
        } else if (measureHeightMode == MeasureSpec.EXACTLY) {
            height = measureHeight;
        } else {
            height = mDefaultHeight;
        }
        
        setMeasuredDimension(width, height);
    }
    
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        
        
        L.i(TAG, "w => " + w);
        L.i(TAG, "h => " + h);
        
        mWith = w - 2 * mPadding;
        
        L.i(TAG, "mWith => " + mWith);
        
        mFloatTickDistance = (float) mWith / 100;
        
        L.i(TAG, "mFloatTickDistance => " + mFloatTickDistance);
        
        mTickDistance = (mWith - 96) / 100;
        
        mCoY = (int) ((float) h / 2 + 0.5f);
        
        mLeftThumb.init(mCoY);
        
        mRightThumb.init(mCoY);
        
        mThumbView.init(mCoY);
        
        if (mLeftIndex != -1 && mRightIndex != -1) {
            setLeftIndex(mLeftIndex);
            setRightIndex(mRightIndex);
        }
        
        L.i(TAG, "onSizeChanged()完毕");
    }
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        //画背景线
        canvas.drawLine(mPadding, mCoY, mWith + mPadding, mCoY, mBackLinePaint);
        canvas.drawLine(mLeftThumb.getX(), mCoY, mRightThumb.getX(), mCoY, mConnectLinePaint);
        
        mLeftThumb.draw(canvas);
        mRightThumb.draw(canvas);
        
        mThumbView.draw(canvas);
        
        L.i(TAG, "onDraw()完毕");
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        
        switch (event.getAction()) {
            
            case MotionEvent.ACTION_DOWN:
                mDiffX = 0;
                mDiffY = 0;
                
                mLastX = event.getX();
                mLastY = event.getY();
                onActionDown(event.getX(), event.getY());
                return true;
            
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp();
                return true;
            
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp();
                return true;
            
            case MotionEvent.ACTION_MOVE:
                onActionMove(event.getX());
                this.getParent().requestDisallowInterceptTouchEvent(true);
                final float curX = event.getX();
                final float curY = event.getY();
                mDiffX += Math.abs(curX - mLastX);
                mDiffY += Math.abs(curY - mLastY);
                mLastX = curX;
                mLastY = curY;
                
                if (mDiffX < mDiffY) {
                    //vertical touch
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else {
                    //horizontal touch (do nothing as it is needed for RangeBar)
                }
                return true;
            
            default:
                return false;
        }
    }
    
    private void onActionMove(float x) {
        if (mLeftThumb.isPressed()) {
            movePointA(x);
        }
        
        if (mRightThumb.isPressed()) {
            movePointB(x);
        }
        
    }
    
    private void movePointB(float x) {
        if (x > mWith + mPadding + mRightThumb.getWidth()) {
            //移出范围
            mRightThumb.setX(mWith + mPadding);
        } else if (x < mLeftThumb.getX() + mLeftThumb.getWidth() + 5) {
            mRightThumb.setX(mLeftThumb.getX() + mLeftThumb.getWidth() + 5);
        } else {
            mRightThumb.setX(x);
        }
        invalidate();
    }
    
    private void movePointA(float x) {
        
        if (x < mPadding - mLeftThumb.getWidth()) {
            //移出范围
            mLeftThumb.setX(mPadding);
        } else if (x > mRightThumb.getX() - 5) {
            //间隔5px
            mLeftThumb.setX(mRightThumb.getX() - 5);
        } else {
            mLeftThumb.setX(x);
            
        }
        invalidate();
    }
    
    private void onActionUp() {
        if (mLeftThumb.isPressed()) {
            releasePointA();
        }
        
        if (mRightThumb.isPressed()) {
            releasePointB();
        }
        
    }
    
    private void releasePointA() {
        
        mLeftIndex = (int) ((mLeftThumb.getX() - mPadding) / mFloatTickDistance + 0.5f);
        
        int pointUpX = (int) (mLeftIndex * mFloatTickDistance + 0.5f) + mPadding;
        
        mLeftThumb.setX(pointUpX);
        
        invalidate();
        mLeftThumb.release();
        if (mListener != null) {
            mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex);
        }
        
    }
    
    private void releasePointB() {
        
        mRightIndex = (int) ((mRightThumb.getX() - mPadding) / mFloatTickDistance + 0.5f);
        int pointUpX = (int) (mRightIndex * mFloatTickDistance + 0.5) + mPadding;
        mRightThumb.setX(pointUpX);
        invalidate();
        mRightThumb.release();
        if (mListener != null) {
            mListener.onRangeChangeListener(this, mLeftIndex, mRightIndex);
        }
        
    }
    
    private void onActionDown(float x, float y) {
        
        if (!mRightThumb.isPressed() && mLeftThumb.isInTargetZone(x, y)) {
            mLeftThumb.press();
        } else if (!mLeftThumb.isPressed() && mRightThumb.isInTargetZone(x, y)) {
            mRightThumb.press();
        }
    }
    
    
    public void setRightIndex(int progress) {
        mRightIndex = progress;
        mRightThumb.setX((int) (mFloatTickDistance * progress + 0.5f) + mPadding);
        invalidate();
    }
    
    public void setLeftIndex(int progress) {
        mLeftIndex = progress;
        mLeftThumb.setX((int) (mFloatTickDistance * progress + 0.5f) + mPadding );
        invalidate();
    }
    
    public void setThumbX(int progress) {
        
        mThumbView.setX(mFloatTickDistance * progress + mPadding + 0.5f);
        
        invalidate();
    }
    
    private OnRangeBarChangeListener mListener;
    
    public void setOnRangeBarChangeListener(OnRangeBarChangeListener listener) {
        mListener = listener;
    }
    
    public interface OnRangeBarChangeListener {
        
        void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex);
    }
    
    public static int dip2px(Context context, float dip) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
        
    }
    
}
