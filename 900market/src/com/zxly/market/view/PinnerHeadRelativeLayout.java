package com.zxly.market.view;



import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.zxly.market.R;
import com.zxly.market.fragment.AppCommentFragment;
import com.zxly.market.fragment.AppIntroduceFragment;
import com.zxly.market.utils.Logger;

public class PinnerHeadRelativeLayout extends RelativeLayout{
	private View head;
	private float lasty;
	private float lastx;
	private OverScroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mMaximumVelocity, mMinimumVelocity;
	private int mHeadHeight;//头部内容的高度
	public int rootviewheight;//PinnerHeadRelativeLayout的原始高度
	private int mreachHeight;//头部向上滑动的距离
	private AppCommentFragment mCommentFragment;
	private AppIntroduceFragment mIntroduceFragment;
	private Fragment currentFragment;
	private boolean isheadHiden;
	@SuppressLint({ "Recycle",  })
	public PinnerHeadRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new OverScroller(context);
		mVelocityTracker = VelocityTracker.obtain();
		mMaximumVelocity = ViewConfiguration.get(context)
				.getScaledMaximumFlingVelocity();
		mMinimumVelocity = ViewConfiguration.get(context)
				.getScaledMinimumFlingVelocity();
		
	}
	public void setFragments(AppCommentFragment fragment1,AppIntroduceFragment fragment2){
		mCommentFragment = fragment1;
		mIntroduceFragment = fragment2;
	}
	@Override
	protected void onFinishInflate() {
		head = findViewById(R.id.head_);
		super.onFinishInflate();
		
		
	}
	public int getmHeadHeight() {
		return mHeadHeight;
	}
	public void setmHeadHeight(int mHeadHeight) {
		this.mHeadHeight = mHeadHeight;
	}
	public void setcurrenFragment(Fragment fragment){
		currentFragment = fragment;
	}
//	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)//实例化完成后并显示时才能计算控件的高度，在实例化时就计算则得0
	{	
		super.onSizeChanged(w, h, oldw, oldh);
		rootviewheight = getMeasuredHeight();
		mHeadHeight = head.getMeasuredHeight();	
		//com.zxly.market.utils.Logger.e("","取得滑动区域的高度"+rootviewheight+ "取得的head_高度-->"+mHeadHeight);
		mreachHeight  = (int) (mHeadHeight*0.5f);
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mVelocityTracker.addMovement(event);
		int action = event.getAction();
		float y = event.getY();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			lasty = y;
			return true;
		case MotionEvent.ACTION_MOVE:
			 float dy = y-lasty;
		     scrollBy(0, (int) -dy);
		     lasty  = y;
			break;
		case MotionEvent.ACTION_CANCEL:		
			break;
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
			int velocityY = (int) mVelocityTracker.getYVelocity();
			com.zxly.market.utils.Logger.e("", "getScrollY()--"+getScrollY());
			if (getScrollY()<mreachHeight&&Math.abs(velocityY) > mMinimumVelocity)
			{
				fling(-velocityY);
			}else if(getScrollY()>=mreachHeight){//当滑动距离已经超过这个值时并松开手后，自动向上滑动隐藏头部
				fling(mMaximumVelocity);
			}
			
			mVelocityTracker.clear();
		}

		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			lasty = ev.getY();
			lastx = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			 float dy = ev.getY()-lasty;
			 if(dy > -3&&dy<3)return false;//有些单击也会出现move事件，在一小段范围内有效
			 if(Math.abs(ev.getX() - lastx)>Math.abs(ev.getY() - lasty))return false;//表示手势是左右滑
			 if(currentFragment instanceof AppIntroduceFragment){
				if(mIntroduceFragment.getScrollView().getScrollY()!=0&&dy > 0)return false;//
				if (!isheadHiden|| (mIntroduceFragment.getScrollView().getScrollY() == 0 && isheadHiden && dy > 0)){
					return true;
					
				}
			 }else if(currentFragment instanceof AppCommentFragment){
				 if(dy > 0&&mCommentFragment.getListView().getChildAt(0)!=null&&mCommentFragment.getListView().getChildAt(0).getY()!=0)return false;
				 if (mCommentFragment.getListView().getChildAt(0)==null||!isheadHiden||( mCommentFragment.getListView().getFirstVisiblePosition() == 0 && isheadHiden && dy > 0&&mCommentFragment.getListView().getChildAt(0)!=null&&mCommentFragment.getListView().getChildAt(0).getY()==0)){
					 
					return true;
				 }
				
			 }
			
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public void scrollTo(int x, int y)
	{
		if (y < 0)//只能向上滑动
		{
			y = 0;
		}
		if (y >mHeadHeight)//只能滑动mHeadHeight的距离
		{
			y = mHeadHeight;
		}
		if (y != getScrollY())
		{
			super.scrollTo(x, y);
		}
		isheadHiden = getScrollY()== mHeadHeight;//getScrollY()表示已经滚动的距离
	}
	@SuppressLint("NewApi")
	public void fling(int velocityY)
	{
		mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mHeadHeight);
		invalidate();
	}
	@SuppressLint("NewApi")
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset())
		{  
			scrollTo(0, mScroller.getCurrY());
			invalidate();
		}
	}
}
