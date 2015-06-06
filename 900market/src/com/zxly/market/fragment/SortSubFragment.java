package com.zxly.market.fragment;

import java.util.List;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.activity.CategorySubActivity;
import com.zxly.market.adapter.CommonListAPPAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.model.SortFragmentModel;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.utils.NetworkUtil;
import com.zxly.market.view.ComLoaingDiaglog;
import com.zxly.market.view.CommenLoadingView;

@SuppressLint("HandlerLeak")
public class SortSubFragment extends BaseFragment implements OnItemClickListener,
		AbsListView.OnScrollListener,MarketReceiver.NetChangeObserver,View.OnClickListener {


	private static final String TAG = "SortSubFragment";
	public static final String CATEGORY_LIST1 = "list1";
	public static final String CLASSID_BOTIQUE = "18ZHUSHOU_JINGPIN";
	public static final String EXTRA_TITLES = "extra_titles";

	private ComLoaingDiaglog loaingDiaglog;
	private ListView mListView;
	private View mFooterView;
	private TextView footerText;
	public int recordCount;
	private int lastVisibleConut;
	private int currentPage = 1;

	private String mPageIndex;
	private String mClassId;
	private CommonListAPPAdapter mAdapter;
	private ProgressBar progressBar;
	private SortFragmentModel mode;
	private CommenLoadingView loadingView;


	public static SortSubFragment newInstance(String currentPage, String classId) {
		SortSubFragment newFragment = new SortSubFragment();
		Bundle bundle = new Bundle();
		bundle.putString("pageIndex", currentPage);
		bundle.putString("classId", classId);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		mPageIndex = args != null ? args.getString("pageIndex") : CATEGORY_LIST1;
		mClassId = args != null ? args.getString("classId") : CLASSID_BOTIQUE;

	}

	@Override
	public int getContentViewId() {
		return R.layout.sort_fragment_sub_layout;
	}

	@Override
	public void initViewAndData() {
		mListView = obtainView(R.id.listview_demo);
		mFooterView = (RelativeLayout) View.inflate(getActivity(), R.layout.loadmore_foot_, null);
		mFooterView.setVisibility(View.GONE);
		footerText = (TextView) mFooterView.findViewById(R.id.message_);
		progressBar = (ProgressBar) mFooterView.findViewById(R.id.progressbar_);
				mListView.addFooterView(mFooterView);
		loadingView = obtainView(R.id.loading_view);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);
		mAdapter = new CommonListAPPAdapter(this.getActivity(),null);
		mListView.setAdapter(mAdapter);
		MarketReceiver.registerObserver(this);

		doLoadData();

	}

	/** 界面切换 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser) {
			if (null != mAdapter && mAdapter.getCount()==0) {
				doLoadData();
			}
			if (null != mAdapter) {
				mAdapter.notifyDataSetChanged();
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	@Override
	public void netWorkConnect() {
		CategorySubActivity activity = (CategorySubActivity) getActivity();
		Logger.d("necc","necc mPageIndex="+mPageIndex);
		if (null!=activity && !activity.isFinishing() && null != mAdapter && mAdapter.getCount()==0 && mPageIndex.equals(activity.getCurrentPage())) {
			doLoadData();
		}
	}


	/** 加载数据 */
	private  void doLoadData() {
		loadingView.showLoadingView();
		if(mode==null){
			mode = new SortFragmentModel(this);
		}
		mode.loadGroupList(mClassId, currentPage);
	}

	@Override
	public void handleInfoMessage(Message msg) {
		if(null == getActivity() || getActivity().isFinishing()) {
			return;
		}
		switch(msg.what) {
		case Constant.MESSAGE_SUCCESS:
			mAdapter.add((List<ApkInfo>)msg.obj);
			recordCount = msg.arg1;
			mFooterView.setVisibility(View.GONE);
			loadingView.hide();
			break;
		case Constant.MESSAGE_FAILED:
		case Constant.MESSAGE_NODATD:
			loadingView.showNoNetView();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ApkInfo info = (ApkInfo)mAdapter.getItem(position);
		toAppDetail(info);
	}

	private void toAppDetail(ApkInfo info) {
		Intent intent = new Intent(getActivity(),AppDetailActivity.class);
		intent.putExtra(Constant.APK_DETAIL, info.getDetailUrl());
		intent.putExtra(Constant.APK_PACKAGE, info.getPackName());
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastVisibleConut = firstVisibleItem + visibleItemCount - mListView.getHeaderViewsCount();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int adapterSize = mAdapter.getCount();
		if(adapterSize==0)return;
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleConut >= adapterSize
				&& mFooterView.getVisibility() == View.GONE) {
			mFooterView.setVisibility(View.VISIBLE);
			if(adapterSize < recordCount){
				Logger.d(TAG, "adapterSize="+adapterSize+",currentPage=" + currentPage+",recordCount="+recordCount);
				currentPage ++;
				footerText.setText(R.string.load_more);
				footerText.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				doLoadData();
			}else{
				footerText.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.btn_net_setting){
			NetworkUtil.enterNetWorkSetting(getActivity());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (null != mAdapter){
			Logger.d("sortSub", "notifyData onResume");
			mAdapter.notifyDataSetChanged();
		}
	}
}
