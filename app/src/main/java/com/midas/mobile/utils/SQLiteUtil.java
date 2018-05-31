package com.midas.mobile.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteUtil extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Cafe.db";

    public SQLiteUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table `cart` (" +
                "pk INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(30)," + // 이름
                "count INTEGER," + // 개수
                "price INTEGER," + // 가격
                "img VARCHAR(100)," + // 이미지
                "size VARCHAR(10));"; // 사이즈
        db.execSQL(query);
    }

    public Cursor select() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * from `cart`";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void insert(String name, int count, int price, String image, String size) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("count", count);
        values.put("price", price);
        values.put("img", image);
        values.put("size", size);

        long newRowId = db.insert("cart", null, values);
        db.close();
    }

    public void delete(int pk) {
        SQLiteDatabase db = getWritableDatabase();

        String select = "pk = ?";
        String[] selections = {String.valueOf(pk)};
        long dbRow = db.delete("cart",select,selections);

        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();

        String deleteAll = "DELETE from `cart`";
        db.execSQL(deleteAll);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
