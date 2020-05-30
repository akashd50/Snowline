package com.greymatter.snowline.db_v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;

import com.greymatter.snowline.db_v2.helpers.DBOpenHelper;

public class DBServices {
    private static DBOpenHelper dbOpenHelper;
    private static SQLiteDatabase writableDB, readableDB;
    public static void initDB(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public static DBOpenHelper getDbOpenHelper() {
        return dbOpenHelper;
    }

    public static SQLiteDatabase getReadableDB() {
        if(readableDB==null) {
            readableDB = dbOpenHelper.getReadableDatabase();
        }
        return readableDB;
    }
    public static SQLiteDatabase getWritableDB() {
        if(writableDB==null) {
            writableDB = dbOpenHelper.getWritableDatabase();
        }
        return writableDB;
    }

    public static void close() {
        dbOpenHelper.close();
    }

}
