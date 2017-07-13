package com.zonsim.demo;

import android.content.Context;
import android.util.AttributeSet;

/**
 * ^-^
 * Created by tang-jw on 2017/7/12.
 */

public class MySeekBar extends android.support.v7.widget.AppCompatSeekBar {
    
    public MySeekBar(Context context) {
        super(context);
    }
    
    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        System.out.println(getPaddingLeft());
    }
}
