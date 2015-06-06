package com.zxly.market.entity;

import java.util.List;

/**
 * 精品页第一个接口，banner,推荐列表
 * @author fengruyi
 *
 */
public class ClassicTopData extends BaseResponseData{
	
	private List<ApkInfo> apkList;
	
	private List<BanerInfo> banAdList;
	
	private List<BanerInfo> jxAdList;
	
	public List<ApkInfo> getApkList() {
		return apkList;
	}
	public void setApkList(List<ApkInfo> apkList) {
		this.apkList = apkList;
	}
	public List<BanerInfo> getBanAdList() {
		return banAdList;
	}
	public void setBanAdList(List<BanerInfo> banAdList) {
		this.banAdList = banAdList;
	}
	public List<BanerInfo> getJxAdList() {
		return jxAdList;
	}
	public void setJxAdList(List<BanerInfo> jxAdList) {
		this.jxAdList = jxAdList;
	}
}
