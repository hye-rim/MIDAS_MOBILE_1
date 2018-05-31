package com.midas.mobile.networks.model;

import com.google.gson.annotations.SerializedName;
import com.midas.mobile.entity.PurchaseData;

import java.util.ArrayList;

/**
 * Created by hyerim on 2018. 5. 27....
 */
public class PurchaseResponse {
    @SerializedName("isSuccess")
    private Boolean isSuccess;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<PurchaseData> result;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<PurchaseData> getResult() {
        return result;
    }
}
