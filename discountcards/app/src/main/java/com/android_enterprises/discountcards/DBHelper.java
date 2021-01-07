package com.android_enterprises.discountcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.User;
import com.android_enterprises.discountcards.model.shopType;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
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

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop Table if exists Users");
        db.execSQL("drop Table if exists Shops");
        db.execSQL("drop Table if exists Cards");
        db.execSQL("create Table Users(firstName TEXT, lastName TEXT, email TEXT PRIMARY KEY, birthday DATE)");
        db.execSQL("create Table Shops(shopId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, shopName TEXT, shopType INTEGER, logoURL TEXT)");
        db.execSQL("create Table Cards(shopId INTEGER REFERENCES Shops (shopId), userEmail TEXT REFERENCES Users (email), discount INTEGER, expiryDate DATE)");

    }

    // To be used in the MainActivity in order to start with a refreshed minimal populated DB
    public Boolean insertSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop Table if exists Users");
        db.execSQL("drop Table if exists Shops");
        db.execSQL("drop Table if exists Cards");
        db.execSQL("create Table Users(firstName TEXT, lastName TEXT, email TEXT PRIMARY KEY, birthday DATE)");
        db.execSQL("create Table Shops(shopId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, shopName TEXT, shopType INTEGER, logoURL TEXT)");
        db.execSQL("create Table Cards(shopId INTEGER REFERENCES Shops (shopId), userEmail TEXT REFERENCES Users (email), discount INTEGER, expiryDate DATE)");

        boolean insertedShops = insertSampleShops();
        boolean insertedUsers = insertSampleUsers();
        boolean insertedCards = insertSampleCards();

        return insertedShops && insertedUsers && insertedCards;
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

    //TODO call this method somewhere
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


    public ArrayList<User> getUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select rowid, * from Users", null);

        ArrayList<User> users = new ArrayList<User>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String fName = cursor.getString(1);
            String lName = cursor.getString(2);
            String email = cursor.getString(3);
            String birthday = cursor.getString(4);

            users.add(new User(id, fName, lName, birthday, email));
            while(cursor.moveToNext()){

                id = cursor.getInt(0);
                fName = cursor.getString(1);
                lName = cursor.getString(2);
                email = cursor.getString(3);
                birthday = cursor.getString(4);

                users.add(new User(id, fName, lName, birthday, email));
            }
        }
        return users;
    }

    public Boolean insertSampleUsers() {
        boolean deleted = deleteUser("john.doe@gmail.com");
        deleted = deleteUser("xi.cho@gmail.com");
        deleted = deleteUser("franck.stank@gmail.com");

        boolean registered = false;
        registered = registerUser("John", "Doe", "john.doe@gmail.com", "01/02/1996");
        registered = registerUser("Xi", "Cho", "xi.cho@gmail.com", "01/02/1998");
        registered = registerUser("Franck", "Stank", "franck.stank@gmail.com", "01/02/1999");

        return registered;
    }


    public User getUser(String mail) {
        SQLiteDatabase db = this.getWritableDatabase();
        User user = new User();

        Cursor cursor = db.rawQuery("Select * from Users where email=?", new String[]{mail});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            String fName = cursor.getString(0);
            String lName = cursor.getString(1);
            String birthday = cursor.getString(3);

            user = new User(0, fName, lName, birthday, mail);
        }


        return user;
    }

    public Boolean registerShop(String shName, int type, String logo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shopName", shName);
        contentValues.put("shopType", type);
        contentValues.put("logoURL", logo);
        long result = db.insert("Shops", null, contentValues);
        if (result == -1)
            return false;
        else return true;
    }

    //TODO call this method somewhere
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

//
//    public Boolean deleteShop(long id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery("Select * from Shops where shopId = ?", new String[]{String.valueOf(id)});
//
//        if (cursor.getCount() > 0) {
//            long result = db.delete("Shops", "shopId=?", new String[]{String.valueOf(id)});
//            if (result == -1)
//                return false;
//            else return true;
//        } else return false;
//    }

    public Boolean deleteShopByName(String shopName) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Shops where shopName = ?", new String[]{shopName});

        if (cursor.getCount() > 0) {
            long result = db.delete("Shops", "shopName=?", new String[]{shopName});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }

    public Boolean insertSampleShops() {
        boolean deleted = deleteShopByName("Lidl");
        deleted = deleteShopByName("Carrefour");
        deleted = deleteShopByName("Kaufland");

        boolean inserted = registerShop("Lidl", shopType.food.getId(),"https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");
        inserted = registerShop("Carrefour", shopType.food.getId(), "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Carrefour_logo_no_tag.svg/1024px-Carrefour_logo_no_tag.svg.png");
        inserted = registerShop("Kaufland", shopType.general.getId(), "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
        return inserted;
    }


    public ArrayList<Shop> getShops() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Shops", null);

        ArrayList<Shop> shops = new ArrayList<Shop>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long shopId = cursor.getInt(0);
            String shopName = cursor.getString(1);
            shopType type = shopType.fromId(cursor.getInt(2));
            String logoUrl = cursor.getString(3);

            shops.add(new Shop(shopId, shopName, type, logoUrl));
            while(cursor.moveToNext()){

                shopId = cursor.getInt(0);
                shopName = cursor.getString(1);
                type = shopType.fromId(cursor.getInt(2));
                logoUrl = cursor.getString(3);

                shops.add(new Shop(shopId, shopName, type, logoUrl));
            }
        }
        return shops;
    }

// Was needed for a special situation in development, in order to not mess up the database
//    public boolean deleteAllShopsButLetOne() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        long shopId=1;
//        boolean inserted = false;
//        boolean deleted = deleteCard(1, "john.doe@gmail.com");
//        deleted = deleteCard(1, "xi.cho@gmail.com");
//        deleted = deleteCard(1, "franck.stank@gmail.com");
//
//        db.execSQL("DELETE FROM Shops");
//
//        boolean registered = registerShop("Lidl", shopType.general, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");
//        if(registered) {
//            ArrayList<Shop> shops = new ArrayList<Shop>();
//            shops = getShops();
//
//            for(Shop shop : shops) {
//                shopId =  shop.getShopId();
//                Log.d(TAG, "The new shop ID: " + String.valueOf(shopId));
//                break;
//            }
//            inserted = createCard(shopId, "john.doe@gmail.com", 50, "20/20/2021");
//            inserted = createCard(shopId, "xi.cho@gmail.com", 12, "20/20/2025");
//            inserted = createCard(shopId, "franck.stank@gmail.com", 50, "20/20/2023");
//        }
//        return inserted;
//    }


    public Shop getShop(long shopId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Shop shop = new Shop();

        Cursor cursor = db.rawQuery("Select * from Shops where shopId=?", new String[]{String.valueOf(shopId)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String shopName = cursor.getString(1);
            shopType type = shopType.fromId(cursor.getInt(2));
            String logoURL = cursor.getString(3);

            shop = new Shop(shopId, shopName, type, logoURL);
        }

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

    public Boolean deleteCardByEmail(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from Cards where userEmail = ?", new String[]{userEmail});

        if (cursor.getCount() > 0) {
            long result = db.delete("Cards", "userEmail = ?", new String[]{userEmail});
            if (result == -1)
                return false;
            else return true;
        } else return false;
    }


    //TODO call this method somewhere
    public DiscountCard getCard(long shopId, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        DiscountCard card = new DiscountCard();

        Cursor cursor = db.rawQuery("Select * from Cards where shopId=? AND userEmail=?", new String[]{String.valueOf(shopId), userEmail});

        if( cursor.getCount() > 0 ) {
            int discount = cursor.getInt(2);
            String expiryDate = cursor.getString(3);

            card = new DiscountCard(shopId, userEmail, discount, expiryDate);
        }
        return card;
    }

    public ArrayList<DiscountCard> getCards() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiscountCard> cards = new ArrayList<DiscountCard>();

        Cursor cursor = db.rawQuery("Select * from Cards", null);
        if( cursor.getCount() > 0 ) {
            cursor.moveToFirst();


            long shopId = cursor.getInt(0);
            String userEmail = cursor.getString(1);
            int discount = cursor.getInt(2);
            String expiryDate = cursor.getString(3);

            cards.add(new DiscountCard(shopId, userEmail, discount, expiryDate));

            while(cursor.moveToNext()){
                shopId = cursor.getInt(0);
                userEmail = cursor.getString(1);
                discount = cursor.getInt(2);
                expiryDate = cursor.getString(3);

                cards.add(new DiscountCard(shopId, userEmail, discount, expiryDate));
            }
        }


        return cards;
    }

    public ArrayList<DiscountCard> getUserCards(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DiscountCard> cards = new ArrayList<DiscountCard>();

        Cursor cursor = db.rawQuery("Select * from Cards where userEmail=?", new String[]{userEmail});
        if( cursor.getCount() > 0 ) {
            cursor.moveToFirst();


            long shopId = cursor.getInt(0);
            int discount = cursor.getInt(2);
            String expiryDate = cursor.getString(3);

            cards.add(new DiscountCard(shopId, userEmail, discount, expiryDate));

            while(cursor.moveToNext()){
                shopId = cursor.getInt(0);
                discount = cursor.getInt(2);
                expiryDate = cursor.getString(3);

                cards.add(new DiscountCard(shopId, userEmail, discount, expiryDate));
            }
        }


        return cards;
    }

    public Boolean insertSampleCards() {
        long shopId = -1;
        boolean inserted = false;

        boolean deleted = deleteCardByEmail("john.doe@gmail.com");
        deleted = deleteCardByEmail("xi.cho@gmail.com");
        deleted = deleteCardByEmail("john.doe@gmail.com");

        ArrayList<Shop> shops = new ArrayList<Shop>();
        shops = getShops();

        if(shops.size() == 3) {
            inserted = createCard(shops.get(0).getShopId(), "john.doe@gmail.com", 50, "20/20/2021");
            inserted = createCard(shops.get(1).getShopId(), "xi.cho@gmail.com", 12, "20/20/2025");
            inserted = createCard(shops.get(2).getShopId(), "franck.stank@gmail.com", 50, "20/20/2023");
        }

        return inserted;
    }

}
