package com.zxly.market.model;

import java.util.List;

import com.zxly.market.entity.CommentData;

/**
 * 应用详情评论接口
 * @author fengruyi
 *
 */
public interface IAppCommentView {
	   
	   /***
	    *显示用户评论数据
	    * @param data
	    */
	   public void showCommentData(CommentData data);
	   
	   public void showMoreCommentData(CommentData data);
	   
	   /**
	    * 显示未连接网络
	    */
	   public void showNoConnection();
	   
	   /***
	    * 请求失败
	    */
	   public void showRequestErorr();
	   
	   /**
	    * 提交评论失败
	    */
	   public void saveCommentFailue();
	   
	   /**
	    * 提交评论成功
	    * @param iconUrl
	    */
	   public void svaeCommentSuccess(String iconUrl);
	   
}
