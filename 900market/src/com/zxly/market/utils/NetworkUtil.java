/* *****************************************
 *      上海市移卓网络科技有限公司
 *      网址：http://www.30.net/
 *      Copyright(C)2012-2013 
 ****************************************** */
package com.zxly.market.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.zxly.market.activity.BaseApplication;

import java.lang.reflect.Method;

/**
 * @class:NetworkUtil
 * @description:网络状态工具类
 * @author:yangwencai
 * @version:v1.0
 * @date:2014-9-15 下午4:03:34
 */
public class NetworkUtil {
	/** 获取网络状态
	 * 
	 * @return 1为wifi连接，2为2g网络，3为3g网络，-1为无网络连接 */
	public static int getNetworkerStatus() {
		ConnectivityManager conMan = (ConnectivityManager) BaseApplication.getInstance().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conMan.getActiveNetworkInfo();
		if (null != info && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (info.getSubtype()) {
				case 1:
				case 2:
				case 4:
					// 2G网络
					return 2;
				default:
					// 3G及其以上网络
					return 3;
				}
			} else if(info.getType() == ConnectivityManager.TYPE_WIFI){
				// wifi网络
				return 1;
			}
		} 
		// 无网络
		return -1;
		
	}

	/** 判断当前网络是否是wifi网络 */
	public static boolean isWifi() {
		return getNetworkerStatus()==1;
	}
	
	/** 判断当前网络是否是2G网络 */
	public static boolean is2G() {
		return getNetworkerStatus() == 2;
	}

	/** 判断当前网络是否是3G网络 */
	public static boolean is3G() {
		return getNetworkerStatus() == 3;
	}

	/** 判断网络连接状态，返回true为连接 */
	public static boolean hasNetwork() {
		return getNetworkerStatus() != -1;
	}	

	/** 进入网络设置 */
	@SuppressWarnings("deprecation")
	public static void enterNetWorkSetting(Context context) {
		String sdkVersion = android.os.Build.VERSION.SDK;
		Intent intent = null;
		if (Integer.valueOf(sdkVersion) > 10) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
			intent.setComponent(comp);
			intent.setAction("android.intent.action.VIEW");
			
		}
		context.startActivity(intent);
	}

	/**
	 * @description:判断是否有SIM卡
	 * @author:XiongWei
	 * @return	true 有卡  false 无卡
	 */
	public static boolean isSimExist() {
		TelephonyManager mTelephonyManager = (TelephonyManager) BaseApplication.getInstance().getSystemService(
				Context.TELEPHONY_SERVICE);
		int simState = mTelephonyManager.getSimState();
		boolean exist = false;
		switch (simState) {
			case TelephonyManager.SIM_STATE_ABSENT:
			case TelephonyManager.SIM_STATE_UNKNOWN:
				exist = false;
				break;
	
			case TelephonyManager.SIM_STATE_READY:
				exist = true;
				break;
		}
		return exist;
	}
	
	/**
	 * @description:WiFi状态
	 * @author:XiongWei
	 * @return
	 */
	public static boolean isWiFiEnabled() {
		WifiManager wifiManager = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}
	
	/**
	 * @description:移动网络是否可用
	 * @author:XiongWei
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isMobileDataEnabled() {
		if (!isSimExist())
			return false;

		ConnectivityManager cm = (ConnectivityManager) BaseApplication.getInstance().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		boolean isEnabled = false;
		try {
//			Class clazz = cm.getClass();
//			Method getMethod = clazz.getMethod("getMobileDataEnabled", null);
//			getMethod.setAccessible(true);
//			isEnabled = (Boolean) getMethod.invoke(cm, null);
		} catch (Exception e) {

			//	反射方式不能正常使用时
			NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			/* reason 可能会存储移动数据状态 */
			String reason = mobile.getReason();
			if (TextUtils.isEmpty(reason)) {
				int networkerStatus = getNetworkerStatus();
				return networkerStatus != -1 && networkerStatus != 1;
			}
			return reason.equals(MOBILE_DATA_ENABLED_STATUS)
					|| reason.equals(MOBILE_DATA_CONNECTED_STATUS);
		}

		return isEnabled;
	}
	/** 移动网络可用标识 */
	public static final String MOBILE_DATA_ENABLED_STATUS = "dataEnabled";
	public static final String MOBILE_DATA_CONNECTED_STATUS = "connected";
	/**
	 * @description:开启关闭WiFi
	 * @author:XiongWei
	 */
	public static void handleOnOffWifi() {
		WifiManager wifiManager = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		} else {
			wifiManager.setWifiEnabled(true);
		}
	}
	
	/**
	 * @description:开启关闭移动网络
	 * @author:XiongWei
	 */
	public static boolean handleOnOffMobileData() {
		ConnectivityManager connManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		Class<?> cmClass = connManager.getClass();
		Class<?>[] argClasses = new Class[1];
		argClasses[0] = boolean.class;
		// 反射ConnectivityManager中hide的方法setMobileDataEnabled，可以开启和关闭GPRS网络
		Method method = null;
		try {
			if (!isSimExist()) {
				return false;
			}
			method = cmClass.getMethod("setMobileDataEnabled", argClasses);
			method.invoke(connManager, isMobileDataEnabled() ? false : true);
			return true;
		} catch (Exception e) {
			Logger.e("NetworkUtil", e.getMessage(), e);
		}
		
		return false;
	}
}
