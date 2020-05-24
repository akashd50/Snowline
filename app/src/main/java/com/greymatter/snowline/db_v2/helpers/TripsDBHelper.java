package com.greymatter.snowline.db_v2.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.greymatter.snowline.db_v2.contracts.TripsContract;

import static com.greymatter.snowline.db_v2.contracts.TripsContract.TripsEntry.*;

public class TripsDBHelper {
    private DBOpenHelper databaseHelper;
    private static String[] columnNames = {BaseColumns._ID, C_ROUTE_ID, C_BLOCK_ID, C_SERVICE_ID, C_TRIP_ID,
            C_TRIP_HEADSIGN, C_DIRECTION_ID, C_BLOCK_ID, C_SHAPE_ID, C_WHEELCHAIR_ACCESSIBLE};
    private int id;
    public TripsDBHelper(DBOpenHelper dbRef) {
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

    public Cursor get(String routeID) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = columnNames;

        String selection = C_ROUTE_ID + " = ?";
        String[] selectionArgs = { routeID };

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
