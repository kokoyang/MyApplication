/**    
 * @FileName: IAppIntroduceView.java  
 * @Package:com.zxly.market.model  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-14 上午9:18:30  
 * @version V1.0    
 */
package com.zxly.market.model;

/** 
 * @ClassName: 介绍接口  
 * @Description: 
 * @author: fengruyi 
 * @date:2015-4-14 上午9:18:30   
 */
public interface IAppIntroduceView {
	/**
	 * 
	 * @Title: expandIntroduceContent  
	 * @Description: 展开或关闭文字多行显示 
	 * @param @param flag 文字内容是否展开
	 * @return void 
	 * @throws
	 */
	public void expandIntroduceContent(boolean flag);
	
	public void showErrorRuquest();
	
	
}
