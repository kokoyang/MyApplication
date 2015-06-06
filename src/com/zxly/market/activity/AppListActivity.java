package com.zxly.market.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.zxly.market.R;
import com.zxly.market.adapter.CommonListAPPAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.model.IRankingView;
import com.zxly.market.model.RankingListControler;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.LoadmoreListView;
import com.zxly.market.view.LoadmoreListView.OnLoadListener;
/**
 * 单个分类列表
 * @author fengruyi
 *
 */
public class AppListActivity extends BaseActivity implements IRankingView,OnClickListener,MarketReceiver.NetChangeObserver{
	private LoadmoreListView mListView;
	private List<ApkInfo> mList;
	private CommonListAPPAdapter mAdapter;
	RankingListControler controler;
	private String classCode ;
    private CommenLoadingView loadingView;
    private AppBroadcastReceiver mAppBroadcastReceiver; 
	public int getContentViewId() {
		return R.layout.activity_list_app;
	}
	public void initViewAndData() {
		
		controler = new RankingListControler(this);
		Intent intent = getIntent();
		if(intent==null){
			Toast.makeText(this, "出现未知错误", Toast.LENGTH_SHORT).show();
			return;
		}
		int title = intent.getIntExtra("title",0);
		classCode = intent.getStringExtra("classcode");
		setBackTitle(title);
		loadingView = obtainView(R.id.loading_view);
		mListView = obtainView(R.id.lv_app);
		loadingView.showLoadingView();
		controler.loadApp(classCode, false);
		MarketReceiver.registerObserver(this);
		mAppBroadcastReceiver=new AppBroadcastReceiver(); 
        IntentFilter intentFilter=new IntentFilter(); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
        intentFilter.addDataScheme("package"); 
        this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	}
	protected void onResume() {
		super.onResume();
		if(mAdapter!=null){
			mAdapter.notifyDataSetChanged();
		}
	}
	public void onBackPressed() {
		super.onBackPressed();
		controler.setFinish(true);
		if(mAppBroadcastReceiver!=null){
			unregisterReceiver(mAppBroadcastReceiver);
			mAppBroadcastReceiver = null;
		}
	}

	public void showApp(List<ApkInfo> object) {
		mList = object;
		if(mList.size()==0){
			showEmptyView();
		}else{
			loadingView.hide();
		}
	    mAdapter = new CommonListAPPAdapter(this, mList);
		if(!controler.isLastPage()){
			mListView.addFootView(this);
			mListView.setOnLoadListener(new OnLoadListener() {
				
				public void onRetry() {
					controler.loadApp(classCode, true);
				}
				
				public void onLoad() {
					controler.loadApp(classCode,true);
				}
			});
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2<mList.size()){
					 Intent intent = new Intent(AppListActivity.this,AppDetailActivity.class);
					 intent.putExtra(Constant.APK_DETAIL, mList.get(arg2).getDetailUrl());
					 startActivity(intent);
				}
			}
			
		});
	}

	public void showMoreApp(List<ApkInfo> object) {
		mList.addAll(object);
		mAdapter.notifyDataSetChanged();
		if(controler.isLastPage()){
			mListView.loadFull();
		}else{
			mListView.onLoadComplete();
		}
	}

	public void showEmptyView() {
		loadingView.showEmptyDataView();
		
	}

	public void showRequestErro() {
		showEmptyView();
		
	}

	public void showNoNetwork() {
		loadingView.showNoNetView();
		loadingView.reloading(this);
		
	}

	public void onClick(View arg0) {
		if(arg0.getId() == R.id.tv_reload){
			loadingView.showLoadingView();
			controler.loadApp(classCode, false);
		}
	}

	public void netWorkConnect() {
		if(!isFinishing()&&mList==null){
			loadingView.reload();
		}
	}

	public void loadMoreFail() {
		mListView.loadFail();
	}
	
	private class AppBroadcastReceiver extends BroadcastReceiver { 
//	    private final String ADD_APP ="android.intent.action.PACKAGE_ADDED"; 
//	    private final String REMOVE_APP ="android.intent.action.PACKAGE_REMOVED"; 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	        String action=intent.getAction(); 
	        String packageName = intent.getDataString();
	        if (Intent.ACTION_PACKAGE_ADDED.equals(action)&&packageName!=null) { 
	        	 ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             if(mAdapter!=null&&mList!=null){
						if(mList.contains(info)){
							mAdapter.notifyDataSetChanged();
						}
					}
	             
	        } 
	        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)&&packageName!=null) { 
	        	 ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             if(mAdapter!=null&&mList!=null){
						if(mList.contains(info)){
							mAdapter.notifyDataSetChanged();
						}
					}
	            
	        } 
	    } 
	   
	}
}
