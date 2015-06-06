/**
 * Copyright(C)2012-2013 深圳市壹捌无限科技有限公司版权所有
 * 创 建 人:yangwencai
 * 修 改 人:
 * 创 建日期:2014-8-20
 * 描	   述:
 * 版 本 号:
 */
package com.zxly.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.ListView;

/** 
 * @author Administrator
 * 
 */
public class HotListView extends ListView
{

    public HotListView(Context context) {
        super(context);
    }

    public HotListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HotListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
