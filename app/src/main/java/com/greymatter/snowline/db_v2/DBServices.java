package com.greymatter.snowline.db_v2;

import android.content.Context;
import androidx.room.Room;

import com.greymatter.snowline.db_v2.helpers.DBOpenHelper;

public class DBServices {
    private static DBOpenHelper dbOpenHelper;
    public static void initDB(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public static DBOpenHelper getDbOpenHelper() {
        return dbOpenHelper;
    }
}
