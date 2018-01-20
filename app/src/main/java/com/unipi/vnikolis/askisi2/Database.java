package com.unipi.vnikolis.askisi2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Βαγγέλης on 4/1/2018.
 */

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Alepis.db";

    public static final String TABLE_NAME = "coordinates";

    public static final String NAME = "Name";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_LONGITUDE = "Longitude";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , Name Text ,Latitude TEXT, Longitude TEXT)");

        db.execSQL("INSERT INTO " + TABLE_NAME + " (Name, Latitude, Longitude) VALUES ('Marousi', '38.0549562', '23.807655') ");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (Name, Latitude, Longitude) VALUES ('Glyfada', '37.8650426', '23.7550447') ");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (Name, Latitude, Longitude) VALUES ('Spata', '37.960201', '23.9774369') ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String name, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(COL_LATITUDE, latitude);
        contentValues.put(COL_LONGITUDE, longitude);
        long i = db.insert(TABLE_NAME, null, contentValues);
        if (i == -1) {
            return false;
        } else return true;
    }



    public Cursor getData(String town){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " +TABLE_NAME+ " WHERE Name = '" +town+ "'", null);
        return res;

    }

}