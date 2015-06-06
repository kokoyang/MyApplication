package com.zxly.market.fragment;

import java.util.List;

import com.lidroid.xutils.exception.DbException;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.MainActivity;
import com.zxly.market.adapter.ListAppDownLoadedAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.service.DownloadService;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.view.PromptDialog;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 已经下载完成页面
 * @author fengruyi
 *
 */
public class TaskDoneFragment extends BaseFragment implements OnClickListener{
	private ListView mListView;
	private ListAppDownLoadedAdapter mAdapter;
	private List<DownLoadTaskInfo> doneglist;
	private View emptyView;
	private Button btn_tomarket;
	private Button btn_clearAll;
	private TextView tv_empty;
	private DownloadManager downloadmananger;
	private PromptDialog mDialog;
	public void handleInfoMessage(Message msg) {
		
		
	}

	public int getContentViewId() {
		
		return R.layout.fragment_downloaded;
	}

	public void initViewAndData() {
		downloadmananger = DownloadService.getDownloadManager(BaseApplication.getInstance());
		mListView = obtainView(R.id.lv_default);
		emptyView  =obtainView(R.id.emptyview);
		btn_clearAll = obtainView(R.id.btn_clear);
		btn_tomarket = obtainView(R.id.btn_go);
		tv_empty = obtainView(R.id.tv_empty);
		tv_empty.setText(R.string.no_done_task);
		tv_empty.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.logo_no_done, 0, 0);
		btn_tomarket.setOnClickListener(this);
		btn_clearAll.setOnClickListener(this);
		mListView.setEmptyView(emptyView);
	    doneglist = downloadmananger.getDoneTask();
		mAdapter = new  ListAppDownLoadedAdapter(getActivity(), doneglist);
		mListView.setAdapter(mAdapter);
		showBottomButton(doneglist.size()!=0);
	}
    
	/**
	 * 添加一个已完成任务到列表中
	 * @param task
	 */
	public void addTask(DownLoadTaskInfo task){
		mAdapter.addItem(task);
		showBottomButton(true);
	}
	/**
	 * 刷新页面
	 */
	public void reFresh(){
		if(mAdapter!=null){
			mAdapter.notifyDataSetChanged();
		}
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_clear:
			if(doneglist==null&&doneglist.size() == 0){
				
				return ;
			}
			if(mDialog==null){
        		mDialog = new PromptDialog(getActivity());
        		mDialog.setCanceledOnTouchOutside(true);
        	}
        	mDialog.setTxt(getActivity().getString(R.string.title_delete), getActivity().getString(R.string.delete_all));
        	mDialog.show(new OnClickListener() {
				
				public void onClick(View arg0) {
					 switch (arg0.getId()) {
					   case R.id.btn_ok:
						   try {
								downloadmananger.removeDownload(doneglist);
								doneglist.clear();
								mAdapter.notifyDataSetChanged();
								showBottomButton(false);
							  } catch (DbException e) {
								e.printStackTrace();
							  }
						   break;
					 }
				  mDialog.dismiss();
				}
			});
			
			break;
		case R.id.btn_go:
			Intent intent = new Intent(getActivity(),MainActivity.class);
			intent.putExtra(Constant.MAIN_SWITCH_TAB, 2);
			startActivity(intent);
			getActivity().finish();
			break;
		default:
			break;
		}
		
	}
    /**
     * 是否显示清空历史按钮
     * @param flag
     */
	private void showBottomButton(boolean flag){
		if(flag){
			btn_clearAll.setVisibility(View.VISIBLE);
			obtainView(R.id.diliver).setVisibility(View.VISIBLE);
		}else{
			btn_clearAll.setVisibility(View.GONE);
			obtainView(R.id.diliver).setVisibility(View.GONE);
		}
	}
}
