package com.zxly.market.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.entity.UpdateInfo;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.view.PromptDialog;


public class DownloadManager {
	private  static final String TAG = "DownloadManager";
	private String filePath;//文件保存路径
    private List<DownLoadTaskInfo> DownLoadTaskInfoList;
    private int maxDownloadThread = 3;
    private Context mContext;
    private DbUtils db;
    public static final int LOADING = 11;
    public static final int CANCLED = 12;
    public static final int SUCCESS = 13;
    public static final int FAILUE = 14;
    
    private static final long MIN_STORAGE = 10;//10M
    private static final int dbVersion = 10;
    private static final String dbName = "zxly_market_db";
//    private DbUpgradeListener dbUpgradeListener = null;
    private Handler handler;
    PromptDialog dialog;
    public DownloadManager(Context appContext) {
    	if(hasSdcard()){
    		filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/900market/";
    	}else{
    		filePath = appContext.getFilesDir().getAbsolutePath()+"/900market/";
    	}
        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class, new HttpHandlerStateConverter());
        mContext = appContext;
        db = DbUtils.create(mContext);
//		db = DbUtils.create(mContext, dbName, dbVersion, new DbUpgradeListener() {
//			
//			@Override
//			public void onUpgrade(DbUtils dbUtils, int arg1, int arg2) {
//				// TODO Auto-generated method stub
//				try {
//					dbUtils.dropDb();
//				} catch (DbException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
        try {
            DownLoadTaskInfoList = db.findAll(Selector.from(DownLoadTaskInfo.class));
           
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
        if (DownLoadTaskInfoList == null) {
            DownLoadTaskInfoList = new ArrayList<DownLoadTaskInfo>();
        }else{
        	 for(int i = 0,length=DownLoadTaskInfoList.size();i<length;i++){
             	if(DownLoadTaskInfoList.get(i).getState() == State.LOADING){
             		DownLoadTaskInfoList.get(i).setState(State.CANCELLED);
             	}
             }
        }
    }


    public int getDownLoadTaskInfoListCount() {
        return DownLoadTaskInfoList.size();
    }
 
    /**
     * 从下载列表中获取未下载完成的任务
     * @return
     */
    public List<DownLoadTaskInfo> getDoingTask(){
    	List<DownLoadTaskInfo> list = new ArrayList<DownLoadTaskInfo>();
    	for(DownLoadTaskInfo task : DownLoadTaskInfoList){
    		if(task.getState()!=State.SUCCESS){
    			list.add(task);
    		}
    	}
    	Logger.e(TAG, "未下载完成的任务数 -->"+list.size());
    	return list;
    }
    /**
     * 获取未下载完成任务数
     * @return
     */
    public int getDoingTaskCount(){
    	int count = 0;
    	for(DownLoadTaskInfo task : DownLoadTaskInfoList){
    		if(task.getState()!=State.SUCCESS){
    			count++;
    		}
    	}
    	return count;
    }
    /**
     * 从下载列表中获取已经下载完成的任务
     * @return
     */
    public List<DownLoadTaskInfo> getDoneTask(){
    	List<DownLoadTaskInfo> list = new ArrayList<DownLoadTaskInfo>();
    	for(DownLoadTaskInfo task : DownLoadTaskInfoList){
    		if(task.getState()==State.SUCCESS){
    			list.add(task);
    		}
    	}
    	return list;
    }
    /**
     * 根据包名取得下载任务里的对象，没有返回null
     * 
     * @param PackageName
     * @return
     */
    public DownLoadTaskInfo getTask(String PackageName){
    	DownLoadTaskInfo info;
    	for (int i = 0,length = DownLoadTaskInfoList.size(); i < length; i++) {
    		info = DownLoadTaskInfoList.get(i);
			if(info.getPackageName().equals(PackageName)){
				return info;
			}
		}
    	return null;
    }
    
    
    
    /***
     * 添加下载任务
     * @param downlaodUrl 下载地址
     * @param fileName 应用名
     * @param packageName 应用包名
     * @param iconUrl 图标地址
     * @param versionName 版本号
     * @param callback 下载进度回调
     * @throws DbException
     */
    public void addNewDownload(String downlaodUrl, String fileName,String packageName,String iconUrl,String versionName,String classCode,String source,long byteSize,
                               final RequestCallBack<File> callback) throws DbException {
    	long size = byteSize/1024/1024;
    	if (!readSDCard(size))return;		
    	if(getTask(packageName)!=null)return;
    	String file_save_path = filePath+System.currentTimeMillis()+"_zxly.apk";
        final DownLoadTaskInfo downLoadTaskInfo = new DownLoadTaskInfo();
        downLoadTaskInfo.setDownloadUrl(downlaodUrl);
        downLoadTaskInfo.setAutoRename(false);
        downLoadTaskInfo.setAutoResume(true);
        downLoadTaskInfo.setPackageName(packageName);
        downLoadTaskInfo.setFileName(fileName);
        downLoadTaskInfo.setIconUrl(iconUrl);
        downLoadTaskInfo.setVersionName(versionName);
        downLoadTaskInfo.setFileSavePath(file_save_path);
        downLoadTaskInfo.setClassCode(classCode);
        downLoadTaskInfo.setSource(source);
        downLoadTaskInfo.setFileLength(byteSize);
        downLoadTaskInfo.setType(0);// 上报用的，0下载，1下载完成，2安装，3打开
        StatisticsManager.getInstance().statistics(downLoadTaskInfo);
        //HttpUtils http = new HttpUtils();
        HttpUtils http = HttpHelper.getHttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(downlaodUrl, file_save_path, true, false, new ManagerCallBack(downLoadTaskInfo, callback));
        downLoadTaskInfo.setHandler(handler);
        downLoadTaskInfo.setState(handler.getState());
        DownLoadTaskInfoList.add(downLoadTaskInfo);
        db.saveOrUpdate(downLoadTaskInfo);
    }
    /**
     * 添加一个下载任务
     * @param info
     * @param callback
     * @throws DbException
     */
    public void addNewDownload(ApkInfo info,RequestCallBack<File> callback) throws DbException{
    	if (!readSDCard(info.getSize()))return;	
    	DownLoadTaskInfo oldTaskInfo = getTask(info.getPackName());
    	if(oldTaskInfo!=null){
    		resumeDownload(oldTaskInfo, null);
    		return;
    	}
    	String file_save_path = filePath+System.currentTimeMillis()+"_zxly.apk";
        DownLoadTaskInfo taskInfo = new DownLoadTaskInfo();
        taskInfo.setDownloadUrl(info.getDownUrl());
        taskInfo.setAutoRename(false);
        taskInfo.setAutoResume(true);
        taskInfo.setFileName(info.getAppName());
        taskInfo.setPackageName(info.getPackName());
        taskInfo.setFileSavePath(file_save_path);
        taskInfo.setIconUrl(info.getIcon());
        taskInfo.setVersionName(info.getVerName());
        taskInfo.setClassCode(info.getClassCode());
        taskInfo.setSource(info.getSource());
        taskInfo.setFileLength(info.getByteSize());
        taskInfo.setType(0);// 上报用的，0下载，1下载完成，2安装，3打开
        StatisticsManager.getInstance().statistics(taskInfo);
       // HttpUtils http = new HttpUtils();
        HttpUtils http = HttpHelper.getHttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(info.getDownUrl(), file_save_path, true, false, new ManagerCallBack(taskInfo, callback));
        taskInfo.setHandler(handler);
        taskInfo.setState(handler.getState());
        DownLoadTaskInfoList.add(taskInfo);
        db.saveOrUpdate(taskInfo);
    }
    
	  /**
	   * 自身升级下载
	   * @param downUrl
	   * @param vercode
	   * @throws DbException
	   */
    public void addNewDownload(String downUrl,int vercode) throws DbException{
    	if(getTask(mContext.getPackageName())!=null)return;
    	String file_save_path = filePath+System.currentTimeMillis()+"_zxly.apk";
        DownLoadTaskInfo taskInfo = new DownLoadTaskInfo();
        taskInfo.setDownloadUrl(downUrl);
        taskInfo.setAutoRename(false);
        taskInfo.setAutoResume(true);
        taskInfo.setFileName("900应用市场");
        taskInfo.setPackageName(mContext.getPackageName());
        taskInfo.setFileSavePath(file_save_path);
        taskInfo.setVersionCode(vercode);
       // HttpUtils http = new HttpUtils();
        HttpUtils http = HttpHelper.getHttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(downUrl, file_save_path, true, false, new ManagerCallBack(taskInfo, null));
        taskInfo.setHandler(handler);
        taskInfo.setState(handler.getState());
        DownLoadTaskInfoList.add(taskInfo);
        db.saveOrUpdate(taskInfo);
    }
    /**
     * 恢复下载
     * @param DownLoadTaskInfo
     * @param callback
     * @throws DbException
     */
    public void resumeDownload(DownLoadTaskInfo DownLoadTaskInfo, final RequestCallBack<File> callback) throws DbException {
    	if (!readSDCard(DownLoadTaskInfo.getFileLength()/1024/1024))return;	
        //HttpUtils http = new HttpUtils();
    	 HttpUtils http = HttpHelper.getHttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(
                DownLoadTaskInfo.getDownloadUrl(),
                DownLoadTaskInfo.getFileSavePath(),
                DownLoadTaskInfo.isAutoResume(),
                DownLoadTaskInfo.isAutoRename(),
                new ManagerCallBack(DownLoadTaskInfo, callback));
        DownLoadTaskInfo.setHandler(handler);
        DownLoadTaskInfo.setState(handler.getState());
        db.saveOrUpdate(DownLoadTaskInfo);
    }
    
    public void removeDownload(DownLoadTaskInfo DownLoadTaskInfo) throws DbException {
        HttpHandler<File> handler = DownLoadTaskInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
        }
        DownLoadTaskInfoList.remove(DownLoadTaskInfo);
        db.delete(DownLoadTaskInfo);
        File file = new File(DownLoadTaskInfo.getFileSavePath());
        if(file.exists()){
        	file.delete();
        }
        DownLoadTaskInfo = null;
    }

    /**
     * 设置下载的任务已经安装
     * @param packageName
     * @throws DbException 
     */
    public void setTaskInstall(String packageName) throws DbException{
    	DownLoadTaskInfo info;
    	for (int i = 0,length = DownLoadTaskInfoList.size(); i < length; i++) {
    		info = DownLoadTaskInfoList.get(i);
			if(info.getPackageName().equals(packageName)){
				Logger.e(TAG, "设置安装标记");
				info.setInstalled(true);
				db.saveOrUpdate(info);
			}
		}
    }
    /**
     * 设置下载的任务已经被卸载
     * @param packageName
     * @throws DbException 
     */
    public void setTaskUnInstall(String packageName) throws DbException{
    	DownLoadTaskInfo info;
    	for (int i = 0,length = DownLoadTaskInfoList.size(); i < length; i++) {
    		info = DownLoadTaskInfoList.get(i);
			if(info.getPackageName().equals(packageName)){
				Logger.e(TAG, "设置卸载标记");
				info.setInstalled(false);
				db.saveOrUpdate(info);
			}
		}
    }
    /**
     * 删除多个下载任务
     * @param taskList
     * @throws DbException
     */
    public void removeDownload(List<DownLoadTaskInfo> taskList) throws DbException{
    	 for (DownLoadTaskInfo downLoadTaskInfo : taskList) {
				removeDownload(downLoadTaskInfo);
			 
		}	
    }
    /***
     * 暂停下载任务
     * @param DownLoadTaskInfo
     * @throws DbException
     */
    public void stopDownload(DownLoadTaskInfo downLoadTaskInfo) throws DbException {
        if(downLoadTaskInfo==null)return;
        HttpHandler<File> handler = downLoadTaskInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            Logger.e(TAG, "stopDownload--->"+handler.getState()+"--"+downLoadTaskInfo.getState());
            handler.cancel();
            if(downLoadTaskInfo.getState()!=State.SUCCESS){
        		
        		downLoadTaskInfo.setState(handler.getState());
        	}
        } 
        db.saveOrUpdate(downLoadTaskInfo);
    }
    /**
     * 暂停全部下载任务
     * @throws DbException
     */
    public void stopAllDownload() throws DbException {
        for (DownLoadTaskInfo DownLoadTaskInfo : DownLoadTaskInfoList) {
            HttpHandler<File> handler = DownLoadTaskInfo.getHandler();
            if (handler != null && !handler.isCancelled()) {
                handler.cancel();
                if(DownLoadTaskInfo.getState()!=State.SUCCESS){
            		DownLoadTaskInfo.setState(handler.getState());
            	}
            }
           
        }
        db.saveOrUpdateAll(DownLoadTaskInfoList);
    }
    /**
     * 退出应用，后台下载
     * @throws DbException
     */
    public void backupDownLoadTaskInfoList() throws DbException {
        for (DownLoadTaskInfo DownLoadTaskInfo : DownLoadTaskInfoList) {
            HttpHandler<File> handler = DownLoadTaskInfo.getHandler();
            if (handler != null) {
                DownLoadTaskInfo.setState(handler.getState());
            }
        }
        db.saveOrUpdateAll(DownLoadTaskInfoList);
    }

    public int getMaxDownloadThread() {
        return maxDownloadThread;
    }

    public void setMaxDownloadThread(int maxDownloadThread) {
        this.maxDownloadThread = maxDownloadThread;
    }
	public class ManagerCallBack extends RequestCallBack<File> {
        private DownLoadTaskInfo downLoadTaskInfo;
        private RequestCallBack<File> baseCallBack;

        public RequestCallBack<File> getBaseCallBack() {
            return baseCallBack;
        }

        public void setBaseCallBack(RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
        }

        private ManagerCallBack(DownLoadTaskInfo downLoadTaskInfo, RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
            this.downLoadTaskInfo = downLoadTaskInfo;
        }

        @Override
        public Object getUserTag() {
            if (baseCallBack == null) return null;
            return baseCallBack.getUserTag();
        }

        @Override
        public void setUserTag(Object userTag) {
            if (baseCallBack == null) return;
            baseCallBack.setUserTag(userTag);
        }

        @Override
        public void onStart() {
        	Log.e("fengruyi", "开始下载"+downLoadTaskInfo);
        	if(handler!=null){
        		Message msg = handler.obtainMessage();
        		msg.what = LOADING;
        		msg.obj = downLoadTaskInfo;
        		handler.sendMessage(msg);
        	}
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {
            	downLoadTaskInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onStart();
            }
        }

        @Override
        public void onCancelled() {
       
        	if(handler!=null){
        		Message msg = handler.obtainMessage();
        		msg.what = CANCLED;
        		msg.obj = downLoadTaskInfo;
        		handler.sendMessage(msg);
        	}
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {	
            	
            	downLoadTaskInfo.setState(handler.getState());
            }
            try {
            	
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
            	
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
            	
                baseCallBack.onCancelled();
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
        	Log.e("fengruyi", "正在下载"+downLoadTaskInfo);
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {	
            	downLoadTaskInfo.setState(handler.getState());
            }
            downLoadTaskInfo.setRate((int)(current-downLoadTaskInfo.getProgress()));
            downLoadTaskInfo.setFileLength(total);
            downLoadTaskInfo.setProgress(current);
           
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onLoading(total, current, isUploading);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
        	Log.e("fengruyi", "下载成功"+downLoadTaskInfo);
            if(downLoadTaskInfo!=null){
                downLoadTaskInfo.setType(1);//下载完成统计
                StatisticsManager.getInstance().statistics(downLoadTaskInfo);
            }
        	if(handler!=null){
        		Message msg = handler.obtainMessage();
        		msg.what = SUCCESS;
        		msg.obj = downLoadTaskInfo;
        		handler.sendMessage(msg);
        	}
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {
            	downLoadTaskInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onSuccess(responseInfo);
            }
            
            AppUtil.installApk(mContext,downLoadTaskInfo);
        }

        @Override
        public void onFailure(HttpException error, String msg) {
        	Log.e("fengruyi", "下载失败"+downLoadTaskInfo);
        	if(handler!=null){
        		Message msg1 = handler.obtainMessage();
        		msg1.what = FAILUE;
        		msg1.obj = downLoadTaskInfo;
        		handler.sendMessage(msg1);
        	}
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {
            	downLoadTaskInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onFailure(error, msg);
            }
        }
    }

    private class HttpHandlerStateConverter implements ColumnConverter<HttpHandler.State> {

        @Override
        public HttpHandler.State getFieldValue(Cursor cursor, int index) {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFieldValue(String fieldStringValue) {
            if (fieldStringValue == null) return null;
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
            return fieldValue.value();
        }

        @Override
        public ColumnDbType getColumnDbType() {
            return ColumnDbType.INTEGER;
        }
    }
    
    
    public interface DownloadStateListener{
    	public void onStart(DownLoadTaskInfo info);
    	public void onLoading(DownLoadTaskInfo info);
    	public void onCancle(DownLoadTaskInfo info);
    	public void onSuccess(DownLoadTaskInfo info);
    	public void onFailure(DownLoadTaskInfo info);
    }
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	/**
	 * 升级自身应用
	 * @param info 检测到的信息
	 * @throws DbException 
	 */
	public void upgradeMyapp(UpdateInfo info) throws DbException{
		   DownLoadTaskInfo task = getTask(mContext.getPackageName());//检测自身应用是否存在于下载任务中
		   if(task!=null){
			   DownLoadTaskInfoList.remove(task);//不管存不存在，都把任务给清了
		       db.delete(task);
		   }
		   if(task!=null&&task.getState() == State.SUCCESS&&info.getVerCode() == task.getVersionCode()){
			   File file  = new File(task.getDownloadUrl());
			   if(file.exists()){
				   AppUtil.installApk(BaseApplication.getInstance(), task);
			   }else{
				   addNewDownload(info.getUrl(), info.getVerCode());
			   }
		   }else{
			    addNewDownload(info.getUrl(), info.getVerCode());
		   }
	}
	 
	 /**
	  * 是否有Sd卡
	  * @return
	  */
	 public  boolean hasSdcard() {
	     String status = Environment.getExternalStorageState();
	     if (status.equals(Environment.MEDIA_MOUNTED)) {
	         return true;
	     } else {
	         return false;
	     }
	 }
	 

	public boolean  readSDCard(float size) {
    	 String state = Environment.getExternalStorageState();
    	 if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	  StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    	  long blockSize = sf.getBlockSize();   
			  long blocks = sf.getAvailableBlocks();   
			  long availableSpare = (blocks*blockSize)/1024/1024;  
			  Logger.e("", "sd可用空间>"+availableSpare);
			  if(availableSpare<=MIN_STORAGE){
				  if(dialog==null){
						dialog = new PromptDialog(BaseApplication.getInstance());
						dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
						dialog.setTxt("空间不足", "手机存储空间不足，请释放内存");
						dialog.setCanceledOnTouchOutside(true);
						dialog.show(new OnClickListener() {
							
							public void onClick(View arg0) {
								dialog.dismiss();
								switch (arg0.getId()) {
								case R.id.btn_ok:
									mContext.startActivity(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
									break;
								}
								
							}
						});
					}else{
						dialog.setTxt("空间不足", "手机存储空间不足，请释放内存");
						dialog.show();
					}
				  return false;
			  }
			  else if(availableSpare<size){
				  if(dialog==null){
						dialog = new PromptDialog(BaseApplication.getInstance());
						dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
						dialog.setCanceledOnTouchOutside(true);
						dialog.setTxt("空间不足", "手机存储空间小于"+size+"M，请释放内存");
						dialog.show(new OnClickListener() {
							
							public void onClick(View arg0) {
								dialog.dismiss();
								switch (arg0.getId()) {
								case R.id.btn_ok:
									mContext.startActivity(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
									break;
								}
								
							}
						});
					}else{
						dialog.setTxt("空间不足","手机存储空间小于"+size+"M，请释放内存");
						dialog.show();
					}
				  return false;
			  }
    	 }
    	 return true;
    	}
}
