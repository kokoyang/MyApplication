package com.zxly.market.model;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zxly.market.R;
import com.zxly.market.activity.MainActivity;

/**
 * 下载通知栏管理
 * @author Administrator
 *
 */
public class NotificationManerger {
	//通知栏  
    private NotificationManager updateNotificationManager = null;  
    private Notification updateNotification = null;  
    //通知栏跳转Intent  
    private Intent updateIntent = null;  
    private PendingIntent updatePendingIntent = null;  
    private Context context;
    public NotificationManerger(Context context){
    	this.context = context;
    }
    
    public void startDownload(){
    	this.updateNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);  
        this.updateNotification = new Notification();  
       
        //设置下载过程中，点击通知栏，回到主界面  
        updateIntent = new Intent(context, MainActivity.class);  
        updatePendingIntent = PendingIntent.getActivity(context,0,updateIntent,0);  
        //设置通知栏显示内容  
        updateNotification.icon = R.drawable.icon_logo;  
        updateNotification.tickerText = "开始下载";  
        updateNotification.setLatestEventInfo(context,"900市场","0%",updatePendingIntent);  
        //发出通知  
        updateNotificationManager.notify(0,updateNotification);
    }
    
    public void updateNotification(int prosess){
    	updateNotification.setLatestEventInfo(context, "正在下载", prosess+"%", updatePendingIntent);  
        updateNotificationManager.notify(0, updateNotification); 
    }
    
    public void completeNotification(){
    	updateNotification.flags|=updateNotification.FLAG_AUTO_CANCEL;  
        //点击安装PendingIntent  
        Uri uri = Uri.fromFile(new File(""));  
        Intent installIntent = new Intent(Intent.ACTION_VIEW);  
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");  
        updatePendingIntent = PendingIntent.getActivity(context, 0, installIntent, 0);  
           
        updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒   
        updateNotification.setLatestEventInfo(context, "900市场", "下载完成,点击安装。", updatePendingIntent);  
        updateNotificationManager.notify(0, updateNotification);  
          
    }
}
