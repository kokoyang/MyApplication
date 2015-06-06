package com.zxly.market.entity;

import java.util.List;


public class HotKeyDatas extends BaseResponseData{
	
	private List<HotKeyInfo>  apkList;
	
	private int pageSize;
	
	private int currPage;
	
	private int recordCount;
	
	private int countPage;
	
	public List<HotKeyInfo> getApkList() {
		return apkList;
	}
	public void setApkList(List<HotKeyInfo> apkList) {
		this.apkList = apkList;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getCountPage() {
		return countPage;
	}
	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}
	
	
}
