package com.midas.mobile.utils;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {
    @SerializedName("isSuccess")
    private Boolean isSuccess;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private String result;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }
}
