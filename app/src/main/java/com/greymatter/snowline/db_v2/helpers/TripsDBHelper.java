package com.greymatter.snowline.db_v2.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.greymatter.snowline.db_v2.DBServices;
import com.greymatter.snowline.db_v2.containers.TripCursorContainer;
import com.greymatter.snowline.db_v2.contracts.TripsContract;

import static com.greymatter.snowline.db_v2.contracts.TripsContract.TripsEntry.*;

public class TripsDBHelper {
    private static String[] columnNames = {BaseColumns._ID, C_ROUTE_ID, C_SERVICE_ID, C_TRIP_ID,
            C_TRIP_HEADSIGN, C_DIRECTION_ID, C_BLOCK_ID, C_SHAPE_ID, C_WHEELCHAIR_ACCESSIBLE};
    public TripsDBHelper() {
    }

    public void insert(int id, String[] toInsert) {
        ContentValues cv = new ContentValues();
        cv.put(columnNames[0], id);
        for(int i=1;i<columnNames.length;i++) {
            cv.put(columnNames[i], toInsert[i-1]);
        }
        DBServices.getWritableDB().insert(TABLE_NAME, null, cv);
    }

    public TripCursorContainer get(ContentValues params) {
        String[] projection = columnNames;
        String selection = "";
        String[] selectionArgs = new String[params.size()];

        int argsIndex = 0;
        for(String s : columnNames) {
            if(params.get(s) != null) {
                if(argsIndex==selectionArgs.length-1) {
                    selection += s + " = ?";
                }else {
                    selection += s + " = ? AND ";
                }

                selectionArgs[argsIndex++] = (String)params.get(s);
            }
        }

        Cursor cursor = DBServices.getReadableDB().query(
                TABLE_NAME,             // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null           // The sort order
        );
        return new TripCursorContainer(cursor);
    }

    public void deleteWhereIdGreaterThan(int id) {
        String selection = "_id >= ?";
        String[] selectionArgs = {id+""};
        DBServices.getWritableDB().delete(TABLE_NAME, selection, selectionArgs);
    }

}
