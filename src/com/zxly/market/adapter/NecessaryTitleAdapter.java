package com.zxly.market.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.entity.Category2nd;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class NecessaryTitleAdapter extends BaseAdapter {
	private ArrayList<Category2nd> mList =  new ArrayList<Category2nd>();
    private Context mContext;
    private String mCurrentTab;
    private float mDensity;
    private float mWidth;

    public NecessaryTitleAdapter(Context context) {
        mDensity = context.getResources().getDisplayMetrics().density;
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mContext = new WeakReference<Context>(context).get();
    }

    public void setTitles(ArrayList<Category2nd> titles) {
    	mList = titles;
    }
    
    public void setCurrentTab(String currentTab) {
    	mCurrentTab = currentTab;
    }

	public int getCount() {
        return Integer.MAX_VALUE;
    }

    public Object getItem(int position) {
    	if(null != mList){
    		return mList.get(position % mList.size()).getClassName();
    	} else {
    		return position;
    	}
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	TextView tv;
        if (convertView == null) {
            convertView = new TextView(mContext);
            tv = (TextView) convertView;
            tv.setLayoutParams(new Gallery.LayoutParams((int) (mWidth/3), (int) (37 * mDensity)));
            tv.setGravity(Gravity.CENTER);
        } else {
        	tv = (TextView) convertView;
        }
        if(null == mList || mList.size() == 0 || null == mCurrentTab) return tv;
        String title = mList.get(position % mList.size()).getClassName();
        tv.setText(title);
        
        if(mCurrentTab.equals(title)) {
        	tv.setTextColor(0xff55BF17);
        } else {
        	tv.setTextColor(0xff666666);
        }
        tv.setBackgroundResource(R.drawable.necessary_title_line);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        return tv;
    }
}
