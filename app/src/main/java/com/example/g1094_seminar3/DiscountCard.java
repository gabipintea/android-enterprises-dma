package com.example.g1094_seminar3;

public class DiscountCard {
    Shop shop = new Shop();
    float discount = 1;

    public DiscountCard(Shop shop, float discount) {
        this.shop = shop;
        this.discount = discount;
    }

    public DiscountCard() {
    }
}
