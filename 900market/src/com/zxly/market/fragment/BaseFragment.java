package com.zxly.market.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{

		public Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				handleInfoMessage(msg);
			}
		};

		public abstract void handleInfoMessage(Message msg);


		private View mContentView;
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    	Bundle savedInstanceState) {
	    	mContentView = inflater.inflate(getContentViewId(), null); 
	    	initViewAndData();
	    	return mContentView;
	    }
	    
	    public abstract int getContentViewId();
	    
	    public abstract void initViewAndData();
	    
		public <T extends View> T obtainView(View convertView, int resId){
	         View v = convertView.findViewById(resId);          
	         return (T)v;
		}
		
		public <T extends View> T obtainView(int resId){
	         View v = mContentView.findViewById(resId);          
	         return (T)v;
		}


}
