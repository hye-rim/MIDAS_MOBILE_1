package com.midas.mobile.utils;

import okhttp3.MediaType;

/**
 * Created by hyerim on 2018. 5. 23....
 */
public class Keys {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private String token = "";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private Keys() { }

    private static class SingleTon {
        private static final Keys instance = new Keys();
    }

    public static Keys getInstance() {
        return SingleTon.instance;
    }
}
