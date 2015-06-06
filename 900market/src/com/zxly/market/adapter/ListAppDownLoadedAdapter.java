package com.zxly.market.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.view.RoundedImageView;
/**
 * 下载完成任务列适配器
 * @author fengruyi
 *
 */
public class ListAppDownLoadedAdapter extends ZXBaseAdapter<DownLoadTaskInfo> implements OnClickListener{
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_app_defaul)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_app_defaul)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_app_defaul)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成  
	public ListAppDownLoadedAdapter(Context context,List<DownLoadTaskInfo> list){
		super(context, list);
	}
	
	public void addItem(DownLoadTaskInfo task){
		mlist.add(0, task);
		notifyDataSetChanged();
	}
	@Override
	public int itemLayoutRes() {
		
		return R.layout.item_list_downloaded;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {
		DownLoadTaskInfo task = mlist.get(position);
		RoundedImageView iv_icon = holder.obtainView(convertView, R.id.iv_app_icon);
		TextView tv_name = holder.obtainView(convertView, R.id.tv_app_name);
		TextView tv_size = holder.obtainView(convertView, R.id.tv_app_size);
		TextView tv_version  = holder.obtainView(convertView, R.id.tv_version);
		Button btn_open = holder.obtainView(convertView, R.id.btn_down);
		ImageLoaderUtil.Load(task.getIconUrl(), iv_icon, options);
		btn_open.setTag(task);
		tv_name.setText(task.getFileName());
		tv_size.setText(Formatter.formatFileSize(context, task.getFileLength()));
		tv_version.setText("版本:"+task.getVersionName());
		if(BaseApplication.getInstance().getInstalledApk(task.getPackageName())!=null){
			btn_open.setText(R.string.open);
			btn_open.setSelected(true);
		}else{
			btn_open.setText(R.string.install);
			btn_open.setSelected(false);
		}
		btn_open.setOnClickListener(this);
		return convertView;
	}

	public void onClick(View v) {
		DownLoadTaskInfo task =  (DownLoadTaskInfo) v.getTag();
		if(v.isSelected()){//打开
			AppUtil.startApk(task);
		}else{//安装
			AppUtil.installApk(context, task);
		}
	}

}
