package com.zxly.market.model;

import java.util.List;

import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.HotKeyInfo;

/**
 * 热闹关键字搜索接口
 * @author Administrator
 *
 */
public interface IHotKeyView extends BaseIterfaceView{
    	
	/**
	 * 
	 * @param obj
	 */
	public void showHotKeysData(List<HotKeyInfo> obj);
	
	public void showMoreHotKeysData(List<HotKeyInfo> obj);
	
	/**
	 * 显示搜索结果
	 * @param obj
	 */
	public void showSearchResult(List<ApkInfo> obj);
	/**
	 * 显示更多查询结果，用于加载更多
	 * @param obj
	 */
	public void showMoreResult(List<ApkInfo> obj);
	
	public void loadMoreFail();
}
