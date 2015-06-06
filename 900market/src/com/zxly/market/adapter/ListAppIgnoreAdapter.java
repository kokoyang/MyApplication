/**    
 * @FileName: ListAppUpgrade.java  
 * @Package:com.zxly.market.adapter  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-13 下午1:38:05  
 * @version V1.0    
 */
package com.zxly.market.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.view.RoundedImageView;

/** 
 * @ClassName: ListAppUpgrade  
 * @Description: 已经忽略升级应用列表适配器 
 * @author: fengruyi 
 * @date:2015-4-13 下午1:38:05   
 */
public class ListAppIgnoreAdapter extends ZXBaseAdapter<ApkInfo> implements OnClickListener{
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_app_defaul)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_app_defaul)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_app_defaul)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成 
	private DbUtils db;
	public ListAppIgnoreAdapter(Context context,List<ApkInfo> list,DbUtils db){
		super(context, list);
		this.db = db;
	}
	
	public int itemLayoutRes() {
		
		return R.layout.item_list_ignore_app;
	}

	
	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {
		ApkInfo apkInfo = mlist.get(position);
		RoundedImageView iv_appIcon = holder.obtainView(convertView, R.id.iv_app_icon);
		TextView tv_appName = holder.obtainView(convertView, R.id.tv_app_name);
		RatingBar rb_rank = holder.obtainView(convertView, R.id.rb_rank);
		TextView tv_appSize = holder.obtainView(convertView, R.id.tv_app_size);
		TextView tv_description = holder.obtainView(convertView, R.id.tv_app_info);
		Button btn_upgrade = holder.obtainView(convertView, R.id.btn_cancel_ignore);
		ImageLoaderUtil.Load(apkInfo.getIcon(), iv_appIcon, options);
    	rb_rank.setEnabled(false);
    	tv_appName.setText(apkInfo.getAppName());
    	rb_rank.setRating(apkInfo.getGrade());
    	tv_appSize.setText(apkInfo.getSize()+"MB");
    	tv_description.setText(Html.fromHtml(apkInfo.getContent()));	
    	btn_upgrade.setTag(position);
    	btn_upgrade.setOnClickListener(this);
		return convertView;
	}

	public void onClick(View v) {
		int index = (Integer) v.getTag();
		try {
			db.delete(mlist.get(index));
			if(BaseApplication.getInstance().needUpgradeList!=null){
				BaseApplication.getInstance().needUpgradeList.add(mlist.get(index));
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		mlist.remove(index);
		notifyDataSetChanged();
	}
	
}
