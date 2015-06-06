package com.zxly.market.bean;

/**
 * 应用设置信息
 * @author fengruyi
 *
 */
public class AppSettingInfo {
	/** 下载完是否自动安装,默认是开启*/
	private boolean isAutoInstall = true;
	
	/** 安装完成是否删除安装包,默认是开启*/
	private boolean isInstallAndDelFile = true;
	
	/** 是否在非wifi网络下下载提醒,默认是开启*/
	private boolean isNOwifiCall = true;
	
	/** 应用新版本提醒,默认是开启*/
	private boolean isNewVersionCall = true;
	
	/** 是否开启通知栏提醒,默认是开启*/
	private boolean isOpenNotification = true;

	public boolean isAutoInstall() {
		return isAutoInstall;
	}

	public void setAutoInstall(boolean isAutoInstall) {
		this.isAutoInstall = isAutoInstall;
	}

	public boolean isInstallAndDelFile() {
		return isInstallAndDelFile;
	}

	public void setInstallAndDelFile(boolean isInstallAndDelFile) {
		this.isInstallAndDelFile = isInstallAndDelFile;
	}

	public boolean isNOwifiCall() {
		return isNOwifiCall;
	}

	public void setNOwifiCall(boolean isNOwifiCall) {
		this.isNOwifiCall = isNOwifiCall;
	}

	public boolean isNewVersionCall() {
		return isNewVersionCall;
	}

	public void setNewVersionCall(boolean isNewVersionCall) {
		this.isNewVersionCall = isNewVersionCall;
	}

	public boolean isOpenNotification() {
		return isOpenNotification;
	}

	public void setOpenNotification(boolean isOpenNotification) {
		this.isOpenNotification = isOpenNotification;
	}
	
	
}
