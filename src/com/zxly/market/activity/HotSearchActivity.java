package com.zxly.market.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.zxly.market.R;
import com.zxly.market.adapter.CommonListAPPAdapter;
import com.zxly.market.adapter.KeysAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.HotKeyInfo;
import com.zxly.market.model.HotKeyControler;
import com.zxly.market.model.IHotKeyView;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.utils.ViewUtil;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.LoadmoreListView;
import com.zxly.market.view.LoadmoreListView.OnLoadListener;

/**
 * 热门搜索界面
 * @author fengruyi
 *
 */
public class HotSearchActivity extends BaseActivity implements IHotKeyView,OnClickListener,MarketReceiver.NetChangeObserver{
	private EditText mEtKey;
	private ImageView mIvSearch;
	private GridView mGridview;
	private LoadmoreListView mlistview;
	private View mHotKeyViews;
	private View mFreshView;
	private View mFreshLable;
	private View mEmptyView;
	HotKeyControler mControler ;
	private List<HotKeyInfo> mKeysList = new ArrayList<HotKeyInfo>();
	private CommonListAPPAdapter mAppAdapter;
	private  KeysAdapter mKeyadapter;
	private List<ApkInfo> mAppList ;
	private String mHotkey;
	private CommenLoadingView loadingView;
	private AppBroadcastReceiver mAppBroadcastReceiver; 
	public int getContentViewId() {
		
		return R.layout.activity_search;
	}
	public void initViewAndData() {
		
		mControler = new HotKeyControler(this);
		setBackTitle("");
		loadingView = obtainView(R.id.loading_view);
		mEtKey = obtainView(R.id.et_key);
		mEmptyView = obtainView(R.id.emptyview);
		mIvSearch = obtainView(R.id.btn_search);
		mGridview = obtainView(R.id.gv_keys);
		mlistview  = obtainView(R.id.lv_app);
		mHotKeyViews = obtainView(R.id.hot_search_key);
		mFreshView = obtainView(R.id.iv_refresh);
		mFreshLable = obtainView(R.id.tv_lable_refresh);
		ViewUtil.setOnClickListener(this,mFreshLable,mFreshView,mIvSearch);
		mHotkey = PrefsUtil.getInstance().getString(Constant.HOT_KEY);
	    if(!TextUtils.isEmpty(mHotkey)){
	    	  mEtKey.setHint(getString(R.string.hot_search)+mHotkey);
	    	  mEtKey.setSelection(mEtKey.length());
	    }else{
	    	mEtKey.setHint("");
	    	mEtKey.setText("");
	    }
		loadData();
		MarketReceiver.registerObserver(this);
		mAppBroadcastReceiver=new AppBroadcastReceiver(); 
        IntentFilter intentFilter=new IntentFilter(); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
        intentFilter.addDataScheme("package"); 
        this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	}
    
	private void loadData(){
		loadingView.showLoadingView();
	    mControler.loadHotKeyData();
	}
	

	public void showEmptyView() {
		loadingView.hide();
		mlistview.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.VISIBLE);
	}

	public void showHotKeysData(List<HotKeyInfo> obj) {
		if(obj.size()==0){
			loadingView.showEmptyDataView();
			return;
		}else{
			loadingView.hide();
		}
		 mKeysList.addAll(obj);
		 mKeyadapter = new KeysAdapter(this, obj);
		 mGridview.setAdapter(mKeyadapter);
		 mGridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
				   HotKeyInfo info = mKeyadapter.getByPosition(arg2);
				   mEtKey.setText(info.getKw());
				   mEtKey.setSelection(mEtKey.length());
				   loadingView.showLoadingView();
				   mEmptyView.setVisibility(View.GONE);
				   mControler.loadAppsByKeys(info.getKw(),false);
				}
				
			});
	}
    
	
	public void showSearchResult(List<ApkInfo> obj) {
		loadingView.hide();
		mEmptyView.setVisibility(View.GONE);
		mHotKeyViews.setVisibility(View.GONE);
		mlistview.setVisibility(View.VISIBLE);
		mAppList = obj;
		mAppAdapter = new CommonListAPPAdapter(this, mAppList);	
		if(!mControler.isResultLastPage()){
			mlistview.addFootView(this);
			mlistview.setOnLoadListener(loadListener);
		}else{
			mlistview.loadFull();
		}
		mlistview.setAdapter(mAppAdapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2<mAppList.size()){
					 Intent intent = new Intent(HotSearchActivity.this,AppDetailActivity.class);
					 intent.putExtra(Constant.APK_DETAIL, mAppList.get(arg2).getDetailUrl());
					 startActivity(intent);
				}
			}
		});
	}
	OnLoadListener loadListener = new OnLoadListener() {
		
		public void onRetry() {
			mControler.loadAppsByKeys(mEtKey.getText().toString(),true);
			
		}
		
		public void onLoad() {
			mControler.loadAppsByKeys(mEtKey.getText().toString(),true);
			
		}
	};
	public void showMoreResult(List<ApkInfo> obj) {
		mlistview.setVisibility(View.VISIBLE);
		mAppList.addAll(obj);
		mAppAdapter.notifyDataSetChanged();
		if(mControler.isResultLastPage()){
			mlistview.loadFull();
		}else{
			mlistview.onLoadComplete();
		}
		
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			if(TextUtils.isEmpty(mEtKey.getText().toString())){
				mEtKey.setText(mHotkey);
			}
			loadingView.showLoadingView();
			mEmptyView.setVisibility(View.GONE);
			mControler.loadAppsByKeys(mEtKey.getText().toString(),false);
			AppUtil.hideSoftInput(this);
			break;
		case R.id.iv_refresh:
			if(!mControler.isKeyLastPage()){
				mControler.loadHotKeyData();
			}else{
				mKeyadapter.nextPage();
			}
			break;

		case R.id.tv_lable_refresh:
			mFreshView.performClick();
			break;
		case R.id.tv_reload:
			loadData();
			break;
		}
		
	}
	protected void onResume() {
		super.onResume();
		if(mlistview.getVisibility()!=View.GONE&&mAppAdapter!=null){
			mAppAdapter.notifyDataSetChanged();
		}
	}
	public void onBackPressed() {
	    if(mHotKeyViews.getVisibility() == View.GONE){
	    	mHotKeyViews.setVisibility(View.VISIBLE);
	    	mlistview.setVisibility(View.GONE);
	    	return;
	    }
	    if(mAppBroadcastReceiver!=null){
			unregisterReceiver(mAppBroadcastReceiver);
			mAppBroadcastReceiver = null;
		}
	    mControler.setFinish(true);
	    AppUtil.hideSoftInput(this);
		super.onBackPressed();
	}

	public void showRequestErro() {
		loadingView.hide();
		Toast.makeText(this, "请求失败", 0).show();
		
	}

	public void showNoNetwork() {
		loadingView.showNoNetView();
		loadingView.reloading(this);
	}

	public void showMoreHotKeysData(List<HotKeyInfo> obj) {
		mKeyadapter.addList(obj);
	}

	public void netWorkConnect() {
		if(!isFinishing()&&mKeyadapter==null){
			loadingView.reload();
		}
		
	}

	public void loadMoreFail() {
		mlistview.loadFail();
		
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
	             if(mAppAdapter!=null&&mAppList!=null){
						if(mAppList.contains(info)){
							mAppAdapter.notifyDataSetChanged();
						}
					}
	             
	        } 
	        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)&&packageName!=null) { 
	        	 ApkInfo info = new ApkInfo();
	             info.setPackName(packageName.replace("package:", ""));
	             if(mAppAdapter!=null&&mAppList!=null){
						if(mAppList.contains(info)){
							mAppAdapter.notifyDataSetChanged();
						}
					}
	            
	        } 
	    } 
	   
	}
}
