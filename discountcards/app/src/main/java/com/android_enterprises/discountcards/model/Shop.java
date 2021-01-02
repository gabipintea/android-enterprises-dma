package com.android_enterprises.discountcards.model;


public class Shop {
    long shopId;
    String shopName = "";
    shopType type = shopType.general;
    String logoUrl = "";


    public Shop() {
    }

    public Shop(long shopId, String shopName, shopType type, String logoUrl) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.type = type;
        this.logoUrl = logoUrl;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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
                ", type=" + type +
                '}';
    }
}

