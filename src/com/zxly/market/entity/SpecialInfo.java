package com.zxly.market.entity;

/**
 * 专题实体类
 * @author fengruyi
 *
 */
public class SpecialInfo {
	/**图片地址*/
	private String specImgUrl;
	/**跳转地址*/
	private String specUrl;
	/***/
	private String classCode;
	/**标题*/
	private String className;
	
	public String getSpecImgUrl() {
		return specImgUrl;
	}
	public void setSpecImgUrl(String specImgUrl) {
		this.specImgUrl = specImgUrl;
	}
	public String getSpecUrl() {
		return specUrl;
	}
	public void setSpecUrl(String specUrl) {
		this.specUrl = specUrl;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	
}
