package com.midas.mobile.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 5. 27....
 */
public class PurchaseData {

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("totalPrice")
    @Expose
    public int totalPrice;

    public PurchaseData(String date, int totalPrice) {
        this.date = date;
        this.totalPrice = totalPrice;
    }


}
