package com.greymatter.snowline.db_v2.containers;

import android.database.Cursor;

import com.greymatter.snowline.db_v2.contracts.TripsContract;

public class TripCursorContainer {
    private Cursor cursor;
    public TripCursorContainer(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getRouteID() {
        return cursor.getString(cursor.getColumnIndexOrThrow(TripsContract.TripsEntry.C_ROUTE_ID));
    }

    public String getShapeID() {
        return cursor.getString(cursor.getColumnIndexOrThrow(TripsContract.TripsEntry.C_SHAPE_ID));
    }

    public boolean moveToNext() {
        return cursor.moveToNext();
    }
}
