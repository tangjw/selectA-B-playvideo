package com.zonsim.demo;

import android.util.Log;

/**
 * 自定义的log类，默认TAG为“print”，改变isDebug可以控制日志是否输出
 * 当然也可以自定义日志的TAG
 * Created by tang-jw on 2016/5/25.
 */
public class L {
    
    
    private static final String TAG = "print";
    private static boolean isDebug = BuildConfig.DEBUG;// 是否需要打印bug，可以在application的onCreate函数里面初始化  
    
    private L() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    // 下面四个是默认tag的函数  
    public static void i(String msg) {
        if (isDebug) {
            msg = checkMsg(msg);
            Log.i(TAG, msg);
        }
    }
    
    public static void d(String msg) {
    
        if (isDebug) {
            msg = checkMsg(msg);
            Log.d(TAG, msg);
        }
    }
    
    public static void e(String msg) {
        if (isDebug) {
            msg = checkMsg(msg);
            Log.e(TAG, msg);
        }
    }
    
    public static void v(String msg) {
        if (isDebug) {
            msg = checkMsg(msg);
            Log.v(TAG, msg);
        }
    }
    
    // 下面是传入自定义tag的函数  
    public static void i(String tag, String msg) {
        if (isDebug) {
            msg = checkMsg(msg);
            Log.i(tag, msg);
        }
    }
    
    public static void d(String tag, String msg) {
        if (isDebug) {
            msg = checkMsg(msg);
            Log.d(tag, msg);
        }
    }
    
    public static void e(String tag, String msg) {
        if (isDebug) {
            msg = checkMsg(msg);
            Log.e(tag, msg);
        }
    }
    
    public static void v(String tag, String msg) {
        if (isDebug) {
            msg = checkMsg(msg);
            Log.v(tag, msg);
        }
    }
    
    private static String checkMsg(String msg) {
        return msg == null ? "null" : msg;
    }
    
    
}

