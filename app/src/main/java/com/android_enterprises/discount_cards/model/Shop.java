package com.android_enterprises.discount_cards.model;


public class Shop {
    long shopId;
    int shopLogo;
    String shopName = "";
    String address = "";
    shopType type = shopType.general;

    public Shop(long shopId, String shopName, String address, shopType type) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.address = address;
        this.type = shopType.general;
    }

    public Shop() {
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(int shopLogo) {
        this.shopLogo = shopLogo;
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

