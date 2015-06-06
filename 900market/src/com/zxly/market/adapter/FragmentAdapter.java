/**
* Copyright(C)2012-2013 深圳市壹捌无限科技有限公司版权所有
* 创 建 人:Jacky
* 修 改 人:Gofeel
* 创 建日期:2013-7-19
* 描    述:
* 版 本 号:
*/ 
package com.zxly.market.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zxly.market.entity.Category2nd;
import com.zxly.market.fragment.SortSubFragment;

import java.util.ArrayList;

/**
 * @author Jacky
 * Fragment适配
 */
public class FragmentAdapter extends FragmentPagerAdapter {
	private ArrayList<Category2nd> mList =  new ArrayList<Category2nd>();
	
	public static final int LOOPCOUNT = 1000; 
	public static final int MIDCOUNT = LOOPCOUNT / 2;
	
    private ArrayList<Fragment> mFragmentsList;
    private boolean mIsCircle;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments, boolean isCircle) {
        super(fm);
        mFragmentsList = fragments;
        mIsCircle = isCircle;
    }

    @Override
    public int getCount() {
    	if(mIsCircle) {
    		return LOOPCOUNT;
    	} else {
    		return mFragmentsList.size();
    	}
    }

    @Override
    public Fragment getItem(int position) {
    	if(mIsCircle) {
    		SortSubFragment fragment = SortSubFragment.newInstance(
                    mList.get(position % mFragmentsList.size()).getClassName(),
                    mList.get(position % mFragmentsList.size()).getClassCode());
			return fragment;
    	} else {
    		return mFragmentsList.get(position);
    	}
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    
    public void setName(ArrayList<Category2nd> name) {
    	mList = name;
    }
}
