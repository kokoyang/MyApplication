/**
B* Copyright(C)2012-2013 深圳市壹捌无限科技有限公司版权所有
* 创 建 人:   Gofeel
* 修 改 人:
* 创 建日期:  2013-7-30
* 描	   述:     标题栏
* 版 本 号:    1.0
*/ 
package com.zxly.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * 阻止Gallery滑动
 */
@SuppressWarnings("deprecation")
public class TopGallery extends Gallery {

    public TopGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TopGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopGallery(Context context) {
        super(context);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}
}
