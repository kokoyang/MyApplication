package com.zxly.market.entity;

/**
 * 广告实体信息
 * @author fengruyi
 *
 */
public class BanerInfo {
	/**图片地址*/
	private String imgUrl;
	/**类型，专题or详情*/
	private int type ;
	/**包名*/
	private String specName;
	/**跳转地址*/
	private String url;
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	
	
	
}
