package com.zxly.market.utils;

import com.zxly.market.activity.BaseApplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

/**
 * 
 * @author fengruyi
 *
 */
public class ViewUtil {
	
	/**
	 * 一次性注册所有的view click事件
	 * @param Listener
	 * @param views
	 */
	public static void setOnClickListener(OnClickListener Listener,View... views){
		for(View view : views){
			view.setOnClickListener(Listener);
		}
	}
	
	/**
	 * 设置view的高度
	 * @param view
	 * @param height
	 */
	 public static void setViewHeight(View view, int height) {
	        if (view == null) {
	            return;
	        }
	        ViewGroup.LayoutParams params = view.getLayoutParams();
	        params.height = height;
	    }
	 /**
	  * 按高度比例设置view的高度
	  * @param view
	  * @param scale
	  */
	 public static void setViewHeightByScale(View view,float scale){
		 if (view == null) {
	            return;
	        }
	        ViewGroup.LayoutParams params = view.getLayoutParams();
	        params.height = (int)(BaseApplication.mHeightPixels*scale);
	 }
	 /**
	  * 按高度比例设置view的宽度
	  * @param view
	  * @param scale
	  */
	 public static void setViewWidthByScale(View view,float scale){
		 if (view == null) {
	            return;
	        }
	        ViewGroup.LayoutParams params = view.getLayoutParams();
	        params.width = (int)(BaseApplication.mWidthPixels*scale);
	 }
	 /**
	  * 设置view的宽度
	  * @param view
	  * @param width
	  */
	 public static void setViewWidth(View view, int width) {
	        if (view == null) {
	            return;
	        }
	        ViewGroup.LayoutParams params = view.getLayoutParams();
	        params.width = width;
	    }
	 /**
	  *  设置view的宽高
	  * @param view
	  * @param size
	  */
	 public static void setViewSize(View view,int size){
		 if (view == null) {
	            return;
	        }
	        ViewGroup.LayoutParams params = view.getLayoutParams();
	        params.width = size;
	        params.height = size;
	 }
	 /**
	  * 设置view通用点击效果
	  * @param color
	  * @param views
	  */
	 public static void setButtonClickStyle(final int color,View ...views){
		 OnTouchListener touchListener = new OnTouchListener(){
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					v.getBackground().setColorFilter(color,PorterDuff.Mode.MULTIPLY);//通过滤镜效果改变图片颜色来达到点击效果
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					v.getBackground().clearColorFilter();//松开时去掉滤镜效果还原图片
				}
				return false;
			}};
		 for (View view : views) {
			 view.setOnTouchListener(touchListener);
		}
	 }
	 
	 
}
