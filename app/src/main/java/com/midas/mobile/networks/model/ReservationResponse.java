package com.midas.mobile.networks.model;

import com.google.gson.annotations.SerializedName;
import com.midas.mobile.entity.ReservationData;

import java.util.ArrayList;

public class ReservationResponse {
    @SerializedName("isSuccess")
    private Boolean isSuccess;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<ReservationData> result;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ReservationData> getResult() {
        return result;
    }
}
