/* *****************************************
 *      上海市移卓网络科技有限公司
 *      网址：http://www.30.net/
 *      Copyright(C)2012-2013 
 ****************************************** */
package com.zxly.market.utils;

import android.content.Context;

import java.util.List;


/**
 * @class:CommonUtils
 * @description:公用工具类
 * @author:XiongWei
 * @date:2014年9月13日 下午3:24:23
 */
public final class CommonUtils {


	public static <T> boolean isEmptyList(List<T> list) {
		return list == null || list.size() == 0;
	}

	public static boolean validate(String result) {
		if(result!=null && !"".equals(result)){
			return true;
		}
		return false;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}



}
