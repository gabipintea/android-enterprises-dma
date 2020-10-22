package com.android_enterprises.discount_cards.model;

import java.util.Date;

public class DiscountCard {
    Shop shop = new Shop();
    float discount = 1;
    Date expiryDate = new Date();

    public DiscountCard(Shop shop, float discount) {
        this.shop = shop;
        this.discount = discount;
    }

    public DiscountCard(Shop shop, float discount, Date expiryDate) {
        this.shop = shop;
        this.discount = discount;
        this.expiryDate = expiryDate;
    }

    public DiscountCard() {
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "shop=" + shop +
                ", discount=" + discount +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
