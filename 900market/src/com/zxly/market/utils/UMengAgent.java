/**
 * @FILE:UMengAgent.java
 * @AUTHOR:XiongWei
 * @DATE:2014年7月29日 上午11:10:32
 **/
package com.zxly.market.utils;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;

/*******************************************
 * @COMPANY:		深圳市掌星立意科技有限公司
 * @CLASS:			UMengAgent
 * @DESCRIPTION:	
 * @AUTHOR:			Yangwencai
 * @VERSION:		v2.0
 * @DATE:			2015年5月4日 上午10:10:32
 *******************************************/
public class UMengAgent {
     /**精选四个Banner点击*/
     public static final String[] banners = {"Banner01","Banner02","Banner03","Banner04"};
    /**精选—神器*/
    public static final String arry_gold = "arry_gold";
    /**精选—游戏*/
    public static final String arry_game = "arry_game";
    /**精选—专题模块*/
    public static final String arry_app = "arry_app";
    /**精选—排行*/
    public static final String arry_list = "arry_list";
    /**精选——达人推荐前10个下载按扭*/
    public static final String[] arry_hold = {"arry_hold01","arry_hold02","arry_hold03","arry_hold04","arry_hold05","arry_hold06","arry_hold07","arry_hold08","arry_hold09","arry_hold10"};





    //**************** UMengAgent Method *************************
    /** 开始准备友盟统计 */
    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }
    
    /** 暂停友盟统计 */
    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }
    
    /**
     *  上报友盟统计事件
     * @param context   Context
     * @param key       对应友盟中的事件ID(一一对应)
     */
    public static void onEvent(Context context, String key) {
        Logger.d("um","um_key = "+key);
        MobclickAgent.onEvent(context, key);
    }
    
    /**
     *  上报异常信息
     */
    public static void reportError(Context context, String error) {
        MobclickAgent.reportError(context, error);
    }
    
    /**
     *   上报异常信息
     */
    public static void reportError(Context context, Throwable e) {
        MobclickAgent.reportError(context, e);
    }
    
    public static void onPageStart(String value) {
        MobclickAgent.onPageStart(value);
    }
    
    public static void onPageEnd(String value) {
        MobclickAgent.onPageEnd(value);
    }
}

