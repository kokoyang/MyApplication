package com.zxly.market.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.zxly.market.R;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.service.DownloadService;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.Logger;
import com.zxly.market.view.ShyzWebView;

/**
 * 专题详情页面
 * @author Administrator
 *
 */
public class TopicDetailActivity extends BaseActivity{
	private final String TAG = "TopicDetailActivity";
	private ShyzWebView shyzWebView ;
	private DownloadManager downloadManager;
	private AppBroadcastReceiver mAppBroadcastReceiver; 
	public int getContentViewId() {
		return R.layout.activity_topic_detail;
	}
	Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case DownloadManager.CANCLED:
					for(int i = 0,length = shyzWebView.getWebviewsContainer().getChildCount();i<length;i++){
						
						((WebView)shyzWebView.getWebviewsContainer().getChildAt(i)).loadUrl("javascript:goo.appStop('"+((DownLoadTaskInfo)msg.obj).getPackageName()+"')");
					}
					break;
				case DownloadManager.FAILUE:
					for(int i = 0,length = shyzWebView.getWebviewsContainer().getChildCount();i<length;i++){
						
						((WebView)shyzWebView.getWebviewsContainer().getChildAt(i)).loadUrl("javascript:goo.appRetry('"+((DownLoadTaskInfo)msg.obj).getPackageName()+"')");
					}
					break;
				case DownloadManager.LOADING:
					for(int i = 0,length = shyzWebView.getWebviewsContainer().getChildCount();i<length;i++){
						
						((WebView)shyzWebView.getWebviewsContainer().getChildAt(i)).loadUrl("javascript:goo.appDownLoad('"+((DownLoadTaskInfo)msg.obj).getPackageName()+"')");
					}
					break;
				case DownloadManager.SUCCESS:
					for(int i = 0,length = shyzWebView.getWebviewsContainer().getChildCount();i<length;i++){
						
						((WebView)shyzWebView.getWebviewsContainer().getChildAt(i)).loadUrl("javascript:goo.appInstall('"+((DownLoadTaskInfo)msg.obj).getPackageName()+"')");
					}
					break;
			}
		};
	};
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(mAppBroadcastReceiver!=null){	
			unregisterReceiver(mAppBroadcastReceiver);		
			mAppBroadcastReceiver = null;
		}
	};
	public void initViewAndData() {
		 shyzWebView = (ShyzWebView) findViewById(R.id.mywebview);
		 Intent intent = getIntent();
		 if(intent==null){
			 Toast.makeText(this, "出错，没有传入参数", 0).show();
			 return;
		 }
		 downloadManager = DownloadService.getDownloadManager(BaseApplication.getInstance());
		 downloadManager.setHandler(myHandler);
		 String webUrl = intent.getStringExtra(Constant.TOPIC_DETIAL_URL); 
		 shyzWebView.createNewWebview(webUrl);
		 shyzWebView.setTitle(intent.getStringExtra(Constant.TOPIC_TITLE));
		 mAppBroadcastReceiver=new AppBroadcastReceiver(); 
	      IntentFilter intentFilter=new IntentFilter(); 
	      intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
	      intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
	      intentFilter.addDataScheme("package"); 
	      this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	}
	private class AppBroadcastReceiver extends BroadcastReceiver { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	        String action=intent.getAction(); 
	        String packageName = intent.getDataString();
	        if (Intent.ACTION_PACKAGE_ADDED.equals(action)&&packageName!=null) { 
	        	for(int i = 0,length = shyzWebView.getWebviewsContainer().getChildCount();i<length;i++){
					((WebView)shyzWebView.getWebviewsContainer().getChildAt(i)).loadUrl("javascript:goo.appOpen('"+packageName.replace("package:", "")+"')");
				}
	            
	        } 
	        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)&&packageName!=null) { 
	        	for(int i = 0,length = shyzWebView.getWebviewsContainer().getChildCount();i<length;i++){
					((WebView)shyzWebView.getWebviewsContainer().getChildAt(i)).loadUrl("javascript:goo.appCancel('"+packageName.replace("package:", "")+"')");
				}
	            
	        } 
	    } 
	   
	}
}
