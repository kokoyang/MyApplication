package com.zxly.market.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.utils.ViewUtil;

/**
 * 应用 通用带有点击滑动切换AB标题的布局
 * @author fengruyi
 *
 */
public class TabSwitchPagerView extends RelativeLayout implements OnClickListener{
	/** 切换页面的水平指示条*/
	private View mTabDiliver;
	/** 正在下载tab标题*/
	private TextView mTabA;
	/** 已经完成tab标题*/
	private TextView mTabB;
	
	private ViewPager mViewPager;
	/**当前显示ViewPager页面的索引*/
	private int mCurrenIndex = 0;
    
	private Context mContext;
	
	private SwitchChangeListener listner;
	
	public TabSwitchPagerView(Context context) {
		super(context);
		mContext = context;
	}
    
	public TabSwitchPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	/**
	 * 设置tab标题文字
	 * @param titleId1 tabA标题 resid
	 * @param titleId2 tabB标题 resid
	 */
	public void setTabTitle(int titleId1,int titleId2){
		mTabA.setText(titleId1);
		mTabB.setText(titleId2);
	}
	
	/**
	 * 设置tab标题文字
	 * @param titleId1 tabA标题 Str
	 * @param titleId2 tabB标题 Str
	 */
	public void setTabTtile(String title1,String title2){
		mTabA.setText(title1);
		mTabB.setText(title2);
	}
	
	/**
	 * 为viewpage适配apapter
	 * @param adapter
	 */
	public void setPagerAdapter(PagerAdapter adapter){
		mViewPager.setAdapter(adapter);
	}
	
	/**
	 * 返回当前viewpager显示的索引
	 * @return
	 */
	public int getCurrenItem(){
		return mViewPager.getCurrentItem();
	}
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTabDiliver = findViewById(R.id.tab_diliver);
		mTabA = (TextView) findViewById(R.id.tv_taba);
		mTabB = (TextView) findViewById(R.id.tv_tabb);
		mViewPager = (ViewPager) findViewById(R.id.vp_pager);
		ViewUtil.setViewWidth(mTabDiliver, (int)BaseApplication.mWidthPixels/2);//设置水平指示条占宽的一半
		ViewUtil.setOnClickListener(this, mTabA,mTabB);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int position) {
				switchTab(position);
				if(listner!=null){
					listner.onPageSelected(position);
				}
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
    
	
	/**
	 * tab页切换
	 * @param index
	 */
	private void switchTab(int index){
		if(index == mCurrenIndex)
			return;
		if(index == 0){
			mTabA.setTextAppearance(mContext,R.style.Main_tab_selected);
			mTabB.setTextAppearance(mContext,R.style.Main_tab_unselected);
			RelativeLayout.LayoutParams lp = (LayoutParams) mTabDiliver.getLayoutParams();
			lp.setMargins(0, 0, 0, 0);
			mTabDiliver.setLayoutParams(lp);
		}else{
			mTabA.setTextAppearance(mContext,R.style.Main_tab_unselected);
			mTabB.setTextAppearance(mContext,R.style.Main_tab_selected);
			RelativeLayout.LayoutParams lp = (LayoutParams) mTabDiliver.getLayoutParams();
			lp.setMargins((int)BaseApplication.mWidthPixels/2, 0, 0, 0);
			mTabDiliver.setLayoutParams(lp);
		}
		mCurrenIndex = index;
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_taba:
			mViewPager.setCurrentItem(0, true);
			break;
		case R.id.tv_tabb:
			mViewPager.setCurrentItem(1, true);
			break;
		default:
			break;
		}
		
	}
	
	public SwitchChangeListener getListner() {
		return listner;
	}

	public void setListner(SwitchChangeListener listner) {
		this.listner = listner;
	}

	public interface SwitchChangeListener{
		public void onPageSelected(int index);
	}
}
