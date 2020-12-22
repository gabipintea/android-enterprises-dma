package com.android_enterprises.discount_cards.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CardsTable {
    // Database table
    public static final String TABLE_TODO = "card";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_JSONCARD = "jsoncard";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODO
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_JSONCARD + " TEXT not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(CardsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(database);
    }
}
