package com.zxly.market.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.entity.CommentInfo;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.view.CircleImageView;

public class AppCommentAdapter extends ZXBaseAdapter<CommentInfo>{
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_defaul_head)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_defaul_head)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_defaul_head)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成  
	public AppCommentAdapter(Context context,List<CommentInfo> list){
		super(context, list);
	}
	
	@Override
	public int itemLayoutRes() {
		return R.layout.item_app_comment;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {
		CommentInfo info = mlist.get(position);
		CircleImageView iv_icon = holder.obtainView(convertView, R.id.civ_head);
	    TextView tv_name  = holder.obtainView(convertView, R.id.tv_name);
	    TextView tv_content = holder.obtainView(convertView, R.id.tv_comment_content);
	    RatingBar rb_rating = holder.obtainView(convertView, R.id.rb_rank);
	    TextView tv_time = holder.obtainView(convertView, R.id.tv_date);
	    rb_rating.setEnabled(false);
	    ImageLoaderUtil.Load(info.getImgUrl(), iv_icon,options);
	    tv_name.setText(info.getUname());
	    tv_content.setText(info.getContent());
	    rb_rating.setRating(info.getRank()); 
	    tv_time.setText(info.getTime());
		return convertView;
	}

}
