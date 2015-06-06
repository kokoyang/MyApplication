package com.zxly.market.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zxly.market.R;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.CommentInfo;
import com.zxly.market.utils.PrefsUtil;
/**
 * 
 * @author fengruyi
 *
 */
public class PublishCommentDialog extends Dialog implements android.view.View.OnClickListener{
    private RatingBar mRbGrade;//评分
    private TextView mTVGrade;//评分说明
    private EditText mEtName;
    private EditText mEtContent;
    private Button mBtnCancel;
    private Button mBtnOk;
	private SubmitCallBack callback;
	private boolean isModify;//是否显示修改
	public PublishCommentDialog(Context context) {
		super(context, R.style.customDialogStyle);
		setContentView(R.layout.dialog_publish_comment);
		init();
	}
    
	private void init(){
		mRbGrade = (RatingBar) findViewById(R.id.rb_grade);
		mTVGrade = (TextView) findViewById(R.id.tv_grade);
		mEtName = (EditText) findViewById(R.id.et_name);
		mEtContent = (EditText) findViewById(R.id.et_cotent);
		mBtnCancel = (Button) findViewById(R.id.btn_cancel);
		mBtnOk = (Button) findViewById(R.id.btn_submit);
		mBtnCancel.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);
		//默认设置
		mEtName.setHint(R.string.comment_name_hint);
		mEtName.setText(PrefsUtil.getInstance().getString(Constant.USER_NAME));
		mEtContent.setHint(R.string.comment_content_hint);
		mRbGrade.setRating(4);
		mTVGrade.setText(getContext().getString(R.string.grade_4));
		mRbGrade.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				switch ((int)arg0.getRating()) {
				case 1:
					mTVGrade.setText(getContext().getString(R.string.grade_1));
					break;
				case 2:
					mTVGrade.setText(getContext().getString(R.string.grade_2));
					break;
				case 3:
					mTVGrade.setText(getContext().getString(R.string.grade_3));
					break;
				case 4:
					mTVGrade.setText(getContext().getString(R.string.grade_4));
					break;
				case 5:
					mTVGrade.setText(getContext().getString(R.string.grade_5));
					break;
				default:
					break;
				}
				
			}
		});
	}
    
	public void changeComment(CommentInfo info){
		isModify = true;
		mBtnCancel.setVisibility(View.GONE);
		mRbGrade.setRating(info.getRank());
		mRbGrade.setProgress(info.getRank());
		mBtnOk.setText(R.string.modify);
		mEtName.setText(info.getUname());
		mEtContent.setText(info.getContent());
	}
	
	public void resetDialog(){
		isModify = false;
		mBtnCancel.setVisibility(View.VISIBLE);
		mBtnOk.setText(R.string.submit);
		mRbGrade.setProgress(4);
		mEtName.setText("");
		mEtContent.setText("");
	}
	public void setOnCallback(SubmitCallBack calback){
			this.callback = calback;
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_submit:
			String name = mEtName.getText().toString();
			if(TextUtils.isEmpty(name)){
				name = getContext().getString(R.string.default_username);
			}
			String content = mEtContent.getText().toString();
			if(TextUtils.isEmpty(content)){
				Toast.makeText(getContext(), "评论内容不能为空", 0).show();
			}else if(callback!=null){
				callback.submit(name, content, (int)mRbGrade.getRating());
			}
			break;
		}
		
	}
	
	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public interface SubmitCallBack{
		public void submit(String name,String content,int grade);
	}
}
