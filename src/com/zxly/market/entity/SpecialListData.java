package com.zxly.market.entity;

import java.util.List;

/**
 * 专题列表 数据
 * @author fengruyi
 *
 */
public class SpecialListData extends BaseResponseData{
	
	private List<SpecialInfo>  apkList;
	
    private int pageSize;
	
	private int currPage;
	
	private int recordCount;
	
	private int countPage;

	public List<SpecialInfo> getApkList() {
		return apkList;
	}

	public void setApkList(List<SpecialInfo> apkList) {
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
