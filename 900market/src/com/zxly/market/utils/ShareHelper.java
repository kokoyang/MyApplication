/**    
 * @FileName: ShareHelper.java  
 * @Package:com.zxly.market.utils  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-14 上午10:43:14  
 * @version V1.0    
 */
package com.zxly.market.utils;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.zxly.market.R;
import com.zxly.market.entity.AppDetailInfo;

/** 
 * @ClassName: ShareHelper  
 * @Description: 社会化分享 
 * @author: fengruyi 
 * @date:2015-4-14 上午10:43:14   
 */
public class ShareHelper {
	private final String DESCRIPTOR = "com.umeng.share";
    private UMSocialService mController = UMServiceFactory
	            .getUMSocialService(DESCRIPTOR);
    private Context context;
    public ShareHelper(Context context){
    	this.context = context;
    	configPlatforms();
    }
	/**
	 * 
	 * @Title: configPlatforms  
	 * @Description: 配置分享平台参数 
	 * @param  
	 * @return void 
	 * @throws
	 */
	private void configPlatforms(){
		 // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        addQQQZonePlatform();
        addWXPlatform();
	}
	/**
	 * 
	 * @Title: addQQQZonePlatform  
	 * @Description:  添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *                image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *                要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *                用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 ) 
	 * @param  
	 * @return void 
	 * @throws
	 */
    private void addQQQZonePlatform() {
        String appId = "1104478150";
        String appKey = "e40js5ygTikcK0as";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
        
    }
    
    /**
     * 
     * @Title: addWXPlatform  
     * @Description: 添加微信平台分享 
     * @param  
     * @return void 
     * @throws
     */
    private void  addWXPlatform(){
    	 // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx6db50fdcff08b263";
        String appSecret = "f74113a9205d5f1253fdf3da62fb5b5b";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
    
    public void setShareContent(AppDetailInfo detailinfo) {
        String  shareTitle = "";//分享的标题
        String  shareContent =String.format(context.getString(R.string.share_content), detailinfo.getAppName()+detailinfo.getDownUrl());//分享的内容
        String  shareurl  = detailinfo.getDownUrl();//分享链接

        UMImage localImage = new UMImage(context,detailinfo.getIcon());
        
       // 设置微信好友分享的内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareContent+shareurl);
        weixinContent.setTitle(shareTitle);
        weixinContent.setTargetUrl(shareurl);
        weixinContent.setShareImage(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent+shareurl);
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareImage(localImage);
        circleMedia.setTargetUrl(shareurl);
        mController.setShareMedia(circleMedia);

      

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(shareContent+shareurl);
        qzone.setTargetUrl(shareurl);
        qzone.setTitle(shareTitle);
        qzone.setShareImage(localImage);
//        qzone.setShareMedia(uMusic);
        mController.setShareMedia(qzone);
        
//        QQShareContent qqShareContent = new QQShareContent();
//        qqShareContent.setShareContent(shareContent);
//        qqShareContent.setTitle(shareTitle);
//        qqShareContent.setShareMedia(localImage);
//        qqShareContent.setShareImage(localImage);
//        qqShareContent.setTargetUrl(shareurl);
//        mController.setShareMedia(qqShareContent);
        
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(shareContent);
        mController.setShareMedia(sinaContent);
   
    }
    /**
     * 
     * @Title: share  
     * @Description: 指定分享平台 
     * @param @param media 分享平台 
     * @return void 
     * @throws
     */
    public void share(SHARE_MEDIA media){
    	mController.directShare(context, media, null);
    }
    
    
}
