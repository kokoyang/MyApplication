package com.zxly.market.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.LoadmoreListView.OnLoadListener;

/**
 * scrollview内嵌套GridView
 * @author fengruyi
 *
 */
public class ZXListView extends ListView implements OnScrollListener,OnClickListener{

	private View footView;
	private ProgressBar progressbar;
	private TextView tvLoadmore;
	private boolean isLoading;// 判断是否正在加载
	private boolean loadEnable = true;// 开启或者关闭加载更多功能
	private OnLoadListener onLoadListener;
	
	public ZXListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	// 加载更多监听
	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.loadEnable = true;
		this.onLoadListener = onLoadListener;
	}
	public boolean isLoadEnable() {
		return loadEnable;
	}
	
	public void addFootView(Context context){
		isLoading = false;
		if(footView==null){
			footView = LayoutInflater.from(context).inflate(R.layout.loadmore_foot_, null);
			progressbar = (ProgressBar) footView.findViewById(R.id.progressbar_);
			tvLoadmore = (TextView) footView.findViewById(R.id.message_);
			tvLoadmore.setOnClickListener(this);
			this.addFooterView(footView);
		    this.setOnScrollListener(this);
		}
		if(getFooterViewsCount()==0){
			this.addFooterView(footView);
		}
	}
	
	// 这里的开启或者关闭加载更多，并不支持动态调整
	public void setLoadEnable(boolean loadEnable) {
		this.loadEnable = loadEnable;
		this.removeFooterView(footView);
	}
	// 用于加载更多结束后的回调
	public void onLoadComplete() {
		isLoading = false;
	}
	//加载失败
	public void loadFail(){
		progressbar.setVisibility(View.GONE);
		tvLoadmore.setSelected(true);//可以点击响应
		tvLoadmore.setText(R.string.load_retry);
	}
	//已经加载完成，没有更多数据可加载
	public void loadFull(){
		isLoading = true;
		removeFooterView(footView);
	}
	
    /*
	 * 定义加载更多接口
	 */
	public interface OnLoadListener {
		public void onLoad();
		public void onRetry();
	}
    
	public void onScroll(AbsListView view, int arg1, int arg2, int arg3) {
		
		
	}
	public void onScrollStateChanged(AbsListView view, int arg1) {
		if(!isLoading&&footView!=null&&getFooterViewsCount()!=0&&view.getLastVisiblePosition() == view.getCount()-1){
			if(onLoadListener!=null){
				onLoadListener.onLoad();
			}
			isLoading = true;
		}
		
	}
	public void onClick(View v) {
		if (onLoadListener!=null&&v.isSelected()) {
			onLoadListener.onRetry();
			
		}
		progressbar.setVisibility(View.VISIBLE);
		tvLoadmore.setSelected(false);//可以点击响应
		tvLoadmore.setText(R.string.load_more);
	}
	
}
