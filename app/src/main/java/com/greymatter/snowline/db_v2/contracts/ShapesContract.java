package com.greymatter.snowline.db_v2.contracts;

import android.provider.BaseColumns;

public class ShapesContract {
    public static class ShapesEntry implements BaseColumns {
        public static final String TABLE_NAME = "shapes_entry";
        public static final String C_SHAPE_ID = "shape_id";
        public static final String C_SHAPE_PT_LAT = "shape_pt_lat";
        public static final String C_SHAPE_PT_LON = "shape_pt_lon";
        public static final String C_SHAPE_PT_SEQUENCE = "shape_pt_sequence";
    }
}
