package com.midas.mobile.service_customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderRequestBody {
    @SerializedName("reservationTime")
    @Expose
    String time;

    @SerializedName("contents")
    List<OrderContents> contents;

    public OrderRequestBody(String time, List<OrderContents> contents) {
        this.time = time;
        this.contents = contents;

    }

    public String getTime() {
        return time;
    }

    public List<OrderContents> getContents() {
        return contents;
    }
}
