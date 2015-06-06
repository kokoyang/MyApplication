package com.zxly.market.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.utils.AppUtil;

public class DialogAboutUS extends Dialog{
	private TextView tv_version;
	private Button btn_ok;
	
	public DialogAboutUS(Context context) {
		super(context, R.style.customDialogStyle);
		setContentView(R.layout.dialog_about_us);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("版本："+AppUtil.getAppVersionName(context));
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new android.view.View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
