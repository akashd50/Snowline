package com.greymatter.snowline.db_v2;

import com.greymatter.snowline.db_v2.contracts.GlobalDataContract.*;
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

    public static final String SQL_CREATE_GLOBAL_DATA_TABLE =
            "CREATE TABLE " + GlobalDataEntry.TABLE_NAME + " (" +
                    GlobalDataEntry._ID + " INTEGER PRIMARY KEY," +
                    GlobalDataEntry.C_TRIPS_TABLE_ID + " TEXT," +
                    GlobalDataEntry.C_SHAPES_TABLE_ID + " TEXT," +
                    GlobalDataEntry.C_TRIPS_TABLE_LOADED + " TEXT," +
                    GlobalDataEntry.C_SHAPES_TABLE_LOADED + " TEXT)";

    public static final String SQL_DELETE_GLOBAL_DATA_TABLE =
            "DROP TABLE IF EXISTS " + GlobalDataEntry.TABLE_NAME;
}
