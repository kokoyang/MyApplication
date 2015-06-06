package com.zxly.market.model;


import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.Category;
import com.zxly.market.entity.Data;
import com.zxly.market.entity.WebInfo;
import com.zxly.market.fragment.BaseFragment;
import com.zxly.market.fragment.DiscoveryFragment;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.utils.AppUtil;
import com.zxly.market.utils.CommonUtils;
import com.zxly.market.utils.JsonUtils;
import com.zxly.market.utils.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yangwencai on 2015/4/14.
 */
public class DiscoveryFragmentModel {

    private BaseFragment discoryFragment;
    private ExecutorService executor;
    private static String TAG = "SortFragmentModel";

    public DiscoveryFragmentModel(BaseFragment fragment){
        executor = Executors.newCachedThreadPool();
        discoryFragment = fragment;
    }

    public void loadDiscoveryData() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                loadDiscoveryDataList("yao1yao", 1, 15, DiscoveryFragment.SHAKE_DATA);//摇一摇数据
                loadDiscoveryDataList("all_search", 1, 9, DiscoveryFragment.GRID_DATA);//大家都在搜
                loadDiscoveryDataList("renqi_hOT",
                        1, Constant.ONE_PAGE_COUNT, DiscoveryFragment.LIST_DATA1);//人气游戏

                loadWebViewData(DiscoveryFragment.WEB_DATA);
            }
        });
    }


    public void loadDiscoveryHotGameData(final int currPage) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                loadDiscoveryDataList("renqi_hOT",
                        currPage, Constant.ONE_PAGE_COUNT, DiscoveryFragment.LIST_DATA2);//人气游戏
            }
        });
    }
            /**
             * 子类应用列不数据
             */
            private  void loadDiscoveryDataList(String mClassId,int currPage, int pageSize,final int type) {
                //classCode=yao1yao&coid=3&type=0&currPage=1&pageSize=5&imei=
                RequestParams params = new RequestParams();
                params.addBodyParameter("coid", "3");
                params.addBodyParameter("classCode", mClassId);
                params.addBodyParameter("type", "0");
                params.addBodyParameter("currPage",  currPage+"");
                params.addBodyParameter("pageSize",  pageSize+"");
                params.addBodyParameter("imei", "");
                HttpHelper.send(HttpRequest.HttpMethod.POST, Constant.CATAGORY_SUB_URL, params, new HttpHelper.HttpCallBack() {

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        Message message = new Message();
                        message.what = Constant.MESSAGE_FAILED;
                        discoryFragment.handleInfoMessage(message);
                        //discoryFragment.handler.obtainMessage(Constant.MESSAGE_FAILED, type, 0).sendToTarget();
                    }

                    @Override
                    public void onSuccess(String result) {
                        Logger.d(TAG, "result=" + result);
                        Data data = JsonUtils.dataFromJson(result);
                        List<ApkInfo> list = null;
                        if(data!=null){
                            list = data.getApkList();
                        }
                        Message message = new Message();
                        if (!CommonUtils.isEmptyList(list)) {
                            Logger.d(TAG, "shake size=" + list.size());
                            if(type==DiscoveryFragment.SHAKE_DATA){
                                for(int i=0;i<list.size();i++){
                                    ApkInfo info = list.get(i);
                                    if(AppUtil.isAppInstalled(BaseApplication.getInstance().getApplicationContext(),info.getPackName())){
                                        list.remove(i);
                                        i--;
                                    }
                                }
                            }
                            message.what = Constant.MESSAGE_SUCCESS;
                            message.arg1 = type;
                            message.arg2 = data.getRecordCount();
                            message.obj = list;
                            //discoryFragment.handler.obtainMessage(Constant.MESSAGE_SUCCESS, type, data.getRecordCount(), list).sendToTarget();
                        } else {
                            message.what = Constant.MESSAGE_NODATD;
                            message.arg1 = type;
                            //discoryFragment.handler.obtainMessage(Constant.MESSAGE_NODATD, type, 0).sendToTarget();
                        }
                        discoryFragment.handleInfoMessage(message);
                    }
                });

            }


    // http://apiqa.18guanjia.com/appmarket/GetAdvertList?coid=3&imei=1234567&code=FX_H5&token=y8t0a9ru6z76w4m8v5dzz2
    private void loadWebViewData(final int type) {
        {
            RequestParams params = new RequestParams();
            params.addBodyParameter("coid", "3");
            params.addBodyParameter("code", "FX_H5");
            HttpHelper.send(HttpRequest.HttpMethod.POST, Constant.DIS_WEBVIEW_URL, params, new HttpHelper.HttpCallBack() {

                @Override
                public void onFailure(HttpException e, String msg) {
                    Message message = new Message();
                    message.what = Constant.MESSAGE_FAILED;
                    discoryFragment.handleInfoMessage(message);
                    //discoryFragment.handler.obtainMessage(Constant.MESSAGE_FAILED, type, 0).sendToTarget();
                }

                @Override
                public void onSuccess(String result) {
                    Logger.d(TAG, "web result=" + result);
                    List<WebInfo> webs = JsonUtils.listFromJson(result, new TypeToken<List<WebInfo>>() {});
                    Message message = new Message();
                    if (!CommonUtils.isEmptyList(webs) && webs.get(0)!=null && CommonUtils.validate(webs.get(0).getUrl())) {
                        message.what = Constant.MESSAGE_SUCCESS;
                        message.arg1 = type;
                        message.obj = webs.get(0).getUrl();
                        //discoryFragment.handler.obtainMessage(Constant.MESSAGE_SUCCESS, type, 0, webs.get(0).getUrl()).sendToTarget();
                    } else {
                        message.what = Constant.MESSAGE_NODATD;
                        message.arg1 = type;
                        //discoryFragment.handler.obtainMessage(Constant.MESSAGE_NODATD, type, 0).sendToTarget();
                    }
                    discoryFragment.handleInfoMessage(message);
                }
            });

        }
    }


}
