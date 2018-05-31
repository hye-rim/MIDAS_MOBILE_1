package com.midas.mobile.service_customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderContents {

    @SerializedName("menuName")
    @Expose
    String menuName;

    @SerializedName("menuSize")
    @Expose
    String menuSize;

    @SerializedName("quatity")
    @Expose
    int quatity;

    public OrderContents(String menuName, String menuSize, int quatity) {
        this.menuName = menuName;
        this.menuSize = menuSize;
        this.quatity = quatity;
    }
}
