/**    
 * @FileName: UpgradeAppActivity.java  
 * @Package:com.zxly.market.activity  
 * @Description: TODO 
 * @author: Administrator   
 * @date:2015-4-13 下午1:29:21  
 * @version V1.0    
 */
package com.zxly.market.activity;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.zxly.market.R;
import com.zxly.market.adapter.ListAppIgnoreAdapter;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;

/** 
 * @ClassName: UpgradeAppActivity  
 * @Description: 已经忽略应用升级界面 
 * @author: fengruyi 
 * @date:2015-4-13 下午1:29:21   
 */
public class IgnoredAppActivity extends BaseActivity {
	private ListView mListView;
	private View mEmptyView;
	private List<ApkInfo> list;
    private DbUtils db;
	@Override
	public int getContentViewId() {
		return R.layout.activity_ignore;
	}

	
	@Override
	public void initViewAndData() {
		setBackTitle(R.string.ignore_app);
		mListView  = obtainView(R.id.lv_ignore_app);
		mEmptyView = obtainView(R.id.tv_empty);
		mListView.setEmptyView(mEmptyView);	
		loadIgnoreAppList();
	}

    /**
     * 
     * @Title: loadIgnoreAppList  
     * @Description: 加载已经忽略app列表 
     * @param  
     * @return void 
     * @throws
     */
	private void loadIgnoreAppList(){
		db = DbUtils.create(this);
		try {
			list = db.findAll(Selector.from(ApkInfo.class));
			ListAppIgnoreAdapter adapter = new ListAppIgnoreAdapter(this, list,db);
			mListView.setAdapter(adapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ApkInfo info = list.get(arg2);
					 Intent intent = new Intent(IgnoredAppActivity.this,AppDetailActivity.class);
					 intent.putExtra(Constant.APK_DETAIL, info.getDetailUrl());
					 startActivity(intent);
					
				}
			});
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	

}
