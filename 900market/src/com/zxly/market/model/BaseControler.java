package com.zxly.market.model;

public class BaseControler {
	/**判断UI界面是否销毁，如果销毁controler就不会给ui呈递数据*/
	
	public boolean isFinish;

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
}
