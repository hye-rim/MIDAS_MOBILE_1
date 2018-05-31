package com.midas.mobile.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ReservationData {
//    private int type;
//    private int mNumber;

    @SerializedName("reservationId")
    @Expose
    private int reservationId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("contents")
    @Expose
    private ArrayList<ReservationContentsData> contents;

    @SerializedName("totalPrice")
    @Expose
    private int totalPrice;


    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ReservationContentsData> getContents() {
        return contents;
    }

    public void setContents(ArrayList<ReservationContentsData> contents) {
        this.contents = contents;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
