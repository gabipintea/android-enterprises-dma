package com.example.g1094_seminar3;

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
}
