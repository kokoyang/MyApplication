package com.zxly.market.view;

import android.content.Intent;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.FeedBackActivity;
import com.zxly.market.activity.MainActivity;
import com.zxly.market.slidemenulib.SlidingMenu;
import com.zxly.market.slidemenulib.SlidingMenu.CanvasTransformer;
import com.zxly.market.utils.NetworkUtil;
import com.zxly.market.utils.ViewUtil;

/**
 * 自定义SlidingMenu 测拉菜单类
 * 
 * @author fengruyi
 * */
public class DrawerView implements OnClickListener{
	private  MainActivity activity;
	SlidingMenu localSlidingMenu;
	private TextView mBtnBack;
	private TextView mBtnFeedBack;
	private TextView mBtnCheckUpdate;
	private TextView mBtnAboutUs;
	// private TextView mBtnSetting;
	private float lastX;
	private DialogAboutUS mDialogUs;
	public DrawerView(MainActivity activity) {
		this.activity = activity;

	}

	public SlidingMenu initSlidingMenu() {
		localSlidingMenu = new SlidingMenu(activity);
		localSlidingMenu.setMode(SlidingMenu.LEFT);// 设置左右滑菜单
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_CONTENT);// 设置要使菜单滑动，触碰屏幕的范围
		// localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
		localSlidingMenu.setFadeDegree(0.35F);// SlidingMenu滑动时的渐变程度
		// localSlidingMenu.setAboveOffset(100);
		localSlidingMenu.attachToActivity(activity, SlidingMenu.LEFT);// 使SlidingMenu附加在Activity左边
		// localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
		localSlidingMenu.setMenu(R.layout.left_drawer_layout);// 设置menu的布局文件
		localSlidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {

			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		});
		initView();
		return localSlidingMenu;
	}

	private void initView() {
		mBtnBack = (TextView) activity.findViewById(R.id.tv_back);
		mBtnFeedBack = (TextView) activity.findViewById(R.id.tv_feedback);
		mBtnCheckUpdate = (TextView) activity.findViewById(R.id.tv_checkupdate);
		mBtnAboutUs = (TextView) activity.findViewById(R.id.tv_aboutus);
		mBtnBack.setText(R.string.market_name);
		ViewUtil.setOnClickListener(this, mBtnBack, mBtnAboutUs,
				mBtnCheckUpdate, mBtnFeedBack);
		// mBtnBack.setOnTouchListener(this);
		// mBtnAboutUs.setOnTouchListener(this);
		// mBtnCheckUpdate.setOnTouchListener(this);
		// mBtnFeedBack.setOnTouchListener(this);
		ViewUtil.setViewHeight(activity.findViewById(R.id.view_top),
				(int) (BaseApplication.mWidthPixels * 0.57f));// 动态设置头部背景图片比例
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			localSlidingMenu.showContent(true);
			break;
		case R.id.tv_feedback:
			Intent intent = new Intent(activity, FeedBackActivity.class);
			activity.startActivity(intent);
			break;
		case R.id.tv_checkupdate:
			if (NetworkUtil.hasNetwork()) {
				
				activity.checkUpdate(false);
			} else {
				Toast.makeText(
						activity,
						R.string.network_err, Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.tv_aboutus:
			if (mDialogUs == null) {
				mDialogUs = new DialogAboutUS(activity);
			}
			mDialogUs.show();
			break;
		default:
			break;
		}
	}
//	public boolean onTouch(View arg0, MotionEvent arg1) {
//		switch (arg1.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			lastX = arg1.getX();
//			break;
//
//		case MotionEvent.ACTION_UP:
//			Logger.e("", "lastX-arg1.getX()--->"+(lastX-arg1.getX()));
//			if(Math.abs(lastX-arg1.getX())==0){
//				switch (arg0.getId()) {
//				case R.id.tv_back:
//					localSlidingMenu.showContent(true);
//					break;
//				case R.id.tv_feedback:
//					 Intent intent = new Intent(activity,FeedBackActivity.class);
//					 activity.startActivity(intent);
//					break;
//				case R.id.tv_checkupdate:
//					activity.checkUpdate(false);
//				break;
//				case R.id.tv_aboutus:
//					if(mDialogUs==null){
//						mDialogUs = new  DialogAboutUS(activity); 
//					}
//					mDialogUs.show();
//				 break;
//				}
//			}
//			break;
//		}
//		return false;
//	}

}
