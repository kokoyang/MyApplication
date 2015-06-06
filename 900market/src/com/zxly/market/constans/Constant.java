package com.zxly.market.constans;
/**
 * 应用常量
 * @author fengruyi
 *
 */
public class Constant {

//	public static String HOST_URL = "http://apiqa.18guanjia.com/";
	/** 激活上报域名*/
	public static final String STAT_URL = "http://api.18guanjia.com/";

	public static String HOST_URL = "http://appstore.18guanjia.com/";
	public static final String APP_TOKEN = "y8t0a9ru6z76w4m8v5dzz2";
	public static final String SETTING_CONFIG = "setting_config";
	public static final String MAIN_SWITCH_TAB = "main_tab";//首页intent传值跳转对应的fragment
    public static final String APK_DETAIL = "apk_detail";
    public static final String APK_PACKAGE = "apk_package";
    public static final String HOT_KEY = "hot_key";
	public static final String UPGRADE_LIST = "ugrade_list";//升级列表
	public static final String IGNORE_LIST = "ignore_list";//已经忽略列表
	public static final String TOPIC_DETIAL_URL = "topic_url";//专题详情地址
	public static final String TOPIC_TITLE = "topic_title";//专题详情地址
	public static final String IGNORE_VERCODE = "ignore_vercode";//忽略版本号key
	public static final String USER_NAME = "user_name";//记录评论用户名
	public static final String UPGRADE＿LISTJSON = "upgrade_json";//升级要上传的本地应用信息参数
	public static final int MESSAGE_SUCCESS = 0;
	public static final int MESSAGE_FAILED = 1;
	public static final int MESSAGE_NODATD = 2;

	/**分类主页url*/
	public static final String CATAGORY_URL = HOST_URL+"AppMarket/GetClassNameList?";

	/**分类子项url*/
	public static final String CATAGORY_SUB_URL = HOST_URL+"AppMarket/GetClassApkList?";

	/**每页应用数*/
	public static final int ONE_PAGE_COUNT = 20;
	/**获取评论列表*/
	public static final String GET_COMMENTS =HOST_URL+"Package/GetCommentsList";
	/**获取热门关键字*/
	public static final String GET_HOTKEYS = HOST_URL +"appmarket/GetTopHitSearchList";
	/**根据关键字搜索列表*/
	public static final String GET_APPS_BY_KEY = HOST_URL +"appmarket/GetSearchApkList";
	/**获取应用详情*/
    public static final String GET_APP_DETAILS = HOST_URL +"appmarket/GetApkDetails";
    /**获取精品前部分数据*/
    public static final String GET_HOMETOP  =  HOST_URL +"AppMarket/GetHomeTopList";
    /**获取精品后部分数据*/
    public static final String GET_HOMEBOTTOM = HOST_URL +"AppMarket/GetHomeBottomList";
    /**保存评论*/
    public static final String SAVE_COMMENT = HOST_URL +"Package/SaveComments";
    /**反馈意见*/
    public static final String SAVE_FEEBACK = HOST_URL +"Feedback/SaveFeedBack";
    /**获取升级应用列表*/
    public static final String GET_UPGRADEAPP = HOST_URL + "AppMarket/GetApkUpgradeList";
	/**自升级*/
	public static final String GET_VERUP = HOST_URL +"/VerUp/GetVerUp";
	/**专题列表*/
	public static final String TOPIC_APP_LIST_URL = HOST_URL +"AppMarket/GetSpecialList?";
	/**发现webview url*/
	public static final String DIS_WEBVIEW_URL = HOST_URL +"appmarket/GetAdvertList?";
	/**专题列表*/
	public static final String GET_SPECIAL_LIST = HOST_URL +"AppMarket/GetSpecialList";

	/**激活上报*/
	public static final String ACTIVATE_URL = STAT_URL +"Stat/ActiveStat?";
	/**统计上报*/
	public static final String STATISTICS_URL = STAT_URL +"Stat/WapStatistics?";


}
