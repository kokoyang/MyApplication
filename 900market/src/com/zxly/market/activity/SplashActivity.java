package com.zxly.market.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.KeyEvent;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.R;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.HotKeyDatas;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.model.ApkUpgradeControler;
import com.zxly.market.model.AppManagerModel;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.PrefsUtil;

/**
 * 启动应用时的闪页
 * @author Administrator
 *
 */
public class SplashActivity extends BaseActivity{
	private boolean getInstalledAppComplete;//判断取得本地所有安装app信息是否已经完成
	private boolean isFinish;
	private long mExitTime = 1000;
	
	@Override
	public int getContentViewId() {
		
		return R.layout.activity_splash;
	}
	
	Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				Logger.e("", "我执行完成");
				ApkUpgradeControler controler = new ApkUpgradeControler();
				controler.loadUpgradeData();
				if(!isFinish){
					Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
					startActivity(mainIntent);
					finish();
				}
			}
		};
	};
	
	
	@Override
	public void initViewAndData() {
		getHotKey();
		getAllApps();
		toActivate();
		myHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(getInstalledAppComplete){
					Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
					startActivity(mainIntent);
					finish();
					isFinish = true;
				}else{
					isFinish = false;
				}
			}
		}, mExitTime);//2秒后跳转		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				return true;
			}
		}
		return false;
	}

	//	/**
//	 * 初始化默认应用 设置 
//	 */
//	private void initAppSetting(){
//		AppSettingInfo settingInfo = PrefsUtil.getInstance().getObject(Constant.SETTING_CONFIG, AppSettingInfo.class);
//		if(settingInfo ==null){
//			settingInfo = new AppSettingInfo();
//			PrefsUtil.getInstance().putObject(Constant.SETTING_CONFIG, settingInfo).commit();
//		}
//	}
	protected void getAllApps(){
		new Thread(){
			public void run() {
				AppManagerModel am = new AppManagerModel();
				BaseApplication.getInstance().mInstalledAppList = am.saveApkInfoToDB(SplashActivity.this);
				getInstalledAppComplete = true;
				myHandler.sendEmptyMessage(1);
				
			};
		}.start();
	}
	private void getHotKey(){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("currPage","1");
		params.addQueryStringParameter("pageSize", "1");
		HttpHelper.send(HttpMethod.GET, Constant.GET_HOTKEYS, params, new HttpCallBack() {
			public void onSuccess(String result) {
				HotKeyDatas data = GjsonUtil.json2Object(result, HotKeyDatas.class);
				if (data != null && data.getStatus() == 200 && data.getApkList() != null) {
					PrefsUtil.getInstance().putString(Constant.HOT_KEY, data.getApkList().get(0).getKw()).commit();
				}
			}

			public void onFailure(HttpException e, String msg) {


			}
		});
	}


	private void toActivate() {
		// http://api.18guanjia.com/Stat/ActiveStat?packName=111&classCode=search&Coid=3&type=0&versoin=1.0&nCoid=&Imei=1111&channel=2&sdk=5.0&apkName=111&apkSize=111&mode=sx&token=y8t0a9ru6z76w4m8v5dzz2
		RequestParams params = new RequestParams();
		params.addBodyParameter("packName", getPackageName());
		params.addBodyParameter("classCode", "");
		params.addBodyParameter("Coid", "3");
		params.addBodyParameter("type", "0");
		params.addBodyParameter("versoin", BaseApplication.mVersionName);
		params.addBodyParameter("nCoid", "");
		params.addBodyParameter("Imei", BaseApplication.imei);
		params.addBodyParameter("channel", BaseApplication.coid);
		params.addBodyParameter("sdk", Build.VERSION.RELEASE);
		params.addBodyParameter("apkName", getString(R.string.app_name));
		params.addBodyParameter("apkSize", "");
		params.addBodyParameter("mode",Build.MODEL);
		params.addBodyParameter("token", Constant.APP_TOKEN);

		HttpHelper.statSend(HttpMethod.POST, Constant.ACTIVATE_URL, params, new HttpCallBack() {
			public void onSuccess(String result) {
				Logger.d("activate", "activate = " + result);
			}

			public void onFailure(HttpException e, String msg) {
			}
		});

	}
	
}
