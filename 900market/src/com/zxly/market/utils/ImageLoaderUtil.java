package com.zxly.market.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoaderUtil {

	public static void Load(String imgUrl,ImageView imageView){
	    ImageLoader.getInstance().displayImage(imgUrl, imageView);
	   }

   public static void Load(String imgUrl,ImageView imageView,DisplayImageOptions o){
     ImageLoader.getInstance().displayImage(imgUrl, imageView,o);
   }
   
   public static void load(String imgUrl,ImageView imageView,int defaultres){
	   
   }
   public static DisplayImageOptions getRoundOptions(int defaultres,int corner){
	   DisplayImageOptions options;  
	   options = new DisplayImageOptions.Builder()  
	   .showStubImage(defaultres)//加载中时显示的图片
	   .showImageForEmptyUri(defaultres)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(defaultres)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory()//设置下载的图片是否缓存在内存中  
	   .cacheOnDisc()//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .displayer(new RoundedBitmapDisplayer(corner))//是否设置为圆角，弧度为多少  
	   .build();//构建完成  
	   return options;
   }
   
}
