package com.zxly.market.activity;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.zxly.market.R;
import com.zxly.market.adapter.ZXFragmentPagerAdapter;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.fragment.RankingFragment;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.TabSwitchPagerView;
import com.zxly.market.view.TabSwitchPagerView.SwitchChangeListener;
/**
 * 排行榜页面
 * @author Administrator
 *
 */
public class RakingListActivity extends BaseFragmentActivity{
	private TabSwitchPagerView mTabPager;
	private RankingFragment appFragment ;
	private RankingFragment gameFragment;
    private AppBroadcastReceiver mAppBroadcastReceiver; 
	public int getContentViewId() {
		
		return R.layout.activity_download_task;
	}
	public void initViewAndData() {
		setBackTitle(R.string.rank_lable);	
		mTabPager = obtainView(R.id.tab_pager);
		mTabPager.setTabTitle(R.string.app_, R.string.game_lable);
		appFragment = new RankingFragment("Ying_yon");
		gameFragment = new RankingFragment("games");
		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(appFragment);
		fragmentList.add(gameFragment);
		ZXFragmentPagerAdapter adapter = new ZXFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		mTabPager.setPagerAdapter(adapter);
		mAppBroadcastReceiver=new AppBroadcastReceiver(); 
        IntentFilter intentFilter=new IntentFilter(); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
        intentFilter.addDataScheme("package"); 
        this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	
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
	        String packageName = intent.getDataString();
	        if ((Intent.ACTION_PACKAGE_ADDED.equals(action)||Intent.ACTION_PACKAGE_REMOVED.equals(action))&&packageName!=null) { 
	        	 ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             appFragment.reFresh(info);
				 gameFragment.reFresh(info);
	        } 
	    } 
	   
	}
}
