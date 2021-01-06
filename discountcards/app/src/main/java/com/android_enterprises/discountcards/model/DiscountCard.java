package com.android_enterprises.discountcards.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android_enterprises.discountcards.DBHelper;

import java.util.Date;

public class DiscountCard implements Parcelable {
    long shopId = -1;
    String userEmail = "";
    int discount = 1;
    String expiryDate = "";

    public DiscountCard(long shopId, String email, int discount, String expiryDate) {
        this.shopId = shopId;
        this.userEmail = email;
        this.discount = discount;
        this.expiryDate = expiryDate;
    }

    protected DiscountCard(Parcel in) {
        shopId = in.readLong();
        userEmail = in.readString();
        discount = in.readInt();
        expiryDate = in.readString();
    }

    public static final Creator<DiscountCard> CREATOR = new Creator<DiscountCard>() {
        @Override
        public DiscountCard createFromParcel(Parcel in) {
            return new DiscountCard(in);
        }

        @Override
        public DiscountCard[] newArray(int size) {
            return new DiscountCard[size];
        }
    };

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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(shopId);
        parcel.writeString(userEmail);
        parcel.writeInt(discount);
        parcel.writeString(expiryDate);
    }
}
