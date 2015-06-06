package com.zxly.market.entity;

import java.util.List;

/**
 * Created by yangwencai on 2015/4/29.
 * web网页链接信息
 */
public class WebInfo {
    // {"imgUrl":"","type":2,"packName":null,"url":"http://www.baidu.com","h5Url":"http://www.baidu.com","spUrl":null,"classCode":null}

    private String  imgUrl;
    private int type;
    private String packName;
    private String url;
    private String h5Url;
    private String spUrl;
    private String classCode;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getSpUrl() {
        return spUrl;
    }

    public void setSpUrl(String spUrl) {
        this.spUrl = spUrl;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }


    @Override
    public String toString() {
        return "WebInfo{" +
                "imgUrl='" + imgUrl + '\'' +
                ", type=" + type +
                ", packName='" + packName + '\'' +
                ", url='" + url + '\'' +
                ", h5Url='" + h5Url + '\'' +
                ", spUrl='" + spUrl + '\'' +
                ", classCode='" + classCode + '\'' +
                '}';
    }
}

