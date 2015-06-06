/**    
 * @FileName: IClassAppActivity.java  
 * @Package:com.zxly.market.model  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-14 下午4:08:47  
 * @version V1.0    
 */
package com.zxly.market.model;

import java.util.List;

import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.BanerInfo;
import com.zxly.market.entity.ClassicTopData;

/** 
 * @ClassName: IClassAppActivity  
 * @Description: TODO 
 * @author: Administrator 
 * @date:2015-4-14 下午4:08:47   
 */
public interface IClassAppActivity {
	
	/**
	 * 加载数据
	 */
	public void loadData();
	/**
	 * 显示精品第一个接口数据
	 * @param data
	 */
	public void showTopData(ClassicTopData data);
	
	/**
	 * 
	 * @Title: showBanner  
	 * @Description: 显示banner 
	 * @param @param object 
	 * @return void 
	 * @throws
	 */
	public void showBanner(List<BanerInfo> banners);
	
	/**
	 * 
	 * @Title: showRecomandApp  
	 * @Description: 显示推荐应用 
	 * @param @param object 
	 * @return void 
	 * @throws
	 */
	public void showRecomandApp(List<ApkInfo> apkList);
	
	/**
	 * 
	 * @Title: showHotApp  
	 * @Description: 显示热门推荐
	 * @param @param object 
	 * @return void 
	 * @throws
	 */
	public void showHotApp(List<ApkInfo> object);
	
	
	/***
	 * 
	 * @Title: showMoreHotApp  
	 * @Description: 加载更多 
	 * @param @param object 
	 * @return void 
	 * @throws
	 */
	public void showMoreHotApp(List<ApkInfo> object);
	
	/**
	 * 
	 * @Title: resizeView  
	 * @Description: 重新计算view的宽高，做适配
	 * @param  
	 * @return void 
	 * @throws
	 */
	public void resizeView();
	
	/**
	 * 热门推荐处请求出错是显示
	 */
	public void showHotArearError();
	
	/**
	 * 热门推荐处请求数据为空时显示
	 */
	public void showHtoArearEmpty();
	
	/**
	 * 显示空白页
	 */
	public void showEmptyView();
	/***
	 * 显示网络异常页面 
	 */
	public void showNetErrorView();
}
