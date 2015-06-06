package com.zxly.market.model;

import java.util.List;

import com.zxly.market.entity.SpecialInfo;

public interface ISpecialView extends BaseIterfaceView{
	
	   public void showSpecialData(List<SpecialInfo> data);
	   
	   public void showMoreSpecialData(List<SpecialInfo> data);
	   
	   public void loadMoreFail();
}
