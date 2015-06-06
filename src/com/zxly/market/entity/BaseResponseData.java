
package com.zxly.market.entity;

import java.util.List;

/**
 *  基础网络请求返回数据格式 
 * @author fengruyi
 *
 */
public class BaseResponseData{
	
	
	protected  int status; 
	protected  String statusText;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
}
