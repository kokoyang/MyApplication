package com.zxly.market.model;

import java.util.List;

import com.zxly.market.entity.ApkInfo;

/**
 * 升级页面接口
 * @author Administrator
 *
 */
public interface IUpgradeView extends BaseIterfaceView{
    
	public void showUpgradeList(List<ApkInfo> list);

}
