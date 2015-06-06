package com.zxly.market.utils;



/**
 * 日志包装工具
 * @author fengruyi
 * 
 */
public class Logger {
    /**
     * 是否调试，发布时请把DEBUG改为false
     */
    public static boolean debug = true;
    /**
     * 是否在客户端记录用户操作
     */
    public static boolean logFile = false;

    public static void v(String tag, String msg) {
        if(debug) {
            android.util.Log.v(tag, msg); 
        }
    }
    public static void v(String tag, String msg, Throwable tr) {
        if(debug) {
            android.util.Log.v(tag, msg, tr); 
        }
    }
    public static void d(String tag, String msg) {
        if(debug) {
            android.util.Log.d(tag, msg);
        }
       
    }
    public static void d(String tag, String msg, Throwable tr) {
        if(debug) {
            android.util.Log.d(tag, msg, tr);
        }
       
    }
    public static void i(String tag, String msg) {
        if(debug) {
            android.util.Log.i(tag, msg);
        }
       
    }
    public static void i(String tag, String msg, Throwable tr) {
        if(debug) {
            android.util.Log.i(tag, msg, tr);
        }
       
    }
    public static void w(String tag, String msg) {
        if(debug) {
            android.util.Log.w(tag, msg);
        }
       
    }
    public static void w(String tag, String msg, Throwable tr) {
        if(debug) {
            android.util.Log.w(tag, msg, tr);
        }
        
    }
    public static void w(String tag, Throwable tr) {
        if(debug) {
            android.util.Log.w(tag, tr);
        }
       
    }
    public static void e(String tag, String msg) {
        if(debug) {
            android.util.Log.e(tag, msg);
        }
       
    }
    public static void e(String tag, String msg, Throwable tr) {
        if(debug) {
            android.util.Log.e(tag, msg, tr);
        }
        
    }
    public static void e(String tag, Throwable tr) {
        if(debug) {
            android.util.Log.e(tag, "", tr);
        }
       
    }

} 
