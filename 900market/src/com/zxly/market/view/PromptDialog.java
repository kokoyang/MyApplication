package com.zxly.market.view;

import com.zxly.market.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 双按钮对话框
 * @author fengruyi
 *
 */
public class PromptDialog extends Dialog {
	private Button btn_ok;
	private Button btn_cancle;
	private TextView tv_title;
	private TextView tv_content;
	
	public PromptDialog(Context context) {
		super(context, R.style.customDialogStyle);
		setContentView(R.layout.prompt_dialog);
		setCanceledOnTouchOutside(false);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancle = (Button) findViewById(R.id.btn_cancle);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
	}
	
	public void setTxt(String title,String content){
		tv_title.setText(title);
		tv_content.setText(content);
	}
	public void setCancleButton(String lable){
		btn_cancle.setText(lable);
	}
    public void show(android.view.View.OnClickListener clickListener){
    	this.show();
    	btn_ok.setOnClickListener(clickListener);
    	btn_cancle.setOnClickListener(clickListener);
    }

}
