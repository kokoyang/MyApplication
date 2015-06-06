package com.zxly.market.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.lidroid.xutils.HttpUtils;
import com.zxly.market.R;
import com.zxly.market.view.BannerCarouselView;

/**
 * 应用详情图片展示页面
 * @author fengruyi
 *
 */
public class ShowPicsActivity extends BaseActivity {
	private BannerCarouselView mPicsView;
	GestureDetector mGesture;
	@Override
	public int getContentViewId() {
		
		return R.layout.activity_show_pics;
	}

	@Override
	public void initViewAndData() {
		mPicsView = obtainView(R.id.pics_pager);
		Intent intent = getIntent();
		String urls[] = intent.getStringArrayExtra("urls");
		int currentIndex = intent.getIntExtra("index", 0);
		mPicsView.setData(urls, false);
		mPicsView.setCurrentItem(currentIndex+1);
		mGesture= new GestureDetector(this, gestureListener);
		mPicsView.getmViewPager().setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				return mGesture.onTouchEvent(event);
			}
		});
	}
	
	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener(){
		public boolean onSingleTapUp(MotionEvent e) {
			finish();
			return false;
		};
	};
	
}
