package com.midas.mobile.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable {
    public final static String ADMIN = "ADMIN";
    public final static String USER = "USER";

    @SerializedName("id")
    @Expose
    public String index;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("reservationCnt")
    @Expose
    public String reservationCnt;

    @SerializedName("registeredDate")
    @Expose
    public String registeredDate;

    //Sign In
    public UserData(String email, String password, String type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }

    //Sign Up
    public UserData(String email,String name, String password,  String type) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.type = type;
    }

    public UserData(String index, String email, String password, String name, String type, String reservationCnt, String registeredDate) {
        this.index = index;
        this.email = email;
        this.password = password;
        this.name = name;
        this.type = type;
        this.reservationCnt = reservationCnt;
        this.registeredDate = registeredDate;
    }
}
