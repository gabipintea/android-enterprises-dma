package com.android_enterprises.discountcards.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    String firstName = "";
    String lastName = "";
    Date birthday = new Date();
    String email = "";
    List<DiscountCard> DiscountCards = new ArrayList<DiscountCard>();

    public User(String firstName, String lastName, Date birthday, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<DiscountCard> getDiscountCards() {
        return DiscountCards;
    }

    public void setDiscountCards(List<DiscountCard> discountCards) {
        DiscountCards = discountCards;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", DiscountCards=" + DiscountCards +
                '}';
    }
}