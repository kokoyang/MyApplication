package com.zxly.market.entity;

import java.util.List;


public class Category {

    /**
     * 一级分类名
     */
    private String className;
    private String iconUrl;
    private List<Category2nd> nodeList;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<Category2nd> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Category2nd> nodeList) {
        this.nodeList = nodeList;
    }
}
