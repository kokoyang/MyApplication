package com.zxly.market.view;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.HotSearchActivity;
import com.zxly.market.js.JsObj;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * 自定义webview组合控件，操作历史记录上一下下一页带有动画切换
 * @author fengruyi
 *
 */
public class ShyzWebView extends RelativeLayout implements OnClickListener{
	private LimitLengthTextView btmBack;
	private View  btnSearch;
	private RelativeLayout webviewsContainer;//多个webview容器
	public RelativeLayout getWebviewsContainer() {
		return webviewsContainer;
	}

	public void setWebviewsContainer(RelativeLayout webviewsContainer) {
		this.webviewsContainer = webviewsContainer;
	}
	private Animation back_an1;//上一页动画，从右向左移动出现
	private Animation back_an2;//上一页动画，从右向左移动消失
	private Context context;
	private int currentWebviewIndex = -1;//记录当前显示webview的索引,用于历史记录切换
	private int webViewIndex = -1;//webviewr索引
	private RelativeLayout.LayoutParams lp;//webview的layoutparams
	private ProgressBar progressbar;//加载网页的进度条
	private boolean isLoading;//页面是否在加载中
	private boolean isReflashing;//是否处于刷新状态
	public ShyzWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public ShyzWebView(Context context) {
		super(context);
		this.context = context;
	}
	public ShyzWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}
    
	private void init(){
		 btmBack = (LimitLengthTextView) findViewById(R.id.tv_back);
		 btnSearch = findViewById(R.id.ibtn_search);
		 progressbar = (ProgressBar) findViewById(R.id.webview_seebar);
		 webviewsContainer = (RelativeLayout) findViewById(R.id.webviews_container);
		 back_an1 = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left);
		 back_an2 = AnimationUtils.loadAnimation(context, R.anim.slide_out_to_right);
		 lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
		 back_an2.setAnimationListener(animationListener);
		 btmBack.setOnClickListener(this);
		 btnSearch.setOnClickListener(this);
	}
	
	public void setTitle(String title){
		if(TextUtils.isEmpty(title))return;
		btmBack.setText(title,13);
	}
	//两个webview间的切换动画监听
	private  AnimationListener animationListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation arg0) {
			
		}
		
		@Override
		public void onAnimationRepeat(Animation arg0) {
			
		}
		
		@SuppressLint("NewApi")
		@Override
		public void onAnimationEnd(Animation arg0) {
			if(arg0 == back_an2){//如果是后退动画结束时隐藏当前页，显示后退一页
				MyWebView currentWebView = (MyWebView) webviewsContainer.getChildAt(currentWebviewIndex+1);
				MyWebView lastWebView = (MyWebView) webviewsContainer.getChildAt(currentWebviewIndex);
				if(currentWebView!=null&&lastWebView!=null){
					//此两个方法是为了切换webview时，暂停当前的播放视频，如果不作处理，则切换页面时，视频仍在继续播放
					currentWebView.onPause();
					lastWebView.onResume();
					currentWebView.setVisibility(View.GONE);
					lastWebView.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	WebViewClient webViewClient  = new  WebViewClient(){
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			//Log.e("fengruyi", "onReceivedError url -->"+failingUrl+"errorCode--"+errorCode+"description-->"+description);
		
			
		};
		@Override
		public void onReceivedSslError(WebView view,
				SslErrorHandler handler, SslError error) {
			// TODO Auto-generated method stub
			handler.proceed();// 接受证书
		}
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
			isLoading = true;
			showCancleView(true);
			
		};
		public void onPageFinished(WebView view, String url) {
			if(!view.getSettings().getLoadsImagesAutomatically()) {
				view.getSettings().setLoadsImagesAutomatically(true);
		    }
			if(isReflashing){
				isReflashing = false;
				showCancleView(false);
			}
			if(view.getTag()!=null&&view.getTag().equals(webViewIndex)&&isLoading){
				isLoading = false;
				//Log.e("fengruyi", "已经加载完成要显示-->"+webViewIndex);	
				
				//currentWebviewIndex ++;
				if(currentWebviewIndex>=1){//两页以上才能隐藏上一页
					webviewsContainer.getChildAt(webViewIndex-1).setVisibility(View.GONE);
				}
				webviewsContainer.getChildAt(webViewIndex).setVisibility(View.VISIBLE);
			}
			
		};
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//Log.e("fengruyi", "shouldOverrideUrlLoading url -->"+url);
			if(webViewIndex>=0){
				MyWebView currentWebview = getVisibleWebview();//当前页
				if(webViewIndex<webviewsContainer.getChildCount()&&currentWebview!=null&&currentWebview.isClickLink()){
					int index = (Integer) currentWebview.getTag();
					destroyAllwebViewsAndGohome(index+1,false);
					currentWebview.stopLoading();
					//Log.e("fengruyi", "创建新的页面");
					createNewWebview(url);
					webviewsContainer.getChildAt(index+1).setVisibility(View.GONE);	//currentWebviewIndex已经+1	
				}
			}
			return false;// super.shouldOverrideUrlLoading(view, url);
		};
		
		
	};
	WebChromeClient  webChromeClient = new WebChromeClient(){
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			//Log.e("fengruyi", "onProgressChanged newProgress -->"+newProgress);
			if(view.getTag().equals(webViewIndex)||view.getTag().equals(currentWebviewIndex)){//表示进度变化是当前的webview
				if(newProgress == 100){
					progressbar.setVisibility(View.GONE);//隐藏加载进度条
				}else{
					//显示加载进度条
					if(progressbar.getVisibility() == View.GONE){
						progressbar.setVisibility(View.VISIBLE);
					}
					progressbar.setProgress(newProgress);
				}
			}
		}
	};
	
	/**
	 * 获取当前可见的webview
	 * @return
	 */
	public MyWebView getVisibleWebview(){
		MyWebView curentWebview = (MyWebView) webviewsContainer.getChildAt(webViewIndex);
		MyWebView nextWebview = (MyWebView) webviewsContainer.getChildAt(webViewIndex-1);
		if(curentWebview!=null&&curentWebview.getVisibility()==View.VISIBLE){
			return curentWebview;
		}else if(nextWebview!=null&&nextWebview.getVisibility()==View.VISIBLE){
			return nextWebview;
		}
		return null;
	}
	/**
	 * 根据url创建新的webview
	 * @param url url地址
	 * 
	 */
	public void createNewWebview(String url){
		webViewIndex++;
		currentWebviewIndex= webViewIndex;
		MyWebView webView = new MyWebView(context);
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		webView.setTag(currentWebviewIndex);
		webView.setWebViewClient(webViewClient);
		webView.setWebChromeClient(webChromeClient);
		webviewsContainer.addView(webView, lp);
		if (!BaseApplication.getInstance().isOnline()) {
			webView.loadUrl("file:///android_asset/error/index.html");
		} else {
			webView.loadUrl(url);
		}
	}
	public WebView getCurrenWebview(){
		return  (WebView) webviewsContainer.getChildAt(currentWebviewIndex);
	}
	/**
	 *上一页的动画切换
	 */
	@SuppressLint("NewApi")
	public void goBack(){
		if(currentWebviewIndex>=1){
			currentWebviewIndex--;
			MyWebView currentWebView = (MyWebView) webviewsContainer.getChildAt(currentWebviewIndex+1);
			MyWebView lastWebView = (MyWebView) webviewsContainer.getChildAt(currentWebviewIndex);
			if(currentWebView!=null&&lastWebView!=null){
				currentWebView.startAnimation(back_an2);//出去
				lastWebView.startAnimation(back_an1);//进入
		    }	
		}else{
			destroyAllwebViewsAndGohome(0,true);
		}
	}
	/**
	 *加载页面中显示可取消按键
	 *@param 是否显示取消按钮，true是显示，false则不显示
	 */
	public void showCancleView(boolean flag){
		
		btmBack.setSelected(flag);
	}
	/**
	 * 取消加载
	 */
	public void cancleLoading(){
		isLoading = false;
		progressbar.setVisibility(View.GONE);//隐藏加载进度条
		destroyAllwebViewsAndGohome(currentWebviewIndex, currentWebviewIndex == 0);
		if(currentWebviewIndex>=0){
			webviewsContainer.getChildAt(currentWebviewIndex).setVisibility(View.VISIBLE);
		}else{
			((Activity)context).finish();
		}
		
	}
	/**
	 * 销毁webview，并根据条件是否要关闭当前页面
	 * @param startIndex
	 * @param isExit
	 */
	public void destroyAllwebViewsAndGohome(int startIndex ,boolean isExit){
		MyWebView destroyWebview;
		for(int i = startIndex,length = webviewsContainer.getChildCount();i<length;i++){
			destroyWebview = (MyWebView) webviewsContainer.getChildAt(startIndex);
			if(destroyWebview!=null){
				destroyWebview.removeAllViews();
				destroyWebview.stopLoading();
				destroyWebview.onPause();
				destroyWebview.destroy();
				webviewsContainer.removeView(destroyWebview);
				destroyWebview = null;
				webViewIndex -- ;
				currentWebviewIndex -- ;
			}
		}
		if(isExit){
			((Activity)context).onBackPressed();
			//gohome
		}
	}
	public WebView getCurrentWebView(){
		return (WebView) webviewsContainer.getChildAt(webViewIndex);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			if(btmBack.isSelected()){
				cancleLoading();
			}else{
				goBack();
			}
			break;
		case R.id.ibtn_search:
			Intent intent = new Intent(context,HotSearchActivity.class);
		    context.startActivity(intent);
		default:
			break;
		}
		
	}
	
	class MyWebView extends WebView{
		private boolean flag;//此判断是为了控制链接是否是手点开的，处理链接重定向产生多个webview
		private float lastX,lastY;
		@SuppressWarnings("deprecation")
		@SuppressLint("SetJavaScriptEnabled")
		public MyWebView(Context context) {
			super(context);
		setLayerType(View.LAYER_TYPE_SOFTWARE,
					null); 
		WebSettings webSettings = getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			JsObj jsObj = new JsObj((Activity)context);
			addJavascriptInterface(jsObj, "roid");
		}
		
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouchEvent(MotionEvent event) {//点击
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				lastX = event.getX();
				lastY = event.getY();
			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				//Log.e("fengruyi", "x 方向-->"+(lastX - event.getX())+ "y 方向-->"+(lastY - event.getY()));
				//处理是否是点击事件，出现大幅度移动的则判断不是点击,解决页面滑动刷新时不用再去新建页面
				flag= (Math.abs(lastX - event.getX())<5)&&(Math.abs(lastY - event.getY())<5);
			}
			return super.onTouchEvent(event);
		}
		/**
		 * 判断链接是否是来源于点击的
		 * @return
		 */
		public boolean isClickLink(){
			boolean temp = flag;
			flag = false;
			return temp;
		}
//		private class MyWebViewDownLoadListener implements DownloadListener {
//
//			@Override
//			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
//					long contentLength) {
//
//				//Log.e("fengruyi", "有下载页面-->"+url);
//
//			}//处理下载页面，不添加刚下载链接无效
//			
//		}
	}
}
