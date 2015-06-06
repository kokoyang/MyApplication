package com.zxly.market.entity;

import java.util.List;

/***
 * 应用详情网络数据返回对照，包括相关列表和应用详情
 * @author fengruyi
 *
 */
public class AppDetalData extends BaseResponseData{
	
	private AppDetailInfo detail;
	
	private List<ApkInfo> relatedList;

	public AppDetailInfo getDetail() {
		return detail;
	}

	public void setDetail(AppDetailInfo detail) {
		this.detail = detail;
	}

	public List<ApkInfo> getRelatedList() {
		return relatedList;
	}

	public void setRelatedList(List<ApkInfo> relatedList) {
		this.relatedList = relatedList;
	}
	
	
}
