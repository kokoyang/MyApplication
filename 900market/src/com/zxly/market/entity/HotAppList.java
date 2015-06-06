/**    
 * @FileName: HotAppList.java  
 * @Package:com.zxly.market.entity  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-14 下午3:55:30  
 * @version V1.0    
 */
package com.zxly.market.entity;

import java.util.List;

/** 
 * @ClassName: HotAppList  
 * @Description: 热门推荐 
 * @author: fengruyi 
 * @date:2015-4-14 下午3:55:30   
 */
public class HotAppList extends BaseResponseData{
	
	private int currPage;
	
	private int pageSize;
	
	private int recordCount;
	
	private int countPage;
	
	private List<ApkInfo> apkList;
	
    private SpecialInfo special;

	public List<ApkInfo> getApkList() {
		return apkList;
	}

	public void setApkList(List<ApkInfo> apkList) {
		this.apkList = apkList;
	}

	public SpecialInfo getSpecial() {
		return special;
	}

	public void setSpecial(SpecialInfo special) {
		this.special = special;
	}

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
    
    
   
}
