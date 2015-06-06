/**    
 * @FileName: IAppDetailView.java  
 * @Package:com.zxly.market.model  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-13 下午8:50:30  
 * @version V1.0    
 */
package com.zxly.market.model;

import com.zxly.market.entity.AppDetalData;
import com.zxly.market.entity.CommentData;

/** 
 * @ClassName: IAppDetailView  
 * @Description: 应用详情的接口 
 * @author: fengruyi 
 * @date:2015-4-13 下午8:50:30   
 */
public interface IAppDetailView extends BaseIterfaceView{
	   
	    /***
	    *显示用户评论数据
	    * @param data
	    */
	   public void showCommentData(CommentData data);
	   /**
	    * 显示更多用户数据
	    * @param data
	    */
	   public void showMoreCommentData(CommentData data);
	   
	   /**
	    * 提交评论失败
	    */
	   public void saveCommentFailue();
	   
	   /**
	    * 提交评论成功
	    * @param iconUrl
	    */
	   public void svaeCommentSuccess();
	   
	   /**
	    * 显示应用详情
	    * @param data
	    */
	   public void showDetailData(AppDetalData data);
	   
}
