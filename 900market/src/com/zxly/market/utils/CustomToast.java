package com.zxly.market.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Toast重复显示（显示时间过长）解决方法
 * @author fengruyi
 *
 */
public class CustomToast {
	private final static int duration = 1500;//1.5秒
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	}; 
	 
	public static void showToast(Context mContext, String text) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
		mToast.setText(text);
		else
		mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		mHandler.postDelayed(r, duration);
		mToast.show();
	}
	 
	public static void showToast(Context mContext, int resId) {
		showToast(mContext, mContext.getResources().getString(resId));
	}

}
	