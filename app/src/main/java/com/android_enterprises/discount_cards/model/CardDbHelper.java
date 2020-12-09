package com.android_enterprises.discount_cards.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class CardDbHelper extends SQLiteOpenHelper {

    public static final String DEBUG_TAG = "CardsDatabase";
    private static final String DATABASE_NAME = "CardsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public CardDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CardsTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        CardsTable.onUpgrade(database, oldVersion, newVersion);
    }

    public void insert(JSONObject card) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CardsTable.COLUMN_JSONCARD, card.toString());
        Long value = db.insert(CardsTable.TABLE_TODO, null, cv);
        Log.d("DatabaseOperation", value.toString());

    }
}
