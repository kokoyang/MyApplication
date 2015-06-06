/**    
 * @FileName: AppDetailModel.java  
 * @Package:com.zxly.market.model  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-13 下午8:05:59  
 * @version V1.0    
 */
package com.zxly.market.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.adapter.ShareAdapter;
import com.zxly.market.utils.ShareHelper;

/** 
 * @ClassName: AppDetailModel  
 * @Description: TODO 
 * @author: 应用详情处理类 
 * @date:2015-4-13 下午8:05:59   
 */
public class AppDetailModel {
	private Context context;
	/**分享对话框*/
	private Dialog  shareDialog;
	private ShareHelper shareHelper;
    public AppDetailModel(Context cotext){
    	this.context = cotext;
    	
    }
    /**
     * 
     * @Title: showShareDialog  
     * @Description: 打开分弹框面板 
     * @param  
     * @return void 
     * @throws
     */
    public void showShareDialog(){
    	View rootview = ((Activity)context).getWindow().getDecorView();
    	if(shareDialog == null){	
    		shareHelper = new ShareHelper(context);
    		shareDialog = new Dialog(context, R.style.customDialogStyle);
    		View view = LayoutInflater.from(context).inflate(R.layout.share_panel, null);
    		shareDialog.setContentView(view);//先执行此方法再设置宽度全屏，不然无效
    		Window window = shareDialog.getWindow();
    		window.setWindowAnimations(R.style.umeng_socialize_dialog_animations);
    		WindowManager.LayoutParams lp = window.getAttributes();  
    		lp.gravity=Gravity.BOTTOM;
    		lp.width = (int) BaseApplication.mWidthPixels;
    		window.setAttributes(lp); 
    		shareDialog.setCanceledOnTouchOutside(true);
		
			GridView gridview = (GridView) view.findViewById(R.id.gv_share);
			Button btncancel = (Button) view.findViewById(R.id.btn_cancel);
		    ShareAdapter adapter = new ShareAdapter(context);
		    gridview.setAdapter(adapter);
		  
		   
		    btncancel.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					shareDialog.dismiss();
					
				}
			});
		    gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					switch (position) {
					case 0:
						shareHelper.share(SHARE_MEDIA.SINA);
						break;
					case 1:
						shareHelper.share(SHARE_MEDIA.WEIXIN);
						break;
					case 2:
						shareHelper.share(SHARE_MEDIA.WEIXIN_CIRCLE);
						break;
					case 3:
						shareHelper.share(SHARE_MEDIA.QZONE);
						break;
					default:
						break;
					}
					shareDialog.dismiss();
				}
			});
		   
    	}
    	shareDialog.show();
    }
    
    public void LoadDescriptionData(){
    	
    }
    
    public void showDescriptionData(){
    	
    }
    
}
