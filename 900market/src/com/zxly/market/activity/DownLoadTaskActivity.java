package com.zxly.market.activity;

import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;

import com.zxly.market.R;
import com.zxly.market.adapter.ZXFragmentPagerAdapter;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.fragment.TaskDoingFragment;
import com.zxly.market.fragment.TaskDoneFragment;
import com.zxly.market.view.TabSwitchPagerView;

/**
 * 下载任务页面
 * @author fengruyi
 *
 */
public class DownLoadTaskActivity extends BaseFragmentActivity{
	private TabSwitchPagerView mTabPager;
	private TaskDoingFragment taskDoingFragment;
	private TaskDoneFragment taskDoneFragment;
	private AppBroadcastReceiver mAppBroadcastReceiver; 
	@Override
	public int getContentViewId() {
		return R.layout.activity_download_task;
	}

	@Override
	public void initViewAndData() {
		setBackTitle(R.string.download_task);	
		mTabPager = obtainView(R.id.tab_pager);
		mTabPager.setTabTitle(R.string.tab_downloading, R.string.tab_download_done);
		showDownloadData();
		mAppBroadcastReceiver=new AppBroadcastReceiver(); 
        IntentFilter intentFilter=new IntentFilter(); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
        intentFilter.addDataScheme("package"); 
        this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	}

	private void showDownloadData(){
	    
		taskDoneFragment = new TaskDoneFragment();
		taskDoingFragment = new TaskDoingFragment();
		ArrayList<Fragment> list = new ArrayList<Fragment>();
		list.add(taskDoingFragment);
		list.add(taskDoneFragment);
		ZXFragmentPagerAdapter fragmentAdapter = new ZXFragmentPagerAdapter(getSupportFragmentManager(), list);
	    mTabPager.setPagerAdapter(fragmentAdapter);
	    
	}

	/**
	 * 完成一个下载任务时，从未完成页面中迁移到已完成中
	 * @param taskInfo
	 */
	public void finishedTask(DownLoadTaskInfo taskInfo){
		taskDoingFragment.removeTask(taskInfo);
		taskDoneFragment.addTask(taskInfo);
	}
	public void onBackPressed() {
		super.onBackPressed();
		if(mAppBroadcastReceiver!=null){
			unregisterReceiver(mAppBroadcastReceiver);
			mAppBroadcastReceiver = null;
		}
	}
	
	private class AppBroadcastReceiver extends BroadcastReceiver { 
//	    private final String ADD_APP ="android.intent.action.PACKAGE_ADDED"; 
//	    private final String REMOVE_APP ="android.intent.action.PACKAGE_REMOVED"; 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	        String action=intent.getAction(); 
	        if (Intent.ACTION_PACKAGE_ADDED.equals(action)) { 
	        	taskDoneFragment.reFresh();
	             
	        } 
	        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) { 
	        	taskDoneFragment.reFresh();
	            
	        } 
	    } 
	   
	}
}
