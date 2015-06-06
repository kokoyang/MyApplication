
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.zxly.market.adapter.RecomandGridViewAppAdapter.GridviewHolder;
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
import com.zxly.market.utils.ViewUtil;
import com.zxly.market.view.LimitLengthTextView;
import com.zxly.market.view.RoundedImageView;

/** 
 * @ClassName: HotAppListAdapter  
 * @Description: 精品页热门应用列表适配器，隔五个应用item嵌套一个广告banner 
 * @author: fengruyi 
 * @date:2015-4-10 下午3:07:36   
 */
public class HotAppListAdapter extends BaseAdapter{
	public static final String TAG = "HotAppListAdapter";
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
	final int TYPE_APP = 0;//app列表布局
	final int TYPE_BANNER  = 1;//banner类型布局
	final int SPACEING_NUM = 5;//隔五个就显示一个
	DownloadManager downloadManager;
	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	public HotAppListAdapter(Context context,List list){
		mContext = context;
		mList = list;
		try {
			mInflater = LayoutInflater.from(context);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		downloadManager = DownloadService.getDownloadManager(BaseApplication.getInstance());
	}
	/* 每个convert view都会调用此方法，获得当前所需要的view样式 
	 * @param position
	 * @return  
	 *
	 */  
	@Override
	public int getItemViewType(int position) {
		if((position+1)%6 == 0){
			return TYPE_BANNER;
		}
		return TYPE_APP;
	}
	 @Override  
     public int getViewTypeCount() {  
         return 2;  
     }  
	
	@Override
	public int getCount() {
		return mList == null?0:mList.size();
	}


	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

  
	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {

//		Logger.e(TAG, "getView--->"+position);
		ApkInfo info = mList.get(position);
		int type = getItemViewType(position);
		ViewHolder_App holder_app = null;
		ViewHolder_banner holder_banner = null;
		if(contentView == null){
			switch (type) {
			case TYPE_APP:
				holder_app = new ViewHolder_App(info);
				contentView = mInflater.inflate(R.layout.item_list_app, null);
			
				ViewUtils.inject(holder_app, contentView);
			    contentView.setTag(holder_app); 
			    holder_app.bindData();
			    holder_app.refresh();
			    if((position+2)%6==0){
					holder_app.diliver.setVisibility(View.GONE);
				}else{
					holder_app.diliver.setVisibility(View.VISIBLE);
				}
				break;

			case TYPE_BANNER:
				holder_banner = new ViewHolder_banner();
				contentView = mInflater.inflate(R.layout.item_list_banner, null);
				contentView.setLayoutParams(lp);
				holder_banner.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
				holder_banner.iv_content = (ImageView) contentView.findViewById(R.id.iv_content);
				ViewUtil.setViewHeight(holder_banner.iv_content, (int)(BaseApplication.mWidthPixels*0.35));
				contentView.setTag(holder_banner);
				break;
			}
		}else{
			switch (type) {
			case TYPE_APP:
				holder_app = (ViewHolder_App) contentView.getTag();
				holder_app.update(info);
				if((position+2)%6==0){
					holder_app.diliver.setVisibility(View.GONE);
				}else{
					holder_app.diliver.setVisibility(View.VISIBLE);
				}
				break;

			case TYPE_BANNER:
				holder_banner = (ViewHolder_banner) contentView.getTag();
				break;
			}
		}
		
		switch (type) {
		case TYPE_APP:
			if(holder_app.taskInfo!=null){
				   HttpHandler<File> handler = holder_app.taskInfo.getHandler();
			       
			        if (handler != null) {
			            RequestCallBack callBack = handler.getRequestCallBack();
			            if (callBack instanceof DownloadManager.ManagerCallBack) {	 
			                DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
			                if (managerCallBack.getBaseCallBack() == null &&! (managerCallBack.getBaseCallBack() instanceof DownloadRequestCallBack)) {
			                    managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
			                }
			            }
			            callBack.setUserTag(new WeakReference<ViewHolder_App>(holder_app));
			        }
			   }
			break;

		case TYPE_BANNER:
			if(TextUtils.isEmpty(info.getContent())){
				contentView.setVisibility(View.GONE);
				contentView.getLayoutParams().height = 1;
			}else{
				holder_banner.tv_title.setText(info.getContent());
				ImageLoaderUtil.Load(info.getIcon(), holder_banner.iv_content, ImageLoaderUtil.getRoundOptions(R.drawable.icon_banner_defalt, 0));
				contentView.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
				contentView.setVisibility(View.VISIBLE);
			}
			break;
		}

		return contentView;
	}
	
	public class ViewHolder_App extends ViewHolder{
		@ViewInject(R.id.iv_app_icon)
		RoundedImageView iv_icon;
	    @ViewInject(R.id.tv_app_name)
	    LimitLengthTextView  tv_appName;
	    @ViewInject(R.id.tv_game_type)
	    TextView tv_appType;
	    @ViewInject(R.id.rb_rank)
	    RatingBar rb_rank;
	    @ViewInject(R.id.tv_app_size)
	    TextView tv_app_size;
	    @ViewInject(R.id.tv_app_info)
	    TextView tv_description;
	    @ViewInject(R.id.diliver)
	    View diliver;
	    @ViewInject(R.id.btn_down)
	    Button btn_down; 
	    
	    ApkInfo apkInfo;
	    PackageState packageState;
	    public ViewHolder_App(ApkInfo apkInfo){
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
		public void bindData(){
			ImageLoaderUtil.Load(apkInfo.getIcon(), iv_icon, options);
			tv_appName.setText(apkInfo.getAppName(),9);
			tv_app_size.setText(apkInfo.getSize()+"M");
			if(TextUtils.isEmpty(apkInfo.getContent())){
 	    		tv_description.setText("");
 	    	}else{
 	    		tv_description.setText(Html.fromHtml(apkInfo.getContent()));
 	    	}
			rb_rank.setRating(apkInfo.getGrade()/2.0f);
			rb_rank.setEnabled(false);
			if(apkInfo.getType()==1){
 	    		tv_appType.setVisibility(View.VISIBLE);
 	    	}else{
 	    		tv_appType.setVisibility(View.INVISIBLE);
 	    	}
		}
	    public void update(ApkInfo apkInfo) {
            this.apkInfo = apkInfo;
            taskInfo = downloadManager.getTask(apkInfo.getPackName());
            bindData();
            refresh();
        }
	    @Override
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
	}
		
		
	
	
	class ViewHolder_banner{
		TextView tv_title;
		ImageView iv_content;
	}
}
