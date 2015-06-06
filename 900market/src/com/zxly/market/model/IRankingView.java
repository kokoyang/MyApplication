package com.zxly.market.model;

import java.util.List;

import com.zxly.market.entity.ApkInfo;

public interface IRankingView extends BaseIterfaceView{
	/**
	 * 
	 * @Title: showHotApp  
	 * @Description: 显示列表
	 * @param @param object 
	 * @return void 
	 * @throws
	 */
	public void showApp(List<ApkInfo> object);
	
	
	/***
	 * 
	 * @Title: showMoreHotApp  
	 * @Description: 加载更多 
	 * @param @param object 
	 * @return void 
	 * @throws
	 */
	public void showMoreApp(List<ApkInfo> object);
	
	/**
	 * 加载更多失败
	 */
	public void loadMoreFail();
	
}
