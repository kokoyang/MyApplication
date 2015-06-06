/**    
 * @FileName: UninstallAPPActivity.java  
 * @Package:com.zxly.market.activity  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-9 上午9:44:23  
 * @version V1.0    
 */
package com.zxly.market.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.zxly.market.R;
import com.zxly.market.adapter.ListUninstallAPPAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.model.AppManagerModel;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.PrefsUtil;

/** 
 * @ClassName: UninstallAPPActivity  
 * @Description:  应用卸载列表
 * @author: fengruyi 
 * @date:2015-4-9 上午9:44:23   
 */
public class UninstallAPPActivity extends BaseActivity{
	private final String TAG = UninstallAPPActivity.class.getSimpleName();
	private ListView mListView;
	private List<ApkInfo> mApplist;
	private ListUninstallAPPAdapter mAdapter;
	private AppBroadcastReceiver mAppBroadcastReceiver; 
	@Override
	public int getContentViewId() {
		return R.layout.activity_uninstall;
	}

	
	@Override
	public void initViewAndData() {
		AppManagerModel model = new AppManagerModel();
		setBackTitle(R.string.app_uninstall);
		mListView = obtainView(R.id.lv_uninstall_apps);
		mApplist = model.getUserApps(this);
		mAdapter= new ListUninstallAPPAdapter(this, mApplist);
		mListView.setAdapter(mAdapter);
		mAppBroadcastReceiver=new AppBroadcastReceiver(); 
        IntentFilter intentFilter=new IntentFilter(); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
        intentFilter.addDataScheme("package"); 
        this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	}
	
	@Override
	public void onBackPressed() {	
		if(mAppBroadcastReceiver!=null){
			unregisterReceiver(mAppBroadcastReceiver);
			mAppBroadcastReceiver = null;
		}
		super.onBackPressed();
	}
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == PackageChangeReceiver.PACKAGE_REMOVE){
				mAdapter.removeItem((ApkInfo) msg.obj);
			}
		};
	};
	
	private class AppBroadcastReceiver extends BroadcastReceiver { 
//	    private final String ADD_APP ="android.intent.action.PACKAGE_ADDED"; 
//	    private final String REMOVE_APP ="android.intent.action.PACKAGE_REMOVED"; 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	        String action=intent.getAction(); 
	        String packageName = intent.getDataString();
	        if ((Intent.ACTION_PACKAGE_REMOVED.equals(action))&&packageName!=null) { 
	        	 ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             mAdapter.removeItem(info);
	        } 
	    } 
	   
	}
}
