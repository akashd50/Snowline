package com.greymatter.snowline.db_v2.containers;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.greymatter.snowline.db_v2.contracts.ShapesContract;

public class ShapeCursorContainer {
    private Cursor cursor;
    public ShapeCursorContainer(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getShapeLat() {
        return cursor.getString(cursor.getColumnIndexOrThrow(ShapesContract.ShapesEntry.C_SHAPE_PT_LAT));
    }

    public String getShapeLon() {
        return cursor.getString(cursor.getColumnIndexOrThrow(ShapesContract.ShapesEntry.C_SHAPE_PT_LON));
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(getShapeLat()), Double.parseDouble(getShapeLon()));
    }

    public boolean moveToNext() {
        return cursor.moveToNext();
    }
}
