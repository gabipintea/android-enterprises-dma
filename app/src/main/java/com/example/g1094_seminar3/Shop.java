package com.example.g1094_seminar3;

public class Shop {
    String shopName = "";
    String address = "";
    enum shopType {food, clothing, general};
    shopType type;

    public Shop(String shopName, String address, shopType type) {
        this.shopName = shopName;
        this.address = address;
        this.type = shopType.general;
    }

    public Shop() {
    }
}
