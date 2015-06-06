package com.zxly.market.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
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
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.service.DownloadService;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.LimitLengthTextView;
import com.zxly.market.view.RoundedImageView;

/**
 * 通用列表app适配器
 * @author fengruyi
 *
 */
public class CommonListAPPAdapter extends BaseAdapter{
	private String TAG = "CommonListAPPAdapter";
	DownloadManager downloadManager;
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_app_defaul)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_app_defaul)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_app_defaul)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成  
	private List<ApkInfo> mList;
	private Context mContext;
	private LayoutInflater mInflater;
	public CommonListAPPAdapter(Context context,List<ApkInfo> list){
		mInflater = LayoutInflater.from(context);
		mContext = context;
		if(list!=null){
			mList = list;
		}else{
			mList = new ArrayList<ApkInfo>();
		}
		downloadManager = DownloadService.getDownloadManager(BaseApplication.getInstance());
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


	public View getView(int position, View view, ViewGroup arg2) {
		Logger.e(TAG, "getView-->"+position);
		MyViewHolder holder = null;
		ApkInfo info  = mList.get(position);
		if (view == null) {
            view = mInflater.inflate(R.layout.item_list_app, null);
            holder = new MyViewHolder(info);
            ViewUtils.inject(holder, view);
            view.setTag(holder);
            holder.bindData();
            holder.refresh();
        } else {
            holder =  (MyViewHolder) view.getTag();
            holder.update(info);
        }
		if(position == getCount()-1){
			holder.diliver.setVisibility(View.GONE);
		}else{
			holder.diliver.setVisibility(View.VISIBLE);
		}
	   if(holder.taskInfo!=null){
		   HttpHandler<File> handler = holder.taskInfo.getHandler();  
	        if (handler != null) {
	            RequestCallBack callBack = handler.getRequestCallBack();
	            if (callBack instanceof DownloadManager.ManagerCallBack) {
	            	 DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
	                if (managerCallBack.getBaseCallBack() == null &&! (managerCallBack.getBaseCallBack() instanceof DownloadRequestCallBack)) {
	                    managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
	                }
	            }
	            callBack.setUserTag(new WeakReference<MyViewHolder>(holder));
	        }
	   }
		return view;
	}
	 
	public void add(List<ApkInfo> list){
		if(mList!=null){
			mList.addAll(list);
			notifyDataSetChanged();
		}
	}
	public void clear(){
		if(mList!=null){
			mList.clear();
		}
	}

	public class MyViewHolder extends ViewHolder{
	    @ViewInject(R.id.iv_app_icon)
		RoundedImageView iv_icon;
	    @ViewInject(R.id.tv_app_name)
	    LimitLengthTextView tv_appName;
	    @ViewInject(R.id.tv_game_type)
	    TextView tv_appType;
		@ViewInject(R.id.tv_app_download_count)
		TextView tv_count;
	    @ViewInject(R.id.rb_rank)
	    RatingBar rb_rank;
	    @ViewInject(R.id.tv_app_size)
	    TextView tv_appSize ;
	    @ViewInject(R.id.tv_app_info)
	    TextView tv_description;
	    @ViewInject(R.id.diliver)
	    View diliver;
	    @ViewInject(R.id.btn_down)
	    Button btn_down;

	    ApkInfo apkInfo;

	    PackageState packageState;
	    public MyViewHolder(ApkInfo apkInfo){
	    	this.apkInfo = apkInfo;
	    	taskInfo = downloadManager.getTask(apkInfo.getPackName());
	    	
	    }
	    @OnClick(R.id.btn_down)
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
                 notifyDataSetChanged();
				break;
			case DOWNLOADED:
				Logger.e(TAG, "url--->"+taskInfo.getFileSavePath());
				 AppUtil.installApk(mContext, taskInfo);
				break;
			case NOEXIST:
				
			case NEEDUPDATE:
				try {
					downloadManager.addNewDownload(apkInfo, new DownloadRequestCallBack());
				} catch (DbException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				  taskInfo = downloadManager.getTask(apkInfo.getPackName());
				  notifyDataSetChanged();
				break;
			case INSTALLED:
				AppUtil.startApk(apkInfo);
				
				break;
			default:
				break;
			}
	    }
	    
	    public void update(ApkInfo apkInfo) {
            this.apkInfo = apkInfo;
            taskInfo = downloadManager.getTask(apkInfo.getPackName());
            bindData();
            refresh();
        }
	    public void bindData(){
	    	ImageLoaderUtil.Load(apkInfo.getIcon(), iv_icon, options);
        	rb_rank.setEnabled(false);
 	    	tv_appName.setText(apkInfo.getAppName(),9);
			boolean isDisCoveryPage = false;
			if(mContext instanceof MainActivity){
				if(((MainActivity)mContext).getCurrentPage()==2){
					isDisCoveryPage = true;
				}
			}
			if(isDisCoveryPage){
				tv_count.setVisibility(View.VISIBLE);
				rb_rank.setVisibility(View.GONE);
				long count = apkInfo.getDownCount();
				String str = "";
				if(count>=10000){
					str = (count/10000)+mContext.getString(R.string.discovery_list_down_wan_count);
				}else{
					str = count+mContext.getString(R.string.discovery_list_down_count);
				}
				tv_count.setText(str);
			}else{
				tv_count.setVisibility(View.GONE);
				rb_rank.setVisibility(View.VISIBLE);
				rb_rank.setRating(apkInfo.getGrade() / 2.0f);
			}
 	    	tv_appSize.setText(apkInfo.getSize()+"MB");
 	    	if(TextUtils.isEmpty(apkInfo.getContent())){
 	    		tv_description.setText("");
 	    	}else{
 	    		tv_description.setText(Html.fromHtml(apkInfo.getContent()));
 	    	}
 	    	if(apkInfo.getType()==1){
 	    		tv_appType.setVisibility(View.VISIBLE);
 	    	}else{
 	    		tv_appType.setVisibility(View.INVISIBLE);
 	    	}
	    }
	    @Override
        public void refresh() {
        	if(taskInfo !=null){
	            switch (taskInfo.getState()) {
	                case WAITING:
	                case STARTED:
	                	btn_down.setText(R.string.waiting);
	                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_57be17));
	                	btn_down.setBackgroundResource(R.drawable.btn_round_border_57be17);
	                	packageState = PackageState.KEEPDOWNLOAD;
	                    break;
	                case LOADING:
	                	if(taskInfo.getFileLength()>0){
	                		btn_down.setText((taskInfo.getProgress()*100/taskInfo.getFileLength())+"%");
	                	}else{
	                		btn_down.setText("0%");
	                	}
	                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_999999));
	                	btn_down.setBackgroundResource(R.drawable.btn_round_border_999999);
	                	packageState = PackageState.KEEPDOWNLOAD;
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
	}
	
	
}
