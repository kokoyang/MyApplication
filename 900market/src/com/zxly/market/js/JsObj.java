package com.zxly.market.js;

import java.io.File;
import java.net.URLDecoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.lidroid.xutils.exception.DbException;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.TopicDetailActivity;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.service.DownloadService;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.Logger;

/**
 * JsObj
 * 
 * @description:被JS调用的
 * @author:huangbqian
 * @date:2015-1-9 下午15:50:01
 */
public class JsObj {
	private DownloadManager downloadManager;
	private Context activity;
	public JsObj(Context activity){
		this.activity = activity;
		downloadManager = DownloadService.getDownloadManager(BaseApplication.getInstance());
	}
	
	/**
	 * 
	 * @param Packagename
	 * @return
	 */
	@JavascriptInterface
	public String getState(String Packagename,String version){
		DownLoadTaskInfo taskInfo = downloadManager.getTask(Packagename);
		if(taskInfo !=null){
            switch (taskInfo.getState()) {
                case WAITING:
                	return "waiting";//等待下载
                case STARTED:
                	 
                case LOADING:
                	return "downloading";//下载中
                case CANCELLED:
                	return "resume";//下载已取消,状态显示继续
                case SUCCESS:
                	if(AppUtil.isAppInstalled(activity, taskInfo.getPackageName())){//存在下载任务中并且已经安装
                		return "open";//打开
                	}
                	else{
	                	File file = new File(taskInfo.getFileSavePath());
	                	if(file.exists()){
	                		return "install";//安装
	                	}else{//下载的文件不存在，则重新下载
	                		try {
								downloadManager.removeDownload(taskInfo);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                		return "download";
	                	}
                	}
                 
                case FAILURE:
                	return "failue";       
            }
		}else{
			ApkInfo tempInfo = BaseApplication.getInstance().getInstalledApk(Packagename);//
    		if(tempInfo!=null){//表示已经安装
    			if(tempInfo.getVerName()!=null&&tempInfo.getVerName().compareTo(version)<0){//有更新的版本
    				return "upgrade";//升级
    			}else{
    				return "open";//打开
    			}
    		}else{//表示未安装也未在下载任务中
    			return "download";
    		}
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param downlaodUrl 下载地址
	 * @param fileName 应用名称
	 * @param packageName 包名
	 * @param iconUrl 图标址址
	 * @param versionName 版本号
	 */
	@JavascriptInterface
	public void toDownLoad(String downlaodUrl, String fileName,String packageName,String iconUrl,String versionName,String classCode,String source,int size){
		try {
			downloadManager.addNewDownload(downlaodUrl, fileName, packageName, iconUrl, versionName,classCode,source,size, null);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 暂停下载
	 * @param packageName 应用包名
	 */
	@JavascriptInterface
	public void toStop(String packageName){
		DownLoadTaskInfo taskInfo = downloadManager.getTask(packageName);
		try {
			downloadManager.stopDownload(taskInfo);
		} catch (DbException e) {
			Logger.e("", "包错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 恢复下载
	 * @param packageName 应用包名
	 */
	@JavascriptInterface
	public void toResume(String packageName){
		DownLoadTaskInfo taskInfo = downloadManager.getTask(packageName);
		try {
			downloadManager.resumeDownload(taskInfo, null);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 安装应用 
	 * @param packageName 应用包名
	 */
	@JavascriptInterface
	public void toInstall(String packageName){
		DownLoadTaskInfo taskInfo = downloadManager.getTask(packageName);
		AppUtil.installApk(BaseApplication.getInstance(),taskInfo);
	}
	/**
	 * 打开应用 
	 * @param Packagename 应用包名
	 * @param appName
	 * @param versionName
	 * @param classCode
	 * @param source
	 * @param size
	 */
	@JavascriptInterface
	public void toOpen(String Packagename,String appName,String versionName,String classCode,String source,int size){
		DownLoadTaskInfo info= new DownLoadTaskInfo();
		info.setPackageName(Packagename);
		info.setFileName(appName);
		info.setVersionName(versionName);
		info.setClassCode(classCode);
		info.setSource(source);
		info.setFileLength(size);
		AppUtil.startApk(info);
	}
	/**
	 * 根据地址跳转到webview
	 * @param url
	 */
	@JavascriptInterface
	public void buildNewWebPage(String url,String title){
		String tit = title;
		try {
			tit = URLDecoder.decode(title);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Intent intent = new Intent(activity,TopicDetailActivity.class);
		intent.putExtra(Constant.TOPIC_DETIAL_URL, url);
		intent.putExtra(Constant.TOPIC_TITLE, tit);
		activity.startActivity(intent);
	}
	
	
	/**
	 * 打开应用详情
	 * @param url 详情地址
	 */
	@JavascriptInterface
	public void buildDetailPage(String url){
		 Logger.e("", "url-->"+url);
		 Intent intent = new Intent(activity,AppDetailActivity.class);
		 intent.putExtra(Constant.APK_DETAIL,url);
		 activity.startActivity(intent);
	}
}