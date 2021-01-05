package com.android_enterprises.discountcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.model.shopType;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "DiscountCards.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table Users(firstName TEXT, lastName TEXT, email TEXT PRIMARY KEY, birthday DATE)");
        db.execSQL("create Table Shops(shopId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, shopName TEXT, shopType INTEGER, logoURL TEXT)");
        db.execSQL("create Table Cards(shopId INTEGER REFERENCES Shops (shopId), userEmail TEXT REFERENCES Users (email), discount INTEGER, expiryDate DATE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Users");
        db.execSQL("drop Table if exists Shops");
        db.execSQL("drop Table if exists Cards");

        onCreate(db);
    }

    public Boolean registerUser(String fName, String lName, String mail, String bDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", fName);
        contentValues.put("lastName", lName);
        contentValues.put("email", mail);
        contentValues.put("birthday", String.valueOf(bDay));
        long result = db.insert("Users", null, contentValues);
        if (result == -1)
            return false;
        else return true;
    }

    public Boolean editUser(String fName, String lName, String mail, String bDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", fName);
        contentValues.put("lastName", lName);
        contentValues.put("email", mail);
        contentValues.put("birthday", String.valueOf(bDay));

        Cursor cursor = db.rawQuery("Select * from Users where email = ?", new String[]{mail});

        if (cursor.getCount() > 0) {

            long result = db.update("Users", contentValues, "email=?", new String[]{mail});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }

    public Boolean deleteUser(String mail) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Users where email = ?", new String[]{mail});

        if (cursor.getCount() > 0) {
            long result = db.delete("Users", "email=?", new String[]{mail});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }

//    public Cursor getUsers() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery("Select * from Users", null);
//
//        return cursor;
//    }

    public ArrayList<User> getUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select rowid, * from Users", null);

        ArrayList<User> users = new ArrayList<User>();

        cursor.moveToFirst();
        while(cursor.moveToNext()){

            int id = cursor.getInt(0);
            String fName = cursor.getString(1);
            String lName = cursor.getString(2);
            String email = cursor.getString(3);
            String birthday = cursor.getString(4);

            users.add(new User(id, fName, lName, birthday, email));
        }

        return users;
    }


    public User getUser(String mail) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Users where email=?", new String[]{mail});

        String fName = cursor.getString(0);
        String lName = cursor.getString(1);
        String birthday = cursor.getString(3);

        User user = new User(0, fName, lName, birthday, mail);

        return user;
    }

    public Boolean registerShop(String shName, shopType type, String logo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shopName", shName);
        contentValues.put("shopType", String.valueOf(type));
        contentValues.put("logoURL", logo);
        long result = db.insert("Shops", null, contentValues);
        if (result == -1)
            return false;
        else return true;
    }

    public Boolean editShop(long id, String shName, shopType type, String logo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shopName", shName);
        contentValues.put("shopType", String.valueOf(type));
        contentValues.put("logoURL", logo);

        Cursor cursor = db.rawQuery("Select * from Shops where shopId = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {

            long result = db.update("Shops", contentValues, "shopId=?", new String[]{String.valueOf(id)});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }

    public Boolean deleteShop(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Shops where shopId = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() > 0) {
            long result = db.delete("Shops", "shopId=?", new String[]{String.valueOf(id)});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }

//    public Cursor getShops() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery("Select * from Shops", null);
//
//        return cursor;
//    }

    public ArrayList<Shop> getShops() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Shops", null);

        ArrayList<Shop> shops = new ArrayList<Shop>();

        cursor.moveToFirst();
        while(cursor.moveToNext()){

            long shopId = cursor.getInt(0);
            String shopName = cursor.getString(1);
            shopType type = shopType.fromId(cursor.getInt(2));
            String logoUrl = cursor.getString(3);

            shops.add(new Shop(shopId, shopName, type, logoUrl));
        }

        return shops;
    }


    public Shop getShop(long shopId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Shops where shopId=?", new String[]{String.valueOf(shopId)});

        cursor.moveToFirst();
        String shopName = cursor.getString(1);
        shopType type = shopType.fromId(cursor.getInt(2));
        String logoURL = cursor.getString(3);

        Shop shop = new Shop(shopId, shopName, type, logoURL);

        return shop;
    }

    public Boolean createCard(long shopId, String userEmail, int discount, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shopId", shopId);
        contentValues.put("userEmail", userEmail);
        contentValues.put("discount", discount);
        contentValues.put("expiryDate", expiryDate);
        long result = db.insert("Cards", null, contentValues);
        if (result == -1)
            return false;
        else return true;
    }

    public Boolean editCard(long shopId, String userEmail, int discount, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("discount", discount);
        contentValues.put("expiryDate", expiryDate);

        Cursor cursor = db.rawQuery("Select * from Cards where shopId = ? AND userEmail = ?", new String[]{String.valueOf(shopId), userEmail});

        if (cursor.getCount() > 0) {

            long result = db.update("Cards", contentValues, "shopId = ? AND userEmail = ?", new String[]{String.valueOf(shopId), userEmail});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }

    public Boolean deleteCard(long shopId, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Cards where shopId = ? AND userEmail = ?", new String[]{String.valueOf(shopId), userEmail});

        if (cursor.getCount() > 0) {
            long result = db.delete("Cards", "shopId = ? AND userEmail = ?", new String[]{String.valueOf(shopId), userEmail});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }

    public Cursor getCards() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Cards", null);

        return cursor;
    }

    public DiscountCard getCard(long shopId, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Cards where shopId=? AND userEmail=?", new String[]{String.valueOf(shopId), userEmail});

        int discount = cursor.getInt(2);
        String expiryDate = cursor.getString(3);

        DiscountCard card = new DiscountCard(shopId, userEmail, discount, expiryDate);

        return card;
    }


    //TODO IDEM for getShops and getUsers
    public ArrayList<DiscountCard> getUserCards(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Cards where userEmail=?", new String[]{userEmail});
        cursor.moveToFirst();

        ArrayList<DiscountCard> cards = new ArrayList<DiscountCard>();

        cursor.moveToFirst();
        while(cursor.moveToNext()){
            long shopId = cursor.getInt(0);
            int discount = cursor.getInt(2);
            String expiryDate = cursor.getString(3);

            cards.add(new DiscountCard(shopId, userEmail, discount, expiryDate));
        }

        return cards;
    }

}
