package com.zonsim.demo.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ^-^
 * Created by tang-jw on 7/7.
 */

public class RangeBar extends View {
    
    private int mTickCount = 101;
    private int mTickStart = 1;
    private int mTickEnd = 100;
    private int mTickInterval = 1;
    private int mLeftIndex;
    private int mRightIndex;
    
    private PointAView mLeftThumb;
    
    private PointBView mRightThumb;
    
    
    private int mDefaultWidth = 500;
    
    private int mDefaultHeight = 150;
    private Paint mBackLinePaint;
    private Paint mConnectLinePaint;
    
    
    //Used for ignoring vertical moves
    private int mDiffX;
    private int mDiffY;
    private float mLastX;
    private float mLastY;
    private int mTickDistance;
    private int mWith;
    private int mProgress;
    private ThumbView mThumbView;
    
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
        
        
        mBackLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackLinePaint.setColor(Color.GRAY);
        mBackLinePaint.setStrokeWidth(3f);
        mBackLinePaint.setStyle(Paint.Style.STROKE);
        
        mConnectLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mConnectLinePaint.setColor(Color.RED);
        mConnectLinePaint.setStrokeWidth(3f);
        mConnectLinePaint.setStyle(Paint.Style.STROKE);
        
        final Context context = getContext();
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
        
        mWith = w - 96;
        mTickDistance = (mWith - 96) / 100;
        
        mLeftThumb.init(90);

//        mLeftThumb.setX((mLeftIndex / (float) (mTickCount - 1)) * (w - 200) + 100);
        
        mRightThumb.init(90);
        
        
        mThumbView.init(90);
        
        if (mProgress != 0) {
            System.out.println("setIndex=>" + mProgress);
            setLeftIndex(mProgress - 5);
            setRightIndex(mProgress);
        }

//        mRightThumb.setX((mRightIndex / (float) (mTickCount - 1)) * (w - 200) + 100);
    }
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        //画背景线
        canvas.drawLine(96, 90, mWith, 90, mBackLinePaint);
        canvas.drawLine(mLeftThumb.getX(), 90, mRightThumb.getX(), 90, mConnectLinePaint);
        
        mLeftThumb.draw(canvas);
        mRightThumb.draw(canvas);
        
        mThumbView.draw(canvas);
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
        if (x > mWith - 48 + mRightThumb.getWidth()) {
            //移出范围
            mRightThumb.setX(mWith - 48);
        } else if (x < mLeftThumb.getX() + mLeftThumb.getWidth() + 5) {
            mRightThumb.setX(mLeftThumb.getX() + mLeftThumb.getWidth() + 5);
        } else {
            mRightThumb.setX(x);
        }
        invalidate();
    }
    
    private void movePointA(float x) {
        
        if (x < 48 - mLeftThumb.getWidth()) {
            //移出范围
            mLeftThumb.setX(48);
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
    
        mLeftIndex = (int) ((mLeftThumb.getX() - 48 + mTickDistance / 2) / mTickDistance);
    
        if (mListener != null) {
            mListener.onRangeChangeListener(this,mLeftIndex,mRightIndex);
        }
        
        int pointUpX = mLeftIndex * mTickDistance + 48;
        
        mLeftThumb.setX(pointUpX);
        
        invalidate();
        mLeftThumb.release();
    }
    
    private void releasePointB() {
    
        mRightIndex = (int) ((mRightThumb.getX() - 48 + mTickDistance / 2) / mTickDistance);
    
        if (mListener != null) {
            mListener.onRangeChangeListener(this,mLeftIndex,mRightIndex);
        }
        
        int pointUpX = mRightIndex * mTickDistance + 48;
        mRightThumb.setX(pointUpX);
        
        invalidate();
        mRightThumb.release();
    }
    
    private void onActionDown(float x, float y) {
        
        
        if (!mRightThumb.isPressed() && mLeftThumb.isInTargetZone(x, y)) {
            mLeftThumb.press();
        } else if (!mLeftThumb.isPressed() && mRightThumb.isInTargetZone(x, y)) {
            mRightThumb.press();
        }
    }
    
    
    public void setRightIndex(int progress) {
        mProgress = progress;
        System.out.println("setIndex=>" + progress);
        mRightThumb.setX(mTickDistance * progress + 96);
        invalidate();
    }
    
    public void setLeftIndex(int progress) {
    
        mLeftIndex = progress;
        mLeftThumb.setX(mTickDistance * progress + 96);
        invalidate();
    }
    
    public void setThumbX(int progress) {
        mRightIndex = progress;
        mThumbView.setX(mTickDistance * progress + 96);
        invalidate();
    }
    
    private OnRangeBarChangeListener mListener;
    
    public void setOnRangeBarChangeListener(OnRangeBarChangeListener listener) {
        mListener = listener;
    }
    
    public interface OnRangeBarChangeListener {
        
        void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex);
    }
    
}
