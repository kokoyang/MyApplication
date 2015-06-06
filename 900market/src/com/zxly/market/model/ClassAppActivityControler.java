/**    
 * @FileName: ClassAppActivityControler.java  
 * @Package:com.zxly.market.model  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-14 下午4:02:18  
 * @version V1.0    
 */
package com.zxly.market.model;

import java.util.List;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.ClassicTopData;
import com.zxly.market.entity.FunctionNameEnum;
import com.zxly.market.entity.HotAppList;
import com.zxly.market.entity.SpecialInfo;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;

/** 
 * @ClassName: ClassAppActivityControler  
 * @Description: 精品页面的逻辑控制器 
 * @author: fengruyi 
 * @date:2015-4-14 下午4:02:18   
 */
public class ClassAppActivityControler {
	IClassAppActivity iClassAppActivity;
	private int pageSize = 5;
	private int currentPage = 1;
	private boolean isLastpage;//是否已经是最后一页
	public ClassAppActivityControler(IClassAppActivity iClassAppActivity){
		this.iClassAppActivity  = iClassAppActivity;
	}
	
	/**
	 * 
	 * @Title: loadHotApp  
	 * @Description: 加载热门app 
	 * @param @param isLoadmore 是否是加载or刷新
	 * @return void 
	 * @throws
	 */
	public void loadHotApp(boolean isLoadmore){
		if(!AppUtil.isOnline(BaseApplication.getInstance())){
			iClassAppActivity.showNetErrorView();
			return ;
		}
		if(!isLoadmore){
			isLastpage = false;
			currentPage = 1;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("classCode", "hot_arry");
		params.addQueryStringParameter("type", "0");
		params.addQueryStringParameter("currPage", currentPage+"");
		params.addQueryStringParameter("pageSize", pageSize+"");
		HttpHelper.send(HttpMethod.GET, Constant.GET_HOMEBOTTOM,params, new HttpCallBack() {
			
			public void onSuccess(String result) {
				//Logger.e("","请求得到的结果：--->"+result);
//				Type  type = new TypeToken<BaseResponseData<HotAppList>>(){}.getType();
//				
				@SuppressWarnings("unchecked")
				HotAppList data = GjsonUtil.json2Object(result, HotAppList.class);
				if(data!=null&&data.getApkList()!=null){
					currentPage++;
					isLastpage = (data.getCountPage() == data.getCurrPage());
					List<ApkInfo> list = data.getApkList();
					SpecialInfo seInfo = data.getSpecial();
					if(seInfo!=null){
						ApkInfo apkInfo = new ApkInfo();
						apkInfo.setContent(seInfo.getClassName());
						apkInfo.setDetailUrl(seInfo.getSpecUrl());
						apkInfo.setIcon(seInfo.getSpecImgUrl());
						list.add(apkInfo);
					}else if(seInfo==null&&!isLastpage){//如果专题为空，但为了能显示出应用列表，还是要创建一个对象，但要在列表getview中作空处理，把item隐藏掉
						ApkInfo apkInfo = new ApkInfo();
						list.add(apkInfo);
					}
					if(data.getCurrPage() == 1){
						iClassAppActivity.showHotApp(list);
					}else{
						iClassAppActivity.showMoreHotApp(data.getApkList());
					}
					
				}else{//出错的情况
					if(currentPage>1){
						iClassAppActivity.showHotArearError();
					}
				}
			}
			
			public void onFailure(HttpException e, String msg) {
				if(currentPage>1){
					iClassAppActivity.showHotArearError();
				}
			}
		});
	}
	
	/**
	 * 加载精品页除了热门应用的数据
	 */
	public void LoadClassicTopData(){
		if(!AppUtil.isOnline(BaseApplication.getInstance())){
			iClassAppActivity.showNetErrorView();
			return ;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("classCode", "Daren_tuijian");
		params.addQueryStringParameter("type", "0");
		params.addQueryStringParameter("currPage", currentPage+"");
		params.addQueryStringParameter("code", "Banner,JX_H5");
		HttpHelper.send(HttpMethod.GET,Constant.GET_HOMETOP,params,new HttpCallBack() {
			
			public void onSuccess(String result) {
				ClassicTopData data = GjsonUtil.json2Object(result, ClassicTopData.class);
				//Logger.e("", "解释结果-->"+GjsonUtil.Object2Json(data)+" ??"+(data!=null&&data.getStatus()==200&&data.getApkList()!=null
						//&&data.getBanAdList()!=null&&data.getJxAdList()!=null));
				if(data!=null&&data.getStatus()==200&&data.getApkList()!=null
						&&data.getBanAdList()!=null&&data.getJxAdList()!=null
						&&data.getApkList().size()>0&&data.getBanAdList().size()>0
						&&data.getJxAdList().size()>0){
					iClassAppActivity.showTopData(data);
				}else{//数据为空或者请求状态码不为200时
					iClassAppActivity.showEmptyView();
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				iClassAppActivity.showEmptyView();
				
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
