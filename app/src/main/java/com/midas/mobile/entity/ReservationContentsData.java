package com.midas.mobile.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReservationContentsData {
//    private int type;
//    private int mNumber;

    @SerializedName("menuName")
    @Expose
    private String menuName;

    @SerializedName("menuQuantity")
    @Expose
    private int menuQuantitiy;

    @SerializedName("menuSize")
    @Expose
    private String menuSize;

    @SerializedName("menuPrice")
    @Expose
    private int menuPrice;

    @SerializedName("menuImage")
    @Expose
    private String menuImage;

    @SerializedName("menuContents")
    @Expose
    private String menuContents;


    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuQuantitiy() {
        return menuQuantitiy;
    }

    public void setMenuQuantitiy(int menuQuantitiy) {
        this.menuQuantitiy = menuQuantitiy;
    }

    public String getMenuSize() {
        return menuSize;
    }

    public void setMenuSize(String menuSize) {
        this.menuSize = menuSize;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuContents() {
        return menuContents;
    }

    public void setMenuContents(String menuContents) {
        this.menuContents = menuContents;
    }
}
