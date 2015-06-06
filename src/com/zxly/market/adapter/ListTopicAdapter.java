/**    
 * @FileName: ListTopicAdapter.java  
 * @Package:com.zxly.market.adapter  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-15 上午10:00:34  
 * @version V1.0    
 */
package com.zxly.market.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.entity.SpecialInfo;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.ViewUtil;


/** 
 * @ClassName: ListTopicAdapter  
 * @Description: 专题列表 
 * @author: fengruyi 
 * @date:2015-4-15 上午10:00:34   
 */
public class ListTopicAdapter extends ZXBaseAdapter<SpecialInfo>{
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_banner_defalt)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_banner_defalt)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_banner_defalt)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成  
	public ListTopicAdapter(Context context,List<SpecialInfo> list){

		super(context, list);
	}

	public int itemLayoutRes() {
		
		return R.layout.item_list_topic;
	}

	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {

//		Logger.e("ListTopicAdapter", "getView-->");
		SpecialInfo info = mlist.get(position); 
		ImageView iv_icon = holder.obtainView(convertView, R.id.iv_topic);
		TextView tv_title = holder.obtainView(convertView, R.id.tv_topic_title);
		ImageLoaderUtil.Load(info.getSpecImgUrl(), iv_icon, options);
		tv_title.setText(info.getClassName());
		ViewUtil.setViewHeightByScale(iv_icon,0.172f);
		return convertView;
	}
	   
	
}
