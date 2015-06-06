package com.zxly.market.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.zxly.market.R;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.adapter.CommonListAPPAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.model.IRankingView;
import com.zxly.market.model.RankingListControler;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.LoadmoreListView;
import com.zxly.market.view.LoadmoreListView.OnLoadListener;


public class RankingFragment extends BaseFragment implements IRankingView,OnClickListener,MarketReceiver.NetChangeObserver{
	private LoadmoreListView mListView;
	private List<ApkInfo> mList;
	private CommonListAPPAdapter mAdapter;
	RankingListControler controler;
	private String classCode;
	private CommenLoadingView loadingView;
	protected boolean isVisible;//是否可见
	private boolean isPrepared;//UI是否实例化
	public RankingFragment(String classcode){
		controler = new RankingListControler(this);
		this.classCode = classcode;
	}
	
	public void handleInfoMessage(Message msg) {
		
		
	}
	public void reFresh(ApkInfo info){
		if(mAdapter!=null&&mList!=null){
			if(mList.contains(info)){
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } 
    }
	public  void onVisible(){
		 if(!isPrepared || !isVisible) {
	            return;
	        }
		 loadingView.showLoadingView();
		 controler.loadApp(classCode, false);
	 }
	public int getContentViewId() {	
		return R.layout.fragment_ranking;
	}

	public void initViewAndData() {
		isPrepared = true;
		loadingView = obtainView(R.id.loading_view);
		mListView = obtainView(R.id.lv_app);
		onVisible();
		MarketReceiver.registerObserver(this);
	}
	public void onResume() {
		super.onResume();
		if(mAdapter!=null){
			mAdapter.notifyDataSetChanged();
		}
	}
	public void onDestroy() {
		super.onDestroy();
		controler.setFinish(true);
	}

	public void showEmptyView() {
		loadingView.showEmptyDataView();
		
	}
	public void showApp(List<ApkInfo> object) {
		mList = object;
		if(mList.size()==0){
			showEmptyView();
		}else{
			loadingView.hide();
		}
	    mAdapter = new CommonListAPPAdapter(getActivity(), mList);	
		if(!controler.isLastPage()){
			mListView.addFootView(getActivity());
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
					 Intent intent = new Intent(getActivity(),AppDetailActivity.class);
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
	public void showNoNetwork() {
		loadingView.showNoNetView();
		loadingView.reloading(this);
	}

	public void onClick(View arg0) {
		if(arg0.getId() == R.id.tv_reload){
			 onVisible();
		}
	}

	public void showRequestErro() {
		Toast.makeText(getActivity(), "请求失败", 0).show();
		showEmptyView();
	}

	public void netWorkConnect() {
		if(getActivity()==null|| getActivity().isFinishing())return;
		if(mAdapter==null){
			 loadingView.reload();
		}
	}

	public void loadMoreFail() {	
		mListView.loadFail();
	}

}
