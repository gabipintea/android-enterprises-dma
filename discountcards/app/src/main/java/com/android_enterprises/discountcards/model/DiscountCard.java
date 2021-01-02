package com.android_enterprises.discountcards.model;

import java.util.Date;

public class DiscountCard {
    long shopId = -1;
    String userEmail = "";
    float discount = 1;
    String expiryDate = "";

    public DiscountCard(long shopId, String email, float discount, String expiryDate) {
        this.shopId = shopId;
        this.userEmail = email;
        this.discount = discount;
        this.expiryDate = expiryDate;
    }

    public long getShopId() { return shopId; }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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

        Shop shop = new Shop(this.getShopId(), "Kaufland", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
        return shop;
    }
}
