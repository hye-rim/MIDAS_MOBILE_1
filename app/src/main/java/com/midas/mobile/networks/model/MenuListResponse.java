package com.midas.mobile.networks.model;

import com.google.gson.annotations.SerializedName;
import com.midas.mobile.entity.MenuData;

import java.util.List;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class MenuListResponse {
    @SerializedName("isSuccess")
    private Boolean isSuccess;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private List<MenuData> result;

    public MenuListResponse(Boolean isSuccess, String message, List<MenuData> result) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.result = result;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public List<MenuData> getResult() {
        return result;
    }
}
