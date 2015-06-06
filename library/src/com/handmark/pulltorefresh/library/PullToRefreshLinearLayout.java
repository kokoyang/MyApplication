/* *****************************************
 *      上海市移卓网络科技有限公司
 *      网址：http://www.30.net/
 *      Copyright(C)2012-2013 
 ****************************************** */
package com.handmark.pulltorefresh.library;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @class:
 * @description:
 * @author:lanhaizhong
 * @version:v4.0
 * @date:2015年1月15日 下午1:49:57
 */
public class PullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout> {

	@Override
	public com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
		LinearLayout linearLayout = new LinearLayout(context, attrs);
		linearLayout.setId(R.id.linearlayout);
		return linearLayout;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		return false;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return true;
	}

	public PullToRefreshLinearLayout(Context context, com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
	}

	public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshLinearLayout(Context context) {
		super(context);
	}

	public PullToRefreshLinearLayout(Context context, com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
			com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle animStyle) {
		super(context, mode, animStyle);

	}
}
