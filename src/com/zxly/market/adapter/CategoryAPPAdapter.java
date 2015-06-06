package com.zxly.market.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.activity.CategorySubActivity;
import com.zxly.market.entity.Category;
import com.zxly.market.entity.Category2nd;
import com.zxly.market.fragment.SortSubFragment;
import com.zxly.market.view.SortGridView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAPPAdapter extends ZXBaseAdapter<Category> implements AdapterView.OnItemClickListener {

	private DisplayImageOptions options;

	public CategoryAPPAdapter(Context context, List<Category> list){
		super(context, list);
		initOptions();
	}

	private void initOptions() {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().showStubImage(R.drawable.catogory_main_default_img)
				.imageScaleType(ImageScaleType.EXACTLY).showImageForEmptyUri(R.drawable.catogory_main_default_img).cacheInMemory();
		builder.cacheOnDisc();
		options = builder.build();
	}
	public void addList(List<Category> list){
		getList().clear();
		add(list);
	}

	@Override
	public int itemLayoutRes() {
		return R.layout.item_list_catage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent,ViewHolder holder) {
		Category category = (Category)getItem(position);
		ImageView img = holder.obtainView(convertView,R.id.sort_fragment_image_item);
		TextView text = holder.obtainView(convertView, R.id.sort_fragment_title_item);
		ImageLoader.getInstance().displayImage(category.getIconUrl(),img,options);
		text.setText(category.getClassName());
		SortGridView gridView = holder.obtainView(convertView, R.id.sort_fragment_list_item);
		gridView.setOnItemClickListener(this);
		gridView.setNumColumns(position==0?3:2);
		GridAdapter  adapter = new GridAdapter(position,category);
		gridView.setAdapter(adapter);
		return convertView;
	}


	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
		List<Category2nd> mList = ((GridAdapter)adapterView.getAdapter()).getList();
		Category2nd necessary = mList.get(pos);
		Intent intent = new Intent(context, CategorySubActivity.class);
		intent.putParcelableArrayListExtra(SortSubFragment.EXTRA_TITLES,reorderArrayList(mList, pos));
		context.startActivity(intent);

	}

	/**
	 * 数组列表重新排序
	 */
	public ArrayList<Category2nd> reorderArrayList(List<Category2nd> list, int index) {
		List<Category2nd> head = list.subList(0, index);
		List<Category2nd> tail = list.subList(index, list.size());
		ArrayList<Category2nd> tmp = new ArrayList<Category2nd>();
		tmp.addAll(tail);
		tmp.addAll(head);
		return tmp;
	}

	class GridAdapter extends BaseAdapter {

		private Category category;
		private List<Category2nd> list;
		private int pos = 0;

		GridAdapter(int position,Category category){
			this.category = category;
			this.list = category.getNodeList();
			this.pos = position;
		}

		public List<Category2nd> getList() {
			return list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int i) {
			return list.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			Holder holder;
			if(null == view){
				holder = new Holder();
				int res = (pos==0)? R.layout.item_list_catage_sub:R.layout.item_list_catage_sub2nd;
				view = LayoutInflater.from(BaseApplication.getInstance()).inflate(res, null);
				holder.img = (ImageView) view.findViewById(R.id.sort_fragment_image_item);
				holder.name = (TextView) view.findViewById(R.id.sort_fragment_title_item);
				holder.vDivider = view.findViewById(R.id.vertical_divider);
				holder.hDivider = view.findViewById(R.id.horizontal_divider);
				view.setTag(holder);
			}else{
				holder = (Holder)view.getTag();
			}

			int count = pos==0?3:2;
				if (i % count == 0) {
					holder.vDivider.setVisibility(View.INVISIBLE);
				} else {
					holder.vDivider.setVisibility(View.VISIBLE);
				}
				if (((getCount() - 1) / count + 1) == ((i) / count + 1) && count==3) {// 判读是否是最后一行
					holder.hDivider.setVisibility(View.INVISIBLE);
				} else {
					holder.hDivider.setVisibility(View.VISIBLE);
				}
//				if (i == getCount() - 1) {
//					holder.hDivider.setVisibility(View.INVISIBLE);
//				}


			Category2nd category = (Category2nd) getItem(i);
			ImageLoader.getInstance().displayImage(category.getIconUrl(),holder.img,options);
			holder.name.setText(category.getClassName());
			return view;
		}

		class Holder{
			ImageView img;
			TextView name;
			View vDivider;
			View hDivider;
		}

	}

}
