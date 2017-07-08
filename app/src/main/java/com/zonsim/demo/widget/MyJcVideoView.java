package com.zonsim.demo.widget;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.zonsim.demo.R;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * ^-^
 * Created by tang-jw on 5/24.
 */

public class MyJcVideoView extends JCVideoPlayerStandard {
    
    private Listener mListener;
    private View mView;
    private View mTvAB;
    private RangeBar mRangeBar;
    private ProgressBar mProgressBar;
    private int mProgress;
    private int mPosition;
    private int mDuration;
    private boolean mABVisibility;
    private int mLeftPosition;
    private int mRightPosition;
    private int mPer;
    
    public void setListener(Listener listener) {
        mListener = listener;
    }
    
    public MyJcVideoView(Context context) {
        super(context);
    }
    
    public MyJcVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public void onAutoCompletion() {
        
        
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            onStateAutoComplete();
            
        } else {
            super.onAutoCompletion();
        }
    }
    
    @Override
    public void init(Context context) {
       
        super.init(context);
    
        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        
        mView = findViewById(R.id.back);
        mTvAB = findViewById(R.id.tv_ab);
        mRangeBar = (RangeBar) findViewById(R.id.bottom_seek_progress1);
        mABVisibility = mRangeBar.getVisibility() == VISIBLE;
        mTvAB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             
                mRangeBar.setVisibility(mABVisibility ? GONE : VISIBLE);
                mABVisibility = mRangeBar.getVisibility() == VISIBLE;
                progressBar.setVisibility(mABVisibility ? GONE : VISIBLE);
    
                mRangeBar.setLeftIndex(mProgress);
                mRangeBar.setRightIndex(mProgress+5);
    
                mPer = mDuration / 100;
                mLeftPosition = mPosition;
                mRightPosition = mPosition + 5 * mPer;
    
    
            }
        });
        
        
        mRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex) {
    
                System.out.println("leftPinIndex"+leftPinIndex);
                System.out.println("rightPinIndex"+rightPinIndex);
                
                //校准一下
                int left = leftPinIndex* mPer;
                if (left < mPosition) {
                    mLeftPosition = left;
                } else {
                    JCMediaManager.instance().mediaPlayer.seekTo(mLeftPosition);
                    mRangeBar.setThumbX(leftPinIndex);
                }
    
                int right = rightPinIndex * mPer;
                if (right > mPosition) {
                    mRightPosition = right;
                } else {
                    JCMediaManager.instance().mediaPlayer.seekTo(mLeftPosition);
                    mRangeBar.setThumbX(leftPinIndex);
                } 
            }
        });
    }
    
    
    @Override
    public void setProgressAndText(int progress, int position, int duration) {
        super.setProgressAndText(progress, position, duration);
        mProgress = progress;
        mPosition = position;
        mDuration = duration;
    
    
        if (mABVisibility) {
            mRangeBar.setThumbX(progress);
            System.out.println("position"+position);
            System.out.println("position"+mRightPosition);
            if (position > mRightPosition) {
                JCMediaManager.instance().mediaPlayer.seekTo(mLeftPosition);
//                setUp();
                System.out.println("----------");
            }  
        }
    
    
    }
    
    @Override
    public int getLayoutId() {
        return R.layout.layout_myjc_videoplayer;
    }
    
    
    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            if (mListener != null) mListener.isFullscreen(true);
            mTvAB.setVisibility(VISIBLE);
            mView.setVisibility(VISIBLE);
        } else {
            if (mListener != null) mListener.isFullscreen(false);
            mView.setVisibility(INVISIBLE);
            mTvAB.setVisibility(GONE);
        }
        
        
    }
    
    public interface Listener {
        void isFullscreen(boolean flag);
    }
}
