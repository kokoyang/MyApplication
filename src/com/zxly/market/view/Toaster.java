package com.zxly.market.view;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.zxly.market.activity.BaseApplication;

/**
 * Created by Yangwencai on 2015/4/20.
 */
public class Toaster {

    public static void toast(final String msg){
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void toast(final int msg){
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
