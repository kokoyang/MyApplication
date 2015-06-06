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
import android.view.View;
import android.widget.GridView;

/** 
 * @author Administrator
 * 
 */
public class SortGridView extends GridView
{

    public SortGridView(Context context) {
        super(context);
    }

    public SortGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    
}
