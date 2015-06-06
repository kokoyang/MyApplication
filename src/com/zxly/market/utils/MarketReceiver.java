/* *****************************************
 *      上海市移卓网络科技有限公司
 *      网址：http://www.30.net/
 *      Copyright(C)2012-2013 
 ****************************************** */
package com.zxly.market.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * @class:MarketReceiver
 * @description:广播处理
 * @author:yangwencai
 * @version:v2.0
 * @date:2015-4-15 下午3:31:57
 */
public class MarketReceiver extends BroadcastReceiver {

	public static final String TAG = "MarketReceiver";
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private static final String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
	public static final String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";

	private static long time = 0; 

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		// 网络状态改变
		boolean istime = false;
		if (CONNECTIVITY_CHANGE_ACTION.equals(action) 
				&& (istime = ((System.currentTimeMillis()-time)>5000))) {
			time = System.currentTimeMillis();
			int status = NetworkUtil.getNetworkerStatus();
			switch (status) {
			case 1:
				Logger.d(TAG, "连上wifi");
				break;
			case 2:
				Logger.d(TAG, "连上2G网络");
				break;
			case 3:
				Logger.d(TAG, "连上3G及其以上网络");
				break;
			case -1:
				Logger.d(TAG, "断开网络");
				break;
			default:
				break;
			}
			// 连上网络
			if (status != -1) {
				if(observer!=null){
					for(int i=0;i<observer.size();i++){
						if(observer.get(i)!=null){
							observer.get(i).netWorkConnect();
						}
					}
				}
			}

		}
		// 开机
		else if (BOOT_COMPLETED_ACTION.equals(action)) {

		}
		// 安装、卸载
		else if (Intent.ACTION_PACKAGE_ADDED.equals(action) || Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
			String packageName = intent.getDataString().substring(8);// intent.getData().getSchemeSpecificPart()
			if (Intent.ACTION_PACKAGE_ADDED.equals(action) && !packageName.equals(context.getPackageName())) {


			} else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {

			}

		} else if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {


		}
		// 语言切换
		else if (ACTION_LOCALE_CHANGED.equals(action)) {
		}

	}

	private static List<NetChangeObserver> observer;
	public interface  NetChangeObserver{
		void netWorkConnect();
	}
	/**
	 * @description:注册接收监听
	 * @author:yangwencai
	 * @param listener
	 */
	public static void registerObserver(NetChangeObserver listener) {
		if(observer==null){
			observer = new ArrayList<NetChangeObserver>();
		}
		if(!observer.contains(listener)){
			observer.add(listener);
		}
	}

	/***
	 * @description:取消接收监听
	 * @author:yangwencai
	 */
	public static void removeRegisterObserver() {
		if(observer!=null){
			for(int i=0;i<observer.size();i++){
				Object o= observer.get(i);
				o = null;
			}
			observer = null;
		}
	}

}