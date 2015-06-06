package com.zxly.market.model;


import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkListData;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.GjsonUtil;

public class RankingListControler extends BaseControler{
	private IRankingView iRankingView;
	private int pageSize = 10;
	private int currentPage = 1;
	private boolean isLastpage;//是否已经是最后一页
	public RankingListControler(IRankingView iRankingView){
		this.iRankingView = iRankingView;
	}
	
	/**
	 * 
	 * @Title: loadApp  
	 * @Description: 加载app 
	 * @param @param isLoadmore 是否是加载or刷新
	 * @return void 
	 * @throws
	 */
	public void loadApp(String classCode,boolean isLoadmore){
		if(!BaseApplication.getInstance().isOnline()){
			iRankingView.showNoNetwork();
			return;
		}
		if(!isLoadmore){
			isLastpage = false;
			currentPage = 1;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("classCode", classCode);
		params.addQueryStringParameter("currPage", currentPage+"");
		params.addQueryStringParameter("pageSize", pageSize+"");
		HttpHelper.send(HttpMethod.GET, Constant.CATAGORY_SUB_URL,params, new HttpCallBack() {
			
			public void onSuccess(String result) {
				//Logger.e("","请求得到的结果：--->"+result);
//				Type  type = new TypeToken<BaseResponseData<HotAppList>>(){}.getType();
//				
				if (isFinish())return;
				@SuppressWarnings("unchecked")
				ApkListData data = GjsonUtil.json2Object(result, ApkListData.class);
				if(data!=null&&data.getApkList()!=null){
					
					isLastpage = (data.getCountPage() == data.getCurrPage());
					if(currentPage==1){
						iRankingView.showApp(data.getApkList());
					}else{
						iRankingView.showMoreApp(data.getApkList());
					}
				    currentPage++;
				}else{//出错的情况
					if(currentPage==1){
						iRankingView.showRequestErro();
					}else{
						iRankingView.loadMoreFail();
					}
				}
			}
			
			public void onFailure(HttpException e, String msg) {
				if (isFinish())return;
				if(currentPage==1){
					iRankingView.showRequestErro();
				}else{
					iRankingView.loadMoreFail();
				}
			}
		});
	}
	
	/**
	 * 
	 * @Title: isLastPage  
	 * @Description: 是否是最后一次 
	 * @param @return 
	 * @return boolean 
	 * @throws
	 */
	public boolean isLastPage(){
		
		return isLastpage;
	}
}
