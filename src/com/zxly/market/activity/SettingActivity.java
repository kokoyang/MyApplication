package com.zxly.market.activity;



import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.zxly.market.R;
import com.zxly.market.bean.AppSettingInfo;
import com.zxly.market.constans.Constant;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.utils.ViewUtil;

public class SettingActivity extends BaseActivity implements OnClickListener{
	/**下载安装设置*/
	private CheckBox mDownloadAndInstall;
	/**安装后删除包设置*/
	private CheckBox mInstallAndDelFile;
	/**非wifi下下载提醒设置*/
	private CheckBox mNowifiCall;
	/**新版本提醒设置*/
	private CheckBox mNewversionCall;
	/**打开通知栏设置*/
	private CheckBox mOpenNOtification;
	
	private AppSettingInfo mSettingInfo;
	@Override
	public int getContentViewId() {
		return R.layout.activity_setting;
	}

	@Override
	public void initViewAndData() {
		setBackTitle(R.string.setting);
		mDownloadAndInstall  = obtainView(R.id.down_and_installstatus);
		mInstallAndDelFile = obtainView(R.id.install_and_delstatus);
		mNowifiCall = obtainView(R.id.nowifistatus);
		mNewversionCall = obtainView(R.id.new_versiontatus);
		mOpenNOtification = obtainView(R.id.open_notify_status);
		mSettingInfo = PrefsUtil.getInstance().getObject(Constant.SETTING_CONFIG, AppSettingInfo.class);
		UpdateUI(mSettingInfo);
		
		ViewUtil.setOnClickListener(this, mDownloadAndInstall,mInstallAndDelFile,mNowifiCall,mNewversionCall,mOpenNOtification);
	}
	@Override
	public void onBackPressed() {
		PrefsUtil.getInstance().putObject(Constant.SETTING_CONFIG, mSettingInfo).commit();//退出时要保存
		super.onBackPressed();
	}
	/**
	 * 
	 * @param settingInfo
	 */
	private void UpdateUI(AppSettingInfo settingInfo){
		if(settingInfo == null)
			return;
		mDownloadAndInstall.setChecked(settingInfo.isAutoInstall());
		mInstallAndDelFile.setChecked(settingInfo.isInstallAndDelFile());
		mNowifiCall.setChecked(settingInfo.isNOwifiCall());
		mNewversionCall.setChecked(settingInfo.isNewVersionCall());
		mOpenNOtification.setChecked(settingInfo.isOpenNotification());
	}

	@Override
	public void onClick(View v) {
		CheckBox checkbox = (CheckBox) v;
		switch (v.getId()) {
		case R.id.down_and_installstatus:
			mSettingInfo.setAutoInstall(checkbox.isChecked());
			break;
		case R.id.install_and_delstatus:
			mSettingInfo.setInstallAndDelFile(checkbox.isChecked());
			break;
		case R.id.nowifistatus:
			mSettingInfo.setNOwifiCall(checkbox.isChecked());
			break;
		case R.id.new_versiontatus:
			mSettingInfo.setNewVersionCall(checkbox.isChecked());
			break;
		case R.id.open_notify_status:
			mSettingInfo.setOpenNotification(checkbox.isChecked());
			break;
		default:
			break;
		}
		
	}
	

}
