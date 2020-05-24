package com.greymatter.snowline.db_v2.contracts;

import android.provider.BaseColumns;

public class StopsContract {
    public static class StopsEntry implements BaseColumns {
        public static final String TABLE_NAME = "stops_entry";
        public static final String COLUMN_NAME_ = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
