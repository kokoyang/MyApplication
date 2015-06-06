/**    
 * @FileName: TopicAppActivity.java  
 * @Package:com.zxly.market.activity  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-15 上午10:15:52  
 * @version V1.0    
 */
package com.zxly.market.activity;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.zxly.market.R;
import com.zxly.market.adapter.ListTopicAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.SpecialInfo;
import com.zxly.market.model.ISpecialView;
import com.zxly.market.model.SpecialActivityControler;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.LoadmoreListView;
import com.zxly.market.view.LoadmoreListView.OnLoadListener;



public class TopicAppActivity extends BaseActivity implements OnClickListener,ISpecialView,MarketReceiver.NetChangeObserver{
	private final String TAG = "TopicAppActivity";
	private LoadmoreListView mListView;
	private CommenLoadingView loadingView;
	private View mBtnSearch;
	private SpecialActivityControler controler;
	private ListTopicAdapter mAdapter;
	private List<SpecialInfo> mList;
	public int getContentViewId() {
		
		return R.layout.activity_topic;
	}

	public void initViewAndData() {
		controler = new SpecialActivityControler(this);
		setBackTitle(R.string.special_label);
		mListView = obtainView(R.id.lv_topic);
		loadingView = obtainView(R.id.loading_view);
		mBtnSearch= obtainView(R.id.ibtn_search);
		mBtnSearch.setOnClickListener(this);
		loadingView.showLoadingView();
		controler.loadSepcial(false);
		MarketReceiver.registerObserver(this);
	}
	public void onBackPressed() {
		controler.setFinish(true);
		super.onBackPressed();
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_search://查询
			Intent searchIntent = new Intent(this,HotSearchActivity.class);
			startActivity(searchIntent);
			break;
		case R.id.tv_reload:
			loadingView.showLoadingView();
			controler.loadSepcial(false);
		default:
			break;
		}
	}


	public void showEmptyView() {
		loadingView.showEmptyDataView();
		
	}

	public void showRequestErro() {
		Toast.makeText(this, "请求出错", 0).show();
		showEmptyView();
	}

	public void showNoNetwork() {
		loadingView.showNoNetView();
		loadingView.reloading(this);
		
	}

	public void showSpecialData(List<SpecialInfo> data) {
		mList = data;
		mAdapter = new ListTopicAdapter(this, mList);
		if(mList.size()==0){
			showEmptyView();
		}
		loadingView.hide();
		if(!controler.isLastPage()){
			mListView.addFootView(this);
			mListView.setOnLoadListener(new OnLoadListener() {
				
				public void onRetry() {
					controler.loadSepcial(false);
				}
				
				public void onLoad() {
					controler.loadSepcial(true);
				}
			});
		}
		Logger.e(TAG, "装载数据-->");
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2<mList.size()){
					 Intent intent = new Intent(TopicAppActivity.this,TopicDetailActivity.class);
					 intent.putExtra(Constant.TOPIC_DETIAL_URL, mList.get(arg2).getSpecUrl());
					 intent.putExtra(Constant.TOPIC_TITLE, mList.get(arg2).getClassName());
					 startActivity(intent);
				}
			}
			
		});
		
	}

	public void showMoreSpecialData(List<SpecialInfo> data) {
		mList.addAll(data);
		mAdapter.notifyDataSetChanged();
		if(controler.isLastPage()){
			mListView.loadFull();
		}else{
			mListView.onLoadComplete();
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
}
