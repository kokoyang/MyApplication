package com.zxly.market.model;

import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.Category;
import com.zxly.market.entity.Data;
import com.zxly.market.fragment.BaseFragment;
import com.zxly.market.fragment.SortFragment;
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
public class SortFragmentModel {

    private BaseFragment sortFragment;
    private ExecutorService executor;
    private static String TAG = "SortFragmentModel";

    public SortFragmentModel(BaseFragment fragment){
        executor = Executors.newCachedThreadPool();
        sortFragment = fragment;
    }
    public void loadAppData (){
        Logger.d(TAG, "result=" + Thread.currentThread().getId());
        executor.submit(new Runnable() {
            @Override
            public void run() {

                RequestParams params = new RequestParams();
                params.addBodyParameter("coid", "3");
                params.addBodyParameter("imei", "");
                HttpHelper.send(HttpRequest.HttpMethod.POST, Constant.CATAGORY_URL, params, new HttpHelper.HttpCallBack() {
                    @Override
                    public void onFailure(HttpException e, String msg) {
                        Message message = new Message();
                        message.what = Constant.MESSAGE_FAILED;
                        sortFragment.handleInfoMessage(message);
                        //sortFragment.handler.obtainMessage(Constant.MESSAGE_FAILED).sendToTarget();
                    }

                    @Override
                    public void onSuccess(String result) {
                        Logger.d(TAG, "result=" + result);
                        List<Category> list = (List<Category>) JsonUtils.listFromJson(result, new TypeToken<List<Category>>() {
                        });
                        Message message = new Message();
                        if (!CommonUtils.isEmptyList(list)) {
                            message.what = Constant.MESSAGE_SUCCESS;
                            message.obj = list;
                            //sortFragment.handler.obtainMessage(Constant.MESSAGE_SUCCESS, list).sendToTarget();
                        } else {
                            message.what = Constant.MESSAGE_NODATD;
                            //sortFragment.handler.obtainMessage(Constant.MESSAGE_NODATD).sendToTarget();
                        }
                        sortFragment.handleInfoMessage(message);
                    }
                });

            }
        });
    }

        /**
         * 子类应用列不数据
         * @param mClassId
         */
        public void loadGroupList(final String mClassId,final int currentPage) {
                {
                    executor.submit(new Runnable() {
                                @Override
                                public void run() {
                                        //classCode=yao1yao&coid=3&type=0&currPage=1&pageSize=5&imei=
                                        RequestParams params = new RequestParams();
                                        params.addBodyParameter("coid","3");
                                        params.addBodyParameter("classCode",mClassId);
                                        params.addBodyParameter("type","0");
                                        params.addBodyParameter("currPage",currentPage+"");
                                        params.addBodyParameter("pageSize",Constant.ONE_PAGE_COUNT+"");
                                        params.addBodyParameter("imei", "");
                                        HttpHelper.send(HttpRequest.HttpMethod.POST, Constant.CATAGORY_SUB_URL, params, new HttpHelper.HttpCallBack() {
                                                    @Override
                                                    public void onFailure(HttpException e, String msg) {
                                                        Message message = new Message();
                                                        message.what = Constant.MESSAGE_FAILED;
                                                        sortFragment.handleInfoMessage(message);
                                                        //sortFragment.handler.obtainMessage(Constant.MESSAGE_FAILED).sendToTarget();
                                                    }

                                                    @Override
                                                    public void onSuccess(String result) {
                                                        Logger.d(TAG, "result=" + result);
                                                        Data data =JsonUtils.dataFromJson(result);
                                                        Logger.d(TAG,"data="+data.toString());
                                                        //List<ApkInfo> list = (List<ApkInfo>) JsonUtils.listFromJson(result, new TypeToken<List<ApkInfo>>() {});
                                                        List<ApkInfo> list = data.getApkList();
                                                        Message message = new Message();
                                                        if (!CommonUtils.isEmptyList(list)) {
                                                            message.what = Constant.MESSAGE_SUCCESS;
                                                            message.arg1 = data.getRecordCount();
                                                            message.obj = list;
                                                            //sortFragment.handler.obtainMessage(Constant.MESSAGE_SUCCESS, data.getRecordCount(),0,list).sendToTarget();
                                                        } else {
                                                            message.what = Constant.MESSAGE_NODATD;
                                                            //sortFragment.handler.obtainMessage(Constant.MESSAGE_NODATD).sendToTarget();
                                                        }
                                                        sortFragment.handleInfoMessage(message);
                                                    }
                                                });

                                }
                        });
                }
        }
}
