package com.greymatter.snowline.db_v2.containers;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.greymatter.snowline.Objects.GlobalData;
import com.greymatter.snowline.db_v2.contracts.GlobalDataContract.*;

public class GlobalDataCursorContainer {
    private Cursor cursor;
    public GlobalDataCursorContainer(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getCurrentTripsId() {
        return cursor.getString(cursor.getColumnIndexOrThrow(GlobalDataEntry.C_TRIPS_TABLE_ID));
    }

    public String getCurrentShapesId() {
        return cursor.getString(cursor.getColumnIndexOrThrow(GlobalDataEntry.C_SHAPES_TABLE_ID));
    }

    public String getTripsTableLoaded() {
        return cursor.getString(cursor.getColumnIndexOrThrow(GlobalDataEntry.C_TRIPS_TABLE_LOADED));
    }

    public String getShapesTableLoaded() {
        return cursor.getString(cursor.getColumnIndexOrThrow(GlobalDataEntry.C_SHAPES_TABLE_LOADED));
    }

    public GlobalData getObject() {
        moveToNext();
        GlobalData data = new GlobalData();
        data.setTripsTableId(Integer.parseInt(getCurrentTripsId()));
        data.setShapesTableId(Integer.parseInt(getCurrentShapesId()));
        data.setTripsTableLoaded(Boolean.getBoolean(getTripsTableLoaded()));
        data.setShapesTableLoaded(Boolean.getBoolean(getShapesTableLoaded()));
        return data;
    }

    public boolean moveToNext() {
        return cursor.moveToNext();
    }
}
