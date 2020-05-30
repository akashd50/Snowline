package com.greymatter.snowline.db_v2.contracts;

import android.provider.BaseColumns;

public class GlobalDataContract {
    public static class GlobalDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "global_data_entry";
        public static final String C_TRIPS_TABLE_ID = "trips_table_id";
        public static final String C_SHAPES_TABLE_ID = "shapes_table_id";
        public static final String C_TRIPS_TABLE_LOADED = "trips_table_loaded";
        public static final String C_SHAPES_TABLE_LOADED = "shapes_table_loaded";
    }
}
