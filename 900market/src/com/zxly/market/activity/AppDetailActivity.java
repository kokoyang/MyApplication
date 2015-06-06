package com.zxly.market.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.zxly.market.R;
import com.zxly.market.adapter.ZXFragmentPagerAdapter;
import com.zxly.market.bean.DownloadRequestCallBack_singleButton;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.AppDetailInfo;
import com.zxly.market.entity.AppDetalData;
import com.zxly.market.entity.CommentData;
import com.zxly.market.entity.CommentInfo;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.fragment.AppCommentFragment;
import com.zxly.market.fragment.AppIntroduceFragment;
import com.zxly.market.model.AppDetailControler;
import com.zxly.market.model.IAppDetailView;
import com.zxly.market.service.DownloadService;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.DownloadManager.ManagerCallBack;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.utils.ViewUtil;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.LimitLengthTextView;
import com.zxly.market.view.PinnerHeadRelativeLayout;
import com.zxly.market.view.PublishCommentDialog;
import com.zxly.market.view.PublishCommentDialog.SubmitCallBack;
import com.zxly.market.view.RoundedImageView;
import com.zxly.market.view.TabSwitchPagerView;
import com.zxly.market.view.TabSwitchPagerView.SwitchChangeListener;
/**
 * 应用详情
 * @author fengruyi
 *
 */
public class AppDetailActivity extends BaseFragmentActivity implements IAppDetailView,OnClickListener{
	private TabSwitchPagerView mSwitchPager;
	private PinnerHeadRelativeLayout mContentView;
	private RelativeLayout bottomView;//底部按键区域
	/**分享按钮*/
	//private ImageButton mBtnShare;
	/**搜索按钮*/
	private ImageButton mBtnSearch;
	/**应用图标*/
	private RoundedImageView mIvIcon;
	/**名称*/
	private LimitLengthTextView mTvAppName;
	/**评分*/
	private RatingBar mRating;
	/**下载量 版本号*/
	private TextView mTvinfo;
	/**评论fragment*/
	AppCommentFragment commentFragment ;
	/**介绍fragment*/
	AppIntroduceFragment introduceFragment ;
	UMSocialService mController = UMServiceFactory
	            .getUMSocialService("com.umeng.share");
//	private ShareDialog shareDialog; 
	private PublishCommentDialog commentDialog;
	/**属于我的评论*/
	private CommentInfo myComment;
	/**评论按钮*/
	private Button mbtnComment;
	/**下载按钮*/
	private Button mBtnDownload;
	/**详情地址*/
	private String detailUrl ;
	public AppDetailInfo mAppdetailInfo;
	private DownloadManager downloadmanager;
	private DownLoadTaskInfo taskInfo;
	public AppDetailControler controler;
	private CommenLoadingView loadingView;
	public int scollheight;//计算可滑动的距离
    private AppBroadcastReceiver mAppBroadcastReceiver; 
	@Override
	public int getContentViewId() {
		return R.layout.activity_app_detail;
	}

	@Override
	public void initViewAndData() {
		controler = new AppDetailControler(this);
		setBackTitle(R.string.app_detail);
		Intent intent = getIntent();
		if(intent==null){
			return ;
		}
	    detailUrl = intent.getStringExtra(Constant.APK_DETAIL);
		loadingView = obtainView(R.id.loading_view);
		bottomView = obtainView(R.id.rlt_bottom);
		mContentView = obtainView(R.id.rlt_content);
		mIvIcon = obtainView(R.id.iv_app_icon);
		mTvAppName = obtainView(R.id.tv_app_name);
		mRating = obtainView(R.id.rb_rank);
		//mBtnShare = obtainView(R.id.ibtn_share);
		mBtnSearch = obtainView(R.id.ibtn_search);
		mTvinfo = obtainView(R.id.tv_app_info);
	    mSwitchPager = obtainView(R.id.switch_pager);
	    mSwitchPager.setTabTitle(R.string.tab_introduce, R.string.tab_comment);
	    mbtnComment = obtainView(R.id.btn_comment);
	    mBtnDownload = obtainView(R.id.btn_down);
	    mRating.setEnabled(false);
	    ViewUtil.setOnClickListener(this,mbtnComment,mBtnDownload,mBtnSearch);
	    mSwitchPager.setListner(new SwitchChangeListener() {//评论按钮和下载按钮切换
			
			public void onPageSelected(int index) {
				switch (index) {
				  case 0:
					  mBtnDownload.setVisibility(View.VISIBLE);
					  mbtnComment.setVisibility(View.GONE);
					  mContentView.setcurrenFragment(introduceFragment);
					break;
					 
				  case 1:
					  //只有安装过的才可以评论
					  if(mBtnDownload.getTag().equals(R.string.open)||mBtnDownload.getTag().equals(R.string.update)){
						  mBtnDownload.setVisibility(View.GONE);
						  mbtnComment.setVisibility(View.VISIBLE);
					  }
					  mContentView.setcurrenFragment(commentFragment);
					break;
				}
				
			}
		});
	    loadingView.showLoadingView();
		controler.loadAppDetail(detailUrl);
		mAppBroadcastReceiver=new AppBroadcastReceiver(); 
        IntentFilter intentFilter=new IntentFilter(); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
        intentFilter.addDataScheme("package"); 
        this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	}
	protected void onResume() {
		super.onResume();
		AppUtil.hideSoftInput(this);
		if(mAppdetailInfo!=null){
			refreshDownloadButton(mAppdetailInfo.getPackName());
			introduceFragment.refreshView();
		}
	};
//	@Override 
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    super.onActivityResult(requestCode, resultCode, data);
//	    /**使用SSO授权必须添加如下代码 */
//	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
//	    if(ssoHandler != null){
//	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//	    }
//	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.ibtn_share:
//			if(shareDialog==null&&mAppdetailInfo!=null){
//				shareDialog = new ShareDialog(this, mAppdetailInfo);
//			}
//			shareDialog.show();
//			break;
		case R.id.ibtn_search:
			Intent intent = new Intent(this,HotSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_down:
			switch ((Integer)v.getTag()) {
			case R.string.waiting:
			case R.string.stop://暂停任务
				try {
					downloadmanager.stopDownload(taskInfo);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//BaseApplication.getInstance().setNeadToFresh(true);
				break;
			case R.string.open:
				AppUtil.startApk(mAppdetailInfo);
				break;
			case R.string.install:
				AppUtil.installApk(this, taskInfo);
				break;
			case R.string.resume:
			case R.string.retry:
				try {
					downloadmanager.resumeDownload(taskInfo, new DownloadRequestCallBack_singleButton());
					taskInfo.getHandler().getRequestCallBack().setUserTag(mBtnDownload);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//BaseApplication.getInstance().setNeadToFresh(true);
				break;
			case R.string.update:
			case R.string.download:
				try {
					
					downloadmanager.addNewDownload(mAppdetailInfo.getDownUrl(),mAppdetailInfo.getAppName(),mAppdetailInfo.getPackName(),mAppdetailInfo.getIcon(),mAppdetailInfo.getVerName(),mAppdetailInfo.getClassCode(),mAppdetailInfo.getSource(),mAppdetailInfo.getByteSize(),new DownloadRequestCallBack_singleButton());
					taskInfo = downloadmanager.getTask(mAppdetailInfo.getPackName());
					if(taskInfo!=null){
						taskInfo.getHandler().getRequestCallBack().setUserTag(mBtnDownload);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	BaseApplication.getInstance().setNeadToFresh(true);
				break;
			default:
				break;
			}
			break;
		case R.id.btn_comment:
			 if(commentDialog==null){
			 commentDialog = new PublishCommentDialog(this);
			 commentDialog.setOnCallback(new SubmitCallBack() {
				public void submit(String name, String content, int grade) {
					controler.submitComment(mAppdetailInfo, name, content, grade);
					PrefsUtil.getInstance().putString(Constant.USER_NAME, name).commit();
					if(myComment==null){
						myComment = new CommentInfo();
						myComment.setUname(name);
						myComment.setContent(content);
						myComment.setRank(grade);
					}else{
						myComment.setUname(name);
						myComment.setContent(content);
						myComment.setRank(grade);
					}
					commentDialog.dismiss();
					
				}
			});
			 commentDialog.setOnCancelListener(new OnCancelListener() {
				
				public void onCancel(DialogInterface arg0) {
					AppUtil.hideSoftInput(AppDetailActivity.this);
				}
			});
		 }
		 if(v.isSelected()){//显示修改评论
			 commentDialog.changeComment(myComment);
		 }else{//评论
			 commentDialog.resetDialog();
		 }
		 commentDialog.show();
			break;
		case R.id.tv_reload:
			 loadingView.showLoadingView();
			 controler.loadAppDetail(detailUrl);
			break;
		default:
			break;
		}
		
	}	
	public void onBackPressed() {
		AppUtil.hideSoftInput(this);
		controler.setFinish(true);
		if(mAppBroadcastReceiver!=null){
			try {
				unregisterReceiver(mAppBroadcastReceiver);
				mAppBroadcastReceiver = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		if(taskInfo!=null&&taskInfo.getHandler()!=null&&taskInfo.getHandler().getRequestCallBack()!=null){
			DownloadManager.ManagerCallBack callback = (ManagerCallBack) taskInfo.getHandler().getRequestCallBack();
			callback.setBaseCallBack(null);
		}
		super.onBackPressed();
	}
	/**
	 * 显示应用详情头部信息
	 * @param detailInfo
	 */
	private void showApKBaseData(AppDetailInfo detailInfo){
		mAppdetailInfo = detailInfo;
		ImageLoaderUtil.Load(detailInfo.getIcon(), mIvIcon);
		mTvAppName.setText(detailInfo.getAppName(),14);
		mRating.setRating(detailInfo.getGrade()/2.0f);
		mTvinfo.setText(detailInfo.getSize()+"MB   "+detailInfo.getDownCount()+"人下载    "+detailInfo.getVerName());
		refreshDownloadButton(detailInfo.getPackName());
	}
	/**
	 * 根据下载任务状态来更新下载按钮
	 * @param packageName
	 */
    public void refreshDownloadButton(String packageName){
    	mBtnDownload.setText(R.string.download);
    	mBtnDownload.setTag(R.string.download);
    	taskInfo = downloadmanager.getTask(packageName);
    	if(taskInfo!=null){
    		 switch (taskInfo.getState()) {
    	        case STARTED:	
    			case WAITING:
    				mBtnDownload.setText(R.string.waiting);
    				mBtnDownload.setTag(R.string.waiting);
    				break;
    			case LOADING:
    				mBtnDownload.setTag(R.string.stop);
    				break;
    			case SUCCESS:
    				if(AppUtil.isAppInstalled(this, taskInfo.getPackageName())){//存在下载任务中并且已经安装
    					mBtnDownload.setText(R.string.open);
    					mBtnDownload.setTag(R.string.open);
                	}else{
	                	File file = new File(taskInfo.getFileSavePath());
	                	if(file.exists()){
	                		mBtnDownload.setText(R.string.install);
	                		mBtnDownload.setTag(R.string.install);
	                	}else{
	                		try {
	                			downloadmanager.removeDownload(taskInfo);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                		mBtnDownload.setText(R.string.download);
	                		mBtnDownload.setTag(R.string.download);
	                	}
                	}
    				break;
    			case FAILURE:
    				mBtnDownload.setText(R.string.retry);
    				mBtnDownload.setTag(R.string.retry);
    				break;
    			case CANCELLED:
    				mBtnDownload.setText(R.string.resume);
    				mBtnDownload.setTag(R.string.resume);
    				break;
    		 	}
    		 HttpHandler<File> handler = taskInfo.getHandler();  
		        if (handler != null) {
		            RequestCallBack callBack = handler.getRequestCallBack();
		            if (callBack instanceof DownloadManager.ManagerCallBack) {
		                DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
		                managerCallBack.setBaseCallBack(new DownloadRequestCallBack_singleButton());  
		            }
		            callBack.setUserTag(mBtnDownload);
		        }
	   }else{
	    	ApkInfo tempInfo = BaseApplication.getInstance().getInstalledApk(mAppdetailInfo.getPackName());
			if(tempInfo!=null){//表示已经安装
				if(tempInfo.getVerName()!=null&&tempInfo.getVerName().compareTo(mAppdetailInfo.getVerName())<0){//有更新的版本
					mBtnDownload.setText(R.string.update);
					mBtnDownload.setTag(R.string.update);
				}else{
					mBtnDownload.setText(R.string.open);
					mBtnDownload.setTag(R.string.open);
				}
			}
	   }
	 if((mBtnDownload.getTag().equals(R.string.open)||mBtnDownload.getTag().equals(R.string.update))&&mSwitchPager.getCurrenItem()==1){
		  mBtnDownload.setVisibility(View.GONE);
		  mbtnComment.setVisibility(View.VISIBLE);
	  }
    }
	public void showCommentData(CommentData data) {
		myComment = data.getCmInfo();
		commentFragment.showCommentData(data.getApkList());
		if(myComment!=null){
			mbtnComment.setText(R.string.btn_change_comment);
			mbtnComment.setSelected(true);
		}else{
			mbtnComment.setSelected(false);
			mbtnComment.setText(R.string.btn_comment);
		}
	}


	@SuppressLint("ShowToast")
	public void saveCommentFailue() {
		Toast.makeText(AppDetailActivity.this, "提交评论失败", Toast.LENGTH_SHORT).show();
		
	}

	public void svaeCommentSuccess() {
		Toast.makeText(AppDetailActivity.this, "提交评论成功", Toast.LENGTH_SHORT).show();
		controler.loadCommentData(mAppdetailInfo.getPackName(), false);
		
	}

	public void showDetailData(AppDetalData data) {
		showContentView();
		showApKBaseData(data.getDetail());
		introduceFragment.showApkDetail(data);
		controler.loadCommentData(mAppdetailInfo.getPackName(), false);
	}
	/**
	 * 有数据了把ui显示出来，并添加介绍和评论fragment添加进来
	 */
	private void showContentView(){
		loadingView.hide();
		downloadmanager = DownloadService.getDownloadManager(BaseApplication.getInstance());
		introduceFragment = new AppIntroduceFragment();
		commentFragment = new AppCommentFragment();
		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(introduceFragment);
		fragmentList.add(commentFragment);
		ZXFragmentPagerAdapter adapter = new ZXFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		mSwitchPager.setPagerAdapter(adapter);
		mContentView.setFragments(commentFragment, introduceFragment);
		mContentView.setcurrenFragment(introduceFragment);
		View rootview = findViewById(R.id.root_layout);
		scollheight = mContentView.getmHeadHeight();
		ViewUtil.setViewHeight(rootview,mContentView.rootviewheight+scollheight+getResources().getDimensionPixelSize(R.dimen.title_bar_height));
		RelativeLayout.LayoutParams lp  = (LayoutParams) bottomView.getLayoutParams();
		lp.bottomMargin = scollheight;	
	}
	public void showEmptyView() {
		loadingView.showEmptyDataView();
		
	}

	public void showRequestErro() {
		Toast.makeText(AppDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
		showEmptyView();
	}

	public void showNoNetwork() {
		loadingView.showNoNetView();
		loadingView.reloading(this);
		
	}

	public void showMoreCommentData(CommentData data) {
		commentFragment.showMoreCommentData(data);
	}
	
	private class AppBroadcastReceiver extends BroadcastReceiver { 
//	    private final String ADD_APP ="android.intent.action.PACKAGE_ADDED"; 
//	    private final String REMOVE_APP ="android.intent.action.PACKAGE_REMOVED"; 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	        String action=intent.getAction(); 
	        String packageName = intent.getDataString();
	        if (Intent.ACTION_PACKAGE_ADDED.equals(action)&&packageName!=null) { 
	        	 Logger.e("", "ACTION_PACKAGE_ADDED");
	             if(packageName.replace("package:", "").equals(mAppdetailInfo.getPackName())){
	            	 Logger.e("", "ACTION_PACKAGE_ADDED2222");
						refreshDownloadButton(mAppdetailInfo.getPackName());
						introduceFragment.refreshView();
					}
	             
	        } 
	        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)&&packageName!=null) { 
	        	 ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             if(packageName.replace("package:", "").equals(mAppdetailInfo.getPackName())){
						refreshDownloadButton(mAppdetailInfo.getPackName());
						introduceFragment.refreshView();
			      }
	            
	        } 
	    } 
	   
	}
}
