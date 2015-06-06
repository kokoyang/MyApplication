/**    
 * @FileName: UpgradeAppActivity.java  
 * @Package:com.zxly.market.activity  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-13 下午1:29:21  
 * @version V1.0    
 */
package com.zxly.market.activity;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.zxly.market.R;
import com.zxly.market.adapter.ListAppUpgradeAdapter;
import com.zxly.market.adapter.ListAppUpgradeAdapter.UpgradeHolderView;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.model.ApkUpgradeControler;
import com.zxly.market.model.IUpgradeView;
import com.zxly.market.service.DownloadService;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.utils.CustomToast;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.utils.ViewUtil;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.PromptDialog;


/** 
 * @ClassName: UpgradeAppActivity  
 * @Description: 应用升级界面 
 * @author: fengruyi 
 * @date:2015-4-13 下午1:29:21   
 */
public class UpgradeAppActivity extends BaseActivity implements IUpgradeView,OnClickListener,MarketReceiver.NetChangeObserver{
	
	private ListView mListView;
	private TextView mTvIgnore;
	private View bottomview;
	private Button mBtnUpgrade;
    private ApkUpgradeControler controler;
    private ListAppUpgradeAdapter adapter;
    private DownloadManager manager;
    private List<ApkInfo> mList;
    private PromptDialog promptDialog;
    private DbUtils db;
    private String json;
    private CommenLoadingView loadingView;
    private AppBroadcastReceiver mAppBroadcastReceiver; 
	@Override
	public int getContentViewId() {
		return R.layout.activity_upgrade;	
	}
    
	@Override
	public void initViewAndData() {
		manager = DownloadService.getDownloadManager(BaseApplication.getInstance());
		controler = new ApkUpgradeControler(this);
		setBackTitle(R.string.app_update);
		mListView  = obtainView(R.id.lv_upgrade_app);
		bottomview = obtainView(R.id.rlt_bottom);
		mTvIgnore = obtainView(R.id.tv_ignore_app);
		mBtnUpgrade = obtainView(R.id.btn_upgrade);
		loadingView = obtainView(R.id.loading_view);
		ViewUtil.setOnClickListener(this, mTvIgnore,mBtnUpgrade);
		mList = BaseApplication.getInstance().getNeedUpgradeAppList();
		if(mList!=null){
			showUpgradeList(mList);
			loadingView.hide();
		}else{
			loadingView.showLoadingView();
			controler.loadUpgradeData();
		}
		mAppBroadcastReceiver=new AppBroadcastReceiver(); 
        IntentFilter intentFilter=new IntentFilter(); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
        intentFilter.addDataScheme("package"); 
        this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	}
    public void onBackPressed() {
    	super.onBackPressed();
    	controler.setFinish(true);
    	if(mAppBroadcastReceiver!=null){
			unregisterReceiver(mAppBroadcastReceiver);
			mAppBroadcastReceiver = null;
		}
    }
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ignore_app:
			Intent intent = new Intent(this,IgnoredAppActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_upgrade://一键升级
			upgradeAll();
			break;
		case R.id.tv_reload://重新加载
			loadingView.showLoadingView();
			controler.loadUpgradeData();
			break;
		default:
			break;
		}
		
	}
	
	protected void onResume() {
		super.onResume();
		reflashView();
	}
	
	public void reflashView(){
		if(adapter!=null){
			adapter.notifyDataSetChanged();
			if(mList.size()==0){
				showEmptyView();
			}else{
				showBottomButton(true);
			}
		}
	}
	
	public void showEmptyView() {
		showBottomButton(false);
	}

	public void showRequestErro() {
		showEmptyView();
		
	}

	public void showNoNetwork() {
		
		loadingView.showNoNetView();
		loadingView.reloading(this);
	}
	

	public void showUpgradeList(List<ApkInfo> list) {
		if(db==null){
			db = DbUtils.create(this);
		}
		mList = list;
		if (mList.size()==0) {
			showEmptyView();
		}else{
			showBottomButton(true);
		}
		adapter = new ListAppUpgradeAdapter(this, mList, manager,db);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 ApkInfo info = mList.get(arg2);
				 Intent intent = new Intent(UpgradeAppActivity.this,AppDetailActivity.class);
				 intent.putExtra(Constant.APK_DETAIL, info.getDetailUrl());
				 startActivity(intent);
				
			}
		});
	}
	
	/**
	 * 是否显示底部一键升级按键，true显示，false隐藏
	 * @param flag
	 */
	public void showBottomButton(boolean flag){
		if(flag){
			bottomview.setVisibility(View.VISIBLE);
			loadingView.hide();
		}else{
			bottomview.setVisibility(View.GONE);
			loadingView.showEmptyDataView();
		}
	}
	/**
	 * 全部更新
	 */
	public void upgradeAll(){
		if(promptDialog==null){
			promptDialog = new PromptDialog(this);
			promptDialog.setTxt(getString(R.string.warm_tip), getString(R.string.upgrade_all));
			promptDialog.show(new OnClickListener() {
				public void onClick(View arg0) {
					switch (arg0.getId()) {
					case R.id.btn_ok:
						
						for(int i = 0,length = mList.size();i<length;i++){
							try {
								manager.addNewDownload(mList.get(i), null);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						adapter.notifyDataSetChanged();
						break;
					}
					promptDialog.dismiss();
					
				}
			});
		}else{
			promptDialog.show();
		}
		
	}

	public void netWorkConnect() {
		if(!isFinishing()&&mList==null){
			loadingView.reload();
		}
		
	}
	
	private class AppBroadcastReceiver extends BroadcastReceiver { 
//	    private final String ADD_APP ="android.intent.action.PACKAGE_ADDED"; 
//	    private final String REMOVE_APP ="android.intent.action.PACKAGE_REMOVED"; 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	        String action=intent.getAction(); 
	        String packageName = intent.getDataString();
	        if ((Intent.ACTION_PACKAGE_ADDED.equals(action))&&packageName!=null) { 
	        	 ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             if(mList!=null){
						mList.remove(info);
						PrefsUtil.getInstance().putString(Constant.UPGRADE_LIST, GjsonUtil.Object2Json(mList)).commit();
			     }
	        } 
	    } 
	   
	}
}
