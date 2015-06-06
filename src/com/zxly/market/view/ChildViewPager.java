package com.zxly.market.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决嵌套viewpager字pager无滑动响应
 * @author fengruyi
 *
 */
public class ChildViewPager extends ViewPager{  
    /** 触摸时按下的点 **/  
  //  PointF downP = new PointF();  
    /** 触摸时当前的点 **/  
  //  PointF curP = new PointF();  
  //  OnSingleTouchListener onSingleTouchListener;  
    private float x;
    private float y;
    private boolean flag = true;
    public ChildViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
   
    public ChildViewPager(Context context) {  
        super(context);  
        // TODO Auto-generated constructor stub  
    }  
   
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
        //当拦截触摸事件到达此位置的时候，返回true，  
        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent  
        //return true;  
    	
    	try{  
    	    super.onInterceptTouchEvent(arg0)  ;
    	} catch(java.lang.IllegalArgumentException ex) {  
    	}  
    	if(arg0.getAction() == MotionEvent.ACTION_DOWN){
    		x = arg0.getX();
    		y = arg0.getY();
    	}
    	if(arg0.getAction() == MotionEvent.ACTION_MOVE){
    		if(Math.abs(arg0.getX()-x)>Math.abs(arg0.getY()-y)){//水平移动的距离比垂直滑动的距离大，想要左边滑动
    			 //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰  
    			getParent().requestDisallowInterceptTouchEvent(true); 
        		return true;
    		}
    	}
    	return false;  
    
    }  
    /**
     * 设置是否可以滑动
     * @param true 可以滑动，false不可以滑动
     */
    public void setCanScoll(boolean flag1){
    	flag = flag1;
    }
    
    public boolean onTouchEvent(MotionEvent arg0) {  
    	if(!flag){
    		getParent().requestDisallowInterceptTouchEvent(false); 
    		return false;
    	}else{
    		return super.onTouchEvent(arg0);
    	}
    }
}  