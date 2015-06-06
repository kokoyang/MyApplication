package com.zxly.market.entity;

/***
 * 评论信息表
 * @author fengruyi
 *
 */
public class CommentInfo {
	/**头像url*/
	private String imgUrl;
	
	/**用户名*/
	private String uname;
	
	/**评论内容*/
	private String content;
	
	/**用户 评分*/
	private int rank;
	
	/**评论时间 */
	private String time;

	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	

}
