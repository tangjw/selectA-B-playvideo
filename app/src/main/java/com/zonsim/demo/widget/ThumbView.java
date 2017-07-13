package com.zonsim.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zonsim.demo.L;
import com.zonsim.demo.R;

/**
 * ^-^
 * Created by tang-jw on 7/8.
 */

public class ThumbView extends View {
    
    private int mBitWidth;
    private int mBitHeight;
    private Rect mSrcRect;
    private int mY;
    private Drawable mDrawable;
    
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
        
        mDrawable = getResources().getDrawable(R.drawable.jc_seek_thumb_normal);
        
        mBitWidth = mDrawable.getIntrinsicWidth();
        mBitHeight = mDrawable.getIntrinsicWidth();

    }
    
    public void init(int coY) {
        mY = coY;
    }
    
    @Override
    public void setX(float x) {
        
        int left = (int) (x - (float) mBitWidth / 2);
        int top = mY - mBitHeight / 2;
        mSrcRect = new Rect(left, top, left + mBitWidth, top + mBitHeight);
        mDrawable.setBounds(mSrcRect);
    
        L.i("RangeBar","ThumbView setX");
        
    }
    
    @Override
    public void draw(Canvas canvas) {
        
        if (mSrcRect != null) {
            mDrawable.draw(canvas);
        }
        
        super.draw(canvas);
    
        L.i("RangeBar","ThumbView draw");
    }
}
