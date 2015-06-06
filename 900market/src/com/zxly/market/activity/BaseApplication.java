package com.zxly.market.activity;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zxly.market.R;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.model.AppManagerModel;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.IPhoneSubInfoUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.PrefsUtil;

public class BaseApplication extends Application {

	public static BaseApplication instance;
	public static LinkedList<ApkInfo> shakeData;
	public static float mHeightPixels = -1f;//屏幕的高，像素值
	public static float mWidthPixels = -1f;//屏幕的宽，像素值
	public static float mDensity = 1f;//屏幕密度
	public static String imei;// 手机imei号
	public static String imsi; // 手机imsi
	public static String coid; // 渠道ID
	public static int mVersionCode;
	public static String mVersionName;
	public  List<ApkInfo> mInstalledAppList;//应用全局保存已安装应用集合
	public  List<ApkInfo> needUpgradeList;//需要更新的应用集合,不包含有已经忽略的
	public boolean neadToFresh;//是否要刷新列表
	public boolean isNeadToFresh() {
		boolean temp = neadToFresh;
		neadToFresh = false;
		return temp;
	}

	public void setNeadToFresh(boolean neadToFresh) {
		this.neadToFresh = neadToFresh;
	}

	public List<ApkInfo> getmInstalledAppList() {
		if(mInstalledAppList == null){
			AppManagerModel am = new AppManagerModel();
			mInstalledAppList = am.saveApkInfoToDB(instance);
		}
		return mInstalledAppList;
	}

	public void setmInstalledAppList(List<ApkInfo> mInstalledAppList) {
		this.mInstalledAppList = mInstalledAppList;
	}

	public static BaseApplication getInstance(){
		return instance;
	}

	/**
	 * 应用初始工作
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		readParams();
		initPrefs();
		initImageLoader();
		getPkgVersion();
	}

	/** 得到版本号、版本名 */
	private void getPkgVersion() {

		PackageInfo pkgInfo = null;
		try {
			pkgInfo = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			mVersionCode = pkgInfo.versionCode;
			mVersionName = pkgInfo.versionName;
			Logger.d("","version code is " + mVersionCode
					+ ", version name is " + mVersionName);
		} catch (PackageManager.NameNotFoundException e) {
			Logger.e("",e.toString());
		}
	}
	
	/**
	 * 获取屏幕参数
	 */
	protected void readParams() {
		Logger.d("dis","new sha1");
		shakeData = new LinkedList<ApkInfo>();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		mWidthPixels = displayMetrics.widthPixels;
		mHeightPixels = displayMetrics.heightPixels;
		mDensity = displayMetrics.density;
		// Logger.e("BaseApplication",
		// "屏幕参数-->"+GjsonUtil.Object2Json(displayMetrics));
		imei = IPhoneSubInfoUtil.getSmallestImei(instance);
		Map<String, String> imeiMap = IPhoneSubInfoUtil.getImeiMap(instance);
		imsi = imeiMap.remove(imei);
		coid = getString(R.string.coid);
	}

	/**
	 * 图片缓存初始化,图片缓存大小 目录等设置
	 */
	protected void initImageLoader() {
		//File cacheDir = StorageUtils.getCacheDirectory(instance);// 缓存目录放置在应用包下
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(instance)  
			    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
			    .threadPoolSize(3)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
			    .memoryCacheSize(2 * 1024 * 1024)    
			    .tasksProcessingOrder(QueueProcessingType.LIFO) 
			    .imageDownloader(new BaseImageDownloader(instance, 5*1000, 30*1000))// connectTimeout (5 s), readTimeout (30 s)超时时间  
			    .writeDebugLogs() // Remove for release app  
			    .build();//开始构建  
		ImageLoader.getInstance().init(config);// 全局初始化此配置

	}
	/**
	 * 初始化SharedPreference
	 */
	@SuppressLint("InlinedApi")
	protected void initPrefs() {
		PrefsUtil.init(getInstance(), getPackageName() + "_preference",
				Context.MODE_MULTI_PROCESS);
	}
	/**
	 * 本地已安装应用信息在应用其他页面常用到，直接放置内存中
	 */
	protected void getAllApps(){
		new Thread(){
			public void run() {
				AppManagerModel am = new AppManagerModel();
				mInstalledAppList = am.saveApkInfoToDB(instance);
			};
		}.start();
	}
	/**
	 * 根据包名取出已经安装的应用信息
	 * @param packageName
	 * @return 
	 */
	public ApkInfo getInstalledApk(String packageName){
		ApkInfo info;
		for (int i = 0,length = getmInstalledAppList().size(); i < length; i++) {
			info = getmInstalledAppList().get(i);
			if(info.getPackName().equals(packageName)){
				return info;
			}
		}
		return null;
	}
	
	/**
	 * 获取需要升级的应用列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ApkInfo> getNeedUpgradeAppList(){
		if(needUpgradeList==null){
			String json = PrefsUtil.getInstance().getString(Constant.UPGRADE_LIST);
			if(json!=null){
				Type  type = new TypeToken<List<ApkInfo>>(){}.getType();
				needUpgradeList = (List<ApkInfo>) GjsonUtil.json2Object(json, type);
			}
		}
		return needUpgradeList;
	}
	
	 /**
     * 检查网络是否已连接
     * @author com.tiantian
     * @param context
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) instance
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前是否是wifi状态
     * @param context
     * @return
     */
    public  boolean isWifiConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
        return false ;
    }
    
   
}
