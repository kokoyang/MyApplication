package com.zxly.market.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.handmark.pulltorefreshview.PullToRefreshBase;
import com.handmark.pulltorefreshview.PullToRefreshBase.Mode;
import com.handmark.pulltorefreshview.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefreshview.PullToRefreshListView2;
import com.zxly.market.R;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.activity.AppListActivity;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.HotSearchActivity;
import com.zxly.market.activity.MainActivity;
import com.zxly.market.activity.RakingListActivity;
import com.zxly.market.activity.TopicAppActivity;
import com.zxly.market.activity.TopicDetailActivity;
import com.zxly.market.adapter.HotAppListAdapter;
import com.zxly.market.adapter.RecomandGridViewAppAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.BanerInfo;
import com.zxly.market.entity.ClassicTopData;
import com.zxly.market.js.WebViewSetting;
import com.zxly.market.model.ClassAppActivityControler;
import com.zxly.market.model.IClassAppActivity;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.utils.UMengAgent;
import com.zxly.market.utils.ViewUtil;
import com.zxly.market.view.BannerCarouselView;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.LoadmoreListView;
import com.zxly.market.view.LoadmoreListView.OnLoadListener;
import com.zxly.market.view.RelativeLayoutForGridView;
import com.zxly.market.view.RelativeLayoutForGridView.OnGridviewItemClickListener;

/**
 * 精品页面
 * @author fengruyi
 *
 */
public class ClassicFragment extends BaseFragment implements IClassAppActivity,OnClickListener,MarketReceiver.NetChangeObserver{
	public static final String TAG = "ClassicFragment";
	private BannerCarouselView mBannerView;
	private WebView mWebview;
	private View mSortLay,
				 mSpecial,
				 mArtifact,
				 mGame,
				 mRank,
				 diliver,
				 mMore;
	private TextView mSearcherBar;
	private RelativeLayoutForGridView mGridView;
	
	private PullToRefreshListView2 pullListView;
	private LoadmoreListView mListView;
	

	/**热门app*/
	private List<ApkInfo> mHotAppList;
	
	
	private HotAppListAdapter mAdapter;
	/**业务控制器*/
	ClassAppActivityControler controler;
	
	
	private RecomandGridViewAppAdapter adapter;
	
	private CommenLoadingView loadingView;
	protected boolean isVisible;//是否可见
	private boolean isPrepared;//UI是否实例化
	@Override
	public int getContentViewId() {
		return R.layout.classic_fragment_layout2;
	}
	@SuppressLint("InflateParams")
	@Override
	public void initViewAndData() {
		  controler = new ClassAppActivityControler(this);
		  loadingView = obtainView(R.id.loading_view);
		  mSearcherBar = obtainView(R.id.search_bar);  
		  View view = LayoutInflater.from(getActivity()).inflate(R.layout.head_classs_listview,null); 
		  mBannerView = (BannerCarouselView) view.findViewById(R.id.v_banner);
		  mSortLay = view.findViewById(R.id.llt_sort);
		  mSpecial = view.findViewById(R.id.rlt_special);
		  mArtifact = view.findViewById(R.id.rlt_artifact);
		  diliver = view.findViewById(R.id.diliver_1);
		  mGame = view.findViewById(R.id.rlt_game);
		  mRank = view.findViewById(R.id.rlt_rank);
		  mMore = view.findViewById(R.id.rlt_more);
		  mGridView = (RelativeLayoutForGridView) view.findViewById(R.id.gv_recomand);
		  mWebview = (WebView) view.findViewById(R.id.web_ad);
		  pullListView = obtainView(R.id.lv_hot);
		  pullListView.setMode(Mode.PULL_FROM_START);
		  mListView = pullListView.getRefreshableView();
		  mListView.addHeaderView(view);
		  resizeView();
	      String key = PrefsUtil.getInstance().getString(Constant.HOT_KEY);
	      if(!TextUtils.isEmpty(key)){
	    	  mSearcherBar.setText(getString(R.string.hot_search)+key);
	      }else{
	    	  mSearcherBar.setText("");
	      }
		  MarketReceiver.registerObserver(this);
		  pullListView.setOnRefreshListener(new OnRefreshListener<LoadmoreListView>() {

			public void onRefresh(
					PullToRefreshBase<LoadmoreListView> refreshView) {
				
				  controler.LoadClassicTopData();
				  controler.loadHotApp(false);
			}
		});
		 isPrepared = true;
		 onVisible();
	}
	/**
	 * 判断fragment是否可见，
	 */
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			  isVisible = true;
	          onVisible();
		}
	}
	public  void onVisible(){
		 if(!isPrepared || !isVisible) {
	            return;
	        }
		 if(loadingView.getVisibility() == View.VISIBLE){
			  loadData();
		 }
	 }
	/**
	 * 有数据时把UI显示出来并设置各种事件监听
	 */
	public void showContentView(){
		 loadingView.hide();
		  ViewUtil.setOnClickListener(this, mSpecial,mArtifact,mGame,mRank,mSearcherBar,mMore);    
		  mListView.setOnItemClickListener(new OnItemClickListener() {
		  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					
					 if(mHotAppList.get(arg2-2) instanceof ApkInfo){
						 ApkInfo info = mHotAppList.get(arg2-2);
						 if(info.getAppName()!=null){//表示是跳转到应用详情
							 Intent intent = new Intent(getActivity(),AppDetailActivity.class);
							 intent.putExtra(Constant.APK_DETAIL, info.getDetailUrl());
							 startActivity(intent);
						 }else{//表示跳转到专题详情
							 Intent intent = new Intent(getActivity(),TopicDetailActivity.class);
							 intent.putExtra(Constant.TOPIC_DETIAL_URL, info.getDetailUrl());
							 intent.putExtra(Constant.TOPIC_TITLE, info.getContent());
							 startActivity(intent);
						 }
					 }
			
			}
		  });
	}
	public void showHotApp(List<ApkInfo> object) {		
		mHotAppList = object;
	    mAdapter = new HotAppListAdapter(getActivity(), mHotAppList);
		if(!controler.isLastPage()){
			mListView.addFootView(getActivity());
			mListView.setOnLoadListener(new OnLoadListener() {
				
				public void onRetry() {
					controler.loadHotApp(true);
				}
				
				public void onLoad() {
					Logger.e(TAG, "加载更多-->");
					controler.loadHotApp(true);
				}
			});
		}
		mListView.setAdapter(mAdapter);
	}
	
	public void resizeView() {
		 ViewUtil.setViewHeight(mBannerView, (int)(BaseApplication.mWidthPixels*0.375f));
		 ViewUtil.setViewHeight(mSortLay, (int)(BaseApplication.mWidthPixels*0.384f));
		 ViewUtil.setViewWidth(mArtifact, (int)(BaseApplication.mWidthPixels*0.25f));
		 ViewUtil.setViewHeight(mArtifact, (int)(BaseApplication.mWidthPixels*0.18f));
		 ViewUtil.setViewHeight(mGame, (int)(BaseApplication.mWidthPixels*0.18f));	
		 ViewUtil.setViewHeight(diliver, (int)(BaseApplication.mWidthPixels*0.18f));	
		 ViewUtil.setViewHeight(mWebview,(int)(BaseApplication.mHeightPixels*0.187f));
	}
	
	public void showMoreHotApp(List<ApkInfo> object) {
		mHotAppList.addAll(object);
		mAdapter.notifyDataSetChanged();
		if(controler.isLastPage()){
			mListView.loadFull();
		}else{
			mListView.onLoadComplete();
		}
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlt_special://专题
			 Intent topicIntent = new Intent(getActivity(),TopicAppActivity.class);
			 startActivity(topicIntent);
			UMengAgent.onEvent(getActivity(), UMengAgent.arry_app);
			break;
		case R.id.rlt_artifact://神器
			Intent artifactIntent = new Intent(getActivity(),AppListActivity.class);
			artifactIntent.putExtra("title", R.string.artifact_label);
			artifactIntent.putExtra("classcode", "Shen_qi0");
		    startActivity(artifactIntent);
			UMengAgent.onEvent(getActivity(),UMengAgent.arry_gold);
			break;
		case R.id.rlt_game://游戏
			Intent gameIntent = new Intent(getActivity(),AppListActivity.class);
			gameIntent.putExtra("title", R.string.game_lable);
			gameIntent.putExtra("classcode", "game-youxi");
			startActivity(gameIntent);
			UMengAgent.onEvent(getActivity(),UMengAgent.arry_game);
			break;
		case R.id.rlt_rank://排行
			Intent rankIntent = new Intent(getActivity(),RakingListActivity.class);
			 startActivity(rankIntent);
			UMengAgent.onEvent(getActivity(), UMengAgent.arry_list);
			break;
		case R.id.rlt_more://更多
			Intent moreIntent = new Intent(getActivity(),AppListActivity.class);
			moreIntent.putExtra("title", R.string.recomand);
			moreIntent.putExtra("classcode", "Daren_tuijian");
			startActivity(moreIntent);
			break;
		case R.id.search_bar:
			 Intent searchIntent = new Intent(getActivity(),HotSearchActivity.class);
	         startActivity(searchIntent);
			 break;
		case R.id.tv_reload://重新加载
			 loadData();
			 break;
		default:
			break;
		}
	}
	public void showBanner(List<BanerInfo> banners) {
		mBannerView.setData(banners, true);
		
	}
	public void showRecomandApp(List<ApkInfo> apkList) {
		adapter = new RecomandGridViewAppAdapter(getActivity(), apkList);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnGridviewItemClickListener() {			
			public void onItemClicked(int position) {
				 ApkInfo info = (ApkInfo) adapter.getItem(position);
				 Intent intent = new Intent(getActivity(),AppDetailActivity.class);
				 intent.putExtra(Constant.APK_DETAIL, info.getDetailUrl());
				 intent.putExtra(Constant.APK_PACKAGE, info.getPackName());
				 startActivity(intent);
			}
		});
	}
	/**
	 * 显示广告html
	 * @param url
	 */
	private void showAdData(String url){
		WebViewSetting setting = new WebViewSetting();
		setting.settings(getActivity(), mWebview);
		mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE,
				null); 
		mWebview.loadUrl(url);
	}
	public void showHotArearError() {
		mListView.loadFail();
		
	}
	public void showHtoArearEmpty() {
		// TODO Auto-generated method stub
		
	}
	public void showEmptyView() {
		if(pullListView.isRefreshing()){
			pullListView.onRefreshComplete();
		}
		loadingView.showEmptyDataView();
	}
	public void showNetErrorView() {
		handler.postDelayed(new Runnable() {	//因为无网络没有请求时间，所以这个方法返回来很快，不能正常关闭正在刷新的动作，所以有模拟耗时请求
			public void run() {
				if(pullListView.isRefreshing()){
					pullListView.onRefreshComplete();
				}
				
			}
		}, 1000);
		if(adapter==null){//没有加载过数据时才显示
			loadingView.showNoNetView();
			loadingView.reloading(this);
		}
	}
	
	public void handleInfoMessage(Message msg) {
	}
	public void showTopData(ClassicTopData data) {
		if(pullListView.isRefreshing()){
			pullListView.onRefreshComplete();
		}
		showContentView();
		showBanner(data.getBanAdList());
		showRecomandApp(data.getApkList());
		if(data.getJxAdList().size()>0){
			showAdData(data.getJxAdList().get(0).getUrl());
		}
	}
	public void loadData() {
		  loadingView.showLoadingView();
		  controler.LoadClassicTopData();
		  controler.loadHotApp(false);
		
	}
	public void onPause() {
		mBannerView.stopPlay();//轮播图播放关闭
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
		 refreshView();
	}
	/**
	 * 刷新gridview和listview
	 */
	public void refreshView(){
		if(mAdapter!=null&&mHotAppList!=null){
		  mAdapter.notifyDataSetChanged();
		}
		if(adapter!=null){	
			mBannerView.startPlay();//轮播图播放开始
			mGridView.notifyDataChanged();
		}
	}
	/**
	 * 根据一个apk信息来更新UI
	 * @param info
	 */
	public void refreshView(ApkInfo info){
		try {
			if(mAdapter!=null&&mHotAppList!=null&&mHotAppList.contains(info)){
				mAdapter.notifyDataSetChanged();
			}else{
				mGridView.notifyDataChanged();
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 达人推荐移除一个已经安装的 apk，替换一个新的
	 * @param info
	 */
	public void removeApkInfo(ApkInfo info){
		mGridView.removeItem(info);
	}
	
	public void netWorkConnect() {
		if(getActivity()==null|| getActivity().isFinishing())return;
		if(((MainActivity)getActivity()).getCurrentPage()==0&&adapter==null){
			loadingView.reload();
		}
		
	}
	
}
