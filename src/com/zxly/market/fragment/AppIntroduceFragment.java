package com.zxly.market.fragment;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.activity.AppDetailActivity;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.ShowPicsActivity;
import com.zxly.market.adapter.RecomandGridViewAppAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.AppDetalData;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.utils.ImageLoaderUtil;
import com.zxly.market.utils.ViewUtil;
import com.zxly.market.view.RelativeLayoutForGridView;
import com.zxly.market.view.RelativeLayoutForGridView.OnGridviewItemClickListener;

/**
 * 应用详情里的介绍页面 
 * @author fengruyi
 *
 */
public class AppIntroduceFragment extends BaseFragment implements OnClickListener{
	private final String TAG ="AppIntroduceFragment";
	private RelativeLayoutForGridView mGridView;//推荐应用列表
	private LinearLayout mPicsContainer;//图片画廊
	private ScrollView mRootview;
	private ImageView mBtnExpand;
	private View mViewExpandHead;
	private TextView mIntroduceContent;
	DisplayImageOptions options = new DisplayImageOptions.Builder() 
	   .showImageOnLoading(R.drawable.icon_detail_default)//加载中时显示的图片
	   .showImageForEmptyUri(R.drawable.icon_detail_default)//设置图片Uri为空或是错误的时候显示的图片  
	   .showImageOnFail(R.drawable.icon_detail_default)  //设置图片加载/解码过程中错误时候显示的图片
	   .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	   .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	   .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
	   .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	   .build();//构建完成 

	DownLoadTaskInfo info;
	private RecomandGridViewAppAdapter adapter;
	@Override
	public void handleInfoMessage(Message msg) {
	}

	@Override
	public int getContentViewId() {
		return R.layout.fragment_app_introduce;
	}
	
	public ScrollView getScrollView(){
		return mRootview;
	}
	@Override
	public void initViewAndData() {
		mGridView = obtainView(R.id.gv_recomand);
		mPicsContainer = obtainView(R.id.llt_pics);
		mRootview = obtainView(R.id.rootview);
		mBtnExpand = obtainView(R.id.ibtn_expand_);
		mBtnExpand.setSelected(false);
		mViewExpandHead = obtainView(R.id.rlt_1);
		mIntroduceContent = obtainView(R.id.tv_introduce_content);
		mRootview.smoothScrollTo(0, 0);
		ViewUtil.setOnClickListener(this, mBtnExpand,mIntroduceContent,mViewExpandHead);
		//RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) mGridView.getLayoutParams();
		//lp.bottomMargin =(int) (((AppDetailActivity)getActivity()).scollheight+BaseApplication.mHeightPixels*0.13);
		mGridView.setPadding(0, 0, 0, (int) (((AppDetailActivity)getActivity()).scollheight+BaseApplication.mHeightPixels*0.13));
	}
	
	/**
	 * 呈现相关应用列表
	 */
	private void showRelatedApp(final List<ApkInfo> list){
	    adapter = new RecomandGridViewAppAdapter(getActivity(), list);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnGridviewItemClickListener() {			
			public void onItemClicked(int position) {
				 ApkInfo info = (ApkInfo) adapter.getItem(position);
				 Intent intent = new Intent(getActivity(),AppDetailActivity.class);
				 intent.putExtra(Constant.APK_DETAIL, info.getDetailUrl());
				 intent.putExtra(Constant.APK_PACKAGE, info.getPackName());
				 startActivity(intent);
			}
		});
	}
	
	/**
	 * 显示应用图片
	 */
	private void showPics(final String urls[]){
		int picWidth = (int) (BaseApplication.mWidthPixels*0.334);
		int margin = (int) (BaseApplication.mWidthPixels*0.025);
		LinearLayout.LayoutParams lp = new LayoutParams(picWidth,(int) (picWidth*1.78));
		ImageView imageview;
		lp.setMargins(margin, margin ,0, margin);
		try {
			for(int i = 0 ;i<urls.length;i++){
				final int index = i;
				imageview = new ImageView(BaseApplication.getInstance()); 
				imageview.setLayoutParams(lp);
				imageview.setScaleType(ScaleType.FIT_XY);
				ImageLoaderUtil.Load(urls[i], imageview,options);
				mPicsContainer.addView(imageview);
				imageview.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),ShowPicsActivity.class);
						intent.putExtra("urls", urls);
						intent.putExtra("index", index);
						startActivity(intent);
					}
				});
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	public void refreshView(){
		mGridView.notifyDataChanged();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_expand_:
			expandIntroduceContent(v.isSelected());
			break;
		case R.id.tv_introduce_content:
			mBtnExpand.performClick();
			break;
		case R.id.rlt_1:
			mBtnExpand.performClick();
			break;
		default:
			break;
		}
	}
	/**
	 * 是否展开应用介绍内容
	 */
	private void expandIntroduceContent(boolean flag){
		if(!flag){
			mBtnExpand.setImageResource(R.drawable.icon_close);
			mIntroduceContent.setSingleLine(false);
		}else{
			mBtnExpand.setImageResource(R.drawable.icon_expand);
			mIntroduceContent.setSingleLine(true);
		}
		mBtnExpand.setSelected(!flag);
	}
	
	public void showApkDetail(AppDetalData data){
		try {
			String[] urls = data.getDetail().getDetailUrls().split(",");
			showPics(urls);
			showRelatedApp(data.getRelatedList());
			mIntroduceContent.setText(Html.fromHtml(data.getDetail().getContent()));
	
		} catch (Exception e) {
			// TODO: handle exception
		}

		
	}
}
