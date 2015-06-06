package com.zxly.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class Category2nd implements  Parcelable{

    private String className;
    private String iconUrl;
    private String classCode;


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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public static final Parcelable.Creator<Category2nd> CREATOR = new Parcelable.Creator<Category2nd>() {
        public Category2nd createFromParcel(Parcel source) {
            Category2nd mNecessary = new Category2nd();
            mNecessary.className = source.readString();
            mNecessary.classCode = source.readString();
            return mNecessary;
        }
        public Category2nd[] newArray(int size) {
            return new Category2nd[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int arg1) {
        parcel.writeString(className);
        parcel.writeString(classCode);
    }
}
