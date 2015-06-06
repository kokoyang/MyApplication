package com.zxly.market.entity;

import com.zxly.market.utils.Logger;

import java.util.List;

/**
 * Created by yangwencai on 2015/4/14.
 * 总分类
 */
public class Data {

    //{"apkList":[],"ClassDetails":null,"currPage":2,"pageSize":20,"recordCount":20,"countPage":1,"statusText":"","status":300}
    private List<ApkInfo> apkList;
    private String statusText;
    private int status;
    private int currPage;
    private int pageSize;
    private int recordCount;
    private int countPage;

    public List<ApkInfo> getApkList() {
        return apkList;
    }

    public void setApkList(List<ApkInfo> apkList) {
        this.apkList = apkList;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    @Override
    public String toString() {
        return "Data{" +
                "apkList=" + apkList +
                ", statusText='" + statusText + '\'' +
                ", status=" + status +
                ", currPage=" + currPage +
                ", pageSize=" + pageSize +
                ", recordCount=" + recordCount +
                ", countPage=" + countPage +
                '}';
    }
}

