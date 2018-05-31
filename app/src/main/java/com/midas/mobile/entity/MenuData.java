package com.midas.mobile.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class MenuData implements Serializable {
    private int type;
    private int mNumber;

    @SerializedName("menuName")
    @Expose
    private String mName;

    @SerializedName("menuPrice")
    @Expose
    private String mCost;

    @SerializedName("menuContents")
    @Expose
    private String mExplain;

    @SerializedName("menuImage")
    @Expose
    private String mImageUrl;

    @SerializedName("registeredDate")
    @Expose
    private String mRegisteredDate;

    @SerializedName("updatedDate")
    @Expose
    private String mUpdatedDate;

    public int getType() {
        return type;
    }

    public int getmNumber() {
        return mNumber;
    }

    public String getmName() {
        return mName;
    }

    public String getmCost() {
        return mCost;
    }

    public String getmExplain() {
        return mExplain;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmRegisteredDate() {
        return mRegisteredDate;
    }

    public String getmUpdatedDate() {
        return mUpdatedDate;
    }

    public MenuData(int type, int number, String name, String cost, String explain, String url) {
        this.type = type;
        this.mNumber = number;
        this.mName = name;
        this.mCost = cost;
        this.mExplain = explain;
        this.mImageUrl = url;
    }

    //create Menu API
    public MenuData(String mName, String mCost, String mExplain, String mImageUrl) {
        this.mName = mName;
        this.mCost = mCost;
        this.mExplain = mExplain;
        this.mImageUrl = mImageUrl;
    }

    //get Menu List API
    public MenuData(String mName, String mCost, String mExplain, String mImageUrl, String mRegisteredDate, String mUpdatedDate) {
        this.mName = mName;
        this.mCost = mCost;
        this.mExplain = mExplain;
        this.mImageUrl = mImageUrl;
        this.mRegisteredDate = mRegisteredDate;
        this.mUpdatedDate = mUpdatedDate;
    }
}
