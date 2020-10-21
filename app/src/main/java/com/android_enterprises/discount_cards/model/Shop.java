package com.android_enterprises.discount_cards.model;


public class Shop {
    String shopName = "";
    String address = "";
    shopType type = shopType.general;

    public Shop(String shopName, String address, shopType type) {
        this.shopName = shopName;
        this.address = address;
        this.type = shopType.general;
    }

    public Shop() {
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public shopType getType() {
        return type;
    }

    public void setType(shopType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopName='" + shopName + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                '}';
    }
}

