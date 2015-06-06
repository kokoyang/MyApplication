package com.zxly.market.model;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zxly.market.activity.TopicAppActivity;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.Category;
import com.zxly.market.entity.Data;
import com.zxly.market.entity.TopicInfo;
import com.zxly.market.fragment.BaseFragment;
import com.zxly.market.http.HttpHelper;
import com.zxly.market.utils.CommonUtils;
import com.zxly.market.utils.JsonUtils;
import com.zxly.market.utils.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yangwencai on 2015/4/14.
 */
public class TopicModel {

    private TopicAppActivity activity;
    private ExecutorService executor;
    private static String TAG = "SortFragmentModel";

    public TopicModel(TopicAppActivity fragment){
        executor = Executors.newCachedThreadPool();
        activity = fragment;
    }
//    public void loadTopicData (){
//        //http://apiqa.18guanjia.com/AppMarket/GetSpecialList?currPage=1&pageSize=10&coid=3&imei=1234567&token=y8t0a9ru6z76w4m8v5dzz2
//        executor.submit(new Runnable() {
//            @Override
//            public void run() {
//
//                RequestParams params = new RequestParams();
//                params.addBodyParameter("coid", "3");
//                HttpHelper.send(HttpRequest.HttpMethod.POST, Constant.TOPIC_APP_LIST_URL, params, new HttpHelper.HttpCallBack() {
//                    @Override
//                    public void onFailure(HttpException e, String msg) {
//                        Logger.d(TAG, "topic result = failure");
//                        activity.handler.obtainMessage(Constant.MESSAGE_FAILED).sendToTarget();
//                    }
//
//                    @Override
//                    public void onSuccess(String result) {
//                        Logger.d(TAG, "topic result=" + result);
//                        List<TopicInfo> list = (List<TopicInfo>) JsonUtils.listFromJson(result, new TypeToken<List<TopicInfo>>() {});
//                        Logger.d(TAG, "topic result=" + result);
//                        if (!CommonUtils.isEmptyList(list)) {
//                            Logger.d(TAG, "topic result size =" + list.size());
//                            activity.handler.obtainMessage(Constant.MESSAGE_SUCCESS, list).sendToTarget();
//                        } else {
//                            Logger.d(TAG, "topic result =" +(list==null));
//                            activity.handler.obtainMessage(Constant.MESSAGE_NODATD).sendToTarget();
//                        }
//                    }
//                });
//
//            }
//        });
//    }


}
