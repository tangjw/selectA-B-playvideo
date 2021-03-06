package com.zonsim.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zonsim.demo.R;


/**
 * 根据宽高比例自动计算高度 FrameLayout
 * Created by tang-jw on 2017/7/7.
 */
public class RatioFrameLayout extends FrameLayout {
    
    /**
     * 宽高比例
     */
    private float mRatio;
    
    public RatioFrameLayout(Context context) {
        this(context, null);
    }
    
    public RatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout);
        mRatio = typedArray.getFloat(R.styleable.RatioFrameLayout_ratioFrameLayout, 0f);
        typedArray.recycle();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mRatio != 0) {
            float height = width / mRatio;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
