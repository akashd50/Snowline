package com.greymatter.snowline.db_v2.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.greymatter.snowline.Objects.GlobalData;
import com.greymatter.snowline.db_v2.DBServices;
import com.greymatter.snowline.db_v2.containers.GlobalDataCursorContainer;

import static com.greymatter.snowline.db_v2.contracts.GlobalDataContract.GlobalDataEntry.*;

public class GlobalDataDBHelper {
    private static String[] columnNames = {BaseColumns._ID, C_TRIPS_TABLE_ID, C_SHAPES_TABLE_ID, C_TRIPS_TABLE_LOADED, C_SHAPES_TABLE_LOADED};
    private static int id = 9999;
    public GlobalDataDBHelper() { }

    public void insert(String[] toInsert) {
        ContentValues cv = new ContentValues();
        cv.put(columnNames[0], id);
        for(int i=1;i<columnNames.length;i++) {
            cv.put(columnNames[i], toInsert[i-1]);
        }
        DBServices.getWritableDB().insert(TABLE_NAME, null, cv);
    }

    public GlobalDataCursorContainer getData() {
        String[] projection = columnNames;

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { id+"" };

        Cursor cursor = DBServices.getReadableDB().query(
                TABLE_NAME,             // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null           // The sort order
        );
        return new GlobalDataCursorContainer(cursor);
    }

    public void update(GlobalData data) {
        ContentValues values = new ContentValues();
        values.put(C_TRIPS_TABLE_ID, data.getTripsTableId());
        values.put(C_SHAPES_TABLE_ID, data.getShapesTableId());
        values.put(C_TRIPS_TABLE_LOADED, ""+data.isTripsTableLoaded());
        values.put(C_SHAPES_TABLE_LOADED, ""+data.isShapesTableLoaded());

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { id+"" };

        int count = DBServices.getWritableDB().update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
