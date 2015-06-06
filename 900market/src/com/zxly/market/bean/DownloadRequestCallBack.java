package com.zxly.market.bean;

import java.io.File;
import java.lang.ref.WeakReference;

import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zxly.market.utils.StatisticsManager;

public class DownloadRequestCallBack extends RequestCallBack<File> {
	
    @SuppressWarnings("unchecked")
    private void refreshListItem() {
        if (userTag == null) return;
        WeakReference<ViewHolder> tag = (WeakReference<ViewHolder>) userTag;
        ViewHolder holder = tag.get();
        if (holder != null) {
        	holder.refresh();

        }
    }

    @Override
    public void onStart() {
        refreshListItem();
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
    	Log.e("fengruyi", "正在下载"+current);
        refreshListItem();
    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {
        refreshListItem();
    }

    @Override
    public void onFailure(HttpException error, String msg) {
        refreshListItem();
    }

    @Override
    public void onCancelled() {
        refreshListItem();
    }
}
