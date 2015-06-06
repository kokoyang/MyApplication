package com.zxly.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 控制字数长度的textview
 * @author Administrator
 *
 */
public class LimitLengthTextView extends TextView{
	public LimitLengthTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	public void setText(String text,int litmitlength){
		if(text==null)return;
		String newText;
		if(text.length()>=litmitlength){
			newText  = text.subSequence(0, litmitlength)+"..";
			setText(newText);
		}else{
			setText(text);
		}
		
	}
}
