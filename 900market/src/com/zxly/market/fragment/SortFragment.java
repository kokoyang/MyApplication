package com.zxly.market.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefreshview.PullToRefreshBase;
import com.handmark.pulltorefreshview.PullToRefreshListView;
import com.zxly.market.R;
import com.zxly.market.activity.HotSearchActivity;
import com.zxly.market.activity.MainActivity;
import com.zxly.market.adapter.CategoryAPPAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.Category;
import com.zxly.market.model.SortFragmentModel;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.utils.NetworkUtil;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.view.ComLoaingDiaglog;
import com.zxly.market.view.CommenLoadingView;

/**
 * 分类页面
 * @author fengruyi
 *
 */
public class SortFragment extends BaseFragment implements View.OnClickListener,
		MarketReceiver.NetChangeObserver ,PullToRefreshBase.OnRefreshListener{

	private static String TAG = "SortFragment";
	private CategoryAPPAdapter adpater;
	private TextView searchBar;
	private PullToRefreshListView listview;
	private SortFragmentModel mode;
	private CommenLoadingView loadingView;

	@Override
	public int getContentViewId() {
		return R.layout.sort_fragment_layout;
	}

	@Override
	public void initViewAndData() {
		searchBar = obtainView(R.id.search_bar);
		String key = PrefsUtil.getInstance().getString(Constant.HOT_KEY);
		if(!TextUtils.isEmpty(key)){
			searchBar.setText(getString(R.string.hot_search)+key);
		}else{
			searchBar.setText("");
		}
		searchBar.setOnClickListener(this);
		loadingView = obtainView(R.id.loading_view);
		listview = obtainView(R.id.listview_demo);
		adpater = new CategoryAPPAdapter(getActivity(),null);
		listview.setAdapter(adpater);
		//Button setting = obtainView(R.id.btn_net_setting);
		listview.setOnRefreshListener(this);
		//setting.setOnClickListener(this);
		MarketReceiver.registerObserver(this);
		//loadAppData();
	}

	/** 界面切换 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser) {
			if (null != adpater && adpater.getCount()==0) {
				loadAppData();
			}
			if (null != adpater) {
				adpater.notifyDataSetChanged();
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void handleInfoMessage(Message msg) {
		if(getActivity()==null|| getActivity().isFinishing())return;
		switch (msg.what){
			case Constant.MESSAGE_SUCCESS:
				adpater.addList((List<Category>) msg.obj);
				searchBar.setVisibility(View.VISIBLE);
				loadingView.hide();
				break;
			case Constant.MESSAGE_NODATD:
			case Constant.MESSAGE_FAILED:
				if(adpater.getCount()==0){
					loadingView.showNoNetView();
					searchBar.setVisibility(View.GONE);
				}
				break;
		}
		if(listview.isRefreshing()){
			listview.onRefreshComplete();
		}
	}

	private void loadAppData() {
		loadingView.showLoadingView();
		if(mode == null){
			mode = new SortFragmentModel(this);
		}
		mode.loadAppData();
	}

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.search_bar){
			Intent searchIntent = new Intent(getActivity(),HotSearchActivity.class);
			startActivity(searchIntent);
		}else if(view.getId()==R.id.btn_net_setting){
			NetworkUtil.enterNetWorkSetting(getActivity());
		}
	}

	@Override
	public void netWorkConnect() {
		if(getActivity()==null|| getActivity().isFinishing())return;
		if (null != adpater && adpater.getCount()==0 && ((MainActivity)getActivity()).getCurrentPage()==1) {
			loadAppData();
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		loadAppData();
	}
}
