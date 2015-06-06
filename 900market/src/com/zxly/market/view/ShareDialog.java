package com.zxly.market.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.adapter.ShareAdapter;
import com.zxly.market.entity.AppDetailInfo;
import com.zxly.market.utils.ShareHelper;

/**
 * 分享弹出框
 * @author fengruyi
 *
 */
public class ShareDialog extends Dialog{
	private ShareHelper shareHelper;
	public ShareDialog(Context context,AppDetailInfo detailinfo) {
		super(context, R.style.customDialogStyle);
		shareHelper = new ShareHelper(context);
		shareHelper.setShareContent(detailinfo);
		View view = LayoutInflater.from(context).inflate(R.layout.share_panel, null);
		setContentView(view);//先执行此方法再设置宽度全屏，不然无效
		Window window = getWindow();
		window.setWindowAnimations(R.style.umeng_socialize_dialog_animations);
		WindowManager.LayoutParams lp = window.getAttributes();  
		lp.gravity=Gravity.BOTTOM;
		lp.width = (int) BaseApplication.mWidthPixels;
		window.setAttributes(lp); 
        setCanceledOnTouchOutside(true);
	
		GridView gridview = (GridView) view.findViewById(R.id.gv_share);
		Button btncancel = (Button) view.findViewById(R.id.btn_cancel);
	    ShareAdapter adapter = new ShareAdapter(context);
	    gridview.setAdapter(adapter);
	   
	    btncancel.setOnClickListener(new android.view.View.OnClickListener() {
			
			public void onClick(View v) {
			   dismiss();
				
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
				dismiss();
			}
		});
	   
	}
	
}
