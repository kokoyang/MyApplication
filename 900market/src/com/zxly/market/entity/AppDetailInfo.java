package com.zxly.market.entity;


/**
 * 应用详情
 * @author fengruyi
 *
 */
public class AppDetailInfo {
	
	    private long id; 
	    
		/**应用图标*/
	    private String icon;
	    /**分类*/
	    private String catName;
	    /**应用名*/
		private String appName;
		/**应用包名*/
		private String packName;
		private String downUrl;
		/**应用大小*/
		private float size;
		/**评分*/
		private float rating;
		/***/
		private int grade;
		/**下载量*/
		private int downCount;
		/**开发者*/
		private String author;
		/**版本名称*/
		private String verName;
		/**图集合*/
		private String detailUrls;
		/**介绍*/
		private String content;
		
		private String classCode;

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
			return (long)(size*1000*1000);
		}
		public void setSize(float size) {
			this.size = size;
		}
		public float getRating() {
			return rating;
		}
		public void setRating(float rating) {
			this.rating = rating;
		}
		public int getDownCount() {
			return downCount;
		}
		public void setDownCount(int downCount) {
			this.downCount = downCount;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getVerName() {
			return verName;
		}
		public void setVerName(String verName) {
			this.verName = verName;
		}
		public String getDetailUrls() {
			return detailUrls;
		}
		public void setDetailUrls(String detailUrls) {
			this.detailUrls = detailUrls;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getCatName() {
			return catName;
		}
		public void setCatName(String catName) {
			this.catName = catName;
		}
		public int getGrade() {
			return grade;
		}
		public void setGrade(int grade) {
			this.grade = grade;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getClassCode() {
			return classCode;
		}
		public void setClassCode(String classCode) {
			this.classCode = classCode;
		}
		public String getDownUrl() {
			return downUrl;
		}
		public void setDownUrl(String downUrl) {
			this.downUrl = downUrl;
		}

	@Override
	public String toString() {
		return "AppDetailInfo{" +
				"id=" + id +
				", icon='" + icon + '\'' +
				", catName='" + catName + '\'' +
				", appName='" + appName + '\'' +
				", packName='" + packName + '\'' +
				", downUrl='" + downUrl + '\'' +
				", size=" + size +
				", rating=" + rating +
				", grade=" + grade +
				", downCount=" + downCount +
				", author='" + author + '\'' +
				", verName='" + verName + '\'' +
				", detailUrls='" + detailUrls + '\'' +
				", content='" + content + '\'' +
				", classCode='" + classCode + '\'' +
				", packType=" + packType +
				", source='" + source + '\'' +
				'}';
	}
}
