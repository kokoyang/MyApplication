package com.zxly.market.model;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.SpecialListData;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;

/**
 * 专题列表控制器
 * @author Administrator
 *
 */
public class SpecialActivityControler extends BaseControler{
	   private ISpecialView iSpecialView;
	   private int pageSize = 10;
	   private int currentPage = 1;
	   private boolean isLastpage;//是否已经是最后一页
	   public SpecialActivityControler(ISpecialView iSpecialView){
		   this.iSpecialView = iSpecialView;
	   }
	    
	   /**
	    * 获取专题列表
	    * @param isLoadmore 是否是加载更多
	    */
		public void loadSepcial(boolean isLoadmore){
			if(!BaseApplication.getInstance().isOnline()){
				iSpecialView.showNoNetwork();
				return;
			}
			if(!isLoadmore){
				isLastpage = false;
				currentPage = 1;
			}
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("currPage", currentPage+"");
			params.addQueryStringParameter("pageSize", pageSize+"");
			HttpHelper.send(HttpMethod.GET, Constant.GET_SPECIAL_LIST,params, new HttpCallBack() {	
				public void onSuccess(String result) {
					Logger.e("","请求得到的结果：--->"+result);
//					Type  type = new TypeToken<BaseResponseData<HotAppList>>(){}.getType();
//					
					if (isFinish())return;
					@SuppressWarnings("unchecked")
					SpecialListData data = GjsonUtil.json2Object(result, SpecialListData.class);
					if(data!=null&&data.getApkList()!=null){
						isLastpage = (data.getCountPage() == data.getCurrPage());
						if(currentPage==1){
							iSpecialView.showSpecialData(data.getApkList());
						}else{
							iSpecialView.showMoreSpecialData(data.getApkList());
						}
					    currentPage++;
					}else{//出错的情况
						if(currentPage==1){
							iSpecialView.showRequestErro();
						}else{
							iSpecialView.loadMoreFail();
						}
					}
				}
				
				public void onFailure(HttpException e, String msg) {
					if (isFinish())return;
					if(currentPage==1){
						iSpecialView.showRequestErro();
					}else{
						iSpecialView.loadMoreFail();
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
