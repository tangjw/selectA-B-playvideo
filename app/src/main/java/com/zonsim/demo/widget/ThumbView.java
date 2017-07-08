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
 * Created by tang-jw on 7/8.
 */

public class ThumbView extends View {
    
    private Bitmap mBitmap;
    private Paint mBitPaint;
    private int mBitWidth;
    private int mBitHeight;
    private Rect mSrcRect;
    private int mY;
    private Rect mDestRect;
    private float mX;
    
    public ThumbView(Context context) {
        this(context, null);
    }
    
    public ThumbView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ThumbView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFirst();
    }
    
    private void initFirst() {
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.thumb)).getBitmap();
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        
        mBitWidth = mBitmap.getWidth();
        mBitHeight = mBitmap.getHeight();
        
        mSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
    
        System.out.println("---------"+mBitWidth);
        System.out.println("---------"+mBitHeight);
    }
    
    public void init(int coY) {
        mY = coY;
        mDestRect = new Rect(30, coY, 60, coY);
    }
    
    @Override
    public void setX(float x) {
        mX = x - mBitWidth / 2;
        mDestRect = new Rect((int) mX, (int) mY - mBitHeight / 2, (int) mX + mBitWidth, (int) mY+mBitHeight / 2);
    }
    
    @Override
    public void draw(Canvas canvas) {
        if (mDestRect != null) {
            canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, mBitPaint);
        }
        
        super.draw(canvas);
    }
}
