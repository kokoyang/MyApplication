package com.zxly.market.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zxly.market.entity.Category;
import com.zxly.market.utils.CommonUtils;
import com.zxly.market.utils.Logger;

/**
 * listview adapter快速适配
 * @author fengruyi
 * @param <T> list集合对象泛型
 *
 */
public abstract class ZXBaseAdapter<T> extends BaseAdapter{
	    Context context;
	    List<T> mlist;
	    protected ZXBaseAdapter(Context context,List<T> list) {
	        this.context = context;
			if(list==null){
				mlist = new ArrayList<T>();
			}else{
				mlist = list;
			}
			Logger.d("ZXBaseAdapter", "list size" + mlist.size());
		}
	    
	    protected ZXBaseAdapter() {
	    }

		public void add(List<T> list){
			if(CommonUtils.isEmptyList(list))return;
			mlist.addAll(list);
			notifyDataSetChanged();
		}

	    public List<T> getList(){
			return mlist;
		}

	    @Override
	    public int getCount() {
	    	return mlist==null?0:mlist.size();
	    }
	    @Override
	    public long getItemId(int position) {
	    	return position;
	    }
	    @Override
	    public Object getItem(int position) {
	    	return mlist.get(position);
	    }
	    /**
	     * 各个控件的缓存
	     */
	    public class ViewHolder{
	        public SparseArray<View> views = new SparseArray<View>();

	        /**
	         * 指定resId和类型即可获取到相应的view
	         * @param convertView
	         * @param resId
	         * @param <T>
	         * @return
	         */
	        @SuppressWarnings({ "unchecked", "hiding" })
			public <T extends View> T obtainView(View convertView, int resId){
	            View v = views.get(resId);
	            if(null == v){
	                v = convertView.findViewById(resId);
	                views.put(resId, v);
	            }
	            return (T)v;
	        }

	    }

	    /**
	     * 改方法需要子类实现，需要返回item布局的resource id
	     * @return
	     */
	    public abstract int itemLayoutRes();

	    @SuppressWarnings("unchecked")
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder;
	        if(null == convertView){
	            holder = new ViewHolder();
	            convertView = LayoutInflater.from(context).inflate(itemLayoutRes(), null);
	            convertView.setTag(holder);
	        }else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        return getView(position, convertView, parent, holder);
	    }

	    /**
	     * 使用该getView方法替换原来的getView方法，需要子类实现
	     * @param position
	     * @param convertView
	     * @param parent
	     * @param holder
	     * @return
	     */
	    public abstract View getView(int position, View convertView, ViewGroup parent, ViewHolder holder);
}
