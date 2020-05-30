package com.greymatter.snowline.db_v2.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.greymatter.snowline.db_v2.DBServices;
import com.greymatter.snowline.db_v2.containers.ShapeCursorContainer;

import static com.greymatter.snowline.db_v2.contracts.ShapesContract.ShapesEntry.*;

public class ShapesDBHelper {
    private static String[] columnNames = {BaseColumns._ID, C_SHAPE_ID, C_SHAPE_PT_LAT, C_SHAPE_PT_LON, C_SHAPE_PT_SEQUENCE};
    public ShapesDBHelper() {
    }

    public void insert(int id, String[] toInsert) {
        ContentValues cv = new ContentValues();
        cv.put(columnNames[0], id);
        for(int i=1;i<columnNames.length;i++) {
            cv.put(columnNames[i], toInsert[i-1]);
        }
        DBServices.getWritableDB().insert(TABLE_NAME, null, cv);
    }

    public ShapeCursorContainer get(String shapeID) {
        String[] projection = columnNames;

        String selection = C_SHAPE_ID + " = ?";
        String[] selectionArgs = { shapeID };

        //String sortOrder = COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = DBServices.getReadableDB().query(
                TABLE_NAME,             // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                null           // The sort order
        );
        return new ShapeCursorContainer(cursor);
    }


    public void deleteWhereIdGreaterThan(int id) {
        String selection = "_id >= ?";
        String[] selectionArgs = {id+""};
        DBServices.getWritableDB().delete(TABLE_NAME, selection, selectionArgs);
    }
}
