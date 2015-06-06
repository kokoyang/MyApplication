/**    
 * @FileName: ListUninstallAPPAdapter.java  
 * @Package:com.zxly.market.adapter  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-9 上午9:40:31  
 * @version V1.0    
 */
package com.zxly.market.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.utils.PackageUtil;
import com.zxly.market.view.RoundedImageView;

/** 
 * @ClassName: ListUninstallAPPAdapter  
 * @Description: 应用卸载列表适配器 
 * @author: fengruyi 
 * @date:2015-4-9 上午9:40:31   
 */
public class ListUninstallAPPAdapter extends ZXBaseAdapter<ApkInfo> implements OnClickListener{
	
	public ListUninstallAPPAdapter(Context context,List<ApkInfo> list){
		super(context, list);
	}
	
	/* (non-Javadoc)  
	 * @return  
	 * @see com.zxly.market.adapter.ZXBaseAdapter#itemLayoutRes()  
	 */  
	@Override
	public int itemLayoutRes() {
		return R.layout.item_list_uninstall;
	}

	/* (non-Javadoc)  
	 * @param position
	 * @param convertView
	 * @param parent
	 * @param holder
	 * @return  
	 * @see com.zxly.market.adapter.ZXBaseAdapter#getView(int, android.view.View, android.view.ViewGroup, com.zxly.market.adapter.ZXBaseAdapter.ViewHolder)  
	 */  
	@Override
	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {
		ApkInfo appInfo = mlist.get(position);
		RoundedImageView appImageview = holder.obtainView(convertView, R.id.iv_app_icon);
		TextView tvAppName = holder.obtainView(convertView, R.id.tv_app_name);
		TextView tvAppVersion = holder.obtainView(convertView, R.id.tv_version);
		TextView tvAppSize = holder.obtainView(convertView, R.id.tv_app_size);
		Button btnDel = holder.obtainView(convertView, R.id.btn_del);
		try {
			
			appImageview.setImageDrawable(context.getPackageManager().getApplicationIcon(appInfo.getPackName()));
			//appImageview.setImageDrawable(context.createPackageContext(appInfo.getPackName(),Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY).getResources().getDrawable(appInfo.getIconid()));
		} catch (NotFoundException | NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvAppName.setText(appInfo.getAppName());
		tvAppVersion.setText("版本："+appInfo.getVerName());
		tvAppSize.setText("大小："+Formatter.formatFileSize(context, (int)appInfo.getSize()));
		btnDel.setTag(position);
		btnDel.setOnClickListener(this);
		return convertView;
	}

	public void removeItem(ApkInfo appinfo){
		mlist.remove(appinfo);
		notifyDataSetChanged();
	}

	/* (non-Javadoc)  
	 * @param arg0  
	 * @see android.view.View.OnClickListener#onClick(android.view.View)  
	 */  
	@Override
	public void onClick(View v) {
		int index = (Integer) v.getTag();
		PackageUtil.uninstallNormal(context, mlist.get(index).getPackName());
	}
}
