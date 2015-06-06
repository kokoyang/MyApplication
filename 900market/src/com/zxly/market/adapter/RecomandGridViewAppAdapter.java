package com.zxly.market.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.MainActivity;
import com.zxly.market.bean.DownloadRequestCallBack;
import com.zxly.market.bean.PackageState;
import com.zxly.market.bean.ViewHolder;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.service.DownloadService;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.UMengAgent;
import com.zxly.market.view.RoundedImageView;
/**
 * 热门推荐适配器
 * @author fengruyi
 *
 */
public class RecomandGridViewAppAdapter extends RelativelayoutBaseAdaper{
	
	private static final String TAG = "GridViewAppAdapter";
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_app_defaul)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_app_defaul)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_app_defaul)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成  
	public DownloadManager downloadManager;
	private List<ApkInfo> mResultList;//保存返回列表中未安装的
	private List<ApkInfo> mInstalledList;//保存返回列表中已安装
	private final int DefaulSize = 8;//最多可以显示8个
	public RecomandGridViewAppAdapter(Context context,List<ApkInfo> list){
		super(context, list);
		downloadManager = DownloadService.getDownloadManager(BaseApplication.getInstance());
	}
	
	public int getCount() {
		return mList == null?0:calculateAppsize();
	}
	

	/**
	 * 如果此apk已经安装，未安装列表数量超过8个，则从列表中移除，再补充一个未安装的
	 * @param apkInfo
	 * @return  有删除则返回true,否返回false
	 */
	public boolean removeItem(ApkInfo apkInfo){
		if(mResultList!=null){
			int length = mResultList.size();
			if(length>DefaulSize&&mList.contains(apkInfo)){
				mList.remove(apkInfo);
				mResultList.remove(apkInfo);
				mList.add(mResultList.get(length-2));
				return true;
			}
			 Logger.e("", "要移除一个应用----false");
		}
		return false;
	}	
	/**
	 * 计算返回的推荐列表数中未安装的数量
	 * @return
	 */
	private int calculateAppsize(){
		int length = mList.size();	
		if(length<=DefaulSize){
			return length;
		}else{
			List<ApkInfo> installedList = BaseApplication.getInstance().getmInstalledAppList();//本地已经安装的应用表
			mResultList = new ArrayList<ApkInfo>();
			mInstalledList = new ArrayList<ApkInfo>();
			int size = 0;
			for(int i = 0;i<length;i++){
				if(!installedList.contains(mList.get(i))){
					mResultList.add(mList.get(i));
					size ++;
				}else{
					mInstalledList.add(mList.get(i));
				}
			}
			if(size>=8){
				mList = mResultList.subList(0, DefaulSize);
			}else{
				mResultList.addAll(mInstalledList.subList(0, DefaulSize-size));
				mList = mResultList;
			}
		}
		return DefaulSize;
		
	}
	
	public class GridviewHolder extends ViewHolder{
    	@ViewInject(R.id.iv_app_icon)
 		RoundedImageView iv_icon;
 	    @ViewInject(R.id.tv_app_name)
 		TextView tv_appName;
 	    @ViewInject(R.id.tv_app_size)
	    TextView tv_appSize ;
 	    @ViewInject(R.id.btn_op)
	    Button btn_down;
 	    ApkInfo apkInfo;
	    PackageState packageState;
	    public GridviewHolder(ApkInfo apkInfo){
	    	this.apkInfo = apkInfo;
	    	taskInfo = downloadManager.getTask(apkInfo.getPackName());
	    }
	    public void update(ApkInfo apkInfo) {
            this.apkInfo = apkInfo;
            taskInfo = downloadManager.getTask(apkInfo.getPackName());
            bindData();
            refresh();
        }
	    public void bindData(){
	    	ImageLoaderUtil.Load(apkInfo.getIcon(), iv_icon, options);
 	    	tv_appName.setText(apkInfo.getAppName());
 	    	tv_appSize.setText(apkInfo.getSize()+"MB");
 	    	refreshHander();
	    }
	    @OnClick(R.id.btn_op)
	    public void download(View view){
	    	switch (packageState) {
			case KEEPDOWNLOAD:
				 try {
					 if(taskInfo!=null){
						 downloadManager.stopDownload(taskInfo);
					 }
                 } catch (DbException e) {
                     LogUtils.e(e.getMessage(), e);
                 }
				 refreshHander();
				break;
			case FAIL:
			case CANCEL:
				 try {
					 if(taskInfo!=null){
						 downloadManager.resumeDownload(taskInfo, new DownloadRequestCallBack());
					 }
                 } catch (DbException e) {
                     LogUtils.e(e.getMessage(), e);
                 }
				 refreshHander();
				break;
			case DOWNLOADED:
				Logger.e(TAG, "url--->"+taskInfo.getFileSavePath());
				 AppUtil.installApk(mContext, taskInfo);
				break;
			case NEEDUPDATE:
				 	
			case NOEXIST:
				try {
					downloadManager.addNewDownload(apkInfo, new DownloadRequestCallBack());
				} catch (DbException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				taskInfo = downloadManager.getTask(apkInfo.getPackName());
				if(apkInfo!=null&&apkInfo.getPosition()<10
						&&mContext instanceof MainActivity){
					UMengAgent.onEvent(mContext,UMengAgent.arry_hold[apkInfo.getPosition()]);
				}
				refreshHander();
				break;
			
			case INSTALLED:
				AppUtil.startApk(apkInfo);
				break;
			default:
				break;
			}
	    }
    	public void refresh() {
        	if(taskInfo !=null){
	            switch (taskInfo.getState()) {
	                case WAITING:
	                case STARTED:
	                	btn_down.setText(R.string.waiting);
	                	packageState = PackageState.KEEPDOWNLOAD;
	                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_57be17));
	                	btn_down.setBackgroundResource(R.drawable.btn_round_border_57be17);
	                    break;
	                case LOADING:
	                	if(taskInfo.getFileLength()>0){
	                		btn_down.setText((taskInfo.getProgress()*100/taskInfo.getFileLength())+"%");
	                	}else{
	                		btn_down.setText("0%");
	                	}
	                	packageState = PackageState.KEEPDOWNLOAD;
	                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_999999));
	                	btn_down.setBackgroundResource(R.drawable.btn_round_border_999999);
	                    break;
	                case CANCELLED:
	                	btn_down.setText(R.string.resume);
	                	packageState = PackageState.CANCEL;
	                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_57be17));
	                	btn_down.setBackgroundResource(R.drawable.btn_round_border_57be17);
	                    break;
	                case SUCCESS:
	                	if(AppUtil.isAppInstalled(mContext, taskInfo.getPackageName())){//存在下载任务中并且已经安装
	                		btn_down.setText(R.string.open);
	                		packageState = PackageState.INSTALLED;
	                		btn_down.setTextColor(mContext.getResources().getColor(R.color.color_6fc9e9));
	                    	btn_down.setBackgroundResource(R.drawable.btn_round_border_6fc9e9);
	                	}
	                	else{
		                	File file = new File(taskInfo.getFileSavePath());
		                	if(file.exists()){
		                		btn_down.setText(R.string.install);
		                		packageState = PackageState.DOWNLOADED;
		                		btn_down.setTextColor(mContext.getResources().getColor(R.color.color_6fc9e9));
		                    	btn_down.setBackgroundResource(R.drawable.btn_round_border_6fc9e9);
		                	}else{
		                		try {
									downloadManager.removeDownload(taskInfo);
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		                		btn_down.setText(R.string.download);
		                		packageState = PackageState.NOEXIST;
		                		btn_down.setTextColor(mContext.getResources().getColor(R.color.color_57be17));
			                	btn_down.setBackgroundResource(R.drawable.btn_round_border_57be17);
		                	}
	                	}
	                    break;
	                case FAILURE:
	                	btn_down.setText(R.string.retry);
	                	packageState = PackageState.FAIL;
	                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_fe9e8a));
	                	btn_down.setBackgroundResource(R.drawable.btn_round_border_fe9e8a);
	                    break;
	                default:
	                    break;
	            }
        	}else{	
        		ApkInfo tempInfo = BaseApplication.getInstance().getInstalledApk(apkInfo.getPackName());
        		if(tempInfo!=null){//表示已经安装
        			if(tempInfo.getVerName()!=null&&tempInfo.getVerName().compareTo(apkInfo.getVerName())<0){//有更新的版本
        				btn_down.setText(R.string.update);
            			packageState = PackageState.NEEDUPDATE;
            			btn_down.setTextColor(mContext.getResources().getColor(R.color.color_57be17));
                    	btn_down.setBackgroundResource(R.drawable.btn_round_border_57be17);
        			}else{
        				btn_down.setText(R.string.open);
            			packageState = PackageState.INSTALLED;
            			btn_down.setTextColor(mContext.getResources().getColor(R.color.color_6fc9e9));
                    	btn_down.setBackgroundResource(R.drawable.btn_round_border_6fc9e9);
        			}
        		}else{//表示未安装也未在下载任务中
        			btn_down.setText(R.string.download);
        			packageState = PackageState.NOEXIST;
        			btn_down.setTextColor(mContext.getResources().getColor(R.color.color_57be17));
                	btn_down.setBackgroundResource(R.drawable.btn_round_border_57be17);
        		}
        	}

    	}
    	/**
    	 * 更新下载句柄
    	 */
    	public void refreshHander(){
   			taskInfo = downloadManager.getTask(apkInfo.getPackName());
    		refresh();
    		if(taskInfo!=null){
    			   HttpHandler<File> handler = taskInfo.getHandler();
    		        if (handler != null) {
    		            RequestCallBack callBack = handler.getRequestCallBack();
    		            if (callBack instanceof DownloadManager.ManagerCallBack) {	 
    		                DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
    		                if (managerCallBack.getBaseCallBack() == null &&! (managerCallBack.getBaseCallBack() instanceof DownloadRequestCallBack)) {
    		                    managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
    		                }
    		            }
    		            callBack.setUserTag(new WeakReference<GridviewHolder>(this));
    		        }
    		   }
    	}
    }
	public View getView(int position) {
		GridviewHolder holder = null;
		ApkInfo info  = mList.get(position);
		info.setPosition(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_app, null);
        holder = new GridviewHolder(info);
        ViewUtils.inject(holder, view);
        view.setTag(holder);
        holder.bindData();
        holder.refresh();
	    return view;
	}
	
	
}
