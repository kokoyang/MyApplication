package com.zxly.market.utils;

import android.os.Handler;
import android.os.Looper;
/**
 * 快速双击关闭应用
 * @author fengruyi
 *
 */
public class DoubleClickCloseUtils {
	/**
	 * 使用例子 onbackpress(){
	 * if(mDoubleClickCloseUtils.isNeedToClose()){
				AppManager.getAppManager().AppExit(this);
		    }else{
				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();;
			}
	 * }
	 */
	private Handler mHandler;
	
	private boolean isBacking = false ;
	
	public DoubleClickCloseUtils(){
		mHandler = new Handler(Looper.getMainLooper());
		
	}
	public boolean isNeedToClose(){
		if(isBacking){
			mHandler.removeCallbacks(onBackTimeRunnable);
			return true;
		}else{
			isBacking = true ;
			mHandler.postDelayed(onBackTimeRunnable, 2000);
			return false;
		}
	}
	
	private Runnable onBackTimeRunnable = new Runnable() {
			
			@Override
			public void run() {
				isBacking = false;
			}
		};
}
