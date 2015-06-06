package com.zxly.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ZXWebview extends WebView{

	public ZXWebview(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				 return true;
			}
		});
	}

}
