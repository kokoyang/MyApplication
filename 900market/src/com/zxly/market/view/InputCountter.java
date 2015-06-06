package com.zxly.market.view;







import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;

/**
 * 
 * 带有文本输入计数器和删除功能的输入框
 * @author fengruyi
 *
 */
public class InputCountter extends RelativeLayout implements OnClickListener{
	private EditText mEdit;
	private TextView mTextview;
	private Button mDelete;
	private int mMaxinputCount;
	private int mBackgroundColor;
	private int mBackground;
	private int mEditPadding;
	private float mEditTextSize;
    private int mEditTextColor;
    private float mLableTextSize;
    private int mLableTextColor;
	private String mLableCounter;
	public InputCountter(Context context) {
		super(context);
	}
	@SuppressLint("NewApi")
	public InputCountter(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputCountter);
		mMaxinputCount = array.getInteger(R.styleable.InputCountter_input_max_count, 200);
		mBackgroundColor = array.getColor(R.styleable.InputCountter_input_background_color,0xffffffff);
		mBackground = array.getResourceId(R.styleable.InputCountter_input_background,0);
		mEditPadding = (int) array.getDimension(R.styleable.InputCountter_input_padding, 10);
		mEditTextSize = array.getDimension(R.styleable.InputCountter_edit_text_size, 16);
		mEditTextColor = array.getColor(R.styleable.InputCountter_edit_text_color,0xff000000);
		mLableTextSize = array.getDimension(R.styleable.InputCountter_lable_text_size, 14);
		mLableTextColor = array.getColor(R.styleable.InputCountter_lable_text_color,0xff999999);
		mEdit = new EditText(context);
		mDelete = new Button(context);
		mTextview = new TextView(context);
		mEdit.setBackgroundResource(mBackground);
		if(mBackground == 0){
			mEdit.setBackgroundColor(mBackgroundColor);
		}
		mLableCounter = "%d/"+mMaxinputCount+"字";
		mEdit.setTextColor(mEditTextColor);
		mEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX,mEditTextSize);
		mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxinputCount)});
		mEdit.setHint(array.getString(R.styleable.InputCountter_edit_hint));
		mTextview.setTextColor(mLableTextColor);
		mTextview.setTextSize(TypedValue.COMPLEX_UNIT_PX,mLableTextSize);
		mEdit.setId(1);
		mDelete.setId(2);
		RelativeLayout.LayoutParams edit_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		int size = (int) (BaseApplication.mDensity*15);
		RelativeLayout.LayoutParams btn_params = new RelativeLayout.LayoutParams(size,size);
		RelativeLayout.LayoutParams tv_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		mEdit.setGravity(Gravity.TOP);
		mEdit.setPadding(mEditPadding, mEditPadding, mEditPadding, mEditPadding+25);
	    this.addView(mEdit, edit_params);
	    btn_params.addRule(RelativeLayout.ALIGN_RIGHT, 1);
	    btn_params.setMargins(0, 0, 5, 5);
	    btn_params.addRule(RelativeLayout.ALIGN_BOTTOM, 1);
		this.addView(mDelete, btn_params);
		tv_params.addRule(RelativeLayout.LEFT_OF,2);
		tv_params.addRule(RelativeLayout.ALIGN_BOTTOM, 1);
		tv_params.setMargins(0, 0, 5, 5);
		this.addView(mTextview, tv_params);
		mDelete.setBackgroundResource(R.drawable.icon_input_delete);
		mTextview.setText(String.format(mLableCounter,mEdit.getText().length()));
		mDelete.setOnClickListener(this);
		mEdit.addTextChangedListener(mWatcher);
		array.recycle();
	}
	public InputCountter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	
	TextWatcher mWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			mTextview.setText(String.format(mLableCounter, mEdit.getText().length()));
			
		}
	};
	
	public Editable getText(){
		return mEdit.getText();
	}
	
	@Override
	public void onClick(View view) {
		mEdit.setText("");
		
	}
	public int getmMaxinputCount() {
		
		return mMaxinputCount;
	}
	public void setmMaxinputCount(int mMaxinputCount) {
		mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxinputCount)});
		this.mMaxinputCount = mMaxinputCount;
	}
	public int getmBackgroundColor() {
		return mBackgroundColor;
	}
	public void setmBackgroundColor(int mBackgroundColor) {
		mEdit.setBackgroundColor(mBackgroundColor);
		this.mBackgroundColor = mBackgroundColor;
	}
	public int getmBackground() {
		return mBackground;
	}
	public void setmBackground(int mBackground) {
		mEdit.setBackgroundResource(mBackground);
		this.mBackground = mBackground;
	}
	public int getmEditPadding() {
		return mEditPadding;
	}
	public void setmEditPadding(int mEditPadding) {
		mEdit.setPadding(mEditPadding, mEditPadding, mEditPadding, mEditPadding+25);
		this.mEditPadding = mEditPadding;
	}
	public float getmEditTextSize() {
		return mEditTextSize;
	}
	public void setmEditTextSize(float mEditTextSize) {
		mEdit.setTextSize(mEditTextSize);
		this.mEditTextSize = mEditTextSize;
	}
	public int getmEditTextColor() {
		return mEditTextColor;
	}
	public void setmEditTextColor(int mEditTextColor) {
		mEdit.setTextColor(mEditTextColor);
		this.mEditTextColor = mEditTextColor;
	}
	public float getmLableTextSize() {
		return mLableTextSize;
	}
	public void setmLableTextSize(float mLableTextSize) {
		mTextview.setTextSize(mLableTextSize);
		this.mLableTextSize = mLableTextSize;
	}
	public int getmLableTextColor() {
		return mLableTextColor;
	}
	public void setmLableTextColor(int mLableTextColor) {
		mTextview.setTextColor(mLableTextColor);
		this.mLableTextColor = mLableTextColor;
	}
}
