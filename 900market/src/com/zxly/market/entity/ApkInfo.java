/**
 * @FileName: ApkInfo.java
 * @Package:com.zxly.market.entity
 * @Description: TODO
 * @author: Administrator
 * @date:2015-4-9 下午4:56:21  
 * @version V1.0
 */
package com.zxly.market.entity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * @ClassName: ApkInfo
 * @Description: 网络请求中app列表中的apk信息
 * @author: fengruyi
 * @date:2015-4-9 下午4:56:21   
 */
public class ApkInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Transient使这个列被忽略，不存入数据库
	@Transient
	private long id;

	/**列表位置*/
	private int position;

	/**应用图标地址*/
	private String icon;

	@Transient
	private String hotUrl;

	/**应用名*/
	private String appName;
	/**应用包名*/
	@Id
	private String packName;
	/**应用大小*/
	private float size;

	/**应用类型*/
	@Transient
	private int type;

	/**应用评分*/
	private int grade;

	/**应用描述*/
	private String  content;

	/**应用下载量*/
	@Transient
	private long downCount;

	/**应用下载地址*/
	private String downUrl;

	/**应用版本号*/
	private String verCode;

	/**应用名称*/
	private String verName;

	@Transient
	private String classCode;

	/**是否是系统应用*/
	private boolean isSystemApp;
    
	private String detailUrl;

	private int packType;

	private String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getPackType() {
		return packType;
	}

	public void setPackType(int packType) {
		this.packType = packType;
	}

	public long getId() {

		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public String getHotUrl() {
		return hotUrl;
	}


	public void setHotUrl(String hotUrl) {
		this.hotUrl = hotUrl;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getPackName() {
		return packName;
	}


	public void setPackName(String packName) {
		this.packName = packName;
	}


	public float getSize() {
		return size;
	}

	public long getByteSize() {
		return (long)(size*1024*1024);
	}

	public void setSize(float size) {
		this.size = size;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getGrade() {
		return grade;
	}


	public void setGrade(int grade) {
		this.grade = grade;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public long getDownCount() {
		return downCount;
	}


	public void setDownCount(long downCount) {
		this.downCount = downCount;
	}


	public String getDownUrl() {
		return downUrl;
	}


	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}


	public String getVerCode() {
		return verCode;
	}


	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}


	public String getVerName() {
		return verName;
	}


	public void setVerName(String verName) {
		this.verName = verName;
	}


	public String getClassCode() {
		return classCode;
	}


	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}


	public boolean isSystemApp() {
		return isSystemApp;
	}


	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ApkInfo)) return false;

		ApkInfo that = (ApkInfo) o;

		if (!this.getPackName().equals(that.getPackName())) return false;

		return true;
	}


	public String getDetailUrl() {
		return detailUrl;
	}


	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}


}
