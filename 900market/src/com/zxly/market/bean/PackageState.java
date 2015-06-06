package com.zxly.market.bean;

/**
 * 指定包名在手机中的状态
 * @author fengruyi
 *
 */
public enum PackageState {
	
	INSTALLED,//已经安装
	
	NOEXIST,//不存在，需要去下载
	
	DOWNLOADED,//已经下载完毕，需要去安装
	
	KEEPDOWNLOAD,//未下载完,正在下载中
	
	NEEDUPDATE,//需要更新
	
	CANCEL,//取消下载
	
	FAIL,//下载失败的
}
