/**    
 * @FileName: FunctionNameEnum.java  
 * @Package:com.zxly.market.entity  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-14 下午3:38:00  
 * @version V1.0    
 */
package com.zxly.market.entity;

/** 
 * @ClassName: FunctionNameEnum  
 * @Description: 请求方法枚举 
 * @author: fengruyi 
 * @date:2015-4-14 下午3:38:00   
 */
public enum FunctionNameEnum {
	GetClassApkList,
	/**热门推荐列表*/
	GetHomeBottomList,
	/**精品类的第一个接口*/
	GetHomeTopList,
	/**应用详情*/
	GetApkDetails,
	/**评论列表*/
	GetCommentsList,
}
