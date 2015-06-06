package com.zxly.market.model;

/**
 * 统一控制器基类
 * @author fengruyi
 *
 */
public interface BaseIterfaceView {
	
	/**
	 * 显示没有数据UI
	 */
	public void showEmptyView();
	
	/**
	 * 显示网络请求失败UI
	 */
	public void showRequestErro();
	
	/**
	 * 显示没有网络UI
	 */
	public void showNoNetwork();
}
