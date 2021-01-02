package com.android_enterprises.discountcards.model;

import java.util.Date;

public class DiscountCard {
    long shopId = -1;
    //Shop shop = new Shop();
    float discount = 1;
    String expiryDate = "";

    public DiscountCard(long shopId, float discount, String expiryDate) {
        this.shopId = shopId;
        this.discount = discount;
        this.expiryDate = expiryDate;
    }

//    public DiscountCard(Shop shop, float discount) {
//        this.shop = shop;
//        this.discount = discount;
//    }
//
//    public DiscountCard(Shop shop, float discount, Date expiryDate) {
//        this.shop = shop;
//        this.discount = discount;
//        this.expiryDate = expiryDate;
//    }


//    public Shop getShop() {
//        return shop;
//    }

    public long getShopId() { return shopId; }

//    public void setShop(Shop shop) {
//        this.shop = shop;
//    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String toString() {
        return "DiscountCard{" +
                "shop=" + shopId +
                ", discount=" + discount +
                ", expiryDate=" + expiryDate +
                '}';
    }

    public Shop getShop() {
        //TODO Extract shop from database based on shopId

        Shop shop = new Shop(this.getShopId(), "Kaufland", "Dorobanti", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
        return shop;
    }
}
