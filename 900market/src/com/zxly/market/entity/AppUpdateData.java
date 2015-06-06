package com.zxly.market.entity;

import java.util.List;

/**
 * 自身应用 升级信息
 * @author Administrator
 *
 */
public class AppUpdateData extends BaseResponseData{
	
	private List<UpdateInfo> apkList;
	
	public List<UpdateInfo> getApkList() {
		return apkList;
	}

	public void setApkList(List<UpdateInfo> apkList) {
		this.apkList = apkList;
	}
}
