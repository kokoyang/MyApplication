package com.zxly.market.service;

import java.lang.reflect.Method;

import com.lidroid.xutils.exception.DbException;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.model.AppManagerModel.PkgSizeObserver;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.StatisticsManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;

/**
 * 静态注册安装卸载广播
 * @author fengruyi
 *
 */
public class PackageChangeReceiver extends BroadcastReceiver{
	private final String TAG = "PackageChangeReceiver";
	public static final int PACKAGE_ADD = 0x1;
	public static final int PACKAGE_REMOVE = 0x2;
	public static  Handler mHandler;
	
	/**
	 * 界面需要监听应用安装和卸载信息时要绑定一个handler
	 * @param handler
	 */
	public static void bindHandler(Handler handler){
		mHandler = handler;
	}
	
	public void onReceive(Context context, Intent intent) {
		//接收卸载广播  
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
        	 String packageName = intent.getDataString();  
             Logger.e(TAG, "监听到卸载广播-->"+packageName);
        	 if(!TextUtils.isEmpty(packageName)){
	             ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             BaseApplication.getInstance().getmInstalledAppList().remove(info);
	             if(mHandler!=null){
		             Message msg = mHandler.obtainMessage();
		             msg.what = PACKAGE_REMOVE;
		             msg.obj = info;
		             mHandler.sendMessage(msg);
	             }
//	             try {
//						DownloadService.getDownloadManager(BaseApplication.getInstance()).setTaskUnInstall(info.getPackName());
//					} catch (DbException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
        	 }
        }
        //接收安装广播
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
        	 String packageName = intent.getDataString();
			DownloadManager downloadmanager = DownloadService.getDownloadManager(BaseApplication.getInstance());
			Logger.e(TAG, "监听到安装广播-->" + packageName );

			if(!TextUtils.isEmpty(packageName)){
				ApkInfo info = new ApkInfo();
				info.setPackName(packageName.replace("package:", ""));
				DownLoadTaskInfo taskInfo = downloadmanager.getTask(info.getPackName());
				Logger.e(TAG, "监听到安装广播-->" + (taskInfo==null) );
				if(taskInfo!=null){
					taskInfo.setType(4);
					StatisticsManager.getInstance().statistics(taskInfo);
				}
	             try {
					queryPacakgeSize(info);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	             BaseApplication.getInstance().getmInstalledAppList().add(info);
	             if(mHandler!=null){
		             Message msg = mHandler.obtainMessage();
		             msg.what = PACKAGE_ADD;
		             msg.obj = info;
		             mHandler.sendMessage(msg);
	             }
//	             try {
//					 downloadmanager.setTaskInstall(info.getPackName());
//				} catch (DbException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
        	 }
        }
        
	}
	/**
	 * 退出界面时要解除绑定
	 */
	public static void unBindHandler(){
		mHandler = null;
	}
	
	  /** 查询应用占用空间*/
    public void queryPacakgeSize(ApkInfo info) {
        if (info != null && info.getPackName() != null) {
            // 使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
            try {
            	PackageManager pm =BaseApplication.instance.getPackageManager(); 
            	PackageInfo packageInfo  =  pm.getPackageInfo(info.getPackName(), 1);
            	info.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
            	info.setVerName(packageInfo.versionName);
                Method getPackageSizeInfo = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                // 调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
                getPackageSizeInfo.invoke(pm, info.getPackName(), new PkgSizeObserver(info));
            } catch (Exception ex) {
               
            }
        }
    }

    /** aidl文件形成的Bindler机制服务类*/
    public class PkgSizeObserver extends IPackageStatsObserver.Stub {
        private ApkInfo info;

        public PkgSizeObserver(ApkInfo info) {
            this.info = info;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cachesize = pStats.cacheSize;  // 缓存大小
            long datasize = pStats.dataSize;    // 数据大小
            long codesize = pStats.codeSize;    // 应用程序大小
            long totalsize = cachesize + datasize + codesize;
            info.setSize(totalsize);

        }
    }
}
