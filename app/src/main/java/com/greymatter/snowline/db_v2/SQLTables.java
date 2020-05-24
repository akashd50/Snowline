package com.greymatter.snowline.db_v2;

import com.greymatter.snowline.db_v2.contracts.TripsContract.*;
import com.greymatter.snowline.db_v2.contracts.ShapesContract.*;

public class SQLTables {
    public static final String SQL_CREATE_TRIPS_TABLE =
            "CREATE TABLE " + TripsEntry.TABLE_NAME + " (" +
                    TripsEntry._ID + " INTEGER PRIMARY KEY," +
                    TripsEntry.C_ROUTE_ID + " TEXT," +
                    TripsEntry.C_SERVICE_ID + " TEXT," +
                    TripsEntry.C_TRIP_ID + " TEXT," +
                    TripsEntry.C_TRIP_HEADSIGN + " TEXT," +
                    TripsEntry.C_DIRECTION_ID + " TEXT," +
                    TripsEntry.C_BLOCK_ID + " TEXT," +
                    TripsEntry.C_SHAPE_ID + " TEXT," +
                    TripsEntry.C_WHEELCHAIR_ACCESSIBLE + " TEXT)";

    public static final String SQL_DELETE_TRIPS_TABLE =
            "DROP TABLE IF EXISTS " + TripsEntry.TABLE_NAME;

    public static final String SQL_CREATE_SHAPES_TABLE =
            "CREATE TABLE " + ShapesEntry.TABLE_NAME + " (" +
                    ShapesEntry._ID + " INTEGER PRIMARY KEY," +
                    ShapesEntry.C_SHAPE_ID + " TEXT," +
                    ShapesEntry.C_SHAPE_PT_LAT + " TEXT," +
                    ShapesEntry.C_SHAPE_PT_LON + " TEXT," +
                    ShapesEntry.C_SHAPE_PT_SEQUENCE + " TEXT)";

    public static final String SQL_DELETE_SHAPES_TABLE =
            "DROP TABLE IF EXISTS " + TripsEntry.TABLE_NAME;
}
