package com.midas.mobile.service_customer.model;

public class ReservationChartModel {
    String month;
    int value;

    public ReservationChartModel(String month, int value) {
        this.month = month;
        this.value = value;
    }

    public String getMonth() {
        return month;
    }

    public int getValue() {
        return value;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
