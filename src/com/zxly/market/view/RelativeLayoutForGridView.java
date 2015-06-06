package com.zxly.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.adapter.RecomandGridViewAppAdapter;
import com.zxly.market.adapter.RecomandGridViewAppAdapter.GridviewHolder;
import com.zxly.market.adapter.RelativelayoutBaseAdaper;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.utils.Logger;
/**
 * 仿gridview的RelativeLayout
 * @author fengruyi
 *
 */
public class RelativeLayoutForGridView extends RelativeLayout{
	private RecomandGridViewAppAdapter adapter;
	private OnGridviewItemClickListener onItemClickListener;
	private   int MARGIN_TOP = (int) (BaseApplication.mHeightPixels*0.025);
	private   int MARGIN_LEFT ; // (int) (BaseApplication.mWidthPixels*0.048);
	public RelativeLayoutForGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		MARGIN_LEFT = (int) ((BaseApplication.mWidthPixels -getResources().getDimensionPixelSize(R.dimen.title_bar_padding)*2-getResources().getDimensionPixelSize(R.dimen.grid_app_btn_width)*4)/3)-8;
	}
	public void setAdapter(RecomandGridViewAppAdapter adapter) {
	    this.adapter = adapter;
	    // setAdapter 时添加 view
	    bindView();
	  }

	  public void setOnItemClickListener(OnGridviewItemClickListener onItemClickListener) {
	    this.onItemClickListener = onItemClickListener;

	  }
	    
	  /**
	   * 绑定 adapter 中所有的 view
	   */
	  private void bindView() {
		Logger.e("", "bindView-->");
	    int viewLength = getChildCount();
	    int adapterCount = adapter.getCount();
	    if (adapter == null||adapterCount==0) {
	      return;
	    }
	   
	    if(viewLength==0){
		    for (int i = 0; i < adapterCount; i++) {
		      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		      final View v = adapter.getView(i);
		      v.setId(i+1);
		      final int tmp = i;
		      final Object obj = adapter.getItem(i);
		      // view 点击事件触发时回调我们自己的接口
		      v.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          if (onItemClickListener != null) {
		            onItemClickListener.onItemClicked(tmp);
		          }
		        }
		      });
		      if(i==0){
		    	 lp.setMargins(0, MARGIN_TOP, 0, 0);
		      }else if(i!=0&&i%4!=0){
		    	  lp.setMargins(MARGIN_LEFT, MARGIN_TOP, 0, 0);
		    	 lp.addRule(RelativeLayout.RIGHT_OF, i);
		    	 if(i>4){
		    		 lp.addRule(RelativeLayout.BELOW, i-3);
		    	 }
		      }else if(i!=0&&i%4==0){
		    	 lp.setMargins(0, MARGIN_TOP, 0, 0);
		    	 lp.addRule(RelativeLayout.BELOW, i-3);
		      }
		      addView(v,lp);
		    }
	    }else  if(adapterCount<viewLength){//表示要删除一些子view
	    	for(int i =1;i<=viewLength-adapterCount;i++){
	    		removeViewAt(viewLength - i);
	    	}
	    	refreshView(adapterCount);
	    }else if(adapterCount >=viewLength){//要增加view
	    	Logger.e("", "增加view");
	    	  refreshView(viewLength);
	    	  for (int i = viewLength ; i < adapterCount; i++) { 
			      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			      lp.setMargins(MARGIN_LEFT,MARGIN_TOP, 0, 0);
			      final View v = adapter.getView(i);
			      v.setId(i+1);
			      final int tmp = i;
			      // view 点击事件触发时回调我们自己的接口
			      v.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			          if (onItemClickListener != null) {
			            onItemClickListener.onItemClicked(tmp);
			          }
			        }
			      });
			      if(i==0){
				    	 lp.setMargins(0, MARGIN_TOP, 0, 0);
			      }else if(i!=0&&i%4!=0){
			    	  lp.setMargins(MARGIN_LEFT, MARGIN_TOP, 0, 0);
			    	 lp.addRule(RelativeLayout.RIGHT_OF, i);
			    	 if(i>4){
			    		 lp.addRule(RelativeLayout.BELOW, i-3);
			    	 }
			      }else if(i!=0&&i%4==0){
			    	 lp.setMargins(0, MARGIN_TOP, 0, 0);
			    	 lp.addRule(RelativeLayout.BELOW, i-3);
			      }
			      addView(v,lp);
			    }
	    }
	  }
	  /**
	   * 如果此apk已经安装，未安装列表数量超过8个，则从列表中移除，再补充一个未安装的
	   * @param apkInfo
	   */
	  public void removeItem(ApkInfo apkInfo){
		 if(adapter.removeItem(apkInfo)){
			 Logger.e("", "要移除一个应用");
			 refreshView(adapter.mList.size());
		 }
	  }
	  
	  /**
	   * 从其他页面返回要执行此方法来刷新状态
	   */
	  public void notifyDataChanged(){
		  GridviewHolder holder;
		  for(int i = 0 ,length = getChildCount();i<length;i++){
			  Logger.e("", "刷新UI---->");
			  holder =  (GridviewHolder) getChildAt(i).getTag();
			 // holder.refresh();
			  holder.refreshHander();
		  }
	  }
	  
	  /**
	   * 刷新gridview数据
	   * @param count 刷新长度
	   */
	  public void refreshView(int count){
		  GridviewHolder holder;
		  for (int i = 0; i < count; i++) {
			  holder =  (GridviewHolder) getChildAt(i).getTag();
			  holder.update((ApkInfo) adapter.getItem(i));
		  }
	  }
	  
	 /**
	   * 
	   * 回调接口
	   */
	  public interface OnGridviewItemClickListener {
	    /**
	     * 
	     * @param position
	     *			点击位置的 index
	     */
	    public void onItemClicked(int position);
	  }
}
