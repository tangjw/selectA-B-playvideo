package com.zonsim.demo.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.zonsim.demo.R;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
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
    private int mProgress = -1;
    private int mPosition = -1;
    private int mDuration = -1;
    private boolean mABVisibility;
    private int mLeftPosition;
    private int mRightPosition;
    private float mPer;
    private int mLeftPinIndex;
    private int mRightPinIndex;
    private boolean isShowAB;
    
    public void setListener(Listener listener) {
        mListener = listener;
    }
    
    public MyJcVideoView(Context context) {
        super(context);
    }
    
    public MyJcVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MyJcVideoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        
        mView = findViewById(R.id.back);
        mTvAB = findViewById(R.id.tv_ab);
        mRangeBar = (RangeBar) findViewById(R.id.bottom_seek_progress1);
        mTvAB.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                isShowAB = !isShowAB;
                
                mRangeBar.setVisibility(isShowAB ? VISIBLE : GONE);
                
                progressBar.setVisibility(isShowAB ? GONE : VISIBLE);
                
                mPer = (float) JCMediaManager.instance().mediaPlayer.getDuration() / 100;
                
                mRangeBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRangeBar.setThumbX(mProgress);
                        mLeftPinIndex = mProgress;
                        mRightPinIndex = mLeftPinIndex + 5;
                        mLeftPosition = (int) (mLeftPinIndex * mPer + 0.5f);
                        mRightPosition = mLeftPosition + (int) (5 * mPer + 0.5f);
                        mRangeBar.setLeftIndex(mLeftPinIndex);
                        mRangeBar.setRightIndex(mRightPinIndex);
                        
                        mRangeBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
                
            }
        });
        
        
        mRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex) {
                
                if (mLeftPinIndex != leftPinIndex) {
                    mLeftPinIndex = leftPinIndex;
                    mLeftPosition = (int) (mLeftPinIndex * mPer + 0.5);
                    if (mLeftPosition > mPosition) {
                        mRangeBar.setThumbX(leftPinIndex);
                        JCMediaManager.instance().mediaPlayer.seekTo(mLeftPosition);
                    }
                }
                if (mRightPinIndex != rightPinIndex) {
                    mRightPinIndex = rightPinIndex;
                    mRightPosition = mLeftPosition + (int) ((rightPinIndex - leftPinIndex) * mPer + 0.5);
                    if (mRightPosition < mPosition) {
                        mRangeBar.setThumbX(leftPinIndex);
                        JCMediaManager.instance().mediaPlayer.seekTo(mLeftPosition);
                    }
                }
            }
        });
    }
    
    
    @Override
    public void setProgressAndText(int progress, int position, int duration) {
        super.setProgressAndText(progress, position, duration);
        
        if (isShowAB && mProgress != progress) {
            mRangeBar.setThumbX(progress);
            
            if (position >= mRightPosition) {
                JCMediaManager.instance().mediaPlayer.seekTo(mLeftPosition);
            }
        }
        mPosition = position;
        mProgress = progress;
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
