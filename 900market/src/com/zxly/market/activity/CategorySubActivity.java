/**
B* Copyright(C)2012-2013 深圳市壹捌无限科技有限公司版权所有
* 创 建 人:	Jacky
* 修 改 人:	Gofeel
* 创 建日期:	2013-7-17
* 描	   述:	必备子界面
* 版 本 号:	1.0
*/ 
package com.zxly.market.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.adapter.FragmentAdapter;
import com.zxly.market.adapter.NecessaryTitleAdapter;
import com.zxly.market.entity.Category2nd;
import com.zxly.market.fragment.SortSubFragment;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.TopGallery;

import java.util.ArrayList;

/**
 * 必备子界面
 *
 */
public class CategorySubActivity extends BaseFragmentActivity implements View.OnClickListener {
	private ArrayList<Category2nd> mList =  new ArrayList<Category2nd>();

	private static final int LOOPCOUNT = 1000;
	private static final int MIDCOUNT = LOOPCOUNT / 2;

	private NecessaryTitleAdapter mAdapter;
	private ViewPager mPager;
	private TopGallery mGallery;

	private TextView mTitleTv;
	private String mCurrentPage;
	private String title;
	private int mCurrentTab;
	private int mSize;
	private ArrayList<Fragment> fragmentsList;

	/** 标题集合信息 */
	public static final String EXTRA_TITLES = "extra_titles";

	@Override
	public int getContentViewId() {
		return R.layout.activity_necessary_sub_page;
	}

	@Override
	public void initViewAndData() {
		mList = getIntent().getParcelableArrayListExtra(CategorySubActivity.EXTRA_TITLES);
		title = mList.get(0).getClassName();
		mSize = mList.size();
		doInitView();
	}

	/** 初始化视图 */
	private void doInitView() {
		initTopBar();
		initTopGroup();
		initViewPager();
	}

	/** 初始化顶部标题 */
	private void initTopBar() {
		mTitleTv = (TextView) findViewById(R.id.tv_back);
		mTitleTv.setText(title);
		mTitleTv.setOnClickListener(this);

	}

	private void initTopGroup() {
		mGallery = (TopGallery) findViewById(R.id.gl_necessary_title);
		mAdapter = new NecessaryTitleAdapter(this);
		mAdapter.setTitles(mList);
		mAdapter.setCurrentTab(title);
		mGallery.setAdapter(mAdapter);
		mCurrentTab = MIDCOUNT/mSize*mSize;
		mGallery.setSelection(mCurrentTab);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 != mCurrentTab){
					String title = arg0.getAdapter().getItem(arg2)+"";
					mTitleTv.setText(title);
					mAdapter.setCurrentTab(title);
					mAdapter.notifyDataSetChanged();
					mCurrentPage = mList.get(arg2%mSize).getClassName();
					//handler.obtainMessage(HANDLER_TAB, arg2, 0).sendToTarget();
					mPager.setCurrentItem(arg2, true);
					mCurrentTab = arg2;
				}
			}
		});
	}

	/**
     * 初始化viewpager
     */
    private void initViewPager() {
        fragmentsList = new ArrayList<Fragment>();
        for(int i=0; i<mList.size(); i++) {
        	fragmentsList.add(SortSubFragment.newInstance(mList.get(i).getClassName(),
					mList.get(i).getClassCode()));
        }
        mPager = (ViewPager) findViewById(R.id.vp_necessary_sub_vPager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentsList, true);
        adapter.setName(mList);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(MIDCOUNT/mSize*mSize, false);
        mPager.setOffscreenPageLimit(3);
        mCurrentPage = title;
    }
    
    public String getCurrentPage() {
    	return mCurrentPage;
    }

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.tv_back){
			finish();
		}
	}

	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @SuppressWarnings("deprecation")
		@Override
        public void onPageSelected(int arg0) {
        	if(arg0 != mCurrentTab){
        		try{
            		mCurrentPage = mList.get(arg0%mSize).getClassName();
            		mTitleTv.setText(mCurrentPage);
            	} catch(Exception e) {
            		Logger.w("nesscessary", e.toString());
            	}
        		mAdapter.setCurrentTab(mCurrentPage);
    			mAdapter.notifyDataSetChanged();
        		//handler.obtainMessage(HANDLER_CONTENT, mCurrentTab-arg0, 0).sendToTarget();
				if(mCurrentTab-arg0<0) {
					mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
				} else {
					mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
				}
            	mCurrentTab = arg0;
        	}
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}


