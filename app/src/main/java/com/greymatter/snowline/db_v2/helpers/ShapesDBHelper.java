package com.greymatter.snowline.db_v2.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import static com.greymatter.snowline.db_v2.contracts.ShapesContract.ShapesEntry.*;

public class ShapesDBHelper {
    private DBOpenHelper databaseHelper;
    private static String[] columnNames = {BaseColumns._ID, C_SHAPE_ID, C_SHAPE_PT_LAT, C_SHAPE_PT_LON, C_SHAPE_PT_SEQUENCE};
    private int id;
    public ShapesDBHelper(DBOpenHelper dbRef) {
        this.databaseHelper = dbRef;
        id=0;
    }

    public void insert(String[] toInsert) {
        ContentValues cv = new ContentValues();
        cv.put(columnNames[0], id++);
        for(int i=1;i<columnNames.length;i++) {
            cv.put(columnNames[i], toInsert[i-1]);
        }
        databaseHelper.getWritableDatabase().insert(TABLE_NAME, null, cv);
    }

    public Cursor get(String shapeID) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = columnNames;

        String selection = C_SHAPE_ID + " = ?";
        String[] selectionArgs = { shapeID };

        //String sortOrder = COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,             // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null           // The sort order
        );
        return cursor;
    }
}
