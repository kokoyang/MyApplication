package com.zxly.market.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.market.R;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.entity.ApkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yangwencai on 2015/4/22.
 */
public class DiscorySearchAdapter extends BaseAdapter {


    private List<ApkInfo> list;

    public  DiscorySearchAdapter(List<ApkInfo> list){
        this.list = list;
    }

    public List<ApkInfo> getList() {
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
            int res = R.layout.item_list_catage_sub2nd;
            view = LayoutInflater.from(BaseApplication.getInstance()).inflate(res, null);
            holder.img = (ImageView) view.findViewById(R.id.sort_fragment_image_item);
            holder.name = (TextView) view.findViewById(R.id.sort_fragment_title_item);
            holder.vDivider = view.findViewById(R.id.vertical_divider);
            holder.hDivider = view.findViewById(R.id.horizontal_divider);
            view.setTag(holder);
        }else{
            holder = (Holder)view.getTag();
        }
        holder.img.setVisibility(View.GONE);
        int count = 3;
        if (i % count == 0) {
            holder.vDivider.setVisibility(View.INVISIBLE);
        } else {
            holder.vDivider.setVisibility(View.VISIBLE);
        }
//        if (((getCount() - 1) / count + 1) == ((i) / count + 1) ) {// 判读是否是最后一行
//            holder.hDivider.setVisibility(View.INVISIBLE);
//        } else {
//            holder.hDivider.setVisibility(View.VISIBLE);
//        }

        ApkInfo info = (ApkInfo) getItem(i);
        String name = info.getAppName();
        if(name.length()>4){
            name = name.substring(0,4);
        }
        holder.name.setText(name);
        return view;
    }

    class Holder{
        ImageView img;
        TextView name;
        View vDivider;
        View hDivider;
    }
}
