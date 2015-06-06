/**    
 * @FileName: PackageUtil.java  
 * @Package:com.zxly.market.utils  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-9 下午3:18:48  
 * @version V1.0    
 */
package com.zxly.market.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/** 
 * @ClassName: PackageUtil  
 * @Description: apk安装和卸载等  
 * @author: fengruyi 
 * @date:2015-4-9 下午3:18:48   
 */
public class PackageUtil {
	
	 /**
	  * 
	  * @Title: uninstallNormal  
	  * @Description: 根据包名卸载应用  
	  * @param @param context
	  * @param @param packageName 
	  * @return void 
	  * @throws
	  */
	 public static void uninstallNormal(Context context, String packageName) {
	        if (packageName == null || packageName.length() == 0) {
	            return;
	        }

		Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(32).append("package:")
		        .append(packageName).toString()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	    }
}
