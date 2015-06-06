package com.zxly.market.adapter;

import java.util.List;

import android.content.Context;

import com.zxly.market.activity.BaseApplication;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.service.DownloadService;


public class GridViewAppAdapter extends RecomandGridViewAppAdapter{
	public GridViewAppAdapter(Context context,List<ApkInfo> list){
		super(context, list);
		downloadManager = DownloadService.getDownloadManager(BaseApplication.getInstance());
	}
	public int getCount() {
		return mList == null?0:mList.size();
	}
}
