package com.greymatter.snowline.db_v2.contracts;

import android.provider.BaseColumns;

public class TripsContract {
    public static class TripsEntry implements BaseColumns {
        public static final String TABLE_NAME = "trips_entry";
        public static final String C_ROUTE_ID = "route_id";
        public static final String C_SERVICE_ID = "service_id";
        public static final String C_TRIP_ID = "trip_id";
        public static final String C_TRIP_HEADSIGN = "trip_headsign_id";
        public static final String C_DIRECTION_ID = "direction_id";
        public static final String C_BLOCK_ID = "block_id";
        public static final String C_SHAPE_ID = "shape_id";
        public static final String C_WHEELCHAIR_ACCESSIBLE = "wheelchair_accessible";
    }
}
