package com.zxly.market.entity;

/**
 * Created by Yangwencai on 2015/4/30.
 */
public class TopicInfo {

    /**
     * {"specImgUrl":"","specUrl":"","classCode":"","className":"全民购物狂欢节"}
     */

    private String specImgUrl;
    private String specUrl;
    private String classCode;
    private String className;


    public String getSpecImgUrl() {
        return specImgUrl;
    }

    public void setSpecImgUrl(String specImgUrl) {
        this.specImgUrl = specImgUrl;
    }

    public String getSpecUrl() {
        return specUrl;
    }

    public void setSpecUrl(String specUrl) {
        this.specUrl = specUrl;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
