/**    
 * @FileName: ListAppUpgrade.java  
 * @Package:com.zxly.market.adapter  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-13 下午1:38:05  
 * @version V1.0    
 */
package com.zxly.market.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
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
import com.zxly.market.activity.UpgradeAppActivity;
import com.zxly.market.adapter.CommonListAPPAdapter.MyViewHolder;
import com.zxly.market.bean.DownloadRequestCallBack;
import com.zxly.market.bean.PackageState;
import com.zxly.market.bean.ViewHolder;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.RoundedImageView;

/** 
 * @ClassName: ListAppUpgrade  
 * @Description: 升级应用列表适配器 
 * @author: fengruyi 
 * @date:2015-4-13 下午1:38:05   
 */
public class ListAppUpgradeAdapter extends BaseAdapter{
	private final String TAG = "ListAppUpgradeAdapter";
	private List<ApkInfo> mList;
	private Context mContext;
	private LayoutInflater mInflater;
	DownloadManager mDownloadManager;
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_app_defaul)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_app_defaul)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_app_defaul)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成  
	
	DbUtils db;
	public ListAppUpgradeAdapter(Context context,List<ApkInfo> list,DownloadManager downloadManager,DbUtils db){
		mInflater = LayoutInflater.from(context);
		mContext = context;
	    mList = list;
	    mDownloadManager = downloadManager;
	    this.db = db;
	}
	
	public int getCount() {
		return mList == null?0:mList.size();
	}
	
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

    
	public View getView(int position, View convertView, ViewGroup parent) {
	   
		UpgradeHolderView holderView = null;
		if(convertView == null){
			holderView = new UpgradeHolderView(mList.get(position));
			convertView = mInflater.inflate(R.layout.item_list_upgrade_app, null);
		    ViewUtils.inject(holderView, convertView);
		    convertView.setTag(holderView);
		    holderView.bindata();
		    holderView.refresh();
		}else{
			holderView =  (UpgradeHolderView) convertView.getTag();
			   holderView.update(mList.get(position));
		}
		if(holderView.taskInfo!=null){
			   HttpHandler<File> handler = holderView.taskInfo.getHandler();  
		        if (handler != null) {
		            RequestCallBack callBack = handler.getRequestCallBack();
		            if (callBack instanceof DownloadManager.ManagerCallBack) {
		            	 DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
		                if (managerCallBack.getBaseCallBack() == null &&! (managerCallBack.getBaseCallBack() instanceof DownloadRequestCallBack)) {
		                    managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
		                }
		            }
		            callBack.setUserTag(new WeakReference<UpgradeHolderView>(holderView));
		        }
		   }
		return convertView;
	}
	
	



	public class UpgradeHolderView extends ViewHolder{
		@ViewInject(R.id.iv_app_icon)
		RoundedImageView iv_icon;
	    @ViewInject(R.id.tv_app_name)
		TextView tv_appName;
	    @ViewInject(R.id.tv_game_type)
	    TextView tv_appType;
	    @ViewInject(R.id.rb_rank)
	    RatingBar rb_rank;
	    @ViewInject(R.id.tv_app_size)
	    TextView tv_appSize ;
	    @ViewInject(R.id.tv_app_info)
	    TextView tv_description;
	    @ViewInject(R.id.btn_upgrade)
		public Button btn_upgrade;
	    @ViewInject(R.id.ibtn_ignore)
	    TextView iv_ignore ;
		
		ApkInfo apkInfo;
		PackageState packageState;
		public UpgradeHolderView(ApkInfo info){
			this.apkInfo = info;
			taskInfo = mDownloadManager.getTask(apkInfo.getPackName());
		}
		@OnClick(R.id.ibtn_ignore)
		public void ignore(View view){	
			mList.remove(apkInfo);	
			try {
				db.saveOrUpdate(apkInfo);
				if(taskInfo!=null){
					mDownloadManager.removeDownload(taskInfo);
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((UpgradeAppActivity)mContext).reflashView();
		}
		@OnClick(R.id.btn_upgrade)
	    public void upgrade(View view){
	    	switch (packageState) {
			case KEEPDOWNLOAD:
				 try {
					 if(taskInfo!=null){
						 mDownloadManager.stopDownload(taskInfo);
					 }
                 } catch (DbException e) {
                     LogUtils.e(e.getMessage(), e);
                 }
				break;
			case CANCEL:
				 try {
					 if(taskInfo!=null){
						 mDownloadManager.resumeDownload(taskInfo, new DownloadRequestCallBack());
					 }
                 } catch (DbException e) {
                     LogUtils.e(e.getMessage(), e);
                 }
                 notifyDataSetChanged();
				break;
			case DOWNLOADED:
				 AppUtil.installApk(mContext, taskInfo);
				break;
			case NOEXIST:
				
			case NEEDUPDATE:
				try {
					mDownloadManager.addNewDownload(apkInfo, new DownloadRequestCallBack());
				} catch (DbException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				  taskInfo = mDownloadManager.getTask(apkInfo.getPackName());
				  notifyDataSetChanged();
				break;
			case FAIL:
				 try {
					 if(taskInfo!=null){
						 mDownloadManager.resumeDownload(taskInfo, new DownloadRequestCallBack());
					 }
                 } catch (DbException e) {
                     LogUtils.e(e.getMessage(), e);
                 }
                 notifyDataSetChanged();
				break;
			case INSTALLED:
				AppUtil.startApk(apkInfo);
				break;
			default:
				break;
			}
	    }
	    public void bindata(){
	    	ImageLoaderUtil.Load(apkInfo.getIcon(), iv_icon, options);
        	rb_rank.setEnabled(false);
 	    	tv_appName.setText(apkInfo.getAppName());
 	    	rb_rank.setRating(apkInfo.getGrade()/2.0f);
 	    	tv_appSize.setText(apkInfo.getSize()+"MB");
 	    	if(TextUtils.isEmpty(apkInfo.getContent())){
 	    		tv_description.setText("");
 	    	}else{
 	    		tv_description.setText(Html.fromHtml(apkInfo.getContent()));
 	    	}	
	    }
	    public void update(ApkInfo apkInfo) {
            this.apkInfo = apkInfo;
            taskInfo = mDownloadManager.getTask(apkInfo.getPackName());
            bindata();
            refresh();
        }
	    @Override
        public void refresh() {
        	
        	if(taskInfo !=null){
	            switch (taskInfo.getState()) {
	                case WAITING:
	                	btn_upgrade.setText(R.string.waiting);
	                	packageState = PackageState.KEEPDOWNLOAD;
	                    break;
	                case STARTED:
	                	
	                case LOADING:
	                	//btn_upgrade.setText(R.string.stop);
	                	if(taskInfo.getFileLength()>0){
	                		btn_upgrade.setText((taskInfo.getProgress()*100/taskInfo.getFileLength())+"%");
	                	}else{
	                		btn_upgrade.setText("0%");
	                	}
	                	packageState = PackageState.KEEPDOWNLOAD;
	                    break;
	                case CANCELLED:
	                	btn_upgrade.setText(R.string.resume);
	                	packageState = PackageState.CANCEL;
	                    break;
	                case SUCCESS:
	                	File file = new File(taskInfo.getFileSavePath());
	                	if(file.exists()){
	                		btn_upgrade.setText(R.string.install);
	                		packageState = PackageState.DOWNLOADED;
	                	}else{
	                		try {
	                			mDownloadManager.removeDownload(taskInfo);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                		btn_upgrade.setText(R.string.update);
	                		packageState = PackageState.NOEXIST;
	                	}
	                    break;
	                case FAILURE:
	                	btn_upgrade.setText(R.string.retry);
	                	packageState = PackageState.FAIL;
	                    break;
	                default:
	                    break;
	            }
        	}else{	
        		btn_upgrade.setText(R.string.update);
        	    packageState = PackageState.NOEXIST;
        	}
        	
	    }
        
	}
}
