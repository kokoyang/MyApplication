/**    
 * @FileName: HttpHelper.java  
 * @Package:com.zxly.market.http  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-8 下午6:31:01  
 * @version V1.0    
 */
package com.zxly.market.http;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.BodyParamsEntity;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.FunctionNameEnum;
import com.zxly.market.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** 
 * @ClassName: HttpHelper  
 * @Description: 网络请求引用的是xutil包，在些基础上再次封装，方便以便切换请求框架 
 * @author: fengruyi 
 * @date:2015-4-8 下午6:31:01   
 */
public class HttpHelper {
	public final static String TAG = HttpHelper.class.getName();
	public static final int TIME_OUT = 1000*7;//超时20秒
	public final static String HOST = "http://apiqa.18guanjia.com/AppMarket/";//请求域名
	private static HttpUtils http;
	
	public static HttpUtils getHttpUtils(){
		if(http==null){
			http = new HttpUtils(TIME_OUT);
			http.configDefaultHttpCacheExpiry(2000);//缓存时间 
			//http.configCurrentHttpCacheExpiry(currRequestExpiry)
		}
		return http;
	}
	/**
	 * 
	 * @Title: send  
	 * @Description: 不带参数的请求
	 * @param @param method 请求的方法 get or post
	 * @param @param url 请求地址
	 * @param @param callbck  回调
	 * @return void 
	 * @throws
	 */
	public static void send(HttpMethod method,String url,final HttpCallBack callbck){
		if(http==null){
			 http = getHttpUtils();
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("imei", BaseApplication.imei);
		params.addBodyParameter("channalId", BaseApplication.coid);
		params.addBodyParameter("coid", "3");
		params.addBodyParameter("imsi", BaseApplication.imsi);
		params.addBodyParameter("token", Constant.APP_TOKEN);
		http.send(method, url, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException e, String msg) {
				callbck.onFailure(e, msg);
				Logger.e(TAG, "请求失败--->"+msg);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//Logger.e(TAG, "成功取得信息--->"+responseInfo.result);
				callbck.onSuccess(responseInfo.result);
				
			}
		});
		
	}
	/**
	 * 
	 * @Title: send  
	 * @Description: 带参数的请求
	 * @param @param method
	 * @param @param method 请求的方法 get or post
	 * @param @param url 请求地址
	 * @param @param callbck  回调
	 * @return void 
	 * @throws
	 */
	public static void send(HttpMethod method,String url,RequestParams params,final HttpCallBack callbck){
		if(http==null){
			 http = getHttpUtils();
		}
		if(params!=null){
			params.addBodyParameter("imei", BaseApplication.imei);
			params.addBodyParameter("channalId", BaseApplication.coid);
			params.addBodyParameter("coid", "3");
			params.addBodyParameter("imsi", BaseApplication.imsi);
			params.addBodyParameter("token", Constant.APP_TOKEN);
		}
		if(params.getEntity() instanceof BodyParamsEntity){
			BodyParamsEntity body = (BodyParamsEntity)params.getEntity();
			try {
				String param = inputStream2String(body.getContent());
				Logger.d("stat", "post send url = " + (url+param));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		http.send(method, url,params, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException e, String msg) {
				callbck.onFailure(e, msg);
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				callbck.onSuccess(responseInfo.result);
				
			}
		});
	}

	/**
	 * 统计上报
	 * @param method
	 * @param url
	 * @param params
	 * @param callbck
	 */
	public static void statSend(HttpMethod method,String url,RequestParams params,final HttpCallBack callbck){
		if(http==null){
			http = getHttpUtils();
		}
		if(params.getEntity() instanceof BodyParamsEntity){
			BodyParamsEntity body = (BodyParamsEntity)params.getEntity();
			try {
				String param = inputStream2String(body.getContent());
				Logger.d("stat", "post stat url = " + (url+param));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		http.send(method, url,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException e, String msg) {
				callbck.onFailure(e, msg);
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				callbck.onSuccess(responseInfo.result);
			}
		});
	}

	private static String inputStream2String(InputStream in_st) throws IOException {
		if(!Logger.debug)return "";
		BufferedReader in = new BufferedReader(new InputStreamReader(in_st));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null){
			buffer.append(line);
		}
		return buffer.toString();
	}
	
	public interface HttpCallBack{
		public void onFailure(HttpException e,String msg);
		public void onSuccess(String result);
	}
}
