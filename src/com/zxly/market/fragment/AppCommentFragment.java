package com.zxly.market.fragment;

import java.util.List;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxly.market.R;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.adapter.AppCommentAdapter;
import com.zxly.market.entity.AppDetailInfo;
import com.zxly.market.entity.CommentData;
import com.zxly.market.entity.CommentInfo;
import com.zxly.market.model.AppCommentControler;
import com.zxly.market.model.IAppCommentView;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.LoadmoreListView;
import com.zxly.market.view.PublishCommentDialog;
import com.zxly.market.view.LoadmoreListView.OnLoadListener;
import com.zxly.market.view.PublishCommentDialog.SubmitCallBack;

/**
 * 应用详情里的评论页面 
 * @author fengruyi
 *
 */
public class AppCommentFragment extends BaseFragment{
	private LoadmoreListView mListView;
	private View mEmptyView;
	private List<CommentInfo> mCommentlist;
	private AppCommentAdapter mAdapter;
	private View headView;
	AppDetailActivity activity;
	public AppCommentFragment(){
	}
	@Override
	public void handleInfoMessage(Message msg) {

	}

	@Override
	public int getContentViewId() {
		return R.layout.fragment_app_comment;
	}

	@Override
	public void initViewAndData() {
		mListView = obtainView(R.id.lv_comments);
		mEmptyView = obtainView(R.id.emptyview);
		mListView.setEmptyView(mEmptyView);
		mListView.setPadding(getResources().getDimensionPixelSize(R.dimen.title_bar_padding),0, getResources().getDimensionPixelSize(R.dimen.title_bar_padding), (int) (((AppDetailActivity)getActivity()).scollheight+BaseApplication.mHeightPixels*0.1));
	}
	
	public ListView getListView(){
		return mListView;
	}

	public void showCommentData(List<CommentInfo> list) {
		if(list == null||list.size() == 0)return;
		mCommentlist = list;
		mAdapter = new AppCommentAdapter(getActivity(), mCommentlist);	
	    activity = (AppDetailActivity) getActivity();
		if(!activity.controler.isLastPage()){
			mListView.addFootView(getActivity());
			mListView.setOnLoadListener(new OnLoadListener() {
				
				public void onRetry() {
					activity.controler.loadCommentData(activity.mAppdetailInfo.getPackName(), true);
				}
				
				public void onLoad() {
					activity.controler.loadCommentData(activity.mAppdetailInfo.getPackName(), true);
				}
			});
		}
		if(headView==null){
			headView = LayoutInflater.from(getActivity()).inflate(R.layout.app_comment_headview, null);
			mListView.addHeaderView(headView);
		}
		TextView commentcount = (TextView) headView.findViewById(R.id.tv_related_lable);
		commentcount.setText(String.format(getString(R.string.comment_count), activity.controler.getCommentCount()));
		mListView.setAdapter(mAdapter);
		
	}

	public void showMoreCommentData(CommentData data) {
		mCommentlist.addAll(data.getApkList());
		mAdapter.notifyDataSetChanged();
		if(activity.controler.isLastPage()){
			mListView.loadFull();
		}else{
			mListView.onLoadComplete();
		}
		
	}
}
