package com.zxly.market.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.entity.HotKeyInfo;

/**
 * 搜索关键字适配器
 * @author fengruyi
 *
 */
public class KeysAdapter extends ZXBaseAdapter<HotKeyInfo>{
	
	private int pagesize = 9;
	private int currentpage = 0;
	private int count = 0;
	private int maxPage;
	private int length;
	public int getCount() {
		return count;
	}
	
	public KeysAdapter(Context context ,List<HotKeyInfo> list){
		super(context, list);	
		if(mlist==null){
			count = 0;
			return;
		}else{
			length = mlist.size();
			count = length;
			maxPage = (int) Math.ceil((length/(float)pagesize));
		} 
	}
    
	public void nextPage(){
		if(maxPage==1)return;
		if(currentpage>=maxPage-1){
			count = pagesize;
			currentpage = 0;
		}else if(currentpage == maxPage-2){	 
			currentpage++;
			count =length - currentpage*pagesize;
		}else{
			count = pagesize;
			currentpage++;
		}
		notifyDataSetChanged();
	}
	
	public int itemLayoutRes() {
		return R.layout.item_hot_key;
	}
	public void  addList(List<HotKeyInfo> list){
		mlist.addAll(list);
		currentpage++;
	    length = mlist.size();
		count = list.size();
		maxPage = (int) Math.ceil((length/(float)pagesize));
		notifyDataSetChanged();
	}
	public HotKeyInfo getByPosition(int position){
		return mlist.get(position+(pagesize*currentpage));
	}
	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {
		HotKeyInfo info = mlist.get(position+(pagesize*currentpage));
		TextView tvHotKey = holder.obtainView(convertView, R.id.tv_key);
		switch (position) {
		case 0:
			tvHotKey.setBackgroundResource(R.drawable.bg_rectangle_border_57be17);
			tvHotKey.setTextColor(context.getResources().getColor(R.color.color_57be17));
			break;
		case 1:
			tvHotKey.setBackgroundResource(R.drawable.bg_rectangle_border_6fc9e9);
			tvHotKey.setTextColor(context.getResources().getColor(R.color.color_6fc9e9));
			break;
		case 2:
			tvHotKey.setBackgroundResource(R.drawable.bg_rectangle_border_fe8d9a);
			tvHotKey.setTextColor(context.getResources().getColor(R.color.color_fe9e8a));
			break;
	    default:
	    	tvHotKey.setBackgroundResource(R.drawable.bg_rectangle_border_dddddd);
	    	tvHotKey.setTextColor(context.getResources().getColor(R.color.color_333333));
	    	break;
		}
		tvHotKey.setText(info.getKw());
		return convertView;
	}

}
