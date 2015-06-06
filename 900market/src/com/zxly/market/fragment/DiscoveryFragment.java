package com.zxly.market.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefreshview.PullToRefreshBase;
import com.handmark.pulltorefreshview.PullToRefreshListView;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxly.market.R;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.HotSearchActivity;
import com.zxly.market.activity.MainActivity;
import com.zxly.market.adapter.CommonListAPPAdapter;
import com.zxly.market.adapter.DiscorySearchAdapter;
import com.zxly.market.bean.DownloadRequestCallBack_singleButton;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.js.WebViewSetting;
import com.zxly.market.model.DiscoveryFragmentModel;
import com.zxly.market.service.DownloadService;
import com.zxly.market.service.PackageChangeReceiver;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.CommonUtils;
import com.zxly.market.utils.DownloadManager;
import com.zxly.market.utils.JsonUtils;
import com.zxly.market.utils.Logger;
import com.zxly.market.utils.MarketReceiver;
import com.zxly.market.utils.NetworkUtil;
import com.zxly.market.utils.PrefsUtil;
import com.zxly.market.utils.RandomUtil;
import com.zxly.market.utils.DownloadManager.ManagerCallBack;
import com.zxly.market.view.CommenLoadingView;
import com.zxly.market.view.SortGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 发现页面 
 * @author fengruyi
 *
 */
public class DiscoveryFragment extends BaseFragment implements SensorEventListener,
		AdapterView.OnItemClickListener ,AbsListView.OnScrollListener,
		View.OnClickListener,MarketReceiver.NetChangeObserver,PullToRefreshBase.OnRefreshListener {


	private RelativeLayout shakeLayout,shakeResult;
	private TextView shakeCountTv;
	private Button shakeBottomTv;
	private ImageView shakeProgress;
	private ImageView shakeResultImg;
	private TextView shakeResultName,shakeResultDetail;
	private DiscoveryFragmentModel mode;
	private AnimationDrawable animationDrawable;
	private SensorManager sensorManager;
	private Sensor sensorObject;
	private Vibrator vibrator;
	private long lastUpdateTime = 0;
	private boolean isResult = false;// 是否有摇到结果
	private boolean isShaking = false;// 是否正在摇
	private boolean noShakeData = false;// 是否后台没有数据
	private float lastX,lastY,lastZ;
	// 速度阈值，当摇晃速度达到这值后产生作用
	private static final int SPEED_SHRESHOLD = 2000;
	// 两次检测的时间间隔
	private static final int UPTATE_INTERVAL_TIME = 100;
	private static final String LEFT_SHAKE_DATA = "LEFT_SHAKE_DATA";
	private static final String LEFT_SHAKE_COUNT = "LEFT_SHAKE_COUNT";
	private static final String SHAKE_LAST_INFO_KEY = "SHAKE_LAST_INFO_KEY";
	private static final String SHAKE_LAST_INFO_TIME= "SHAKE_LAST_INFO_TIME";
	public static final int SHAKE_DATA = 0;
	public static final int GRID_DATA = 1;
	public static final int LIST_DATA1 = 21;
	public static final int LIST_DATA2 = 22;
	public static final int WEB_DATA = 3;



	private SortGridView allSearchGrid;
	private DiscorySearchAdapter discorySearchAdapter;

	private PullToRefreshListView hotListView;
	private CommonListAPPAdapter hotListAdapter;
	private int currentPage = 1;
	private int recordCount;
	private RelativeLayout mHeaderView;
	private View mFooterView;
	private TextView footerText;
	private ProgressBar progressBar;
	private int lastVisibleConut;
	private ApkInfo shakeInfo;
	private TextView searchBar;
	private WebView webView;
	private CommenLoadingView loadingView;
	private DownloadManager downloadmanager;
	private DownLoadTaskInfo taskInfo;//当前摇出app的下载任务
	@Override
	public int getContentViewId() {
		return R.layout.discovery_fragment_layout;
	}

	@Override
	public void initViewAndData() {
		downloadmanager = DownloadService.getDownloadManager(BaseApplication.getInstance());
		if(CommonUtils.isEmptyList(BaseApplication.shakeData)){
			Logger.d("dis","new sha");
		}
		currentPage =1;
		searchBar = obtainView(R.id.search_bar);
	    String key = PrefsUtil.getInstance().getString(Constant.HOT_KEY);
		if(!TextUtils.isEmpty(key)){
			searchBar.setText(getString(R.string.hot_search)+key);
		}else{
			searchBar.setText("");
		}
		searchBar.setOnClickListener(this);
		hotListView = obtainView(R.id.lv_hot);
		mHeaderView = (RelativeLayout) View.inflate(getActivity(), R.layout.discovery_fragment_head, null);
		mFooterView = (RelativeLayout) View.inflate(getActivity(), R.layout.loadmore_foot_, null);
		mFooterView.setVisibility(View.GONE);
		loadingView = obtainView(R.id.loading_view);
		footerText = (TextView) mFooterView.findViewById(R.id.message_);
		progressBar = (ProgressBar) mFooterView.findViewById(R.id.progressbar_);
		hotListView.getRefreshableView().addHeaderView(mHeaderView);
		hotListView.getRefreshableView().addFooterView(mFooterView);
		hotListView.setOnRefreshListener(this);
		hotListView.setOnItemClickListener(this);
		hotListView.setOnScrollListener(this);
		hotListAdapter = new CommonListAPPAdapter(getActivity(),null);
		hotListView.setAdapter(hotListAdapter);
		MarketReceiver.registerObserver(this);
		initHeaderView();
		registerShake();
	}

	private void registerShake() {

		vibrator = (Vibrator)getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
		sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
		if(sensorManager!=null){
			sensorObject = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		if (sensorObject != null) {
			sensorManager.registerListener(this, sensorObject, SensorManager.SENSOR_DELAY_FASTEST);
		}
		if(!isShakeDate()){
			String lastInfo = PrefsUtil.getInstance().getString(SHAKE_LAST_INFO_KEY);
			shakeInfo = JsonUtils.fromJson(lastInfo, ApkInfo.class);
			ImageLoader.getInstance().displayImage(shakeInfo.getIcon(), shakeResultImg);
			shakeResultName.setText(shakeInfo.getAppName());
			shakeResultDetail.setText(Html.fromHtml(shakeInfo.getContent()).toString());
			stopDownload();
			changeShakeLayout(true);
			shakeCountTv.setText(R.string.discovery_shake_over);
		}
	}
	
	/** 界面切换 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser) {
			if (null != hotListAdapter && hotListAdapter.getCount()==0) {
				loadDiscoveryData();
			}
			if (null != hotListAdapter) {
				hotListAdapter.notifyDataSetChanged();
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	private boolean isShakeDate() {
		int oldDate = PrefsUtil.getInstance().getInt(SHAKE_LAST_INFO_TIME,0);
		return !(oldDate!=0 && Calendar.getInstance().get(Calendar.DATE)==oldDate);
	}

	private boolean isToDay() {
		int oldDate = PrefsUtil.getInstance().getInt(LEFT_SHAKE_DATA,0);
		return (oldDate!=0 && Calendar.getInstance().get(Calendar.DATE)==oldDate);
	}

	private void initHeaderView() {
		shakeLayout = (RelativeLayout) mHeaderView.findViewById(R.id.discovery_shake_layout);
		shakeLayout.setOnClickListener(this);
		shakeCountTv = (TextView) mHeaderView.findViewById(R.id.discovery_shake_count);
		shakeBottomTv = (Button) mHeaderView.findViewById(R.id.discovery_shake_bottom_text);
		shakeProgress = (ImageView) mHeaderView.findViewById(R.id.discovery_pb_shake);
		shakeResult = (RelativeLayout) mHeaderView.findViewById(R.id.discovery_shake_result);
		shakeResultImg = (ImageView) mHeaderView.findViewById(R.id.discovery_shake_result_img);
		shakeResultName = (TextView) mHeaderView.findViewById(R.id.discovery_shake_result_name);
		shakeResultDetail = (TextView) mHeaderView.findViewById(R.id.discovery_shake_result_detail);


		allSearchGrid = (SortGridView) mHeaderView.findViewById(R.id.discovery_all_search_grid);
		allSearchGrid.setOnItemClickListener(this);
		shakeBottomTv.setOnClickListener(this);
		webView = (WebView) mHeaderView.findViewById(R.id.web_ad);
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE,
				null); 
		new WebViewSetting().settings(getActivity(), webView);
	}


	private void loadDiscoveryData() {
		loadingView.showLoadingView();
		if(mode==null){
			mode = new DiscoveryFragmentModel(this);
		}
		currentPage = 1;
		mode.loadDiscoveryData();
	}

	private void loadDiscoveryHotGameData(){
		if(mode==null){
			mode = new DiscoveryFragmentModel(this);
		}
		mode.loadDiscoveryHotGameData(currentPage);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void handleInfoMessage(Message msg) {
		if(getActivity()==null|| getActivity().isFinishing())return;
		switch (msg.what){
			case Constant.MESSAGE_SUCCESS:
				if(msg.arg1==DiscoveryFragment.SHAKE_DATA){
					List<ApkInfo> data = (List<ApkInfo>)msg.obj;
					Logger.d("disc","shakeData size = "+data.size());
					int length = data.size()<5?data.size():5;
					if(isToDay()){// 为当天时，检查一下有没有摇过
						int leftCount = PrefsUtil.getInstance().getInt(LEFT_SHAKE_COUNT, -1);
						Logger.d("disc","shakeData leftCount = "+leftCount);
						if(leftCount!=-1 && leftCount!=0){
							if(leftCount<length){
								length = leftCount;
								Logger.d("disc","shakeData length = "+length);
							}
						}
					}
					int[] idx = RandomUtil.getLotteryArray(0, data.size() - 1, length);
					List<ApkInfo> shakeData= new ArrayList<ApkInfo>();
					if(idx!=null){
						for (int i=0;i<idx.length;i++) {
							shakeData.add(data.get(idx[i]));
						}
					}
					if(!CommonUtils.isEmptyList(shakeData)){
						if(BaseApplication.shakeData.size()==0){
							BaseApplication.shakeData.addAll(shakeData);
						}
						if(isShakeDate()){
							changeShakeLayout(false);
						}
					}
				}else if(msg.arg1==DiscoveryFragment.GRID_DATA){
					discorySearchAdapter = new DiscorySearchAdapter((List<ApkInfo>)msg.obj);
					allSearchGrid.setAdapter(discorySearchAdapter);
				}else if(msg.arg1==DiscoveryFragment.LIST_DATA1){
					hotListAdapter.clear();
					hotListAdapter.add((List<ApkInfo>) msg.obj);
					recordCount = msg.arg2;
					mFooterView.setVisibility(View.GONE);
					loadingView.hide();
				}else if(msg.arg1==DiscoveryFragment.LIST_DATA2){
					hotListAdapter.add((List<ApkInfo>) msg.obj);
					recordCount = msg.arg2;
					mFooterView.setVisibility(View.GONE);

					loadingView.hide();
				}else if(msg.arg1==DiscoveryFragment.WEB_DATA){
					webView.loadUrl((String)msg.obj);
				}
				searchBar.setVisibility(View.VISIBLE);
				break;
			case Constant.MESSAGE_NODATD:
			case Constant.MESSAGE_FAILED:
				if(msg.arg1==DiscoveryFragment.SHAKE_DATA){
					noShakeData = true;
				}
				if(hotListAdapter.getCount()==0){
					loadingView.showNoNetView();
					mFooterView.setVisibility(View.GONE);
					searchBar.setVisibility(View.GONE);
				}
				break;
		}
		if(hotListView.isRefreshing()){
			hotListView.onRefreshComplete();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.d("disc", "disc onStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Logger.d("disc", "disc onStop");
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.e("disc", "disc onResume");
		refreshView();
		if (sensorObject != null) {
			sensorManager.registerListener(this, sensorObject, SensorManager.SENSOR_DELAY_FASTEST);
		}
	}
   /**
	 * 刷新listview
	 */
	public void refreshView() {
		refreshDownloadButton(shakeInfo);
		if(hotListAdapter!=null){	
			hotListAdapter.notifyDataSetChanged();
		}
	}
	
	public void refeshView(ApkInfo info){
		if(shakeInfo!=null&&shakeInfo == info){
			refreshDownloadButton(shakeInfo);
		}
		if(hotListAdapter!=null){	
			hotListAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Logger.d("disc", "disc onPause");
		if(sensorManager!=null){
			sensorManager.unregisterListener(this);
		}
		if(taskInfo!=null&&taskInfo.getHandler()!=null&&taskInfo.getHandler().getRequestCallBack()!=null){
			DownloadManager.ManagerCallBack callback = (ManagerCallBack) taskInfo.getHandler().getRequestCallBack();
			callback.setBaseCallBack(null);
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastVisibleConut = firstVisibleItem + visibleItemCount - hotListView.getRefreshableView().getHeaderViewsCount();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int adapterSize = hotListAdapter.getCount();
		if(adapterSize==0)return;
		Logger.d("disc", "adapterSize=" + adapterSize + ",lastVisibleConut=" + lastVisibleConut + ",currentPage=" + currentPage);
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleConut >= adapterSize
				&& mFooterView.getVisibility() == View.GONE) {
			mFooterView.setVisibility(View.VISIBLE);
			if(adapterSize < recordCount){
				currentPage ++;
				footerText.setText(R.string.load_more);
				footerText.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				loadDiscoveryHotGameData();
			}else{
				footerText.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
			}
		}
	}


	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {

		int sensorType = event.sensor.getType();
		if (sensorType == Sensor.TYPE_ACCELEROMETER  && ((MainActivity)getActivity()).getCurrentPage()==2){
			// 现在检测时间
			long currentUpdateTime = System.currentTimeMillis();
			// 两次检测的时间间隔
			long timeInterval = currentUpdateTime - lastUpdateTime;
			// 判断是否达到了检测时间间隔
			if (timeInterval < UPTATE_INTERVAL_TIME)
				return;
			// 现在的时间变成last时间
			lastUpdateTime = currentUpdateTime;

			// 获得x,y,z坐标
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];

			// 获得x,y,z的变化值
			float deltaX = x - lastX;
			float deltaY = y - lastY;
			float deltaZ = z - lastZ;

			// 将现在的坐标变成last坐标
			lastX = x;
			lastY = y;
			lastZ = z;

			double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
					* deltaZ)
					/ timeInterval * 10000;
			if(speed >= SPEED_SHRESHOLD && CommonUtils.isEmptyList(BaseApplication.shakeData)){
				if(noShakeData){
					stopDownload();
					setNoShakeData();
				}
				return;
			}
			// 达到速度阀值，发出提示
			if (speed >= SPEED_SHRESHOLD && !isShaking && isShakeDate()) {
				stopDownload();
				vibrator.vibrate( new long[]{500,200,500,200}, -1);
				hotListView.getRefreshableView().setSelectionFromTop(0,0);
				isShaking = true;
				changeShakeLayout(false);
				Logger.d("disco", "onSensorChanged on shake");
				shakeProgress.setBackgroundResource(R.anim.shake);
				animationDrawable = (AnimationDrawable) shakeProgress.getBackground();
				animationDrawable.start();
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						shakeInfo = BaseApplication.shakeData.removeFirst();
						PrefsUtil.getInstance().putInt(LEFT_SHAKE_COUNT, BaseApplication.shakeData.size()).commit();
						PrefsUtil.getInstance().putInt(LEFT_SHAKE_DATA, Calendar.getInstance().get(Calendar.DATE)).commit();
						ImageLoader.getInstance().displayImage(shakeInfo.getIcon(), shakeResultImg);
						shakeResultName.setText(shakeInfo.getAppName());
						shakeResultDetail.setText(Html.fromHtml(shakeInfo.getContent()).toString());
						changeShakeLayout(true);
						if(BaseApplication.shakeData.size()==0){
							shakeCountTv.setText(R.string.discovery_shake_over);
							PrefsUtil.getInstance().putInt(SHAKE_LAST_INFO_TIME, Calendar.getInstance().get(Calendar.DATE)).commit();
							PrefsUtil.getInstance().putString(SHAKE_LAST_INFO_KEY, JsonUtils.toJson(shakeInfo,false)).commit();
						}
						animationDrawable.stop();
						vibrator.cancel();
						shakeProgress.setBackgroundResource(R.drawable.shake_progress_default);
						isShaking = false;
					}
				},1600);
			}
		}
	}

	private void stopDownload() {
		if(shakeCountTv.getText().toString().equals(getString(R.string.discovery_shake_over))){
			return;
		}
		if(taskInfo!=null&&taskInfo.getHandler()!=null&&taskInfo.getHandler().getRequestCallBack()!=null){
			DownloadManager.ManagerCallBack callback = (ManagerCallBack) taskInfo.getHandler().getRequestCallBack();
			callback.setBaseCallBack(null);

		}
		shakeBottomTv.setTag(R.string.download);
	}

	@SuppressLint("ResourceAsColor")
	private void changeShakeLayout(boolean isResult) {
		this.isResult = isResult;
		if(isResult){
			refreshDownloadButton(shakeInfo);
			shakeProgress.setVisibility(View.GONE);
			shakeResult.setVisibility(View.VISIBLE);
			shakeLayout.setBackgroundResource(R.drawable.discovery_shake_result_bg);
			setShakeCountText();
			shakeBottomTv.setText(R.string.download);
			shakeBottomTv.setTextColor(getResources().getColor(R.color.color_235a00));
			shakeBottomTv.setBackgroundResource(R.drawable.discovery_shake_app_down_bt);

		}else{
			shakeProgress.setVisibility(View.VISIBLE);
			shakeResult.setVisibility(View.GONE);
			shakeLayout.setBackgroundResource(R.drawable.discovery_shake_bg);
			setShakeCountText();
			shakeBottomTv.setText(R.string.discovery_shake_bottom_text);
			shakeBottomTv.setTextColor(getResources().getColor(R.color.color_fffc00));
			shakeBottomTv.setBackgroundResource(0);
		}
	}

	private ForegroundColorSpan blueSpan = new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(R.color.color_fffc00));
	private void setShakeCountText() {
		String first = getString(R.string.discovery_shake_right_text);
		String count = BaseApplication.shakeData.size()+"";
		String last = getString(R.string.discovery_shake_count);
		StringBuffer buffer = new StringBuffer();
		buffer.append(first).append(count).append(last);
		SpannableStringBuilder builder = new SpannableStringBuilder(buffer.toString());
		int dealIndex = buffer.toString().indexOf(count);
		builder.setSpan(blueSpan, dealIndex, dealIndex + count.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		shakeCountTv.setText(builder);
	}


	private void setNoShakeData(){
		this.isResult = false;
		shakeProgress.setVisibility(View.GONE);
		shakeResult.setVisibility(View.GONE);
		shakeLayout.setBackgroundResource(R.drawable.discovery_shake_nodata_bg);
		shakeCountTv.setText(R.string.discovery_shake_over);
		shakeBottomTv.setText(R.string.discovery_shake_bottom_nodata_text);
		shakeBottomTv.setTextColor(getResources().getColor(R.color.color_white));
		shakeBottomTv.setBackgroundResource(0);
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		if(adapterView.getId()==allSearchGrid.getId()){
			if(discorySearchAdapter!=null){
				ApkInfo info = (ApkInfo)discorySearchAdapter.getItem(i);
				toAppDetail(info);
			}
		}else{
			ApkInfo info = (ApkInfo)hotListAdapter.getItem(i-2);
			toAppDetail(info);
		}

	}

	private void toAppDetail(ApkInfo info) {
		Intent intent = new Intent(getActivity(),AppDetailActivity.class);
		intent.putExtra(Constant.APK_DETAIL, info.getDetailUrl());
		intent.putExtra(Constant.APK_PACKAGE, info.getPackName());
		startActivity(intent);
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.discovery_shake_layout:
			if(shakeInfo!=null && isResult){
				toAppDetail(shakeInfo);
			}
			break;

		case R.id.search_bar :
			Intent searchIntent = new Intent(getActivity(),HotSearchActivity.class);
			startActivity(searchIntent);
			break;
		case  R.id.btn_net_setting:
			NetworkUtil.enterNetWorkSetting(getActivity());
			break;
		case R.id.discovery_shake_bottom_text:
			if(isShaking || !isResult){
				break;
			}
			if(view.getTag()==null)return;
			switch ((int)view.getTag()) {
			case R.string.waiting:
			case R.string.stop://暂停任务
				try {
					downloadmanager.stopDownload(taskInfo);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.string.open:
				AppUtil.startApk(taskInfo);
				break;
			case R.string.install:
				AppUtil.installApk(getActivity(), taskInfo);
				break;
			case R.string.resume:
			case R.string.retry:
				try {
					downloadmanager.resumeDownload(taskInfo, new DownloadRequestCallBack_singleButton());
					taskInfo.getHandler().getRequestCallBack().setUserTag(shakeBottomTv);
				} catch (DbException e) {
					e.printStackTrace();
				}
				break;
			case R.string.update:
			case R.string.download:
				try {
					downloadmanager.addNewDownload(shakeInfo,new DownloadRequestCallBack_singleButton());
					taskInfo = downloadmanager.getTask(shakeInfo.getPackName());
					if(taskInfo!=null&&taskInfo.getHandler()!=null){
						taskInfo.getHandler().getRequestCallBack().setUserTag(shakeBottomTv);
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			break;
		}

	}
	/**
	 * 根据下载任务状态来更新下载按钮
	 * @param packageName
	 */
    public void refreshDownloadButton(ApkInfo apkInfo){
    	if(apkInfo==null)return;
		setshakeBottomText(R.string.download);
    	shakeBottomTv.setTag(R.string.download);
    	taskInfo = downloadmanager.getTask(apkInfo.getPackName());
    	if(taskInfo!=null){
    		 switch (taskInfo.getState()) {
    	        case STARTED:
    			case WAITING:
    				setshakeBottomText(R.string.waiting);
    				shakeBottomTv.setTag(R.string.waiting);
    				break;
    			case LOADING:
					
    				shakeBottomTv.setTag(R.string.stop);
    				break;
    			case SUCCESS:
    				if(AppUtil.isAppInstalled(getActivity(), taskInfo.getPackageName())){//存在下载任务中并且已经安装
						setshakeBottomText(R.string.open);
    					shakeBottomTv.setTag(R.string.open);
                	}else{
	                	File file = new File(taskInfo.getFileSavePath());
	                	if(file.exists()){
							setshakeBottomText(R.string.install);
	                		shakeBottomTv.setTag(R.string.install);
	                	}else{
	                		try {
	                			downloadmanager.removeDownload(taskInfo);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							setshakeBottomText(R.string.download);
	                		shakeBottomTv.setTag(R.string.download);
	                	}
                	}
    				break;
    			case FAILURE:
					setshakeBottomText(R.string.retry);
    				shakeBottomTv.setTag(R.string.retry);
    				break;
    			case CANCELLED:
					setshakeBottomText(R.string.resume);
    				shakeBottomTv.setTag(R.string.resume);
    				break;
    		 	}
    		 HttpHandler<File> handler = taskInfo.getHandler();  
		        if (handler != null) {
		            RequestCallBack callBack = handler.getRequestCallBack();
		            if (callBack instanceof DownloadManager.ManagerCallBack) {
		                DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
		                managerCallBack.setBaseCallBack(new DownloadRequestCallBack_singleButton());  
		            }
		            callBack.setUserTag(shakeBottomTv);
		        }
	   }else{
	    	ApkInfo tempInfo = BaseApplication.getInstance().getInstalledApk(apkInfo.getPackName());
			if(tempInfo!=null){//表示已经安装
				if(tempInfo.getVerName()!=null&&tempInfo.getVerName().compareTo(apkInfo.getVerName())<0){//有更新的版本
					setshakeBottomText(R.string.update);
					shakeBottomTv.setTag(R.string.update);
				}else{
					setshakeBottomText(R.string.open);
					shakeBottomTv.setTag(R.string.open);
				}
			}
	   }
    }

	private void setshakeBottomText(int res) {
		if(!isShaking && isResult){
			shakeBottomTv.setText(res);
		}
	}

	@Override
	public void netWorkConnect() {
		if(getActivity()==null|| getActivity().isFinishing())return;
		if (null != hotListAdapter && hotListAdapter.getCount()==0 && ((MainActivity)getActivity()).getCurrentPage()==2) {
			loadDiscoveryData();
		}
	}


	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		stopDownload();
		loadDiscoveryData();
	}
}
