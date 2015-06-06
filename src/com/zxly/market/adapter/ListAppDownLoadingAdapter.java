package com.zxly.market.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zxly.market.activity.DownLoadTaskActivity;
import com.zxly.market.bean.DownloadRequestCallBack;
import com.zxly.market.bean.ViewHolder;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.PromptDialog;
import com.zxly.market.view.RoundedImageView;
/**
 * 正在下载任务列适配器
 * @author fengruyi
 *
 */
public class ListAppDownLoadingAdapter extends BaseAdapter{
	public static final String TAG = "ListAppDownLoadingAdapter";
	private DownloadManager downloadmanager;
	private List<DownLoadTaskInfo> mlist;
	private Context mContext;
	private PromptDialog mDialog;
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_app_defaul)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_app_defaul)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_app_defaul)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成  
	private String deleteContent;
	public ListAppDownLoadingAdapter(Context context,List<DownLoadTaskInfo> list,DownloadManager manager){
		mContext = context;
		mlist = list;
		downloadmanager = manager;
		deleteContent = mContext.getString(R.string.delete_content);
	}
	
	public int getCount() {
		
		return mlist == null?0:mlist.size();
	}
    
	public Object getItem(int arg0) {
		
		return mlist.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}
    
	public  void removeItem(DownLoadTaskInfo taskInfo){
		mlist.remove(taskInfo);
		notifyDataSetChanged();
	}
	public View getView(int position, View view, ViewGroup arg2) {
		DownloadItemViewHolder holder = null;
		DownLoadTaskInfo downloadInfo = mlist.get(position);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_downloading, null);
            holder = new DownloadItemViewHolder(downloadInfo);
            ViewUtils.inject(holder, view);
            view.setTag(holder);
            holder.bindData();
            holder.refresh();
        } else {
            holder = (DownloadItemViewHolder) view.getTag();
            holder.update(downloadInfo);
        }

        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null) {
            RequestCallBack callBack = handler.getRequestCallBack();
            if (callBack instanceof DownloadManager.ManagerCallBack) {
                DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
                if (managerCallBack.getBaseCallBack() == null &&! (managerCallBack.getBaseCallBack() instanceof DownloadRequestCallBack)) {
                    managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
                }
                
            }
            callBack.setUserTag(new WeakReference<DownloadItemViewHolder>(holder));
        }
		return view;
	}
	
	
	public class DownloadItemViewHolder extends ViewHolder{
        @ViewInject(R.id.iv_app_icon)
		RoundedImageView iv_icon;
        @ViewInject(R.id.tv_app_name)
		TextView iv_appname;
        @ViewInject(R.id.seebar_prosess)
		ProgressBar pb_process;
        @ViewInject(R.id.tv_app_size)
		TextView tv_size;
        @ViewInject(R.id.tv_download_rate)
		TextView tv_rate;
        @ViewInject(R.id.btn_down)
		Button  btn_down;
        @ViewInject(R.id.btn_del)
        TextView btn_del ;


        public DownloadItemViewHolder(DownLoadTaskInfo downloadInfo) {
            this.taskInfo = downloadInfo;
        }

        @OnClick(R.id.btn_down)
        public void stop(View view) {
            HttpHandler.State state = taskInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                case LOADING:
                    try {
                    	downloadmanager.stopDownload(taskInfo);
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    break;
                case CANCELLED:
                case FAILURE:
                    try {
                    	downloadmanager.resumeDownload(taskInfo, new DownloadRequestCallBack());
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

        @OnClick(R.id.btn_del)
        public void remove(View view) {
        	if(mDialog==null){
        		mDialog = new PromptDialog(mContext);
        		mDialog.setCanceledOnTouchOutside(true);
        	}
        	mDialog.setTxt(mContext.getString(R.string.title_delete), String.format(deleteContent, taskInfo.getFileName()));
        	mDialog.show(new OnClickListener() {
				
				public void onClick(View arg0) {
				  mDialog.dismiss();
				  switch (arg0.getId()) {
				  case R.id.btn_ok:
					  try { 
			            	File file = new File(taskInfo.getFileSavePath());
			            	file.delete();
			            	downloadmanager.removeDownload(taskInfo);
			            	mlist.remove(taskInfo);
			            	notifyDataSetChanged();
			               } catch (DbException e) {
			                 Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
			               }
					break;
				  }
				}
			});
           
        }
        public void bindData(){
        	ImageLoaderUtil.Load(taskInfo.getIconUrl(), iv_icon, options);
        	iv_appname.setText(taskInfo.getFileName());
        }
        public void update(DownLoadTaskInfo downloadInfo) {
            this.taskInfo = downloadInfo;
            bindData();
            refresh();
        }
        
        @Override
        public void refresh() {
        	tv_rate.setText(Formatter.formatFileSize(mContext,taskInfo.getRate())+"/s");
    	    tv_size.setText(Formatter.formatFileSize(mContext, taskInfo.getProgress())+"/"+Formatter.formatFileSize(mContext, taskInfo.getFileLength()));
            if (taskInfo.getFileLength() > 0) {
            	pb_process.setProgress((int) (taskInfo.getProgress() * 100 / taskInfo.getFileLength()));
            } else {
            	pb_process.setProgress(0);
            }
            HttpHandler.State state = taskInfo.getState();
            switch (state) {
                case WAITING:
                	btn_down.setBackgroundResource(R.drawable.btn_round_border_999999);
                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                	btn_down.setText(mContext.getString(R.string.waiting));
                	break;
                case STARTED:
                	
                case LOADING:
                	btn_down.setBackgroundResource(R.drawable.btn_round_border_999999);
                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                	btn_down.setText(mContext.getString(R.string.stop));
                    break;
                case CANCELLED:
                	btn_down.setBackgroundResource(R.drawable.btn_round_border_57be17);
                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_57be17));
                	btn_down.setText(mContext.getString(R.string.resume));
                    break;
                case SUCCESS:
                	((DownLoadTaskActivity)mContext).finishedTask(taskInfo);
                    break;
                case FAILURE:
                	btn_down.setBackgroundResource(R.drawable.btn_round_border_fe9e8a);
                	btn_down.setTextColor(mContext.getResources().getColor(R.color.color_fe9e8a));
                	btn_down.setText(mContext.getString(R.string.retry));
                    break;
                default:
                    break;
            }
        }
    }

	
}
