package com.zonsim.demo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zonsim.demo.R;

/**
 * ^-^
 * Created by tang-jw on 7/7.
 */

public class PointBView extends View {
    
    private Bitmap mBitmap;
    private Paint mBitPaint;
    
    private Rect mSrcRect, mDestRect;
    private int mBitWidth;
    private int mBitHeight;
    
    public PointBView(Context context) {
        this(context, null);
    }
    
    public PointBView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public PointBView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        initFirst();
        
        
    }
    
    private void initFirst() {
        
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ico_loop_b)).getBitmap();
        
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        
        mBitWidth = mBitmap.getWidth();
        mBitHeight = mBitmap.getHeight();
        
        mSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
        //+10padding
        mTargetRadiusPx = Math.max(mBitHeight, mBitWidth);
    }
    
    /**
     * 初始位置
     *
     * @param coY
     */
    public void init(int coY) {
        mY = coY;
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (mDestRect != null) {
            canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, mBitPaint);
        }
        
        super.draw(canvas);
    }
    
    
    private boolean mIsPressed = false;
    
    private int mX;
    private int mY;
    
    @Override
    public void setX(float x) {
        mX = (int) (x + 0.5f);
        mDestRect = new Rect(mX, mY - mBitHeight, mX + mBitWidth, mY);
    }
    
    @Override
    public float getX() {
        return mX;
    }
    
    @Override
    public boolean isPressed() {
        return mIsPressed;
    }
    
    /**
     * Sets the state of the pin to pressed
     */
    public void press() {
        mIsPressed = true;
    }
    
    private float mTargetRadiusPx;
    
    public boolean isInTargetZone(float x, float y) {
        
        if ((mX+mBitWidth - x) > 0 && (mY - y) > 0) {
            return (mX+mBitWidth - x) < mTargetRadiusPx && (mY - y) < mTargetRadiusPx;
        } else {
            return false;
        }
        
    }
    
    public void release() {
        mIsPressed = false;
    }
}
