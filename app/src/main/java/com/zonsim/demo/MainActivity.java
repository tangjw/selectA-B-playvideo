package com.zonsim.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appyvet.rangebar.RangeBar;
import com.bumptech.glide.Glide;
import com.zonsim.demo.widget.MyJcVideoView;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MainActivity extends AppCompatActivity {
    
    private MyJcVideoView mJCVideoPlayer;
    private RangeBar mRangeBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        mJCVideoPlayer = (MyJcVideoView) findViewById(R.id.videoplayer);
       
        setPlayer();
    
//        mJCVideoPlayer.seekToInAdvance = 21;
    }
    
    private void setPlayer() {
        
        final String mp4url = "http://video.jiecao.fm/11/24/6/%E5%AD%94%E6%98%8E%E7%81%AF.mp4";
        final String imgurl = "http://img4.jiecaojingxuan.com/2016/11/24/2c3e62bb-6a32-4fb0-a1d5-d1260ad436a4.png@!640_360";
        
        
        mJCVideoPlayer.setUp(mp4url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "我说标题");
        Glide.with(this)
                .load(imgurl)
                .into(mJCVideoPlayer.thumbImageView);
        
        
    }
    
    
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
    
    
    @Override
    protected void onDestroy() {
        JCMediaManager.textureView = null;
        super.onDestroy();
        
    }
}
