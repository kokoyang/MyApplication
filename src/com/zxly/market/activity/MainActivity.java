package com.zxly.market.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zxly.market.R;
import com.zxly.market.adapter.ZXFragmentPagerAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.AppUpdateData;
import com.zxly.market.entity.UpdateInfo;
import com.zxly.market.fragment.ClassicFragment;
import com.zxly.market.fragment.DiscoveryFragment;
import com.zxly.market.fragment.SortFragment;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.http.HttpHelper.HttpCallBack;
import com.zxly.market.service.DownloadService;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.slidemenulib.SlidingMenu;
import com.zxly.market.utils.AppManager;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.DoubleClickCloseUtils;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.GjsonUtil;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.utils.ViewUtil;
import com.zxly.market.view.ComLoaingDiaglog;
import com.zxly.market.view.DrawerView;
import com.zxly.market.view.PromptDialog;

/**
 * 应用首页 viewpager加载三个分类fragment,可以左右滑动切换
 * 
 * @author fengruyi
 * 
 */
public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {
	public static final String TAG = MainActivity.class.getSimpleName();
	public static final int TAB_CLASS = 0;
	public static final int TAB_SORT = 1;
	public static final int TAB_DISCOVERY = 2;
	private ViewPager mViewpager;
	private View mViewRedDot;//下载按钮红点提示
	private View mBtnSlideMenu;// 则滑菜单按钮
	private View mBtnManager;// 管理按键
	private View mTab_1;// 精品标签区域
	private View mTab_2;// 分类标签区域
	private View mTab_3;// 发现标签区域
	private SlidingMenu mSidedrawer;// 则滑菜单布局内容
	private TextView mTvClassic;// 精品标签
	private TextView mTvSorty;// 分类标签
	private TextView mTvDiscovery;// 发现标签
	private int currentPageindex = 0;// 当前显示的fragment位置
	ClassicFragment classFragment;
	DiscoveryFragment discoveryFragment;
	SortFragment sortFragment;
	private DoubleClickCloseUtils mDoubleClickCloseUtils;// 双击返回健处理
	private PromptDialog promptDialog;
	private int ignoreVercode;// 忽略的版本号
	DownloadManager downloadmanager;
	private ComLoaingDiaglog checkDialog;
	@Override
	public int getContentViewId() {
		return R.layout.activity_main;

	}

	@Override
	public void initViewAndData() {
		mDoubleClickCloseUtils = new DoubleClickCloseUtils();
		mViewRedDot = obtainView(R.id.view_reddot);
		mTvClassic = obtainView(R.id.tv_tab_classic);
		mTvSorty = obtainView(R.id.tv_tab_sort);
		mTvDiscovery = obtainView(R.id.tv_tab_discovery);
		mViewpager = obtainView(R.id.vp_main);
		mTab_1 = obtainView(R.id.tab_1);
		mTab_2 = obtainView(R.id.tab_2);
		mTab_3 = obtainView(R.id.tab_3);
		mBtnSlideMenu = obtainView(R.id.ibtn_slidemenu);
		mBtnManager = obtainView(R.id.ibtn_download);
		mSidedrawer = new DrawerView(this).initSlidingMenu();
		 classFragment = new ClassicFragment();
		 discoveryFragment = new DiscoveryFragment();
		 sortFragment = new SortFragment();
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(classFragment);
		fragments.add(sortFragment);
		fragments.add(discoveryFragment);
		ZXFragmentPagerAdapter adapter = new ZXFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		mViewpager.setOffscreenPageLimit(2);
		mViewpager.setAdapter(adapter);
		ViewUtil.setOnClickListener(this, mBtnSlideMenu, mBtnManager,mTab_1,mTab_2,mTab_3);
		mViewpager.setOnPageChangeListener(pageChangeListener);
		showPage(currentPageindex);
		ignoreVercode = PrefsUtil.getInstance().getInt(Constant.IGNORE_VERCODE);
		checkUpdate(true);
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			int tabIndex = intent.getIntExtra(Constant.MAIN_SWITCH_TAB, 0);
			showPage(tabIndex);
		}
	}
    protected void onResume() {
    	 showRedot() ;
    	 PackageChangeReceiver.bindHandler(myHandler);
    	super.onResume();
    }
    Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case DownloadManager.LOADING:
					mViewRedDot.setVisibility(View.VISIBLE);
					break;
				case DownloadManager.SUCCESS:
					 showRedot();
					 break;
				case PackageChangeReceiver.PACKAGE_REMOVE:
					classFragment.refreshView((ApkInfo) msg.obj);
					discoveryFragment.refeshView((ApkInfo) msg.obj);
					Logger.e(TAG, "有包移除");
					break;
				case PackageChangeReceiver.PACKAGE_ADD:	
					classFragment.refreshView((ApkInfo) msg.obj);
//					classFragment.removeApkInfo((ApkInfo) msg.obj);
					discoveryFragment.refeshView((ApkInfo) msg.obj);
					Logger.e(TAG, "有包安装");
					break;
			}
		};
	};
	/**
	 * 是否要显示红点
	 */
    protected void showRedot() {
    	downloadmanager = DownloadService.getDownloadManager(this);
		downloadmanager.setHandler(myHandler);
		if(downloadmanager.getDoingTaskCount()!=0||(BaseApplication.getInstance().getNeedUpgradeAppList()!=null&&BaseApplication.getInstance().getNeedUpgradeAppList().size()!=0)){
			mViewRedDot.setVisibility(View.VISIBLE);
		}else{
			mViewRedDot.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_slidemenu:
			showSlideMenu();
			break;
		case R.id.ibtn_download:
			Intent intent = new Intent(this, ManagerActivity.class);
			startActivity(intent);
			break;
		case R.id.tab_1:
			showPage(0);
			break;
		case R.id.tab_2:
			showPage(1);
			break;
		case R.id.tab_3:
			showPage(2);
			break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		PackageChangeReceiver.unBindHandler();
		if (mSidedrawer.isMenuShowing()) {
			mSidedrawer.showContent();
		} else {
			if (mDoubleClickCloseUtils.isNeedToClose()) {
				super.onBackPressed();
				try {
					DownloadService.getDownloadManager(
							BaseApplication.getInstance())
							.stopAllDownload();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PrefsUtil.getInstance().putString(Constant.UPGRADE_LIST, null)
				.commit();
				AppManager.getAppManager().AppExit(this);//暂时不退出进程
			} else {
				
				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
			}
		}
		return;
	}

	/**
	 * 根据索引切换fragment
	 * 
	 * @param index
	 */
	private void showPage(int index) {
		if (index == currentPageindex)
			return;
		mViewpager.setCurrentItem(index);
	}

	/**
	 * 根据选择的页面切换tab标签不同样式
	 */
	private void changeTabStyle(int index) {
		switch (index) {
		case 0:
			mTvClassic.setTextAppearance(this, R.style.Main_tab_selected);
			mTvSorty.setTextAppearance(this, R.style.Main_tab_unselected);
			mTvDiscovery.setTextAppearance(this, R.style.Main_tab_unselected);
			mTvClassic.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
					R.drawable.dot_tab_selected);
			mTvSorty.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			mTvDiscovery.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			break;
		case 1:
			mTvSorty.setTextAppearance(this, R.style.Main_tab_selected);
			mTvClassic.setTextAppearance(this, R.style.Main_tab_unselected);
			mTvDiscovery.setTextAppearance(this, R.style.Main_tab_unselected);
			mTvSorty.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
					R.drawable.dot_tab_selected);
			mTvClassic.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			mTvDiscovery.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			break;
		case 2:
			mTvDiscovery.setTextAppearance(this, R.style.Main_tab_selected);
			mTvClassic.setTextAppearance(this, R.style.Main_tab_unselected);
			mTvSorty.setTextAppearance(this, R.style.Main_tab_unselected);
			mTvDiscovery.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
					R.drawable.dot_tab_selected);
			mTvSorty.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			mTvClassic.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			break;

		default:
			break;
		}
	}

	/**
	 * 显示则滑菜单
	 */
	private void showSlideMenu() {
		if (mSidedrawer.isMenuShowing()) {
			mSidedrawer.showContent();
		} else {
			mSidedrawer.showMenu();
		}
	}

	public int getCurrentPage() {
		return currentPageindex;
	}

	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int index) {
			if (index == 0) {
				mSidedrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else {
				mSidedrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
			currentPageindex = index;
			changeTabStyle(currentPageindex);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * 检测应用更新
	 * 
	 * @param ignoreNoUpdate
	 *            是否忽略显示没有最新更新提示，true则忽略
	 */
	public void checkUpdate(final boolean ignoreNoUpdate) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("packName", getPackageName());
		params.addQueryStringParameter("verCode",
				AppUtil.getAppVersionCode(this) + "");
		if(!ignoreNoUpdate){
			if(checkDialog==null){
				checkDialog = ComLoaingDiaglog.show(this, "正在检查更新...", false);
			}
			checkDialog.show();
		}
		HttpHelper.send(HttpMethod.GET, Constant.GET_VERUP, params,
				new HttpCallBack() {
					public void onSuccess(String result) {
						if(checkDialog!=null&&checkDialog.isShowing()){
							checkDialog.dismiss();
						}
						AppUpdateData data = GjsonUtil.json2Object(result,
								AppUpdateData.class);
						if (data != null && data.getStatus() == 200
								&& data.getApkList() != null) {
							int lenth = data.getApkList().size();
							if (lenth > 0) {
								UpdateInfo info = data.getApkList().get(
										lenth - 1);
								if (ignoreVercode < info.getVerCode()
										|| !ignoreNoUpdate) {
									showUpgradeDialog(info);
								}
							} else {
								if (!ignoreNoUpdate) {
									showUpgradeDialog(null);
								}
							}
						}

					}

					public void onFailure(HttpException e, String msg) {
						if(checkDialog!=null&&checkDialog.isShowing()){
							Toast.makeText(MainActivity.this, "检查失败", 0).show();
							checkDialog.dismiss();
						}
					}
				});
	}

	/**
	 * 显示版本升级dialog
	 */
	public void showUpgradeDialog(final UpdateInfo info) {
		if (promptDialog == null) {
			promptDialog = new PromptDialog(this);
			promptDialog.show(new OnClickListener() {
				public void onClick(View arg0) {
					switch (arg0.getId()) {
					case R.id.btn_ok:
						if (info != null) {
							try {
								DownloadService.getDownloadManager(
										BaseApplication.getInstance())
										.upgradeMyapp(info);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						break;
					case R.id.btn_cancle:
						if (info != null) {
							Logger.e(TAG, "忽略的版本号-->" + info.getVerCode());
							PrefsUtil
									.getInstance()
									.putInt(Constant.IGNORE_VERCODE,
											info.getVerCode()).commit();
						}
						break;
					}
					promptDialog.dismiss();
				}
			});
		} else {
			promptDialog.show();
		}
		if (info != null) {
			promptDialog.setTxt(
					getString(R.string.check_update),
					String.format(getString(R.string.upate_conten),
							info.getVerName()));
			promptDialog.setCancleButton(getString(R.string.ingore));
		} else {
			promptDialog.setTxt(getString(R.string.check_update),
					getString(R.string.no_upate));
			promptDialog.setCancleButton(getString(R.string.cancel));
		}
	}

}
