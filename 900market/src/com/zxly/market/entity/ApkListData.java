
package com.zxly.market.entity;

import java.util.List;
/**
 * apk分页列表
 * @author fengruyi
 *
 */
public class ApkListData extends BaseResponseData{
	
	private List<ApkInfo>  apkList;
	
	private int pageSize;
	
	private int currPage;
	
	private int recordCount;
	
	private int countPage;

	public List<ApkInfo> getApkList() {
		return apkList;
	}

	public void setApkList(List<ApkInfo> apkList) {
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
