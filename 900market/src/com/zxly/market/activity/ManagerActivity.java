package com.zxly.market.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.service.DownloadService;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.ViewUtil;

/**
 * 应用管理页面
 * 
 * @author fengruyi
 *
 */
public class ManagerActivity extends BaseActivity implements OnClickListener{
	
	private View mDownloadTask;
	private View mAppUpdate;
	private View mAppUninstall;
	private TextView mDownloadDot;//下载任务的红色提醒圆点
	private TextView mDownloadBigDot;//带数字的大红点
	private TextView mUpdateDot;//应用更新的红色提醒圆点
	private TextView mUpdateBigDot;//带数字的更新大红点
	DownloadManager downloadManager;
	@Override
	public int getContentViewId() {
		return R.layout.activity_manager;
	}

	@Override
	public void initViewAndData() {
		setBackTitle(R.string.manager);
		downloadManager = DownloadService.getDownloadManager(BaseApplication.getInstance());
		mDownloadTask = obtainView(R.id.rlt_download);
		mAppUpdate = obtainView(R.id.rlt_app_update);
		mAppUninstall = obtainView(R.id.rlt_app_uninstall);
		mDownloadDot = obtainView(R.id.download_dot);
		mUpdateDot = obtainView(R.id.update_dot);
		mDownloadBigDot = obtainView(R.id.download_big_dot);
		mUpdateBigDot = obtainView(R.id.update_big_dot);
		ViewUtil.setOnClickListener(this, mDownloadTask,mAppUninstall,mAppUpdate);
		ViewUtil.setViewSize(mDownloadDot, (int)(BaseApplication.mWidthPixels*0.022f));
		ViewUtil.setViewSize(mUpdateDot, (int)(BaseApplication.mWidthPixels*0.022f));
		ViewUtil.setViewSize(mDownloadBigDot, (int)(BaseApplication.mWidthPixels*0.055f));
		ViewUtil.setViewSize(mUpdateBigDot, (int)(BaseApplication.mWidthPixels*0.055f));
	}
    
	protected void onResume() {
		int count = downloadManager.getDoingTaskCount();
		if(count==0){
			mDownloadDot.setVisibility(View.GONE);
			mDownloadBigDot.setVisibility(View.GONE);
		}else if(count>=10){
			mDownloadDot.setVisibility(View.VISIBLE);
			mDownloadBigDot.setVisibility(View.GONE);
		}else{
			mDownloadBigDot.setVisibility(View.VISIBLE);
			mDownloadDot.setVisibility(View.GONE);
			mDownloadBigDot.setText(count+"");
		}
		if(BaseApplication.getInstance().getNeedUpgradeAppList()!=null){
			int size = BaseApplication.getInstance().getNeedUpgradeAppList().size();
			if(size==0){
				mUpdateDot.setVisibility(View.GONE);
				mUpdateBigDot.setVisibility(View.GONE);
			}else if(size <=10){
				mUpdateBigDot.setVisibility(View.VISIBLE);
				mUpdateDot.setVisibility(View.GONE);
				mUpdateBigDot.setText(size+"");
			}else{
				mUpdateBigDot.setVisibility(View.GONE);
				mUpdateDot.setVisibility(View.VISIBLE);
			}
		}else{
			mUpdateDot.setVisibility(View.GONE);
			mUpdateBigDot.setVisibility(View.GONE);
		}
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlt_download:
			Intent downloadIntent = new Intent(this,DownLoadTaskActivity.class);
			startActivity(downloadIntent);
			break;
		case R.id.rlt_app_uninstall:
			Intent uninstallIntent = new Intent(this,UninstallAPPActivity.class);
			startActivity(uninstallIntent);
			break;
		case R.id.rlt_app_update:
			Intent upgradeIntent = new Intent(this,UpgradeAppActivity.class);
			startActivity(upgradeIntent);
			break;
		default:
			break;
		}
		
	}
	
	
}
