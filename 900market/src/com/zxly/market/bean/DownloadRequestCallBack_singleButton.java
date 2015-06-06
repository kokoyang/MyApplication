package com.zxly.market.bean;

import java.io.File;
import java.lang.ref.WeakReference;

import android.util.Log;
import android.widget.Button;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zxly.market.R;

public class DownloadRequestCallBack_singleButton extends RequestCallBack<File> {
	
    private void refreshListItem(State state,int percent){
    	 if (userTag == null) return;
		Button tag = null;
		if(userTag instanceof Button){
			 tag = (Button) userTag;
		}
		if(tag==null)return;
    	 switch (state) {
        case STARTED:
 			
		case WAITING:
			tag.setText(R.string.waiting);
			tag.setTag(R.string.waiting);
			break;
		case LOADING:
			tag.setText(percent+"%");
			tag.setTag(R.string.stop);
			break;
		case SUCCESS:
			tag.setText(R.string.install);
			tag.setTag(R.string.install);
			break;
		case FAILURE:
			tag.setText(R.string.retry);
			tag.setTag(R.string.retry);
			break;
		case CANCELLED:
			tag.setText(R.string.resume);
			tag.setTag(R.string.resume);
			break;
		default:
			break;
		}
    }
    
    @Override
    public void onStart() {
        refreshListItem(State.STARTED,0);
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
    	if(total>0){
    		refreshListItem(State.LOADING,(int)(current*100/total));
    	}else{
    		refreshListItem(State.LOADING,0);
    	}
    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {
        refreshListItem(State.SUCCESS,0);
    }

    @Override
    public void onFailure(HttpException error, String msg) {
        refreshListItem(State.FAILURE,0);
    }

    @Override
    public void onCancelled() {
        refreshListItem(State.CANCELLED,0);
    }
}
