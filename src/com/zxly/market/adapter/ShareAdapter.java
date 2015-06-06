/**    
 * @FileName: ShareAdapter.java  
 * @Package:com.zxly.market.adapter  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-13 下午1:38:05  
 * @version V1.0    
 */
package com.zxly.market.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zxly.market.R;

/** 
 * @ClassName: ShareAdapter  
 * @Description: 分享列表适配器
 * @author: fengruyi 
 * @date:2015-4-13 下午1:38:05   
 */
public class ShareAdapter extends ZXBaseAdapter<Object>{
	private int[] nameArray ;
	private int[] drawbleArray;
	public ShareAdapter(Context context){
		this.context = context;
		nameArray = new int[]{R.string.sina,R.string.wechat,R.string.wechat_circle,R.string.qzone};
		drawbleArray = new int[]{R.drawable.icon_sina,R.drawable.icon_wechat,R.drawable.icon_wxcircle,R.drawable.icon_qzone};
	}
	/* (non-Javadoc)  
	 * @return  
	 * @see com.zxly.market.adapter.ZXBaseAdapter#getCount()  
	 */  
	public int getCount() {
		return nameArray.length;//test
	}
	/* (non-Javadoc)  
	 * @return  
	 * @see com.zxly.market.adapter.ZXBaseAdapter#itemLayoutRes()  
	 */  
	public int itemLayoutRes() {
		
		return R.layout.item_share;
	}

	/* (non-Javadoc)  
	 * @param position
	 * @param convertView
	 * @param parent
	 * @param holder
	 * @return  
	 * @see com.zxly.market.adapter.ZXBaseAdapter#getView(int, android.view.View, android.view.ViewGroup, com.zxly.market.adapter.ZXBaseAdapter.ViewHolder)  
	 */  
	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {
		TextView tv_appName = holder.obtainView(convertView, R.id.tv_app_name);
		tv_appName.setText(nameArray[position]);
		tv_appName.setCompoundDrawablesWithIntrinsicBounds(0, drawbleArray[position], 0, 0);
		return convertView;
	}
	
}
