package com.zxly.market.model;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.ApkListData;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.PrefsUtil;

public class ApkUpgradeControler extends BaseControler{
	private final String TAG = "ApkUpgradeControler";
	private IUpgradeView iUpgradeView;
	
	public ApkUpgradeControler(){};
	public ApkUpgradeControler (IUpgradeView iUpgradeView){
		this.iUpgradeView = iUpgradeView;

	}
	
	/**
	 * 请求可更新应用数据
	 */
	public void loadUpgradeData(){
		if(!BaseApplication.getInstance().isOnline()){
			if(iUpgradeView!=null){
				iUpgradeView.showNoNetwork();
			}
			return;
		}
		String json = PrefsUtil.getInstance().getString(Constant.UPGRADE＿LISTJSON);
		RequestParams params = new RequestParams();
		//params.addQueryStringParameter("req",json);
		//Logger.e(TAG, "更新参数0--->"+json);
		params.addBodyParameter("req", json);
		HttpHelper.send(HttpMethod.POST, Constant.GET_UPGRADEAPP, params, new HttpCallBack() {		
			public void onSuccess(String result) {
				//Logger.e(TAG, "取得更新列表--->"+result);
				if(isFinish()) return;
				ApkListData data = GjsonUtil.json2Object(result, ApkListData.class);
				if(data!=null&&data.getApkList()!=null){
					List<ApkInfo> upgradeList = data.getApkList();
					if(upgradeList.size()==0){
						if(iUpgradeView!=null){
							iUpgradeView.showEmptyView();
						}
						BaseApplication.getInstance().needUpgradeList = upgradeList;
						PrefsUtil.getInstance().putString(Constant.UPGRADE_LIST,GjsonUtil.Object2Json(upgradeList)).commit();
					}else{
						List<ApkInfo> ignoreList;
						try {
							ignoreList = DbUtils.create(BaseApplication.getInstance()).findAll(Selector.from(ApkInfo.class));
							if(ignoreList!=null&&ignoreList.size()>0){//过虑已经忽略升级的列表
								for(int i = 0,length = upgradeList.size();i<length;i++){
									if(ignoreList.contains(upgradeList.get(i))){
										upgradeList.remove(i);
										length = length - 1;
										i = i -1;
									}
								}
							}
						} catch (DbException e) {
						  
						}
						BaseApplication.getInstance().needUpgradeList = upgradeList;
						PrefsUtil.getInstance().putString(Constant.UPGRADE_LIST,GjsonUtil.Object2Json(upgradeList)).commit();
						if(iUpgradeView!=null){
							iUpgradeView.showUpgradeList(upgradeList);
						}
					}
				;
				}else{
					if(iUpgradeView!=null){
						iUpgradeView.showRequestErro();
					}
				}
			}
			public void onFailure(HttpException e, String msg) {
				 Logger.e(TAG, "onFailure--->"+msg);
				if(isFinish()) return;
				if(iUpgradeView!=null){
					iUpgradeView.showRequestErro();
				}
			}
		});
	}
	
	/**
	 * 本地安装的应用拼接成json用于请求参数
	 * @return
	 */
	public String getRequestJson(){
		ArrayList<UpgradeInfo> list = new ArrayList<UpgradeInfo>();
		UpgradeInfo upgradeInfo ;
		ApkInfo apkInfo;
		for(int i = 0,length = BaseApplication.getInstance().mInstalledAppList.size();i<length;i++){
			apkInfo = BaseApplication.getInstance().mInstalledAppList.get(i);
			upgradeInfo = new UpgradeInfo();
			upgradeInfo.setPackName(apkInfo.getPackName());
			upgradeInfo.setVer(apkInfo.getVerCode());
			list.add(upgradeInfo);
		}
		String json = GjsonUtil.Object2Json(list);
		Logger.e(TAG, "json--->"+json);
		return GjsonUtil.Object2Json(list);
	}
	
	
	public class UpgradeInfo{
		
		String ver;
		String packName;
		String source = "local";
		
		public String getVer() {
			return ver;
		}
		public void setVer(String ver) {
			this.ver = ver;
		}
		public String getPackName() {
			return packName;
		}
		public void setPackName(String packName) {
			this.packName = packName;
		}
		public String getClassCode() {
			return source;
		}
		public void setClassCode(String source) {
			this.source = source;
		}
	}
	
}
