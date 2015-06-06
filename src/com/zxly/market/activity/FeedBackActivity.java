package com.zxly.market.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.R;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.BaseResponseData;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.CustomToast;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.view.ComLoaingDiaglog;
import com.zxly.market.view.InputCountter;

/**
 * 意见反馈
 * @author fengruyi
 *
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener{
	
    private InputCountter mFeedbackContent;
	private EditText mContract;
	private Button mBtnSubmit;
	private ComLoaingDiaglog dialog;
	@Override
	public int getContentViewId() {
		
		return R.layout.activity_feedback;
	}

	@Override
	public void initViewAndData() {
		setBackTitle(R.string.feedbak_lable);
		mFeedbackContent = obtainView(R.id.feedback_content);
		mContract = obtainView(R.id.et_contact_way);
		mBtnSubmit = obtainView(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(mFeedbackContent.getText().length()<=4){
			CustomToast.showToast(this, R.string.input_limit);
			return ;
		}
		saveFeedBack(mFeedbackContent.getText().toString(),mContract.getText().toString());
	}
	/**
	 * 
	 * @param content 反馈内容
	 * @param way 联系方式
	 */
	public void saveFeedBack(String content ,String way){
		AppUtil.hideSoftInput(this);
		if(dialog == null){
			dialog = ComLoaingDiaglog.show(this, getString(R.string.do_submit), false);
		}else{
			dialog.show();
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("typeid", "AppMarket2.0");
		params.addQueryStringParameter("email", way);
		params.addQueryStringParameter("contents", content);
		params.addQueryStringParameter("version", AppUtil.getAppVersionName(this));
		params.addQueryStringParameter("sdk_ver", getAndroidOSVersion());
		HttpHelper.send(HttpMethod.POST, Constant.SAVE_FEEBACK, params, new HttpCallBack() {
			
			public void onSuccess(String result) {
				BaseResponseData data = GjsonUtil.json2Object(result, BaseResponseData.class);
				if(data!=null&&data.getStatus() == 200){
					sendSuccess();
				}else{
					sendFailue();
				}
				
			}
			
			public void onFailure(HttpException e, String msg) {
				sendFailue();
				
			}
		});
	}
	public void onBackPressed() {
		AppUtil.hideSoftInput(this);
		super.onBackPressed();
	}
	/**
	 * 提交失败
	 */
	public void sendFailue(){
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		Toast.makeText(this, "提交失败", 0).show();
	}
	public void sendSuccess(){
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		Toast.makeText(this, "提交成功", 0).show();
		finish();
	}
	public  String  getAndroidOSVersion()  
    {  
         int osVersion;  
         try  
         {  
            osVersion = android.os.Build.VERSION.SDK_INT;  
         }  
         catch (NumberFormatException e)  
         {  
            osVersion = 0;  
         }  
           
         return osVersion+"";  
   }  
}
