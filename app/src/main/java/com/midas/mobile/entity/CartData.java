package com.midas.mobile.entity;

public class CartData {
    private int number;
    private String name;
    private int count;
    private int price;
    private String size;
    private String image;

    public CartData(int number, String name, int count, int price, String size, String image) {
        this.number = number;
        this.name = name;
        this.count = count;
        this.price = price;
        this.size = size;
        this.image = image;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public String getImage() {
        return image;
    }
}
