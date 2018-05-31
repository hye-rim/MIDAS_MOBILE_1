package com.midas.mobile.networks.model;

import com.google.gson.annotations.SerializedName;

public class FCMResponse {

    @SerializedName("fcm")
    String fcm;

    public FCMResponse(String fcm) {
        this.fcm = fcm;
    }

    public String getFcm() {
        return fcm;
    }
}
