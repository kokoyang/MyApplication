package com.zxly.market.model;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.AppDetailInfo;
import com.zxly.market.entity.BaseResponseData;
import com.zxly.market.entity.CommentData;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;

/**
 * 应用详情评论逻辑控制
 * @author fengruyi
 *
 */
public class AppCommentControler {
	private final String TAG = "AppCommentControler";
	private IAppCommentView iAppCommentView;	
	private String pageSize = "15";//页长度
	private int currPage = 1;//当前页
	private boolean isLastPage;
	public AppCommentControler(IAppCommentView iAppCommentView){
		this.iAppCommentView = iAppCommentView;
	}
	
	/**
	 * 根据包名请求评论数据
	 * @param packname
	 */
	public void loadCommentData(String packname,boolean isLoadmore){
		if(!isLoadmore){
			isLastPage = false;
			currPage = 1;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currPage", currPage+"");
		params.addQueryStringParameter("pageSize", pageSize);
		params.addQueryStringParameter("packname", packname);
		HttpHelper.send(HttpMethod.GET, Constant.GET_COMMENTS, params, new HttpCallBack() {
			
			public void onSuccess(String result) {
				CommentData data = GjsonUtil.json2Object(result, CommentData.class);
				Logger.e(TAG, "取得的评论-->"+result);
				if(data!=null&&data.getStatus()==200){
					isLastPage = data.getCurrPage() == data.getCountPage();
					
					if(currPage==1){
						iAppCommentView.showCommentData(data);
					}else{
						iAppCommentView.showMoreCommentData(data);
					}
					currPage ++;
				}else{
					
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				
				iAppCommentView.showRequestErorr();
				
			}
		});
	}
	
	public void submitComment(AppDetailInfo detailInfo,String userName,String content,int grade){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("packName", detailInfo.getPackName());
		params.addQueryStringParameter("apkName", detailInfo.getAppName());
		params.addQueryStringParameter("rank", grade+"");
		params.addQueryStringParameter("userName", userName);
		params.addQueryStringParameter("content", content);
		params.addQueryStringParameter("classCode", detailInfo.getClassCode());
		params.addQueryStringParameter("appId", detailInfo.getId()+"");
		HttpHelper.send(HttpMethod.POST, Constant.SAVE_COMMENT, params, new HttpCallBack() {
			
			public void onSuccess(String result) {
				BaseResponseData data = GjsonUtil.json2Object(result, BaseResponseData.class);
				if(data!=null&&data.getStatus()==200){	
					iAppCommentView.svaeCommentSuccess("");
			
				}else{
					iAppCommentView.saveCommentFailue();
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				
				iAppCommentView.saveCommentFailue();
				
			}
		});
	}
	
	public boolean isLastPage(){
		return isLastPage;
	}
}
