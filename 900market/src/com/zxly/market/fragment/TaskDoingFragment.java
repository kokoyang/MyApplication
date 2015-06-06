package com.zxly.market.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.MainActivity;
import com.zxly.market.adapter.ListAppDownLoadingAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.service.DownloadService;
import com.zxly.market.utils.DownloadManager;
/**
 * 已经未下载完成页面
 * @author fengruyi
 *
 */
public class TaskDoingFragment extends BaseFragment implements OnClickListener{
	private ListView mListView;
	private ListAppDownLoadingAdapter doingLoadAdapter;
	private View emptyView;
	private Button btn_tomarket;
	private DownloadManager manager;
	
	public void handleInfoMessage(Message msg) {
		
		
	}
 
	public int getContentViewId() {
		
		return R.layout.fragment_downloading;
	}
  
	public void initViewAndData() {
		manager = DownloadService.getDownloadManager(BaseApplication.getInstance());
		mListView = obtainView(R.id.lv_default);
		emptyView  =obtainView(R.id.emptyview);
		btn_tomarket = obtainView(R.id.btn_go);
		btn_tomarket.setOnClickListener(this);
		mListView.setEmptyView(emptyView);
		List<DownLoadTaskInfo> doinglist = DownloadService.getDownloadManager(BaseApplication.getInstance()).getDoingTask();
		doingLoadAdapter = new ListAppDownLoadingAdapter(getActivity(), doinglist,manager);
		mListView.setAdapter(doingLoadAdapter);
		
	}
	public void onResume() {
		super.onResume();
		if(doingLoadAdapter!=null){
			doingLoadAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 未下载任务有任务完成时把该任务移到已完成中
	 * @param taskInfo
	 */
    public void removeTask(DownLoadTaskInfo taskInfo){
    	doingLoadAdapter.removeItem(taskInfo);
    }
    
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(),MainActivity.class);
		intent.putExtra(Constant.MAIN_SWITCH_TAB, 2);
		startActivity(intent);
		getActivity().finish();
	}

}
