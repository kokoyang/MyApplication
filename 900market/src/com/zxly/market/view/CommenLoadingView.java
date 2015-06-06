package com.zxly.market.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.zxly.market.R;
import com.zxly.market.utils.NetworkUtil;

/**
 *共用加载-无网络-无数据页
 * @author fengruyi
 *
 */
public class CommenLoadingView extends RelativeLayout{
    private View LoadingView;
    private View emptyDataView;
    private View notNetView;
	private ViewStub viewstub_empty;
	private ViewStub viewstub_net;
	private View btnNetSetting;
	private View btnReload;
	public CommenLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	protected void onFinishInflate() {
		super.onFinishInflate();
		LoadingView = findViewById(R.id.rlt_loading);
		
	}
	
	/**
	 * 隐藏整个页面
	 */
	public void hide(){
		setVisibility(View.GONE);
	}
	/**
	 * 显示加载页面
	 */
	public void showLoadingView(){
		LoadingView.setVisibility(View.VISIBLE);
		if(notNetView!=null){
			notNetView.setVisibility(View.GONE);
		}
		if(emptyDataView!=null){
			emptyDataView.setVisibility(View.GONE);
		}
	}
	/**
	 * 显示数据为空页面
	 */
	public void showEmptyDataView(){
		setVisibility(View.VISIBLE);
		viewstub_empty = (ViewStub) findViewById(R.id.stu_emptyview);
		if(viewstub_empty!=null){//表示还没有inflater过
		    viewstub_empty.inflate();
			emptyDataView =findViewById(R.id.com_empy_view);
		}
		emptyDataView.setVisibility(View.VISIBLE);
		if(notNetView!=null){
			notNetView.setVisibility(View.GONE);
		}
		LoadingView.setVisibility(View.GONE);
	}
	/**
	 * 显示无网络页面
	 */
	public void showNoNetView(){
		setVisibility(View.VISIBLE);
		viewstub_net = (ViewStub) findViewById(R.id.stu_netview);
		if(viewstub_net!=null){//表示还没有inflater过
			viewstub_net.inflate();
			notNetView = findViewById(R.id.com_neterro_view);
			btnReload = findViewById(R.id.tv_reload);
			btnNetSetting = findViewById(R.id.btn_net_setting);
			btnNetSetting.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					NetworkUtil.enterNetWorkSetting(getContext());
				}
			});
		}
		notNetView.setVisibility(View.VISIBLE);
		if(emptyDataView!=null){
			emptyDataView.setVisibility(View.GONE);
		}
		LoadingView.setVisibility(View.GONE);
	}

	public void reloading(OnClickListener listenter){
		if(btnReload!=null){
			btnReload.setOnClickListener(listenter);
		}
	}

	public void reload(){
		if(btnReload!=null){
			btnReload.performClick();
		}
	}
}
