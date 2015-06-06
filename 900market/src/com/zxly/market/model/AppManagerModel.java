/**    
 * @FileName: AppManagerModel.java  
 * @Package:com.zxly.market.model  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-9 下午2:18:36  
 * @version V1.0    
 */
package com.zxly.market.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;

import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.PrefsUtil;

/** 
 * @ClassName: AppManagerModel  
 * @Description: apk管理 
 * @author: fengruyi 
 * @date:2015-4-9 下午2:18:36   
 */
public class AppManagerModel {	  
		
		/**
		 * PackageInfo 提取应用
		 * @param context
		 * @return
		 */
	   public List<ApkInfo> saveApkInfoToDB(Context context){
		   ArrayList<ApkInfo> appList = new ArrayList<ApkInfo>(); //用来存储获取的应用信息数据
		   List<ResolveInfo> apps = null;
		   Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		   mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		   PackageManager pm = context.getPackageManager();
		   apps = pm.queryIntentActivities(mainIntent, 0);
			ArrayList<UpgradeInfo> upGradelist = new ArrayList<UpgradeInfo>();
			UpgradeInfo upgradeInfo ;
			ResolveInfo resolveInfo;
			ApkInfo apkInfo;
			PackageInfo packageInfo;
			for(int i=0;i<apps.size();i++) { 
				resolveInfo = apps.get(i);
				if(!resolveInfo.activityInfo.applicationInfo.packageName.equals(context.getPackageName())){//本应用 不列入内
					upgradeInfo = new UpgradeInfo();
					apkInfo=new ApkInfo(); 
					try {
						packageInfo = pm.getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName, 0);
						apkInfo.setAppName(resolveInfo.loadLabel(pm).toString());
						apkInfo.setPackName(packageInfo.packageName); 
						apkInfo.setVerName(packageInfo.versionName);
						apkInfo.setVerCode(packageInfo.versionCode+""); 
						apkInfo.setSystemApp((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0);
						if(!appList.contains(apkInfo)){
							upgradeInfo.setPackName(apkInfo.getPackName());
							upgradeInfo.setVer(apkInfo.getVerCode());
							upGradelist.add(upgradeInfo);
							queryPacakgeSize(apkInfo);
							appList.add(apkInfo);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			PrefsUtil.getInstance().putString(Constant.UPGRADE＿LISTJSON, GjsonUtil.Object2Json(upGradelist)).commit();
			return appList;
	   }
		/**
		 * ResolveInfo 提取应用
		 * @param context
		 * @return
		 */
	   public List<ApkInfo> getAllapp(Context context){
		   ArrayList<ApkInfo> appList = new ArrayList<ApkInfo>(); //用来存储获取的应用信息数据
		   List<ResolveInfo> apps = null;
		   Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		   mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		   PackageManager pm = context.getPackageManager();
		   apps = pm.queryIntentActivities(mainIntent, 0);
			ArrayList<UpgradeInfo> upGradelist = new ArrayList<UpgradeInfo>();
			UpgradeInfo upgradeInfo ;
			ResolveInfo resolveInfo;
			ApkInfo apkInfo;
			PackageInfo packageInfo;
			for(int i=0;i<apps.size();i++) { 
				resolveInfo = apps.get(i);
				if(!resolveInfo.activityInfo.applicationInfo.packageName.equals(context.getPackageName())){//本应用 不列入内
					upgradeInfo = new UpgradeInfo();
					apkInfo=new ApkInfo(); 
					try {
						packageInfo = pm.getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName, 0);
						apkInfo.setAppName(resolveInfo.loadLabel(pm).toString());
						apkInfo.setPackName(packageInfo.packageName); 
						apkInfo.setVerName(packageInfo.versionName);
						apkInfo.setVerCode(packageInfo.versionCode+""); 
						apkInfo.setSystemApp((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)!=0);
						if(!appList.contains(apkInfo)){
							upgradeInfo.setPackName(apkInfo.getPackName());
							upgradeInfo.setVer(apkInfo.getVerCode());
							upGradelist.add(upgradeInfo);
							queryPacakgeSize(apkInfo);
							appList.add(apkInfo);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			PrefsUtil.getInstance().putString(Constant.UPGRADE＿LISTJSON, GjsonUtil.Object2Json(upGradelist)).commit();
			return appList;
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
	   
	    /** 查询应用占用空间*/
	    public void queryPacakgeSize(ApkInfo info) throws Exception {
	        if (info != null && info.getPackName() != null) {
	            // 使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
	            try {
	            	PackageManager pm =BaseApplication.instance.getPackageManager(); 
	                Method getPackageSizeInfo = pm.getClass().getMethod(
	                        "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
	                // 调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
	                getPackageSizeInfo.invoke(pm, info.getPackName(), new PkgSizeObserver(info));
	            } catch (Exception ex) {
	                throw ex;
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
	   /**
	    * 
	    * @Title: getUserApps  
	    * @Description: 从数据库中取出用户安装的apk信息
	    * @param @param context
	    * @param @return 
	    * @return List<ApkInfo> 
	    * @throws
	    */
	   public List<ApkInfo> getUserApps(Context context){
		    List<ApkInfo> list = new ArrayList<ApkInfo>();
		    ApkInfo info;
		    for(int i = 0,length = BaseApplication.getInstance().mInstalledAppList.size();i<length;i++){
		    	info =  BaseApplication.getInstance().mInstalledAppList.get(i);
		    	if(!info.isSystemApp()){
		    		list.add(info);
		    	}
		    }
		    return list;
	   }
}
