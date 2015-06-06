package com.zxly.market.model;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkListData;
import com.zxly.market.entity.HotKeyDatas;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;

/**
 * 搜索关键字逻辑处理
 * @author fengruyi
 *
 */
public class HotKeyControler extends BaseControler{
	private String TAG = "HotKeyControler";
	IHotKeyView iHotKeyView;
	private int key_currentpage = 1;
	private int key_pageSize = 9;
	private int result_currentPage = 1;
	private int result_pagesize = 10;
	private boolean isKeyLastPage = false;
	private boolean isResultLastPage = false;
	
	public HotKeyControler(IHotKeyView iHotKeyView){
		this.iHotKeyView = iHotKeyView;
	}
	
	public void loadHotKeyData(){
		if(!BaseApplication.getInstance().isOnline()){
			iHotKeyView.showNoNetwork();
			return;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currPage", key_currentpage+"");
		params.addQueryStringParameter("pageSize", key_pageSize+"");
		HttpHelper.send(HttpMethod.GET, Constant.GET_HOTKEYS, params, new HttpCallBack() {
			
			public void onSuccess(String result) {
				if (isFinish())return;
				HotKeyDatas data = GjsonUtil.json2Object(result, HotKeyDatas.class);
				if(data!=null&&data.getStatus() == 200&&data.getApkList()!=null){
					key_currentpage++;
					isKeyLastPage = (data.getCountPage() == data.getCurrPage());
					if(data.getCurrPage() == 1){
						iHotKeyView.showHotKeysData(data.getApkList());
					}else{
						iHotKeyView.showMoreHotKeysData(data.getApkList());
					}
				}else{
					iHotKeyView.showRequestErro();
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				if (isFinish())return;
				iHotKeyView.showRequestErro();
			}
		});
	}
	
	public void loadAppsByKeys(String key,boolean isLoadMore){
		if(!isLoadMore){
			result_currentPage = 1;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currPage", result_currentPage+"");
		params.addQueryStringParameter("pageSize", result_pagesize+"");
		params.addQueryStringParameter("keyword", key);
		HttpHelper.send(HttpMethod.GET, Constant.GET_APPS_BY_KEY, params, new HttpCallBack() {
			
			public void onSuccess(String result) {
				if (isFinish())return;
				ApkListData data = GjsonUtil.json2Object(result, ApkListData.class);
				if(data!=null&&data.getStatus() == 200&&data.getApkList()!=null&&data.getApkList().size()>0){
					result_currentPage++;
					isResultLastPage = (data.getCountPage() == data.getCurrPage());
					if(data.getCurrPage() == 1){
						iHotKeyView.showSearchResult(data.getApkList());
					}else{
						iHotKeyView.showMoreResult(data.getApkList());
					}
				}else{
					if(result_currentPage==1){
						iHotKeyView.showEmptyView();
					}else{
						iHotKeyView.loadMoreFail();
					}
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				if (isFinish())return;
				if(result_currentPage==1){
					iHotKeyView.showEmptyView();
				}else{
					iHotKeyView.loadMoreFail();
				}
			}
		});
	}
	
	
	public boolean isKeyLastPage() {
		return isKeyLastPage;
	}


	public boolean isResultLastPage() {
		return isResultLastPage;
	}

}
