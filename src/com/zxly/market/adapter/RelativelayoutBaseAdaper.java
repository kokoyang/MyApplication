package com.zxly.market.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zxly.market.entity.ApkInfo;

/**
 * 为RelativelayoutGridview写的适配器
 * @author fengruyi
 *
 */
public abstract class RelativelayoutBaseAdaper {
	public List<ApkInfo> mList;
	public Context mContext;
	
	public RelativelayoutBaseAdaper(Context context,List<ApkInfo> list){
		this.mContext = context;
		this.mList = list;
				
	}
	
	public LayoutInflater getLayoutInflater() {
	    if (mContext != null) {
	      return LayoutInflater.from(mContext);
	    }
	    return null;
	  }

	  public int getCount() {
	    if (mList != null) {
	      return  mList.size();
	    }

	    return 0;
	  };

	  public Object getItem(int position) {
	    if (mList != null) {
	      return mList.get(position);
	    }

	    return null;
	  };

	  /**
	   * 供子类复写
	   * 
	   * @param position
	   * @return
	   */
	  public abstract View getView(int position);
}
