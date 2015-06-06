package com.zxly.market.view;

import java.util.ArrayList;
import java.util.List;

import u.aly.ar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.activity.TopicDetailActivity;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.BanerInfo;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.Logger;

/**
 * 自定义可以动态添加图片张数和自动滚动轮播控件，基于viewpager实现
 * @author fengruyi
 *
 */
public class BannerCarouselView extends RelativeLayout implements OnPageChangeListener,OnClickListener{
	/**
	 * 是否需要自动轮播
	 */
	private boolean isAutoPlaying;
	/**
	 * 轮播图集
	 */
	private List<ImageView> mImageviews;
	/**
	 * 图片地址
	 */
	//private List<String> imageUris;
//	/**
//	 * 本地图片id
//	 */
//	private int[]  imageresIds;
	/**
	 * 指示点
	 */
	private List<View> mDots;
	/**
	 * 实现轮播的组件
	 */
	private ChildViewPager mViewPager;

	/**
	 * 装载小圆点的layout
	 */
	private LinearLayout mDotsLayout;
	/**
	 * 当前显示图片的位置，是假的位置，除去0 和最后一个
	 */
	private int currentItem  = 0;
	/**
	 * 輪番時間六間隔為5秒
	 */
	private final int TIME_RATE = 5000;
	
	
    private MyPagerAdapter mAdapter;
    
    private String[] urls;
    
    private int dotSize ;//圆点大小
    
	public BannerCarouselView(Context context) {
		
		super(context,null);
		// TODO Auto-generated constructor stub
	}

	public BannerCarouselView(Context context, AttributeSet attrs) {
		super(context, attrs,0);
		
		
	}
	public BannerCarouselView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	public ViewPager getmViewPager() {
		return mViewPager;
	}

	// 切换当前显示的图片

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(!isAutoPlaying){
				return;
			}
			if (mImageviews.size() > 0) {
				//Logger.e("", "当前的banner--》"+currentItem);
				currentItem = (currentItem + 1) % (mImageviews.size()-2);
				//Logger.e("", "要显示的下一个banner--》"+currentItem);
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				mHandler.sendEmptyMessageDelayed(0, TIME_RATE);
			}
		};
	};
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		dotSize = getResources().getDimensionPixelSize(R.dimen.banner_dot_size);
		mImageviews = new ArrayList<ImageView>();
		mDots = new ArrayList<View>();
		mAdapter = new MyPagerAdapter();
		mViewPager = (ChildViewPager) findViewById(R.id.viewPager);
		mViewPager.setPageMargin(0);
		mDotsLayout = (LinearLayout) findViewById(R.id.layout_dots);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(3);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(isAutoPlaying){
			mHandler.removeCallbacksAndMessages(null);
			mHandler.sendEmptyMessageDelayed(0, TIME_RATE);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(!isAutoPlaying){
			mHandler.removeCallbacksAndMessages(null);
		}
	}
	
	public void setCurrentItem(int index){
		mViewPager.setCurrentItem(index);
	}
	/*
	public void setData(int[] imageresIds,boolean isAutoPlaying){
		this.imageresIds = imageresIds;
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.setMargins(7, 0, 0, 0);
		if(mDots.size()==0){
			for(int i = 0 ,length = imageresIds.length+2; i<length; i++){
				if(i<length-2){
					ImageView viewDot =  new ImageView(getContext());
					if(i == 0 ){
						viewDot.setBackgroundResource(R.drawable.dot_focused);
					}else{
						viewDot.setBackgroundResource(R.drawable.dot_normal);
					}
					viewDot.setLayoutParams(lp);
					mDotsLayout.addView(viewDot,lp);
					mDots.add(viewDot);
				}
				ImageView imageView = new ImageView(getContext());
				imageView.setScaleType(ScaleType.FIT_XY);
				//imageView.setImageUrl("");
				//imageView.setOnClickListener(l);//点击事件
				if(i==0){
					imageView.setBackgroundResource(imageresIds[length-3]);
				}else if(i==length-1){
					imageView.setBackgroundResource(imageresIds[0]);
				}else{
					imageView.setBackgroundResource(imageresIds[i-1]);
				}
				mImageviews.add(imageView);
			}
			mAdapter.notifyDataSetChanged();
			mViewPager.setCurrentItem(1, false);
		}else{
			for(int i = 0 ,length = imageresIds.length+2; i<length; i++){
				if(i==0){
					mImageviews.get(i).setBackgroundResource(imageresIds[length-3]);
				}else if(i==length-1){
					mImageviews.get(i).setBackgroundResource(imageresIds[0]);
				}else{
					mImageviews.get(i).setBackgroundResource(imageresIds[i-1]);
				}
			}
			//mAdapter.notifyDataSetChanged();
			mViewPager.setCurrentItem(1, false);
		}
		if(isAutoPlaying){
			startPlay();
		}
	}
	*/
	public void setData(String[] urls,boolean isAutoPlaying){
		this.urls = urls;
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(dotSize,dotSize);
		lp.setMargins(dotSize, 0, 0, 0);
		for(int i = 0 ,length = urls.length+2; i<length; i++){
			if(i<length-2){
				ImageView viewDot =  new ImageView(getContext());
				if(i == 0 ){
					viewDot.setBackgroundResource(R.drawable.dot_focused);
				}else{
					viewDot.setBackgroundResource(R.drawable.dot_normal);
				}
				viewDot.setLayoutParams(lp);
				mDotsLayout.addView(viewDot,lp);
				mDots.add(viewDot);
			}
			ImageView imageView = new ImageView(getContext());
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.FIT_XY);
			if(i==0){
				ImageLoaderUtil.Load(urls[length-3], imageView);
			}else if(i==length-1){
				ImageLoaderUtil.Load(urls[0], imageView);
			}else{
				ImageLoaderUtil.Load(urls[i-1], imageView);
			}
			mImageviews.add(imageView);
		}
		
		mAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(1, false);
		if(isAutoPlaying){
			startPlay();
		}
	}

	public void setData(List<BanerInfo> banners,boolean isAutoPlaying){
		DisplayImageOptions options = new DisplayImageOptions.Builder() 
		   .showImageOnLoading(R.drawable.icon_banner_defalt)//加载中时显示的图片
		   .showImageForEmptyUri(R.drawable.icon_banner_defalt)//设置图片Uri为空或是错误的时候显示的图片  
		   .showImageOnFail(R.drawable.icon_banner_defalt)  //设置图片加载/解码过程中错误时候显示的图片
		   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
		   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
		   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
		   .build();//构建完成  
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(dotSize,dotSize);
		lp.setMargins(dotSize, 0, 0, 0);
		if(mDots.size()==0){
		for(int i = 0 ,length = banners.size()+2; i<length; i++){
			if(i<length-2){
				ImageView viewDot =  new ImageView(getContext());
				if(i == 0 ){
					viewDot.setBackgroundResource(R.drawable.dot_focused);
				}else{
					viewDot.setBackgroundResource(R.drawable.dot_normal);
				}
				viewDot.setLayoutParams(lp);
				mDotsLayout.addView(viewDot,lp);
				mDots.add(viewDot);
			}
			ImageView imageView = new ImageView(getContext());
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			imageView.setBackgroundResource(R.drawable.icon_banner_defalt);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setOnClickListener(this);
			if(i==0){
				ImageLoaderUtil.Load(banners.get(length-3).getImgUrl(), imageView,options);
				imageView.setTag(banners.get(length-3));
			}else if(i==length-1){
				ImageLoaderUtil.Load(banners.get(0).getImgUrl(), imageView,options);
				imageView.setTag(banners.get(0));
			}else{
				ImageLoaderUtil.Load(banners.get(i-1).getImgUrl(), imageView,options);
				imageView.setTag(banners.get(i-1));
			}
			
			mImageviews.add(imageView);
		}
		mAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(1, false);
		}else{
			int x = banners.size()+2 - mImageviews.size();
			if(x<0){//banner图片数量减少,
				for(int i = 0;i<-x;i++){
					mViewPager.removeView(mImageviews.get(0));
					mImageviews.remove(0);
					mDotsLayout.removeView(mDots.get(0));
					mDots.remove(0);
				}
				mViewPager.removeAllViews();
			}else if(x>0){//banner图片数量减少
				for(int i = 0;i<x;i++){
					ImageView viewDot =  new ImageView(getContext());
					viewDot.setBackgroundResource(R.drawable.dot_normal);
					viewDot.setLayoutParams(lp);
					mDotsLayout.addView(viewDot,lp);
					mDots.add(viewDot);
					ImageView imageView = new ImageView(getContext());
					imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
					imageView.setBackgroundResource(R.drawable.icon_banner_defalt);
					imageView.setScaleType(ScaleType.FIT_XY);
					mImageviews.add(imageView);
				}
				mViewPager.removeAllViews();
			}
			
			for(int i = 0 ,length = banners.size()+2; i<length; i++){
				if(i==0){
					ImageLoaderUtil.Load(banners.get(length-3).getImgUrl(), mImageviews.get(i),options);
					mImageviews.get(i).setTag(banners.get(length-3));
				}else if(i==length-1){
					ImageLoaderUtil.Load(banners.get(0).getImgUrl(), mImageviews.get(i),options);
					mImageviews.get(i).setTag(banners.get(0));
				}else{
					ImageLoaderUtil.Load(banners.get(i-1).getImgUrl(), mImageviews.get(i),options);
					mImageviews.get(i).setTag(banners.get(i-1));
				}
			}
			mAdapter.notifyDataSetChanged();
			mViewPager.setCurrentItem(1, false);
		}
		
		if(isAutoPlaying&&banners.size()>1){
			mViewPager.setCanScoll(true);
			startPlay();
		}else if(banners.size()==1){
			Logger.e("", "让banner不能滑动");
			mViewPager.setCanScoll(false);
			stopPlay();
		}
	}
	
	
	public void startPlay(){
		isAutoPlaying = true;
		mHandler.removeCallbacksAndMessages(null);
		mHandler.sendEmptyMessageDelayed(0, TIME_RATE);
	}
	public void stopPlay(){
		isAutoPlaying = false;
		mHandler.removeCallbacksAndMessages(null);
	}
	
	
	 /** 
     * 设置选中的tip的背景 
     * @param selectItems 
     */  
    private void setImageBackground(int selectItems){ 
        for(int i=0; i<mDots.size(); i++){  
            if(i == selectItems){  
            	mDots.get(i).setBackgroundResource(R.drawable.dot_focused);  
            }else{  
            	mDots.get(i).setBackgroundResource(R.drawable.dot_normal);  
            }  
        }  
    }  
	
	private class MyPagerAdapter extends PagerAdapter{
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView imageview = (ImageView) object;
			container.removeView(imageview);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view  = mImageviews.get(position);
			container.addView(view);
			return view;
		}
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
		@Override
		public int getCount() {
			return mImageviews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		if(ViewPager.SCROLL_STATE_IDLE==arg0){//滑动停止
			mViewPager.setCurrentItem(currentItem, false);  
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		int pageIndex = position;   
        if (position == 0) {  
            // 当视图在第一个时，将页面号设置为图片的最后一张。  
            pageIndex  = mDots.size();  
        } else if (position == mDots.size()+1) {  
            // 当视图在最后一个是,将页面号设置为图片的第一张。  
            pageIndex = 1;  
        }  
        currentItem = pageIndex;
		setImageBackground(currentItem-1);
	}

	public void onClick(View arg0) {
		if(arg0.getTag() instanceof BanerInfo){
//			try {
//				UMengAgent.onEvent(getContext(), UMengAgent.banners[currentItem-1]);
//			} catch (Exception e) {
//			}
			
			BanerInfo info = (BanerInfo) arg0.getTag();
			if(info.getType()==1){
				Intent intent = new Intent(getContext(),TopicDetailActivity.class);
				intent.putExtra(Constant.TOPIC_DETIAL_URL, info.getUrl());
				intent.putExtra(Constant.TOPIC_TITLE, info.getSpecName());
				getContext().startActivity(intent);
			}else{
				Intent intent = new Intent(getContext(),AppDetailActivity.class);
				intent.putExtra(Constant.APK_DETAIL, info.getUrl());
				getContext().startActivity(intent);
			}
		}
	}

	
}
