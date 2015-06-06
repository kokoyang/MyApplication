/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zxly.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


public class YZScrollView extends ScrollView {

	public YZScrollView(Context context) {
		super(context);
	}

	public YZScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(borderListener!=null){
			borderListener.onBottom();
		}
	}

	private OnBorderListener borderListener;
	public void setOnBorderListener(OnBorderListener l){
		borderListener = l;
	}

	/**
	 * OnBorderListener, Called when scroll to top or bottom
	 *
	 * @author Trinea 2013-5-22
	 */
	public interface OnBorderListener {

		/**
		 * Called when scroll to bottom
		 */
		public void onBottom();

		/**
		 * Called when scroll to top
		 */
		//public void onTop();
	}
}
