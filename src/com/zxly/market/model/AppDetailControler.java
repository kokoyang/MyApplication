package com.zxly.market.model;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.AppDetailInfo;
import com.zxly.market.entity.AppDetalData;
import com.zxly.market.entity.BaseResponseData;
import com.zxly.market.entity.CommentData;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;

public class AppDetailControler extends BaseControler{
	private final String TAG = "AppDetailControler";
	private IAppDetailView iAppDetailView;
	private String pageSize = "15";//页长度
	private int currPage = 1;//当前页
	private boolean isLastPage;
	private int commentCount;//评论数
	public AppDetailControler(IAppDetailView iAppDetailView){
		this.iAppDetailView = iAppDetailView;
	}
	
	/**
	 * 根据url请求应用详情
	 * @param url
	 */
	public void loadAppDetail(final String url){
		if(!BaseApplication.getInstance().isOnline()){
			iAppDetailView.showNoNetwork();
			return;
		}
		HttpHelper.send(HttpMethod.GET,url, new HttpCallBack() {
			
			public void onSuccess(String result) {
				Logger.d("tag","detail url= "+url);
				Logger.d("tag","detail result= "+result);
				if(isFinish())return;
				AppDetalData data  = GjsonUtil.json2Object(result, AppDetalData.class);
				if(data!=null&&data.getStatus() == 200&&data.getDetail()!=null){
					iAppDetailView.showDetailData(data);
				}else{
					iAppDetailView.showRequestErro();
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				if(isFinish())return;
				iAppDetailView.showRequestErro();
			}
		});
	}
	
	/**
	 * 根据包名请求评论数据，
	 * @param packname
	 * @param isLoadmore 是否是加载更多的
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
				if(isFinish())return;
				CommentData data = GjsonUtil.json2Object(result, CommentData.class);
				if(data!=null&&data.getStatus()==200){
					commentCount = data.getRecordCount();
					isLastPage = data.getCurrPage() == data.getCountPage();
					if(currPage==1){
						iAppDetailView.showCommentData(data);
					}else{
						iAppDetailView.showMoreCommentData(data);
					}
					currPage ++;
				}else{
					iAppDetailView.showRequestErro();
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				 if(isFinish())return;
				 iAppDetailView.showRequestErro();
				
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
				if(isFinish())return;
				BaseResponseData data = GjsonUtil.json2Object(result, BaseResponseData.class);
				if(data!=null&&data.getStatus()==200){	
					iAppDetailView.svaeCommentSuccess();
			
				}else{
					iAppDetailView.saveCommentFailue();
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				if(isFinish())return;
				iAppDetailView.saveCommentFailue();
				
			}
		});
	}
	
	public boolean isLastPage(){
		return isLastPage;
	}

	public int getCommentCount() {
		return commentCount;
	}

	
}
