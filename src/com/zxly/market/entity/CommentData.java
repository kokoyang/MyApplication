package com.zxly.market.entity;

import java.util.List;

/***
 * 评论数据
 * @author fengruyi
 *
 */
public class CommentData extends BaseResponseData{
	
	private int currPage;
	
	private int pageSize;
	
	private int recordCount;
	
	private int countPage;
	/**评论列表*/
	private List<CommentInfo> apkList;
	/**自己已经评论过的*/
	private CommentInfo cmInfo;
	
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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
	public List<CommentInfo> getApkList() {
		return apkList;
	}
	public void setApkList(List<CommentInfo> apkList) {
		this.apkList = apkList;
	}
	public CommentInfo getCmInfo() {
		return cmInfo;
	}
	public void setCmInfo(CommentInfo cmInfo) {
		this.cmInfo = cmInfo;
	}
	
	
}
