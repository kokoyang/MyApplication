package com.zxly.market.utils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.BodyParamsEntity;
import com.zxly.market.activity.BaseApplication;
import com.zxly.market.constans.Constant;
import com.zxly.market.entity.ApkInfo;
import com.zxly.market.entity.Data;
import com.zxly.market.entity.DownLoadTaskInfo;
import com.zxly.market.http.HttpHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yangwencai on 2015/4/28.
 */
public class StatisticsManager {

    private static StatisticsManager instance;
    private ExecutorService executor;
    private static final int core = 3;

    private StatisticsManager(){
        executor = Executors.newFixedThreadPool(core);
    }

    public static StatisticsManager getInstance(){
        if(instance==null){
            instance = new StatisticsManager();
        }
        return instance;
    }

    /**
     * http://apiqa.18guanjia.com/Stat/WapStatistics?
     * PackName=&ClassCode=search&Coid=3&Type=0&SystemVer=1.0&NCoid=&Imei=&Channel=&Source=local&ApkName=&ApkSize=&token=y8t0a9ru6z76w4m8v5dzz2
     */
    public void statistics(final DownLoadTaskInfo info){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addBodyParameter("PackName", info.getPackageName());
                params.addBodyParameter("ClassCode", info.getClassCode());
                params.addBodyParameter("Coid", "3");
                params.addBodyParameter("Type", info.getType()+"");
                params.addBodyParameter("SystemVer", BaseApplication.mVersionName);
                params.addBodyParameter("NCoid", "1");
                params.addBodyParameter("Imei",  BaseApplication.imei);
                params.addBodyParameter("Channel",  BaseApplication.coid);
                params.addBodyParameter("PackType", info.getSource()+"");
                params.addBodyParameter("ApkName", info.getFileName());
                long length = info.getFileLength();
                if(length<500){
                    length = length * 1024* 1024;
                }
                params.addBodyParameter("ApkSize", length + "");
                params.addBodyParameter("token",Constant.APP_TOKEN);

                HttpHelper.statSend(HttpRequest.HttpMethod.POST, Constant.STATISTICS_URL, params, new HttpHelper.HttpCallBack() {

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        Logger.d("stat", "failure stat url result = " + msg);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Logger.d("stat", "success stat url result = " + result);
                    }
                });
            }
        });
    }




}
